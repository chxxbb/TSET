package com.example.chen.tset.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;

import com.example.chen.tset.Data.entity.Pharmacyremind;
import com.example.chen.tset.R;
import com.example.chen.tset.Utils.db.PharmacyDao;
import com.example.chen.tset.View.activity.HomeActivity;
import com.example.chen.tset.View.activity.LeadActivity;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

public class ServiceTwo extends Service {

    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        JMessageClient.init(getApplicationContext());
        JPushInterface.setDebugMode(true);


        try {
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_REDELIVER_INTENT;
    }

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {

                    SharedPsaveuser sp1 = new SharedPsaveuser(getApplicationContext());


                    if (sp1.getRemindState()) {

                        try {
                            Time time = new Time();
                            time.setToNow();
                            String currentTime = time.format("%H:%M");

                            PharmacyDao db = new PharmacyDao(getApplicationContext());
                            SharedPsaveuser sp = new SharedPsaveuser(getApplicationContext());

                            List<Pharmacyremind> list = db.chatfind(sp.getTag().getId() + "");


                            for (int i = 0; i < list.size(); i++) {
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                long timeMillis = findTimeMillis(df.format(curDate));

                                long startTime = findTimeMillis(list.get(i).getStartdate());

                                long endTime = findTimeMillis(list.get(i).getOverdate());


                                if (timeMillis >= startTime && timeMillis <= endTime) {
                                    if (currentTime.equals(list.get(i).getTime1())) {
                                        send();
                                    } else if (currentTime.equals(list.get(i).getTime2())) {
                                        send();
                                    } else if (currentTime.equals(list.get(i).getTime3())) {
                                        send();
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                    }

                    boolean b = HomeActivity.isServiceWorked(ServiceTwo.this, "com.example.chen.tset.Utils.ServiceOne");

                    if (!b) {
                        Intent service = new Intent(ServiceTwo.this, ServiceOne.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 59999);
        }
    });


    @Override
    public IBinder onBind(Intent arg0) {
        return null;

    }


    private void send() {


        Intent intent = new Intent(getApplicationContext(), LeadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification noti = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.app_log)
                .setContentText("亲，该吃药了")
                .setContentTitle("一条咸鱼干")
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        noti.defaults = Notification.DEFAULT_ALL;
        mNotificationManager.notify(0, noti);

    }


    public long findTimeMillis(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();

        return timestamp;
    }

}
