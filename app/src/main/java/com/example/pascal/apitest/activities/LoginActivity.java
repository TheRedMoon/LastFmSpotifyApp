package com.example.pascal.apitest.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pascal.apitest.R;
import com.example.pascal.apitest.util.BaseApp;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "a2bcc3e457d74e66b0de917374839125";
    private static final String REDIRECT_URI = "my-lastfm-app://callback";
    private Button login, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login_btn);
        skip = findViewById(R.id.skip_btn);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AuthenticationRequest.Builder builder =
                        new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

                builder.setScopes(new String[]{"user-read-private", "playlist-modify-public", "streaming"}); //https://developer.spotify.com/web-api/using-scopes/
                AuthenticationRequest request = builder.build();

                AuthenticationClient.openLoginInBrowser(LoginActivity.this, request);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = AuthenticationResponse.fromUri(uri);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.e("Token achieved", "Success");
                    Intent i = new Intent(this, MainActivity.class);
                    BaseApp.setToken(LoginActivity.this, response.getAccessToken());
                    startActivity(i);
                    // Handle successful response
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}
