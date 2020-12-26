package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.SQLite.conexionSQLiteHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ClassDialogFactura extends DialogFragment {
    TextView producto,cantidad,precio_C,precio_D,tvimagenSQLite;
  //  ImageButton btn_delete, btn_Actualizar;
    ImageView imgProductoDetalle;
    EditText editcantidad2;
    MainFacturaList variable = new MainFacturaList();
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.detalle_factura,null);

        builder.setView(view)
                .setTitle("Detalle Factura")
                .setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActualizarDatosSQLite();

                    }
                })
                .setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EliminarDatosSQLite();


                    }
                }

                );

        producto=view.findViewById(R.id.id_producto);
        producto.setText(variable.nombre);

        cantidad=view.findViewById(R.id.editTextCantidad2);
        cantidad.setText(String.valueOf(variable.cantidadProducto));

        precio_C=view.findViewById(R.id.id_precio_cordobas);
        precio_C.setText(String.valueOf(variable.precioProducto));

        precio_D=view.findViewById(R.id.id_precio_dolares);
        precio_D.setText(String.valueOf(variable.idProd));

       // btn_delete=view.findViewById(R.id.imageButton_Eliminar);

        imgProductoDetalle=view.findViewById(R.id.imageProducto);
        String imagen= variable.nombreImagen;
        File file= new File("///storage/emulated/0/MARNOR/"+imagen+".jpg");
        Picasso.get().load(file)
                .placeholder(R.drawable.bucandoimg)
                .error(R.drawable.error)
                .into(imgProductoDetalle);

        tvimagenSQLite=view.findViewById(R.id.tvimagenBD_SQLlite);
        tvimagenSQLite.setText(variable.nombreImagen);

        editcantidad2=view.findViewById(R.id.editTextCantidad2);
        return builder.create();

    }


    private void ActualizarDatosSQLite() {
        /*mandando a llamar conexion a SQLite */
       conexionSQLiteHelper conn= new conexionSQLiteHelper(getContext(),"bd_productos",null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        db.execSQL("update producto set cantidad ="+cantidad.getText().toString()+" where id = "+precio_D.getText().toString()+" ");
        db.close();

        Toast.makeText(getContext(),"Actualizado!!!",Toast.LENGTH_SHORT).show();
        Intent refresh = new Intent(getContext(), MainFacturaList.class);
        startActivity(refresh);


    }

    private void EliminarDatosSQLite() {
        conexionSQLiteHelper conn = new conexionSQLiteHelper(getContext(),"bd_productos",null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        db.execSQL("DELETE FROM producto WHERE id = "+precio_D.getText().toString()+" ");
        db.close();
       Intent refresh = new Intent(getContext(), MainFacturaList.class);
       startActivity(refresh);
        Toast.makeText(getContext(),"Registro Eliminado!!!",Toast.LENGTH_SHORT).show();
    }


}
