package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


public class MainActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtMail;
    private EditText edtPassword;
    private Button btnSignUp;
    private Button btnLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sign Up");

        edtMail = findViewById(R.id.edtMail);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPwd);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    signUp();
                }

                return false;
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogIn.class);
                startActivity(intent);
            }
        });

        if (ParseUser.getCurrentUser() != null) {
            transitionToTrumpUsers();
        }


    }

    private void transitionToTrumpUsers() {

        Intent intent = new Intent(MainActivity.this, TrumpUsers.class);
        startActivity(intent);
        finish();

    }

    private void signUp() {

        if (edtMail.getText().toString().equals("") ||
                edtName.getText().toString().equals("") ||
                edtPassword.getText().toString().equals("")) {

            FancyToast.makeText(MainActivity.this, "Missing datas", Toast.LENGTH_SHORT,
                    FancyToast.INFO, true).show();

        } else {

            ParseUser appUser = new ParseUser();
            appUser.setEmail(edtMail.getText().toString());
            appUser.setUsername(edtName.getText().toString());
            appUser.setPassword(edtPassword.getText().toString());

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Signing up" + edtName.getText().toString());
            progressDialog.show();

            appUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        FancyToast.makeText(MainActivity.this, "Succes", Toast.LENGTH_SHORT,
                                FancyToast.SUCCESS, true).show();

                        transitionToTrumpUsers();

                    } else {

                        FancyToast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT,
                                FancyToast.ERROR, true).show();

                    }

                    progressDialog.dismiss();

                }
            });

        }

    }

}