package finanzas.p.e.mensajeriaepfinanzas.Control;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoConfirmarEliminacion;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaContract;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaDbHelper;
import finanzas.p.e.mensajeriaepfinanzas.R;

public class ListarGrupos extends AppCompatActivity implements DialogoConfirmarEliminacion.DialogoConfirmarEliminacionListener{


    private ArrayList<String> resultados=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String tableName= MensajeriaContract.Grupo.nombreTabla;
    private SQLiteDatabase newDB;
    ListView list;
    public String dbPath, valor;
    SQLiteDatabase db;
    MensajeriaDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_grupos);
        dbPath="/data/data"+this.getApplicationContext().getPackageName()+"/databases/";
        mDbHelper=new MensajeriaDbHelper(this.getApplicationContext());
        db=mDbHelper.getWritableDatabase();
        openAndQueryDatabase();
        list=(ListView)findViewById(R.id.listaGrupos);
        ListAdapter valores=adapter=new ArrayAdapter<String>(this,R.layout.custom_textview,resultados);
        list.setAdapter(valores);
        list=(ListView)findViewById(R.id.listaGrupos);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = list.getItemAtPosition(position);
                TextView tv=(TextView)view;
                valor=((TextView)view).getText().toString();
                mostrarConfirmacion();
            }
        });

    }

    public void openAndQueryDatabase() {
        resultados.clear();
        MensajeriaDbHelper dbHelper = new MensajeriaDbHelper(this.getApplicationContext());
        //revisar si existe la base y entonces abrirla
        String myPath=dbPath+"Mensajeria.db";
        File file=new File(myPath);
        if (file.exists() && !file.isDirectory())
        {
            newDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        newDB = dbHelper.getWritableDatabase();
        Cursor c = newDB.rawQuery("SELECT * FROM "+ MensajeriaContract.Grupo.nombreTabla,null);

        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    String nombre = c.getString(c.getColumnIndex(MensajeriaContract.Grupo.nombreColumnaNombreGrupo));
                    resultados.add(nombre);
                }while (c.moveToNext());
            }
        }
    }

    //Codigo del Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuapp, menu);
        menu.getItem(3).setEnabled(false);
        menu.getItem(4).setVisible(false);
        return true;
    }

    private void displayResultList()
    {
        list.setAdapter(null);
        adapter=new ArrayAdapter<String>(this,R.layout.custom_textview,resultados);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.miEnviarMensaje:
                Intent enviarMensaje=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(enviarMensaje);
                finish();
                break;
            case R.id.miRegistrarUsuario:
                Intent registrarUsuario=new Intent(getApplicationContext(), RegistrarUsuario.class);
                startActivity(registrarUsuario);
                finish();
                break;
            case R.id.miListarUsuarios:
                Intent listarContactos=new Intent(getApplicationContext(), ListarContactos.class);
                startActivity(listarContactos);
                finish();
                break;
            case R.id.miListarGrupo:
                Intent listarGrupo =new Intent(getApplicationContext(), ListarGrupos.class);
                startActivity(listarGrupo);
                finish();
                break;
            case R.id.miCrear:
                Intent crearNuevoGrupo =new Intent(getApplicationContext(), CrearNuevoGrupo.class);
                startActivity(crearNuevoGrupo);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrarConfirmacion()
    {
        DialogFragment dialogo=new DialogoConfirmarEliminacion();
        dialogo.show(getFragmentManager(),"confirmarEliminacion");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        newDB.delete(MensajeriaContract.Grupo.nombreTabla,"nombreGrupo="+valor,null);
        openAndQueryDatabase();
        displayResultList();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
