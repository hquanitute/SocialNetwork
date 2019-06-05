package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialnetwork.Objects.Post;
import com.example.socialnetwork.R;

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
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(layout,null);
        TextView tv_name = convertView.findViewById(R.id.tv_NameAccount);
        TextView tv_status = convertView.findViewById(R.id.tv_Content);
        ImageView imageView= convertView.findViewById(R.id.imageView);

        Post post = posts.get(position);
        tv_name.setText(post.getAccount_name());
        tv_status.setText(post.getText());
//        if(post.getImage()!=null){
//            imageView.setVisibility(View.VISIBLE);
//            imageView.setImageDrawable("Kha làm ở đây");
//        }
        return convertView;
    }
}
