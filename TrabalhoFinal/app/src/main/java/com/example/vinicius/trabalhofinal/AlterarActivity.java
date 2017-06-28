package com.example.vinicius.trabalhofinal;

//import android.app.DialogFragment;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by vinicius on 07.06.17.
 */
public class AlterarActivity extends AppCompatActivity {

    int codigo, tipo;
    EditText etNome, etQntAgua, etValor;
    Switch sTipo;
    Button btLimpar, btAlterar;
    TextView tvVoltar, tvTipo, tvAlterarFoto;
    private static final int PICK_IMAGE = 1;
    ImageView ivFotoAlterar;
    Bitmap foto;
    BancoController bc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);

        this.etNome = (EditText) findViewById(R.id.etNome);
        this.sTipo = (Switch) findViewById(R.id.sTipo);
        this.btAlterar = (Button) findViewById(R.id.btAlterarAlterarPote);
        this.btLimpar = (Button) findViewById(R.id.btLimparAlterarPote);
        this.tvVoltar = (TextView) findViewById(R.id.tvVoltarAlterarPote);
        this.tvTipo = (TextView) findViewById(R.id.tvTipoIrrigacaoAlterarPote);
        this.tvAlterarFoto = (TextView) findViewById(R.id.tvAlterarFoto);
        this.ivFotoAlterar = (ImageView) findViewById(R.id.ivFotoAlterar);
        this.etQntAgua = (EditText) findViewById(R.id.etQntAgua);
        this.etValor = (EditText) findViewById(R.id.etValor);

        Intent itRecebeId = this.getIntent();
        if (itRecebeId != null) {
            Bundle bundle = itRecebeId.getExtras();
            this.codigo = bundle.getInt("codigo");
        } else {
            sair();
        }

        this.bc = new BancoController(getBaseContext());
        final Cursor cursor = this.bc.carregaDadosPotesById(this.codigo);


        this.etNome.setText(cursor.getString(cursor.getColumnIndex(CriaBanco.NOME)));

        try{
            byte[] f = cursor.getBlob(cursor.getColumnIndex(CriaBanco.FOTO));
            this.foto = BitmapFactory.decodeByteArray(f, 0, f.length);
            this.ivFotoAlterar.setImageBitmap(foto);
        }catch (Exception e){
            this.ivFotoAlterar.setImageResource(R.drawable.imgdefaut);
        }
        this.ivFotoAlterar.setAdjustViewBounds(false);

        this.tipo = cursor.getInt(cursor.getColumnIndex(CriaBanco.TIPO_IRRIGACAO));

        if (this.tipo == 1) {
            this.sTipo.setChecked(true);
            tvTipo.setText("Hora: ");
            this.etValor.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        } else {
            this.sTipo.setChecked(false);
            tvTipo.setText("Valor: ");
            this.etValor.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        this.etValor.setText(cursor.getString(cursor.getColumnIndex(CriaBanco.VALOR)));


        sTipo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    tvTipo.setText("Hora: ");
                    etValor.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                    tipo = 1;
                }else{
                    tvTipo.setText("Valor: ");
                    etValor.setInputType(InputType.TYPE_CLASS_NUMBER);
                    tipo = 0;
                }
            }
        });

        this.etQntAgua.setText(cursor.getString(cursor.getColumnIndex(CriaBanco.QNT_AGUA)));




        this.btLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNome.setText("");
                sTipo.setChecked(false);
            }
        });

        this.tvAlterarFoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
            }
        });

        this.tvVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sair();
            }
        });

        this.btAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "Pote: " + Integer.toString(codigo) + ";Tipo: " + Integer.toString(tipo) + ";Valor: " + etValor.getText().toString() + ";QntAgua: " + etQntAgua.getText().toString();
                MeuMqtt.pub(msg);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] f = stream.toByteArray();
                bc.alterarPotes(codigo, etNome.getText().toString(), f, tipo, etValor.getText().toString(), Integer.parseInt(etQntAgua.getText().toString()));
                sair();
            }
        });

    }

    protected void sair(){
        Intent intent = new Intent(AlterarActivity.this, InicialActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                foto = BitmapFactory.decodeStream(inputStream);
                ivFotoAlterar.setImageBitmap(foto);
                ivFotoAlterar.setAdjustViewBounds(true);

            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Não foi possível alterar a imagem.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

}


