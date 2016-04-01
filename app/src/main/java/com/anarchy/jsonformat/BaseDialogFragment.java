package com.anarchy.jsonformat;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * Version 1.0
 * <p/>
 * Date: 15/11/30 17:38
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2014-2015 Shanghai Xiaotu Network Technology Co., Ltd.
 */
public abstract class BaseDialogFragment extends DialogFragment {
    private static final int MAX_HEIGHT = 471;

    private FrameLayout mFrameLayout;
    private  float dimAmount = 0.6f;
    private int gravity = Gravity.CENTER;
    private int animStyle = android.R.style.Animation_Dialog;
    private int mMaxHeight = getMaxHeight();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFrameLayout = (FrameLayout) inflater.inflate(R.layout.dialog_base,container);
        mFrameLayout.addView(onCreateContentView(inflater,container));
        return mFrameLayout;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        mFrameLayout.measure(View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST)
                , View.MeasureSpec.makeMeasureSpec((int) (mMaxHeight*getResources().getDisplayMetrics().density),
                View.MeasureSpec.AT_MOST));
        int height = mFrameLayout.getMeasuredHeight();
        params.windowAnimations = animStyle;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        params.height = (int) (mMaxHeight*getResources().getDisplayMetrics().density+0.5f);
        params.gravity = gravity;
        params.dimAmount = dimAmount;
    }

    protected abstract View onCreateContentView(LayoutInflater inflater,ViewGroup container);

    public float getDimAmount() {
        return dimAmount;
    }

    public void setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setAnimStyle(int animStyle) {
        this.animStyle = animStyle;
    }

    /**
     *
     * @return unit dp
     */
    protected int getMaxHeight(){
        return MAX_HEIGHT;
    }
}
