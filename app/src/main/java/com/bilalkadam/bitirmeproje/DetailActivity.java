package com.bilalkadam.bitirmeproje;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;
public class DetailActivity extends AppCompatActivity {
    TextView genelBakisBolum, yorumlarBolum, calismaSaatleriTxt, girisUcretitxt, genelBilgiTxt, adresTxt,yerAdiTxt, drawerText,yorumYapBtn;
    EditText yorumTxt;
    LinearLayout genelBakisLayout, yorumlarLayout;
    ScrollView scrollView, scrollView2;
    String profileImage,userID,yerId, id,userImage,name;
    DrawerLayout drawerLayout;
    ImageView drawerImages;
    List<SlideModel> slideModels = new ArrayList<>();
    ArrayList<String> commentFromFB;
    ArrayList<String> userNameFromFB;
    ArrayList<String> userImageFromFB;
    CommentRecyclerAdapter commentRecyclerAdapter;
    ImageSlider imageSlider;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        drawerText = findViewById(R.id.drawerNameText);
        drawerImages = findViewById(R.id.drawerImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        commentFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();
        userNameFromFB = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        yorumTxt = findViewById(R.id.yorum);
        yorumYapBtn = findViewById(R.id.yorumYap);
        genelBakisBolum = findViewById(R.id.genelBakis);
        yorumlarBolum = findViewById(R.id.yorumlar);
        calismaSaatleriTxt = findViewById(R.id.calismaSaatleri);
        girisUcretitxt = findViewById(R.id.girisUcreti);
        genelBilgiTxt = findViewById(R.id.genelBilgi);
        adresTxt = findViewById(R.id.adres);
        yerAdiTxt = findViewById(R.id.appbartxt);
        scrollView = findViewById(R.id.scrollView);
        scrollView2 = findViewById(R.id.scrollView2);
        yorumlarLayout = findViewById(R.id.yorumlarLayout);
        genelBakisLayout = findViewById(R.id.genelBakisLayout);
        imageSlider = findViewById(R.id.slider);
        imageSlider.setImageList(slideModels,true);
        userID = firebaseAuth.getUid();
        id = getIntent().getExtras().getString("itemCollectionId");
        yerId = getIntent().getExtras().getString("itemId");
        getDescription();
        getComment();
        //recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerViewComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerAdapter = new CommentRecyclerAdapter(commentFromFB,userNameFromFB,userImageFromFB);
        recyclerView.setAdapter(commentRecyclerAdapter);

        DocumentReference docReference = firebaseFirestore.collection("users").document(userID);
        docReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                 name = documentSnapshot.getString("Name");
            }
        });
        DocumentReference docReference1 = firebaseFirestore.collection("users").document(userID).collection("image").document( "image");;
        docReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
               userImage = documentSnapshot.getString("userImage");
            }
        });
        yorumYapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yorum = yorumTxt.getText().toString();
                yorumTxt.setText("");
               //String UserID = firebaseAuth.getCurrentUser().getUid();
                String uniqueID = UUID.randomUUID().toString();
                DocumentReference documentReference = firebaseFirestore.collection("sehir").document(id).collection(id).document(yerId).collection("Comment").document(uniqueID);
                Map<String,Object> comment = new HashMap<>();
                comment.put("comment",yorum);
                comment.put("name",name);
                comment.put("userImage",userImage);
               // System.out.println(name);
                // System.out.println(userImage)
                commentFromFB.clear();
                userImageFromFB.clear();
                userNameFromFB.clear();
                documentReference.set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("basarılı");
                        Toast.makeText(DetailActivity.this,"Yorumunuz başarıyla eklenmiştir..",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        String genelBakisStart = genelBakisBolum.getText().toString();
        SpannableString spanString = new SpannableString(genelBakisStart);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        genelBakisBolum.setText(spanString);
        genelBakisBolum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genelBakisLayout.setVisibility(View.VISIBLE);
                yorumlarLayout.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                scrollView2.setVisibility(View.GONE);
                String txt = genelBakisBolum.getText().toString();
                SpannableString spanString = new SpannableString(txt);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                genelBakisBolum.setText(spanString);
                String txt2 = yorumlarBolum.getText().toString();
                SpannableString spanStringYenii = new SpannableString(txt2);
                yorumlarBolum.setText(spanStringYenii);
            }
        });
        yorumlarBolum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genelBakisLayout.setVisibility(View.GONE);
                yorumlarLayout.setVisibility(View.VISIBLE);
                scrollView2.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                String txt2 = genelBakisBolum.getText().toString();
                String txt = yorumlarBolum.getText().toString();
                SpannableString spanString = new SpannableString(txt);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                yorumlarBolum.setText(spanString);
                SpannableString spanStringYeni = new SpannableString(txt2);
                genelBakisBolum.setText(spanStringYeni);
            }
        });
        DocumentReference docReference2 = firebaseFirestore.collection("users").document(userID).collection("image").document( "image");;
        docReference2.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                profileImage = documentSnapshot.getString("userImage");
            }
        });
    }
    public void getComment() {
        CollectionReference collectionReference = firebaseFirestore.collection("sehir").document(id).collection(id).document(yerId).collection("Comment");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null){
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> data = snapshot.getData();
                        String comment = (String) data.get("comment");
                        String userName = (String) data.get("name");
                        String userImage = (String) data.get("userImage");
                        commentFromFB.add(comment);
                        userImageFromFB.add(userImage);
                        userNameFromFB.add(userName);
                        commentRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    public void getDescription(){
        DocumentReference documentReference = firebaseFirestore.collection("sehir").document(id).collection(id).document(yerId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";
                if (snapshot != null && snapshot.exists()) {
                    Map<String,Object> snapshotData = snapshot.getData();
                    String calismaSaatleri = (String) snapshotData.get("calismaSaatleri");
                    String girisUcreti = (String) snapshotData.get("girisUcreti");
                    String genelBilgi = (String) snapshotData.get("genelBilgi");
                    String adres = (String) snapshotData.get("adres");
                    String yerAdi = (String) snapshotData.get("yerAdi");
                    String yerResmi = (String) snapshotData.get("yerResmi");
                    String yerResmi2 = (String) snapshotData.get("yerResmi2");
                    String yerResmi3 = (String) snapshotData.get("yerResmi3");

                    calismaSaatleriTxt.setText(calismaSaatleri);
                    girisUcretitxt.setText(girisUcreti);
                    genelBilgiTxt.setText(genelBilgi);
                    adresTxt.setText(adres);
                    yerAdiTxt.setText(yerAdi);
                    slideModels.add(new SlideModel(yerResmi));
                    slideModels.add(new SlideModel(yerResmi2));
                    slideModels.add(new SlideModel(yerResmi3));
                    imageSlider.setImageList(slideModels,true);
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
