package il.ac.shenkar.octoid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

public class TasksBroadcastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Build notification
        Notification note = new Notification.Builder(context)
                .setContentTitle(intent.getStringExtra("task"))
                .setContentText("Task Reminder").setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        note.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(0, note);

    }
}

