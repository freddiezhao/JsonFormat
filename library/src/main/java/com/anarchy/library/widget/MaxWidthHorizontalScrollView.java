package com.anarchy.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

/**
 * User:  Anarchy
 * Email:  rsshinide38@163.com
 * CreateTime: 四月/01/2016  21:47.
 * Description:
 */
public class MaxWidthHorizontalScrollView extends HorizontalScrollView {
    private static final int MAX_PX = 1440;
    private int maxPixel;
    public MaxWidthHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public MaxWidthHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaxWidthHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        maxPixel = (int) (getResources().getDisplayMetrics().density*MAX_PX+0.5f);
    }
    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();

        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop()
                + getPaddingBottom(), lp.height);

        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(maxPixel, MeasureSpec.AT_MOST);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    /**
     * FrameLayout use this measure child
     * @param child
     * @param parentWidthMeasureSpec
     * @param widthUsed
     * @param parentHeightMeasureSpec
     * @param heightUsed
     */
    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);
        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                maxPixel, MeasureSpec.AT_MOST);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
}
