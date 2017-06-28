package com.example.vinicius.trabalhofinal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/**
 * Created by vinicius on 15.06.17.
 */
public class MeuMqtt implements IMqttActionListener{ //MqttCallback
    static MqttAndroidClient client;
    static Context context;
    static SharedPreferences prefs;
    static BancoController bc;

    static String topico;


    static String topicPub; //= "EstanteDoVinicius1993Pub";// login + "Pub";
    static String topicSub;// = "EstanteDoVinicius1993Sub"; //login + "Sub";
    static int flagConection = 0;

    public MeuMqtt(Context context){
        this.context = context;
        bc = new BancoController(context);
    }

    public static void connect(){
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, "tcp://iot.eclipse.org:1883", clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setConnectionTimeout(10);

        prefs = context.getSharedPreferences("userLogin", context.MODE_PRIVATE);
        topico = prefs.getString("login", "");
        topicPub = topico + "Pub";
        topicSub = topico + "Sub";


        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(context, "Conectado à estante!!!", Toast.LENGTH_LONG).show();
                    flagConection = 1;
                    pub("Vai, toma");

                    subscribe(topicSub);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context, "Falha na conexão!", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(context, "Conexão perdida", Toast.LENGTH_LONG).show();
                connect();
                flagConection = 0;

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload = new String(message.getPayload());
                try{
                    String[] valores = payload.split(": ");
                    if (valores[0] == "Volume"){
                        int volume = Integer.parseInt(valores[1]);
                        bc.inserirGerais(volume);
                    }
                }catch(Exception e){

                }
                Toast.makeText(context, payload, Toast.LENGTH_LONG).show();
                System.out.println(payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                String r = token.toString();
                Toast.makeText(context, r, Toast.LENGTH_LONG).show();

            }
        });

    }

    public static void pub(String msg){
        if (flagConection == 0){
            connect();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 10000);
        }
        if (flagConection == 1){
            try {
                client.publish(topicPub, msg.getBytes(), 2, false);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }


    }

    public static void subscribe(String topic){
        try {
            client.subscribe(topic, 0);
        }catch (MqttException mqttEx){
            mqttEx.printStackTrace();
        }
    }


    @Override
    public void onSuccess(IMqttToken asyncActionToken) {

    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

    }

    public static void disconnect(){
        try{
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}