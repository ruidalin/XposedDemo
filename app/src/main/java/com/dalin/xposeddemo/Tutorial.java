package com.dalin.xposeddemo;

import android.os.Bundle;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Tutorial implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!"com.dalin.xposeddemo".equalsIgnoreCase(lpparam.packageName)) return;
        XposedBridge.log("Loaded app: " + lpparam.packageName);
        XposedHelpers.findAndHookMethod("com.dalin.xposeddemo.MainActivity", lpparam.classLoader, "onCreate", Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        // this will be called before the clock was updated by the original method
                        XposedBridge.log("beforeHookedMethod onCreate");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // this will be called after the clock was updated by the original method
                        XposedBridge.log("afterHookedMethod onCreate");
                        TextView mTextView = (TextView) XposedHelpers.getObjectField(param.thisObject, "mTextView");
                        mTextView.setText("Hello Xposed!");
                    }
                });
    }
}