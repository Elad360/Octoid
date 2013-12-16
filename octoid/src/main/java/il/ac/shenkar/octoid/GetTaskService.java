package il.ac.shenkar.octoid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


public class GetTaskService extends IntentService
{

    public GetTaskService() {super("GetTaskService");}

    @Override
    protected void onHandleIntent(Intent intent)
    {
        TasksWebManager webManager = new TasksWebManager("http://mobile1-tasks-dispatcher.herokuapp.com/task/random");
        String taskDescription = webManager.pullTask();

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification note = new Notification.Builder(this)
                .setContentTitle("Added Task: " + taskDescription)
                .setContentText("Task Reminder").setSmallIcon(R.drawable.icon)
                .setContentIntent(contentIntent).build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        note.defaults = Notification.DEFAULT_SOUND;


        if (!taskDescription.isEmpty())
        {
            TaskListModel.getInstance(this).pushTask(taskDescription);
            notificationManager.notify(0, note);
        }
    }
}
