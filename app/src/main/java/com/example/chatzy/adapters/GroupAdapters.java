package com.example.chatzy.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatzy.databinding.ItemContainerRecentGroupBinding;
import com.example.chatzy.listeners.GroupListener;
import com.example.chatzy.models.Group;

import java.util.List;

public class GroupAdapters extends RecyclerView.Adapter<GroupAdapters.UserViewHolder>{

    private final List<Group> groupsList;
    private final GroupListener groupListener;

    public GroupAdapters(List<Group> groupsList, GroupListener groupListener) {
        this.groupsList = groupsList;
        this.groupListener = groupListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerRecentGroupBinding itemContainerUserBinding = ItemContainerRecentGroupBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(groupsList.get(position));
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentGroupBinding binding;

        UserViewHolder(ItemContainerRecentGroupBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        void setUserData(Group group) {
            binding.textName.setText("Nhóm: " +group.name);
            binding.imageProfile1.setImageBitmap(getGroupImage(group.image1));
            binding.imageProfile2.setImageBitmap(getGroupImage(group.image2));
            binding.textRecentMessage.setText(group.message);
            binding.textDateTime.setText(" · " +group.time);

            binding.getRoot().setOnClickListener(v -> groupListener.onGroupClicked(group));
        }
    }

    private Bitmap getGroupImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
