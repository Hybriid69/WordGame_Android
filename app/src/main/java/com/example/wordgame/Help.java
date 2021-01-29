package com.example.wordgame;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class Help extends AppCompatActivity {

    private EditText subjectText,messageText;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //link xml
        subjectText = findViewById(R.id.subjectTxt);
        messageText = findViewById(R.id.messageTxt);
        sendButton = findViewById(R.id.contactButton);

        //button click
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subjectText.getText().toString().matches(""))
                    {
                        subjectText.setError("Subject cannot be empty");
                    }
                    else if (messageText.getText().toString().matches(""))
                    {
                        messageText.setError("Message cannot be empty");
                    }
                    else {
                        sendEmail();
                    }
                }
            });
        try {
            // background image
            ConstraintLayout rlMain = findViewById(R.id.helpLayout);
            final ImageView imageView = new ImageView(this);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
            );
            imageView.setLayoutParams(params);
            rlMain.addView(imageView);

            Glide.with(this)
                    .load(R.raw.background_play)
                    .into(new ImageViewTarget<Drawable>(imageView) {
                        @Override
                        protected void setResource(@Nullable Drawable resource) {
                            imageView.setBackground(resource);
                            subjectText.bringToFront();
                            messageText.bringToFront();
                            sendButton.bringToFront();

                        }
                    });
        }
        catch (Exception ex)
        {
            Log.e("Main background", ex.toString());
        }
    }

    // data to add to email client
    private void sendEmail() {
        //Receiver
        String recipientList = "farmworks69@gmail.com";
        //Receiver list
        String[] recipients = recipientList.split(",");
        String subject = subjectText.getText().toString();
        String message = messageText.getText().toString();
        //Add details to intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        //Choose mail client
        startActivity(Intent.createChooser(intent, "Choose an email client"));
        subjectText.getText().clear();
        messageText.getText().clear();
    }
}
