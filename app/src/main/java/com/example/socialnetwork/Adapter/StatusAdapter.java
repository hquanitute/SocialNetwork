package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialnetwork.Objects.Post;
import com.example.socialnetwork.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StatusAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Post> posts;

    public StatusAdapter(Context context, int layout, List<Post> posts) {
        this.context = context;
        this.layout = layout;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
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
        //LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.status_row,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_NameAccount = convertView.findViewById(R.id.tv_NameAccount);
            viewHolder.tv_Content  = convertView.findViewById(R.id.tv_Content);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Post post = posts.get(position);
        viewHolder.tv_NameAccount.setText(post.getAccount_name());
        viewHolder.tv_Content.setText(post.getText());
        if(post.getImage()!=""){
            viewHolder.imageView.setVisibility(View.VISIBLE);
            //imageView.setImageDrawable("Kha làm ở đây");
            Picasso.with(context).load(post.getImage().toString()).into(viewHolder.imageView);
        }
        return convertView;
    }
    public class ViewHolder {
        TextView tv_NameAccount,tv_Content;
        ImageView imageView;
        Button btnLike,btnComment,btnShare;
    }
}
