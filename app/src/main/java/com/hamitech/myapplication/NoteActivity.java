package com.hamitech.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hamitech.myapplication.Models.NoteModel;
import com.hamitech.myapplication.RecyclerViewDecorator.Utility;
import com.hamitech.myapplication.persistance.NoteRepository;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.OvalShape;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

public class NoteActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener
, GestureDetector.OnDoubleTapListener, View.OnClickListener,TextWatcher {
    private static final int EDIT_MODE_ENABLED=1;
    private static final int EDIT_MODE_DISABLED=0;
    private static final String SHOWCASE_ID = "simple";

    //ui components
    private TextView mTextView;
    private EditText mEditText;
    private LinedEditText mLinedEdittext;
    RelativeLayout backcontainer,checkcontainer;
    ImageButton backarrow,check;
//var
    private boolean mIsNewNote;
    private NoteModel initialNote;
    private NoteModel finalnote;
    private GestureDetector gestureDetector;
    private int mMode;
    private NoteRepository noteRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        backarrow=findViewById(R.id.toolbar_back_arrow);
        check=findViewById(R.id.toolbar_check_arrow);
        backcontainer=findViewById(R.id.back_arrow_container);
        checkcontainer=findViewById(R.id.check_arrow_container);
        mTextView=findViewById(R.id.note_text_title);
        noteRepository= new NoteRepository(this);
        mEditText=findViewById(R.id.note_edittext_title);
        mLinedEdittext=findViewById(R.id.notepad);

        if (isNewNote()){
                    //EditMode
            setNewProperties();
            enableEditMode();
            enableInteractionOfEditText();
        }
        else
        {           //ViewMode
            setNoteProperties();
            disableEditMode();
            disableInteractionOfEditText();
        }
        setListeners();
        presentShowcaseSequence();

    }

    //Show intro to first time users

    private void presentShowcaseSequence() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(300); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) { }});
        sequence.setConfig(config);
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.note_edittext_title))
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setTitleText("Type Title for your Notes here")
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setSkipText("SKIP")
                        .setTarget(findViewById(R.id.check_arrow_container))
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setTitleText("Tap on ✓ to save")
                        .withCircleShape()

                        .build()
        );
        sequence.addSequenceItem(

                new MaterialShowcaseView.Builder(this)

                        .setTarget(findViewById(R.id.notepad))
                        .setDismissText("That's it. Tap here to start using :)")
                        .setDismissTextColor(R.color.yellow)
                        .setShape(new OvalShape())
                        .setTitleText("Type your Content here")
                        .setContentText("By default saved notes open in read only mode. You can double tap to Edit them")
                        .build()

        );
        sequence.start();
    }
    //to hide keyboard when saving note
    private void hideSoftKeyboard(){
        InputMethodManager imm=(InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    //to save any cahnges in notepad or to add new notes
    private void saveChanges(){
        if (mIsNewNote){
                saveNewNote();
        }else{
            updateNotes();
        }
    }
// to update any current note
    private void updateNotes(){
        noteRepository.updateNote(finalnote);
    }
    //to add new note
    private void saveNewNote(){
        noteRepository.insertnote(finalnote);

    }
    //to enable edit mode
    private void enableEditMode(){
        backcontainer.setVisibility(View.GONE);
        checkcontainer.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
        mEditText.setVisibility(View.VISIBLE);
        mMode=EDIT_MODE_ENABLED;
        enableInteractionOfEditText();

    }
//to disable edit mode
    private void disableEditMode(){
        backcontainer.setVisibility(View.VISIBLE);
        checkcontainer.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mEditText.setVisibility(View.GONE);
        mMode=EDIT_MODE_DISABLED;
        disableInteractionOfEditText();
        String temp= mLinedEdittext.getText().toString();
        temp =temp.replace("/n","");
        temp =temp.replace(" ","");
        if (temp.length()>0){
            finalnote.setTitle(mEditText.getText().toString());
            finalnote.setDescription(mLinedEdittext.getText().toString());
            String timestamp= Utility.getCurrentTimestamp();
            finalnote.setTimestamp(timestamp);
            if (!finalnote.getDescription().equals(initialNote.getDescription())
                    ||!finalnote.getTitle().equals(initialNote.getTitle())){
                saveChanges();
            }
        }
    }

    private void disableInteractionOfEditText(){
        mLinedEdittext.setKeyListener(null);
        mLinedEdittext.setFocusable(false);
        mLinedEdittext.setFocusableInTouchMode(false);
        mLinedEdittext.setCursorVisible(false);
        mLinedEdittext.clearFocus();
    }

    private void enableInteractionOfEditText(){
        mLinedEdittext.setKeyListener(new EditText(this).getKeyListener());
        mLinedEdittext.setFocusable(true);
        mLinedEdittext.setFocusableInTouchMode(true);
        mLinedEdittext.setCursorVisible(true);
        mLinedEdittext.requestFocus();
    }
    private boolean isNewNote(){

        if (getIntent().hasExtra("selected_note")){
            initialNote=getIntent().getParcelableExtra("selected_note");
          finalnote= new NoteModel();
          finalnote.setTimestamp(initialNote.getTimestamp());
          finalnote.setTitle(initialNote.getTitle());
          finalnote.setDescription(initialNote.getDescription());
          finalnote.setId(initialNote.getId());
            mIsNewNote=false;
            mMode=EDIT_MODE_DISABLED;
            return false;

        }

        mMode=EDIT_MODE_ENABLED;
        mIsNewNote=true;
        return true;

    }
    private void setNewProperties(){

        mTextView.setText("New note");
        mEditText.setText("New note");
        initialNote= new NoteModel();
        finalnote= new NoteModel();
        initialNote.setTitle("Note Title");
        finalnote.setTitle("Note Title");

    }
    private void setNoteProperties(){
    mEditText.setText(initialNote.getTitle());
    mTextView.setText(initialNote.getTitle());
    mLinedEdittext.setText(initialNote.getDescription());

    }
private void setListeners(){
        mLinedEdittext.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this,this);
        mTextView.setOnClickListener(this);
        check.setOnClickListener(this);
        backarrow.setOnClickListener(this);
        mEditText.addTextChangedListener(this);


}

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        enableEditMode();
        return false;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.toolbar_check_arrow:{
                hideSoftKeyboard();
                disableEditMode();
                break;
            }
            case R.id.note_text_title:{
                enableEditMode();
                mEditText.requestFocus();
                mEditText.setSelection(mEditText.length());
                break;
            }
            case R.id.toolbar_back_arrow:
            {
                finish();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mMode==EDIT_MODE_ENABLED)
        {
            onClick(check);

        }
        else{
        super.onBackPressed();
    }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode",mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    mMode=savedInstanceState.getInt("mode");
    if (mMode==EDIT_MODE_ENABLED)
    {
        enableEditMode();
    }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       mTextView.setText( charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

