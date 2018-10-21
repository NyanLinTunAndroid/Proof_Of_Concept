package com.poc.proof_of_concept;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button signOut;
    GoogleApiClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleSignInClient.connect();
        listView = (ListView) findViewById(R.id.list_view);
        if(Data.jobs.size() > 0){
            ListViewAdapter adapter = new ListViewAdapter(getApplicationContext(), Data.jobs);
            listView.setAdapter(adapter);
        }
        else {new LoadTask().execute();}
        signOut = (Button) findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        mGoogleSignInClient.disconnect();
                        finish();
                    }
                });
            }
        });
    }

    class LoadTask extends AsyncTask<String, String, Void> {
        private ProgressDialog pd = new ProgressDialog(MainActivity.this);
        InputStream inputStream = null;
        String result = "";

        @Override
        protected void onPreExecute() {
            pd.setMessage("Downlaoding...");
            pd.show();
            pd.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id, job_id;
                    String priority, company, address;
                    Double lat, lng;
                    JSONObject object = jsonObject.getJSONObject("geolocation");
                    id = jsonObject.getInt("id");
                    job_id = jsonObject.getInt("job-id");
                    priority = jsonObject.getString("priority");
                    company = jsonObject.getString("company");
                    address = jsonObject.getString("address");
                    lat = object.getDouble("latitude");
                    lng = object.getDouble("longitude");
                    Data.jobs.add(new Job(id, job_id, priority, company, address, lat, lng));
                }
                pd.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListViewAdapter adapter = new ListViewAdapter(getApplicationContext(), Data.jobs);
            listView.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(String... params) {
            String url_select = "https://api.myjson.com/bins/8d195.json";
            ArrayList<NameValuePair> param = new ArrayList<>();
            try{
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url_select);
                httpGet.setHeader("Content-Type", "application/json");
                httpGet.setHeader("Accept", "application/json");
                //HttpPost httpPost = new HttpPost(url_select);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                StatusLine statusLine = httpResponse.getStatusLine();
                int statusCode =statusLine.getStatusCode();
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
//                try {
//                    httpPost.setEntity(new UrlEncodedFormEntity(param));
//                    HttpResponse httpResponse = httpClient.execute(httpPost);
//                    HttpEntity httpEntity = httpResponse.getEntity();
//                    inputStream = httpEntity.getContent();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                inputStream.close();
                result = builder.toString();
                Log.i("RESULT", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
