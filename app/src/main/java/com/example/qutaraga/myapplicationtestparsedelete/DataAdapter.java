package com.example.qutaraga.myapplicationtestparsedelete;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private MyList<PhotoURL> photoURLMyList;
    private Context context;

    public DataAdapter(Context context,MyList<PhotoURL> _photoURLMyList) {
        this.context = context;
        this.photoURLMyList = _photoURLMyList;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Picasso.with(context).load(photoURLMyList.get(i).getPhotoURL()).resize(640, 360).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return photoURLMyList.listSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_android;
        public ViewHolder(View view) {
            super(view);
            img_android = (ImageView)view.findViewById(R.id.img_android);
        }
    }

    public MyList<PhotoURL> getPhotoURLMyList() {
        return photoURLMyList;
    }


}