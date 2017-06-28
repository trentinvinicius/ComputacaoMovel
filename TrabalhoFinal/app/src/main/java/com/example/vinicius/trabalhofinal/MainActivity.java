package com.example.vinicius.trabalhofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btentrar;
    int mensagemExibida = 0;
    MeuMqtt mqtt;
    EditText etLogin;
    static int flag = 0;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mqtt = new MeuMqtt(this);
        //mqtt2.connect();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.prefs = getApplicationContext().getSharedPreferences("userLogin", MODE_PRIVATE);
        this.etLogin = (EditText) findViewById(R.id.etLogin);
        this.btentrar = (Button) findViewById(R.id.btEntrar);


        String login = prefs.getString("login", "");
        this.etLogin.setText(login);

        this.btentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor;
                editor = prefs.edit();
                editor.putString("login", etLogin.getText().toString());
                editor.commit();
                mqtt.connect();
                //MeuMqtt.pub("Vai, toma");

                Intent intent = new Intent(MainActivity.this, InicialActivity.class);
                startActivity(intent);

            }
        });


        BancoController bc = new BancoController(getBaseContext());

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
}
