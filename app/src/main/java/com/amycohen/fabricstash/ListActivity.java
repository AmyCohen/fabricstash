package com.amycohen.fabricstash;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

public class ListActivity extends SingleFragmentActivity {

    @Override
    Fragment getFragment() {
        return new ListFragment();
    }
}