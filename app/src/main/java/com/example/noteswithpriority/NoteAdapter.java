package com.example.noteswithpriority;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
  private List<NoteEntity> notes=new ArrayList<>();
  private OnItemClickListener listener;


  @NonNull
  @Override
  public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
    return new NoteHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
    NoteEntity currentNotes=notes.get(position);
    holder.textviewTitle.setText(currentNotes.getTitle());

    holder.textviewDescription.setText(currentNotes.getDescription());
    holder.textviewPriority.setText(String.valueOf(currentNotes.getPriority()));
  }

  @Override
  public int getItemCount() {
    return notes.size();
  }

  public void setNotes(List<NoteEntity> notes){
    this.notes=notes;
    notifyDataSetChanged();
  }
  public NoteEntity getNoteAt(int position){
    return  notes.get(position);
  }

  class NoteHolder extends RecyclerView.ViewHolder{
    private TextView textviewTitle,textviewDescription,textviewPriority;

    public NoteHolder(@NonNull View itemView) {
      super(itemView);
      textviewTitle=itemView.findViewById(R.id.text_view_title);
      textviewDescription=itemView.findViewById(R.id.text_view_description);
      textviewPriority=itemView.findViewById(R.id.text_view_priority);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position=getAdapterPosition();
          if (listener!=null && position != RecyclerView.NO_POSITION){
            listener.onItemClick(notes.get(position));
          }

        }
      });
    }
  }
public void restore(int position,NoteEntity entity){
    notes.add(position,entity);
    notifyItemInserted(position);
}
public List<NoteEntity> getdata(){
    return notes;
}

    public interface OnItemClickListener{
    void onItemClick(NoteEntity note);
  }
  public void setOnItemClickListener(OnItemClickListener listener){
    this.listener=listener;
  }
}
