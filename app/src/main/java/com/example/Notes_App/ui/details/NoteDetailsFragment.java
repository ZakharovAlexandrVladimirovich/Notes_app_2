package com.example.Notes_App.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Notes_App.R;
import com.example.Notes_App.domain.AppRouteManger;
import com.example.Notes_App.domain.AppRouter;
import com.example.Notes_App.domain.Note;
import com.example.Notes_App.domain.NoteRepo;
import com.example.Notes_App.domain.NotesFirestoreRepo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class NoteDetailsFragment extends Fragment {

    public final static String TAG = "NoteDetailsFragment";
    public final static String REMOVE = "remove_note";
    public final static String REMOVED_NOTES = "removed_note";
    private static final String SELECTED_NOTE = "SELECTED_NOTE";

    final NoteRepo noteRepo = NotesFirestoreRepo.INSTANCE;
    Note note;
    AppRouter appRouter;
//    NotesStorage notesStorage;

    public NoteDetailsFragment() {
    }

    public static NoteDetailsFragment newInstance(Note note) {
        NoteDetailsFragment fragment = new NoteDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof AppRouteManger) {
            appRouter = ((AppRouteManger) getActivity()).getAppRouter();

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        appRouter = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        notesStorage = new NotesStorage(requireContext());
//        noteRepo = new NoteRepoImpl();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TextView noteName = view.findViewById(R.id.fragment_note_details_name);
        TextView noteDescription = view.findViewById(R.id.fragment_note_details_description);
        TextView noteDate = view.findViewById(R.id.fragment_note_details_date);

        if (getArguments() != null) {
            note = getArguments().getParcelable(SELECTED_NOTE);

            noteName.setText(note.getName());
            noteDescription.setText(note.getDescription());
            noteDate.setText(note.getFromatedDate());

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_note_option) {
            Toast.makeText(getContext(), "Edit note", Toast.LENGTH_SHORT).show();
            appRouter.showNoteEditor(note);

        }
        if (item.getItemId() == R.id.delete_option) {
            Toast.makeText(getContext(), "Delete note", Toast.LENGTH_SHORT).show();
            new MaterialAlertDialogBuilder(requireContext())
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setTitle("Are you sure about your decision?")
                    .setMessage("This note will be deleted.\nProceed?")
                    .setPositiveButton("yes", (dialog, which) -> {
                        noteRepo.removeNote(note, result -> {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(REMOVED_NOTES, note);
                            getParentFragmentManager().setFragmentResult(REMOVE, bundle);
                            appRouter.back();
                        });
//                        notesStorage.setList("notes", noteRepo.getNotes());
                    })
                    .setNegativeButton("No", null).show();

        }
        return super.onOptionsItemSelected(item);
    }
}