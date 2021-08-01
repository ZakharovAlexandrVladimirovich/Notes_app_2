package com.example.Notes_App.domain;

import java.util.ArrayList;
import java.util.List;

public class NoteRepoImpl implements NoteRepo {

    private static final List<Note> notes = new ArrayList<>();

    public static final NoteRepo INSTANCE = new NoteRepoImpl();


    @Override
    public void getNotes(Callback<List<Note>> callback) {

    }

    @Override
    public void addNote(Note note, Callback<Note> callback) {
        notes.add(note);
        callback.onSuccess(note);

    }

    @Override
    public void removeNote(Note note, Callback<Note> callback) {
        notes.remove(note);
    }

    @Override
    public void updateNote(Note note, Callback<Note> callback) {

    }

    @Override
    public void removeAllCollection(Callback callback) {

    }


    @Override
    public boolean addAll(List<Note> list) {
        if (list == null) {
            return false;
        }
        notes.clear();
        notes.addAll(list);
        return true;
    }
}
