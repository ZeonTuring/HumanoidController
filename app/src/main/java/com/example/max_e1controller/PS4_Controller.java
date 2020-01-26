package com.example.max_e1controller;

import android.content.Intent;
import android.util.AttributeSet;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.content.Context;
import android.app.Activity;

import java.util.ArrayList;

public class PS4_Controller {

    Activity activity;
    int controllerID;
    public enum Button
    {
        GAMEPAD_TRIANGLE,
        GAMEPAD_CROSS,
        GAMEPAD_SQUARE,
        GAMEPAD_CIRCLE,
        L1,
        R1,
        L2,
        R2,
        L3,
        R3,
        OPTION,

        NO_KEY,
        
        NONE_NONE_UP,
        NONE_NONE_DOWN,
        NONE_NONE_LEFT,
        NONE_NONE_RIGHT,
        NONE_UP_NONE,
        NONE_UP_UP,
        NONE_UP_DOWN,
        NONE_UP_LEFT,
        NONE_UP_RIGHT,
        NONE_DOWN_NONE,
        NONE_DOWN_UP,
        NONE_DOWN_DOWN,
        NONE_DOWN_LEFT,
        NONE_DOWN_RIGHT,
        NONE_LEFT_NONE,
        NONE_LEFT_UP,
        NONE_LEFT_DOWN,
        NONE_LEFT_LEFT,
        NONE_LEFT_RIGHT,
        NONE_RIGHT_NONE,
        NONE_RIGHT_UP,
        NONE_RIGHT_DOWN,
        NONE_RIGHT_LEFT,
        NONE_RIGHT_RIGHT,
        UP_NONE_NONE,
        UP_NONE_UP,
        UP_NONE_DOWN,
        UP_NONE_LEFT,
        UP_NONE_RIGHT,
        UP_UP_NONE,
        UP_UP_UP,
        UP_UP_DOWN,
        UP_UP_LEFT,
        UP_UP_RIGHT,
        UP_DOWN_NONE,
        UP_DOWN_UP,
        UP_DOWN_DOWN,
        UP_DOWN_LEFT,
        UP_DOWN_RIGHT,
        UP_LEFT_NONE,
        UP_LEFT_UP,
        UP_LEFT_DOWN,
        UP_LEFT_LEFT,
        UP_LEFT_RIGHT,
        UP_RIGHT_NONE,
        UP_RIGHT_UP,
        UP_RIGHT_DOWN,
        UP_RIGHT_LEFT,
        UP_RIGHT_RIGHT,


        DOWN_NONE_NONE,
        DOWN_NONE_UP,
        DOWN_NONE_DOWN,
        DOWN_NONE_LEFT,
        DOWN_NONE_RIGHT,
        DOWN_UP_NONE,
        DOWN_UP_UP,
        DOWN_UP_DOWN,
        DOWN_UP_LEFT,
        DOWN_UP_RIGHT,
        DOWN_DOWN_NONE,
        DOWN_DOWN_UP,
        DOWN_DOWN_DOWN,
        DOWN_DOWN_LEFT,
        DOWN_DOWN_RIGHT,
        DOWN_LEFT_NONE,
        DOWN_LEFT_UP,
        DOWN_LEFT_DOWN,
        DOWN_LEFT_LEFT,
        DOWN_LEFT_RIGHT,
        DOWN_RIGHT_NONE,
        DOWN_RIGHT_UP,
        DOWN_RIGHT_DOWN,
        DOWN_RIGHT_LEFT,
        DOWN_RIGHT_RIGHT,

