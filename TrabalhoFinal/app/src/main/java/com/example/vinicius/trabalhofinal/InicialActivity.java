package com.example.vinicius.trabalhofinal;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InicialActivity extends AppCompatActivity {
    ArrayList<String> nomes;
    ArrayList<byte[]> fotos;
    TextView tvReservatorio, tvSair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        tvReservatorio = (TextView) findViewById(R.id.tvReservatorio);
        tvSair = (TextView) findViewById(R.id.tvSair);

        BancoController bc = new BancoController(getBaseContext());

        final Cursor cursor = bc.carregaDadosPotes();   //"POTES"

        if(cursor.getCount() == 0){
            for (int i = 0; i<6; i++) {
                String nome = "Pote " + Integer.toString(i);
                bc.insereDadosPotes(nome, null, 0, "0", 0);
            }
            Toast.makeText(getApplicationContext(), "Dados inseridos", Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());     //para reiniciar a activity e carregar o cursor com os dados corretos
        }

        cursor.moveToFirst();
        this.nomes = new ArrayList<String>();
        fotos = new ArrayList<byte[]>();

        while(!cursor.isAfterLast()) {
            nomes.add(cursor.getString(cursor.getColumnIndex(CriaBanco.NOME)));
            byte[] f = cursor.getBlob(cursor.getColumnIndex(CriaBanco.FOTO));
            fotos.add(f);
            cursor.moveToNext();
        }

        GridView gv = (GridView)findViewById(R.id.gridView);

        gv.setAdapter(new Adaptador(this, nomes, fotos));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int codigo;
                cursor.moveToPosition(position);
                codigo = cursor.getInt(cursor.getColumnIndexOrThrow(CriaBanco.ID_POTE));

                Intent intent = new Intent(InicialActivity.this, PoteActivity.class);
                intent.putExtra("codigo", codigo);
                startActivity(intent);
                finish();
            }
        });

        this.tvReservatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InicialActivity.this, ReservatorioActivity.class);
                startActivity(intent);
                finish();

            }
        });

        this.tvSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeuMqtt.disconnect();
                finish();
            }
        });

    }
}
