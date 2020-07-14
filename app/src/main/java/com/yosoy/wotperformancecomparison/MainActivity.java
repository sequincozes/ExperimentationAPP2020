package com.yosoy.wotperformancecomparison;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.iot.coap.Client;
import com.google.iot.coap.HostLookupException;
import com.google.iot.coap.LocalEndpointManager;
import com.google.iot.coap.Message;
import com.google.iot.coap.RequestBuilder;
import com.google.iot.coap.UnsupportedSchemeException;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;

import java.io.IOException;
import java.security.Security;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {


    EditText runs;

    //String milBytes = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    //String payload = milBytes.concat(milBytes).concat(milBytes).concat(milBytes).concat(milBytes).concat(milBytes).concat(milBytes).concat(milBytes).concat(milBytes).concat(milBytes);
    String coapPayload = "not set";
    int coapRun = 0;
    private TEA tea = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        addButton();

    }

    private enum SecurityMode {
        NO, TEA, DES, AES128, AES256
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addButton() {
        runs = findViewById(R.id.editText);
        runs.setText("Run #" + "0");


        Button reset = findViewById(R.id.buttonReset); reset.setOnClickListener(view -> {
            runs.setText("Run #" + "0");

        });
        Button coap = findViewById(R.id.buttonStartCoAP); coap.setOnClickListener(view -> {
            runs.setText("Executando CoAP...");
            String uri = "coap://coap.me/test"; //"coap://californium.eclipse.org"
            runCoap(500, SecurityMode.NO, false, true, uri);
            runs.setText("Finished! Run #" + "500");
            finish();

        });
        Button coapTea = findViewById(R.id.buttonStartCoAPTEA); coapTea.setOnClickListener(view -> {
            runs.setText("Executando CoAP + TEA...");
            String uri = "coap://coap.me/test"; //"coap://californium.eclipse.org"
            runCoap(500, SecurityMode.TEA, false, true, uri);
            runs.setText("Finished! Run #" + "500");
            finish();

        });
        Button coapDes = findViewById(R.id.buttonStartCoAPDES); coapDes.setOnClickListener(view -> {
            runs.setText("Executando CoAP + DES...");
            String uri = "coap://coap.me/test"; //"coap://californium.eclipse.org"
            runCoap(500, SecurityMode.DES, false, true, uri);
            runs.setText("Finished! Run #" + "500");
            finish();

        });
        Button coapAES128 = findViewById(R.id.buttonStartCoAPAES128); coapAES128.setOnClickListener(view -> {
            runs.setText("Executando CoAP + AES128...");
            String uri = "coap://coap.me/test"; //"coap://californium.eclipse.org"
            runCoap(500, SecurityMode.AES128, false, true, uri);
            runs.setText("Finished! Run #" + "500");
            finish();

        });
        Button coapAES256 = findViewById(R.id.buttonStartCoAPAES256); coapAES256.setOnClickListener(view -> {
            runs.setText("Executando CoAP + AES256...");
            String uri = "coap://coap.me/test"; //"coap://californium.eclipse.org"
            runCoap(500, SecurityMode.AES256, false, true, uri);
            runs.setText("Finished! Run #" + "500");
            finish();

        });
        Button mqtt = findViewById(R.id.buttonStartMQTT); mqtt.setOnClickListener(view -> {
            runs.setText("Executando MQTT...");
            runMqtt(499,MqttQos.AT_LEAST_ONCE,SecurityMode.NO);
            runs.setText("Finished! Run #" + "500");
            finish();
        });
        Button mqttTea = findViewById(R.id.buttonStartMQTTTEA); mqttTea.setOnClickListener(view -> {
            runs.setText("Executando MQTT + TEA...");
            runMqtt(499,MqttQos.AT_LEAST_ONCE,SecurityMode.TEA);
            runs.setText("Finished! Run #" + "500");
            finish();
        });
        Button mqttAES128 = findViewById(R.id.buttonStartMQTTAES128); mqttAES128.setOnClickListener(view -> {
            runs.setText("Executando MQTT + AES128...");
            runMqtt(499,MqttQos.AT_LEAST_ONCE,SecurityMode.AES128);
            runs.setText("Finished! Run #" + "500");
            finish();
        });
        Button mqttAES256 = findViewById(R.id.buttonStartMQTTAES256); mqttAES256.setOnClickListener(view -> {
            runs.setText("Executando MQTT + AES256...");
            String uri = "coap://coap.me/test"; //"coap://californium.eclipse.org"
            runMqtt(499,MqttQos.AT_LEAST_ONCE,SecurityMode.AES256);
            runs.setText("Finished! Run #" + "500");
            finish();
        });
        Button mqttDES = findViewById(R.id.buttonStartMQTTDES); mqttDES.setOnClickListener(view -> {
            runs.setText("Executando MQTT + DES...");
            runMqtt(499,MqttQos.AT_LEAST_ONCE,SecurityMode.DES);
            runs.setText("Finished! Run #" + "500");
            finish();
        });

        Button[] buttons= new Button[]{
                coap, mqtt,
                coapTea, mqttTea,
                coapDes, mqttDES,
                coapAES128, mqttAES128,
                coapAES256, mqttAES256,
        };
        // Simulação de clique
        //buttons[9].performClick();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void runCoap(int n, SecurityMode security, boolean large, boolean confirmable, String uri) {
        try {
            // Pre-processing
            coapRun = 0;
            generateKey(security);
            LocalEndpointManager manager = new LocalEndpointManager();
            Client client = new Client(manager, uri);
            String messagePayload;
            RequestBuilder requestBuilder;
            if (large) {
                requestBuilder = client.newRequestBuilder().setConfirmable(confirmable).changePath("/large");
                Message response = requestBuilder.send().getResponse();
                messagePayload = response.getPayloadAsString();
            } else {
                requestBuilder = client.newRequestBuilder().setConfirmable(confirmable);
                Message response = requestBuilder.send().getResponse();
                messagePayload = response.getPayloadAsString();
            }
            coapPayload = messagePayload;

            // Message Sending
            for (int h = 0; h < n; h++) {
                String times = getEncryptationTime(messagePayload, security);
                sendCoAPMessage(requestBuilder, times, coapRun++);
            }

        } catch (
                InterruptedException e) {
            e.printStackTrace();
        } catch (
                TimeoutException e) {
            e.printStackTrace();
        } catch (
                HostLookupException e) {
            e.printStackTrace();
        } catch (IOException |
                UnsupportedSchemeException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void runMqtt(int n, MqttQos qos, SecurityMode security) {
        String uri = "coap://coap.me/test"; //"coap://californium.eclipse.org"
        runCoap(1, security, false, true, uri);
generateKey(security);
        Mqtt3BlockingClient client = Mqtt3Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .buildBlocking();
        client.connect();
        System.out.println("Get coapPayload to " + coapPayload.length() + " bytes.;;");
        String messagePayload = coapPayload;
        System.out.println("Set messagePayload to " + messagePayload.length() + " bytes.;;");

        for (int i = 0; i < n; i++) {
            long beginFullTime = System.nanoTime();
            getEncryptationTime(messagePayload, security);
            client.publishWith().topic("test/topic").qos(qos).payload(messagePayload.getBytes()).send();
            long fullTime = System.nanoTime() - beginFullTime;
            System.out.println(";" + i++ + ";" + ajeita(fullTime) + ";" + qos + ";" + messagePayload.length() + "bytes" + ";");
        }

        client.disconnect();

    }

    private float ajeita(long time) {
        return (Float.valueOf(time) / 1000000);
    }

    private void bateryScript() {

//                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//                Intent batteryStatus = registerReceiver(null, ifilter);
//
//                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//                float batteryPct = level * 100 / (float) scale;
//                System.out.println("Batery level: " + level + "/" + scale + "(" + batteryPct + "%)");
//
//
//                Mqtt3BlockingClient client = Mqtt3Client.builder()
//                        .identifier(UUID.randomUUID().toString())
//                        .serverHost("broker.hivemq.com")
//                        .buildBlocking();
//                System.out.println("Will connect");
//                client.connect();
//                System.out.println("Will publish");
//                client.publishWith().topic("test/topic").qos(MqttQos.AT_LEAST_ONCE).payload("1".getBytes()).send();
//                client.disconnect();
//
//                level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//                scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//                batteryPct = level * 100 / (float) scale;
//                System.out.println("Batery level: " + level + "/" + scale + "(" + batteryPct + "%)");
//
//                System.out.println("Batery level: " + level + "(" + batteryPct + "%)");
    }


    private void sendCoAPMessage(RequestBuilder requestBuilder, String times, int i) throws
            InterruptedException, TimeoutException, HostLookupException, IOException {
        long resetedTime = System.nanoTime();
        Message response = requestBuilder.send().getResponse();
        long networkTime = System.nanoTime() - resetedTime;
        int responseCode = response.getCode();
        int payloadSize = response.getPayload().length;
        if (responseCode != 69) {
            System.out.println(";" + i + ";;NOT 200:" + responseCode);
            finish();
        }
        System.out.println(";" + ajeita(networkTime) + ";" + times + ";;" + payloadSize + "bytes");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getAllEncryptationTimes(TEA tea, String messagePayload) {
        /* Encripta nos quatro algoritmos*/
        long resetedTime = System.nanoTime();
        String encAes128 = AES.encrypt128(messagePayload);
        long aes128EncTime = 2 * (System.nanoTime() - resetedTime);

        resetedTime = System.nanoTime();
        String encAes256 = AES.encrypt256(messagePayload);
        long aes256EncTime = 2 * (System.nanoTime() - resetedTime);

        resetedTime = System.nanoTime();
        String encDes = DES.encrypt(messagePayload);
        long desEncTime = 2 * (System.nanoTime() - resetedTime);

        resetedTime = System.nanoTime();
        String encTea = tea.encrypt(messagePayload);
        long teaEncTime = 2 * (System.nanoTime() - resetedTime);

        /* Decripta nos quatro algoritmos*/
        resetedTime = System.nanoTime();
        String decAes128 = AES.decrypt128(encAes128);
        long aes128decTime = 2 * (System.nanoTime() - resetedTime);

        resetedTime = System.nanoTime();
        String decAes256 = AES.decrypt256(encAes256);
        long aes256decTime = 2 * (System.nanoTime() - resetedTime);

        resetedTime = System.nanoTime();
        String decDes = DES.decrypt(encDes);
        long desdecTime = 2 * (System.nanoTime() - resetedTime);

        resetedTime = System.nanoTime();
        String decTea = tea.decrypt(encTea);
        long teadecTime = 2 * (System.nanoTime() - resetedTime);

        System.out.println("----------------------DEBUG:");
        System.out.println("enc:");
        System.out.println("DEBUG:" + encAes128);
        System.out.println("DEBUG:" + encAes256);
        System.out.println("DEBUG:" + encDes);
        System.out.println("DEBUG:" + encTea);
        System.out.println("Dec:");
        System.out.println("DEBUG:" + decAes128);
        System.out.println("DEBUG:" + decAes256);
        System.out.println("DEBUG:" + decDes);
        System.out.println("DEBUG:" + decTea);

        return ajeita(aes128EncTime) + ";" + ajeita(aes256EncTime) + ";" + ajeita(desEncTime) + ";" + ajeita(teaEncTime) + ";"
                + ajeita(aes128decTime) + ";" + ajeita(aes256decTime) + ";" + ajeita(desdecTime) + ";" + ajeita(teadecTime) + ";";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getEncryptationTime(String messagePayload, SecurityMode security) {
        long resetedTime = 0;
        switch (security) {
            case NO:
                return "0;0";
            case DES:
                resetedTime = System.nanoTime();
                String encDes = DES.encrypt(messagePayload);
                long desEncTime = 2 * (System.nanoTime() - resetedTime);
                resetedTime = System.nanoTime();
                String decDes = DES.decrypt(encDes);
                long desdecTime = 2 * (System.nanoTime() - resetedTime);
                return desEncTime + ";" + desdecTime;
            case AES128:
                resetedTime = System.nanoTime();
                String encAes128 = AES.encrypt128(messagePayload);
                long aes128EncTime = 2 * (System.nanoTime() - resetedTime);
                resetedTime = System.nanoTime();
                String decAes128 = AES.decrypt128(encAes128);
                long aes128decTime = 2 * (System.nanoTime() - resetedTime);
                return aes128EncTime + ";" + aes128decTime;
            case AES256:
                resetedTime = System.nanoTime();
                String encAes256 = AES.encrypt256(messagePayload);
                long aes256EncTime = 2 * (System.nanoTime() - resetedTime);
                resetedTime = System.nanoTime();
                String decAes256 = AES.decrypt256(encAes256);
                long aes256decTime = 2 * (System.nanoTime() - resetedTime);
                return aes256EncTime + ";" + aes256decTime;
            case TEA:
                resetedTime = System.nanoTime();
                String encTea = tea.encrypt(messagePayload);
                long teaEncTime = 2 * (System.nanoTime() - resetedTime);
                resetedTime = System.nanoTime();
                String decTea = tea.decrypt(encTea);
                long teadecTime = 2 * (System.nanoTime() - resetedTime);
                return teaEncTime + ";" + teadecTime;
        }
        return "Operation mode not implemented.";

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void generateKey(SecurityMode security) {
        switch (security) {
            case DES:
                DES.generateKey();
                break;
            case AES128:
                AES.generateKeyAES128();
                break;
            case AES256:
                AES.generateKeyAES256();
                break;
            case TEA:
                tea = new TEA();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void runAll(String key, String uri) {
//
//        System.out.println(";; --------- CoAP Large");
//        runCoap(5, 100, true, true, true, uri);
//
///// Mesma payload do CoAP, extraida das rodadas anteriroes
//        System.out.println(";; --------- QoS 0");
//        runMqtt(500, MqttQos.AT_MOST_ONCE, false);
//        System.out.println(";; --------- QoS 1");
//        runMqtt(500, MqttQos.AT_LEAST_ONCE, false);
//        System.out.println(";; --------- QoS 2");
//        runMqtt(500, MqttQos.EXACTLY_ONCE, false);
//        System.out.println(";; --------- QoS 0 + Sec");
//        runMqtt(500, MqttQos.AT_MOST_ONCE, true);
//        System.out.println(";; --------- QoS 1 + Sec");
//        runMqtt(500, MqttQos.AT_LEAST_ONCE, true);
//        System.out.println(";; --------- QoS 2 + Sec");
//        runMqtt(500, MqttQos.EXACTLY_ONCE, true);
    }


}
