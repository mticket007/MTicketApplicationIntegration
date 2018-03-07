package com.ticket.m.mticketapplicationintegration;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;


public class QrcodeGeneratorView extends AppCompatActivity {

    private ImageView Qrcodeid;
    public final static int QrcodeWidth=500;
    private Bitmap bitmap;
    private String QrcodeValue;
    private TextView errorTv;
    String theValue;

    //size of the Bitmap
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator_view);
        //instantiating the com.ticket.m.mticketapplicationintegration.QrDBHelper class


        String sourceStation=getIntent().getStringExtra("source");
        String destinationStation=getIntent().getStringExtra("destination");
        int price=getIntent().getIntExtra("price",0);
        String bookingTime,expiry_time;
        bookingTime=getIntent().getStringExtra("bookingTime");
        expiry_time=getIntent().getStringExtra("expiry_time");
        Qrcodeid=findViewById(R.id.Qrcodeid);
        //errorTv instantiating
        errorTv=findViewById(R.id.errorTv);
        QrcodeValue="{\"sourceStation\":"+sourceStation+",\"destinationStation\":"+destinationStation+",\"fair\":"+price+",\"bookingTime\":"+bookingTime+",\"expiry_time\":"+expiry_time+"}";
        System.out.println("size of string is "+QrcodeValue.length());
        try{
            System.out.println("code is executed before conversion jsonObject");
            //error is in this part while converting from string to the json
            JSONObject jsonObject=new JSONObject(QrcodeValue);
            theValue=jsonObject.toString();

        }
        catch(JSONException e)
        {

          e.printStackTrace();
            System.out.println("before exception"+theValue.charAt(85));

        }
        // current date of the generation of the QRCODE and Random number
                try {
                    bitmap = TextToImageEncode(theValue);
                   /* QrDBHelper qrDBHelper=new QrDBHelper(this);
                    SQLiteDatabase db=qrDBHelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    size=bitmap.getByteCount();
                    ByteBuffer buffer=ByteBuffer.allocate(size);
                    bitmap.copyPixelsToBuffer(buffer);
                    byte[] array=buffer.array();
                    System.out.println(size+"length of the array is "+buffer.array());
                    values.put("Qrcode",array);
                    db.insert("QrCode",null,values);*/
                   //instantiating QrDBHelper class
                    QrDBHelper qrDBHelper=new QrDBHelper(this);
                   //checking if database is available
                    SQLiteDatabase dbR = qrDBHelper.getReadableDatabase();
                    Cursor  c=dbR.rawQuery("SELECT * FROM QrCode",null);
                    if(c.moveToFirst()) {
                        //reading blob data from sqlite db
                        try {
                            String[] column = {"Qrcode"};
                            SQLiteDatabase dbReading = qrDBHelper.getReadableDatabase();
                            Cursor dbCursor = dbReading.rawQuery("select Qrcode from QrCode where Id=1",null);
                            dbCursor.moveToFirst();
                            int i = dbCursor.getColumnIndex("Qrcode");
                            System.out.println("index value is " + i);
                            byte[] byteArray = dbCursor.getBlob(dbCursor.getColumnIndex("Qrcode"));
                            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            /*ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
                            Bitmap bm=BitmapFactory.decodeStream(byteArrayInputStream);*/
                            Qrcodeid.setImageBitmap(bm);
                            Toast.makeText(this, "Retrieving executed", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            System.out.println("error is " + e);
                        }
                    }
                    else {
                        Qrcodeid.setImageBitmap(bitmap);
                        SQLiteDatabase db = qrDBHelper.getWritableDatabase();
                        //getting an image file from the Qrcodeid image view and converting it to the drawable image adding it to the db
                        Bitmap bt = ((BitmapDrawable) Qrcodeid.getDrawable()).getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bt.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] img = byteArrayOutputStream.toByteArray();
                        ContentValues values = new ContentValues();
                        values.put("Id",1);
                        values.put("Qrcode", img);
                        db.insert("QrCode", null, values);


                    }

                }

                catch (WriterException e)
                {
                    e.printStackTrace();
                }
    }
    //
    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try
        {
            bitMatrix=new MultiFormatWriter().encode(Value, BarcodeFormat.DATA_MATRIX.QR_CODE, QrcodeWidth,QrcodeWidth,null);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
        int bitMatrixWidth=bitMatrix.getWidth();
        int bitMatrixHeight=bitMatrix.getHeight();
        int[] pixels=new int[bitMatrixWidth*bitMatrixHeight];
        for(int y=0;y<bitMatrixHeight;y++)
        {
            int offset=y*bitMatrixWidth;
            for(int x=0;x<bitMatrixWidth;x++)
            {
                pixels[x+offset]=bitMatrix.get(x,y)?getResources().getColor(R.color.blackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap  bitmap=Bitmap.createBitmap(bitMatrixWidth,bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels,0,500,0,0,bitMatrixWidth,bitMatrixHeight);
        return bitmap;
    }

    }

