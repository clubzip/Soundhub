package com.example.useretrofit2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SavePopupActivity extends Activity {

    private TextView textView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_savepopup);

        textView1 = (TextView) findViewById(R.id.editFileName);

        Button button = (Button) findViewById(R.id.FileNameSaveBtn);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newFileName = textView1.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("filename", newFileName);
                setResult(0, intent);

                finish();

            }
        });



    }
}
