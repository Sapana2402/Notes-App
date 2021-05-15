package com.example.noteswithpriority;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<NoteEntity>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository=new NoteRepository(application);
        allNotes=repository.getAllNotes();
    }

    void Insert(NoteEntity note){
        repository.Insert(note);
    }


    void Update(NoteEntity note){
        repository.Update(note);
    }


    void Delete(NoteEntity note){
        repository.Delete(note);
    }

    void deleteAllNotes(){
        repository.deleteAllNotes();
    }


    LiveData<List<NoteEntity>> getAllNotes(){
        return allNotes;
    }
}
