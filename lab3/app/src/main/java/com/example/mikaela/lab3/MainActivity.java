package com.example.mikaela.lab3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        InteractiveSearcher is = new InteractiveSearcher(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        is.setLayoutParams(params);
        layout.addView(is);

        setContentView(layout);
    }

}
