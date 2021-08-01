package com.example.Notes_App.domain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Notes_App.R;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    Fragment fragment;
    private OnNoteViewClickListener clickListener;
    private OnNoteViewLongClickListener longClickListener;
    private final List<Note> notesList = new ArrayList<>();

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public interface OnNoteViewClickListener {
        void onNoteClickListener(@NonNull Note note);
    }

    public interface OnNoteViewLongClickListener {
        void onNoteViewLongClickListener(@NonNull Note note, int index);
    }


    public OnNoteViewClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnNoteViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public OnNoteViewLongClickListener getLongClickListener() {
        return longClickListener;
    }

    public void setLongClickListener(OnNoteViewLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public boolean removeNote(Note note) {
        return notesList.remove(note);
    }

    public boolean updateNote(Note note) {
        for (int i = 0; i < notesList.size(); i++) {

            Note item = notesList.get(i);

            if (item.getId().equals(note.getId())) {

                notesList.remove(i);
                notesList.add(i, note);

                return true;
            }
        }
        return false;
    }

    public void setData(List<Note> list) {
        if (list != null) {
            notesList.clear();
            notesList.addAll(list);
        }
    }

    public void add(Note note) {
        notesList.add(note);
    }

    public int getIndex(Note note) {
        if (notesList.contains(note)) {
            return notesList.indexOf(note);
        }
        return -1;
    }

    public void removeAll() {
        notesList.clear();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {

        Note note = notesList.get(position);

        holder.bind(note);


    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteName;
        TextView noteDescription;
        TextView noteDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            fragment.registerForContextMenu(itemView);

            itemView.setOnClickListener(v -> {
                if (getClickListener() != null) {
                    getClickListener().onNoteClickListener(notesList.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(v -> {
                itemView.showContextMenu();

                if (getLongClickListener() != null) {
                    int index = getAdapterPosition();
                    getLongClickListener().onNoteViewLongClickListener(notesList.get(index), index);
                }

                return true;
            });


            noteName = itemView.findViewById(R.id.item_note_name);
            noteDescription = itemView.findViewById(R.id.item_note_description);
            noteDate = itemView.findViewById(R.id.item_note_date);
        }

        public void bind(Note note) {
            noteName.setText(note.getName());
            noteDescription.setText(note.getDescription());
            noteDate.setText(note.getFromatedDate());
        }
    }
}
