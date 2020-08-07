package com.example.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Base64;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.Connection.RequestHttpConnection;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Button btn_facebook_login, btn_login;
    private CallbackManager mCallbackManager;
    String facebook_id, result, url, google_id;
    EditText etEmail, etPW;
    String google = "0", facebook = "0";
    ContentValues LoginContents = new ContentValues();
    TextView btn_create;
    public LoginManager loginManager;
    static public Context context;
    public static Activity activity;

    String id,password;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "Oauth2Google";

    public GoogleSignInClient mGoogleSignInClient;
    private String AppName = "AppName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        activity = MainActivity.this;

        tedPermission();

        startService(new Intent(this,ForcedTerminationService.class));
        url = "http://192.168.0.112:3001/api/signin";

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPW = (EditText) findViewById(R.id.etPassword);

        mCallbackManager = CallbackManager.Factory.create();

        btn_facebook_login = (Button) findViewById(R.id.btn_facebook_login);
        btn_facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginManager = LoginManager.getInstance();
                loginManager.logInWithReadPermissions(MainActivity.this,
                        Arrays.asList("public_profile", "email"));
                loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                    }
                    @Override
                    public void onCancel() {
                    }
                    @Override
                    public void onError(FacebookException error) {
                    }
                });
            }
        });

        btn_create = (TextView) findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        btn_login = (Button) findViewById(R.id.btnLogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = etEmail.getText().toString();
                password = etPW.getText().toString();
                LoginContents.clear();
                LoginContents.put("userid", id);
                LoginContents.put("password",password);

                MainActivity.LoginNetworkTask LoginTask = new MainActivity.LoginNetworkTask(url, LoginContents);
                LoginTask.execute();
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        findViewById(R.id.btn_google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    /**
        @Override
        protected void onStart() {
            super.onStart();
            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                google = "1";
                google_id = account.getId();
                LoginContents.put("googleID", google_id);
                MainActivity.LoginNetworkTask LoginTask = new MainActivity.LoginNetworkTask(url, LoginContents);
                LoginTask.execute();
            }
        }
    **/


    private void signIn() {
        // Build a GoogleSignInClient with the options specified by gso.
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(GoogleSignInAccount account) {
        LoginContents.clear();
        LoginContents.put("googleID", account.getId());
        google="1";
        MainActivity.LoginNetworkTask LoginTask = new MainActivity.LoginNetworkTask(url, LoginContents);
        LoginTask.execute();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            System.out.println("handleSignInResult");
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken==null) {
                Toast.makeText(MainActivity.this,"User Logged Out",Toast.LENGTH_SHORT).show();
            } else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    facebook_id = object.getString("id");
                    System.out.println(facebook_id);

                    facebook = "1";
                    LoginContents.clear();
                    LoginContents.put("facebookID", facebook_id);
                    MainActivity.LoginNetworkTask LoginTask = new MainActivity.LoginNetworkTask(url, LoginContents);
                    LoginTask.execute();

                } catch (JSONException e) { e.printStackTrace(); }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private class LoginNetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public LoginNetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
            result = requestHttpConnection.request(url, values);
            System.out.println("doInbackground");
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jObject = new JSONObject(s);

                System.out.println("message : " + jObject.getString("message"));
                System.out.println("facebook : " + facebook);
                System.out.println("google : " + google);


                if ( (!jObject.getString("message").equals("succeeded")) && facebook.equals("1") ) {
                    facebook = "0";
                    google = "0";

                    Intent intent = new Intent(MainActivity.this, CreateSocialAccountActivity.class);
                    intent.putExtra("facebookID", facebook_id);
                    intent.putExtra("type","facebookID");
                    startActivity(intent);
                } else if ( (!jObject.getString("message").equals("succeeded")) && google.equals("1") ) {
                    facebook = "0";
                    google = "0";

                    Intent intent = new Intent(MainActivity.this, CreateSocialAccountActivity.class);
                    intent.putExtra("googleID", google_id);
                    intent.putExtra("type","googleID");
                    startActivity(intent);
                } else if ((!jObject.getString("message").equals("succeeded"))) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, "Check your ID and Password", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this,"User Logged In",Toast.LENGTH_SHORT).show();                        }
                    });
                    Intent intent = new Intent(MainActivity.this, ExploreActivity.class);
                    intent.putExtra("userid",jObject.getString("userid"));
                    intent.putExtra("email", jObject.getString("email"));
                    startActivity(intent);
                    finish();
                }

            }catch (Exception e) {e.printStackTrace();}
        }
    }


    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                SharedPreferences pref = getApplicationContext().getSharedPreferences("checkFirst", getApplicationContext().MODE_PRIVATE);
                boolean checkFirst = pref.getBoolean("checkFirst", false);
                if(checkFirst == false){//앱 최초 실행시
                    //System.out.println("First run @@@@@@@@@@@@@@@@@@@@@#$%#$^@^#$%@#$^@^#");
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("checkFirst",true);
                    editor.commit();
                    File dir = new File("/sdcard/" + AppName);

                    if(!dir.exists()) {
                        //System.out.println("Try mkdirs @@@@@@@@@@@@@@@@@@@@@#$%#$^@^#$%@#$^@^#");
                        dir.mkdirs();
                    }
                    //else System.out.println("Already exist @@@@@@@@@@@@@@@@@@@@@#$%#$^@^#$%@#$^@^#");
                }

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
                .check();


    }
}