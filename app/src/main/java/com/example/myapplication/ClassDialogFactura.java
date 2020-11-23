package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ClassDialogFactura extends DialogFragment {
    TextView producto,cantidad,precio_C,precio_D;
    ImageButton btn_delete;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.detalle_factura,null);

        builder.setView(view)
                .setTitle("Detalle Factura")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        producto=view.findViewById(R.id.id_producto);
        cantidad=view.findViewById(R.id.editTextCantidad2);
        precio_C=view.findViewById(R.id.id_precio_cordobas);
        precio_D=view.findViewById(R.id.id_precio_dolares);
        btn_delete=view.findViewById(R.id.imageButton_Eliminar);

        return builder.create();

    }
}
