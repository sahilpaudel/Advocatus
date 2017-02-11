package com.sahilpaudel.app.advocatus.facebook;

import android.view.View;

/**
 * Created by Sahil Paudel on 2/11/2017.
 */

interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
