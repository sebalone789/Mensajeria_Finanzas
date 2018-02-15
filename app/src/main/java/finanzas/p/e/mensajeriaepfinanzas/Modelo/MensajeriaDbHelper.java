package finanzas.p.e.mensajeriaepfinanzas.Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by usuario on 12/06/2017.
 */

public class MensajeriaDbHelper extends SQLiteOpenHelper{
    public SQLiteDatabase db;
    public String dbPath;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Mensajeria.db";
    public static Context currentContext;

    private MensajeriaContract mensajeriaContract;

    public MensajeriaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        currentContext=context;
        dbPath="/data/data/"+context.getPackageName()+"/databases/";
        createDatabase();
    }

    public void onCreate(SQLiteDatabase db) {

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void createDatabase(){
        boolean dbExists = checkDbExists();
        if (dbExists) {
            // do nothing
        } else {
            db = currentContext.openOrCreateDatabase(DATABASE_NAME, 0, null);
            db.execSQL(mensajeriaContract.getSqlCreateEntriesGrupo());
            db.execSQL(mensajeriaContract.getSqlCreateEntriesPersona());
        }
    }

    private boolean checkDbExists() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = dbPath + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }


}
