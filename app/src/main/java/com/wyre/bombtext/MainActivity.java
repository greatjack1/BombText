package com.wyre.bombtext;

import android.support.v4.app.Fragment;

/**
 * Created by yaakov on 8/14/17.
 */

public class MainActivity extends AbstractFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
}
