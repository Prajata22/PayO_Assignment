package com.applex.payo_assignment.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applex.payo_assignment.R;
import com.applex.payo_assignment.Models.UserModel;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.ProgrammingViewHolder> {

    private final ArrayList<UserModel> mList;
    private OnItemLongClickListener mListener;

    public interface OnItemLongClickListener { void onLongCLick(int position); }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) { mListener = listener; }

    public RecyclerAdapter(ArrayList<UserModel> list) { this.mList = list; }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ProgrammingViewHolder(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {
        UserModel currentItem = mList.get(position);

        holder.id.setText("ID : " + currentItem.getId());

        if(currentItem.getFirst_name() != null && currentItem.getLast_name() != null
                && !currentItem.getFirst_name().isEmpty() && !currentItem.getLast_name().isEmpty()) {
            holder.name.setText(currentItem.getFirst_name() + " " + currentItem.getLast_name());
        }

        if(currentItem.getEmail() != null && !currentItem.getEmail().isEmpty()) {
            holder.email.setText("Email : " + currentItem.getEmail());
        }

        if (currentItem.getAvatar() != null && !currentItem.getAvatar().isEmpty()) {
            Picasso.get().load(currentItem.getAvatar()).error(R.drawable.ic_account_circle_black_24dp).into(holder.profile_pic);
        }
        else {
            holder.profile_pic.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ProgrammingViewHolder extends RecyclerView.ViewHolder {

        TextView name, id, email;
        ImageView profile_pic;

        private ProgrammingViewHolder(@NonNull View view, OnItemLongClickListener listener) {
            super(view);

            name = view.findViewById(R.id.name);
            id = view.findViewById(R.id.id);
            email = view.findViewById(R.id.email);
            profile_pic = view.findViewById(R.id.profile_pic);

            PushDownAnim.setPushDownAnimTo(view)
                .setScale(PushDownAnim.MODE_STATIC_DP, 3)
                .setOnLongClickListener(view1 -> {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onLongCLick(position);
                        }
                    }
                    return false;
                });
        }
    }
}