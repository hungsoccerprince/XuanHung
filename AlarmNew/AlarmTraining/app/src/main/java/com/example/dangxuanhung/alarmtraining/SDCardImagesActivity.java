package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Dang Xuan Hung on 31/03/2016.
 */
public class SDCardImagesActivity extends Activity {

    private static final String TAG = SDCardImagesActivity.class.getSimpleName() ;
    /**
     * Cursor used to access the results from querying for images on the SD card.
     */
    ImageView imgView;
    private Button btn1,btn2, btn3, btn4 ;
    private TextView tvReply,tvQuestion;
    private LinearLayout lnPoint,lnGame;
    private Cursor cursor_img;
    private String answer = null,name_image = null,date_image=null,fake_date=null;
    private int point=0;
    /*
     * Column index for the Thumbnails Image IDs.
     */
    private int columnIndex;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_layout);

        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn3=(Button)findViewById(R.id.btn3);
        btn4=(Button)findViewById(R.id.btn4);
        tvReply =(TextView)findViewById(R.id.tvReply);
        imgView = (ImageView) findViewById(R.id.imgView);
        tvReply =(TextView)findViewById(R.id.tvReply);
        lnPoint = (LinearLayout)findViewById(R.id.lnPoint);
        tvQuestion = (TextView)findViewById(R.id.tvQuestion);
        lnGame = (LinearLayout)findViewById(R.id.lnGame);

        imgView.setVisibility(View.VISIBLE);

        getPoint_image();

        final Handler handler=new Handler();
        // Set up an array of the Thumbnail Image ID column we want
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN
        };
        // Create the cursor_img pointing to the SDCard
        cursor_img = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                null,       // Return all rows
                null,
                MediaStore.Images.Media.BUCKET_ID);
        // Get the column index of the Thumbnails Image ID

        Log.d("ImageSize", String.valueOf(cursor_img.getCount()));

        if (cursor_img.getCount() > 0) {

            Random rand = new Random();
            int imgRand = rand.nextInt(cursor_img.getCount() - 1);
            cursor_img.moveToPosition(imgRand);

            int imageID = cursor_img.getInt(cursor_img.getColumnIndex(MediaStore.Images.Media._ID));
            Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(imageID));
            imgView.setImageURI(uri);

            String dateTaken = cursor_img.getString(cursor_img.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
            Uri filePathUri = Uri.parse(cursor_img.getString(cursor_img.getColumnIndex(MediaStore.Images.Media.DATA)));
            name_image = filePathUri.getLastPathSegment();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(dateTaken));
            Date time = calendar.getTime();
            date_image = formatTime(time);
            fake_date = fakeFormatTime(time);
            Log.d("Time", date_image);
            Log.d(TAG, "name : "+ name_image);

            int rand_question = rand.nextInt(3)+1;
            switch (rand_question){
                case 1:
                    tvQuestion.setText(R.string.question_image_name);
                    answer = name_image;
                    getAnswer_Image(answer, date_image,fake_date);
                    checkAnswer(handler,savedInstanceState);
                    break;
                case 3:
                    tvQuestion.setText(R.string.question_image_name);
                    answer = name_image;
                    getAnswer_Image(answer, date_image,fake_date);
                    checkAnswer(handler,savedInstanceState);
                    break;
                case 2:
                    tvQuestion.setText(R.string.question_image_time);
                    answer = date_image;
                    getAnswer_Image(answer,name_image,fake_date);
                    checkAnswer(handler,savedInstanceState);
                    break;
                case 4:
                    tvQuestion.setText(R.string.question_image_time);
                    answer = date_image;
                    getAnswer_Image(answer,name_image,fake_date);
                    checkAnswer(handler,savedInstanceState);
                    break;
            }

        }

        else{
            lnGame.setVisibility(View.GONE);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Alarm Music");

            alertDialogBuilder
                    .setMessage("Your photo gallery is empty! \n Take pictures to use application!")
                    .setCancelable(false)
                    .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.dismiss();
                            Intent i_setAlarm = new Intent(SDCardImagesActivity.this,SetAlarmService.class);
                            i_setAlarm.putExtra("next",1);
                            startService(i_setAlarm);
                            Intent intent_stop = new Intent(SDCardImagesActivity.this, RingtonePlayingService.class);
                            intent_stop.putExtra("extra", "off");
                            startService(intent_stop);
                            SDCardImagesActivity.this.finish();

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    private void checkAnswer(final Handler handler, final Bundle savedInstanceState ){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn1.getText().equals(answer))
                {
                    point += 1;
                    getPoint_image();
                    if(getPoint_image()){
                        tvReply.setText(R.string.complete);
                        tvReply.setTextColor(Color.BLUE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    else {
                        onCreate(savedInstanceState );

                    }
                }
                else {
                    onCreate(savedInstanceState );
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn2.getText().equals(answer))
                {
                    point += 1;
                    getPoint_image();
                    if(getPoint_image()){
                        tvReply.setText(R.string.complete);
                        tvReply.setTextColor(Color.BLUE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    else {
                        onCreate(savedInstanceState );
                    }
                }
                else {
                    onCreate(savedInstanceState );
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn3.getText().equals(answer))
                {
                    point += 1;
                    getPoint_image();
                    if(getPoint_image()){
                        tvReply.setText(R.string.complete);
                        tvReply.setTextColor(Color.BLUE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    else {
                        onCreate(savedInstanceState );
                    }
                }
                else {
                    onCreate(savedInstanceState );
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn4.getText().equals(answer))
                {
                    point += 1;
                    getPoint_image();
                    if(getPoint_image()){
                        tvReply.setText(R.string.complete);
                        tvReply.setTextColor(Color.BLUE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    }
                    else {
                        onCreate(savedInstanceState );
                    }
                }
                else {
                    onCreate(savedInstanceState );
                }
            }
        });



    }

    private void getAnswer_Image(String answer,String not_answer,String date_fake){
        Random rand = new Random();
        int rand_button = rand.nextInt(3)+1;

        if(rand_button==1){
            btn1.setText(answer);
            btn2.setText(not_answer);
            btn3.setText("Next");
            btn4.setText(date_fake);
        }

        if(rand_button==2){
            btn2.setText(answer);
            btn3.setText(not_answer);
            btn4.setText("Next");
            btn1.setText(date_fake);
        }

        if(rand_button==3){
            btn3.setText(answer);
            btn4.setText(not_answer);
            btn1.setText("Next");
            btn2.setText(date_fake);
        }

        if(rand_button==4){
            btn4.setText(answer);
            btn1.setText(not_answer);
            btn2.setText("Next");
            btn3.setText(date_fake);
        }
    }


    private boolean getPoint_image(){
        Boolean check = false;

        switch (point){
            case 3:
                Intent i_setAlarm = new Intent(this,SetAlarmService.class);
                i_setAlarm.putExtra("next",1);
                Intent intent_stop = new Intent(SDCardImagesActivity.this, RingtonePlayingService.class);
                intent_stop.putExtra("extra", "off");
                startService(intent_stop);
                startService(i_setAlarm);
                Log.d(TAG, String.valueOf(point));
                check = true;
                break;
            case 2:
                lnPoint.setBackgroundResource(R.drawable.point_3);
                Log.d(TAG, String.valueOf(point));
                break;
            case 1:
                lnPoint.setBackgroundResource(R.drawable.point_2);
                Log.d(TAG, String.valueOf(point));
                break;
            case 0:
                lnPoint.setBackgroundResource(R.drawable.point_1);
                break;
        }
        return check;
    }



    public String formatTime(Date time) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String timeFormat = df.format(time);
        return timeFormat;
    }

    public String fakeFormatTime(Date time) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String timeFormat = df.format(time);
        return timeFormat;
    }


    @Override
    public void onBackPressed() {
        return ;
    }

}






/*
    private class ImageAdapter extends BaseAdapter {
        private Context context;
        public ImageAdapter(Context localContext) {
            context = localContext;
        }
        public int getCount() {
            return cursor_img.getCount();
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                cursor_img.moveToPosition(position);
                // Get the current value for the requested column
                int imageID = cursor_img.getInt(columnIndex);
                // Set the content of the image based on the provided URI
                picturesView.setImageURI(Uri.withAppendedPath(
                        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID));
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView.setPadding(8, 8, 8, 8);
                picturesView.setLayoutParams(new GridView.LayoutParams(100, 100));
            }
            else {
                picturesView = (ImageView)convertView;
            }
            return picturesView;
        }
    }
*/


