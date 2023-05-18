package com.hzc.androidcommontools;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.hzc.commontools.ui.imagelookview.LookImageActivity;
import com.hzc.commontools.ui.imagerecycleview.ImageBean;
import com.hzc.commontools.ui.imagerecycleview.ImageListAdapter;
import com.hzc.commontools.ui.imagerecycleview.ImageRecycleView;
import com.hzc.commontools.utils.ImageUtil;
import com.hzc.commontools.utils.PhotoUtil;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_1,bt_2;
    private ImageRecycleView imageRecycleView;
    private List<ImageBean> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        bt_1 = findViewById(R.id.bt_1);
        bt_2 = findViewById(R.id.bt_2);
        imageRecycleView = findViewById(R.id.imageRecycleView);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);

        list = new ArrayList<>();
        imageRecycleView.initData(this,list);
    }

    private String img;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_1:
                PhotoUtil.takePhotoFromCamera(this, new PhotoUtil.TakePhotoFormCameraCallback() {
                    @Override
                    public void toImagePath(String filePath) {
                        String img2 = ImageUtil.defaultCompress(PhotoActivity.this,filePath);
                        ImageBean imageBean = new ImageBean();
                        imageBean.setPath(img2);
                        list.add(imageBean);
                    }
                });
                break;
            case R.id.bt_2:
                PhotoUtil.takePhotoFromAlbum(this, false, null, new PhotoUtil.TakePhotoFormAlbumCallback() {
                    @Override
                    public void toImageList(ArrayList<Photo> list) {
                        if(list != null && list.size() > 0){

                        }
                    }
                });
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
