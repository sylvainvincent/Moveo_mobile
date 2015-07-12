package fr.moveoteam.moveomobile.others;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Sylvain on 21/06/15.
 */
public class CustomScrollView extends ScrollView {

    private boolean enableScrolling = true;

    public boolean isEnableScrolling() {
        return enableScrolling;
    }

    public void setEnableScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {

        return isEnableScrolling() && super.onInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        return isEnableScrolling() && super.onInterceptTouchEvent(ev);
    }
}
