package il.ac.shenkar.octoid;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TasksWebManager
{
    private String urlStr;

    public TasksWebManager(String url)
    {
        this.urlStr = url;
    }

    public String pullTask()
    {
        Task task = null;
        HttpURLConnection urlConnection = null;
        URL url;

        try
        {
            url = new URL(urlStr);
        }
        catch (MalformedURLException e)
        {
            return "";
        }

        if (url != null)
        {
            try
            {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader inReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inReader);
                StringBuilder responseBuilder = new StringBuilder();
                for (String line=bufferedReader.readLine(); line!=null; line=bufferedReader.readLine())
                {
                    responseBuilder.append(line);
                }
                String response = responseBuilder.toString();

                JSONObject jsonResponse = new JSONObject(response);
                String description = jsonResponse.getString("description");
                long id = jsonResponse.getLong("id");
                task = new Task(id, description);
                return task.getDescription();
            }
            catch (IOException e)
            {
                return "";
            }
            catch (JSONException e)
            {
                return "";
            }
            finally
            {
                urlConnection.disconnect();
            }

        }

        return "";
    }
}
