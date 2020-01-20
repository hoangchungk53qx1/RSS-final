package com.bhsoft.rssfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bhsoft.rssfinal.Adapter.FeedAdapter;
import com.bhsoft.rssfinal.Common.HTTPDataHandler;
import com.bhsoft.rssfinal.Model.Item;
import com.bhsoft.rssfinal.Model.RSSObject;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
     RecyclerView recyclerView;
     Context context;

     private ArrayList<Item> arrayItem = new ArrayList<>();
     private FeedAdapter feedAdapter;
    public final String RSS_link = "https://vnexpress.net/rss/suc-khoe.rss";
    private final   String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" SỨC KHỎE ");

        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        feedAdapter = new FeedAdapter(arrayItem);
        recyclerView.setAdapter(feedAdapter);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Chờ chút ");
        mDialog.setCancelable(false);

        new loadRSSAsync().execute(RSS_to_Json_API+RSS_link);
    }


    private class loadRSSAsync extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
           // mDialog.isShowing();
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HTTPDataHandler http = new HTTPDataHandler();
            result = http.GetHTTPData(params[0]);
            return  result;
        }

        @Override
        protected void onPostExecute(String s) {
            RSSObject rssObject = new Gson().fromJson(s,RSSObject.class);

            arrayItem.clear();
            arrayItem.addAll(rssObject.getItems());
            feedAdapter.notifyDataSetChanged();
            if(mDialog.isShowing())

           // mDialog.isShowing();
            mDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
// sử dụng onOptionsItemSelected dùng để sử dụng Refresh lại danh sách các item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_refresh)
            new loadRSSAsync().execute(RSS_to_Json_API+RSS_link);
        return true;
    }
}


