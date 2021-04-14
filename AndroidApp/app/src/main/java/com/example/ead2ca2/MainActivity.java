package com.example.ead2ca2;

import android.os.Bundle;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import 	android.widget.AdapterView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.android.volley.*;

import android.content.Context;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get("https://eadca2api.azure-api.net/v1/all")
                .addHeaders("Host", "eadca2api.azure-api.net")
                .addHeaders("Ocp-Apim-Subscription-Key", "bf926c881ec745a590ba799d50736dfb")
                .addHeaders("Ocp-Apim-Trace", "true")
                .setTag("getall")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        myFunction(response);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }

    private void myFunction(@org.jetbrains.annotations.NotNull JSONArray jsonarr){

        final ListView listview = (ListView) findViewById(R.id.listview);
        String[] values = new String[] { };
        final ArrayList<String> list = new ArrayList<String>();
        if(!jsonarr.isNull(0)) {
            for (int i = 0; i < jsonarr.length(); i++) {
                Log.d("catttt", jsonarr.opt(i).toString());
                list.add(jsonarr.optJSONObject(i).opt("username").toString());
            }
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }


    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}