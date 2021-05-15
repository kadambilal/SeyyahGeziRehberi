package com.bilalkadam.bitirmeproje;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.SehirHolder> {
    private ArrayList<String> sehirAdiList;
    private ArrayList<String> sehirResmiList;
    private SehirListener mSehirListener;
    public HomeRecyclerAdapter(ArrayList<String> sehirAdiList, ArrayList<String> sehirResmiList, SehirListener sehirListener) {
        this.sehirAdiList = sehirAdiList;
        this.sehirResmiList = sehirResmiList;
        this.mSehirListener = sehirListener;
    }
    @NonNull
    @Override
    public SehirHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // View Holdera oluşturulunca ne yapılacagı yazılacak // olusturdugum xml ile baglama ıslemını yapıcam
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_home,parent,false);
        return new SehirHolder(view,mSehirListener);
    }
    @Override
    public void onBindViewHolder(@NonNull SehirHolder holder, int position) { // View Holdera baglanınca ne yapılacagı yazılacak
        holder.sehirText.setText(sehirAdiList.get(position));
        Picasso.get().load(sehirResmiList.get(position)).into(holder.imageSehir);
    }
    @Override
    public int getItemCount() { // recycler view da kac tane row olacagını burada belırtıyoruz.
        return sehirAdiList.size();
    }
    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }
    class SehirHolder extends RecyclerView.ViewHolder implements View.OnClickListener { // oluşturdugumuz gorunumler burada tutuluyor.
        ImageView imageSehir;
        TextView sehirText;
        SehirListener sehirListener;
        public SehirHolder(@NonNull View itemView, SehirListener sehirListener) {
            super(itemView);
            imageSehir = itemView.findViewById(R.id.recyclerview_row_sehirResmi);
            sehirText = itemView.findViewById(R.id.recyclerview_row_sehirAdi);
            this.sehirListener = sehirListener ;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            sehirListener.OnSehirClick(getAdapterPosition());
        }
    }
    public interface SehirListener{
       void OnSehirClick(int position);
    }
}