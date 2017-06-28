package com.example.vinicius.trabalhofinal;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vinicius on 04.06.17.
 */
public class PoteActivity extends AppCompatActivity {

    TextView tvNome, tvTipoIrrigacao, tvValor, tvTH, tvVoltar, tvQntAgua;
    ImageView ivFoto;
    Button btAlterar;
    int codigo, tipo;
    Bitmap foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pote);

        this.tvNome = (TextView)findViewById(R.id.tvNome);
        this.tvTipoIrrigacao = (TextView)findViewById(R.id.tvTipoIrrigacao);
        this.tvValor = (TextView)findViewById(R.id.tvThresholdHora);
        this.tvTH = (TextView)findViewById(R.id.tvTH);
        this.tvQntAgua = (TextView)findViewById(R.id.tvQntAgua);
        this.ivFoto = (ImageView)findViewById(R.id.ivFoto);
        this.tvVoltar = (TextView)findViewById(R.id.tvVoltarPote);
        this.btAlterar = (Button)findViewById(R.id.btAlterarPote);

        Intent itRecebeId = this.getIntent();
        if (itRecebeId != null) {
            Bundle bundle = itRecebeId.getExtras();
            this.codigo = bundle.getInt("codigo");
        }else{
            finish();
        }

        BancoController bc = new BancoController(getBaseContext());

        final Cursor cursor = bc.carregaDadosPotesById(this.codigo);

        this.tvNome.setText(cursor.getString(cursor.getColumnIndex(CriaBanco.NOME)));
        this.tvNome.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        this.tipo = cursor.getInt(cursor.getColumnIndex(CriaBanco.TIPO_IRRIGACAO));

        if (tipo == 0){
            this.tvTipoIrrigacao.setText("Threshold");
            this.tvTH.setText("Valor: ");
        }else{
            this.tvTipoIrrigacao.setText("Diário");
            this.tvTH.setText("Hora: ");
        }

        this.tvValor.setText(cursor.getString(cursor.getColumnIndex(CriaBanco.VALOR)));
        String qnt = cursor.getString(cursor.getColumnIndex(CriaBanco.QNT_AGUA)) + " ml";
        this.tvQntAgua.setText(qnt);

        try{
            byte[] f = cursor.getBlob(cursor.getColumnIndex(CriaBanco.FOTO));
            this.foto = BitmapFactory.decodeByteArray(f, 0, f.length);
            this.ivFoto.setImageBitmap(foto);
        }catch (Exception e){
            this.ivFoto.setImageResource(R.drawable.imgdefaut);
        }

        this.ivFoto.setAdjustViewBounds(false);

        this.tvVoltar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PoteActivity.this, InicialActivity.class);
                startActivity(intent);
                finish();
            }
        });

        this.btAlterar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(PoteActivity.this, AlterarActivity.class);
                intent.putExtra("codigo", codigo);
                startActivity(intent);
                finish();
            }
        });


    }
}
