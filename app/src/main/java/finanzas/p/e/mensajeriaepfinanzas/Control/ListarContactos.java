package finanzas.p.e.mensajeriaepfinanzas.Control;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoConfirmarEliminacion;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaContract;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaDbHelper;
import finanzas.p.e.mensajeriaepfinanzas.R;

public class ListarContactos extends AppCompatActivity implements DialogoConfirmarEliminacion.DialogoConfirmarEliminacionListener{

    private ArrayList<String> resultados=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> resultadosGrupos=new ArrayList<>();
    private ArrayAdapter<String> adapterGrupos;
    private String tableName= MensajeriaContract.Persona.nombreTabla;
    private SQLiteDatabase newDB;
    ListView list;
    public String dbPath, valor;
    SQLiteDatabase db;
    MensajeriaDbHelper mDbHelper;
    Spinner spGrupos;
    Integer valorGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contactos);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/RobotoSlab-Regular.ttf");
        dbPath="/data/data"+this.getApplicationContext().getPackageName()+"/databases/";
        mDbHelper=new MensajeriaDbHelper(this.getApplicationContext());
        db=mDbHelper.getWritableDatabase();
        spGrupos=(Spinner)findViewById(R.id.listaGruposListar);
        list=(ListView)findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = list.getItemAtPosition(position);
                TextView tv=(TextView)view;
                valor=((TextView)view).getText().toString();
                valor=valor.substring(valor.length()-9,valor.length());
                mostrarConfirmacion();
            }
        });

        //llenar spinner
        llenarListaGrupos();
        adapterGrupos=new ArrayAdapter<String>(this,R.layout.custom_spinner_2,resultadosGrupos);
        spGrupos.setAdapter(adapterGrupos);
        spGrupos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor c2=db.rawQuery("SELECT * FROM "+MensajeriaContract.Grupo.nombreTabla+" WHERE "+
                        MensajeriaContract.Grupo.nombreColumnaNombreGrupo+" LIKE '"+spGrupos.getSelectedItem().toString()+"'",null);
                if(c2.moveToFirst())
                {
                    valorGrupo=c2.getInt(c2.getColumnIndex(MensajeriaContract.Grupo._ID));
                    openAndQueryDatabase(valorGrupo);
                    displayResultList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void displayResultList()
    {
        list.setAdapter(null);
        adapter=new ArrayAdapter<String>(this,R.layout.custom_textview,resultados);
        list.setAdapter(adapter);
    }

    //Codigo del Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuapp, menu);
        menu.getItem(2).setEnabled(false);
        menu.getItem(5).setVisible(false);

        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menu.findItem(R.id.miBusuqeda).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    public void openAndQueryDatabase(int idGrupo) {
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
            Cursor c = newDB.rawQuery("SELECT * FROM "+ MensajeriaContract.Persona.nombreTabla+
                    " WHERE "+ MensajeriaContract.Persona.nombreColumnaGrupo+" LIKE "+idGrupo+
                    " ORDER BY "+ MensajeriaContract.Persona.nombreColumnaApPaterno+" ASC",null);

            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String nombre = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaNombre));
                        String apPaterno = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaApPaterno));
                        String apMaterno = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaApMaterno));
                        String numero = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaNumero));
                        resultados.add(nombre+" "+apPaterno+" "+apMaterno+" "+numero);
                    }while (c.moveToNext());
                }
            }
        }



    private void llenarListaGrupos()
    {
        String myPath=dbPath+"Mensajeria.db";
        File file=new File(myPath);
        if (file.exists() && !file.isDirectory())
        {
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        db = mDbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ MensajeriaContract.Grupo.nombreTabla,null);
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    String nombre = c.getString(c.getColumnIndex(MensajeriaContract.Grupo.nombreColumnaNombreGrupo));
                    resultadosGrupos.add(nombre);
                }while (c.moveToNext());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.miEnviarMensaje:
                Intent enviarMensaje=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(enviarMensaje);
                newDB.close();
                finish();
                break;
            case R.id.miRegistrarUsuario:
                Intent registrarUsuario=new Intent(getApplicationContext(), RegistrarUsuario.class);
                startActivity(registrarUsuario);
                newDB.close();
                finish();
                break;
            case R.id.miListarUsuarios:
                Intent listarContactos=new Intent(getApplicationContext(), ListarContactos.class);
                startActivity(listarContactos);
                newDB.close();
                finish();
                break;
            case R.id.miListarGrupo:
                Intent crearNuevoGrupo =new Intent(getApplicationContext(), ListarGrupos.class);
                startActivity(crearNuevoGrupo);
                finish();
                break;
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
        newDB.delete(MensajeriaContract.Persona.nombreTabla,"numero="+valor,null);
        openAndQueryDatabase(valorGrupo);
        displayResultList();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
