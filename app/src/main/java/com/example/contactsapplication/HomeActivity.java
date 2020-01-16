package com.example.contactsapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements DataAdapter.IDataAdapterCallBack {

    Button btn_fetch, btn_clear;
    RecyclerView rv_data;
    TextView tv_msg;
    ImageView iv_back;
    DbCreation db;
    DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = new DbCreation(this);
        initiateViews();
        events();
        setData();

    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();

            }
        });
        btn_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();


            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAllContact();
                setData();

            }
        });

    }

    private void fetchData() {
        String noOfRecords = db.recordsNumber();
        if (!noOfRecords.equalsIgnoreCase("0")) {

            Toast.makeText(this, "Data already have been fetched. Clear the data. ", Toast.LENGTH_SHORT).show();

        } else {
            new GetContacts().execute();


        }

    }

    private void exitDialog() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_exit);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            dialog.setCancelable(false);


            Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
            Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);


            btn_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                }
            });

            btn_OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                    System.exit(0);
                }
            });


            dialog.show();
        } catch (Exception e) {
            Log.d("Dialog Exception: ", e.toString());
        }
    }

    private void initiateViews() {
        btn_fetch = (Button) findViewById(R.id.btn_fetch);
        rv_data = (RecyclerView) findViewById(R.id.rv_data);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_clear = (Button) findViewById(R.id.btn_clear);
    }

    @Override
    public void clickedDataName(String name) {
        db.deleteContact(name);
        setData();

    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {
        String jsonStr = "";
        private ProgressDialog pDialog;
        private String url = "https://api.androidhive.info/json/contacts.json";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(url);

            Log.e("Response from url: ", " " + jsonStr);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();


            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);


                        String name = c.getString("name");
                        String image = c.getString("image");
                        String mobile = c.getString("phone");
                        db.addContact(name, image, mobile);


                    }

                    setData();

                } catch (final JSONException e) {
                    Log.e("Json parsing error: ", " " + e.getMessage());


                }
            } else {
                Log.e("Error : ", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check Internet",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

        }

    }

    private void setData() {
        ArrayList<ContactsPOJO> al_contacts = new ArrayList<>();
        al_contacts = db.getContacts();
        if (al_contacts.size() == 0) {
            rv_data.setVisibility(View.GONE);
            tv_msg.setVisibility(View.VISIBLE);
        } else {
            rv_data.setVisibility(View.VISIBLE);
            tv_msg.setVisibility(View.GONE);
        }
        adapter = new DataAdapter(this, al_contacts, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false);
        rv_data.setLayoutManager(mLayoutManager);
        rv_data.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        rv_data.setNestedScrollingEnabled(false);
    }
}
