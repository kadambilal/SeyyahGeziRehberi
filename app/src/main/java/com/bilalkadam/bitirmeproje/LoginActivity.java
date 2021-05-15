package com.bilalkadam.bitirmeproje;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class LoginActivity extends AppCompatActivity {
    EditText emailId, password;
    TextView tvSignUp, girisYap;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener; //Firebase kimlik doğrulaması
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        girisYap = findViewById(R.id.girisYapBtn);
        tvSignUp = findViewById(R.id.textView);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        };
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Lütfen Mail Adresinizi Giriniz..");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Lütfen Şifrenizi Giriniz..");
                    password.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this,"Email ve Şifre Boş Girilemez..",Toast.LENGTH_LONG).show();
                }
                else if ( ! email.isEmpty() && !pwd.isEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Giriş Yapılamadı Tekrar deneyiniz..",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intToHome = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this,"Bilinmeyen Bir Hata Meydana Geldi..",Toast.LENGTH_LONG).show();
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}