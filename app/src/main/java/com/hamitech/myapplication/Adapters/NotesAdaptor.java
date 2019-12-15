package com.hamitech.myapplication.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamitech.myapplication.Models.NoteModel;
import com.hamitech.myapplication.R;
import com.hamitech.myapplication.RecyclerViewDecorator.Utility;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class NotesAdaptor extends RecyclerView.Adapter<NotesAdaptor.ViewHolder>{

    private ArrayList<NoteModel> arrayList= new ArrayList<>();
private OnNotesListner mNoteListner;
    public NotesAdaptor(ArrayList<NoteModel> arrayList, OnNotesListner mNoteListner) {
        this.arrayList = arrayList;
        this.mNoteListner=mNoteListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view,parent,false);
        return new ViewHolder(view,mNoteListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

      try {
          String month= arrayList.get(position).getTimestamp().substring(0,2);
          month=Utility.getMonthFromNumber(month);
          String year= arrayList.get(position).getTimestamp().substring(3);
          String timeStamp=month+" "+year;

          holder.title.setText(arrayList.get(position).getTitle());
          holder.timestamp.setText(timeStamp);

      }catch (NullPointerException e)
      {
          Log.d(TAG, "onBindViewHolder: "+ e.getMessage());
      }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,timestamp;
        OnNotesListner onNotesListner;
        public ViewHolder(@NonNull View itemView, OnNotesListner onClickListener) {
            super(itemView);
            onNotesListner=onClickListener;
            timestamp=itemView.findViewById(R.id.timestamp);
            title=itemView.findViewById(R.id.notetitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                onNotesListner.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNotesListner{
        void onNoteClick(int position);

    }

}