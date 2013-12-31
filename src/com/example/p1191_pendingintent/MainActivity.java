package com.example.p1191_pendingintent;


import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

  final String LOG_TAG = "myLogs";

  NotificationManager nm;
  AlarmManager am;
  Intent intent1;
  Intent intent2;
  PendingIntent pIntent1;
  PendingIntent pIntent2;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    am = (AlarmManager) getSystemService(ALARM_SERVICE);
  }

  public void onClick1(View view) {
	  
		Calendar calendar = Calendar.getInstance();
		// 10:17 AM 
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 52);
		calendar.set(Calendar.SECOND, 0);
	  
	  
	    intent1 = createIntent("action 1", "extra 1");
	    pIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0);

	    intent2 = createIntent("action 2", "extra 2");
	    pIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0);

	    Log.d(LOG_TAG, "start");
	    am.set(AlarmManager.RTC, System.currentTimeMillis() + 12000, pIntent1);
	    
	  //  am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
      //          AlarmManager.INTERVAL_DAY, pIntent2);
	   // am.setRepeating(AlarmManager.ELAPSED_REALTIME,
	   //     SystemClock.elapsedRealtime() + 8000, 10000, pIntent2);
	    
	    
  }

  public void onClick2(View view) {
	  
	  am.cancel(pIntent1);
	  am.cancel(pIntent2);

  }
  
  public void onClick3(View view) {
	  
	  Toast.makeText(this, "Нажал!", Toast.LENGTH_LONG).show();
	  
	  Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
      intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
      startActivityForResult(intent, 999);
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  
	  /*
	  
	  if (resultCode == RESULT_OK) {
		    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
		    if (uri != null) {
		    	String ringTonePath = uri.toString();
		    	Toast.makeText(this, ringTonePath, Toast.LENGTH_LONG).show();
		    }
	  }
	  */
	  
	  Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
      if (resultCode == RESULT_OK && uri != null){
    	  String ringtone = uri.toString();
          Cursor c = getContentResolver().query(uri, null, null, null, null);
          int id;
          if (c != null && c.moveToFirst()) {
             id = c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
             if (id != -1){ringtone=c.getString(id);}
             Toast.makeText(this, ringtone, Toast.LENGTH_LONG).show();
             // ! разобраться почему вылетает если нажать отмена !
             //else{ringtone=rs(R.string.default_melody);}
             //rp.setText(" "+ringtone);
             //rp.refreshDrawableState();
             //ringtone += "=" + uri.toString();
       }}
      
      // ошибки уходят, если сделать автоисправление
      MediaPlayer mediaPlayer = new MediaPlayer();
      mediaPlayer.setDataSource(uri.toString());
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      mediaPlayer.prepare();
      mediaPlayer.start();
  }

  Intent createIntent(String action, String extra) {
    Intent intent = new Intent(this, Receiver.class);
    intent.setAction(action);
    intent.putExtra("extra", extra);
    return intent;
  }

  void compare() {
    Log.d(LOG_TAG, "intent1 = intent2: " + intent1.filterEquals(intent2));
    Log.d(LOG_TAG, "pIntent1 = pIntent2: " + pIntent1.equals(pIntent2));
  }

  @SuppressWarnings("deprecation")
void sendNotif(int id, PendingIntent pIntent) {
    Notification notif = new Notification(R.drawable.ic_launcher, "Notif "
        + id, System.currentTimeMillis());
    notif.flags |= Notification.FLAG_AUTO_CANCEL;
    notif.setLatestEventInfo(this, "Title " + id, "Content " + id, pIntent);
    nm.notify(id, notif);
  }
}