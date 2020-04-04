package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrumpUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;

    private ArrayList<String> mArrayList;

    private ArrayAdapter<String> mArrayAdapter;

    private String followedUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trump_users);

        FancyToast.makeText(this, "Welcome " + ParseUser.getCurrentUser().getUsername(),
                Toast.LENGTH_SHORT, FancyToast.INFO,true).show();

        mListView = findViewById(R.id.listView);

        mArrayList = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, mArrayList);

        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);


 //       try {


            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {

                    if (e == null) {

                        if (objects.size() > 0) {

                            for (ParseUser user : objects) {

                                mArrayList.add(user.getUsername());

                            }

                            mListView.setAdapter(mArrayAdapter);

                            for (String trumpUser : mArrayList){

                                if (ParseUser.getCurrentUser().getList("fanOf") != null) {

                                    if (ParseUser.getCurrentUser().getList("fanOf").contains(trumpUser)) {
                                        followedUser = followedUser + trumpUser + "\n";


                                        mListView.setItemChecked(mArrayList.indexOf(trumpUser), true);

                                        FancyToast.makeText(TrumpUsers.this, ParseUser.getCurrentUser().getUsername() + " is following " + followedUser, Toast.LENGTH_SHORT,
                                                FancyToast.INFO, true).show();

                                    }
                                }

                            }


                        }

                    }

                }
            });
 //       }catch ()



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logoutUser:

                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {

                        Intent intent = new Intent(TrumpUsers.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

                break;

            case R.id.sendTrumpItem:
                Intent intent = new Intent(TrumpUsers.this, SendTrumpAct.class);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;

        if (checkedTextView.isChecked()){

            FancyToast.makeText(TrumpUsers.this, mArrayList.get(position) + " is followed", Toast.LENGTH_SHORT,
                    FancyToast.INFO, true).show();

            ParseUser.getCurrentUser().add("fanOf", mArrayList.get(position));

        }else {

            FancyToast.makeText(TrumpUsers.this, mArrayList.get(position) + " is unfollowed", Toast.LENGTH_SHORT,
                    FancyToast.INFO, true).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(mArrayList.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);

        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){

                    FancyToast.makeText(TrumpUsers.this, "saved", Toast.LENGTH_SHORT,
                            FancyToast.SUCCESS, true).show();

                }
            }
        });

    }
}
