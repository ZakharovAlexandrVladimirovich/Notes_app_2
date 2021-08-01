package com.example.Notes_App.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NotesStorage {

    private static SharedPreferences sPrefs;

    public NotesStorage(Context context) {
        sPrefs = context.getSharedPreferences("com.example.notesList", Context.MODE_PRIVATE);
    }

    public void setList(String key, List<Note> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json);
    }

    public static void set(String key, String value) {
        sPrefs.edit()
                .putString(key, value)
                .apply();
    }

    public List<Note> getList(String key) {
        String serializedNotesList = sPrefs.getString(key, null);

        Gson gson = new Gson();
        Type type = new TypeToken<List<Note>>() {
        }.getType();
        return gson.fromJson(serializedNotesList, type);
    }
}
