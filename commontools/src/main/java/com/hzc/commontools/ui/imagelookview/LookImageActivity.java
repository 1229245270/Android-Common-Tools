package com.hzc.commontools.ui.imagelookview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.hzc.commontools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 12292
 */
public class LookImageActivity extends AppCompatActivity {

    private LookImageViewPager look_image;
    private LookImageAdapter adapter;
    private LookImageIndicatorView tab_layout;

    public static void openActivity(AppCompatActivity activity,ArrayList<Object> list,int position){
        Intent intent = new Intent(activity,LookImageActivity.class);
        intent.putExtra("url",list);
        intent.putExtra("position",position);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_look_image);
        look_image = findViewById(R.id.look_image);
        tab_layout = findViewById(R.id.tab_layout);
        Intent intent = getIntent();
        if(intent != null){
            List<String> url = intent.getStringArrayListExtra("url");
            int position = intent.getIntExtra("position",0);
            List<View> viewList = new ArrayList<>();
            for(int i = 0;i < url.size();i++){
                PhotoView photoView = new PhotoView(this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                photoView.setLayoutParams(params);
                Glide.with(this).load(url.get(i)).into(photoView);
                viewList.add(photoView);
            }
            adapter = new LookImageAdapter(viewList);
            look_image.setAdapter(adapter);
            look_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
            look_image.setCurrentItem(position);
            tab_layout.setViewPager(look_image);
        }
    }
}
