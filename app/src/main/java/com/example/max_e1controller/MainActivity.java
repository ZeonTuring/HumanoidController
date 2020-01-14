package com.example.max_e1controller;


import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Key;
import java.sql.BatchUpdateException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import android.app.ActionBar;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.InputDevice;
import android.util.DisplayMetrics;

import androidx.core.content.ContextCompat;


public class MainActivity extends Activity implements OnTouchListener {
    GlobalVariable gv;
    PS4_Controller ps4_controller;
    Mode mode = Mode.Demo;
    WalkingState walkingState = WalkingState.end;
    AttackState attackState = AttackState.end;

    boolean isBluetoothConnected;
    boolean isPS4Found;

    android.widget.Button button;
    android.widget.Button disable_all_torque;
    TextView text1;
    TextView text2;


    Timer forward_timer;
    Timer backward_timer;
    Timer left_timer;
    Timer right_timer;
    Timer L1_timer;
    Timer R1_timer;
    Handler delay;

    int alt_action;

    public enum WalkingState
    {
        forward_stepping,
        backward_stepping,

        end
    }


    public enum AttackState
    {
        attacking,
        end
    }

    public enum Mode
    {
        Demo,
        Preliminary,
        Fight

    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv = (GlobalVariable) getApplicationContext();
        if (gv.getBluetooth() == null)
            gv.setBluetooth(new Bluetooth(this));
        ps4_controller = new PS4_Controller();
        text1 = findViewById(R.id.debugText);
        text2 = findViewById(R.id.debugText2);

        button = findViewById(R.id.bluetooth);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(MainActivity.this, BluetoothActivity.class);
                // currentContext.startActivity(activityChangeIntent);

