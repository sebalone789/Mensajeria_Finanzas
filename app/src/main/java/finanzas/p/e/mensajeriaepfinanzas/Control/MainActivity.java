package finanzas.p.e.mensajeriaepfinanzas.Control;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoConfirmacion;
import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoFaltaMensaje;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaContract;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaDbHelper;
import finanzas.p.e.mensajeriaepfinanzas.R;

public class MainActivity extends AppCompatActivity {

    Button Send;
    EditText Msg;
    TextView selecGrupo;
    private SQLiteDatabase newDB;
    public String dbPath;
    MensajeriaDbHelper dbHelper;
    String myPath;
    Spinner spGrupos;
    ArrayList<String> resultados=new ArrayList<>();
    //Id del grupo
    Integer valorGrupo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbPath="/data/data"+this.getApplicationContext().getPackageName()+"/databases/";
        dbHelper = new MensajeriaDbHelper(this.getApplicationContext());
        //revisar si existe la base y entonces abrirla
        myPath=dbPath+"Mensajeria.db";

        //cargar datos
        cargarDatos();

        //llenar spinner
        spGrupos=(Spinner)findViewById(R.id.listaGruposMensaje);
        llenarListaGrupos();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.custom_spinner,resultados);
        spGrupos.setAdapter(adapter);

        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/RobotoSlab-Regular.ttf");
        selecGrupo=(TextView)findViewById(R.id.txtSelecGrupoMain);
        selecGrupo.setTypeface(typeface);

        Msg = (EditText) findViewById(R.id.txtMensaje);
        Msg.setTypeface(typeface);

        Send = (Button) findViewById(R.id.botonMensaje);
        Send.setTypeface(typeface);



        Send.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                //Verificar que haya un mensaje a enviar
                if(Msg.getText()!=null){
                //Buscar el id del grupo
                Cursor c2=newDB.rawQuery("SELECT * FROM "+MensajeriaContract.Grupo.nombreTabla+" WHERE "+
                        MensajeriaContract.Grupo.nombreColumnaNombreGrupo+" LIKE '"+spGrupos.getSelectedItem().toString()+"'",null);
                if(c2.moveToFirst())
                {
                    valorGrupo=c2.getInt(c2.getColumnIndex(MensajeriaContract.Grupo._ID));
                }
                try{

                File file=new File(myPath);
                if (file.exists() && !file.isDirectory())
                {
                    newDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
                }
                newDB = dbHelper.getWritableDatabase();
                Cursor c = newDB.rawQuery("SELECT * FROM "+ MensajeriaContract.Persona.nombreTabla+" WHERE "+
                                MensajeriaContract.Persona.nombreColumnaGrupo+" LIKE "+valorGrupo,null);

                if (c != null ) {
                    if  (c.moveToFirst()) {
                        do {
                            String nombre = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaNombre));
                            String apPaterno = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaApPaterno));
                            String apMaterno = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaApMaterno));
                            String numero = c.getString(c.getColumnIndex(MensajeriaContract.Persona.nombreColumnaNumero));
                            sendSMS(numero, nombre+" "+apPaterno+" "+apMaterno+" "+ Msg.getText().toString());
                        }while (c.moveToNext());
                    }
                }
            } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {
            if (newDB != null)
                newDB.close();
        }
                Msg.setText("");
                mostrarConfirmacion();
            }
            else
                {mostrarDialogoFaltaMensaje();}
            }
        });
    }

    //Codigo del Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuapp, menu);
        menu.getItem(0).setEnabled(false);
        menu.getItem(4).setVisible(false);
        menu.getItem(5).setVisible(false);
        return true;
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
                Intent crearNuevoGrupo =new Intent(getApplicationContext(), ListarGrupos.class);
                startActivity(crearNuevoGrupo);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void llenarListaGrupos()
    {
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

    public void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void mostrarConfirmacion()
    {
        DialogFragment dialogo=new DialogoConfirmacion();
        dialogo.show(getFragmentManager(),"confirmacion");
    }

    public void mostrarDialogoFaltaMensaje()
    {
        DialogFragment dialogo=new DialogoFaltaMensaje();
        dialogo.show(getFragmentManager(),"faltaMensaje");
    }

    //Cargar datos base
    private void cargarDatos(){
        //Valores
        String[] grupos={"Alumnos","Profesores","Postulantes"};
        //verificar que no exista el registro
        String myPath=dbPath+"Mensajeria.db";
        File file=new File(myPath);
        if (file.exists() && !file.isDirectory())
        {
            newDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        newDB = dbHelper.getWritableDatabase();
        try{
            for(int i=0;i<grupos.length;i++){
            Cursor c = newDB.rawQuery("SELECT * FROM "+ MensajeriaContract.Grupo.nombreTabla+" WHERE "+
                MensajeriaContract.Grupo.nombreColumnaNombreGrupo+" LIKE '"+grupos[i]+"'",null);
            //Si el registro no existe ingresar
            if (!c.moveToFirst()){
                ContentValues values=new ContentValues();
                values.put(MensajeriaContract.Grupo.nombreColumnaNombreGrupo,grupos[i]);
                long newRowId=newDB.insert(MensajeriaContract.Grupo.nombreTabla,null,values);
            }
            else
            {
                System.out.println("Ya existe este registro");
            }}}catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {
            if (newDB != null)
                newDB.close();
        }
    }
}
