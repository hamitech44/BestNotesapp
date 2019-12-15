package com.hamitech.myapplication.async;

import android.os.AsyncTask;

import com.hamitech.myapplication.Models.NoteModel;
import com.hamitech.myapplication.persistance.NoteDao;

public class InsertAsyncTask extends AsyncTask<NoteModel,Void,Void> {
        private NoteDao noteDao;

    public InsertAsyncTask(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(NoteModel... noteModels) {
        noteDao.insertNotes(noteModels);
    return null;
    }
}
