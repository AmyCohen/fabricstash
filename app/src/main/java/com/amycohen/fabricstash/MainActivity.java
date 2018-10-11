package com.amycohen.fabricstash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
//    @BindView(R.id.LogIn) Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.LogIn)
    public void logIn() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
