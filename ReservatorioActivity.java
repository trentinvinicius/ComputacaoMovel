package com.example.vinicius.trabalhofinal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vinicius on 11.06.17.
 */
public class ReservatorioActivity extends AppCompatActivity {

    ProgressBar pbReservatorio;
    TextView tvVoltar, tvVolume;
    BancoController bc;
    int volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservatorio);


        pbReservatorio = (ProgressBar) findViewById(R.id.pbReservatorio);
        tvVoltar = (TextView) findViewById(R.id.tvVoltarReservatorio);
        tvVolume = (TextView) findViewById(R.id.tvVolume);


        this.bc = new BancoController(getBaseContext());
        try{
            final Cursor cursor = this.bc.carregaGerais();
            if(cursor.getCount() == 0){
                Toast.makeText(getApplicationContext(), "No momento nenhuma informação sobre o reservatório encontra-se disponível.", Toast.LENGTH_LONG).show();
            }else{
                cursor.moveToLast();
                this.volume = cursor.getInt(cursor.getColumnIndex(CriaBanco.VOLUME));
                String v = Integer.toString(volume) + " %";
                tvVolume.setText(v);
                ProgressBarAnimation anim = new ProgressBarAnimation(pbReservatorio, 0, volume);
                anim.setDuration(1000);
                pbReservatorio.startAnimation(anim);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Não foi possível ler as informações", Toast.LENGTH_LONG).show();
        }




        this.tvVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservatorioActivity.this, InicialActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
