package com.example.Notes_App.domain;

import androidx.fragment.app.FragmentManager;

import com.example.Notes_App.R;
import com.example.Notes_App.ui.about.AboutFragment;
import com.example.Notes_App.ui.auth.AuthFragment;
import com.example.Notes_App.ui.creator.NoteCreatorFragment;
import com.example.Notes_App.ui.details.NoteDetailsFragment;
import com.example.Notes_App.ui.editor.NoteEditorFragment;
import com.example.Notes_App.ui.list.NotesListFragment;

public class AppRouter {

    final FragmentManager fragmentManager;

    public AppRouter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showNotesList() {
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, NotesListFragment.newInstance(), NotesListFragment.TAG)
                .commit();
    }

    public void showNoteDetails(Note note) {
        fragmentManager.beginTransaction()
                .addToBackStack(NoteDetailsFragment.TAG)
                .replace(R.id.main_container, NoteDetailsFragment.newInstance(note), NoteDetailsFragment.TAG)
                .commit();
    }

    public void showAbout() {
        fragmentManager
                .beginTransaction()
                .addToBackStack(AboutFragment.TAG)
                .replace(R.id.main_container, AboutFragment.newInstance(), AboutFragment.TAG)
                .commit();
    }

    public void showNoteCreator() {
        fragmentManager
                .beginTransaction()
                .addToBackStack(NoteCreatorFragment.TAG)
                .replace(R.id.main_container, NoteCreatorFragment.newInstance(), NoteCreatorFragment.TAG)
                .commit();
    }

    public void showNoteEditor(Note note) {
        fragmentManager
                .beginTransaction()
                .addToBackStack(NoteCreatorFragment.TAG)
                .replace(R.id.main_container, NoteEditorFragment.newInstance(note), NoteEditorFragment.TAG)
                .commit();
    }

    public void showAuth() {
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, AuthFragment.newInstance(), AuthFragment.TAG)
                .commit();
    }


    public void back() {
        fragmentManager.popBackStack();
    }
}
