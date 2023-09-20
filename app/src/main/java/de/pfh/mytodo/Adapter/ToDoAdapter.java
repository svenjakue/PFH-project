package de.pfh.mytodo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import de.pfh.mytodo.AddNewToDo;
import de.pfh.mytodo.MainActivity;
import de.pfh.mytodo.Model.ToDoModel;
import de.pfh.mytodo.R;
import de.pfh.mytodo.Util.Database;

//Liste der Aufgaben wird in einer RecyclerView angezeigt.
// Zum Anzeigen, Bearbeiten und Löschen von ToDos
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private Database db;

    //Adapter Konstruktor
    public ToDoAdapter (Database db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    //Layout für Listenelement
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_layout, parent, false);
        return new ViewHolder(itemView);
    }
    // Daten an ViewHolder
    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        ToDoModel item = todoList.get(position);
        //Text und Status
        holder.todo.setText(item.getTodo());
        holder.todo.setChecked(toBoolean(item.getStatus()));
        //Listener, um Status zu aktualisieren in Datenbank
        holder.todo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1); //abgehakt
                }
                else{
                    db.updateStatus(item.getId(),0); //noch zu erledigen
                }
            }
        });
    }

    public int getItemCount(){
        return todoList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    //Liste der Aufgaben aktualisieren
    public void setToDo(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();//benachrichtigung über Änderungen
    }
    //Context der MainActivity
    public Context getContext() {return activity;}

    //Aufgabe löschen
    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    //Editor
    public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("todo",item.getTodo());
        AddNewToDo f = new AddNewToDo();
        f.setArguments(bundle);
        f.show(activity.getSupportFragmentManager(), AddNewToDo.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox todo;

        ViewHolder(View view){
            super(view);
            todo = view.findViewById(R.id.todoCheckBox);
        }
    }
}
