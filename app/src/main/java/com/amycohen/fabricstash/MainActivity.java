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
    private static final String TAG = "EmailPassword";
    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
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
            mEmail.setText(info);
        }
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
//        if (!validateForm()) {
//            return;
//        }

//        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
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
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                // [START_EXCLUDE]
//                if (!task.isSuccessful()) {
//                    mStatusTextView.setText(R.string.auth_failed);
//                }
//                hideProgressDialog();
                // [END_EXCLUDE]
            }
        });
// [END sign_in_with_email]
    }



//    @OnClick(R.id.LogIn)
//    public void logIn() {
//        if (signInWithEmailAndPassword(email, password)) {
//            Intent intent = new Intent(this, ListActivity.class);
//            startActivity(intent);
//        } else {
//            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//        }
//    }
}
