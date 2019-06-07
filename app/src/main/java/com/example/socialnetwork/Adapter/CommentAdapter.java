package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.socialnetwork.Objects.Comment;
import com.example.socialnetwork.R;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Comment> comments;

    public CommentAdapter(Context context, int layout, List<Comment> comments) {
        this.context = context;
        this.layout = layout;
        this.comments = comments;
    }

    public CommentAdapter() {
    }

    @Override
    public int getCount() {
        return comments.size();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.comment_row,parent,false);
            viewHolder =    new ViewHolder();
            viewHolder.tv_NameAccount = convertView.findViewById(R.id.tv_name);
            viewHolder.tv_Content  = convertView.findViewById(R.id.tv_content);
            viewHolder.tv_date = convertView.findViewById(R.id.tv_date);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment =comments.get(position);
        if(comment!=null){
            viewHolder.tv_NameAccount.setText(comment.getComment_account());
            viewHolder.tv_Content.setText(comment.getContent());
            viewHolder.tv_date.setText(comment.getCommenting_date().toString());
        }

        return convertView;
    }
    private class ViewHolder {
        TextView tv_NameAccount,tv_Content,tv_date;
    }
}
