package com.example.r2d2_app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.Timer;
import java.util.UUID;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.*;
import android.content.Intent;

public class MainActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 1;
    final int ArduinoData = 1;
    final String LOG_TAG = "myLogs";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private static String MacAddress = "98:D3:31:F9:E0:8C"; // MAC-адрес БТ модуля
    private static final UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectedThred MyThred = null;
    public TextView mytext;
    Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11;
    boolean fl = false;
    Handler h;
    private StringBuilder sb = new StringBuilder();
    String[] messageStr = {
            "Ага... я понял твою задумку.",
            "Я написал песенку в стиле даб-степ. Хочешь послушать?",
            "Унц-унц-унц...",
            "Ха-ха!",
            "Ну что-о-о... Как дела?",
            "Привет, мой лучший друг!"
    };

    String[] moveQuotes = {
            "RUN FOR YOUR LIIIIIVES!!!",
            "Unts unts unts unts!",
            "Unts unts unts unts!",
            "Wheeeee!",
            "Dangit, I'm out!",
            "Health over here!"
    };
    /*
    * CRY

    notes: 659, 587, 739, 659, 698, 1046, 830, 739, 1046, 830, 739,
        932, 1318, 1108, 1396, 1661, 783, 1108, 1760, 1244, 2637,
        1244, 880, 987, 1479, 1108, 1318, 2093, 932, 987, 1244, 1760,
        1396, 1046, 1108, 1244, 2093, 1046, 1108, 3135, 1244, 3135,
        987, 2093, 880, 1244, 1108, 2637, 1244, 698, 1174, 1046, 783,
        739, 659, 622, 698, 783

    duration:  9, 26, 26, 9, 9, 9, 9, 9, 9, 9, 9, 17, 9, 17, 9, 9,
        9, 9, 9, 17, 9, 9, 9, 17, 9, 9, 17, 9, 17, 17, 9, 9,
        17, 9, 9, 26, 9, 9, 9, 9, 9, 61, 9, 9, 9, 9, 9, 9, 9,
        9, 9, 9, 17, 9, 9, 9, 17

    delay: 20, 39, 29, 78, 10, 10, 10, 30, 10, 10, 10, 10, 29, 10,
        49, 10, 10, 30, 69, 30, 19, 10, 10, 20, 19, 10, 10, 77,
        10, 29, 19, 10, 10, 48, 20, 49, 29, 30, 30, 30, 10, 20,
        117, 10, 20, 10, 20, 60, 10, 10, 10, 10, 40, 117, 10, 10,
        10, 19
    --------------------------------------------------------------------------

    R2D2Question

    notes: 2793, 3135, 1864, 1661, 1479, 1567, 1661, 1760, 1864, 1975, 2093, 2217, 2349, 2489, 2959, 2637, 2637, 2793, 2959, 2489, 2959, 2793, 1864, 1760, 1864, 1975, 2093, 2217, 2349, 2489, 2637, 2793, 2959, 3135
    duration:  17, 17, 9, 96, 17, 35, 9, 17, 17, 9, 9, 9, 9, 9, 26, 35, 35, 17, 17, 26, 9, 9, 17, 235, 17, 26, 44, 26, 26, 9, 17, 17, 26, 70
    delay: 19, 65, 10, 349, 19, 39, 10, 19, 19, 10, 10, 10, 10, 38, 29, 47, 47, 19, 330, 29, 105, 46, 19, 262, 19, 29, 48, 29, 49, 10, 19, 19, 29, 78
    --------------------------------------------------------------------------

    Surprise

    notes: 233, 246, 277, 293, 349, 391, 440, 493, 1479, 1567, 1864, 2093, 2489, 739, 880, 659, 830, 880, 932, 783, 2793, 880, 1108, 1396, 1174, 1244, 1396, 1479, 1864, 2217, 2349, 2793, 2489, 987, 932
    duration:  9, 9, 9, 9, 9, 9, 9, 9, 17, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9
    delay: 10, 10, 10, 10, 10, 10, 10, 39, 19, 20, 10, 10, 58, 20, 10, 10, 10, 20, 10, 10, 10, 59, 10, 10, 10, 10, 10, 10, 10, 10, 10, 118, 50, 10, 10
    --------------------------------------------------------------------------

    QuoteThree

    notes: 1760, 1864, 1975, 2489, 2349, 1396, 1174, 1108, 1479, 2637, 1864, 415, 554, 622, 739, 440, 987, 1046, 739, 698, 987, 1174, 1864, 523, 698, 1174, 1661, 1479, 1760, 2217, 3135, 2217, 523, 1760, 2349, 1318, 1864, 2637, 2489, 1760, 1567
    duration:  17, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 17, 9, 17, 9, 9, 9, 9, 17, 35, 26, 9, 9, 9, 9, 9, 9, 9, 9
    delay:  19, 10, 10, 10, 59, 10, 10, 156, 10, 98, 10, 49, 10, 20, 10, 10, 29, 20, 20, 10, 10, 10, 117, 19, 39, 19, 10, 10, 10, 10, 49, 49, 29, 10, 10, 10, 10, 10, 68, 10, 10
    --------------------------------------------------------------------------

    Offer

    notes: 174, 554, 184, 195, 207, 233, 293, 233, 369, 440, 493, 554, 622, 698, 783, 1396, 1174, 1318, 1661, 1760, 2093, 2349, 2637, 2793, 2959, 3135, 1975, 1174, 554, 2217, 659, 783
    duration:   9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 17, 9, 9, 9, 9, 35, 9, 9, 9
    delay: 10, 10, 10, 10, 10, 20, 10, 10, 10, 20, 10, 10, 10, 20, 10, 10, 10, 10, 10, 10, 10, 10, 10, 19, 10, 380, 20, 90, 39, 10, 10, 10
    --------------------------------------------------------------------------

    Broken

    notes: 1174, 1760, 987, 1661, 2793, 554, 587, 523, 2637, 2793, 1108, 523, 783, 493, 783, 1108, 1174, 493, 523, 207, 1046, 1108, 220
    duration:  9, 9, 9, 17, 17, 9, 9, 26, 9, 148, 9, 9, 9, 17, 9, 26, 26, 26, 26, 17, 17, 61, 9
    delay: 10, 10, 10, 19, 19, 49, 89, 29, 10, 204, 10, 10, 49, 19, 10, 107, 39, 29, 87, 19, 19, 68, 10
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        mytext = (TextView) findViewById(R.id.txtrobot);

        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {
                mytext.setText("Bluetooth включен. Все отлично.");
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            MyError("Fatal Error", "Bluetooth ОТСУТСТВУЕТ");
        }

        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b5 = (Button) findViewById(R.id.b5);
        b6 = (Button) findViewById(R.id.b6);
        b7 = (Button) findViewById(R.id.b7);
        b8 = (Button) findViewById(R.id.b8);
        b9 = (Button) findViewById(R.id.b9);
        b10 = (Button) findViewById(R.id.b10);
        b11 = (Button) findViewById(R.id.b11);

        b0.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("0");
                mytext.setText(moveQuotes[4]);
                if (fl) {
                    fl = false;
                    b1.setEnabled(true);
                    b2.setEnabled(true);
                    b3.setEnabled(true);
                    b4.setEnabled(true);
                    b5.setEnabled(true);
                    b5.setEnabled(true);
                    b6.setEnabled(true);
                    b7.setEnabled(true);
                    b8.setEnabled(true);
                    b9.setEnabled(true);
                    b10.setEnabled(true);
                    b11.setEnabled(true);
                }
            }
        });

        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("1");
                mytext.setText(moveQuotes[0]);
            }
        });

        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("2");
                mytext.setText(moveQuotes[3]);
            }
        });

        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("3");
                mytext.setText(moveQuotes[1]);
            }
        });

        b4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("4");
                mytext.setText(moveQuotes[2]);
            }
        });

        b5.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("5");

                int rand = (int) (Math.random() * messageStr.length);
                String str = messageStr[rand];
                mytext.setText("- R2D2: '" + str + "'");
            }
        });

        b6.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("6");
                mytext.setText(moveQuotes[5]);
            }
        });

        b7.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("7");
                mytext.setText(moveQuotes[5]);
            }
        });

        b8.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("8");
                mytext.setText(moveQuotes[5]);
            }
        });

        b9.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyThred.sendData("9");
                mytext.setText(moveQuotes[5]);
            }
        });

        View.OnTouchListener btnTouchRight = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    MyThred.sendData("10");
                } else if (action == MotionEvent.ACTION_UP) {
                    MyThred.sendData("12");
                }

                return false;
            }
        };

        b10.setOnTouchListener(spinHead("a"));
        b11.setOnTouchListener(spinHead("b"));


        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case ArduinoData: {
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);// формируем строку
                        int beginOfLineIndex = sb.indexOf("*");//определяем символы начала строки
                        int endOfLineIndex = sb.indexOf("#");//определяем символы конца строки
                        //Если блок данных соотвествует маске *данные# то выполняем код
                        /*Log.d(LOG_TAG, "***Получаем данные: " + beginOfLineIndex + "***"  );
                        Log.d(LOG_TAG, "***Получаем данные: " + endOfLineIndex + "***"  );
                        if ((endOfLineIndex > 0) && (beginOfLineIndex == 0)) { // если встречаем конец строки,
                            String sbprint = sb.substring(beginOfLineIndex, endOfLineIndex-3); // то извлекаем строку
                            Log.d(LOG_TAG, "***Получаем данные: " + sbprint + "***"  );
                            if (sbprint == "mes") {

                            } else {
                                int sbprintInt = Integer.parseInt(sbprint);

                            }
                        }*/

                        sb.delete(0, sb.length());
                        break;
                    }
                }
            };
        };
    }

    public View.OnTouchListener spinHead(String side) {
        View.OnTouchListener btnTouchLeft = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    MyThred.sendData(side);
                } else if (action == MotionEvent.ACTION_UP) {
                    MyThred.sendData("c");
                }

                return false;
            }
        };

        return btnTouchLeft;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {
                BluetoothDevice device = btAdapter.getRemoteDevice(MacAddress);
                Log.d(LOG_TAG, "***Получили удаленный Device***"+device.getName());

                try {
                    btSocket = device.createRfcommSocketToServiceRecord(BT_UUID);
                    Log.d(LOG_TAG, "...Создали сокет...");
                } catch (IOException e) {
                    MyError("Fatal Error", "В onResume() Не могу создать сокет: " + e.getMessage() + ".");
                }

                btAdapter.cancelDiscovery();
                Log.d(LOG_TAG, "***Отменили поиск других устройств***");
                Log.d(LOG_TAG, "***Соединяемся...***");

                try {
                    btSocket.connect();
                    Log.d(LOG_TAG, "***Соединение успешно установлено***");
                } catch (IOException e) {
                    try {
                        btSocket.close();
                    } catch (IOException e2) {
                        MyError("Fatal Error", "В onResume() не могу закрыть сокет" + e2.getMessage() + ".");
                    }
                }

                MyThred = new ConnectedThred(btSocket);
                MyThred.start();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "...In onPause()...");

        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {
                if (MyThred.status_OutStrem() != null) {
                    MyThred.cancel();
                }
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    MyError("Fatal Error", "В onPause() Не могу закрыть сокет" + e2.getMessage() + ".");
                }
            }
        }
    }//OnPause

    private void MyError(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }


    //Отдельный поток для передачи данных
    private class ConnectedThred extends Thread {
        private final BluetoothSocket copyBtSocket;
        private final OutputStream OutStrem;
        private final InputStream InStrem;

        public ConnectedThred(BluetoothSocket socket) {
            copyBtSocket = socket;
            OutputStream tmpOut = null;
            InputStream tmpIn = null;
            try {
                tmpOut = socket.getOutputStream();
                tmpIn = socket.getInputStream();
            } catch (IOException e) {}

            OutStrem = tmpOut;
            InStrem = tmpIn;
        }

        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;

            while(true){
                try {
                    bytes = InStrem.read(buffer);
                    h.obtainMessage(ArduinoData, bytes, -1, buffer).sendToTarget();
                } catch(IOException e) {
                    break;
                }
            }

        }

        public void sendData(String message) {
            byte[] msgBuffer = message.getBytes();
            Log.d(LOG_TAG, "***Отправляем данные: " + message + "***"  );

            try {
                OutStrem.write(msgBuffer);
            } catch (IOException e) {}
        }

        public void cancel() {
            try {
                copyBtSocket.close();
            } catch(IOException e) {}
        }

        public Object status_OutStrem() {
            if (OutStrem == null) {
                return null;
            } else {return OutStrem;}
        }
    }
}