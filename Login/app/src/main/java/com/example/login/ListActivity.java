package com.example.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                PopupMenu popup = new PopupMenu(ListActivity.this, view);
                ListActivity.this.getMenuInflater().inflate(R.menu.menu_listview, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.play:
                                Intent intent = new Intent(ListActivity.this, PlayActivity.class);

                                ListViewCustomDTO dto = (ListViewCustomDTO) adapter.getItem(position);
                                String f_name = dto.getName();

                                intent.putExtra("filename", f_name);
                                startActivity(intent);

                                break;

                            case R.id.delete:
                                ListViewCustomDTO del_dto = (ListViewCustomDTO) adapter.getItem(position);

                                File file = new File(fileDir + del_dto.getName());
                                file.delete();

                                adapter.delItem(del_dto);
                                adapter.notifyDataSetChanged();

                                Toast.makeText(ListActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                break;

                            case R.id.upload:
                                ListViewCustomDTO dto_up = (ListViewCustomDTO) adapter.getItem(position);
                                String f_name_up = dto_up.getName();
                                upLoad2Server uls = (upLoad2Server) new upLoad2Server().execute(fileDir + f_name_up);
                                Toast.makeText(ListActivity.this, "업로드되었습니다.", Toast.LENGTH_SHORT).show();
                                break;

                        }

                        return false;
                    }
                });

                popup.show();
                return;
            }
        });


        listView.setAdapter(adapter);


    }
}
