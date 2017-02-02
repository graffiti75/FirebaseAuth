package br.android.cericatto.firebaseauth.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.android.cericatto.firebaseauth.model.Message;

/**
 * MessageAdapter.java.
 *
 * @author Rodrigo Cericatto
 * @since Jan 28, 2017
 */
public class MessageAdapter extends BaseAdapter {

    //--------------------------------------------------
    // Attributes
    //--------------------------------------------------

    private LayoutInflater mInflater;
    private List<Message> mItems;

    //--------------------------------------------------
    // Attributes
    //--------------------------------------------------

    public MessageAdapter(LayoutInflater inflater, List<Message> mensagens){
        mInflater = inflater;
        mItems = mensagens;
    }

    //--------------------------------------------------
    // BaseAdapter
    //--------------------------------------------------

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Message getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2,null);
            holder.text = (TextView) convertView.findViewById(android.R.id.text1);
            holder.user = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Message message = mItems.get(position);
        holder.text.setText(message.getText());
        holder.user.setText(message.getUser());
        return convertView;
    }

    //--------------------------------------------------
    // ViewHolder
    //--------------------------------------------------

    class ViewHolder{
        public TextView text;
        public TextView user;
    }
}