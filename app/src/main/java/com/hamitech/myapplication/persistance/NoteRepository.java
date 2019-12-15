package com.hamitech.myapplication.persistance;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.hamitech.myapplication.Models.NoteModel;
import com.hamitech.myapplication.async.DeleteAsyncTask;
import com.hamitech.myapplication.async.InsertAsyncTask;
import com.hamitech.myapplication.async.UpdateAsyncTask;

import java.util.List;

public class NoteRepository {

    private NoteDatabase noteDatabase;

    public NoteRepository(Context context) {
        noteDatabase= NoteDatabase.getInstance(context);
    }

    public void insertnote(NoteModel noteModel){
     new InsertAsyncTask(noteDatabase.getNoteDao()).execute(noteModel);
    }
    public void updateNote(NoteModel noteModel){
  new UpdateAsyncTask(noteDatabase.getNoteDao()).execute(noteModel);
    }
    public LiveData<List<NoteModel>> retrievedata()
    {
        return noteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(NoteModel noteModel){
 new DeleteAsyncTask(noteDatabase.getNoteDao()).execute(noteModel);
    }

}
