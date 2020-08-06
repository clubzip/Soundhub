package com.example.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login.Connection.RequestHttpConnection;

import org.json.JSONException;
import org.json.JSONObject;


public class CreateAccountActivity extends Activity {

    String url, id, email, pw, result, check = "0";
    EditText create_id, create_email, create_pw;
    ContentValues CreateContents = new ContentValues();

    MainActivity MA = (MainActivity) MainActivity.activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_createaccount);

        url = "http://192.168.0.102:3001/api/signup";

        create_id = findViewById(R.id.create_id);
        create_email = findViewById(R.id.create_email);
        create_pw = findViewById(R.id.create_pw);

    }

    public void mOnClose(View v){
        //데이터 전달하기

        id = create_id.getText().toString();
        email = create_email.getText().toString();
        pw = create_pw.getText().toString();
        url = "http://192.168.0.102:3001/api/signup";

        CreateContents.put("userid", id);
        CreateContents.put("email" , email);
        CreateContents.put("pw" , pw);

        CreateAccountActivity.CreateNetworkTask CreateTask = new CreateAccountActivity.CreateNetworkTask(url, CreateContents);
        CreateTask.execute();
    }

    private class CreateNetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        public CreateNetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
            result = requestHttpConnection.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                final JSONObject jObject = new JSONObject(s);

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            if (!jObject.getString("message").equals("succeeded")) {
                                Toast.makeText(CreateAccountActivity.this, "Your ID is not valid", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                });

                if (jObject.getString("message").equals("succeeded")) {

                    Intent intent = new Intent(CreateAccountActivity.this, ExploreActivity.class);

                    intent.putExtra("userid", id);
                    intent.putExtra("password", pw);
                    intent.putExtra("email", email);

                    startActivity(intent);
                    finish();
                    MA.finish();

                } else { create_id.setText(""); }

            }catch (Exception e) {e.printStackTrace();}

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

}
