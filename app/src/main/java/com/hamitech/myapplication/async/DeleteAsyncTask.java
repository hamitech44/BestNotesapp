package com.hamitech.myapplication.async;

import android.os.AsyncTask;

import com.hamitech.myapplication.Models.NoteModel;
import com.hamitech.myapplication.persistance.NoteDao;

public class DeleteAsyncTask extends AsyncTask<NoteModel,Void,Void> {
        private NoteDao noteDao;

    public DeleteAsyncTask(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(NoteModel... noteModels) {
        noteDao.deleteNotes(noteModels);
    return null;
    }
}
