package il.ac.shenkar.octoid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;

public class ListViewTasksActivity extends Activity
{

    Context context;
    ItemListBaseAdapter taskListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        TaskListModel taskListModel = TaskListModel.getInstance(this);

        ListView taskList = (ListView) findViewById(R.id.listV_main);
        taskListAdapter = new ItemListBaseAdapter(this);
        taskList.setAdapter(taskListAdapter);

        Button newTaskButton = (Button) findViewById(R.id.new_task_button);
        newTaskButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(context, CreateTaskActivity.class));
            }
        });

        Calendar calendar =  Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long when = calendar.getTimeInMillis(); // Set first alarm service to 00:00
        int interval = 1000 * 60 * 60 * 24; // Every 24 hours

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, GetTaskService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when, interval, pendingIntent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        taskListAdapter.notifyDataSetChanged();
    }
}
