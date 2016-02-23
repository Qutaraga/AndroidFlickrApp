package com.example.qutaraga.myapplicationtestparsedelete;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialog extends ProgressDialog{

    public Dialog(Context context) {
        super(context);
        createLoadingDialog();
    }


    public Dialog(Context context, int theme) {
        super(context, theme);
    }

    public void createLoadingDialog(){

        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setMessage("Loading pictures...");
        setIndeterminate(true);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

    }
}
