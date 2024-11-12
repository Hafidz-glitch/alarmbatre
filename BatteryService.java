public class BatteryService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private BroadcastReceiver batteryReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();
        registerBatteryReceiver();
        return START_STICKY;
    }

    private void startForegroundService() {
        NotificationChannel channel = new NotificationChannel(
            "battery_channel", 
            "Battery Monitoring", 
            NotificationManager.IMPORTANCE_LOW
        );
        
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, "battery_channel")
            .setContentTitle("Battery Manager Active")
            .setSmallIcon(R.drawable.battery_icon)
            .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    private void registerBatteryReceiver() {
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

                if (level <= 20 && status != BatteryManager.BATTERY_STATUS_CHARGING) {
                    sendLowBatteryNotification();
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    private void sendLowBatteryNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "battery_channel")
            .setContentTitle("Low Battery")
            .setContentText("Battery level is critically low!")
            .setSmallIcon(R.drawable.low_battery_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat.from(this).notify(2, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }
}