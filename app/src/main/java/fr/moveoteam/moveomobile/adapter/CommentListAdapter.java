package fr.moveoteam.moveomobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.dao.UserDAO;
import fr.moveoteam.moveomobile.model.Comment;
import fr.moveoteam.moveomobile.model.Function;

/**
 * Created by Sylvain on 22/05/15.
 */
public class CommentListAdapter extends BaseAdapter {

    private ArrayList<Comment> commentArrayList;
    private LayoutInflater layoutInflater;
    ViewHolderComment viewHolderComment;
    private Context context;
    private int userId;

    public CommentListAdapter(Context context, ArrayList<Comment> commentArrayList, int userId){
        this.commentArrayList = commentArrayList;
        this.context = context;
        this.userId = userId;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commentArrayList != null?commentArrayList.size():1;
    }

    @Override
    public Object getItem(int position) {
        return commentArrayList != null?commentArrayList.get(position):null;
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

        if(commentArrayList == null){
            viewHolderComment.avatarComment.setVisibility(View.GONE);
            viewHolderComment.userNameComment.setText("Pas de commentaires");
            viewHolderComment.commentContent.setVisibility(View.GONE);
            viewHolderComment.timeComment.setVisibility(View.GONE);
            viewHolderComment.modifyText.setVisibility(View.GONE);
        }
        else {
            if (commentArrayList.get(position).getUserAvatarBase64() != null){
                viewHolderComment.imageUrl = commentArrayList.get(position).getUserAvatarBase64();
                new DownloadImage().execute(viewHolderComment);
            }
            //viewHolderComment.avatarComment.setImageBitmap(Function.decodeBase64(commentArrayList.get(position).getUserAvatarBase64()));
            viewHolderComment.userNameComment.setText(commentArrayList.get(position).getUserFirstName() + " " + commentArrayList.get(position).getUserLastName());
            viewHolderComment.commentContent.setText(commentArrayList.get(position).getMessage());
            viewHolderComment.timeComment.setText("Il y a " + Function.differenceDate(commentArrayList.get(position).getDate()));
            Log.e("position comment", position + "");
            UserDAO userDAO = new UserDAO(context);
            userDAO.open();
            if (commentArrayList.get(position).getIdUser() != userId){
                viewHolderComment.modifyText.setVisibility(View.INVISIBLE);
            }
            else{
                viewHolderComment.modifyText.setVisibility(View.VISIBLE);
            }
            userDAO.close();

        }
        return convertView;
    }

    static class ViewHolderComment {
        ImageView avatarComment;
        TextView userNameComment, commentContent, timeComment;
        TextView modifyText;
        Bitmap bitmap;
        String imageUrl;
    }

    private class DownloadImage extends AsyncTask<ViewHolderComment, Void, ViewHolderComment> {

        URL urlImage;
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        @Override
        protected ViewHolderComment doInBackground(ViewHolderComment... args) {
            ViewHolderComment viewHolderTripImage = args[0];
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://moveo.besaba.com/"+viewHolderTripImage.imageUrl);
            HttpResponse response;
            try {
                response = (HttpResponse)client.execute(request);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
                InputStream inputStream = bufferedEntity.getContent();
                viewHolderTripImage.bitmap  = BitmapFactory.decodeStream(inputStream);

                //urlImage = new URL("http://moveo.besaba.com/"+viewHolderTripImage.imageUrl);
               // connection = (HttpURLConnection) urlImage.openConnection();
                /*if (connection != null) {
                    inputStream = connection.getInputStream();
                    viewHolderTripImage.bitmap = BitmapFactory.decodeStream(inputStream);
                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            return viewHolderTripImage;
        }

        @Override
        protected void onPostExecute(ViewHolderComment result) {
            if (result.bitmap == null) {
                result.avatarComment.setImageResource(R.drawable.default_avatar);
            } else {
                result.avatarComment.setImageBitmap(result.bitmap);
            }
        }
    }

}
