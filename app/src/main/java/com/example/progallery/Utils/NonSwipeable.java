package com.example.progallery.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class NonSwipeable extends ViewPager {

    public NonSwipeable(@NonNull Context context) {
        super(context);
        SetMyScroller();
    }

    public NonSwipeable(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        SetMyScroller();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    private void SetMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(false);
            scroller.set(this, new MyScroller(getContext()));
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int StartX, int StartY, int dx, int dy, int duration) {
            super.startScroll(StartX, StartY, dx, dy, duration);
        }
    }
}
