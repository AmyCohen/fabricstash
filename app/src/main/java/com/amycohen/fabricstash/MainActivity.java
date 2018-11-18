package com.amycohen.fabricstash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.loginInfo) LinearLayout logInOptions;
    @BindView(R.id.logOutInfo) LinearLayout logOutOptions;
    @BindView(R.id.createAccountInfo) LinearLayout createAccountOptions;
    @BindView(R.id.newEmail) EditText mNewEmail;
    @BindView(R.id.newPassword) EditText mNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
//        createUidRefInDatabase();
    }


    public void updateUI(FirebaseUser user) {
        if (user == null) {
            logOutOptions.setVisibility(View.GONE);
            createAccountOptions.setVisibility(View.GONE);
            logInOptions.setVisibility(View.VISIBLE);
        } else {
            logOutOptions.setVisibility(View.VISIBLE);
            logInOptions.setVisibility(View.GONE);
            createAccountOptions.setVisibility(View.GONE);

            String info = "";
            if (user.getUid() != null && user.getEmail() != null) {
                info = user.getEmail();
            } else if (user.getUid() != null) {
                info = "anonymous " + user.getUid();
            }
            mEmail.setText(info);
        }
    }


    @OnClick(R.id.newAccountButton)
    public void goToCreateNewAccount(){
        createAccountOptions.setVisibility(View.VISIBLE);
        logOutOptions.setVisibility(View.GONE);
        logInOptions.setVisibility(View.GONE);
    }


    @OnClick(R.id.createAccountButton)
    public void createNewAccountActivity() {
        String email = mNewEmail.getText().toString();
        String password = mNewPassword.getText().toString();

        createAccount(email, password);

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    goToList();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

            }
        });
    }

    @OnClick(R.id.LogIn)
    public void logInActivity () {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        signIn(email, password);
    }

    //SOURCE: https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/EmailPasswordActivity.java
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    goToList();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

            }

        });
    }
    @OnClick(R.id.goToList)
    public void goToList() {
        Intent intent = new Intent (MainActivity.this, FabricListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logOut)
    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private DatabaseReference createUidRefInDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("fabricInventory");

        if (myRef.child(uid).equals(uid)) {
            myRef = database.getReference("fabricInventory").child(uid);
            return myRef;

        } else {
            DatabaseReference addNewUser = myRef.push();
            addNewUser.child("user").setValue(uid);
            return addNewUser;
        }

    }


}
