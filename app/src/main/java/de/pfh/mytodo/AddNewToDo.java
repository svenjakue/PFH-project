package de.pfh.mytodo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import de.pfh.mytodo.Model.ToDoModel;
import de.pfh.mytodo.Util.Database;

public class AddNewToDo extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    //UI
    private EditText newTaskText;
    private Button newTaskSaveBotton;
    // Datenbank Verbindung
    private Database db;

    public static AddNewToDo newInstance(){
        return new AddNewToDo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    //Tastatur reagiert auf Änderungen im layout
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_todo, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super. onViewCreated(view, savedInstanceState);
        newTaskText =getView().findViewById(R.id.newToDoText);
        newTaskSaveBotton = getView().findViewById(R.id.newToDoButton);

        //Datenbankverbindung
        db = new Database(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if(task.length()>0)
                newTaskSaveBotton.setTextColor(Color.GREEN);
        }
        //Textänderungen Eingabe
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveBotton.setEnabled(false);
                    newTaskSaveBotton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveBotton.setEnabled(true);
                    newTaskSaveBotton.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        newTaskSaveBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String text = newTaskText.getText().toString();
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"),text);
                }
                else{
                    ToDoModel task = new ToDoModel();
                    task.setTodo(text);
                    task.setStatus(0);
                }
                dismiss();
            }
        });
    }
    //Dialog Fenster schließen
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
