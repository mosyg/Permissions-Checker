package edu.uiuc.permissionschecker.receivers;

import edu.uiuc.permissionschecker.services.PhoneMonitorService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        
        if(bundle == null)
                return;
        
        String phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        Log.i("CallsReceiver","---"+phonenumber);
        Log.i("CallsReceiver","---"+bundle.toString());
        for (String key : bundle.keySet()) {
        	Log.i("CallsReceiver","---KEY:"+key);
        	Log.i("CallsReceiver","---DATA:"+bundle.get(key));
        }
        
        if (phonenumber != null) {
        	Log.i("CallsReceiver","---LAUNCHING SERVICE-----------------");
	        Intent i = new Intent(context, PhoneMonitorService.class);
	        context.startService(i);
        }
        
        String info = "Detect Calls sample application\nOutgoing number: " + phonenumber;
        
        //Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

}
