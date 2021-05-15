package com.bilalkadam.bitirmeproje;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.NoteHolder> {
    private ArrayList<String> noteList;
    public NotesRecyclerAdapter(ArrayList<String> noteList) {
        this.noteList = noteList;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_row_notes,parent,false);
        return new NoteHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.noteRecyclerRowTxt.setText(noteList.get(position));
    }
    @Override
    public int getItemCount() {
        return noteList.size();
    }
    class NoteHolder extends RecyclerView.ViewHolder{
        TextView noteRecyclerRowTxt;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteRecyclerRowTxt = itemView.findViewById(R.id.recycler_row_notes_text);
        }
    }
}
