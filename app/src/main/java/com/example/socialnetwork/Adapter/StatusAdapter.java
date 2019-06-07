package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.socialnetwork.Objects.Comment;
import com.example.socialnetwork.Objects.Post;
import com.example.socialnetwork.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatusAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Post> posts;
    private CommentAdapter adapter;
    private String displayname;
    private DatabaseReference mDatabase;

    public StatusAdapter(Context context, int layout, List<Post> posts,  String displayname) {
        this.context = context;
        this.layout = layout;
        this.posts = posts;
        this.adapter = adapter;
        this.displayname = displayname;
    }

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
            viewHolder.lv_comments= convertView.findViewById(R.id.comments);
            viewHolder.ed_comment=convertView.findViewById(R.id.et_comment);
            viewHolder.btnComment=convertView.findViewById(R.id.btnComment);


            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Post post = posts.get(position);
        viewHolder.tv_NameAccount.setText(post.getAccount_name());
        viewHolder.tv_Content.setText(post.getText());
        viewHolder.imageView.setImageResource(0);
        if(post.getComments()!=null&&post.getComments().size()>0){
            adapter = new CommentAdapter(context,R.layout.comment_row,post.getComments());
            viewHolder.lv_comments.setAdapter(adapter);
        }
        if(post.getImage()!=null){
            viewHolder.imageView.setVisibility(View.VISIBLE);
            //imageView.setImageDrawable("Kha làm ở đây");
            Picasso.with(context).load(post.getImage().toString()).into(viewHolder.imageView);
        }
        viewHolder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment(post,viewHolder.ed_comment.getText().toString(),displayname);
            }
        });
        return convertView;
    }
    private class ViewHolder {
        TextView tv_NameAccount,tv_Content;
        ImageView imageView;
        ListView lv_comments;
        EditText ed_comment;
        Button btnComment;
    }
    private void sendComment(Post oldPost, String comment, String account_name){
        mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        Date date = new Date();
        Comment comment1 = new Comment(String.valueOf(System.currentTimeMillis()),account_name,oldPost.getPost_id(),date,comment);
        if(oldPost.getComments()!=null){
            oldPost.getComments().add(comment1);
        }
        else {
            //code dài dòng
            List<Comment> comments = new ArrayList<Comment>();
            comments.add(comment1);
            oldPost.getComments().add(comment1);
        }
        mDatabase.child(oldPost.getPost_id()).setValue(oldPost);
    }
}
