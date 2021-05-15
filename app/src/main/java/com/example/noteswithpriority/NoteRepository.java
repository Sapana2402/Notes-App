package com.example.noteswithpriority;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<NoteEntity>> allnotes;

    public NoteRepository(Application application){
        NoteDatabase database =NoteDatabase.getInstance(application);
        noteDao=database.noteDao();
        allnotes=noteDao.getAllNotes();
    }


    void Insert(NoteEntity note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }


    void Update(NoteEntity note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }


    void Delete(NoteEntity note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    void deleteAllNotes(){
        new DeleteAllNotesNoteAsyncTask(noteDao).execute();
    }


    LiveData<List<NoteEntity>> getAllNotes(){
        return allnotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<NoteEntity,Void,Void>{
        private NoteDao noteDao;

        public InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... note) {
            noteDao.Insert(note[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<NoteEntity,Void,Void>{
        private NoteDao noteDao;

        public UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... note) {
            noteDao.Update(note[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<NoteEntity,Void,Void>{
        private NoteDao noteDao;

        public DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... note) {
            noteDao.Delete(note[0]);
            return null;
        }
    }

    private static class DeleteAllNotesNoteAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;

        public DeleteAllNotesNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }


}
