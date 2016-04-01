package com.anarchy.library;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Version 1.0
 * Date: 16/4/1 16:06
 * Author: zhendong.wu@shoufuyou.com
 * Copyright © 2014-2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */
public class JsonFormatDialogFragment extends BaseDialogFragment {
    private String mJson;


    private RecyclerView mRecyclerView;

    public static JsonFormatDialogFragment newInstance(String json){
        JsonFormatDialogFragment fragment = new JsonFormatDialogFragment();
        fragment.mJson = json;
        return fragment;
    }
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_json_format,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new JsonAdapter(mJson));
        return view;
    }
}
