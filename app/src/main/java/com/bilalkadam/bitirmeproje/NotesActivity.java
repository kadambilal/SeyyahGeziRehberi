package com.bilalkadam.bitirmeproje;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class NotesActivity extends AppCompatActivity {
    TextView changeAppBarText,drawerText,notGonderBtn;
    DrawerLayout drawerLayout;
    ImageView drawerImages;
    String profileImage,userID;
    EditText noteTxt;
    Button noteGonderBtn;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    ArrayList<String> notesFromFB;
    NotesRecyclerAdapter notesRecyclerAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_notes);
        drawerText = findViewById(R.id.drawerNameText);
        drawerImages = findViewById(R.id.drawerImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        notesFromFB = new ArrayList<>();
        changeAppBarText = findViewById(R.id.appbartxt);
        changeAppBarText.setText("Not Defteri");
        userID = firebaseAuth.getUid();
        noteTxt = findViewById(R.id.noteTxt);
        notGonderBtn = findViewById(R.id.noteGonder);
        DocumentReference docReference1 = firebaseFirestore.collection("users").document(userID).collection("image").document( "image");;
        docReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                profileImage = documentSnapshot.getString("userImage");
            }
        });
        getNoteFromFirebase();
        RecyclerView recyclerViewNotes = findViewById(R.id.recyclerView_notes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerAdapter = new NotesRecyclerAdapter(notesFromFB);
        recyclerViewNotes.setAdapter(notesRecyclerAdapter);
        notGonderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String not = noteTxt.getText().toString();
                noteTxt.setText("");
                String uniqueID = UUID.randomUUID().toString();
                documentReference = firebaseFirestore.collection("users").document(userID).collection("notes").document(uniqueID);
                Map<String,Object> notes = new HashMap<>();
                notes.put("note",not);
                notesFromFB.clear();
                documentReference.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NotesActivity.this,"Notunuz Başarıyla Eklenmiştir..",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
   public void getNoteFromFirebase(){
       CollectionReference collectionReference = firebaseFirestore.collection("users").document(userID).collection("notes");
       collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
               for (DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){
                   Map<String,Object> notes = snapshot.getData();
                   String note = (String) notes.get("note");
                   System.out.println(note);
                   notesFromFB.add(note);
                   notesRecyclerAdapter.notifyDataSetChanged();
               }
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
        Intent intToMain =  new Intent(this, SignUpActivity.class);
        intToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intToMain);
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