        LEFT_NONE_NONE,
        LEFT_NONE_UP,
        LEFT_NONE_DOWN,
        LEFT_NONE_LEFT,
        LEFT_NONE_RIGHT,
        LEFT_UP_NONE,
        LEFT_UP_UP,
        LEFT_UP_DOWN,
        LEFT_UP_LEFT,
        LEFT_UP_RIGHT,
        LEFT_DOWN_NONE,
        LEFT_DOWN_UP,
        LEFT_DOWN_DOWN,
        LEFT_DOWN_LEFT,
        LEFT_DOWN_RIGHT,
        LEFT_LEFT_NONE,
        LEFT_LEFT_UP,
        LEFT_LEFT_DOWN,
        LEFT_LEFT_LEFT,
        LEFT_LEFT_RIGHT,
        LEFT_RIGHT_NONE,
        LEFT_RIGHT_UP,
        LEFT_RIGHT_DOWN,
        LEFT_RIGHT_LEFT,
        LEFT_RIGHT_RIGHT,

        RIGHT_NONE_NONE,
        RIGHT_NONE_UP,
        RIGHT_NONE_DOWN,
        RIGHT_NONE_LEFT,
        RIGHT_NONE_RIGHT,
        RIGHT_UP_NONE,
        RIGHT_UP_UP,
        RIGHT_UP_DOWN,
        RIGHT_UP_LEFT,
        RIGHT_UP_RIGHT,
        RIGHT_DOWN_NONE,
        RIGHT_DOWN_UP,
        RIGHT_DOWN_DOWN,
        RIGHT_DOWN_LEFT,
        RIGHT_DOWN_RIGHT,
        RIGHT_LEFT_NONE,
        RIGHT_LEFT_UP,
        RIGHT_LEFT_DOWN,
        RIGHT_LEFT_LEFT,
        RIGHT_LEFT_RIGHT,
        RIGHT_RIGHT_NONE,
        RIGHT_RIGHT_UP,
        RIGHT_RIGHT_DOWN,
        RIGHT_RIGHT_LEFT,
        RIGHT_RIGHT_RIGHT,
        
        NO_GEN_KEY
    }
    public enum Position
    {
        LEFT,
        RIGHT
    }
    class ControllerKey
    {
        Button button;
        class Joystick
        {
            Position position;
            public int Axis_X;
            public int Axis_Y;
        }
    }
    public PS4_Controller(Activity act)
    {
        activity = act;
    }

    static public Button DecodeKeyCode(int keyCode)
    {
        if (keyCode == 99)
            return Button.GAMEPAD_TRIANGLE;
        else if (keyCode == 97)
            return Button.GAMEPAD_CROSS;
        else if (keyCode == 96)
            return Button.GAMEPAD_SQUARE;
        else if (keyCode == 98)
            return Button.GAMEPAD_CIRCLE;
        else if (keyCode == 100)
            return Button.L1;
        else if (keyCode == 101)
            return Button.R1;
        else if (keyCode == 102)
            return Button.L2;
        else if (keyCode == 103)
            return Button.R2;
        else if (keyCode == 109)
            return Button.L3;
        else if (keyCode == 108)
            return Button.R3;
        else if (keyCode == 105)
            return Button.OPTION;
        
        return Button.NO_KEY;
    }

    private static float GetJoystickAxis(MotionEvent event, InputDevice device, int axis)
    {
        final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());
        
        if (range != null)
        {
            final float flat = range.getFlat();
            final float value = event.getAxisValue(axis);
            if (Math.abs(value) > flat)
                return value;
        }
        return 0;
    }
    
    private static int GetJoystickPosition(float x, float y, boolean left)
    {
        if (y < 0 && (-y) > Math.abs(x))
            return 16 + (left ? 0 : 240);
        if (y >0 && y > Math.abs(x))
            return 32 + (left ? 0 : 480);
        if (x < 0 && (-x) > Math.abs(y))
            return 64 + (left ? 0 : 960);
        if (x > 0 && x > Math.abs(y))
            return 128 + (left ? 0 : 1920);
        
        return 0;
    }
    
    public Button GetGenericButtonPressed(InputEvent event) {
            MotionEvent motionEvent = (MotionEvent) event;
                float x_axis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
                float y_axis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);
                int button_pressed = 0;

                // Check if the AXIS_HAT_X value is -1 or 1, and set the D-pad
                // LEFT and RIGHT direction accordingly.
                if (Float.compare(x_axis, -1.0f) == 0) {
                    button_pressed = 4;
                } else if (Float.compare(x_axis, 1.0f) == 0) {
                    button_pressed = 8;
                }
                // Check if the AXIS_HAT_Y value is -1 or 1, and set the D-pad
                // UP and DOWN direction accordingly.
                else if (Float.compare(y_axis, -1.0f) == 0) {
                    button_pressed = 1;
                } else if (Float.compare(y_axis, 1.0f) == 0) {
                    button_pressed = 2;
                }

                InputDevice inputDevice = event.getDevice();

