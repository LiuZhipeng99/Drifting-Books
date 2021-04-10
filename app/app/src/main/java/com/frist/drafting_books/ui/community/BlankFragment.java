package com.frist.drafting_books.ui.community;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frist.drafting_books.DB.GetBookFromLean;
import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.AVObject;
import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    static public final Boolean NOTYET=true;
    static public final Boolean ALREADY=false;
    private View rootView;
    private List<BundleBoat> recyclerViewItem;
    private RecyclerView recyclerView;//声明RecyclerView


    private static final String TAG = "BlankFragment";


    public BlankFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
        //用来放在bundle里传输数据

    public static BlankFragment newInstance(Boolean judge,List<BundleBoat> list) {
        BlankFragment fragment = new BlankFragment();
       //
        Bundle bundle=new Bundle();
        bundle.putBoolean("judge",judge);

        bundle.putSerializable("booklist", (Serializable) list);
        fragment.setArguments(bundle);



        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().getBoolean("judge")){
                //TODO:这种情况取全国数据库,并且初始化recyclerItem
                ///下面为测试代码
//                BookHouseItemBean bean=new BookHouseItemBean();
//                bean.setImage("http://qiniu.lifelover.top/touxiang20210301170002.png");
//                bean.setText("你好可爱");
                List<BundleBoat> mylist=(List)getArguments().getSerializable("booklist");

                recyclerViewItem=mylist;

            }else {
                //TODO:这种情况取好友数据库，并且初始化recyclerItem
                List<BundleBoat> mylist=(List)getArguments().getSerializable("booklist");
                recyclerViewItem=mylist;
            }
        }
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_blank, container, false);//这一句防止重复解析。
        }
        initView();//初始化各种frament界面。
        //Log.d(TAG, "onCreateView: ");
        return rootView;
    }

    private void initView() {//可以获得layout，然后对layout进行操作。
        initRefresh();
        initRecyclerview();
    }

    private void initRecyclerview() {
        recyclerView=rootView.findViewById(R.id.recycler_view);
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);//设置网格布局
        MyRecyclerViewAdapter rv_adapter = new MyRecyclerViewAdapter(getContext(),recyclerViewItem);//把内容传进去。
        recyclerView.setAdapter(rv_adapter);
        recyclerView.setLayoutManager(manager);
    }

    //初始化下拉刷新功能。
    void initRefresh(){
        PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) rootView.findViewById(R.id.ptr_frame_layout);

        //第一种头部,StoreHouse风格的头部实现
//    StoreHouseHeader storeHouseHeader = new StoreHouseHeader(getContext());
//    storeHouseHeader.setPadding(0,100,0,0);
//    storeHouseHeader.setBackgroundColor(Color.BLACK);
//    storeHouseHeader.setTextColor(Color.WHITE);
//    storeHouseHeader.initWithString("drifting~");//只可英文，中文不可运行(添加时间)
//    ptrFrameLayout.setHeaderView(storeHouseHeader);
//    ptrFrameLayout.addPtrUIHandler(storeHouseHeader);

        //第二种头部,Material Design风格的头部实现,类似SwipeRefreshLayout
        MaterialHeader materialHeader = new MaterialHeader(getContext());
        materialHeader.setColorSchemeColors(new int[]{R.color.purple_700, Color.GREEN, Color.BLUE});
        ptrFrameLayout.setHeaderView(materialHeader);
        ptrFrameLayout.addPtrUIHandler(materialHeader);


        //第三种头部,经典 风格的头部实现，下拉箭头+时间，，下拉刷新
//        //定义下拉刷新样式
//        PtrClassicDefaultHeader ptrClassicDefaultHeader = new PtrClassicDefaultHeader(getContext());
//        //添加到顶部
//        ptrFrameLayout.setHeaderView(ptrClassicDefaultHeader);
//        //添加到布局中
//        ptrFrameLayout.addPtrUIHandler(ptrClassicDefaultHeader);

        //上拉加载
        //定义上拉加载样式
        PtrClassicDefaultFooter ptrClassicDefaultFooter = new PtrClassicDefaultFooter(getContext());
        //为加载添加布局样式
        ptrFrameLayout.setFooterView(ptrClassicDefaultFooter);
        //添加到布局中
        ptrFrameLayout.addPtrUIHandler(ptrClassicDefaultFooter);
        //监听
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            //加载更多监听
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "刷新结束", Toast.LENGTH_SHORT).show();
                        //完成加载，隐藏布局
                        ptrFrameLayout.refreshComplete();
                    }
                },1000);

            }
            //下拉刷新监听
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "加载结束", Toast.LENGTH_SHORT).show();
                        //结束刷新，隐藏布局
                        ptrFrameLayout.refreshComplete();
                    }
                },1000);

            }
        });
        //设置模式
        //BOTH：下拉刷新，下拉加载
        //NONE：都不监听
        //LOAD_MORE：只有下拉加载
        //REFRESH：只有下拉刷新
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

}