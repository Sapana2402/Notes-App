 package com.example.noteswithpriority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

 public class AddEditNoteActivity extends AppCompatActivity {
private EditText et_title,et_description;
private NumberPicker numberPicker;
private LinearLayout linearLayout;
public static final String EXTRA_ID="com.example.noteswithpriority.EXTRA_ID";
public static final String EXTRA_TITLE="com.example.noteswithpriority.EXTRA_TITLE";
public static final String EXTRA_DESCRIPTION="com.example.noteswithpriority.EXTRA_DESCRIPTION";
public static final String EXTRA_PRIORITY="com.example.noteswithpriority.EXTRA_PRIORITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        et_title=findViewById(R.id.e_title);
        et_description=findViewById(R.id.e_description);
        numberPicker=findViewById(R.id.e_number_picker);
        linearLayout=findViewById(R.id.l1);


        numberPicker.setTextColor(Color.parseColor("#ffd32b3b"));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Intent intent=getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Notes");
            et_title.setText(intent.getStringExtra(EXTRA_TITLE));
            et_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }else {
            setTitle("add notes");
        }
    }

     private void savenotes() {
            String title=et_title.getText().toString();
            String description=et_description.getText().toString();
            int priority=numberPicker.getValue();

            if (title.isEmpty() || description.isEmpty()){
                Snackbar snackbar=Snackbar.make(linearLayout,"Please Insert Value",Snackbar.LENGTH_LONG);
                snackbar.show();
                return;
            }

         Intent data=new Intent();
         data.putExtra(EXTRA_TITLE,title);
         data.putExtra(EXTRA_DESCRIPTION,description);
         data.putExtra(EXTRA_PRIORITY,priority);

         int id=getIntent().getIntExtra(EXTRA_ID,-1);
         if (id !=-1){
             data.putExtra(EXTRA_ID,id);
         }

         setResult(RESULT_OK,data);
         finish();
    }
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater menuInflater=getMenuInflater();
         menuInflater.inflate(R.menu.menu,menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                savenotes();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

     }


 }