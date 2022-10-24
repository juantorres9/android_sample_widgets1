package com.example.myactivitytests;




import android.content.Intent;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MainActivity2 extends AppCompatActivity implements Runnable{


    Button btn,b4;
    ProgressBar progress;
    TextView tv;

    Berechnung langeBerechnung;
    ArrayList<Integer> erg;
    boolean laeuftNoch;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn = (Button)findViewById(R.id.button);
        b4 = (Button)findViewById(R.id.button4);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        tv = (TextView) findViewById(R.id.textView);

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),ProgressActivity.class);
                startActivity(i);

            }
        });
    }



    @Override
    public void run() {
        int progressWert = 0;
        tv.setText("Berechnung gestartet.");
        while(laeuftNoch){
            progressWert = progressWert + 5;

            if(progressWert < 100){
                progress.setProgress(progressWert);
            }else {
                progressWert = 0;
                progress.setProgress(progressWert);
            }
        }
        tv.setText("Berechnung beendet.");
        progress.setProgress(100);
    }

    public void onClick(View v){
        handler.post(this);

        Callable<ArrayList<Integer>> call = new Berechnung();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ArrayList<Integer>> future = executor.submit(call);

        try {
            ArrayList<Integer> erg = future.get();
            int index = ((Berechnung)call).getIndex();
            int grenze = ((Berechnung)call).getGrenze();
            Log.d("App", "Die längste Folge bis " + grenze + " entsteht für die Eingabe " + index + " und hat eine Länge von " + erg.size() + ".");
            ((Berechnung)call).ausgabe(erg);
        } catch (Exception e) {
            // Nothing
        }
        laeuftNoch = false;
    }


}