                MainActivity.this.startActivityForResult(activityChangeIntent, 1);
            }
        });
        //mevent.clear();
        disable_all_torque = findViewById(R.id.disable_all_torque);
        disable_all_torque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u003e\u00c1\u0000\u00ff".getBytes("ISO-8859-1"));
                }
                catch (Exception e){}
            }
        });
        Button up = findViewById(R.id.forward);
        up.setOnTouchListener(this);

        final Button button_mode = findViewById(R.id.mode);


        button_mode.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                switch(mode)
                {
                    case Demo:
                        mode = Mode.Preliminary;
                        button_mode.setText("Prelim");
                        break;
                    case Preliminary:
                        mode = Mode.Fight;
                        button_mode.setText("Fight");
                        break;
                    case Fight:
                        mode = Mode.Demo;
                        button_mode.setText("Demo");
                        break;
                }
            }
        });
    }

    public void ProcessWalkAction(PS4_Controller.Button key)
    {
            try {
                switch (key) {
                    case UP_NONE_NONE:
                            switch (mode) {
                                case Demo:
                                    if (alt_action != 0)
                                        switch(alt_action)
                                        {
                                            case 16: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0005\u00fa\u0000\u00ff".getBytes("ISO-8859-1"));
                                            case 32: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0006\u00f9\u0000\u00ff".getBytes("ISO-8859-1"));
                                        }
                                    else
                                        //step forward
                                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u0001\u00fe\u0000\u00ff".getBytes("ISO-8859-1"));

                                    break;
                                case Preliminary:
                                    if (alt_action != 0)
                                        switch(alt_action)
                                        {
                                            case 16: gv.getBluetooth().SendDataLE("\u00ff\u0055\r\u00f2\u0000\u00ff".getBytes("ISO-8859-1"));
                                            case 32: gv.getBluetooth().SendDataLE("\u00ff\u0055\u000e\u00f1\u0000\u00ff".getBytes("ISO-8859-1"));
                                            case 2: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0001\u00fe\u0000\u00ff".getBytes("ISO-8859-1"));
                                            case 18: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0005\u00fa\u0000\u00ff".getBytes("ISO-8859-1"));
                                            case 34: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0006\u00f9\u0000\u00ff".getBytes("ISO-8859-1"));
                                        }
                                    else

                                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u000b\u00f4\u0000\u00ff".getBytes("ISO-8859-1"));

                                    break;
                                case Fight:
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0015\u00ea\u0000\u00ff".getBytes("ISO-8859-1"));
                                    break;
                            }
                            break;
                    case DOWN_NONE_NONE:
                        switch (mode) {
                            case Demo:
                               if (alt_action != 0)
                                    switch(alt_action)
                                    {
                                        case 16: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0007\u00f8\u0000\u00ff".getBytes("ISO-8859-1"));
                                        case 32: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0008\u00f7\u0000\u00ff".getBytes("ISO-8859-1"));
                                    }
                                else
                                    //step backward
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0002\u00fd\u0000\u00ff".getBytes("ISO-8859-1"));
                            case Preliminary:
                              if (alt_action != 0)
                                    switch(alt_action)
                                    {
                                        case 16: gv.getBluetooth().SendDataLE("\u00ff\u0055\u000f\u00f0\u0000\u00ff".getBytes("ISO-8859-1"));
                                        case 32: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0010\u00ef\u0000\u00ff".getBytes("ISO-8859-1"));
                                        case 2: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0002\u00fd\u0000\u00ff".getBytes("ISO-8859-1"));
                                        case 18: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0007\u00f8\u0000\u00ff".getBytes("ISO-8859-1"));
                                        case 34: gv.getBluetooth().SendDataLE("\u00ff\u0055\u0008\u00f7\u0000\u00ff".getBytes("ISO-8859-1"));
                                    }
                                else
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u000c\u00f3\u0000\u00ff".getBytes("ISO-8859-1"));

                                break;
                            case Fight:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0016\u00e9\u0000\u00ff".getBytes("ISO-8859-1"));
                                break;
                        }
                        break;
                    case LEFT_NONE_NONE:
                        switch (mode)
                        {
                            case Demo: case Preliminary:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0003\u00fc\u0000\u00ff".getBytes("ISO-8859-1"));
                                break;
                            case Fight:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0017\u00e8\u0000\u00ff".getBytes("ISO-8859-1"));
                                break;
                        }
                        break;
                    case RIGHT_NONE_NONE:
                        switch (mode)
                        {
                            case Demo: case Preliminary:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0004\u00fb\u0000\u00ff".getBytes("ISO-8859-1"));
                                break;
                            case Fight:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0018\u00e7\u0000\u00ff".getBytes("ISO-8859-1"));
                                break;
                        }
                        break;
                }
            } catch (Exception e) {

            }


    }

    @Override
    protected void onDestroy()
    {
        if (forward_timer != null)
            forward_timer.cancel();
        if (backward_timer != null)
            backward_timer.cancel();
        if (left_timer != null)
            left_timer.cancel();
        if (right_timer != null)
            right_timer.cancel();
        if (L1_timer != null)
            L1_timer.cancel();
        if (R1_timer != null)
            R1_timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean bluetooth_connection = data.getBooleanExtra("bluetooth_connection", false);
        if (!bluetooth_connection)
        {
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.bluetooth_button_black));
        } else
        {
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.bluetooth_button_blue));
        }
    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        text1.setText(ps4_controller.GetGenericButtonPressed(event).toString());
        final InputEvent inputEvent = event;
        if (forward_timer != null)
            forward_timer.cancel();
        forward_timer = new Timer();
        forward_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                        ProcessWalkAction(ps4_controller.GetGenericButtonPressed(inputEvent));
            }
        },200,100);
        return true;
