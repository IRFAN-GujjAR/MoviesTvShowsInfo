package com.example.moviestvshowsinfo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * CustomViewPager class is extended from ViewPager, because we want to disable swiping in viewpager.
 */
public class CustomViewPager extends ViewPager {

    private boolean enabled;

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.enabled=true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enabled) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.enabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    /**
     * This method will bed called to disable or enable the swiping of viewpager.
     */
    public void enableSwiping(boolean enabled){
        this.enabled=enabled;
    }
}
