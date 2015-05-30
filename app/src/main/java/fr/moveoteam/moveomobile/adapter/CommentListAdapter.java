package fr.moveoteam.moveomobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Function;

/**
 * Created by Sylvain on 22/05/15.
 */
public class CommentListAdapter extends BaseAdapter {

    ArrayList<Comment> commentArrayList;
    LayoutInflater layoutInflater;
    ViewHolderComment viewHolderComment;
    Context context;

    public CommentListAdapter(Context context, ArrayList<Comment> commentArrayList){
        this.commentArrayList = commentArrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commentArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("position comment",position+"");

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.row_list_comments, null);
            viewHolderComment = new ViewHolderComment();
            viewHolderComment.avatarComment = (ImageView) convertView.findViewById(R.id.avatar_comment);
            viewHolderComment.userNameComment = (TextView) convertView.findViewById(R.id.username_comment);
            viewHolderComment.commentContent = (TextView) convertView.findViewById(R.id.comment_content);
            viewHolderComment.timeComment = (TextView) convertView.findViewById(R.id.time_comment);
            viewHolderComment.modifyText = (TextView) convertView.findViewById(R.id.modify_text);
            convertView.setTag(viewHolderComment);
        } else {
            viewHolderComment = (ViewHolderComment) convertView.getTag();
        }
        if(commentArrayList.get(position).getUserAvatarBase64() != null)
            viewHolderComment.avatarComment.setImageBitmap(Function.decodeBase64(commentArrayList.get(position).getUserAvatarBase64()));
        viewHolderComment.userNameComment.setText(commentArrayList.get(position).getUserFirstName()+" "+commentArrayList.get(position).getUserLastName());
        viewHolderComment.commentContent.setText(commentArrayList.get(position).getMessage());
        viewHolderComment.timeComment.setText(viewHolderComment.timeComment.getText()+" "+ commentArrayList.get(position).getDate());
        Log.e("position comment",position+"");
        UserDAO userDAO = new UserDAO(context);
        userDAO.open();
        if(commentArrayList.get(position).getIdUser() != userDAO.getUserDetails().getId())
            viewHolderComment.modifyText.setVisibility(View.INVISIBLE);
        userDAO.close();

        return convertView;
    }

    static class ViewHolderComment {
        ImageView avatarComment;
        TextView userNameComment, commentContent, timeComment;
        TextView modifyText;

    }

}
