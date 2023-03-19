package com.demo1.smsapp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.CalendarSheetModelBinding;
import com.demo1.smsapp.databinding.SearchSheetModelBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.jetbrains.annotations.NotNull;



public class CalendarBottomSheetModel extends BottomSheetDialogFragment  {
    public CalendarBottomSheetModel() {

    }

    public CalendarBottomSheetModel(ISetOnclick iSetOnclick) {
        this.iSetOnclick = iSetOnclick;
    }
    ISetOnclick iSetOnclick;
    CalendarSheetModelBinding calendarBottomSheetModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        calendarBottomSheetModel = CalendarSheetModelBinding.inflate(inflater);
        calendarBottomSheetModel.simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//                        Toast.makeText(getContext(), i1+" "+i2, Toast.LENGTH_SHORT).show();
                        iSetOnclick.SelectDate(i1,i2);
                    }
                });
        return calendarBottomSheetModel.getRoot();
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try {
            iSetOnclick = (ISetOnclick) context;
        }catch (Exception e){

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = calendarBottomSheetModel.getRoot();
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
        });
    }



    public interface ISetOnclick{
        void SelectDate(int month,int date);
    }
}


