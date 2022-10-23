package com.example.myactivitytests;


import android.provider.Settings;
        import android.telecom.Call;
        import android.util.Log;

        import java.util.ArrayList;
        import java.util.concurrent.Callable;
        import java.util.concurrent.Future;

public class Berechnung implements Callable<ArrayList<Integer>> {

    ArrayList<Integer> list = new ArrayList<Integer>();
    ArrayList<ArrayList<Integer>> all = new ArrayList<ArrayList<Integer>>();
    int index;
    int grenze;

    public ArrayList<Integer> call(){
        all = new ArrayList<ArrayList<Integer>>();
        all.add(0, new ArrayList<Integer>());
        Log.d("App", "HEY WE START MAIN BEFORE bocule all = new ArrayList<ArrayList<Integer>>() is size == "+all.size());


        grenze = 5;

        for(int i=1; i <= grenze; i++){
            Log.d("App", "MAIN Boucle interieur (" + i + ")"+" hey ArrayList<ArrayList<Integer>> all size AVANT vaut="+all.size());

            list = new ArrayList<Integer>();
            all.add(i, collatz(i));
        }

        int max = all.get(1).size();
        index = 1;
        Log.d("App", "HEY WE END MAIN bocule all = new ArrayList<ArrayList<Integer>>() is size == "+all.size());
        Log.d("App","THE MAX VAUT  int max = all.get(1).size()="+max );
        for(int i=2; i <= grenze; i++){
            if(all.get(i).size() > max){
                Log.d("App","THE MAX VALUE"+max+" has been OVERPASSED BY INDEX ="+i+"THE NEW MAX IS="+all.get(i).size() );

                max = all.get(i).size();
                index = i;
            }
        }


        return all.get(index);
    }


    public ArrayList<Integer> collatz(int n){
        list.add(n);
        Log.d("App", "collatz("+n+") first called with size of list="+list.size() );
        if(n > 1) {
            Log.d("App", "Condition interieur n>1 au moins 2 car n==" + n );
            if (n % 2 == 0) {
                n = n/2;
                Log.d("App", "n est PAIR donc faire n=n/2=" +" RETURN func collatz(n="+n+" )" );
                return collatz(n);
            } else
            {
                Log.d("App", "n est IMPAIR n==" + n );
                n = 3*n + 1;
                Log.d("App", "n est IMPAIR donc faire n=3*n+1=" +" RETURN func collatz(n="+n+" )" );
                return collatz(n);
            }
        }else{
            Log.d("App", "Condition interieur n<=1  car n==" + n );
            Log.d("App", "RETURN REAL list avec taille list.size()="+list.size() );
            for (int t :list){
                System.out.println("le contenu de la list avant destruction est ="+t);
            }

            return list;
        }
    }

    public int getIndex(){
        return index;
    }

    public int getGrenze(){
        return grenze;
    }

    public void ausgabe(ArrayList<Integer> l){
        for(int i=0; i < l.size(); i++){
            Log.d("App", (i+1) + ".)\t" + l.get(i) + "");
        }
    }

}