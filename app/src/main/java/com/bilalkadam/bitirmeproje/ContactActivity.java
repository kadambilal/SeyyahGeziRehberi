package com.bilalkadam.bitirmeproje;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class ContactActivity extends AppCompatActivity {
    TextView changeAppBar,oneriGonder,drawerText;
    DrawerLayout drawerLayout;
    ImageView drawerImages;
    EditText oneriTxt;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    String userID,email,name,profileImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        drawerText = findViewById(R.id.drawerNameText);
        drawerImages = findViewById(R.id.drawerImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        changeAppBar = findViewById(R.id.appbartxt);
        changeAppBar.setText("iletişim");
        oneriTxt= findViewById(R.id.oneriTxt);
        oneriGonder = findViewById(R.id.oneriGonder);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();
        DocumentReference docReference1 = firebaseFirestore.collection("users").document(userID).collection("image").document( "image");;
        docReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                profileImage = documentSnapshot.getString("userImage");
            }
        });
        DocumentReference docReference = firebaseFirestore.collection("users").document(userID);
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                email = documentSnapshot.getString("email");
                name = documentSnapshot.getString("Name");
            }
        });
        oneriGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oneri = oneriTxt.getText().toString();
                oneriTxt.setText("");
                //String UserID = firebaseAuth.getCurrentUser().getUid();
                String uniqueID = UUID.randomUUID().toString();
                documentReference = firebaseFirestore.collection("oneri").document(uniqueID);
                Map<String,Object> oneriler = new HashMap<>();
                oneriler.put("oneri",oneri);
                oneriler.put("mail",email);
                oneriler.put("name",name);
                // System.out.println(name);
                // System.out.println(userImage);
                documentReference.set(oneriler).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("basarılı");
                        Toast.makeText(ContactActivity.this,"GERİ BİLDİRİMİNİZ TARAFIMIZA İLETİLMİŞTİR.. TEŞEKKÜR EDERİZ..",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    public void ClickMenu(View view){
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                drawerText.setText(documentSnapshot.getString("Name")+" "+(documentSnapshot.getString("Surname")));
            }
        });
        Picasso.get().load(profileImage).into(drawerImages);
        //open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer Layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
    private static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //drawer acıksa kapat
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
    public void ClickNotes(View view){
        Intent intent = new Intent(this,NotesActivity.class);
        startActivity(intent);
    }
    public void ClickDashboard(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    public void ClickAboutUS(View view){
        Intent itoInfo = new Intent(this, ContactActivity.class);
        startActivity(itoInfo);
    }
    public void ClickLogout(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intToMain =  new Intent(this, LoginActivity.class);
        intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intToMain);
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
