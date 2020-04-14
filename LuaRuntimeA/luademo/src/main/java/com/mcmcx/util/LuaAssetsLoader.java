package com.mcmcx.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.ArrayList;

//
public class LuaAssetsLoader
{
    private Context _context;
    public Context getContext(){ return _context; }
    private static LuaAssetsLoader _singleton = null;
    public static LuaAssetsLoader getSingleton() {
        return _singleton;
    }

    private long _threadID = -1;
    private String _threadName = "";
    public long getThreadID(){ return _threadID; }
    public String getThreadName(){ return _threadName; }

    private String              _appBaseDir;
    private String              _assetBaseDir;
    private AssetManager        _assetManager;
    private ArrayList<String>   _luaFiles = new ArrayList<String>();

    public static LuaAssetsLoader getSingletonAndCreate(Activity activity) {
        if(_singleton == null) {
            _singleton = CreateInstance(activity);
        }
        return _singleton;
    }

    private static LuaAssetsLoader CreateInstance(Activity activity) {
        LuaAssetsLoader loader = new LuaAssetsLoader(activity);
        if(!loader.Initialize()) {
            loader = null;
            return null;
        }
        return loader;
    }


    //
    public LuaAssetsLoader(Context context) {
        this._context = context;
    }

    //
    public void Release() {
    }

    public boolean Initialize()
    {
        _threadID = Thread.currentThread().getId();
        _threadName = Thread.currentThread().getName();

        Log.println(Log.INFO, "LXUTIL", "Initialize module (LuaLoader) ...");
        Log.println(Log.INFO, "LXUTIL", String.format("Info : release %s (SDK: %d), thread : (%d) %s",
                Build.VERSION.RELEASE, Build.VERSION.SDK_INT, this._threadID, this._threadName));


        //
        try {
            _assetManager = this._context.getAssets();
            if(_assetManager == null) {
                return false;
            }

            this._appBaseDir = this._context.getApplicationContext().getPackageResourcePath() + "!/";
            this._assetBaseDir = this._appBaseDir + "assets/";

            String[] filenames = _assetManager.list("ToLua/Lua");
            for(int i = 0; i < filenames.length; i ++) {
                filenames[i] = "ToLua/Lua/" + filenames[i];
            }
            this._luaFiles.addAll(Arrays.asList(filenames));
            filenames = _assetManager.list("Lua");
            for(int i = 0; i < filenames.length; i ++) {
                filenames[i] = "Lua/" + filenames[i];
            }
            this._luaFiles.addAll(Arrays.asList(filenames));
            Log.println(Log.WARN, "LXUTIL", "(LuaLoader) native count : " + this._luaFiles.size());

        }catch(Exception e){
            Log.println(Log.ERROR, "LXUTIL", "Initialize module (LuaLoader) error : " + e.getMessage());
        }

        return true;
    }

    public byte[] readBufferFromFile(String filename) {

        try {
            if(filename.contains("jar:file://")) {
                filename = filename.replace("jar:file://", "");
                filename = filename.replace(this._assetBaseDir, "");

                if(_assetManager != null) {
                    BufferedInputStream stream = new BufferedInputStream(_assetManager.open(filename, AssetManager.ACCESS_STREAMING));
                    int length = stream.available();
                    byte[] buffer = new byte[length];
                    stream.read(buffer, 0, length);
                    stream.close();
                    return buffer;
                }
            } else {
                File file = new File(filename);
                if(file.exists() && file.isFile()) {
                    FileInputStream stream = new FileInputStream(file);
                    int length = stream.available();
                    byte[] buffer = new byte[length];
                    stream.read(buffer, 0, length);
                    stream.close();
                    return buffer;
                }
            }

        }catch(Exception e){
            Log.println(Log.ERROR, "LXUTIL", "(LuaLoader) read file ("+ filename +") error : " + e.getMessage());
        }
        return null;
    }

    public String readStringFromFile(String filename) {
        try {
            if(filename.contains("jar:file://")) {
                filename = filename.replace("jar:file://", "");
                filename = filename.replace(this._assetBaseDir, "");

                if(_assetManager != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(_assetManager.open(filename, AssetManager.ACCESS_STREAMING),
                            Charset.forName("UTF-8")));

                    StringBuffer ss = new StringBuffer();
                    if(reader.ready()) {
                        String s;
                        while((s=reader.readLine()) != null) {
                            ss.append(s + System.lineSeparator());
                        }
                    }
                    reader.close();
                    return ss.toString();
                }
            } else {
                File file = new File(filename);
                if(file.exists() && file.isFile()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
                    StringBuffer ss = new StringBuffer();
                    if(reader.ready()) {
                        String s;
                        while((s=reader.readLine()) != null) {
                            ss.append(s + System.lineSeparator());
                        }
                    }
                    reader.close();
                    return ss.toString();
                }
            }


        }catch(Exception e){
            Log.println(Log.ERROR, "LXUTIL", "(LuaLoader) read file ("+ filename +")error : " + e.getMessage());
        }
        return "";
    }
}
