package com.example.vinicius.trabalhofinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vinicius on 03.06.17.
 */
public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "estante9.db";

    public static final String TABELA_POTES = "potes";
    public static final String ID_POTE = "_id";
    public static final String NOME = "nome";
    public static final String FOTO = "foto";
    public static final String TIPO_IRRIGACAO = "tipoIrrigacao";
    public static final String VALOR = "valor";
    public static final String QNT_AGUA = "qntAgua";

    public static final String TABELA_GERAIS = "gerais";
    public static final String ID_GERAIS = "_id";
    public static final String VOLUME = "volume";


    public static final int VERSAO = 1;

    public CriaBanco(Context context) {

        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql1 = "CREATE TABLE " + TABELA_POTES + " ( "
                + ID_POTE + " integer primary key autoincrement, "
                + NOME + " text, "
                + FOTO + " blob, "
                + TIPO_IRRIGACAO + " integer, "
                + VALOR + " text, "
                + QNT_AGUA + " integer"
                + ")";

        String sql2 = "CREATE TABLE " + TABELA_GERAIS + " ( "
                + ID_GERAIS + " integer primary key autoincrement, "
                + VOLUME + " integer"
                + ")";

        db.execSQL(sql1);
        db.execSQL(sql2);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_POTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_GERAIS);
        onCreate(db);
    }
}