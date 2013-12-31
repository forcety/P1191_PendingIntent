package com.example.p1191_pendingintent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;



public class Receiver extends BroadcastReceiver {

  final String LOG_TAG = "myLogs";
  

  @Override
  public void onReceive(Context ctx, Intent intent) {
    Log.d(LOG_TAG, "onReceive");
    Log.d(LOG_TAG, "action = " + intent.getAction());
    Log.d(LOG_TAG, "extra = " + intent.getStringExtra("extra"));
    
    String tmp = intent.getAction();
    
    if (tmp.equals("action 1")) {
    	Toast.makeText(ctx, "Сработал будильник № 1", Toast.LENGTH_LONG).show();
	}   
    
    if (tmp.equals("action 2")) {
    	Toast.makeText(ctx, "Сработал будильник № 2", Toast.LENGTH_LONG).show();
	}
    
    Intent intn = new Intent (ctx, MainActivity.class);
    intn.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
    ctx.startActivity (intn);
  }
}