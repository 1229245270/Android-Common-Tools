package com.hzc.commontools.ui.imagerecycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hzc.commontools.R;
import com.hzc.commontools.ui.imagelookview.LookImageActivity;
import com.hzc.commontools.utils.PhotoUtil;

import java.util.ArrayList;
import java.util.List;

public class ImageRecycleView extends RecyclerView {
    public ImageRecycleView(@NonNull Context context) {
        this(context,null);
    }

    public ImageRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImageRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }
    private int maxSize;
    private Context context;
    private ImageListAdapter adapter;

    /**
     * 初始化界面
     */
    public void initView(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageRecycleView);
        maxSize = typedArray.getInteger(R.styleable.ImageRecycleView_maxSize,6);
        this.context = context;
    }

    public void initData(List<ImageBean> list,ImageListAdapter.OnImageItemClick onImageItemClick){
        adapter = new ImageListAdapter(getContext(),list,maxSize,15);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        setLayoutManager(gridLayoutManager);
        setAdapter(adapter);
        if(onImageItemClick != null) {
            adapter.setOnImageItemClick(onImageItemClick);
        }
    }

    public void initData(AppCompatActivity activity,List<ImageBean> list){
        ImageListAdapter.OnImageItemClick onImageItemClick = new ImageListAdapter.OnImageItemClick() {
            @Override
            public void onLook(ImageBean imageBean, int position) {
                ArrayList<Object> strings = new ArrayList<>();
                for(int i = 0;i < list.size();i++){
                    strings.add(list.get(i).getPath());
                }
                LookImageActivity.openActivity(activity,strings,0);
            }

            @Override
            public void onPhoto() {
                PhotoUtil.takePhotoFromCamera(activity, new PhotoUtil.TakePhotoFormCameraCallback() {
                    @Override
                    public void toImagePath(String filePath) {
                        if(filePath != null){
                            ImageBean imageBean = new ImageBean();
                            imageBean.setPath(filePath);
                            list.add(imageBean);
                            adapter.notifyItemInserted(adapter.getItemCount());
                            adapter.notifyItemRangeChanged(list.size() - 1,adapter.getItemCount() - list.size() + 1);
                        }
                    }
                });
            }

            @Override
            public void onDelete(ImageBean imageBean, int position) {
                list.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,list.size() - position);
            }
        };
        initData(list, onImageItemClick);
    }
}
