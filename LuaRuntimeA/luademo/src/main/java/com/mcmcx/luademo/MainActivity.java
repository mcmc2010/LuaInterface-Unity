package com.mcmcx.luademo;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.mcmcx.util.LuaAssetsLoader;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LuaAssetsLoader loader = LuaAssetsLoader.getSingletonAndCreate(this);
        loader.readStringFromFile("jarfile://"+this.getApplicationContext().getPackageResourcePath() + "!/assets/Lua/tolua.lua");
    }
}
