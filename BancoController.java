package com.example.vinicius.trabalhofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.sql.Time;

/**
 * Created by vinicius on 04.06.17.
 */
public class BancoController {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoController(Context context) {
        banco = new CriaBanco(context);
    }

    public String insereDadosPotes(String nome, byte[] foto, int tipoIrrigacao, String valor, int qntAgua) { // foto pode ser bitmap

        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NOME, nome);
        valores.put(CriaBanco.FOTO, foto);
        valores.put(CriaBanco.TIPO_IRRIGACAO, tipoIrrigacao);
        valores.put(CriaBanco.VALOR, valor);
        valores.put(CriaBanco.QNT_AGUA, qntAgua);
        resultado = db.insert(CriaBanco.TABELA_POTES, null, valores);
        db.close();

        if (resultado == -1) {
            return "ERRO ao inserir registro";
        } else {
            return "Registro inserido com sucesso";
        }
    }


    public Cursor carregaDadosPotes() {
        Cursor cursor;
        db = banco.getReadableDatabase();

        String[] campos = {CriaBanco.ID_POTE, CriaBanco.NOME, CriaBanco.FOTO, CriaBanco.TIPO_IRRIGACAO, CriaBanco.VALOR, CriaBanco.QNT_AGUA};
        cursor = db.query(CriaBanco.TABELA_POTES, campos, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();

        return cursor;
    }

    public Cursor carregaDadosPotesById(int id) {
        Cursor cursor;
        String[] campos = {CriaBanco.ID_POTE, CriaBanco.NOME, CriaBanco.FOTO, CriaBanco.TIPO_IRRIGACAO, CriaBanco.VALOR, CriaBanco.QNT_AGUA};
        String where = CriaBanco.ID_POTE + "=" + id;
        db = banco.getReadableDatabase();

        cursor = db.query(CriaBanco.TABELA_POTES, campos, where, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }

    public void alterarPotes(int id, String nome, byte[] foto, int tipoIrrigacao, String valor, int qntAgua) {
        ContentValues valores;
        String where;

        db = banco.getWritableDatabase();

        where = CriaBanco.ID_POTE + "=" + id;

        valores = new ContentValues();
        valores.put(CriaBanco.NOME, nome);
        valores.put(CriaBanco.FOTO, foto);
        valores.put(CriaBanco.TIPO_IRRIGACAO, tipoIrrigacao);
        valores.put(CriaBanco.VALOR, valor);
        valores.put(CriaBanco.QNT_AGUA, qntAgua);

        db.update(CriaBanco.TABELA_POTES, valores, where, null);
        db.close();
    }

    public void deletarPotes(int id) {
        String where = CriaBanco.ID_POTE + "=" + id;
        db = banco.getReadableDatabase();
        db.delete(CriaBanco.TABELA_POTES, where, null);
        db.close();
    }

    public Cursor carregaGerais() {
        Cursor cursor;
        db = banco.getReadableDatabase();

        String[] campos = {CriaBanco.ID_GERAIS, CriaBanco.VOLUME};
        cursor = db.query(CriaBanco.TABELA_GERAIS, campos, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();

        return cursor;
    }

    public void inserirGerais(int volume){
        ContentValues valores;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.VOLUME, volume);
        db.insert(CriaBanco.TABELA_GERAIS, null, valores);
        db.close();

    }
}