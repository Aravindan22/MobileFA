package com.example.android.mobilefa;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class ProgressDialog {

    protected Activity progressActivity;
    protected AlertDialog alertDialog;

    ProgressDialog(Activity activity) {
        progressActivity = activity;
    }

    void startProgress() {

        AlertDialog.Builder builder = new AlertDialog.Builder(progressActivity);

        LayoutInflater inflater = progressActivity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_spinner, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissProgress() {
        alertDialog.dismiss();
    }

}
