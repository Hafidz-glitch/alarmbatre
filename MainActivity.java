public class MainActivity extends AppCompatActivity {
    private TextView batteryPercentage;
    private ProgressBar batteryProgress;
    private Switch batteryOptimizationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupBatteryMonitoring();
        setupOptimizationSwitch();
    }

    private void initializeViews() {
        batteryPercentage = findViewById(R.id.batteryPercentage);
        batteryProgress = findViewById(R.id.batteryProgress);
        batteryOptimizationSwitch = findViewById(R.id.batteryOptimizationSwitch);
    }

    private void setupBatteryMonitoring() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                batteryPercentage.setText(level + "%");
                batteryProgress.setProgress(level);
            }
        }, filter);
    }

    private void setupOptimizationSwitch() {
        batteryOptimizationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                requestBatteryOptimizationExemption();
            }
        });
    }

    private void requestBatteryOptimizationExemption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }
}