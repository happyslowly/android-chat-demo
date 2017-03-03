package info.codesert.chatpp;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


public class ChatAdapter extends BaseAdapter {
    private List<ChatMessage> messageList;
    private static LayoutInflater inflater = null;

    public ChatAdapter(Activity activity, List<ChatMessage> messageList) {
        this.messageList = messageList;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = messageList.get(position);
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.bubble_chat, null);
        }
        TextView senderMessageText = (TextView) view.findViewById(R.id.sender_message_text);
        TextView receiverMessageText = (TextView) view.findViewById(R.id.receiver_message_text);
        TextView senderNameText = (TextView) view.findViewById(R.id.sender_name_text);
        TextView receiverNameText = (TextView) view.findViewById(R.id.receiver_name_text);

        senderMessageText.setMovementMethod(LinkMovementMethod.getInstance());
        receiverMessageText.setMovementMethod(LinkMovementMethod.getInstance());

        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.bubble_layout);

        // if message is mine then align to right
        if (message.isMine) {
            senderNameText.setText(message.sender);
            receiverNameText.setText("");

            senderMessageText.setText(Html.fromHtml(message.body));
            senderMessageText.setVisibility(View.VISIBLE);
            receiverMessageText.setVisibility(View.INVISIBLE);
        }
        // If not mine then align to left
        else {
            senderNameText.setText("");
            receiverNameText.setText(message.sender);

            receiverMessageText.setText(Html.fromHtml(message.body));
            senderMessageText.setVisibility(View.INVISIBLE);
            receiverMessageText.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void add(ChatMessage message) {
        messageList.add(message);
    }
}
