package finanzas.p.e.mensajeriaepfinanzas.Modelo;

import android.provider.BaseColumns;

/**
 * Created by usuario on 12/06/2017.
 */

public class MensajeriaContract {
    private MensajeriaContract() {
    }

    public static class Persona implements BaseColumns {
        public static final String nombreTabla = "persona";
        public static final String nombreColumnaNombre = "nombre";
        public static final String nombreColumnaApPaterno = "apPaterno";
        public static final String nombreColumnaApMaterno = "apMaterno";
        public static final String nombreColumnaNumero = "numero";
        public static final String nombreColumnaGrupo = "idGrupo";
    }

    public static class Grupo implements BaseColumns {
        public static final String nombreTabla = "Grupo";
        public static final String nombreColumnaNombreGrupo = "nombreGrupo";
    }

    private static final String TEXT_TYPE = "TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES_GRUPO = "CREATE TABLE IF NOT EXISTS " + Grupo.nombreTabla + " (" +
            Grupo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Grupo.nombreColumnaNombreGrupo + " " + TEXT_TYPE + " )";

    private static final String SQL_CREATE_ENTRIES_PERSONA = "CREATE TABLE IF NOT EXISTS " + Persona.nombreTabla + " (" +
            Persona._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + " "+
            Persona.nombreColumnaApPaterno + " " + TEXT_TYPE + COMMA_SEP + " "+
            Persona.nombreColumnaApMaterno + " " + TEXT_TYPE + COMMA_SEP+ " " +
            Persona.nombreColumnaNombre + " " + TEXT_TYPE + COMMA_SEP + " "+
            Persona.nombreColumnaNumero + " " + TEXT_TYPE + COMMA_SEP + " "+
            Persona.nombreColumnaGrupo + " " + "INTEGER" + COMMA_SEP+ " "+
            "FOREIGN KEY (" + Persona.nombreColumnaGrupo + ") REFERENCES " + Grupo.nombreTabla + "(" +
            Grupo._ID + "))";

    private static final String SQL_DELETE_ENTRIES_PERSONA =
            "DROP TABLE IF EXISTS " + Persona.nombreTabla;

    private static final String SQL_DELETE_ENTRIES_GRUPO = "DROP TABLE IF EXISTS " + Grupo.nombreTabla;


    public static String getSqlCreateEntriesGrupo() {
        return SQL_CREATE_ENTRIES_GRUPO;
    }

    public static String getSqlCreateEntriesPersona() {
        return SQL_CREATE_ENTRIES_PERSONA;
    }

    public static String getSqlDeleteEntriesGrupo() {
        return SQL_DELETE_ENTRIES_GRUPO;
    }
    public static String getSqlDeleteEntriesPersona() {
        return SQL_DELETE_ENTRIES_PERSONA;
    }
}
