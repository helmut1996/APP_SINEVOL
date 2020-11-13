package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.zip.Inflater;

public class  ClassDialog extends AppCompatDialogFragment {
    private TextView nombre,zona,direccion,credito,creditotal;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_clientes,null);

        builder.setView(view)
                .setTitle("Cliente")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent  intent = new Intent(getContext(),MainProductosCliente.class);
                        startActivity(intent);
                    }
                });

        nombre=view.findViewById(R.id.text_nombre);
        zona=view.findViewById(R.id.text_zona);
        direccion=view.findViewById(R.id.text_direccion);
        credito=view.findViewById(R.id.text_credito);
        creditotal=view.findViewById(R.id.text_credito_disponible);

        return builder.create();

    }
}
