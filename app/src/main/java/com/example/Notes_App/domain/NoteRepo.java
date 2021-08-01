package com.example.Notes_App.domain;

import java.util.List;

public interface NoteRepo {

    void getNotes(Callback<List<Note>> callback);

    void addNote(Note note, Callback<Note> callback);

    void removeNote(Note note, Callback<Note> callback);

    void updateNote(Note note, Callback<Note> callback);

    void removeAllCollection(Callback callback);

    boolean addAll(List<Note> list);
}
