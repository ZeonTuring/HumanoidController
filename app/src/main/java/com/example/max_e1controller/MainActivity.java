package com.example.max_e1controller;


import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    boolean isTorqueOn = true;

    Button button_up;
    Button button_down;
    Button button_left;
    Button button_right;
    Button button_turn_left;
    Button button_turn_right;
    Button button_left_side_step;
    Button button_right_side_step;
    Button button_duck;
    Button button_bluetooth;
    Button button_ps4_controller;
    Button button_left_punch;
    Button button_left_forward_punch;
    Button button_left_side_punch;
    Button button_left_side_step_punch;
    Button button_right_punch;
    Button button_right_forward_punch;
    Button button_right_side_punch;
    Button button_right_side_step_punch;
    Button button_forward_double_punch;
    Button button_enable_all_torque;
    Button button_disable_all_torque;
    Button button_disable_upper_torque;
    Button button_standing;
    Button button_fight;
    Button button_get_up_front;
    Button button_get_up_back;
    Button button_defense;
    Button button_fall;
    Button button_mode;

    TextView text1;
    TextView text2;


    Timer forward_timer;
    Timer backward_timer;
    Timer left_timer;
    Timer right_timer;
    Timer L1_timer;
    Timer R1_timer;
    Handler delay;

    int alt_action = 0;
    int movement = 0;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv = (GlobalVariable) getApplicationContext();
        if (gv.getBluetooth() == null)
            gv.setBluetooth(new Bluetooth(this));
        ps4_controller = new PS4_Controller(this);

        button_up = findViewById(R.id.forward);
        button_down = findViewById(R.id.down);
        button_left = findViewById(R.id.left);
        button_right = findViewById(R.id.right);
        button_turn_left = findViewById(R.id.left_turn);
        button_turn_right = findViewById(R.id.right_turn);
        button_left_side_step = findViewById(R.id.left_side_step);
        button_right_side_step = findViewById(R.id.right_side_step);
        
        button_duck = findViewById(R.id.duck);
        button_bluetooth = findViewById(R.id.bluetooth);
        button_ps4_controller = findViewById(R.id.ps4);
        button_left_punch = findViewById(R.id.left_punch);
        button_left_forward_punch = findViewById(R.id.left_forward_punch);
        button_left_side_punch = findViewById(R.id.left_side_punch);
        button_left_side_step_punch = findViewById(R.id.left_side_step_punch);
        button_right_punch = findViewById(R.id.right_punch);
        button_right_forward_punch = findViewById(R.id.right_forward_punch);
        button_right_side_punch = findViewById(R.id.right_side_punch);
        button_right_side_step_punch = findViewById(R.id.right_side_step_punch);
        button_forward_double_punch = findViewById(R.id.forward_double_punch);
        button_enable_all_torque = findViewById(R.id.enable_all_torque);
        button_disable_all_torque = findViewById(R.id.disable_all_torque);
        button_disable_upper_torque = findViewById(R.id.disable_top_torque);
        button_standing = findViewById(R.id.standing);
        button_fight = findViewById(R.id.fight);
        button_get_up_front = findViewById(R.id.get_up_front);
        button_get_up_back = findViewById(R.id.get_up_back);
        button_defense = findViewById(R.id.defense);
        button_fall = findViewById(R.id.fall);
        button_mode = findViewById(R.id.mode);

        button_up.setOnTouchListener(this);
        button_down.setOnTouchListener(this);
        button_left.setOnTouchListener(this);
        button_right.setOnTouchListener(this);
        button_turn_left.setOnTouchListener(this);
        button_turn_right.setOnTouchListener(this);
        button_left_side_step.setOnTouchListener(this);
        button_right_side_step.setOnTouchListener(this);
        button_duck.setOnTouchListener(this);
        button_bluetooth.setOnTouchListener(this);
        button_ps4_controller.setOnTouchListener(this);
        button_left_punch.setOnTouchListener(this);
        button_left_forward_punch.setOnTouchListener(this);
        button_left_side_punch.setOnTouchListener(this);
        button_left_side_step_punch.setOnTouchListener(this);
        button_right_punch.setOnTouchListener(this);
        button_right_forward_punch.setOnTouchListener(this);
        button_right_side_punch.setOnTouchListener(this);
        button_right_side_step_punch.setOnTouchListener(this);
        button_forward_double_punch.setOnTouchListener(this);
        button_enable_all_torque.setOnTouchListener(this);
        button_disable_all_torque.setOnTouchListener(this);
        button_disable_upper_torque.setOnTouchListener(this);
        button_standing.setOnTouchListener(this);
        button_fight.setOnTouchListener(this);
        button_get_up_front.setOnTouchListener(this);
        button_get_up_back.setOnTouchListener(this);
        button_defense.setOnTouchListener(this);
        button_fall.setOnTouchListener(this);
        button_mode.setOnTouchListener(this);

        text1 = findViewById(R.id.debugText);
        text2 = findViewById(R.id.debugText2);
    }

    public void ProcessMovement(PS4_Controller.Button key)
    {
                switch (key) {
                    case UP_NONE_NONE:
                        movement += 1;
                        switch (mode) {
                            case Demo:
                                if (alt_action != 0)
                                    switch (alt_action) {
                                        case 16:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0005\u00fa\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 32:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0006\u00f9\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                    }
                                else
                                    //step forward
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0001\u00fe\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));

                                break;
                            case Preliminary:
                                if (alt_action != 0)
                                    switch (alt_action) {
                                        case 16:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\r\u00f2\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 32:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u000e\u00f1\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 2:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0001\u00fe\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 18:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0005\u00fa\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 34:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0006\u00f9\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                    }
                                else

                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u000b\u00f4\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));

                                break;
                            case Fight:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0011\u00ee\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                break;
                        }
                        break;
                    case DOWN_NONE_NONE:
                        movement += 2;
                        switch (mode) {
                            case Demo:
                                if (alt_action != 0)
                                    switch (alt_action) {
                                        case 16:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0007\u00f8\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 32:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0008\u00f7\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                    }
                                else
                                    //step backward
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0002\u00fd\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                            case Preliminary:
                                if (alt_action != 0)
                                    switch (alt_action) {
                                        case 16:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u000f\u00f0\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 32:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0010\u00ef\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 2:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0002\u00fd\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 18:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0007\u00f8\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                        case 34:
                                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0008\u00f7\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                    }
                                else
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u000c\u00f3\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));

                                break;
                            case Fight:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0012\u00ed\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                break;
                        }
                        break;
                    case LEFT_NONE_NONE:
                        movement += 4;
                        switch (mode) {
                            case Demo:
                            case Preliminary:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0003\u00fc\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                break;
                            case Fight:
                                if (alt_action != 4)
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0013\u00ec\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                break;
                        }
                        break;
                    case RIGHT_NONE_NONE:
                        movement += 8;
                        switch (mode) {
                            case Demo:
                            case Preliminary:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0004\u00fb\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                break;
                            case Fight:
                                if (alt_action != 8)
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0014\u00eb\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                break;
                        }
                        break;

                    case NONE_NONE_DOWN:
                        movement += 512;
                        if (mode == Mode.Fight)
                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0017\u00e8\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                        break;
                    case NONE_NONE_LEFT:
                        movement += 1024;
                        switch (mode)
                        {
                            case Fight:
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0015\u00ea\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                        }
                        break;
                    case NONE_NONE_RIGHT:
                        movement += 2048;
                        switch (mode)
                        {
                            case Fight:
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0016\u00e9\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                        }
                        break;
