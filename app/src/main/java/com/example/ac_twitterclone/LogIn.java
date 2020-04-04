package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogIn extends AppCompatActivity {

    private EditText edtMail;
    private EditText edtPassword;
    private Button btnSignUp;
    private Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setTitle("Log In");

        edtMail = findViewById(R.id.edtMail);
        edtPassword = findViewById(R.id.edtPwd);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtMail.getText().toString().equals("") ||
                edtPassword.getText().toString().equals("")){

                    FancyToast.makeText(LogIn.this, "Missing datas", Toast.LENGTH_SHORT,
                            FancyToast.INFO, true).show();

                }else {

                    final ProgressDialog progressDialog = new ProgressDialog(LogIn.this);
                    progressDialog.setMessage("Logging in");
                    progressDialog.show();

                    ParseUser.logInInBackground(edtMail.getText().toString(), edtPassword.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {

                                    if (user != null && e == null){

                                        FancyToast.makeText(LogIn.this, user.get("username") + "Succes",
                                                Toast.LENGTH_SHORT,
                                                FancyToast.SUCCESS, true).show();

                                        transitionToTrumpUsers();

                                    }else {

                                        FancyToast.makeText(LogIn.this, e.getMessage(),
                                                Toast.LENGTH_SHORT,
                                                FancyToast.ERROR, true).show();

                                    }

                                    progressDialog.dismiss();

                                }
                            });

                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }


    private void transitionToTrumpUsers() {


        Intent intent = new Intent(LogIn.this, TrumpUsers.class);
        startActivity(intent);
        finish();

    }
}
