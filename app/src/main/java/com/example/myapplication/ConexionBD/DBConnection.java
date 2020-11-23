package com.example.myapplication.ConexionBD;

import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    private static String ip = "192.168.0.211";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "BDMARNOR";
    private static String username = "sa";
    private static String password = "Helmut123";
    public Connection connection = null;
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;

    public DBConnection(){}

    public void conectar()
    {
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
            Toast.makeText(getApplicationContext(),"Pin incorrecto",Toast.LENGTH_LONG).show();
            //textView.setText("Conectado exitosamente");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //textView.setText("ERROR");
        } catch (SQLException e) {
            e.printStackTrace();
            //textView.setText("Fallo al conectarse");
        }
    }
}
