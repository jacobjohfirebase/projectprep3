package com.examplemobileappcompany.projectprep3bleaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.examplemobileappcompany.projectprep3aidlservice.IMyAidlInterface;


/*
Action items for today:
1. Submit the Github exercise (Edwin)
2. Submit the Espresso coding lab (Luis)

Action items for Wednesday 0900 AM
1. Watch the Jenkins tutorial and write and paragraph on how Jenkins work
2. Create simple POC application that uses
     -Dagger custom scopes
     -BLE to find devices and transfer simple string data
     -AIDL to communicate among different apps
     -Argument captors for Stubbing

 */
public class MainActivity extends AppCompatActivity {

    private IMyAidlInterface serviceInterface;
    private boolean connected = false;

    private Button callServiceButton;
    private TextView textView;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connected = true;
            serviceInterface = IMyAidlInterface.Stub.asInterface(service);
            callServiceButton.setEnabled(true);
            Log.d("mylog", "connected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("mylog", "disconnected");
            serviceInterface = null;
            connected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callServiceButton = (Button) findViewById(R.id.button);
        callServiceButton.setEnabled(false);

        textView = (TextView) findViewById(R.id.textView);

        Intent intent = new Intent("com.examplemobileappcompany.projectprep3aidlservice.MyService.BIND");
        intent.setPackage("com.examplemobileappcompany.projectprep3aidlservice");
      //  intent.setAction(IMyAidlInterface.class.getName());
        bindService(intent, serviceConnection,Context.BIND_AUTO_CREATE);
        Log.d("mylog", String.valueOf(connected));
        try {
            if(connected) {
                Log.d("mylog", serviceInterface.testInterface());
                Log.d("mylog", String.valueOf(serviceInterface.getTime()));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void callService(View view) {
        if(connected){

            Log.d("mylog", String.valueOf(connected));
            try {
                String s = serviceInterface.testInterface();
                Log.d("mylog", s);
                textView.setText(s);

                Log.d("mylog", String.valueOf(serviceInterface.getTime()));

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void startScan(View view) {
        Intent i = new Intent(this, DeviceScanActivity.class);
        startActivity(i);
    }
}
