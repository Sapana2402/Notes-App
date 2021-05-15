package com.example.noteswithpriority;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    private CoordinatorLayout coordinatorLayout;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FloatingActionButton floatingActionButton;
    public static final int ADD_NOTES_REQUEST=1;
    public static final int EDIT_NOTES_REQUEST=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        coordinatorLayout=findViewById(R.id.c1);

        floatingActionButton=findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AddEditNoteActivity.class);
                startActivityForResult(intent,ADD_NOTES_REQUEST);
            }
        });

        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        );
   /*     recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
*/
        NoteAdapter adapter=new NoteAdapter();
        recyclerView.setAdapter(adapter);

        Snackbar snackbar=Snackbar.make(coordinatorLayout,"Save and organize your ideas wherever you are",Snackbar.LENGTH_LONG);
        snackbar.show();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility()==View.VISIBLE){
                    floatingActionButton.hide();
                }else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE){
                    floatingActionButton.show();
                }
            }
        });

        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> notes) {
                adapter.setNotes(notes);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete Note");
                alert.setMessage("Are you sure you want to delete this note? This cannot be undone.");
                alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteViewModel.Delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                        Snackbar snackbar=Snackbar.make(coordinatorLayout,"Removed",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //   adapter.getNoteAt(viewHolder.getAdapterPosition());
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());

                    }
                });
                alert.create();
                alert.show();
/*
               *//* final  int position=viewHolder.getAdapterPosition();
                final NoteEntity entity=adapter.getdata().get(position);
            *//* noteViewModel.Delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));

                Snackbar snackbar=Snackbar.make(coordinatorLayout,"removed",Snackbar.LENGTH_LONG);
              *//*  snackbar.setAction("udo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.restore(position, entity);
                        recyclerView.scrollToPosition(position);
                    }
                });
              *//*  snackbar.show();*/
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoteEntity note) {
                Intent intent=new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());

                startActivityForResult(intent,EDIT_NOTES_REQUEST);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==ADD_NOTES_REQUEST && resultCode==RESULT_OK){


            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            NoteEntity note=new NoteEntity(title,description,priority);
            noteViewModel.Insert(note);

            Toast.makeText(getApplicationContext(),"note saved",Toast.LENGTH_LONG).show();

        }else if (requestCode==EDIT_NOTES_REQUEST && resultCode==RESULT_OK){

            int id=data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);


            if (id== -1){
                Toast.makeText(getApplicationContext(),"note not update",Toast.LENGTH_LONG).show();
                return;
            }

            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            NoteEntity note=new NoteEntity(title,description,priority);
            note.setId(id);
            noteViewModel.Update(note);

            Toast.makeText(getApplicationContext(),"note updated",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"note not saved",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                noteViewModel.deleteAllNotes();
                Toast.makeText(getApplicationContext(),"note all deleted",Toast.LENGTH_LONG).show();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }
}
















