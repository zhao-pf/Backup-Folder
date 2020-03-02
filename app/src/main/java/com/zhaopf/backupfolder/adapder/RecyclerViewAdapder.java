package com.zhaopf.backupfolder.adapder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhaopf.backupfolder.R;

import java.util.List;

/**
 * Created by 赵鹏飞 on 2020/2/18 11:32
 * 适配器
 */
public class RecyclerViewAdapder extends RecyclerView.Adapter<RecyclerViewAdapder.ViewHolder> {

    private String[] mName;
    private final LayoutInflater layoutInflater;

    public RecyclerViewAdapder(List<String> str, Context mContext) {
        mName = str.toArray(new String[]{});
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerViewAdapder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.filedir_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_diritem.setText(mName[position]);
    }

    @Override
    public int getItemCount() {
        return mName.length;
    }

    public void putData(List<String> str) {
        mName = str.toArray(new String[]{});
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_diritem;
        ViewHolder(View itemView) {
            super(itemView);
            tv_diritem = itemView.findViewById(R.id.tv_diritem);
        }
    }
}
