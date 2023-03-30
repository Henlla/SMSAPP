package com.demo1.smsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo1.smsapp.R;
import com.demo1.smsapp.databinding.ListTakeMarkDetailsBinding;
import com.demo1.smsapp.models.StudentTakeMarkModel;
import com.demo1.smsapp.utils.InputFilterMinMax;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListStudentTakeMarkAdapter extends RecyclerView.Adapter<ListStudentTakeMarkAdapter.ListStudentTakeMarkVH> {
    List<StudentTakeMarkModel> studentTakeMarkModels;
    private FirebaseStorage firebaseStorage;
    Context context;

    public void setData(List<StudentTakeMarkModel> studentTakeMarkModels) {
        this.studentTakeMarkModels = studentTakeMarkModels;
        notifyDataSetChanged();
    }

    public List<StudentTakeMarkModel> getList() {
        return this.studentTakeMarkModels;
    }

    @NonNull
    @NotNull
    @Override
    public ListStudentTakeMarkVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ListTakeMarkDetailsBinding listTakeMarkDetailsBinding = ListTakeMarkDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        firebaseStorage = FirebaseStorage.getInstance();
        return new ListStudentTakeMarkVH(listTakeMarkDetailsBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ListStudentTakeMarkVH holder, @SuppressLint("RecyclerView") int position) {
        StudentTakeMarkModel studentTakeMarkModel = studentTakeMarkModels.get(position);
        StorageReference reference = firebaseStorage.getReference().child(studentTakeMarkModel.getStudent().getStudentByProfile().getAvatarPath());
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .error(R.drawable.image_notavailable)
                        .into(holder.listTakeMarkDetailsBinding.profileImage);
            }
        });
        holder.listTakeMarkDetailsBinding.tvStudentName.setText(studentTakeMarkModel.getStudent().getStudentByProfile().getFirstName() + " " + studentTakeMarkModel.getStudent().getStudentByProfile().getLastName());
        holder.listTakeMarkDetailsBinding.tvStudentCode.setText(studentTakeMarkModel.getStudent().getStudentCard());
        if(studentTakeMarkModel.getTimesUpdate() < 2){
            holder.listTakeMarkDetailsBinding.obj.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
            holder.listTakeMarkDetailsBinding.asm.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});

            if (studentTakeMarkModel.getAsm().equals(0.0)) {
                holder.listTakeMarkDetailsBinding.asm.setText("");
            } else {
                holder.listTakeMarkDetailsBinding.asm.setHint(String.valueOf(studentTakeMarkModel.getAsm()));
            }

            if (studentTakeMarkModel.getObj().equals(0.0)) {
                holder.listTakeMarkDetailsBinding.obj.setText("");
            } else {
                holder.listTakeMarkDetailsBinding.obj.setHint(String.valueOf(studentTakeMarkModel.getObj()));
            }


            holder.listTakeMarkDetailsBinding.obj.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().equals("")) {
                        studentTakeMarkModels.get(position).setObj(Double.valueOf(editable.toString()));
                    } else {
                        studentTakeMarkModels.get(position).setObj(0.0);
                    }
                }
            });

            holder.listTakeMarkDetailsBinding.asm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().equals("")) {
                        studentTakeMarkModels.get(position).setAsm(Double.valueOf(editable.toString()));
                    } else {
                        studentTakeMarkModels.get(position).setAsm(0.0);
                    }
                }
            });
        }else{
            if (studentTakeMarkModel.getAsm().equals(0.0)) {
                holder.listTakeMarkDetailsBinding.asm.setText("");
            } else {
                holder.listTakeMarkDetailsBinding.asm.setHint(String.valueOf(studentTakeMarkModel.getAsm()));
            }

            if (studentTakeMarkModel.getObj().equals(0.0)) {
                holder.listTakeMarkDetailsBinding.obj.setText("");
            } else {
                holder.listTakeMarkDetailsBinding.obj.setHint(String.valueOf(studentTakeMarkModel.getObj()));
            }
            holder.listTakeMarkDetailsBinding.obj.setEnabled(false);
            holder.listTakeMarkDetailsBinding.asm.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return studentTakeMarkModels.size() > 0 ? studentTakeMarkModels.size() : 0;
    }

    public static class ListStudentTakeMarkVH extends RecyclerView.ViewHolder {
        ListTakeMarkDetailsBinding listTakeMarkDetailsBinding;

        public ListStudentTakeMarkVH(ListTakeMarkDetailsBinding listTakeMarkDetailsBinding) {
            super(listTakeMarkDetailsBinding.getRoot());
            this.listTakeMarkDetailsBinding = listTakeMarkDetailsBinding;
        }
    }
}
