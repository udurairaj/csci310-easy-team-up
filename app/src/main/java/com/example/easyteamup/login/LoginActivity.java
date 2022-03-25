package com.example.easyteamup.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyteamup.MainActivity;
import com.example.easyteamup.R;
import com.example.easyteamup.User;
import com.example.easyteamup.UserTable;

public class LoginActivity extends AppCompatActivity {

    private Boolean successLogin = true;
    private Boolean successSignup = true;
    UserTable userTable = new UserTable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // Call when login button gets clicked
    public void onClickLogIn(View view) {
        EditText usernameField = (EditText)findViewById(R.id.usernameLoginBox);
        EditText passwordField = (EditText)findViewById(R.id.passwordLoginBox);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if (username.length() == 0) {
            usernameField.setError("Please enter your username.");
            successLogin = false;
        }
        if (password.length() == 0) {
            passwordField.setError("Please enter your password.");
            successLogin = false;
        }
        User loginUser = userTable.contains(username);
        if (successLogin && loginUser != null) {
            if (loginUser.getPassword().equals(User.hash(password))) {
                User u = userTable.getUser(loginUser.getUserID());
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("user", u);
                startActivity(i);
            }
            else {
                passwordField.setError("Incorrect password. Please try again.");
            }
        }
        else {
            AlertDialog.Builder loginFail = new AlertDialog.Builder(this);
            loginFail.setMessage("Account not found. Please try again.");
            loginFail.setTitle("Error");
            loginFail.setPositiveButton("Close", null);
            loginFail.create().show();
        }

        successLogin = true; // reset success flag
    }

    public void onClickSignUp(View view) {
        EditText usernameField = (EditText)findViewById(R.id.usernameSignupBox);
        EditText passwordField = (EditText)findViewById(R.id.passwordSignupBox);
        EditText nameField = (EditText)findViewById(R.id.nameSignupBox);
        EditText emailField = (EditText)findViewById(R.id.emailSignupBox);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();

        if (username.length() == 0) {
            usernameField.setError("Please enter your username.");
            successSignup = false;
        }
        else if (userTable.contains(username) == null) {
            usernameField.setError("Username taken.");
            successSignup = false;
        }

        if (password.length() == 0) {
            passwordField.setError("Please enter your password.");
            successSignup = false;
        }
        else if (password.length() < 8) {
            passwordField.setError("Password must be at least 8 characters.");
            successSignup = false;
        }
        if (name.length() == 0) {
            nameField.setError("Please enter your name.");
            successSignup = false;
        }
        if(email.length() == 0) {
            emailField.setError("Please enter your email.");
            successSignup = false;
        }
        else if (userTable.containsEmail(email) != null) {
            emailField.setError("Account with this email already exists.");
            successSignup = false;
        }

        if (successSignup) {
            User signupUser = new User(name, email, username, password);
            User u = userTable.getUser(userTable.addUser(signupUser));
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("user", u);
            startActivity(i);
        }
        else {
            AlertDialog.Builder loginFail = new AlertDialog.Builder(this);
            loginFail.setMessage("Please fix errors and try again.");
            loginFail.setTitle("Error");
            loginFail.setPositiveButton("Close", null);
            loginFail.create().show();
        }

        successSignup = true; // reset success flag
    }

}