//                final float x = GetJoystickAxis(motionEvent, inputDevice, MotionEvent.AXIS_X);
//                final float y = GetJoystickAxis(motionEvent, inputDevice, MotionEvent.AXIS_Y);
//                final float z = GetJoystickAxis(motionEvent, inputDevice, MotionEvent.AXIS_Z);
//                final float rz = GetJoystickAxis(motionEvent, inputDevice, MotionEvent.AXIS_RZ);

                button_pressed += GetJoystickPosition(GetJoystickAxis(motionEvent, inputDevice, MotionEvent.AXIS_X), GetJoystickAxis(motionEvent, inputDevice, MotionEvent.AXIS_Y), true)
                    +GetJoystickPosition(GetJoystickAxis(motionEvent, inputDevice, MotionEvent.AXIS_Z), GetJoystickAxis(motionEvent, inputDevice, MotionEvent.AXIS_RZ), false);

                switch (button_pressed)
                {
                    case 0:
                        return Button.NO_GEN_KEY;

                    case 1:
                        return Button.UP_NONE_NONE;
                    case 2:
                        return Button.DOWN_NONE_NONE;
                    case 4:
                        return Button.LEFT_NONE_NONE;
                    case 8:
                        return Button.RIGHT_NONE_NONE;
                        
                    case 16:
                        return Button.NONE_UP_NONE;
                    case 32:
                        return Button.NONE_DOWN_NONE;
                    case 64:
                        return Button.NONE_LEFT_NONE;
                    case 128:
                        return Button.NONE_RIGHT_NONE;

                    case 256:
                        return Button.NONE_NONE_UP;
                    case 512:
                        return Button.NONE_NONE_DOWN;
                    case 1024:
                        return Button.NONE_NONE_LEFT;
                    case 2048:
                        return Button.NONE_NONE_RIGHT;
                        
                        
                    case 257:
                        return Button.UP_NONE_UP;
                    case 513:
                        return Button.UP_NONE_DOWN;
                    case 1025:
                        return Button.UP_NONE_LEFT;
                    case 2049:
                        return Button.UP_NONE_RIGHT;

                    
                    case 258:
                        return Button.DOWN_NONE_UP;
                    case 514:
                        return Button.DOWN_NONE_DOWN;
                    case 1026:
                        return Button.DOWN_NONE_LEFT;
                    case 2050:
                        return Button.DOWN_NONE_RIGHT;
                        
                    
                    case 260:
                        return Button.LEFT_NONE_UP;
                    case 516:
                        return Button.LEFT_NONE_DOWN;
                    case 1028:
                        return Button.LEFT_NONE_LEFT;
                    case 2052:
                        return Button.LEFT_NONE_RIGHT;
                        
                    
                    case 264:
                        return Button.RIGHT_NONE_UP;
                    case 520:
                        return Button.RIGHT_NONE_DOWN;
                    case 1032:
                        return Button.RIGHT_NONE_LEFT;
                    case 2056:
                        return Button.RIGHT_NONE_RIGHT;

                    case 272:
                        return Button.NONE_UP_UP;
                    case 528:
                        return Button.NONE_UP_DOWN;
                    case 1040:
                        return Button.NONE_UP_LEFT;
                    case 2064:
                        return Button.NONE_UP_RIGHT;

                    case 288:
                        return Button.NONE_DOWN_UP;
                    case 544:
                        return Button.NONE_DOWN_DOWN;
                    case 1056:
                        return Button.NONE_DOWN_LEFT;
                    case 2080:
                        return Button.NONE_DOWN_RIGHT;

                    case 320:
                        return Button.NONE_LEFT_UP;
                    case 576:
                        return Button.NONE_LEFT_DOWN;
                    case 1088:
                        return Button.NONE_LEFT_LEFT;
                    case 2112:
                        return Button.NONE_LEFT_RIGHT;

                    case 384:
                        return Button.NONE_RIGHT_UP;
                    case 640:
                        return Button.NONE_RIGHT_DOWN;
                    case 1152:
                        return Button.NONE_RIGHT_LEFT;
                    case 2176:
                        return Button.NONE_RIGHT_RIGHT;
                }

        return Button.NO_GEN_KEY;
    }

    public boolean VerifyController()
    {

        // Get PS4 Controller ID
        try {
            ArrayList<Integer> gameControllerDeviceIds = new ArrayList<Integer>();

            int[] deviceIds = InputDevice.getDeviceIds();
            for (int deviceId : deviceIds) {
                InputDevice dev = InputDevice.getDevice(deviceId);
                int sources = dev.getSources();
                if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                        && ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)
                        && ((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)) {
                    if (!gameControllerDeviceIds.contains(deviceId)) {
                        gameControllerDeviceIds.add(deviceId);
                    }
                }
            }
            controllerID = gameControllerDeviceIds.get(0);
            return true;
        }
        catch (Exception e)
        {
            Toast.makeText(activity,"Input Device Not Found!",Toast.LENGTH_SHORT).show();
        }
        return false;
    }