//                    case NONE_DOWN_DOWN:
//                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0032\u00cd\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
//                        break;
//                    case NONE_UP_UP:
//                        if (mode == Mode.Fight)
//                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0033\u00cc\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
//                        break;
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
            button_bluetooth.setBackground(ContextCompat.getDrawable(this, R.drawable.bluetooth_button_black));
        } else
        {
            button_bluetooth.setBackground(ContextCompat.getDrawable(this, R.drawable.bluetooth_button_blue));
        }
    }
    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        text1.setText(ps4_controller.GetGenericButtonPressed(event).toString());
        final InputEvent inputEvent = event;
        if (forward_timer != null)
            forward_timer.cancel();
        PS4_Controller.Button key = ps4_controller.GetGenericButtonPressed(inputEvent);
        if (key != PS4_Controller.Button.NO_GEN_KEY) {
            forward_timer = new Timer();
            forward_timer.schedule(new TimerTask() {
                @Override
                public void run() {
                        ProcessMovement(ps4_controller.GetGenericButtonPressed(inputEvent));
                }
            }, 0, 300);
        }
        else movement = 0;
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
//                        ProcessMovement(PS4_Controller.Button.DPAD_UP);
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
////                            ProcessMovement(PS4_Controller.Button.DPAD_NONE);
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
//                        ProcessMovement(PS4_Controller.Button.DPAD_DOWN);
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
//                        ProcessMovement(PS4_Controller.Button.DPAD_LEFT);
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
//                        ProcessMovement(PS4_Controller.Button.DPAD_RIGHT);
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
                    if (mode == Mode.Fight)
                    {

                                if (alt_action == 4)
                                    if (movement == 4)
                                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u0020\u00df\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                                    else
                                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u001a\u00e5\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    }
                    alt_action -= 4;
                    return true;
                }
                case GAMEPAD_CIRCLE:
                {
                    if (text1.getText() == "Circle Up")
                        text2.setText("Circle Up");
                    else
                        text1.setText("Circle Up");
                    if (mode == Mode.Fight)
                    {
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001c\u00e3\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    }
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
                    if (mode == Mode.Fight)
                    {
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001a\u00e5\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    }
                    return true;
                }
                case R1:
                {
                    if (text1.getText() == "R1 Up")
                        text2.setText("R1 Up");
                    else
                        text1.setText("R1 Up");
                    alt_action -= 32;
                    if (mode == Mode.Fight)
                    {
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001c\u00e3\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    }
                    return true;
                }
                case L2:
                {
                    if (text1.getText() == "L2 Up")
                        text2.setText("L2 Up");
                    else
                        text1.setText("L2 Up");
                    if (forward_timer != null)
                        forward_timer.cancel();
                    forward_timer = new Timer();
                    forward_timer.schedule(new TimerTask() {
                        @Override
                        public void run() {


                        }
                    },0,100);

                    alt_action -= 64;
                    return true;
                }
                case R2:
                {
                    if (text1.getText() == "R2 Up")
                        text2.setText("R2 Up");
                    else
                        text1.setText("R2 Up");
                    if (forward_timer != null)
                        forward_timer.cancel();
                    forward_timer = new Timer();
                    forward_timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (mode == Mode.Fight) {
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u003c\u00c3\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                            }

                        }
                    },0,100);
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
                    if (mode == Mode.Fight)
                    {
                        if (movement == 4)
                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u001f\u00e0\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                        else
                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0019\u00e6\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    }
                    return true;
                }
                case GAMEPAD_CIRCLE:
                {
                    if (text1.getText() == "Circle")
                        text2.setText("Circle");
                    else
                    text1.setText("Circle");
                    alt_action += 8;
                    if (mode == Mode.Fight)
                    {
                        if (movement == 8)
                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u0021\u00de\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                        else
                            gv.getBluetooth().SendDataLE("\u00ff\u0055\u001b\u00e4\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    }
                    return true;
                }
                case L1:
                {
                    if (text1.getText() == "L1")
                        text2.setText("L1");
                    else
                        text1.setText("L1");

                    alt_action += 16;
                    if (mode == Mode.Fight)
                    {
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001d\u00e2\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    }
                    return true;
                }
                case R1:
                {
                    if (text1.getText() == "R1")
                        text2.setText("R1");
                    else
                        text1.setText("R1");
                    alt_action += 32;
                    if (mode == Mode.Fight)
                    {
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001e\u00e1\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    }

                    return true;
                }
                case L2:
                {
                    if (text1.getText() == "L2")
                        text2.setText("L2");
                    else
                        text1.setText("L2");
                    alt_action += 64;
//                    if (forward_timer != null)
//                        forward_timer.cancel();
//                    forward_timer = new Timer();
//                    forward_timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            if (alt_action == 128) {
//                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u003d\u00c2\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
//                                isTorqueOn = false;
//                            }
//
//                        }
//                    },0,100);
                    return true;
                }
                case R2:
                {
                    if (text1.getText() == "R2")
                        text2.setText("R2");
                    else
                        text1.setText("R2");

                    alt_action += 128;
                    if (forward_timer != null)
                        forward_timer.cancel();
                    forward_timer = new Timer();
                    forward_timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (mode == Mode.Fight) {
                                gv.getBluetooth().SendDataLE("\u00ff\u0055\u003d\u00c2\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                            }

                        }
                    },0,100);
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
                case OPTION:
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
               ProcessMovement(PS4_Controller.Button.UP_NONE_NONE);
                break;
            case R.id.down:
                ProcessMovement(PS4_Controller.Button.DOWN_NONE_NONE);
                break;
            case R.id.left:
                ProcessMovement(PS4_Controller.Button.LEFT_NONE_NONE);
                break;
            case R.id.right:
                ProcessMovement(PS4_Controller.Button.RIGHT_NONE_NONE);
                break;
            case R.id.left_turn:
                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0015\u00ea\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.right_turn:
                gv.getBluetooth().SendDataLE("\u00ff\u0055\u0016\u00e9\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.left_side_step:
                if (mode == Mode.Demo || mode == Mode.Preliminary)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0003\u00fc\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                else gv.getBluetooth().SendDataLE("\u00ff\u0055\u0013\u00ec\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.right_side_step:
                if (mode == Mode.Demo || mode == Mode.Preliminary)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0004\u00fb\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                else gv.getBluetooth().SendDataLE("\u00ff\u0055\u0014\u00eb\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.bluetooth:
                if (me.getAction() == MotionEvent.ACTION_UP) {
                    Intent activityChangeIntent = new Intent(MainActivity.this, BluetoothActivity.class);
                    // currentContext.startActivity(activityChangeIntent);

                    MainActivity.this.startActivityForResult(activityChangeIntent, 1);
                }
                break;
            case R.id.ps4:
                if (me.getAction() == MotionEvent.ACTION_UP) {
                    if (ps4_controller.VerifyController())
                        button_ps4_controller.setBackground(ContextCompat.getDrawable(this, R.drawable.ps4_button_blue));
                    else button_ps4_controller.setBackground(ContextCompat.getDrawable(this, R.drawable.ps4_button_black));
                }
                break;
            case R.id.standing:
                if (me.getAction() == MotionEvent.ACTION_UP)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0028\u00d7\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));

                break;
            case R.id.fight:
                if (me.getAction() == MotionEvent.ACTION_UP)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0029\u00d6\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));

                break;
            case R.id.get_up_front:
                if (me.getAction() == MotionEvent.ACTION_UP)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0032\u00cd\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));

                break;
            case R.id.get_up_back:
                if (me.getAction() == MotionEvent.ACTION_UP)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u0033\u00cc\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));

                break;

            case R.id.enable_all_torque:
                if (me.getAction() == MotionEvent.ACTION_UP)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u003c\u00c3\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));

                break;
            case R.id.disable_top_torque:
                if (me.getAction() == MotionEvent.ACTION_UP)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u003d\u00c2\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.disable_all_torque:
                if (me.getAction() == MotionEvent.ACTION_UP)
                    gv.getBluetooth().SendDataLE("\u00ff\u0055\u003e\u00c1\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.mode:
                if (me.getAction() == MotionEvent.ACTION_UP)
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
                break;
            case R.id.duck:
                if (mode == Mode.Fight)
                    if (me.getAction() == MotionEvent.ACTION_UP)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u0017\u00e8\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.left_forward_punch:
                if (mode == Mode.Fight) {
                    if (me.getAction() == MotionEvent.ACTION_DOWN)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u0019\u00e6\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    else if (me.getAction() == MotionEvent.ACTION_UP)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001a\u00e5\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                }
                break;
            case R.id.right_forward_punch:
                if (mode == Mode.Fight)
                    if (me.getAction() == MotionEvent.ACTION_DOWN)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001b\u00e4\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    else if (me.getAction() == MotionEvent.ACTION_UP)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001c\u00e3\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.left_side_punch:
                if (mode == Mode.Fight)
                    if (me.getAction() == MotionEvent.ACTION_DOWN)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001d\u00e2\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    else if (me.getAction() == MotionEvent.ACTION_UP)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001a\u00e5\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.right_side_punch:
                if (mode == Mode.Fight)
                    if (me.getAction() == MotionEvent.ACTION_DOWN)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001e\u00e1\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    else if (me.getAction() == MotionEvent.ACTION_UP)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001c\u00e3\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.left_side_step_punch:
                if (mode == Mode.Fight)
                    if (me.getAction() == MotionEvent.ACTION_DOWN)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u001f\u00e0\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    else if (me.getAction() == MotionEvent.ACTION_UP)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u0020\u00df\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
            case R.id.right_side_step_punch:
                if (mode == Mode.Fight)
                    if (me.getAction() == MotionEvent.ACTION_DOWN)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\u0021\u00de\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                    else if (me.getAction() == MotionEvent.ACTION_UP)
                        gv.getBluetooth().SendDataLE("\u00ff\u0055\"\u00dd\u0000\u00ff".getBytes(StandardCharsets.ISO_8859_1));
                break;
        }
        return true;
    }
}
