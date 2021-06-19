package com.example.sinevol;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class ClassDialogLoading extends MainCheques {
    private Activity activity;
    private AlertDialog dialog;

    ClassDialogLoading(Activity myActivity){
        activity=myActivity;

    }

    public void iniciarCarga(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater= activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

    public void cerrarCarga(){
//        LimpiarCampos();
        dialog.dismiss();
    }
}