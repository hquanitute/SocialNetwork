package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.socialnetwork.Objects.Friend;
import com.example.socialnetwork.R;

import java.util.List;

public class FriendAdapter  extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Friend> friends;
    public FriendAdapter(Context context, int layout, List<Friend> friends) {
        this.context = context;
        this.layout = layout;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.row_friend,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvAvatar=convertView.findViewById(R.id.tvAvatar);
            viewHolder.tvName=convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Friend friend = friends.get(position);
        viewHolder.tvAvatar.setText(friend.getIdfriend());
        viewHolder.tvName.setText(friend.getName_friend());
        return convertView;
    }
    private class ViewHolder {
        TextView tvAvatar,tvName;
    }
}
