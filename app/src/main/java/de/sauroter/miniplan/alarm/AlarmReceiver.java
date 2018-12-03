package de.sauroter.miniplan.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.sauroter.miniplan.miniplan.BuildConfig;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.util.AlarmUtil;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TIME = "TIME";
    public static final String PLACE = "PLACE";
    public static final String CHANNEL_ID = "ALARMS";
    private static final DateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.GERMAN);
    @Nullable
    private static Bitmap largeIcon;


    @Override
    public void onReceive(@NonNull final Context context, @NonNull final Intent intent) {
        final Date date = (Date) intent.getSerializableExtra(TIME);
        final String place = intent.getStringExtra(PLACE);

        if (place != null && date != null) {
            sendNotification(date, place, context);
        }
    }

    public static void sendNotification(@NonNull final Date date,
                                        @NonNull final String place,
                                        @NonNull final Context context) {


        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        final Notification notification = createNotification(date, place, context);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.notification_channel_name), importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(Objects.hash(date, place), notification);
        removeAlarmForNotification(date, place, context.getApplicationContext());
    }


    private static Notification createNotification(@NonNull final Date date,
                                                   @NonNull final String place,
                                                   @NonNull final Context context) {


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        builder.setSmallIcon(R.drawable.ic_stat_miniplanthumbnail);
        if (largeIcon == null) {
            largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.minilogo_wei_);
        }
        builder.setLargeIcon(largeIcon);

        builder.setContentTitle(context.getString(R.string.notification_title))
                .setContentText(dateFormat.format(date) + context.getString(R.string.notifaction_text_seperator) + place)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent contentIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(launchIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        return builder.build();
    }

    /**
     * @param gracePeriod grace period between alarm and event in milliseconds.
     */
    public static void setAlarmForNotification(final int gracePeriod,
                                               @NonNull final Date date,
                                               @NonNull final String place,
                                               @NonNull final Context context) {
        final Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(TIME, date);
        intent.putExtra(PLACE, place);


        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, -gracePeriod);

        AlarmUtil.addAlarm(context, intent, Objects.hash(date, place), calendar);
    }


    public static void removeAlarmForNotification(@NonNull final Date date,
                                                  @NonNull final String place,
                                                  @NonNull final Context context) {

        final Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(TIME, date);
        intent.putExtra(PLACE, place);

        final int id = Objects.hash(date, place);
        final boolean before = AlarmUtil.hasAlarm(context, intent, id);
        Log.d(AlarmReceiver.class.getName(), "Alarm canceled for " + date + " place " + place + "    " + before);
        AlarmUtil.cancelAlarm(context, intent, id);
        if (BuildConfig.DEBUG && before && AlarmUtil.hasAlarm(context, intent, id)) {
            throw new AssertionError();
        }
    }
}