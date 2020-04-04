package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTrumpAct extends AppCompatActivity implements View.OnClickListener{

    private EditText edtTrump;

    private ListView listViewTrump;

    private Button btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_trump);

        edtTrump = findViewById(R.id.edtTrump);

        listViewTrump = findViewById(R.id.listViewTrumps);
        btnView = findViewById(R.id.btnView);
        btnView.setOnClickListener(this);

//        HashMap<String, Integer> numbers = new HashMap<>();
//        numbers.put("Number1", 1);
//        numbers.put("Number2", 2);

    }

    public void tappedTrump(View view) {

        ParseObject parseObject = new ParseObject("MyTrump");
        parseObject.put("tweet", edtTrump.getText().toString());
        parseObject.put("user", ParseUser.getCurrentUser().getUsername());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Trumping...");
        progressDialog.show();
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){

                    FancyToast.makeText(SendTrumpAct.this, ParseUser.getCurrentUser().getUsername() +
                            "'s trump" + "(" + edtTrump.getText().toString() + ")" + " is trumped!!!", Toast.LENGTH_LONG,
                            FancyToast.SUCCESS, true).show();

                }else {

                    FancyToast.makeText(SendTrumpAct.this, e.getMessage(), Toast.LENGTH_LONG,
                            FancyToast.ERROR, true).show();

                }

                progressDialog.dismiss();
            }
        });

    }


    @Override
    public void onClick(View v) {

        final ArrayList<HashMap<String, String>> trumplist = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(SendTrumpAct.this, trumplist,
                android.R.layout.simple_list_item_2, new String[]{"trumpUserName", "trumpValue"},
                new int[]{android.R.id.text1, android.R.id.text2});
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTrump");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    for (ParseObject trumpObject : objects){

                        HashMap<String, String> userTrump = new HashMap<>();
                        userTrump.put("trumpUserName", trumpObject.getString("user"));
                        userTrump.put("trumpValue", trumpObject.getString("tweet"));
                        trumplist.add(userTrump);

                    }

                    listViewTrump.setAdapter(adapter);

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
