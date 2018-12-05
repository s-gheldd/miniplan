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
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.sauroter.miniplan.activity.SettingsActivity;
import de.sauroter.miniplan.miniplan.BuildConfig;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.task.RemovePastDatabaseEntriesTask;
import de.sauroter.miniplan.util.AlarmUtil;
import timber.log.Timber;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TIME = "TIME";
    public static final String PLACE = "PLACE";
    public static final String CHANNEL_ID = "ALARMS";
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.GERMAN);
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


        final Date now = new Date();
        final String graceString = PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsActivity.PREF_ALARM_GRACE, "15");
        final int grace = Integer.parseInt(graceString) * 1000 * 60;

        // if date is to long in the past, dont send notification
        if (now.getTime() - date.getTime() > grace) {
            return;
        }


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

        new RemovePastDatabaseEntriesTask(context).execute(new Date(System.currentTimeMillis() - 3600000));
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


        final Date now = new Date();

        builder.setContentTitle(context.getString(R.string.notification_title))
                .setColor(ContextCompat.getColor(context, R.color.flame_red))
                .setContentText(getRemainingTimeString(now, date))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getRemainingTimeStringLong(now, date, place)))
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

    private static String getRemainingTimeString(final Date now, final Date date) {
        final long hour = getHour(now, date);
        final long minute = getMinute(now, date);
        if (hour != 0) {
            return String.format(Locale.GERMANY, "Gottesdienst in %d Stunden %d Minuten", hour, minute, dateFormat.format(date));
        } else {
            return String.format(Locale.GERMANY, "Gottesdienst in %d Minuten", minute, dateFormat.format(date));
        }
    }

    private static String getRemainingTimeStringLong(final Date now, final Date date, final String place) {

        final long hour = getHour(now, date);
        final long minute = getMinute(now, date);
        if (hour != 0) {
            return String.format(Locale.GERMANY, "Gottesdienst in %d Stunden %d Minuten\n%s Uhr, %s", hour, minute, dateFormat.format(date), place);
        } else {
            return String.format(Locale.GERMANY, "Gottesdienst in %d Minuten\n%s Uhr, %s", minute, dateFormat.format(date), place);
        }
    }

    static long getHour(@NonNull final Date now, @NonNull final Date date) {
        final long dif = (date.getTime() - now.getTime());

        final long hour = dif / DateUtils.HOUR_IN_MILLIS;
        return hour < 0 ? 0 : hour;
    }

    static long getMinute(@NonNull final Date now, @NonNull final Date date) {
        final long dif = (date.getTime() - now.getTime());

        final long minute = (dif % DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS;
        return minute < 0 ? 0 : minute;
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
        Timber.d("Alarm canceled for %s place %s result %b", date, place, before);
        AlarmUtil.cancelAlarm(context, intent, id);
        if (BuildConfig.DEBUG && before && AlarmUtil.hasAlarm(context, intent, id)) {
            throw new AssertionError();
        }
    }
}