package com.example.screenoverlay;

import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Input {
    static Method injectInputEventMethod;
    static Object inputManager;
    final static String TAG = "input";

    static void injectInputEvent(MotionEvent motionEvent) {
        try {
            injectInputEventMethod.invoke(inputManager, motionEvent, 0);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    static {
        String methodName = "getInstance";
        Object[] objArr = new Object[0];
        try {
            Class<?> inputManagerClass;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                inputManagerClass = Class.forName("android.hardware.input.InputManagerGlobal");
            } else {
                inputManagerClass = android.hardware.input.InputManager.class;
            }

            inputManager = inputManagerClass.getDeclaredMethod(methodName)
                    .invoke(null, objArr);
            //Make MotionEvent.obtain() method accessible
            methodName = "obtain";
            MotionEvent.class.getDeclaredMethod(methodName)
                    .setAccessible(true);

            //Get the reference to injectInputEvent method
            methodName = "injectInputEvent";

            injectInputEventMethod = inputManagerClass.getMethod(methodName, android.view.InputEvent.class, Integer.TYPE);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
