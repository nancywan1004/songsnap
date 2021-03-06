package com.androidtutorialshub.loginregister;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.androidtutorialshub.loginregister.activities.TimerPageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.androidtutorialshub.loginregister.activities.SongListActivity;
import com.androidtutorialshub.loginregister.sql.SongDBHelper;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import com.androidtutorialshub.loginregister.model.Song;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity = MainActivity.this;

    private String CLIENT_ID = "CLIENT_ID";
    private String REDIRECT_URI = "REDIRECT_URI";
    private String currClientID;
    private String currRedirectURI;
    private SpotifyAppRemote mSpotifyAppRemote;
    private String text = "";
    private SongDBHelper songDBHelper;

    Button new_snap;
    ConnectionParams connectionParams;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(CLIENT_ID, currClientID);
        savedInstanceState.putString(REDIRECT_URI, currRedirectURI);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "current playing    " + text, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new_snap = (Button) findViewById(R.id.button);
        new_snap.setEnabled(false);
        new_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTimerPage = new Intent(getApplicationContext(), TimerPageActivity.class);
                intentTimerPage.putExtra("CLIENT_ID", currClientID);
                intentTimerPage.putExtra("REDIRECT_URI", currRedirectURI);
                startActivity(intentTimerPage);
            }
        });

        Button snap_his = (Button) findViewById(R.id.button2);
        snap_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(getApplicationContext(), SongListActivity.class);
                startActivity(intentMain);
                Log.i("Content "," Go to History page. ");
            }
        });

        initObjects();
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        songDBHelper = new SongDBHelper(activity);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tippage) {
            // Handle the camera action
            Intent intentMain = new Intent(MainActivity.this,
                    MainActivity.class);
            MainActivity.this.startActivity(intentMain);
            Log.i("Content "," Main layout ");
        } else if (id == R.id.nav_history) {
            Intent intentMain = new Intent(MainActivity.this,
                    SongListActivity.class);
            MainActivity.this.startActivity(intentMain);
            Log.i("Content "," Main layout ");

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        currClientID = getIntent().getStringExtra("CLIENT_ID");
        currRedirectURI = getIntent().getStringExtra("REDIRECT_URI");
        connectionParams =
                new ConnectionParams.Builder(currClientID)
                        .setRedirectUri(currRedirectURI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void connected() {

        // Play a playlist
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                    @Override
                    public void onEvent(final PlayerState playerState) {
                        final Track track = playerState.track;
                        final Song song = new Song();

                        song.setId(track.uri);
                        song.setName(track.name);
                        song.setArtist(track.artist.name);
                        song.setAlbum(track.album.name);

                        songDBHelper.addSong(song);

                        final ImageView imgView = (ImageView) findViewById(R.id.trackImg);
                        mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(new CallResult.ResultCallback<Bitmap>() {
                            @Override
                            public void onResult(Bitmap bitmap) {
                                imgView.setImageBitmap(bitmap);
                            }
                        });

                        final Button button = (Button) findViewById(R.id.start_playing);

                        Button prev = (Button) findViewById(R.id.prev);
                        Button next = (Button) findViewById(R.id.next);

                        if (playerState.isPaused)
                            button.setBackground(getDrawable(R.drawable.play_button));
                        else
                            button.setBackground(getDrawable(R.drawable.pause_button));


                        if (prev != null) {
                            prev.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mSpotifyAppRemote.getPlayerApi().skipPrevious();
                                }
                            });
                        }
                        if (next != null) {
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mSpotifyAppRemote.getPlayerApi().skipNext();
                                }
                            });
                        }

                        if (playerState.isPaused) {
                            if (button != null) {
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:29z6MBXfjJho9J2vQ32R8J");
                                        new_snap.setEnabled(true);
                                    }
                                });
                            }
                        }
                        else {
                            if (button != null) {
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mSpotifyAppRemote.getPlayerApi().pause();
                                    }
                                });
                            }

                            if (track != null) {
                                Log.d("MainActivity", track.name + " by " + track.artist.name);
                            }
                        }
                    }
                });

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                    @Override
                    public void onEvent(PlayerState playerState) {
                        final Track track = playerState.track;
                        if (track != null) {
                            text = track.name;
                            Log.d("MainActivity", track.name + " by " + track.artist.name);
                        }
                    }
                });
    }

}
