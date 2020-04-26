package com.androidtutorialshub.loginregister.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.adapters.SongsRecyclerAdapter;
import com.androidtutorialshub.loginregister.model.Song;
import com.androidtutorialshub.loginregister.sql.SongDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lalit on 10/10/2016.
 */

public class SongListActivity extends AppCompatActivity {

    private AppCompatActivity activity = SongListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewSongs;
    private List<Song> listSongs;
    private SongsRecyclerAdapter songsRecyclerAdapter;
    private SongDBHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().setTitle("");
        initViews();
        initObjects();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewSongs = (RecyclerView) findViewById(R.id.recyclerViewUsers);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listSongs = new ArrayList<>();
        songsRecyclerAdapter = new SongsRecyclerAdapter(listSongs);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewSongs.setLayoutManager(mLayoutManager);
        recyclerViewSongs.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSongs.setHasFixedSize(true);
        recyclerViewSongs.setAdapter(songsRecyclerAdapter);
        databaseHelper = new SongDBHelper(activity);

        String emailFromIntent = getIntent().getStringExtra("NAME");
        textViewName.setText(emailFromIntent);

        getDataFromSQLite();
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listSongs.clear();
                listSongs.addAll(databaseHelper.getAllSongs());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                songsRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
