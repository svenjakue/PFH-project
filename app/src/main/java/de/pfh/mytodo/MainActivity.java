package de.pfh.mytodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.pfh.mytodo.Adapter.ToDoAdapter;
import de.pfh.mytodo.Model.ToDoModel;
import de.pfh.mytodo.Util.Database;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView todoRecyclerView;
    private ToDoAdapter todoAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> todoList;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //Aktionsbar ausblenden

        db = new Database(this);
        db.openDatabase(); //Datenbankverbindung öffnen

        todoList = new ArrayList<>();

        // RecyclerView für ToDoListe initialisiert
        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new ToDoAdapter(db,this);
        todoRecyclerView.setAdapter(todoAdapter);

        fab = findViewById(R.id.fab); //Floating Action Button

        //Nach rechts und links swipen
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItem(todoAdapter));
        itemTouchHelper.attachToRecyclerView(todoRecyclerView);

        todoList = db.getAllToDos();
        Collections.reverse(todoList);
        todoAdapter.setToDo(todoList);

        //neue Aufgaben hinzufügen, eingeben können
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewToDo.newInstance().show(getSupportFragmentManager(),AddNewToDo.TAG);
            }
        });
    }
    //Dialogfenster zum eingeben der ToDos wird geschlossen
    @Override
    public void handleDialogClose(DialogInterface dialog){
        todoList = db.getAllToDos();
        Collections.reverse(todoList);
        todoAdapter.setToDo(todoList);
        todoAdapter.notifyDataSetChanged();
    }
}