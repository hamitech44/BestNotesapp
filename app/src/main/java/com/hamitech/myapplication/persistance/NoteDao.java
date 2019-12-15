package com.hamitech.myapplication.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hamitech.myapplication.Models.NoteModel;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    long[] insertNotes(NoteModel... noteModels);

    @Delete
    int deleteNotes(NoteModel... noteModels);

    @Query("SELECT * FROM notestable")
    LiveData<List<NoteModel>> getNotes();

    @Update
    int updateNotes(NoteModel... noteModels);
}
