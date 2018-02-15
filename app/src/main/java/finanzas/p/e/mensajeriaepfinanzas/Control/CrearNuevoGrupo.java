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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoConfirmacionCreacion;
import finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos.DialogoRegistroExistente;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaContract;
import finanzas.p.e.mensajeriaepfinanzas.Modelo.MensajeriaDbHelper;
import finanzas.p.e.mensajeriaepfinanzas.R;

public class CrearNuevoGrupo extends AppCompatActivity {

    Button btnCrearGrupo,btnCancelar;
    EditText txtNombre;
    MensajeriaDbHelper mDbHelper;
    SQLiteDatabase db;
    public String dbPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nuevo_grupo);
        TextView titulo=(TextView)findViewById(R.id.txtTituloGrupo);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/RobotoSlab-Regular.ttf");
        titulo.setTypeface(typeface);
        mDbHelper=new MensajeriaDbHelper(this.getApplicationContext());
        dbPath="/data/data"+this.getApplicationContext().getPackageName()+"/databases/";
        db=mDbHelper.getWritableDatabase();
        btnCrearGrupo=(Button)findViewById(R.id.btnCrearGrupo);
        btnCancelar=(Button)findViewById(R.id.btnCancelarGrupo);
        txtNombre=(EditText)findViewById(R.id.txtNombreGrupo);
        btnCrearGrupo.setTypeface(typeface);
        btnCancelar.setTypeface(typeface);
        txtNombre.setTypeface(typeface);

        //Crear Nuevo Grupo
        btnCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verificar que no exista el registro
                String myPath=dbPath+"Mensajeria.db";
                File file=new File(myPath);
                if (file.exists() && !file.isDirectory())
                {
                    db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
                }
                db = mDbHelper.getWritableDatabase();
                try{Cursor c = db.rawQuery("SELECT * FROM "+ MensajeriaContract.Grupo.nombreTabla+" WHERE "+
                        MensajeriaContract.Grupo.nombreColumnaNombreGrupo+" LIKE '"+txtNombre.getText()+"'",null);
                //Si el registro no existe ingresar
                if (!c.moveToFirst()){
                    ContentValues values=new ContentValues();
                    values.put(MensajeriaContract.Grupo.nombreColumnaNombreGrupo,txtNombre.getText().toString());
                    long newRowId=db.insert(MensajeriaContract.Grupo.nombreTabla,null,values);
                    mostrarConfirmacion();
                }
                else
                {
                    mostrarRegistroExistente();
                }}catch (SQLiteException se ) {
                Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            } finally {
                if (db != null)
                    db.close();
            }
                limpiar();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
            }
        });
    }

    //Codigo del Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuapp, menu);
        menu.getItem(3).setEnabled(false);
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

    private void limpiar()
    {
        txtNombre.setText(null);
        finish();
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
}
