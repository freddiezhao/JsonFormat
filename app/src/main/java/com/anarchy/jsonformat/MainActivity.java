package com.anarchy.jsonformat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void show(View view){
        try {
            InputStream in =  getAssets().open("json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String temp;
            while ((temp =reader.readLine())!=null){
                buffer.append(temp);
            }
            JsonFormatDialogFragment.newInstance(buffer.toString()).show(getSupportFragmentManager(),"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
