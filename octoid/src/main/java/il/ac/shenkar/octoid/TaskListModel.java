package il.ac.shenkar.octoid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class TaskListModel extends SQLiteOpenHelper
{
    private static TaskListModel instance = null;
    private ArrayList<Task> tasks;
    private Context context;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "octoidDB";
    // Tasks table name
    private static final String TABLE_TASKS = "tasks";

    // Tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    //Private Constructor. Enforces singleton.
    private TaskListModel(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        tasks = getTasks();
    }

    //Public accessor for the class
    public synchronized static TaskListModel getInstance(Context context)
    {
        if (instance==null)
        {
            instance = new TaskListModel(context);
        }
        return instance;
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
//      Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

//      Create tables again
        onCreate(db);
    }


    //Add new task
    public void pushTask(String description)
    {
        tasks.add(new Task(System.currentTimeMillis(), description));

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,System.currentTimeMillis());
        values.put(KEY_NAME, description);

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    //Remove any task
    public void removeTask(Task taskToRemove)
    {
        tasks.remove(taskToRemove);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?", new String[] { String.valueOf(taskToRemove.getId()) });
        db.close();
    }

    public int getCount()
    {
        return tasks.size();
    }

    public boolean isEmpty()
    {
        return tasks.isEmpty();
    }

    //Get items from new to old
    public Task getItem(int i)
    {
       return tasks.get(tasks.size()-i-1);
    }

    public ArrayList<Task> getTasks()
    {
        // Getting all tasks
        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Task task = new Task(Long.parseLong(cursor.getString(0)),cursor.getString(1));
                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        return taskList;
    }

}

