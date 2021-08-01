package com.example.Notes_App.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Notes_App.R;
import com.example.Notes_App.domain.AppRouteManger;
import com.example.Notes_App.domain.AppRouter;
import com.example.Notes_App.domain.Note;
import com.example.Notes_App.domain.NoteRepo;
import com.example.Notes_App.domain.NotesAdapter;
import com.example.Notes_App.domain.NotesFirestoreRepo;
import com.example.Notes_App.ui.DialogFragments.NotesRemoverDialogFragment;
import com.example.Notes_App.ui.DialogFragments.RemoverDialogFragment;
import com.example.Notes_App.ui.creator.NoteCreatorFragment;
import com.example.Notes_App.ui.editor.NoteEditorFragment;

public class NotesListFragment extends Fragment {

    public final static String TAG = "NotesListFragment";

    NoteRepo noteRepo = NotesFirestoreRepo.INSTANCE;
    AppRouter appRouter;
    //    NotesStorage notesStorage;
    private int longClickIndex;
    private Note longClickNote;
    public NotesAdapter notesAdapter;
    private boolean isLoading = false;
    private ProgressBar progressBar;

    public static NotesListFragment newInstance() {
        return new NotesListFragment();
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
        notesAdapter = new NotesAdapter(this);

        loadNotesList();

        notesAdapterClickListeners();

        setNoteEditorResult();
        setNoteCreatorResult();

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        setRecyclerViewAnimation(recyclerView);

        progressBar = view.findViewById(R.id.progress);

        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        }
//        noteRepo.addAll(notesStorage.getList("notes"));
//        List<Note> noteList = noteRepo.getNotes();

//        notesAdapter.setData(noteList);
//        notesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(notesAdapter);
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_note_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_edit) {
            appRouter.showNoteEditor(longClickNote);
            return true;
        }

        if (item.getItemId() == R.id.action_delete) {
            showRemoveDialogFragment();
            setNoteRemoverResult();
//          notesStorage.setList("notes", noteRepo.getNotes());
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_note_option) {
            appRouter.showNoteCreator();
        }
        if (item.getItemId() == R.id.remove_all_option) {
            showRemoveAllDialogFragment();
            setAllNotesRemoverResult();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerViewAnimation(RecyclerView recyclerView) {
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(1500L);
        animator.setAddDuration(1500L);
        animator.setChangeDuration(1500L);
        recyclerView.setItemAnimator(animator);
    }

    private void showRemoveAllDialogFragment() {
        NotesRemoverDialogFragment.newInstance().show(getChildFragmentManager(), NotesRemoverDialogFragment.TAG);
    }

    private void showRemoveDialogFragment() {
        RemoverDialogFragment.newInstance().show(getChildFragmentManager(), RemoverDialogFragment.TAG);
    }


    private void loadNotesList() {
        isLoading = true;

        noteRepo.getNotes(result -> {
            notesAdapter.setData(result);
            notesAdapter.notifyDataSetChanged();

            isLoading = false;

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void notesAdapterClickListeners() {
        notesAdapter.setClickListener(appRouter::showNoteDetails);

        notesAdapter.setLongClickListener((note, index) -> {
            longClickIndex = index;
            longClickNote = note;
        });
    }

    private void setAllNotesRemoverResult() {
        getChildFragmentManager().setFragmentResultListener(NotesRemoverDialogFragment.YES, getViewLifecycleOwner(), (requestKey, result) ->
                noteRepo.removeAllCollection(result1 -> {
                    int itemCount = notesAdapter.getItemCount();
                    notesAdapter.removeAll();
                    notesAdapter.notifyItemRangeRemoved(0, itemCount);
                }));

    }

    private void setNoteRemoverResult() {
        getChildFragmentManager().setFragmentResultListener(RemoverDialogFragment.YES, getViewLifecycleOwner(), (requestKey, result) ->
                noteRepo.removeNote(longClickNote, result1 -> {
                    notesAdapter.removeNote(longClickNote);
                    notesAdapter.notifyItemRemoved(longClickIndex);
                }));
    }

    private void setNoteCreatorResult() {
        getParentFragmentManager().setFragmentResultListener(NoteCreatorFragment.CREATE, this, (requestKey, result) -> {
            if (result.containsKey(NoteCreatorFragment.NEW_NOTE)) {
                Note createdNote = result.getParcelable(NoteCreatorFragment.NEW_NOTE);
                notesAdapter.add(createdNote);
                int position = notesAdapter.getIndex(createdNote);
                notesAdapter.notifyItemInserted(position);
            }
        });
    }

    private void setNoteEditorResult() {
        getParentFragmentManager().setFragmentResultListener(NoteEditorFragment.EDIT, this, (requestKey, result) -> {
            if (result.containsKey(NoteEditorFragment.UPDATED_NOTE)) {
                Note editedNote = result.getParcelable(NoteEditorFragment.UPDATED_NOTE);
                notesAdapter.updateNote(editedNote);
                notesAdapter.notifyItemChanged(longClickIndex);
            }
        });
    }
}