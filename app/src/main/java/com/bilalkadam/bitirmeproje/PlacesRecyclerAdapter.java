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
public class PlacesRecyclerAdapter extends RecyclerView.Adapter <PlacesRecyclerAdapter.PlacesHolder>{
    private ArrayList<String> yerAdiList;
    private ArrayList<String> yerResmiList;
    private PlacesListener mplacesListener;
    public PlacesRecyclerAdapter(ArrayList<String> yerAdiList, ArrayList<String> yerResmiList, PlacesListener placesListener) {
        this.yerAdiList = yerAdiList;
        this.yerResmiList = yerResmiList;
        this.mplacesListener = placesListener;
    }
    @NonNull
    @Override
    public PlacesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //view holder olusturulunca ne yapılacagı
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewPlaces = layoutInflater.inflate(R.layout.recycler_row_places,parent,false);
        return new PlacesHolder(viewPlaces,mplacesListener);
    }
    @Override
    public void onBindViewHolder(@NonNull PlacesHolder holder, int position) { //view holdera baglanınca ne yapılacagı
        holder.yerAdiText.setText(yerAdiList.get(position));
        Picasso.get().load(yerResmiList.get(position)).into(holder.imageView);
    }
    @Override
    public int getItemCount() {  // kac tane row olacagı
        return yerAdiList.size();
    }
    class PlacesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{  // olusturulan görünümleri tanımlıyoruz gorunumler bunun ıcınde tanımallanır
        ImageView imageView;
        TextView yerAdiText;
        PlacesListener placesListener;
            public PlacesHolder(@NonNull View itemView, PlacesListener placesListener) {
                super(itemView);
                imageView = itemView.findViewById(R.id.recyclerview_row_yerResmi);
                yerAdiText = itemView.findViewById(R.id.recyclerView_row_yerAdi);
                this.placesListener = placesListener;
                itemView.setOnClickListener(this);
            }
        @Override
        public void onClick(View v) {
            placesListener.onPlacesClick(getAdapterPosition());
        }
    }
        public interface PlacesListener{
            void onPlacesClick(int position);
        }
}
