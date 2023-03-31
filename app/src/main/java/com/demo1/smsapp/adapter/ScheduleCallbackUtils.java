package com.demo1.smsapp.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import com.demo1.smsapp.dto.ScheduleDetailModel;

import java.util.List;

public class ScheduleCallbackUtils extends DiffUtil.Callback {

    List<ScheduleDetailModel> oldList,newList;

    public ScheduleCallbackUtils(List<ScheduleDetailModel> oldList, List<ScheduleDetailModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
