package com.example.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class CreateSocialAccountActivity extends Activity {

    String url, url_google, url_facebook, social_id, id, email, type, result;
    EditText create_id, create_email;
    ContentValues CreateSocialContents = new ContentValues();
    MainActivity MA = (MainActivity) MainActivity.activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_createsocialaccount);

        url_facebook = "http://192.168.0.102:3001/api/signup/facebook";
        url_google = "http://192.168.0.102:3001/api/signup/google";

        create_id = findViewById(R.id.create_id);
        create_email = findViewById(R.id.create_email);

        Intent receivedIntent = getIntent();
        social_id = receivedIntent.getStringExtra("socialID");
        type = receivedIntent.getStringExtra("type");

        if (type.equals("facebookID")) {
            url = url_facebook;
        } else if (type.equals("googleID")) {
            url = url_google;
        }

    }

    public void mOnClose(View v){
        //데이터 전달하기

        id = create_id.getText().toString();
        email = create_email.getText().toString();

        CreateSocialContents.put("userid", id);
        CreateSocialContents.put("email" , email);
        CreateSocialAccountActivity.CreateSocialNetworkTask CreateSocialTask = new CreateSocialAccountActivity.CreateSocialNetworkTask(url, CreateSocialContents);
        CreateSocialTask.execute();
    }

    private class CreateSocialNetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public CreateSocialNetworkTask(String url, ContentValues values) {
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
                                Toast.makeText(CreateSocialAccountActivity.this, "Your ID is not valid", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                });

                if (jObject.getString("message").equals("succeeded")) {

                    Intent intent = new Intent(CreateSocialAccountActivity.this, ExploreActivity.class);

                    intent.putExtra(type, social_id);
                    intent.putExtra("userid", id);
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
