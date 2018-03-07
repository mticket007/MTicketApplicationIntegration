package com.ticket.m.mticketapplicationintegration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
                //converting it to the date-month-year format and converting the values into string format
                DateFormat dateFormat=new SimpleDateFormat("dd-MMM-yyyy");
                String bookingTime=dateFormat.format(time_of_booking);
                String expriy_time=dateFormat.format(expiryTime);
                Intent intent=new Intent(BookTicket.this,QrcodeGeneratorView.class);
                intent.putExtra("source",sourceId.getText().toString());//adding the values of sourceid  in the  intent hashmap object
                intent.putExtra("destination",destinationId.getText().toString());//adding the value of destinationid in intent hashmap object
                intent.putExtra("price",price);
                intent.putExtra("bookingTime",bookingTime);
                intent.putExtra("expiry_time",expriy_time);
                startActivity(intent);//starting the activity QrcodeGeneratorView class
            }
        });
    }
}
