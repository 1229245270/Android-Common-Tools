package com.hzc.commontools.ui.imagerecycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author 12292
 */
public class ImageSquareView extends RelativeLayout {
    public ImageSquareView(Context context) {
        super(context);
    }

    public ImageSquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageSquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
