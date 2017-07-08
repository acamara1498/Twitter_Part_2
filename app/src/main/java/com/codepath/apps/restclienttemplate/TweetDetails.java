package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetails extends AppCompatActivity {


    TwitterClient client;
    EditText etCompose2;
    TextView tvCharCount2;
    Button btnReply;
    ImageView ivReply;
    Context context;
    TextView tvUserName2;
    TextView tvHandle2;
    TextView tvBody2;
    TextView tvTime;
    TextView tvWhoReply;
    Tweet tweet;
    String message;
    long uid;
    String screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        etCompose2 = (EditText) findViewById(R.id.etCompose2);
        tvCharCount2 = (TextView) findViewById(R.id.tvCharCount2);
        etCompose2.addTextChangedListener(TextEditorWatcher);
        btnReply = (Button) findViewById(R.id.btnReply);
        ivReply = (ImageView) findViewById(R.id.ivReply);
        tvWhoReply = (TextView) findViewById(R.id.tvWhoReply);

        screenName = getIntent().getStringExtra("screen_name");
        String userName = getIntent().getStringExtra("name");
        String body = getIntent().getStringExtra("body");
        String profileImageUrl = getIntent().getStringExtra("profileImageUrl");
        String createdAt = getIntent().getStringExtra("createdAt");
        uid = getIntent().getLongExtra("tweet_id", 0);

        this.context = this;

        Glide.with(context)
                .load(profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 50, 0))
                .into(ivReply);


        tvHandle2 = (TextView) findViewById(R.id.tvHandle2);
        tvHandle2.setText("@" +screenName);

        tvUserName2 = (TextView) findViewById(R.id.tvUserName2);
        tvUserName2.setText(userName);

        tvBody2 = (TextView) findViewById(R.id.tvBody2);
        tvBody2.setText(body);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText(createdAt);


        tvWhoReply.setText("Replying to @"+ screenName);


        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        ivReply = (ImageView) findViewById(R.id.ivReply);

    }

    private final TextWatcher TextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharCount2.setText(String.valueOf(140-s.length()));

            if (s.length()>140)
            {
                tvCharCount2.setTextColor(Color.RED);
                btnReply.setAlpha(.5f);
                btnReply.setBackgroundColor(Color.GRAY);
                btnReply.setClickable(false);
                Toast.makeText(TweetDetails.this, "Too many chaacters", Toast.LENGTH_SHORT).show();;
            } else
            {
                tvCharCount2.setTextColor(Color.BLACK);
                btnReply.setAlpha(1f);
                btnReply.setClickable(true);
            }

        }

        public void afterTextChanged(Editable s) {
        }
    };

    public void makeReply(View view) {
        client = TwitterApp.getRestClient();
        message = ("@"+screenName + " "+etCompose2.getText().toString());
        client.sendTweet(message, uid, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try {
                    // TODO
                    tweet = Tweet.fromJSON(response);
                    Intent intent = new Intent(TweetDetails.this, TimelineActivity.class);
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TwitterClient", response.toString());

            }

        });
    }

}
