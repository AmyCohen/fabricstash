package com.amycohen.fabricstash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String TAG;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.loginInfo) LinearLayout logInOptions;
    @BindView(R.id.logOut) Button logOutOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }


    public void updateUI(FirebaseUser user) {
        if (user == null) {
            logOutOptions.setVisibility(View.GONE);
            logInOptions.setVisibility(View.VISIBLE);
        } else {
            logOutOptions.setVisibility(View.VISIBLE);
            logInOptions.setVisibility(View.GONE);

            String info = "";
            if (user.getUid() != null && user.getEmail() != null) {
                info = user.getEmail() + " " + user.getUid();
            } else if (user.getUid() != null) {
                info = "anonymous " + user.getUid();
            }
            email.setText(info);
        }
    }

//    private void createAccount() {
//        mAuth.createUserWithEmailAndPassword(email,password)
//            .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete (@NonNull Task< AuthResult > task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success");
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    updateUI(user);
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show();
//                    updateUI(null);
//                }
//
//                // ...
//            }
//        });
//    }
    public void signInWithEmailAndPassword (EditText email, EditText password){
//    public Task<AuthResult> signInWithEmailAndPassword (String email, String password){
    mAuth.signInWithEmailAndPassword(String.valueOf(email), String.valueOf(password)
    )
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
//                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                // ...
            }
        });
    }

    @OnClick(R.id.LogIn)
    public void logIn() {
        signInWithEmailAndPassword(email, password);
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
