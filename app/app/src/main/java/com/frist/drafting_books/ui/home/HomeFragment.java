package com.frist.drafting_books.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.frist.drafting_books.R;

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



}