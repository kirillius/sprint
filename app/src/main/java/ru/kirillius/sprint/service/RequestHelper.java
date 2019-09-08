package ru.kirillius.sprint.service;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.interfaces.OnCompleteRequest;
import ru.kirillius.sprint.models.EmptyResponse;

/**
 * Created by Lavrentev on 05.10.2017.
 */

public class RequestHelper {

    Context context;
    OnCompleteRequest listener;
    String request;

    public RequestHelper(Context context) {
        this.context = context;
    }

    public void executePostRequest(String method, String body, OnCompleteRequest listener) {
        this.listener = listener;
        request = "";//"{\"startRecord\":1,\"recordCount\":1000,\"orderBy\":\"ID\",\"orderDir\":\"ASC\",\"filters\":["+filters+"]}";
        new ServerConnection().execute(context.getResources().getString(R.string.url_backend) + method, "POST");
    }

    public void executeGetRequest(String method, HashMap<String, String> params, OnCompleteRequest listener) {
        this.listener = listener;
        if(params==null)
            params = new HashMap<>();

        try {
            params.putAll(CommonHelper.getAuth(context));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request = CommonHelper.mapToString(params);
        new ServerConnection().execute(context.getResources().getString(R.string.url_backend) + method, request, "GET");
    }

    private class ServerConnection extends AsyncTask<String, Void, String> {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            InputStream inputStream;

            try {
                urlConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert urlConnection != null;
                urlConnection.setRequestMethod(params[2]);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                //urlConnection.setRequestProperty("AUTHORIZATION", params[2]);
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);

                if(!params[2].equals("GET")) {
                    urlConnection.setDoOutput(true);

                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                    out.write(params[1]);
                    out.flush();
                    out.close();
                }

                System.out.println("response addr: "+params[0]);
                if(urlConnection.getResponseCode()==200)
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                else
                    inputStream = new BufferedInputStream(urlConnection.getErrorStream());

                String response = CommonHelper.convertInputStreamToString(inputStream);
                urlConnection.disconnect();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            System.out.println("response json: "+json);
            super.onPostExecute(json);

            if(json!=null)
                try {
                    /*EmptyResponse emptyResponse = gson.fromJson(json, EmptyResponse.class);
                    if(emptyResponse.errorCode==null) {
                        emptyResponse = new EmptyResponse("503", "Server unavailable");
                        listener.onComplete(gson.toJson(emptyResponse));
                    }*/
                    if(listener!=null)
                        listener.onComplete(json);
                }
                catch (IllegalStateException exp) {
                    exp.printStackTrace();
                }
            else {
                EmptyResponse emptyResponse = new EmptyResponse("503", "Server unavailable");
                listener.onComplete(gson.toJson(emptyResponse));
            }
        }
    }
}
