package com.frist.drafting_books.ui.record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.frist.drafting_books.R;
import com.frist.drafting_books.ui.home.HomeViewModel;
import com.google.zxing.integration.android.IntentIntegrator;

public class RecordFragment extends Fragment {

    private RecordViewModel recordViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        recordViewModel =
                new ViewModelProvider(this).get(RecordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_community, container, false);

//        Intent qr = new Intent(this.getContext(), QrCodeActivity.class);
//        startActivity(qr);

        new IntentIntegrator(this.getActivity())
                .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                //.setPrompt("请对准二维码")// 设置提示语
                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                .setCaptureActivity(QrCodeActivity.class)//自定义扫码界面
                .initiateScan();// 初始化扫码


        return root;
    }


}