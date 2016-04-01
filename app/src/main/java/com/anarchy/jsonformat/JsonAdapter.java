package com.anarchy.jsonformat;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anarchy.jsonformat.widget.MyLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Version 1.0
 * <p/>
 * Date: 16/4/1 16:56
 * Author: zhendong.wu@shoufuyou.com
 * <p/>
 * Copyright © 2014-2016 Shanghai Xiaotu Network Technology Co., Ltd.
 */
public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.ItemViewHolder> {
    private JSONObject mJSONObject;
    private JSONArray mJSONArray;

   public JsonAdapter(String string){
       try {
           mJSONObject = new JSONObject(string);
       } catch (JSONException e) {

           try {
               mJSONArray = new JSONArray(string);
           } catch (JSONException e1) {
               e1.printStackTrace();
           }
       }
   }

    public JsonAdapter(JSONObject jsonObject){
        mJSONObject = jsonObject;
    }

    public JsonAdapter(JSONArray jsonArray){
        mJSONArray = jsonArray;
    }


    private void resetVisibility(ItemViewHolder holder){
        holder.leftText.setVisibility(View.GONE);
        holder.icon.setVisibility(View.GONE);
        holder.rightText.setVisibility(View.GONE);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.json_item,parent,false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        resetVisibility(holder);
        if(mJSONArray == null&&mJSONObject == null){
            holder.leftText.setVisibility(View.VISIBLE);
            holder.leftText.setText("不是有效的json格式");
            return;
        }

        if(mJSONObject!=null){
            if(position == 0) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(R.drawable.minus);
                holder.rightText.setVisibility(View.VISIBLE);
                holder.rightText.setText("{");
                return;
            }
            if(position == getItemCount()-1){
                holder.rightText.setVisibility(View.VISIBLE);
                holder.rightText.setText("}");
                return;
            }
            JSONArray array = mJSONObject.names();
            String key = array.optString(position-1);
            if(position<getItemCount()-1) {
                handleJsonObject(mJSONObject, key, holder,true);
            }else {
                handleJsonObject(mJSONObject,key,holder,false);
            }
        }
    }


    class JsonObjectClick implements View.OnClickListener{
        private JSONObject jsonObject;
        private ItemViewHolder holder;
        private LayoutInflater mInflater;
        JsonObjectClick(JSONObject jsonObject, ItemViewHolder holder){
            this.jsonObject = jsonObject;
            this.holder = holder;
            mInflater = LayoutInflater.from(holder.itemView.getContext());
        }
        @Override
        public void onClick(View v) {
            holder.icon.setImageResource(R.drawable.minus);
            holder.rightText.setText("{");
            JSONArray array = jsonObject.names();
            for (int i=0;i<array.length();i++){
                View itemView = mInflater.inflate(R.layout.json_item,null);
                ItemViewHolder viewHolder = new ItemViewHolder(itemView);
                if(i<array.length()-1){
                    handleJsonObject(jsonObject,array.optString(i),viewHolder,true);
                }else {
                    handleJsonObject(jsonObject,array.optString(i),viewHolder,false);
                }
                ((MyLinearLayout)holder.itemView).addViewNoInvalidate(itemView);
            }
            holder.itemView.invalidate();
            holder.itemView.requestLayout();
        }
    }


    private void handleJsonObject(JSONObject jsonObject, String key, ItemViewHolder holder,boolean appendComma){

        Object value = jsonObject.opt(key);
        holder.leftText.setVisibility(View.VISIBLE);
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        keyBuilder.append("\"").append(key).append("\"").append(":");
        keyBuilder.setSpan(new ForegroundColorSpan(0xFF922799),0,keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.leftText.setText(keyBuilder);
        holder.rightText.setVisibility(View.VISIBLE);
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        if(value == null){
            valueBuilder.append(null);
            valueBuilder.setSpan(new ForegroundColorSpan(0xFFF1592A),0,valueBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(value instanceof Number){
            valueBuilder.append(value.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(0xFF25AAE2),0,valueBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(value instanceof JSONObject){
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.plus);
            valueBuilder.append("Object{...}");
            holder.icon.setOnClickListener(new JsonObjectClick((JSONObject) value,holder));
        }

        if(value instanceof JSONArray){
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.plus);
            valueBuilder.append("Array["+((JSONArray)value).length()+"]");
        }

        if(value instanceof String){
            valueBuilder.append("\"").append(value.toString()).append("\"");
            valueBuilder.setSpan(new ForegroundColorSpan(0xFF3AB54A),0,valueBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(appendComma){
            valueBuilder.append(",");
        }
        holder.rightText.setText(valueBuilder);
    }


    @Override
    public int getItemCount() {
        if(mJSONObject!=null){
            return mJSONObject.names().length()+2;
        }
        if(mJSONArray!=null){
            return mJSONArray.length()+2;
        }
        return 1;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
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
