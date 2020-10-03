package com.lab69.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.listviewid);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();


    }


    public ArrayList<File> findSong(File root) {
        ArrayList<File> at = new ArrayList<>();
        File[] files = root.listFiles();

            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    at.addAll(findSong(singleFile));
                }
                else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav") ||
                    singleFile.getName().endsWith(".m4a") || singleFile.getName().endsWith(".flac") ||
                            singleFile.getName().endsWith(".wma") || singleFile.getName().endsWith(".aac"))
                    {
                        at.add(singleFile);
                    }
                }
            }
        return at;
    }

    void display() {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());


            items = new String[ mySongs.size() ];
            for (int i = 0; i<mySongs.size(); i++) {
                //toast(mySongs.get(i).getName().toString());
                items[i] = mySongs.get(i).getName().toString()
                        .replace(".mp3", "").replace(".wav", "")
                        .replace(".m4a", "").replace(".flac", "")
                        .replace(".wma", "").replace(".aac", "");

            }

        ArrayAdapter<String> adp = new
                ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
        listview.setAdapter(adp);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songname=listview.getItemAtPosition(position).toString();
                startActivity(new Intent(MainActivity.this,MusicActivity.class)
                        .putExtra("pos",position).putExtra("songs",mySongs).putExtra("songname",songname));

            }
        });


    }

}
