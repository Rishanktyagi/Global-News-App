package com.example.android.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        ArrayList<Word> words=new ArrayList<Word>();
        words.add(new Word("one","lutti"));
        words.add(new Word("two","ythh"));
        words.add(new Word("three","rfgdf"));
        words.add(new Word("four","rgrr"));
        words.add(new Word("five","wrter"));
        words.add(new Word("six","ioop"));
        words.add(new Word("seven","sfbhn"));
        words.add(new Word("eigth","lhnn"));
        words.add(new Word("nine","lumm"));
        words.add(new Word("ten","luee"));


        WordAdapter Adapter=new WordAdapter(this,words);
        ListView listview=(ListView)  findViewById(R.id.list);
        listview.setAdapter(Adapter);

    }
}