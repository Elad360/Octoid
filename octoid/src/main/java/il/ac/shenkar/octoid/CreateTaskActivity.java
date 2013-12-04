package il.ac.shenkar.octoid;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class CreateTaskActivity extends Activity
{
    private TaskListModel taskListModel;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        taskListModel = TaskListModel.getInstance(this);

        final TextView descriptionTextView = (TextView) findViewById(R.id.edit_message);
        final Button newTaskButton = (Button) findViewById(R.id.create_task_button);

        newTaskButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String taskDescription = descriptionTextView.getText().toString();
                taskListModel.pushTask(taskDescription);
                startAlert(view,taskDescription);
                finish();
            }
        });
    }

    public void startAlert(View view, String taskDescription)
    {
        Intent intent = new Intent(this, TasksBroadcastReceiver.class);
        intent.putExtra("task", taskDescription);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),234324243, intent, 0);

        DatePicker picker = (DatePicker) findViewById(R.id.date_picker);
        int day = picker.getDayOfMonth();
        int month = picker.getMonth();
        int year = picker.getYear();

        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        int second = 0;
        Calendar calendar =  Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        long when = calendar.getTimeInMillis();


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
        Toast.makeText(this, "Reminder set to: " + day + "/" + month + "/" + year + " " + hour + ":" + minute, Toast.LENGTH_LONG).show();

    }
}

