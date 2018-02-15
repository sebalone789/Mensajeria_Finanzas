package finanzas.p.e.mensajeriaepfinanzas.Control;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaContract;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaDbHelper;
import finanzas.p.e.mensajeriaepfinanzas.R;

public class ActividadBusqueda extends AppCompatActivity {

    public ArrayList<String> resultadosBusqueda=new ArrayList<>();
    private SQLiteDatabase newDB;
    public String dbPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_busqueda);
        dbPath="/data/data"+this.getApplicationContext().getPackageName()+"/databases/";

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //realizarBusqueda(query);
        }
    }

    /*private void realizarBusqueda(String query){
        try {
            resultadosBusqueda.clear();
            MensajeriaDbHelper dbHelper = new MensajeriaDbHelper(this.getApplicationContext());
            //revisar si existe la base y entonces abrirla
            String myPath=dbPath+"Mensajeria.db";
            File file=new File(myPath);
            if (file.exists() && !file.isDirectory())
            {
                newDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
            newDB = dbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT * FROM "+ MensajeriaContract.Persona.nombreTabla+" WHERE "+
                    MensajeriaContract.Persona.nombreColumnaApPaterno+" LIKE %"+query+"%",null);

            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String nombre = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaNombre));
                        String apPaterno = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaApPaterno));
                        String apMaterno = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaApMaterno));
                        String numero = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaNumero));
                        resultadosBusqueda.add(nombre+" "+apPaterno+" "+apMaterno+" "+numero);
                    }while (c.moveToNext());
                }
            }
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
    }*/
}
