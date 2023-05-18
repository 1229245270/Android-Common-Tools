package com.hzc.commontools.ui.imagerecycleview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hzc.commontools.R;
import com.hzc.commontools.utils.DensityUtil;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private Context context;
    private List<ImageBean> list;
    private int maxSize = 6;
    private int padding = 15;


    public ImageListAdapter(Context context,List<ImageBean> list){
        this.context = context;
        this.list = list;
    }

    public ImageListAdapter(Context context,List<ImageBean> list,int maxSize,int padding){
        this.context = context;
        this.list = list;
        this.maxSize = maxSize;
        this.padding = padding;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.image_list_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int pos = position;

        int dp = DensityUtil.dip2px(context,padding);
        holder.imageSquareView.setPadding(dp,dp,dp,dp);

        if(pos < list.size()){
            Glide.with(context).load(list.get(pos).getPath()).into(holder.ivPhoto);
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onImageItemClick != null){
                        if(pos < list.size()){
                            onImageItemClick.onLook(list.get(pos),pos);
                        }
                    }
                }
            });
        }else{
            Glide.with(context).load(R.drawable.add_image).into(holder.ivPhoto);
            holder.ivDelete.setVisibility(View.GONE);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onImageItemClick != null){
                        onImageItemClick.onPhoto();
                    }
                }
            });
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onImageItemClick != null){
                    if(pos < list.size()){
                        onImageItemClick.onDelete(list.get(pos),pos);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list.size() >= maxSize){
            return list.size();
        }else{
            return list.size() + 1;
        }
    }

    private OnImageItemClick onImageItemClick;

    public void setOnImageItemClick(OnImageItemClick onImageItemClick) {
        this.onImageItemClick = onImageItemClick;
    }

    public interface OnImageItemClick{
        /**
         * 查看照片
         * @param imageBean 子项
         * @param position 位置
         */
        void onLook(ImageBean imageBean,int position);

        /**
         * 拍照
         */
        void onPhoto();

        /**
         * 删除
         * @param imageBean 子项
         * @param position 位置
         */
        void onDelete(ImageBean imageBean,int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPhoto;
        private final ImageView ivDelete;
        private final ImageSquareView imageSquareView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            imageSquareView = itemView.findViewById(R.id.imageSquareView);
        }
    }
}
