package com.ticket.m.mticketapplicationintegration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class BookTicket extends AppCompatActivity {
    Button bookBtn;
    EditText sourceId,destinationId;
    int price;
    Date time_of_booking,expiryTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        bookBtn =findViewById(R.id.bookBtn);
        sourceId=findViewById(R.id.sourceId);
        destinationId=findViewById(R.id.destinationId);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                time_of_booking=calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR,1);
                expiryTime=calendar.getTime();
                Intent intent=new Intent(BookTicket.this,QrcodeGeneratorView.class);
                intent.putExtra("source",sourceId.getText().toString());//adding the values of sourceid  in the  intent hashmap object
                intent.putExtra("destination",destinationId.getText().toString());//adding the value of destinationid in intent hashmap object
                intent.putExtra("price",price);
                intent.putExtra("time_of_booking",time_of_booking);
                intent.putExtra("expiryTime",expiryTime);
                startActivity(intent);//starting the activity QrcodeGeneratorView class
            }
        });
    }
}
