package com.example.pascal.apitest.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//spotify shit:
import com.example.pascal.apitest.Constants;
import com.example.pascal.apitest.MyTask;
import com.example.pascal.apitest.Process;
import com.example.pascal.apitest.R;
import com.example.pascal.apitest.RemoteFetch;
import com.example.pascal.apitest.model.User;
import com.example.pascal.apitest.util.BaseApp;
import com.example.pascal.apitest.util.PrefUtils;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

//http://kaaes.github.io/spotify-web-api-android/javadoc/
//https://github.com/thelinmichael/spotify-web-api-java/blob/master/examples/data/playlists/CreatePlaylistExample.java
//https://github.com/kaaes/spotify-web-api-android
//https://github.com/kaaes/spotify-web-api-android/blob/master/spotify-api/src/test/java/kaaes/spotify/webapi/android/SpotifyServiceTest.java
public class MainActivity extends AppCompatActivity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    public static final String ARRAYLIST = "ARRAYLIST";
    Context context = this;
    private TextView txtEta;
    private EditText limitLastfm, playlistfield;
    private Button getLastfm, setcontraints;
    private Spinner periodLastfm;
    private IntentFilter intentFilter;
    private String weather, token;
    ArrayList<String> tracks;
    ArrayList<String> artists;
    private MenuItem weatherbutton;
    private static final String CLIENT_ID = "a2bcc3e457d74e66b0de917374839125";
    private static final String REDIRECT_URI = "my-lastfm-app://callback";
    private List<String> users;
    private Player mPlayer;
    private static final int REQUEST_CODE = 1337;
    private String period;
    private String playlistname;

    //keytool -list -v -keystore "C:\Users\Jeroen\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
//https://www.programcreek.com/java-api-examples/index.php?api=kaaes.spotify.webapi.android.models.Pager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        artists = new ArrayList<String>();
        tracks = new ArrayList<>();
        artists = new ArrayList<>();
        getLastfm = findViewById(R.id.lastfm_btn);
        periodLastfm = findViewById(R.id.period_lastfm);
        limitLastfm =  findViewById(R.id.limit_lastfm);
        txtEta = findViewById(R.id.current_playlist);
        playlistname = PrefUtils.getStringPreference(MainActivity.this,"playlistname", null);
        if(playlistname!= null)        txtEta.setText(playlistname);
        period = "overall";

        ArrayList<String> periods = new ArrayList<String>( );
        periods.add("overall");
        periods.add("7day");
        periods.add("1month");
        periods.add("3month");
        periods.add("6month");
        periods.add("12month");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, periods);
        periodLastfm.setAdapter(adapter);
        periodLastfm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                period =periodLastfm.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent intent = getIntent();
        if(intent.getStringExtra(Constants.EXTRA_TOKEN) != null){
            token = intent.getStringExtra(Constants.EXTRA_TOKEN);
        }
        setcontraints =  findViewById(R.id.set_btn);

        setcontraints.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PrefAct.class);
                startActivity(i);
            }
        });

        getLastfm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                users = new ArrayList<>();
                List<User> userdatabase = BaseApp.userlistDAO.selectAll();
                if(userdatabase == null || userdatabase.size() == 0){
                    Toast.makeText(context, "Please add users in the set spotify constraints user manager", Toast.LENGTH_SHORT).show();
                }
                else{
                    for(User u: userdatabase){
                        if(u.getActive() != 0) {
                            users.add(u.getUsername());
                        }
                    }
                }
                boolean limiton = !limitLastfm.getText().toString().equals("");
                //todo make preference playlist or quick playlist chooser.
                if(limiton ){
                    String limit = limitLastfm.getText().toString();
                    new Process(context, new RemoteFetch() , users, period, limit).execute();
                }
                else{
                    new Process(context, new RemoteFetch() , users, period, "25").execute();
                }
            }
        });


        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_DONE_FETCHING_ETA);
        intentFilter.addAction(Constants.ACTION_DONE_FETCHING_WEATHER);
        intentFilter.addAction(Constants.ACTION_DONE_FETCHING_LASTFM);
        registerReceiver(broadcastReciever, intentFilter);
