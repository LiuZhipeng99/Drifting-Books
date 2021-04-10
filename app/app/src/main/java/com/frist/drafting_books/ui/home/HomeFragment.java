package com.frist.drafting_books.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.frist.drafting_books.DB.GetBookFromLean;
import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.R;
import com.frist.drafting_books.ui.community.BundleBoat;
import com.frist.drafting_books.ui.community.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVUser;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    private View userview,attentionview,attentionerview,dynamicview;
    private RecyclerView recyclerView;
    private List<BundleBoat> myBookList=new ArrayList<>();

    private static final String TAG = "HomeFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

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


        //初始化个人书表
        LeancloudDB dbt = LeancloudDB.getInstance();
        dbt.showMyBooks(new GetBookFromLean() {
            @Override
            public void querySuccess(List<AVObject> books, List<AVObject> book_borrow) {
                //在这里获取
//                BundleBoat b1 = new BundleBoat(book_borrow.get(0));





            }

            @Override
            public void querySuccess(List<AVObject> books) {
                for(int i=0;i<books.size();i++){
                    myBookList.add(new BundleBoat(books.get(i)));
                }
                initRecyclerView();
            }


            @Override
            public void queryOneSuccess(AVObject book) {

            }

            @Override
            public void queryFail(Error e) {
                Log.d(TAG, "queryFail: 获取书籍失败");
                Toast.makeText(getContext(),"获取书籍失败",Toast.LENGTH_SHORT).show();
            }
        });







        return root;
    }

    @SuppressLint("ResourceType")
    private void initRecyclerView(){
        recyclerView=root.findViewById(R.id.recycler_view);
        GridLayoutManager manager=new GridLayoutManager(getContext(),3);//设置网格布局
        MyRecyclerViewAdapter rv_adapter = new MyRecyclerViewAdapter(getContext(),myBookList);//把内容传进去。
        if(myBookList.size()>0){
            LinearLayout linearLayout=root.findViewById(R.id.linearLayoutHaveBg);
            linearLayout.setBackground(new ColorDrawable(Color.WHITE));
        }
        recyclerView.setAdapter(rv_adapter);
        recyclerView.setLayoutManager(manager);
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