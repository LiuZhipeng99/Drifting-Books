package com.frist.drafting_books.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.R;

import cn.leancloud.AVUser;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    private View userview,attentionview,attentionerview,dynamicview;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userview = root.findViewById(R.id.user);
        attentionview = root.findViewById(R.id.attention);
        attentionerview = root.findViewById(R.id.attentioner);
        dynamicview = root.findViewById(R.id.dynamic);
//        显示个人信息
//        LeancloudDB dbt = LeancloudDB.getInstance();
//        initDetail();
        //！！！这个root和this的root不同步
        TextView userName = root.findViewById(R.id.userNameView);
        ImageView userHead = root.findViewById(R.id.userhead);
        AVUser curU = AVUser.getCurrentUser();
        userName.setText("   "+curU.getUsername()+"的书房");
        Glide.with(this).load(curU.getString("imageLink"))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                .transform(new GrayscaleTransformation(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.nobookcover).into(userHead);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });


        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(),User.class);
                startActivity(intent);
            }
        });
        attentionview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(),attention.class);
                startActivity(intent);
            }
        });
        attentionerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(),Attentioner.class);
                startActivity(intent);
            }
        });
        dynamicview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(),Dynamic.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void initDetail() {
        TextView userName = root.findViewById(R.id.userNameView);
        ImageView userHead = root.findViewById(R.id.userhead);
        AVUser curU = AVUser.getCurrentUser();
        userName.setText(curU.getUsername()+"的书房");
        Glide.with(this).load(curU.getString("imageLink"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.nobookcover).into(userHead);
    }


}