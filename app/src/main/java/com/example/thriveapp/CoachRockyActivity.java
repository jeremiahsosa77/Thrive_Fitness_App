package com.example.thriveapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CoachRockyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;
    List<RockyMessage> messageList;
    MessageAdapter messageAdapter;
    OkHttpClient client = new OkHttpClient();
    static final String OPENAI_API_KEY = "API_KEY_GOES_HERE";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_rocky);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.rocky_recycler_view);
        messageEditText = findViewById(R.id.message_text);
        sendButton = findViewById(R.id.send_btn);
        findViewById(R.id.back_button).setOnClickListener(v -> finish());


        // Setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) ->{
            String question = messageEditText.getText().toString().trim();
            if(!question.isEmpty()) {
                addToChat(question, RockyMessage.SENT_BY_USER);
                messageEditText.setText("");
                try {
                    callAPI(question);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    void addToChat(String message, String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new RockyMessage(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void callAPI(String question) throws JSONException {
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject()
                .put("role", "system")
                .put("content", "You are Coach Rocky, a tough-love boxing trainer who motivates users with grit and heart. Respond with encouragement and fight metaphors."));
        messages.put(new JSONObject()
                .put("role", "user")
                .put("content", question));
        JSONObject json = new JSONObject()
                .put("model", "gpt-3.5-turbo")
                .put("messages", messages);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(CoachRockyActivity.this, "API Error", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        String botReply = res
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        addToChat(botReply.trim(), RockyMessage.SENT_BY_BOT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String errorBody = response.body().string();
                    runOnUiThread(() -> Toast.makeText(CoachRockyActivity.this, "Error " + response.code() + ": " + errorBody, Toast.LENGTH_LONG).show());
                    return;
                }
            }
        });
    }

}

