package com.example.charlie.tickr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Stock extends AppCompatActivity {

    String price, stockName, amount, timeStamp, total;
    String dateAsString, totalAsString;
    String stockName1 = "MSFT", stockName2 = "APPL", stockName3 = "GOOG";
//    Date[] dates1 = new Date[10], dates2 = new Date[10], dates3 = new Date[10];
//    Float[] totals1 = new Float[10], totals2 = new Float[10], totals3 = new Float[10];
    Boolean fullArray =false;

    ArrayList<Date> dates1 = new ArrayList<Date>();
    ArrayList<Date> dates2 = new ArrayList<Date>();
    ArrayList<Date> dates3 = new ArrayList<Date>();
    ArrayList<Float> totals1 = new ArrayList<Float>();
    ArrayList<Float> totals2 = new ArrayList<Float>();
    ArrayList<Float> totals3 = new ArrayList<Float>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        getSupportActionBar().setTitle("Stock Value");

        final TextView SP = (TextView) findViewById(R.id.StockPrice);
        Button StockPrice = (Button) findViewById(R.id.FindStock);
        StockPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                stockName = "MSFT";
                Stocks s = new Stocks(stockName);

                new Thread(s).start();
                try {
                    //System.out.printf(s.returnAPIresponse("hello"));
                    double value = s.getStockPrice("MSFT");
                    Calendar calendar = Calendar.getInstance();
                    timeStamp = calendar.getTime()+"";
                    price = "";
                    if (value > 0.0) {
                        price = value + "";
                        Log.d("priceTAG", price);
                        SP.setText(price);

                    } else {
                        price = "INVALID API REQUEST CALL";
                    }

                    Log.d("OutPutTestingTagg",price);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final EditText ed = (EditText) findViewById(R.id.editText2);
                final TextView sp = (TextView) findViewById(R.id.StockDrop);
                String s1,s2;
                float n1,n2;
                s1=ed.getText().toString();
                s2=SP.getText().toString();
                amount = s2;
                n1=Float.parseFloat(s1);
                n2=Float.parseFloat(s2);
                Calculations c = new Calculations();

                total = c.calckNetValue(n2,n1)+"";
                sp.setText("You have "+total+" value of price");



//    public void testThis(){
//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Stock s = new Stock("MSFT");
//                try  {
//                    //System.out.printf(s.returnAPIresponse("hello"));
//                    double re = s.getStockPrice("MSFT");
//                    String price = "";
//                    if(re > 0.0){
//                        price = re+"";
//                    }else{
//                        price = "INVALID API REQUEST CALL";
//                    }
//
//                    Log.d("OutPutTestingTag", price);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//    }


                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                HashMap<String, String> transactionHistory = new HashMap<String, String>();
                HashMap<String, String> transaction = new HashMap<String, String>();

                transaction.put("amount", amount);
                transaction.put("name", stockName);
                transaction.put("price", price);
                transaction.put("time", timeStamp);
                transaction.put("total", total);
                transaction.put("type", "Stock");

                mDatabase.child("transactionHistory").push().setValue(transaction);
            }

        });


        //transactionHistory.push(transaction);

        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("transactionHistory");

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0, j=0, k=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    dateAsString = snapshot.child("time").getValue().toString();
                    Date theSameDate = new Date();
                    try{
                        theSameDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateAsString);

                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    Log.d("timeTAG", dateAsString);
                    totalAsString = snapshot.child("total").getValue().toString();
                    float theSameFloat = Float.valueOf(totalAsString);

                    if(snapshot.child("name").getValue().toString().equals(stockName1)){
                        dates1.add(theSameDate);
                        totals1.add(theSameFloat);
                    }
                    else if(snapshot.child("name").getValue().toString().equals(stockName2)){
                        dates2.add(theSameDate);
                        totals2.add(theSameFloat);
                    }
                    else if(snapshot.child("name").getValue().toString().equals(stockName3)){
                        dates2.add(theSameDate);
                        totals3.add(theSameFloat);
                    }
                    else
                        Log.d("invalidStockNameError", "ERROR: Invalid Stock Name");
                }

                //Graph params
                GraphView graph = (GraphView) findViewById(R.id.graph);
                graph.setTitle("Test Title");
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                graph.getGridLabelRenderer().setVerticalAxisTitle("Price");

                LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[] {

                        new DataPoint(dates1.get(0), totals1.get(0)),
                        new DataPoint(dates1.get(1), totals1.get(1)),
                        new DataPoint(dates1.get(2), totals1.get(2)),
                        new DataPoint(dates1.get(3), totals1.get(3)),
                        new DataPoint(dates1.get(4), totals1.get(4)),
//                        new DataPoint(dates1.get(5), totals1.get(5)),
//                        new DataPoint(dates1.get(6), totals1.get(6)),
//                        new DataPoint(dates1.get(7), totals1.get(7)),
//                        new DataPoint(dates1.get(8), totals1.get(8)),
//                        new DataPoint(dates1.get(9), totals1.get(9))

                });
                graph.addSeries(series1);
                series1.setDrawDataPoints(true);
                series1.setTitle("Test Title 1");
                series1.setColor(Color.BLACK);

                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {

//                        new DataPoint(dates2[0], totals2[0]),
//                        new DataPoint(dates2[1], totals2[1]),
//                        new DataPoint(dates2[2], totals2[2]),
//                        new DataPoint(dates2[3], totals2[3]),
//                        new DataPoint(dates2[4], totals2[4]),
//                        new DataPoint(dates2[6], totals2[6]),
//                        new DataPoint(dates2[7], totals2[7]),
//                        new DataPoint(dates2[8], totals2[8]),
//                        new DataPoint(dates2[9], totals2[9]),
//                        new DataPoint(dates2[10], totals2[10])

                });
                graph.addSeries(series2);
                series2.setDrawDataPoints(true);
                series2.setTitle("Test Title 2");
                series2.setColor(Color.RED);

                LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
//
//                        new DataPoint(dates3[0], totals3[0]),
//                        new DataPoint(dates3[1], totals3[2]),
//                        new DataPoint(dates3[2], totals3[2]),
//                        new DataPoint(dates3[3], totals3[3]),
//                        new DataPoint(dates3[4], totals3[4]),
//                        new DataPoint(dates3[6], totals3[6]),
//                        new DataPoint(dates3[7], totals3[7]),
//                        new DataPoint(dates3[8], totals3[8]),
//                        new DataPoint(dates3[9], totals3[9]),
//                        new DataPoint(dates3[10], totals3[10])

                });
                graph.addSeries(series3);
                series3.setDrawDataPoints(true);
                series3.setTitle("Test Title 3");
                series3.setColor(Color.GREEN);

                // set date label formatter
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
                graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 5 because of the space

                // set manual x bounds to have nice steps
                graph.getViewport().setMinX(dates1.get(0).getTime());
                graph.getViewport().setMaxX(dates1.get(5).getTime());
                graph.getViewport().setMinY(0.0);
                graph.getViewport().setMaxY(1000.0);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setScalable(false);
                graph.getViewport().setScrollable(true);

                // as we use dates as labels, the human rounding to nice readable numbers
                // is not necessary
                graph.getGridLabelRenderer().setHumanRounding(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.setting:
                Toast.makeText(this,"We are Student of GSU",Toast.LENGTH_LONG).show();
                break;
            case R.id.setting1:
                ToBank();


        }
        return super.onOptionsItemSelected(item);
    }

    private void ToBank() {
        startActivity(new Intent(getApplicationContext(),Bank.class));
    }


}



