package il.ac.shenkar.octoid;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.net.URL;
import java.util.Calendar;


public class CreateTaskActivity extends Activity
{
    private TaskListModel taskListModel;
    Button btnSelectDate,btnSelectTime,btnNewTask, btnGetRandomTask;

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    // variables to save user selected date and time
    public  int year,month,day,hour,minute;
    // declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
    private int mYear, mMonth, mDay,mHour,mMinute;

    // constructor
    public CreateTaskActivity()
    {
        // Assign current Date and Time Values to Variables
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        taskListModel = TaskListModel.getInstance(this);
        final TextView descriptionTextView = (TextView) findViewById(R.id.edit_message);

        // get the references of buttons
        btnGetRandomTask=(Button)findViewById(R.id.button_get_random_task);
        btnSelectDate=(Button)findViewById(R.id.button_select_date);
        btnSelectTime=(Button)findViewById(R.id.button_select_time);
        btnNewTask = (Button) findViewById(R.id.button_create_task);

        btnGetRandomTask.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new GetRandomTaskFromWeb("http://mobile1-tasks-dispatcher.herokuapp.com/task/random").execute();
            }
        });

        // Set ClickListener on btnSelectDate
        btnSelectDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Show the DatePickerDialog
                showDialog(DATE_DIALOG_ID);
            }
        });

        // Set ClickListener on btnSelectTime
        btnSelectTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Show the TimePickerDialog
                showDialog(TIME_DIALOG_ID);
            }
        });

        btnNewTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String taskDescription = descriptionTextView.getText().toString();
                if (taskDescription.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Task description cannot be empty", Toast.LENGTH_LONG).show();
                }
                else
                {
                    taskListModel.pushTask(taskDescription);
                    if (year != 0 && month != 0 && day != 0 && hour != 0 && minute != 0)
                    {
                        startAlert(view,taskDescription);
                    }
                    else
                    {
                        Toast.makeText(view.getContext(), "Task created. No alarm was set", Toast.LENGTH_LONG).show();
                    }

                    finish();
                }

            }
        });

    }


    // Register  DatePickerDialog listener
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                // the callback received when the user "sets" the Date in the DatePickerDialog
                public void onDateSet(DatePicker view, int yearSelected,
                                      int monthOfYear, int dayOfMonth) {
                    year = yearSelected;
                    month = monthOfYear;
                    day = dayOfMonth;
                    // Set the Selected Date in Select date Button
                    btnSelectDate.setText("Date selected : "+day+"-"+month+"-"+year);
                }
            };

    // Register  TimePickerDialog listener
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                // the callback received when the user "sets" the TimePickerDialog in the dialog
                public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                    hour = hourOfDay;
                    minute = min;
                    // Set the Selected Date in Select date Button
                    btnSelectTime.setText("Time selected :"+hour+":"+minute);
                }
            };


    // Method automatically gets Called when you call showDialog()  method
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // create a new DatePickerDialog with values you want to show
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
            // create a new TimePickerDialog with values you want to show
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, mHour, mMinute, false);

        }
        return null;
    }


    private void startAlert(View view, String taskDescription)
    {

        Intent intent = new Intent(this, TasksBroadcastReceiver.class);
        intent.putExtra("task", taskDescription);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),234324243, intent, 0);

        int second = 0;
        Calendar calendar =  Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        long when = calendar.getTimeInMillis();


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
        Toast.makeText(this, "Task created. Reminder set to: " + day + "/" + month + "/" + year + " " + hour + ":" + minute, Toast.LENGTH_LONG).show();

    }


///////////////////////////////////////////////////////////
    private class GetRandomTaskFromWeb extends AsyncTask<URL, Integer, String>
    {
        ProgressDialog mProgressDialog;
        String urlStr;

        public GetRandomTaskFromWeb(String url)
        {
            this.urlStr = url;
        }

        @Override
        protected String doInBackground(URL... urls)
        {
            try
            {
                TasksWebManager webManager = new TasksWebManager(urlStr);
                return webManager.pullTask();  //Background thread
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result)
        {
            final TextView descriptionTextView = (TextView) findViewById(R.id.edit_message);
            descriptionTextView.setText(result);
        }

    }
//////////////////////////////////////////////////////////


}


