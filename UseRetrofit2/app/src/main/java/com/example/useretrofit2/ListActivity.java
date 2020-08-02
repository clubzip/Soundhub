package com.example.useretrofit2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;

public class ListActivity extends Activity {

    private ListView listView;
    private ListViewCustomAdapter adapter;
    String filePath;
    String fileDir;
    String AppName = "AppName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fileDir = "/sdcard/" + AppName + "/";

        listView = (ListView) findViewById(R.id.lv_main);
        adapter = new ListViewCustomAdapter();

        File f = new File(fileDir);
        File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.US).endsWith(".mp4");//확장자

            }
        });

        for(int i=0;i<files.length;i++){
            ListViewCustomDTO dto = new ListViewCustomDTO();
            dto.setName(files[i].getName());
            adapter.addItem(dto);

        }

        listView.setAdapter(adapter);


    }
}
