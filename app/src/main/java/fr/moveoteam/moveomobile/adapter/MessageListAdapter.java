package fr.moveoteam.moveomobile.adapter;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.moveoteam.moveomobile.R;
import fr.moveoteam.moveomobile.activity.SendMessageActivity;
import fr.moveoteam.moveomobile.model.Dialog;

/**
 * Created by Sylvain on 31/05/15.
 */
public class MessageListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    ArrayList<Dialog> dialogArrayList;
    ViewHolderMessage viewHolderMessage;
    Context context;

    public MessageListAdapter(Context context, ArrayList<Dialog> dialogArrayList){
        this.dialogArrayList = dialogArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dialogArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dialogArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){

            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.row_list_messages, null);
            viewHolderMessage = new ViewHolderMessage();
            viewHolderMessage.userName = (TextView) convertView.findViewById(R.id.message_username);
            viewHolderMessage.content = (TextView) convertView.findViewById(R.id.message_content);
            viewHolderMessage.date = (TextView) convertView.findViewById(R.id.date_message);
            viewHolderMessage.reply = (TextView) convertView.findViewById(R.id.reply_text);
			convertView.setTag(viewHolderMessage);
        }else{
            viewHolderMessage = (ViewHolderMessage) convertView.getTag();
        }

        viewHolderMessage.userName.setText(dialogArrayList.get(position).getRecipientFirstName()+" "+dialogArrayList.get(position).getRecipientLastName());
        viewHolderMessage.content.setText(dialogArrayList.get(position).getMessage());
        viewHolderMessage.date.setText(dialogArrayList.get(position).getDate());
        viewHolderMessage.reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SendMessageActivity.class);
						intent.putExtra("name",dialogArrayList.get(position).getRecipientFirstName()+" "+dialogArrayList.get(position).getRecipientLastName());
						intent.putExtra("friendId",Integer.toString(dialogArrayList.get(position).getRecipientId()));
                        context.startActivity(intent);
						
                    }
                });
        return convertView;
    }

    static class ViewHolderMessage{
        TextView userName;
        TextView content;
        TextView date;
        TextView reply;
    }
}
