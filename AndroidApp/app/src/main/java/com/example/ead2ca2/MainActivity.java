package com.example.ead2ca2;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import 	android.widget.AdapterView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());
        getAll();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("Clicked?", "Yes");
                setContentView(R.layout.activity_search);
                Button btn = (Button) findViewById(R.id.searchButton);
                btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String query = ((EditText) findViewById(R.id.queryEditText)).getText().toString();
                        getQuery(query);
                        setContentView(R.layout.activity_main);
                    }
                });
            }
        });

    }

    private void getQuery(String query){
        AndroidNetworking.get("https://eadca2api.azure-api.net/v1/search/"+query)
                .addHeaders("Host", "eadca2api.azure-api.net")
                .addHeaders("Ocp-Apim-Subscription-Key", "bf926c881ec745a590ba799d50736dfb")
                .addHeaders("Ocp-Apim-Trace", "true")
                .setTag("getall")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        displayListView(response);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getAll(){
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
                        displayListView(response);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getOne(String id){
        AndroidNetworking.get("https://eadca2api.azure-api.net/v1/person/" + id)
                .addHeaders("Host", "eadca2api.azure-api.net")
                .addHeaders("Ocp-Apim-Subscription-Key", "bf926c881ec745a590ba799d50736dfb")
                .addHeaders("Ocp-Apim-Trace", "true")
                .setTag("getone")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        singleClicked(response);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void displayListView(@org.jetbrains.annotations.NotNull JSONArray jsonarr){

        final ListView listview = (ListView) findViewById(R.id.listview);
        final ArrayList<String> list = new ArrayList<String>();
        if(!jsonarr.isNull(0)) {
            for (int i = 0; i < jsonarr.length(); i++) {
                list.add(jsonarr.optJSONObject(i).opt("username").toString()+" #"+jsonarr.optJSONObject(i).opt("personId").toString());
            }
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        final boolean[] clickable = {true};
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if(clickable[0]) {
                        JSONObject item = jsonarr.optJSONObject(position);
                        clickable[0] = false;
                        singleClicked(item);
                    }
                }
            });
        }

    private void singleClicked(@NotNull JSONObject in) {

        final ListView singleview = (ListView) findViewById(R.id.listview);

            final ArrayList<String> l = new ArrayList<String>();
            l.add(in.opt("username").toString()+ " #" + in.opt("personId").toString());
            l.add(in.optJSONObject("bitcoinWalletAddress").opt("cryptocurrency").toString() + " "+getString(R.string.address)+": " + in.optJSONObject("bitcoinWalletAddress").opt("walletAddress").toString() + " "+getString(R.string.amount)+": " + in.optJSONObject("bitcoinWalletAddress").opt("amount").toString());
            l.add(in.optJSONObject("etheriumWalletAddress").opt("cryptocurrency").toString() + " "+getString(R.string.address)+": " + in.optJSONObject("etheriumWalletAddress").opt("walletAddress").toString() +" "+getString(R.string.amount)+": " + in.optJSONObject("etheriumWalletAddress").opt("amount").toString());
            l.add(in.optJSONObject("cardanoWalletAddress").opt("cryptocurrency").toString() + " "+getString(R.string.address)+": " + in.optJSONObject("cardanoWalletAddress").opt("walletAddress").toString() + " "+getString(R.string.amount)+": " + in.optJSONObject("cardanoWalletAddress").opt("amount").toString());
            l.add(in.optJSONObject("binanceWalletAddress").opt("cryptocurrency").toString() + " "+getString(R.string.address)+": " + in.optJSONObject("binanceWalletAddress").opt("walletAddress").toString()  + " "+getString(R.string.amount)+": " + in.optJSONObject("binanceWalletAddress").opt("amount").toString());
            l.add(in.optJSONObject("litecoinWalletAddress").opt("cryptocurrency").toString() + " "+getString(R.string.address)+": " + in.optJSONObject("litecoinWalletAddress").opt("walletAddress").toString()  + " "+getString(R.string.amount)+": " + in.optJSONObject("litecoinWalletAddress").opt("amount").toString());

            final StableArrayAdapter a = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, l);
            singleview.setAdapter(a);
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


}