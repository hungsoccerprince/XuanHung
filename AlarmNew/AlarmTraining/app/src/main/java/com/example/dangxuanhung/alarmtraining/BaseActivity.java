package com.example.dangxuanhung.alarmtraining;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by MaiNam on 4/26/2016.
 */

public class BaseActivity extends AppCompatActivity {
    public enum TypeSlideOut {
        out_left_to_right,
        out_top_to_bottom,
        out_none
    }

    public enum TypeSlideIn {
        in_right_to_left,
        in_bottom_to_up,
        in_none
    }

    private TypeSlideOut typeSlide = TypeSlideOut.out_none;

    public TypeSlideOut getSlideOut() {
        return typeSlide;
    }

    public void setSlideOut(TypeSlideOut typeSlide) {
        this.typeSlide = typeSlide;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        slideOut();
    }

    private void slideOut() {
        switch (typeSlide) {
            case out_top_to_bottom:
                overridePendingTransition(R.anim.no_change, R.anim.slide_out_down);
                break;
            case out_left_to_right:
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
                break;
            case out_none:
                break;
        }
    }

    public static void setSlideIn(Activity mActivity, TypeSlideIn typeSlideIn) {
        switch (typeSlideIn) {
            case in_bottom_to_up:
                mActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.no_change);
                break;
            case in_right_to_left:
                mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case in_none:
                break;
        }
    }
}
