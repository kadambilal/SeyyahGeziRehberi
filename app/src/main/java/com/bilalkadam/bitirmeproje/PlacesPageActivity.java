package com.bilalkadam.bitirmeproje;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
public class PlacesPageActivity extends AppCompatActivity implements PlacesRecyclerAdapter.PlacesListener {
    TextView appbarTxt,drawerText;
    DrawerLayout drawerLayout;
    String userID,id,profileImage;
    FirebaseAuth firebaseAuth;
    ArrayList<String> yerAdiFB;
    ArrayList<String> yerResmiFB;
    ArrayList<String> yerIDFB;
    FirebaseFirestore firebaseFirestore;
    PlacesRecyclerAdapter placesRecyclerAdapter;
    ImageView drawerImages;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        firebaseAuth = FirebaseAuth.getInstance();
        drawerText = findViewById(R.id.drawerNameText);
        drawerLayout = findViewById(R.id.drawer_layout);
        userID = firebaseAuth.getUid();
        appbarTxt = findViewById(R.id.appbartxt);
        yerAdiFB = new ArrayList<>();
        yerResmiFB = new ArrayList<>();
        yerIDFB = new ArrayList<>();
        drawerImages = findViewById(R.id.drawerImage);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getPlacesDataFromFirestore();
        //recyclerview tanımlama
        RecyclerView recyclerViewPlaces = findViewById(R.id.recycler_row_places);
        recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(this)); //nasıl olusturulacagını yazdık
        placesRecyclerAdapter = new PlacesRecyclerAdapter(yerAdiFB,yerResmiFB,this);
        recyclerViewPlaces.setAdapter(placesRecyclerAdapter); // birbirine bagladık
        id = getIntent().getExtras().getString("itemId");
        DocumentReference docReference1 = firebaseFirestore.collection("users").document(userID).collection("image").document( "image");;
        docReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                profileImage = documentSnapshot.getString("userImage");
            }
        });
        appbarTxt.setText(id);
    }
    public void getPlacesDataFromFirestore (){
        id = getIntent().getExtras().getString("itemId");
        CollectionReference collectionReference = firebaseFirestore.collection("sehir").document(id).collection(id); // değişecek salt sorgu yapılcak
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (queryDocumentSnapshots != null){
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> dataPlaces = snapshot.getData();
                        String yerAdi = (String) dataPlaces.get("yerAdi");
                        String yerResmi = (String) dataPlaces.get("yerResmi");
                        String yerID = (String) dataPlaces.get("yerID");
                        yerAdiFB.add(yerAdi);
                        yerResmiFB.add(yerResmi);
                        yerIDFB.add(yerID);
                        placesRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    @Override
    public void onPlacesClick(int position) {
        Intent intentToDescription = new Intent(PlacesPageActivity.this, DetailActivity.class);
        intentToDescription.putExtra("itemId", yerAdiFB.get(position));
        intentToDescription.putExtra("itemCollectionId",id);
        startActivity(intentToDescription);
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
        //close drawer Layout
        closeDrawer(drawerLayout);
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
        Intent itoInfo = new Intent(this, ProfileActivity.class);
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
