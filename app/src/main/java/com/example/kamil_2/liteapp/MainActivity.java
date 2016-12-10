package com.example.kamil_2.liteapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button btnAddNew;
    private Button btnEditCompleted;

    private Button btnClearCompleted;

    private Button btnSave;
    private Button btnCancel;

    private Button btnEditTask;
    private Button btnCancelEditTask;


    private EditText etNewTask;
    private EditText etNewTask2;
    private ListView lvTodos;
    private LinearLayout llControlButtons;
    private LinearLayout llNewTaskButtons;
    private LinearLayout llEditTaskButtons;

    int elementid;

    private TodoDbAdapter todoDbAdapter;
    private Cursor todoCursor;
    private List<TodoTask> tasks;
    private TodoTasksAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUiElements();
        initListView();
        initButtonsOnClickListeners();



        if(tasks.size()<2)
        {  todoDbAdapter.insertTodo("jabłka","Polska listopad 2016");
        todoDbAdapter.insertTodo("pomarańcze","Hiszpania październik 2016");
        todoDbAdapter.insertTodo("orzechy włoskie","Włoczy wrzesień 2016");  }


        final Button button = (Button) findViewById(R.id.buttonNewActive);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  setContentView(R.layout.activity_accel);

                Intent iinent= new Intent(MainActivity.this,Main2Activity.class);
                startActivity(iinent);

            }
        });

    }

    private void initUiElements() {
        btnAddNew = (Button) findViewById(R.id.btnAddNew);
        btnEditCompleted = (Button) findViewById(R.id.btnEditCompleted);

        btnClearCompleted = (Button) findViewById(R.id.btnClearCompleted);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnEditTask = (Button) findViewById(R.id.btnEditTask);
        btnCancelEditTask = (Button) findViewById(R.id.btnCancelEditTask);

        etNewTask = (EditText) findViewById(R.id.etNewTask);
        etNewTask2 = (EditText) findViewById(R.id.etNewTask2);
        lvTodos = (ListView) findViewById(R.id.lvTodos);
        llControlButtons = (LinearLayout) findViewById(R.id.llControlButtons);

        llNewTaskButtons = (LinearLayout) findViewById(R.id.llNewTaskButtons);
        llEditTaskButtons = (LinearLayout) findViewById(R.id.llEditTaskButtons);
    }

    private void initListView() {
        fillListViewData();
        initListViewOnItemClick();
    }

    private void fillListViewData() {
        todoDbAdapter = new TodoDbAdapter(getApplicationContext());
        todoDbAdapter.open();
        getAllTasks();
        listAdapter = new TodoTasksAdapter(this, tasks);
        lvTodos.setAdapter(listAdapter);
    }

    private void getAllTasks() {
        tasks = new ArrayList<TodoTask>();
        todoCursor = getAllEntriesFromDb();
        updateTaskList();
    }

    private Cursor getAllEntriesFromDb() {
        todoCursor = todoDbAdapter.getAllTodos();
        if(todoCursor != null) {
            startManagingCursor(todoCursor);
            todoCursor.moveToFirst();
        }
        return todoCursor;
    }

    private void updateTaskList() {
        if(todoCursor != null && todoCursor.moveToFirst()) {
            do {
                long id = todoCursor.getLong(TodoDbAdapter.ID_COLUMN);
                String name = todoCursor.getString(TodoDbAdapter.NAME_COLUMN);
                String description = todoCursor.getString(TodoDbAdapter.DESCRIPTION_COLUMN);
                tasks.add(new TodoTask(id, name, description));
            } while(todoCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        if(todoDbAdapter != null)
            todoDbAdapter.close();
        super.onDestroy();
    }

    private void initListViewOnItemClick() {
        lvTodos.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                TodoTask task = tasks.get(position);

               elementid =(int) id;

                Toast.makeText(getApplicationContext(), task.getDescription().toString()+id, Toast.LENGTH_SHORT).show();

                updateListViewData();
            }
        });
    }

    private void updateListViewData() {
        todoCursor.requery();
        tasks.clear();
        updateTaskList();
        listAdapter.notifyDataSetChanged();
    }

    private void initButtonsOnClickListeners() {
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAddNew:
                        addNewTask();
                        break;
                    case R.id.btnEditCompleted:
                        editNewTask();
                        break;

                    case R.id.btnSave:
                        saveNewTask();
                        break;

                    case R.id.btnEditTask:
                        saveEditNewTask();
                        break;

                    case R.id.btnCancel:
                        cancelNewTask();
                        break;

                    case R.id.btnCancelEditTask:
                        cancelEditTask();
                        break;

                    case R.id.btnClearCompleted:
                        clearCompletedTasks();
                        break;
                    default:
                        break;
                }
            }
        };
        btnAddNew.setOnClickListener(onClickListener);
        btnEditCompleted.setOnClickListener(onClickListener);

        btnClearCompleted.setOnClickListener(onClickListener);
        btnEditTask.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
        btnCancelEditTask.setOnClickListener(onClickListener);
    }

    private void showOnlyNewTaskPanel() {
        setVisibilityOf(llControlButtons, false);
        setVisibilityOf(llEditTaskButtons, false);
        setVisibilityOf(llNewTaskButtons, true);
        setVisibilityOf(etNewTask, true);
        setVisibilityOf(etNewTask2, true);
    }

    private void showOnlyEditTaskPanel() {
        setVisibilityOf(llControlButtons, false);
        setVisibilityOf(llNewTaskButtons, false);
        setVisibilityOf(llEditTaskButtons, true);
        setVisibilityOf(etNewTask, true);
        setVisibilityOf(etNewTask2, true);
    }


    private void showOnlyControlPanel() {
        setVisibilityOf(llControlButtons, true);
        setVisibilityOf(llNewTaskButtons, false);
        setVisibilityOf(llEditTaskButtons, false);
        setVisibilityOf(etNewTask, false);
        setVisibilityOf(etNewTask2, false);
    }

    private void setVisibilityOf(View v, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        v.setVisibility(visibility);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNewTask.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etNewTask2.getWindowToken(), 0);
    }

    private void addNewTask(){
        showOnlyNewTaskPanel();
    }


    private void editNewTask() {
        showOnlyEditTaskPanel();
    }

    private void saveNewTask(){

        String taskName = etNewTask.getText().toString();
        String taskDescription = etNewTask2.getText().toString();

        if(taskDescription.equals("")){
            etNewTask.setError("Your task description couldn't be empty string.");
        } else {
            todoDbAdapter.insertTodo(taskName, taskDescription);
            etNewTask.setText("");
            etNewTask2.setText("");
            hideKeyboard();
            showOnlyControlPanel();
        }
        updateListViewData();
    }

    private void saveEditNewTask(){

        TodoTask task2 =  tasks.get(elementid);

        String taskName = etNewTask.getText().toString();
        String taskDescription = etNewTask2.getText().toString();


        if(taskDescription.equals("")){
            etNewTask.setError("Your task description couldn't be empty string.");
        } else {

            task2.setName(taskName);
            task2.setDescription(taskDescription);

            todoDbAdapter.updateTodo(task2);
            etNewTask.setText("");
            etNewTask2.setText("");
            hideKeyboard();
            showOnlyControlPanel();
        }
        updateListViewData();
    }



    private void cancelNewTask() {
        etNewTask.setText("");
        etNewTask2.setText("");
        showOnlyControlPanel();
    }

    private void cancelEditTask() {
        etNewTask.setText("");
        etNewTask2.setText("");
        showOnlyControlPanel();
    }

    private void clearCompletedTasks(){

       TodoTask task2 =  tasks.get(elementid);

        todoDbAdapter.deleteTodo(task2.getId());

        updateListViewData();
    }

}