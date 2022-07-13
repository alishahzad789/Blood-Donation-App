package com.example.bloodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private String uid="";
    private String name="";
    @Override
    protected void onPause() {
        super.onPause();
        String listenerID = "ChatActivity";
        CometChat.removeMessageListener(listenerID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String listenerID = "ChatActivity";
        CometChat.removeMessageListener(listenerID);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);

        Intent i=new Intent();
        uid =getIntent().getExtras().getString("UID");
        name = getIntent().getExtras().getString("NAME");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C10A0A")));

        EditText MsgSendBox=findViewById(R.id.MsgSendBox);
        ImageButton SendMsg=findViewById(R.id.sendbtn);
        ArrayList<MessageModel> LIST=new ArrayList<>();

        RecyclerView RV=findViewById(R.id.recycler_view);
        RV.setHasFixedSize(true);
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter da=new CustomAdapter(ChatActivity.this,LIST);
        RV.setAdapter(da);

        SendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Msg=MsgSendBox.getText().toString();
                Log.d("**SENDMSG", "MSG IS: " + Msg);
                String receiverid=uid;
                TextMessage textMessage = new TextMessage(receiverid, Msg,CometChatConstants.RECEIVER_TYPE_USER);
                CometChat.sendMessage(textMessage, new CometChat.CallbackListener <TextMessage> () {
                    @Override
                    public void onSuccess(TextMessage textMessage) {
                        try {
                            MessageModel M=new MessageModel(Msg,2);
                            LIST.add(M);
                            da.notifyDataSetChanged();

                        }catch (Exception e){
                            Log.d("**InsideSendMsg", e.toString());
                        }
                        Log.d("**cometchatsend", "Message sent successfully: " + textMessage.toString());
                    }
                    @Override
                    public void onError(CometChatException e) {
                        Log.d("**cometchatsend", "Message sending failed with exception: " + e.getMessage());
                    }
                });

            }
        });

        CometChat.addMessageListener("ChatActivity", new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage textMessage) {
                MessageModel m=new MessageModel(((TextMessage) textMessage).getText(),1);
                LIST.add(m);
                da.notifyDataSetChanged();
                Log.d("Listener", "Text message received successfully: " + textMessage.toString());
            }
            @Override
            public void onCustomMessageReceived(CustomMessage customMessage) {
                Log.d("Listener", "Custom message received successfully: " +customMessage.toString());
            }
        });



    }
}