package de.pfh.mytodo.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.pfh.mytodo.Model.ToDoModel;

//https://www.programmierenlernenhq.de/android-sqlite-projekt-in-android-studio-anlegen/
public class Database extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    //Tabellen und Spaltennamen
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    //Tabelle erstellen
    private static final String CREATE_TABLE = "CREATE TABLE " + TODO_TABLE + " (" + ID + ", " + TASK + ", " + STATUS + ") ";

    private SQLiteDatabase db;

    public Database(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    } //erstellt Tabelle, wenn nicht vorhanden
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Löscht alte und ersteltt neue Tabelle bei Änderungen https://stackoverflow.com/questions/48130300/android-studio-error-in-sqlitedatabase-drop-if-table-exists
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    //Neues ToDo in Datenbank hinzufügen
    public void insertToDo(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTodo());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE,null, cv);
    }
    //Alle ToDos aus der Datenbank abrufen > Liste ToDoModel
    public List<ToDoModel> getAllToDos(){
        List<ToDoModel> todoList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModel todo = new ToDoModel();
                        todo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                        todo.setTodo(cursor.getString(cursor.getColumnIndexOrThrow(TASK)));
                        todo.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(STATUS)));
                        todoList.add(todo);
                    }while (cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cursor.close();
        }
        return todoList;
    }
    //Status aktualisieren
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv,ID+ "=?", new String[]{String.valueOf(id)});
    }
    //Text der Aufgabe aktualisieren
    public void updateTask(int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "+?", new String[]{String.valueOf(id)});

    }
    //Aufgabe löschen
    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }
}
