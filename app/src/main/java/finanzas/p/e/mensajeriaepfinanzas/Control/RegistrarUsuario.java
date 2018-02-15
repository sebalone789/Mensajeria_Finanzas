package finanzas.p.e.mensajeriaepfinanzas.Control;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoConfirmacionCreacion;
import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoFaltaApMaterno;
import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoFaltaApPaterno;
import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoFaltaNombre;
import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoFaltaNumero;
import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoRegistroExistente;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaContract;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaDbHelper;
import finanzas.p.e.mensajeriaepfinanzas.R;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class RegistrarUsuario extends AppCompatActivity {

    //Valores del registro
    Button btnAceptar,btnCancelar,btnLeerArchivo;
    EditText txtNombre,txtApPaterno,txtApMaterno,txtNumero;
    Spinner spGrupos;
    ArrayList<String> listaGrupos=new ArrayList<>();
    public MensajeriaDbHelper mDbHelper;
    SQLiteDatabase db;
    public String dbPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        TextView titulo=(TextView)findViewById(R.id.txtTitulo);
        TextView selecGrupo=(TextView)findViewById(R.id.txtSeleccionarGrupo);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/RobotoSlab-Regular.ttf");
        titulo.setTypeface(typeface);
        selecGrupo.setTypeface(typeface);
        mDbHelper=new MensajeriaDbHelper(this.getApplicationContext());
        dbPath="/data/data"+this.getApplicationContext().getPackageName()+"/databases/";
        db=mDbHelper.getWritableDatabase();
        btnAceptar=(Button)findViewById(R.id.btnAceptar);
        btnCancelar=(Button)findViewById(R.id.btnCancelar);
        btnLeerArchivo=(Button)findViewById(R.id.btnLeerArchivo);
        txtNombre=(EditText)findViewById(R.id.txtNombre);
        txtApPaterno=(EditText)findViewById(R.id.txtApPaterno);
        txtApMaterno=(EditText)findViewById(R.id.txtApMaterno);
        txtNumero=(EditText)findViewById(R.id.txtNumero);
        spGrupos=(Spinner)findViewById(R.id.listaGrupos);
        txtApMaterno.setTypeface(typeface);
        txtNombre.setTypeface(typeface);
        txtNumero.setTypeface(typeface);
        txtApPaterno.setTypeface(typeface);
        btnCancelar.setTypeface(typeface);
        btnAceptar.setTypeface(typeface);

        //llenar el spinner
        llenarListaGrupos();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.custom_spinner,listaGrupos);
        spGrupos.setAdapter(adapter);

        //Guardar los datos en la base de datos
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verificar que todos los campos esten llenos
                if(txtApMaterno.getText()!=null){
                    if(txtNombre.getText()!=null){
                        if(txtApPaterno.getText()!=null){
                            if(txtNumero.getText()!=null){
                //verificar que no exista el registro
                String myPath=dbPath+"Mensajeria.db";
                File file=new File(myPath);
                if (file.exists() && !file.isDirectory())
                {
                    db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
                }
                db = mDbHelper.getWritableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM "+ MensajeriaContract.Persona.nombreTabla+" WHERE "+
                        MensajeriaContract.Persona.nombreColumnaNombre+" LIKE '"+txtNombre.getText().toString()+"' AND "+
                        MensajeriaContract.Persona.nombreColumnaApPaterno+" LIKE '"+txtApPaterno.getText().toString()+"' AND "+
                        MensajeriaContract.Persona.nombreColumnaApMaterno+" LIKE '"+txtApMaterno.getText().toString()+"' AND "+
                        MensajeriaContract.Persona.nombreColumnaNumero+" LIKE '"+txtNumero.getText().toString()+"'",null);
                //Si el registro no existe ingresar
                if (!c.moveToFirst()){
                ContentValues values=new ContentValues();
                values.put(MensajeriaContract.Persona.nombreColumnaNombre,txtNombre.getText().toString());
                values.put(MensajeriaContract.Persona.nombreColumnaApPaterno,txtApPaterno.getText().toString());
                values.put(MensajeriaContract.Persona.nombreColumnaApMaterno,txtApMaterno.getText().toString());
                values.put(MensajeriaContract.Persona.nombreColumnaNumero,txtNumero.getText().toString());
                    //Buscar el id del grupotry
                    Cursor c2=db.rawQuery("SELECT * FROM "+MensajeriaContract.Grupo.nombreTabla+" WHERE "+
                            MensajeriaContract.Grupo.nombreColumnaNombreGrupo+" LIKE '"+spGrupos.getSelectedItem().toString()+"'",null);
                    if(c2.moveToFirst())
                    {
                        values.put(MensajeriaContract.Persona.nombreColumnaGrupo,c2.getInt(c2.getColumnIndex(MensajeriaContract.Grupo._ID)));
                    }
                long newRowId=db.insert(MensajeriaContract.Persona.nombreTabla,null,values);
                mostrarConfirmacion();
                }
                else
                {
                    mostrarRegistroExistente();
                }
                limpiar();
                            }
                            else {mostrarDialogoFaltaNumero();}
                        }
                        else {mostrarDialogoFaltaApPaterno();}
                    }
                    else {mostrarDialogoFaltaNombre();}
                }
                else {mostrarDialogoFaltaApMaterno();}
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
            }
        });
        btnLeerArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileManager=new Intent(getApplicationContext(), BuscarArchivo.class);
                startActivity(fileManager);
            }
        });
    }

    //Codigo del Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuapp, menu);
        menu.getItem(1).setEnabled(false);
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
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        db = mDbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ MensajeriaContract.Grupo.nombreTabla,null);
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    String nombre = c.getString(c.getColumnIndex(MensajeriaContract.Grupo.nombreColumnaNombreGrupo));
                    listaGrupos.add(nombre);
                }while (c.moveToNext());
            }
        }
    }

    public void limpiar(){
        txtApMaterno.setText(null);
        txtApPaterno.setText(null);
        txtNombre.setText(null);
        txtNumero.setText(null);
    }

    public void mostrarConfirmacion()
    {
        DialogFragment dialogo=new DialogoConfirmacionCreacion();
        dialogo.show(getFragmentManager(),"confirmacionCreacion");
    }

    public void mostrarRegistroExistente()
    {
        DialogFragment dialogo=new DialogoRegistroExistente();
        dialogo.show(getFragmentManager(),"registroExistente");
    }

    public void mostrarDialogoFaltaNombre()
    {
        DialogFragment dialogo=new DialogoFaltaNombre();
        dialogo.show(getFragmentManager(),"faltaNombre");
    }

    public void mostrarDialogoFaltaApPaterno()
    {
        DialogFragment dialogo=new DialogoFaltaApPaterno();
        dialogo.show(getFragmentManager(),"faltaApPaterno");
    }

    public void mostrarDialogoFaltaApMaterno()
    {
        DialogFragment dialogo=new DialogoFaltaApMaterno();
        dialogo.show(getFragmentManager(),"faltaApMaterno");
    }

    public void mostrarDialogoFaltaNumero()
    {
        DialogFragment dialogo=new DialogoFaltaNumero();
        dialogo.show(getFragmentManager(),"faltaNumero");
    }


}