//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        boolean handled = false;
//        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
//                == InputDevice.SOURCE_GAMEPAD) {
//            if (event.getRepeatCount() == 0) {
//                switch (keyCode) {
//                    case KeyEvent.KEYCODE_DPAD_UP:
//                    {
//
//                    }
//                    break;
//                    case KeyEvent.KEYCODE_DPAD_DOWN:
//                    {
//
//                    }
//                    case KeyEvent.KEYCODE_DPAD_LEFT:
//                    {
//
//                    }
//                    break;
//                    case KeyEvent.KEYCODE_DPAD_RIGHT:
//                    {
//
//                    }
//                    break;
//                    default:
//                        break;
//                }
//            }
//            if (handled) {
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//    @Override
//    public boolean onGenericMotionEvent(MotionEvent event)
//    {
//        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
//                InputDevice.SOURCE_JOYSTICK &&
//                event.getAction() == MotionEvent.ACTION_MOVE) {
//
//            // Process all historical movement samples in the batch
//            final int historySize = event.getHistorySize();
//
//            // Process the movements starting from the
//            // earliest historical position in the batch
//            for (int i = 0; i < historySize; i++) {
//                // Process the event at historical position i
//                ProcessJoystickInput(event, i);
//            }
//
//            // Process the current movement sample in the batch (position -1)
//            ProcessJoystickInput(event, -1);
//            return true;
//        }
//        return super.onGenericMotionEvent(event);
//    }
//
//    private void ProcessJoystickInput(MotionEvent event, int historyPos)
//    {
//        InputDevice inputDevice = event.getDevice();
//        float x = GetJoystickValue(event, inputDevice, MotionEvent.AXIS_X, historyPos);
//    }
//
//    private static float GetJoystickValue(MotionEvent event,
//                                          InputDevice device, int axis, int historyPos)
//    {
//        final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());
//
//        if (range != null)
//        {
//            final float flat = range.getFlat();
//            final float value = historyPos < 0 ? event.getAxisValue(axis) : event.getHistoricalAxisValue(axis, historyPos);
//
//            if (Math.abs(value) > flat)
//                return value;
//        }
//        return 0;
//    }

}
