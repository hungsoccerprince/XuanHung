package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
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

    /**
     * Cursor used to access the results from querying for images on the SD card.
     */
    ImageView img ;
    TextView tvDateImage;
    private Cursor cursor;
    /*
     * Column index for the Thumbnails Image IDs.
     */
    private int columnIndex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_image);

        img = (ImageView)findViewById(R.id.img);
        tvDateImage = (TextView)findViewById(R.id.tvDateImage);

        // Set up an array of the Thumbnail Image ID column we want
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };
        // Create the cursor pointing to the SDCard
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                null,       // Return all rows
                null,
                MediaStore.Images.Media.BUCKET_ID);
        // Get the column index of the Thumbnails Image ID

        Log.d("ImageSize",String.valueOf(cursor.getCount()));

        if(cursor.getCount()>0){

            Random rand = new Random();
            int imgRand = rand.nextInt(cursor.getCount()-1);
            cursor.moveToPosition(imgRand);

            int imageID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(imageID));

            File file = new File(String.valueOf(uri));
            Date lastModDate = new Date(file.lastModified());
            Log.i("File last modified @ : ", lastModDate.toString());

            String dateTaken = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(dateTaken));

            Date time = calendar.getTime();

            Log.d("Time", formatTime(time));
            //tvDateImage.setText(formatTime(date));
            tvDateImage.setText(formatTime(time));
            img.setImageURI(uri);
        }

    }

    public String formatTime(Date time){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String timeFormat = df.format(time);
        return timeFormat;
    }


    private class ImageAdapter extends BaseAdapter {
        private Context context;
        public ImageAdapter(Context localContext) {
            context = localContext;
        }
        public int getCount() {
            return cursor.getCount();
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
                // Move cursor to current position
                cursor.moveToPosition(position);
                // Get the current value for the requested column
                int imageID = cursor.getInt(columnIndex);
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

}
