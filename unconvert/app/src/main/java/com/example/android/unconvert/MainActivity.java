package com.example.android.unconvert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String s=editText.getText().toString();
//                int kg=Integer.parseInt(s);
//                double pound=2.205*kg;
//                textView.setText("the corressponding value in pounds is"+pound);
//            }
//        });
    }
    public void calculate(View view)
    {
        String s=editText.getText().toString();
                int kg=Integer.parseInt(s);
                double pound=2.205*kg;
                textView.setText("the corressponding value in pounds is"+pound);
    }






}