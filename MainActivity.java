package com.example.vinicius.trabalhofinal;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    Button btentrar;
    int mensagemExibida = 0;
    MeuMqtt mqtt;
    MeuMqtt2 mqtt2;
    static int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mqtt2 = new MeuMqtt2(this);
        mqtt2.connect();


        this.btentrar = (Button) findViewById(R.id.btEntrar);
        this.btentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeuMqtt2.pub();
                Intent intent = new Intent(MainActivity.this, InicialActivity.class);
                startActivity(intent);
            }
        });


        BancoController bc = new BancoController(getBaseContext());
        bc.inserirGerais(23);

        try{
            final Cursor reservatorio = bc.carregaGerais();
            reservatorio.moveToLast();
            int volume = reservatorio.getInt(reservatorio.getColumnIndex(CriaBanco.VOLUME));
            if (volume < 30) {
                if (mensagemExibida == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Atenção");
                    builder.setIcon(R.drawable.alertareservatorio);
                    builder.setMessage("Nível do reservatório está abaixo de 30%!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", null);
                    AlertDialog dialog = builder.show();
                    TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
                    TextView title = (TextView) dialog.findViewById(android.R.id.title);
                    Button bt = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) bt.getLayoutParams();
                    positiveButtonLL.gravity = Gravity.CENTER;
                    bt.setLayoutParams(positiveButtonLL);
                    title.setGravity(Gravity.CENTER);
                    messageView.setGravity(Gravity.CENTER);
                    mensagemExibida = 1;
                }

            } else{
                mensagemExibida = 0;
            }
        }catch (Exception e){

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (flag == 0){
            //flag = 1;

        }

    }
}
