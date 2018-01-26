###[官网教程](https://github.com/rovo89/XposedBridge/wiki/Development-tutorial)

> app/build.gradle: 
```groovy
    repositories {
        jcenter();
    }
    
    dependencies {
        provided 'de.robv.android.xposed:api:82'
        provided 'de.robv.android.xposed:api:82:sources'
    }
``` 
> AndroidManifest.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.dalin.xposeddemo"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk android:minSdkVersion="15" />

    <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name" >
        <meta-data
            android:name="xposedmodule"
            android:value="true" />
        <meta-data
            android:name="xposeddescription"
            android:value="Easy example which makes the status bar clock red and adds a smiley" />
        <meta-data
            android:name="xposedminversion"
            android:value="53" />
    </application>
</manifest>
```

> assets/xposed_init

```text
com.dalin.xposeddemo.Tutorial
```
> Tutorial.java
```java
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
```

> Now install/start your app again. As you have already enabled it in the Xposed Installer when you started it the first time, you do not need to do that again, rebooting is enough.

> 打开Xposed -- 模块 -- 勾选XposedDemo -- 重启 -- 打开Xposed -- 日志  
> 每次修改安装后都要重启才生效(有时候可能需要重新勾选XposedDemo才可以)
