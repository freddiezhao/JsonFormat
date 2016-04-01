package com.anarchy.library;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anarchy.library.widget.MyLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Version 1.0
 * Date: 16/4/1 16:56
 * Author: zhendong.wu@shoufuyou.com
 * Copyright © 2014-2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */
public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.ItemViewHolder> {
    private JSONObject mJSONObject;
    private JSONArray mJSONArray;
    private final static String SIX_SPACE = "      ";
    private String errorMsg;
    public JsonAdapter(String string) {
        try {
            mJSONObject = new JSONObject(string);
        } catch (JSONException e) {
            errorMsg = e.getMessage();
            try {
                mJSONArray = new JSONArray(string);
            } catch (JSONException e1) {
               errorMsg = errorMsg +"---"+e1.getMessage();
            }
        }
    }

    public JsonAdapter(JSONObject jsonObject) {
        mJSONObject = jsonObject;
    }

    public JsonAdapter(JSONArray jsonArray) {
        mJSONArray = jsonArray;
    }


    private void resetVisibility(ItemViewHolder holder) {
        holder.leftText.setVisibility(View.GONE);
        holder.icon.setVisibility(View.GONE);
        holder.rightText.setVisibility(View.GONE);
    }



    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.json_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        resetVisibility(holder);
        if (mJSONArray == null && mJSONObject == null) {
            holder.leftText.setVisibility(View.VISIBLE);
            holder.leftText.setText("不是有效的json格式:"+errorMsg);
            return ;
        }
        if (mJSONObject != null) {
            if (position == 0) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(R.drawable.minus);
                holder.rightText.setVisibility(View.VISIBLE);
                holder.rightText.setText("{");
                return ;
            }
            if (position ==  getItemCount() - 1) {
                holder.rightText.setVisibility(View.VISIBLE);
                holder.rightText.setText("}");
                return ;
            }
            JSONArray array = mJSONObject.names();
            String key = array.optString(position - 1);
            if (position <   getItemCount() - 2) {
                handleJsonObject(mJSONObject.opt(key), key, holder, true, 1);
            } else {
                handleJsonObject(mJSONObject.opt(key), key, holder, false, 1);
            }
        }

        if(mJSONArray != null){
            if(position == 0){
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(R.drawable.minus);
                holder.rightText.setVisibility(View.VISIBLE);
                holder.rightText.setText("[");
                return ;
            }
            if(position == getItemCount() - 1){
                holder.rightText.setVisibility(View.VISIBLE);
                holder.rightText.setText("]");
                return ;
            }
            Object value = mJSONArray.opt(position-1);
            if (position < getItemCount() - 2) {
                handleJsonArray(value,holder,true,1);
            } else {
                handleJsonArray(value,holder,false,1);
            }
        }
    }


    @Override
    public int getItemCount() {
        if (mJSONObject != null) {
            return mJSONObject.names().length() + 2;
        }
        if (mJSONArray != null) {
            return mJSONArray.length() + 2;
        }
        return 1;
    }



    class JsonClick implements View.OnClickListener {
        private Object object;
        private ItemViewHolder holder;
        private LayoutInflater mInflater;
        private int hierarchy;
        private boolean isCollapsed = true;
        private boolean appendComma;
        private CharSequence lastValue;
        private boolean isArray;

        JsonClick(Object value, ItemViewHolder holder, int hierarchy, boolean appendComma, boolean isArray) {
            this.object = value;
            this.holder = holder;
            mInflater = LayoutInflater.from(holder.itemView.getContext());
            this.hierarchy = hierarchy;
            this.appendComma = appendComma;
            this.isArray = isArray;
        }

        @Override
        public void onClick(View v) {
            MyLinearLayout container = (MyLinearLayout) holder.itemView;
            if (container.getChildCount() == 1) {
                isCollapsed = false;
                holder.icon.setImageResource(R.drawable.minus);
                lastValue = holder.rightText.getText();
                holder.rightText.setText(isArray?"[":"{");
                JSONArray array;
                if(isArray){
                    array = (JSONArray) object;
                }else {
                    array = ((JSONObject)object).names();
                }
                for (int i = 0; i < array.length(); i++) {
                    View itemView = mInflater.inflate(R.layout.json_item, null);
                    ItemViewHolder viewHolder = new ItemViewHolder(itemView);
                    Object value = array.opt(i);
                    if (i < array.length() - 1) {
                        if(isArray){
                            handleJsonArray(value,viewHolder,true,hierarchy);
                        }else {
                            handleJsonObject(((JSONObject) object).opt((String) value), array.optString(i), viewHolder, true, hierarchy);
                        }
                    } else {
                        if(isArray){
                            handleJsonArray(value,viewHolder,false,hierarchy);
                        }else {
                            handleJsonObject(((JSONObject) object).opt((String) value), array.optString(i), viewHolder, false, hierarchy);
                        }
                    }
                    ((MyLinearLayout) holder.itemView).addViewNoInvalidate(itemView);
                }
                View itemView = mInflater.inflate(R.layout.json_item, null);
                ItemViewHolder viewHolder = new ItemViewHolder(itemView);
                viewHolder.rightText.setVisibility(View.VISIBLE);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < hierarchy - 1; i++) {
                    builder.append(SIX_SPACE);
                }
                builder.append(isArray?"]":"}").append(appendComma ?",":"");
                viewHolder.rightText.setText(builder);
                ((MyLinearLayout) holder.itemView).addViewNoInvalidate(itemView);
                holder.itemView.invalidate();
                holder.itemView.requestLayout();
            } else {
                CharSequence temp = holder.rightText.getText();
                holder.rightText.setText(lastValue);
                lastValue = temp;
                holder.icon.setImageResource(isCollapsed?R.drawable.minus:R.drawable.plus);
                for (int i = 1; i < container.getChildCount(); i++) {
                    container.getChildAt(i).setVisibility(isCollapsed?View.VISIBLE:View.GONE);
                }
                isCollapsed = !isCollapsed;
            }
        }
    }


    private void handleJsonArray(Object value,ItemViewHolder holder,boolean appendComma,int hierarchy){
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        for (int i = 0; i < hierarchy; i++) {
            keyBuilder.append(SIX_SPACE);
        }
        holder.leftText.setVisibility(View.VISIBLE);
        holder.leftText.setText(keyBuilder);
        handleValue(value,holder,appendComma,hierarchy);
    }

    private void handleJsonObject(Object value, String key, ItemViewHolder holder, boolean appendComma, int hierarchy) {
        holder.leftText.setVisibility(View.VISIBLE);
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        for (int i = 0; i < hierarchy; i++) {
            keyBuilder.append(SIX_SPACE);
        }
        keyBuilder.append("\"").append(key).append("\"").append(":");
        keyBuilder.setSpan(new ForegroundColorSpan(0xFF922799), 0, keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.leftText.setText(keyBuilder);
        handleValue(value,holder,appendComma,hierarchy);
    }

    private void handleValue(Object value,ItemViewHolder holder,boolean appendComma,int hierarchy){
        holder.rightText.setVisibility(View.VISIBLE);
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        if (value instanceof Number) {
            valueBuilder.append(value.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(0xFF25AAE2), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (value instanceof JSONObject) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.plus);
            valueBuilder.append("Object{...}");
            holder.icon.setOnClickListener(new JsonClick( value, holder, hierarchy + 1,appendComma,false));
        }

        if (value instanceof JSONArray) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.plus);
            valueBuilder.append("Array[" + ((JSONArray) value).length() + "]");
            holder.icon.setOnClickListener(new JsonClick(value,holder,hierarchy+1,appendComma,true));
        }

        if (value instanceof String) {
            valueBuilder.append("\"").append(value.toString()).append("\"");
            valueBuilder.setSpan(new ForegroundColorSpan(0xFF3AB54A), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (valueBuilder.length() == 0 || value == null) {
            valueBuilder.append("null");
            valueBuilder.setSpan(new ForegroundColorSpan(0xFFF1592A), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (appendComma) {
            valueBuilder.append(",");
        }
        holder.rightText.setText(valueBuilder);
    }

    static class ItemViewHolder  extends RecyclerView.ViewHolder{
        TextView leftText;
        ImageView icon;
        TextView rightText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            leftText = (TextView) itemView.findViewById(R.id.text_left);
            icon = (ImageView) itemView.findViewById(R.id.image_icon);
            rightText = (TextView) itemView.findViewById(R.id.text_right);
        }
    }
}
