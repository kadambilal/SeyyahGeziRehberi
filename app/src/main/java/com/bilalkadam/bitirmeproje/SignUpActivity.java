package com.bilalkadam.bitirmeproje;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
public class SignUpActivity extends AppCompatActivity {
    EditText emailId, password,userName,userSurname;
    TextView tvSignIn,kaydolbtn;
    String userID;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.nameText);
        userSurname = findViewById(R.id.surnameText);
        emailId = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        kaydolbtn = findViewById(R.id.kayitButon);
        tvSignIn = findViewById(R.id.textView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) { // kullanıcı varsa boş gelmıyorsa ;
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            kaydolbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  String email = emailId.getText().toString();
                    // String pwd = password.getText().toString();
                    String email = emailId.getText().toString().trim();
                    String pwd = password.getText().toString().trim();
                    String name = userName.getText().toString().trim();
                    String surname = userSurname.getText().toString().trim();
                    if (email.isEmpty()) {
                        emailId.setError("Lütfen Mail Adresinizi Giriniz..");
                        emailId.requestFocus();
                    } else if (pwd.isEmpty()) {
                        password.setError("Lütfen Şifrenizi Giriniz..");
                        password.requestFocus();
                    } else if (email.isEmpty() && pwd.isEmpty()) {
                        Toast.makeText(SignUpActivity.this, "Mail ve Şifre Boş Girilemez..", Toast.LENGTH_LONG).show();
                    } else if (!email.isEmpty() && !pwd.isEmpty()) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Kayıt Başarısız", Toast.LENGTH_LONG).show();
                                } else {
                                    userID = firebaseAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("Name",name);
                                    user.put("Surname",surname);
                                    user.put("email",email);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(SignUpActivity.this,"Kaydınız Başarıyla Gerçekleştirilmiştir..",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    DocumentReference docReference = firebaseFirestore.collection("users").document(userID).collection("image").document( "image");
                                    Map<String,Object> newImage = new HashMap<>();
                                    newImage.put("userImage","https://firebasestorage.googleapis.com/v0/b/bitirme-aba6b.appspot.com/o/users%2Fprofile.jpg?alt=media&token=4a6e7fc5-b3e8-4104-a944-de3faff3f504");
                                    docReference.set(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG","onSuccess: user Profşle created "+userID);
                                        }
                                    });
                                    // Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Malesef Bilinmeyen Bir Hata Meydana Geldi..", Toast.LENGTH_LONG).show();
                    }
                }
            });
            tvSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
        }
    }
}