package info.codesert.chatpp;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private static final String USER1 = "User1";
    private static final String USER2 = "User2";
    private static final String EMPTY_STRING = "";
    private static final String URL = "http://10.0.2.2:5000/v1/chatbotserv";
    public static final int TIMEOUT_MS = 30000;

    private EditText mMessageEditText;
    private Button mSendMessageButton;
    private ListView mMessageListView;
    private List<ChatMessage> mChatMessages;
    private ChatAdapter mChatAdapter;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        title = this.getTitle().toString();

        mMessageEditText = (EditText) findViewById(R.id.message_edit_text);
        mMessageEditText.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    mSendMessageButton.setEnabled(true);
                } else {
                    mSendMessageButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMessageListView = (ListView) findViewById(R.id.message_list_view);

        mSendMessageButton = (Button) findViewById(R.id.send_message_button);
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMessage();
            }
        });
        mSendMessageButton.setEnabled(false);

        // ----Set autoscroll of listview when a new message arrives----//
        mMessageListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mMessageListView.setStackFromBottom(true);

        mChatMessages = new ArrayList<ChatMessage>();
        mChatAdapter = new ChatAdapter(this, mChatMessages);
        mMessageListView.setAdapter(mChatAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    public void sendTextMessage() {
        String message = mMessageEditText.getEditableText().toString();
        if (!message.equalsIgnoreCase(EMPTY_STRING)) {
            final ChatMessage chatMessage = new ChatMessage(USER1, USER2, message, true);
            Log.d(TAG, chatMessage.toString());
            mMessageEditText.setText("");
            mChatAdapter.add(chatMessage);
            mChatAdapter.notifyDataSetChanged();
            waitForResponse(message);
        }
    }

    private void waitForResponse(String message) {
        JSONObject input = new JSONObject();
        try {
            input.put("message", message);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, input, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    setTitle(title);
                    receiveTextMessage(response.getString("message"));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setTitle(title);
                receiveTextMessage(getResources().getString(R.string.error_response));
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                        TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
        setTitle(R.string.typing);
    }

    public void receiveTextMessage(String text) {
        if (text != null && !text.equalsIgnoreCase(EMPTY_STRING)) {
            final ChatMessage chatMessage = new ChatMessage(USER2, USER1, text, false);
            Log.d(TAG, chatMessage.toString());
            mChatAdapter.add(chatMessage);
            mChatAdapter.notifyDataSetChanged();
        }
    }
}
