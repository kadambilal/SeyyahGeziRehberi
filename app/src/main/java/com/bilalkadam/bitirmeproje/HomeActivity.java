package com.bilalkadam.bitirmeproje;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.Map;
public class HomeActivity extends AppCompatActivity implements HomeRecyclerAdapter.SehirListener {
    DrawerLayout drawerLayout;
    TextView drawerText;
    String userID,profileImage;
    ImageView drawerImages;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<String> sehirAdiFB;
    ArrayList<String> sehirResmiFB;
    HomeRecyclerAdapter homeRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerText = findViewById(R.id.drawerNameText);
        drawerImages = findViewById(R.id.drawerImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        sehirAdiFB = new ArrayList<>();
        sehirResmiFB = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getUid();
        getDataFromFirestore();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeRecyclerAdapter = new HomeRecyclerAdapter(sehirAdiFB,sehirResmiFB,this);
        recyclerView.setAdapter(homeRecyclerAdapter); // birbirlerine bağladık.
        DocumentReference docReference1 = firebaseFirestore.collection("users").document(userID).collection("image").document( "image");;
        docReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                profileImage = documentSnapshot.getString("userImage");
            }
        });
    }
    public void getDataFromFirestore(){
        CollectionReference collectionReference = firebaseFirestore.collection("sehir");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
            if (e!=null){
                Toast.makeText(HomeActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
            if (queryDocumentSnapshots != null){
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    Map<String,Object> data = snapshot.getData();
                    String sehirAdi = (String) data.get("sehirAdi");
                    String sehirResmi = (String) data.get("sehirResmi");
                    String sehirID = (String) data.get("sehirID");
                    sehirAdiFB.add(sehirAdi);
                    sehirResmiFB.add(sehirResmi);
                    homeRecyclerAdapter.notifyDataSetChanged(); // yenı veri geldiğini uyarıyoruz.
                }
            }
            }
        });
    }
    @Override
    public void OnSehirClick(int position) {
        Intent intent = new Intent(this,PlacesPageActivity.class);
        intent.putExtra("itemId", sehirAdiFB.get(position));
        startActivity(intent);
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