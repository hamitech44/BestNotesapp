package com.hamitech.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hamitech.myapplication.Adapters.NotesAdaptor;
import com.hamitech.myapplication.Models.NoteModel;
import com.hamitech.myapplication.RecyclerViewDecorator.RecyclerViewDecorator;
import com.hamitech.myapplication.persistance.NoteRepository;

import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.OvalShape;


public class MainActivity extends AppCompatActivity implements NotesAdaptor.OnNotesListner, View.OnClickListener {
    RecyclerView recyclerView;
    private static final String SHOWCASE_ID = "simple example";
    private ArrayList<NoteModel> arrayList = new ArrayList<>();
    private NotesAdaptor notesAdaptor;
    private NoteRepository noteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbarNew));
        setTitle("Notes");
        noteRepository= new NoteRepository(this);
        findViewById(R.id.fab).setOnClickListener(this);
        recyclerView = findViewById(R.id.myrecyclerview);
        initRecyclerView();
        retrieveNotes();
        presentShowcaseSequence();
    }

    // to introduce app to first time users
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(300); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {

            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {

            }
        });
        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.myrecyclerview))
                        .setDismissText("GOT IT")
                        .setDismissTextColor(R.color.yellow)
                        .setShape(new OvalShape())
                        .setTitleText("Your Notes will be displayed here.")
                        .setContentText("Swipe from left or right to delete your note.")
                        .build()

        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.fab))
                        .setDismissText("Let's add your first note")
                        .setDismissTextColor(R.color.ydsd)
                        .setTitleText("You can tap on + sign to add a new note")
                        .withCircleShape()
                        .build()
        );
        sequence.start();
    }



    private void retrieveNotes(){
        noteRepository.retrievedata().observe(this, new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> noteModels) {
                if (arrayList.size()>0){
                    arrayList.clear();
                }
                if (noteModels!=null){
                    arrayList.addAll(noteModels);
                }
                notesAdaptor.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewDecorator recyclerViewDecorator=new RecyclerViewDecorator(20);
        recyclerView.addItemDecoration(recyclerViewDecorator);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
        notesAdaptor = new NotesAdaptor(arrayList,this);
        recyclerView.setAdapter(notesAdaptor);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent= new Intent(this,NoteActivity.class);
        intent.putExtra("selected_note",arrayList.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent =new Intent(this,NoteActivity.class);
        startActivity(intent);
    }
    public void deleteNote(NoteModel noteModel){
        arrayList.remove(noteModel);
        notesAdaptor.notifyDataSetChanged();
        noteRepository.deleteNote(noteModel);
    }
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallBack =
            new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
        }

                @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    deleteNote(arrayList.get(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this,"Note Deleted",Toast.LENGTH_SHORT).show();
        }
    };
}
