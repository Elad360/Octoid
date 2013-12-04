package il.ac.shenkar.octoid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
    }

    @Override
    public void onResume()
    {
        super.onResume();
        taskListAdapter.notifyDataSetChanged();
    }
}
