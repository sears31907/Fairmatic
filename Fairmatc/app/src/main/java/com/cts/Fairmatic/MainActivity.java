package com.cts.Fairmatic;
import com.zendrive.sdk.*;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zendrive.sdk.insurance.ZendriveInsurance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        zenDriveSetup();
        Log.d("TAG", String.format("ZendriveSDK is Setup = %s", Zendrive.isSDKSetup(getApplicationContext()) ));
        ZendriveInsurance.stopPeriod(getApplicationContext(), insuranceCallback);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ZendriveConfiguration getZenDriveConfig(){
        final ZendriveConfiguration zendriveConfiguration =
                new ZendriveConfiguration("qIT2nWj1hLNwoa4AcnfQrKNOJ8fS0xz3", "D100", ZendriveDriveDetectionMode.INSURANCE);
        Log.d("zenDriveConfig", zendriveConfiguration.toString());
        return zendriveConfiguration;
    }


    public boolean zenDriveSetup(){
        Zendrive.setup(
                this.getApplicationContext(),
                this.getZenDriveConfig(),
                MyZendriveBroadcastReceiver.class, //rename to your custom class
                MyZendriveNotificationProvider.class, //rename to your custom class
                new ZendriveOperationCallback() {
                    @Override
                    public void onCompletion(ZendriveOperationResult result) {
                        if (result.isSuccess()) {
                            Log.d("TAG", "ZendriveSDK setup success");
                        } else {
                            Log.d("TAG", String.format("ZendriveSDK setup failed %s", result.getErrorCode().toString()));
                        }
                    }
                }
        );

        return true;


    }

    ZendriveOperationCallback insuranceCallback = new ZendriveOperationCallback() {
        @Override
        public void onCompletion(ZendriveOperationResult zendriveOperationResult) {
            if (!zendriveOperationResult.isSuccess()) {
                Log.d("ZendriveSDKDebug", "Insurance period switch failed, error: " + zendriveOperationResult.getErrorCode().name());
            }
            else   {
                Log.d("tag", "Zendrive Insurace Callback Success");
            }
        }
    };

}