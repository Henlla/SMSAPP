package com.demo1.smsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.demo1.smsapp.databinding.SearchSheetModelBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SearchBottomSheetModel extends BottomSheetDialogFragment {
    public SearchBottomSheetModel() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    SearchSheetModelBinding searchSheetModelBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchSheetModelBinding = SearchSheetModelBinding.inflate(inflater);
        return searchSheetModelBinding.getRoot();
    }
}