//        switch(ps4_controller.GetGenericButtonPressed(event))
//        {
//            case DPAD_UP:
//            {
//                if (text1.getText() == "UP")
//                    text2.setText("UP");
//                else
//                    text1.setText("UP");
//                forward_timer = new Timer();
//                forward_timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        ProcessWalkAction(PS4_Controller.Button.DPAD_UP);
//                    }
//                },200,100);
//               return true;
//            }
//            case DPAD_NONE:
//            {
//                if (text1.getText() == "NONE")
//                    text2.setText("NONE");
//                else
//                    text1.setText("NONE");
//
//                if (forward_timer != null)
//                    forward_timer.cancel();
//                if (backward_timer != null)
//                    backward_timer.cancel();
//                if (left_timer != null)
//                    left_timer.cancel();
//                if (right_timer != null)
//                    right_timer.cancel();
////                if(walkingState != WalkingState.end) {
////                    delay = new Handler();
////                    delay.postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            ProcessWalkAction(PS4_Controller.Button.DPAD_NONE);
////                        }
////                    }, 200);
//               // }
//
//                return true;
//            }
//            case DPAD_DOWN:
//            {
//                if (text1.getText() == "DOWN")
//                    text2.setText("DOWN");
//                else
//                text1.setText("DOWN");
//                backward_timer = new Timer();
//                backward_timer.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        ProcessWalkAction(PS4_Controller.Button.DPAD_DOWN);
//                    }
//                },0,100);
//                return true;
//            }
//            case DPAD_LEFT:
//            {
//                text1.setText("LEFT");
//                left_timer = new Timer();
//                left_timer.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        ProcessWalkAction(PS4_Controller.Button.DPAD_LEFT);
//                    }
//                },0,100);
//                return true;
//            }
//            case DPAD_RIGHT:
//            {
//                text1.setText("RIGHT");
//                right_timer = new Timer();
//                right_timer.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        ProcessWalkAction(PS4_Controller.Button.DPAD_RIGHT);
//                    }
//                },0,100);
//                return true;
//            }
//            case NONE_UP:
//            {
//                if (text1.getText() == "NONE_UP")
//                    text2.setText("NONE_UP");
//                else
//                    text1.setText("NONE_UP");
//
//                return true;
//            }
//            case NONE_DOWN:
//            {
//                if (text1.getText() == "NONE_DOWN")
//                    text2.setText("NONE_DOWN");
//                else
//                    text1.setText("NONE_DOWN");
//
//                return true;
//            }
//            case NONE_LEFT:
//            {
//                if (text1.getText() == "NONE_LEFT")
//                    text2.setText("NONE_LEFT");
//                else
//                    text1.setText("NONE_LEFT");
//
//                return true;
//            }
//            case NONE_RIGHT:
//            {
//                if (text1.getText() == "NONE_RIGHT")
//                    text2.setText("NONE_RIGHT");
//                else
//                    text1.setText("NONE_RIGHT");
//
//                return true;
//            }
////            case none:
////            {
////                if (text1.getText() == "NONE_NONE")
////                    text2.setText("NONE_NONE");
////                else
////                    text1.setText("NONE_NONE");
////
////                return true;
////            }
//            default:
//
//
//        }
//        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (event.getRepeatCount() == 0)
        {
            switch(PS4_Controller.DecodeKeyCode(keyCode))
            {
                case GAMEPAD_TRIANGLE:
                {
                    if (text1.getText() == "Triangle Up")
                        text2.setText("Triangle Up");
                    else
                        text1.setText("Triangle Up");
                    alt_action -= 1;
                    return true;
                }
                case GAMEPAD_CROSS:
                {
                    if (text1.getText() == "Cross Up")
                        text2.setText("Cross Up");
                    else
                        text1.setText("Cross Up");
                    alt_action -= 2;
                    return true;
                }
                case GAMEPAD_SQUARE:
                {
                    if (text1.getText() == "Square Up")
                        text2.setText("Square Up");
                    else
                        text1.setText("Square Up");
                    alt_action -= 4;
                    return true;
                }
                case GAMEPAD_CIRCLE:
                {
                    if (text1.getText() == "Circle Up")
                        text2.setText("Circle Up");
                    else
                        text1.setText("Circle Up");
                    alt_action -= 8;
                    return true;
                }
                case L1:
                {
                    if (text1.getText() == "L1 Up")
                        text2.setText("L1 Up");
                    else
                        text1.setText("L1 Up");
                    alt_action -= 16;
                    return true;
                }
                case R1:
                {
                    if (text1.getText() == "R1 Up")
                        text2.setText("R1 Up");
                    else
                        text1.setText("R1 Up");
                    alt_action -= 32;
                    return true;
                }
                case L2:
                {
                    if (text1.getText() == "L2 Up")
                        text2.setText("L2 Up");
                    else
                        text1.setText("L2 Up");
                    alt_action -= 64;
                    return true;
                }
                case R2:
                {
                    if (text1.getText() == "R2 Up")
                        text2.setText("R2 Up");
                    else
                        text1.setText("R2 Up");
                    alt_action -= 128;
                    return true;
                }
                case L3:
                {
                    if (text1.getText() == "L3 Up")
                        text2.setText("L3 Up");
                    else
                        text1.setText("L3 Up");
                    alt_action -= 256;
                    return true;
                }
                case R3:
                {
                    if (text1.getText() == "R3 Up")
                        text2.setText("R3 Up");
                    else
                        text1.setText("R3 Up");
                    alt_action -= 512;
                    return true;
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getRepeatCount() == 0)
        {
            switch(PS4_Controller.DecodeKeyCode(keyCode))
            {
                case GAMEPAD_TRIANGLE:
                {
                    if (text1.getText() == "Triangle")
                        text2.setText("Triangle");
                    else
                    text1.setText("Triangle");
                    alt_action += 1;
                    return true;
                }
                case GAMEPAD_CROSS:
                {
                    if (text1.getText() == "Cross")
                        text2.setText("Cross");
                    else
                    text1.setText("Cross");
                    alt_action += 2;
                    return true;
                }
                case GAMEPAD_SQUARE:
                {
                    if (text1.getText() == "Square")
                        text2.setText("Square");
                    else
                    text1.setText("Square");
                    alt_action += 4;
                    return true;
                }
                case GAMEPAD_CIRCLE:
                {
                    if (text1.getText() == "Circle")
                        text2.setText("Circle");
                    else
                    text1.setText("Circle");
                    alt_action += 8;
                    return true;
                }
                case L1:
                {
                    if (text1.getText() == "L1")
                        text2.setText("L1");
                    else
                        text1.setText("L1");
                   alt_action += 16;
                    return true;
                }
                case R1:
                {
                    if (text1.getText() == "R1")
                        text2.setText("R1");
                    else
                        text1.setText("R1");
                    alt_action += 32;
                    return true;
                }
                case L2:
                {
                    if (text1.getText() == "L2")
                        text2.setText("L2");
                    else
                        text1.setText("L2");
                    alt_action += 64;
                    return true;
                }
                case R2:
                {
                    if (text1.getText() == "R2")
                        text2.setText("R2");
                    else
                        text1.setText("R2");
                    alt_action += 128;
                    return true;
                }
                case L3:
                {
                    if (text1.getText() == "L3")
                        text2.setText("L3");
                    else
                        text1.setText("L3");
                    alt_action += 256;
                    return true;
                }
                case R3:
                {
                    if (text1.getText() == "R3")
                        text2.setText("R3");
                    else
                        text1.setText("R3");
                    alt_action += 512;
                    return true;
                }
                default:
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent me)
    {
        switch (v.getId())
        {
            case R.id.forward:
            {
               ProcessWalkAction(PS4_Controller.Button.UP_NONE_NONE);
            }
            break;
            case R.id.down:
            {
                ProcessWalkAction(PS4_Controller.Button.DOWN_NONE_NONE);
            }
            break;
            case R.id.left:
            {
                ProcessWalkAction(PS4_Controller.Button.LEFT_NONE_NONE);
            }
            break;
            case R.id.right:
            {
                ProcessWalkAction(PS4_Controller.Button.RIGHT_NONE_NONE);
            }
            break;
        }
        return true;
    }
}
