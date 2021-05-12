package com.example.jonnyspizza;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.jonnyspizza.CustomObjects.Order;

public class UserAccountActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView errorMessageTV;
    private Button okBtn;

    private UserAccount userAccount;
    private RESTHandler restHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        this.userAccount = (UserAccount) getIntent().getSerializableExtra(getString(R.string.user_account_name));
        this.restHandler = new RESTHandler(this);
    }

    /**
     * Handles the Sign In Account Btn Click by signing into the account
     * @param view
     */
    public void signInAccountBtn_Click(View view){
        EditText usernameText = findViewById(R.id.userNameSignInText);
        EditText passwordText = findViewById(R.id.passwordSignInText);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (validateSignInDetails()){
            restHandler.signInUser(username, password);
        }
    }

    /**
     * Once user credentials have been validated, sign in and return home
     * @param userID
     * @param username
     * @param password
     */
    protected void completeSignIn(int userID, String username, String password){
        userAccount.signIn(userID, username, password);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.user_account_name), userAccount);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Determines if the required input to sign into an account is valid
     * @return boolean - true if all the input is valid
     */
    private boolean validateSignInDetails(){
        boolean isValid = true;
        String emptyString = "";

        EditText usernameText = findViewById(R.id.userNameSignInText);
        EditText passwordText = findViewById(R.id.passwordSignInText);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (username.equals(emptyString)){
            usernameText.setError("Required!");
            isValid = false;
        }

        if (password.equals(emptyString)){
            passwordText.setError("Required!");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Handles the New Account Btn Click by changing the display from "Sign In" to "Create New Account"
     * @param view
     */
    public void newAccountBtn_Click(View view){
        Button newAccountBtn = (Button) view;
        LinearLayout signInLayout = findViewById(R.id.userAccountSignInLayout);
        LinearLayout createAccountLayout = findViewById(R.id.userAccountNewAccountLayout);

        newAccountBtn.setVisibility(View.GONE);
        signInLayout.setVisibility(View.GONE);
        createAccountLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Handles the Create Account Btn Click by creating a new account
     * @param view
     */
    public void createAccountBtn_Click(View view){
        if (validateNewAccountDetails()){

            EditText usernameText = findViewById(R.id.userNameNewText);
            EditText passwordText = findViewById(R.id.passwordNewText);

            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();

            restHandler.addUser(username, password);
        }
    }

    /**
     * Determines if the required input to create a new account is valid
     * @return boolean - true if all the input is valid
     */
    private boolean validateNewAccountDetails(){
        boolean isValid = true;
        String emptyString = "";

        EditText usernameText = findViewById(R.id.userNameNewText);
        EditText passwordText = findViewById(R.id.passwordNewText);
        EditText passwordConfirmText = findViewById(R.id.passwordConfirmNewText);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordConfirm = passwordConfirmText.getText().toString();

        if (username.equals(emptyString)){
            usernameText.setError("Required!");
            isValid = false;
        }

        if (password.equals(emptyString)){
            passwordText.setError("Required!");
            isValid = false;
        }

        if (passwordConfirm.equals(emptyString)){
            passwordConfirmText.setError("Required!");
            isValid = false;
        }

        if (!password.equals(passwordConfirm)){
            passwordText.setError("Passwords do not match!");
            passwordConfirmText.setError("Passwords do not match!");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Handles the Back button click by taking the user back to the Main menu without making any changes
     * @param view
     */
    public void userAccountBackToMainBtn_Click(View view){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    /**
     * Displays an error popup if issues arise with the user account sign in or creation
     * @param message String - error message to display to the user
     */
    protected void createErrorDialog(String message){

        dialogBuilder = new AlertDialog.Builder(this);
        final View userAccountErrorPopupView = getLayoutInflater().inflate(R.layout.user_account_error_popup, null);

        errorMessageTV = (TextView) userAccountErrorPopupView.findViewById(R.id.userAccountErrorMsgText);
        errorMessageTV.setText(message);

        okBtn = (Button) userAccountErrorPopupView.findViewById(R.id.userAccountErrorOkBtn);

        dialogBuilder.setView(userAccountErrorPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}