//        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
//                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
//                .build();
//        AuthenticationClient.openLoginInBrowser(this, request);
    }

    @Override
    public void onPause() {
        try {        unregisterReceiver(broadcastReciever);
        } catch(IllegalArgumentException e) {e.fillInStackTrace();}
        super.onPause();
    }

    @Override
    public void onResume() {
        registerReceiver(broadcastReciever, intentFilter);
        if(playlistname!= null)        txtEta.setText(playlistname);
        super.onResume();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        // Check if result comes from the correct activity
//        if (requestCode == REQUEST_CODE) {
//            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
//            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
//                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
//                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
//                    @Override
//                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
//                        mPlayer = spotifyPlayer;
//                        mPlayer.addConnectionStateCallback(MainActivity.this);
//                        mPlayer.addNotificationCallback(MainActivity.this);
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
//                    }
//                });
//            }
//        }
//    }

    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.fragment_stops, menu);
        weatherbutton = menu.findItem(R.id.menu_weather);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        if( weather != null) { //https://openweathermap.org/weather-conditions
            switch(weather){
                case "Clouds":
                    weatherbutton.setIcon(R.drawable.cloudy);
                    invalidateOptionsMenu();
                    break;
                case "Sunny":
                    weatherbutton.setIcon(R.drawable.sunny);
                    invalidateOptionsMenu();
                    break;
                case "Rain":
                    weatherbutton.setIcon(R.drawable.rainy);
                    invalidateOptionsMenu();
                    break;
                case "Clear":
                    weatherbutton.setIcon(R.drawable.sunny);
                    invalidateOptionsMenu();
                    break;
            }
        }
        return true;
    }

    private final BroadcastReceiver broadcastReciever = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(Constants.ACTION_DONE_FETCHING_ETA)) {
                if(intent.getStringExtra(Constants.EXTRA_ETA) != null){
                    String eta = intent.getStringExtra(Constants.EXTRA_ETA);
                    txtEta.setText(eta);
                }
            }
            if (action.equals(Constants.ACTION_DONE_FETCHING_WEATHER)) {
                if(intent.getStringExtra(Constants.EXTRA_WEATHER) != null){
                    weather = intent.getStringExtra(Constants.EXTRA_WEATHER);
                    txtEta.setText(weather);
                    invalidateOptionsMenu();
                }
            }
            if (action.equals(Constants.ACTION_DONE_FETCHING_LASTFM)) {
                if(intent.getStringArrayListExtra(Constants.EXTRA_LASTFM) != null){

                    if(intent.getStringArrayListExtra(Constants.EXTRA_TRACKS) != null){
                        tracks = intent.getStringArrayListExtra(Constants.EXTRA_TRACKS);
                    }
                    if(intent.getStringArrayListExtra(Constants.EXTRA_ARTIST) != null){
                       artists = intent.getStringArrayListExtra(Constants.EXTRA_TRACKS);
                    }
//                    playlistid = PrefUtils.getStringPreference(MainActivity.this,"playlistid", null);
                    playlistname = PrefUtils.getStringPreference(MainActivity.this,"playlistname", null);

                    if(playlistname == null){
                        Toast.makeText(context, "Please set an activeplaylist by going to setting spotify constraints and pressing manage playlist", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<String> songs = intent.getStringArrayListExtra(Constants.EXTRA_LASTFM);
                    setTracksAndArtists(songs);
                    new MyTask(token, tracks, artists, users, playlistname).execute("");
                    Intent i = new Intent(context, ListViewActivity.class);
                    i.putExtra(ARRAYLIST, songs);
                    startActivity(i);
                }
                else{
                }
            }
        }
    };

    private void setTracksAndArtists(ArrayList<String> songs) {
        for(int i = 0; i<songs.size(); i++){
            String s = songs.get(i);
            s = s.substring(s.indexOf(":") + 1);
            s = s.substring(0, s.indexOf("-"));
            artists.add(s);
            s = songs.get(i);
            s = s.substring(s.indexOf("-") + 1);
            tracks.add(s);
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        mPlayer.playUri(null, "spotify:track:4DLD8URKoqueOdQ4JWtvZ8", 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");

    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Uri uri = intent.getData();
//        if (uri != null) {
//            AuthenticationResponse response = AuthenticationResponse.fromUri(uri);
//            switch (response.getType()) {
//                // Response was successful and contains auth token
//                case TOKEN:
////                    Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
////                    Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
////                        @Override
////                        public void onInitialized(SpotifyPlayer spotifyPlayer) {
////                            mPlayer = spotifyPlayer;
////                            mPlayer.addConnectionStateCallback(MainActivity.this);
////                            mPlayer.addNotificationCallback(MainActivity.this);
////                        }
////
////                        @Override
////                        public void onError(Throwable throwable) {
////                            Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
////                        }
////                    });
//                    // Handle successful response
//                    token = response.getAccessToken();
//                    createPlaylist();
//
//
//                    break;
//
//                // Auth flow returned an error
//                case ERROR:
//                    // Handle error response
//                    break;
//
//                // Most likely auth flow was cancelled
//                default:
//                    // Handle other cases
//            }
//        }
//    }

    private void createPlaylist() {
    }
}
