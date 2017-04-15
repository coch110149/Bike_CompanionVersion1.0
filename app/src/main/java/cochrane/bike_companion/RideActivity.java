package cochrane.bike_companion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import cochrane.bike_companion.Model.Ride;
import cochrane.bike_companion.Utilities.RideTracker;
import cochrane.bike_companion.Utilities.Utils;


public class RideActivity extends AppCompatActivity
{
    private static final String TAG = RideActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 40464;
    private TextView mDistanceText, mCurSpeedText, mMaxSpeedText, mAvgSpeedText, mElevLossText,
            mElevGainText, mGpsStatusView, mElevationText;
    private Button mStopButton, mStartPauseButton;
    private ImageButton mLockButton;
    private Chronometer mDurationText;
    private long mTimeWhenPaused = 0;
    private boolean mIsServiceCreated;
    private RideReceiver mReceiver;
    private int mBikeId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //// TODO: 14/04/2017 manage bikes and groups in menu
        }
        mReceiver = new RideReceiver();
        mBikeId = getIntent().getIntExtra(Utils.BIKES, -1);
        mLockButton = (ImageButton) findViewById(R.id.lock_button);
        mStopButton = (Button) findViewById(R.id.stop_riding);
        mDurationText = (Chronometer) findViewById(R.id.duration_chronometer);
        mCurSpeedText = (TextView) findViewById(R.id.max_speed_text);
        mElevLossText = (TextView) findViewById(R.id.eleve_loss_text);
        mElevGainText = (TextView) findViewById(R.id.eleve_gain_text);
        mMaxSpeedText = (TextView) findViewById(R.id.current_speed_text);
        mAvgSpeedText = (TextView) findViewById(R.id.avg_speed_text);
        mDistanceText = (TextView) findViewById(R.id.distance_text);
        mElevationText = (TextView) findViewById(R.id.elevation_text);
        mStartPauseButton = (Button) findViewById(R.id.start_riding);
        mGpsStatusView = (TextView) findViewById(R.id.gpsStatus);
        mStartPauseButton.setEnabled(false);
        mStopButton.setEnabled(false);
        mLockButton.setEnabled(true);
        mLockButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                setButtonState();
            }
        });
        mStartPauseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                startPauseRide();
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                Intent stopIntent = new Intent(RideActivity.this, RideTracker.class);
                stopIntent.putExtra(Utils.STOP_SERVICE, true);
                startService(stopIntent);
            }
        });
        setButtonState();
        startTrackingRideThroughService();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mReceiver, new IntentFilter(Utils.SERVICE_BROADCAST));
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }


    private void startTrackingRideThroughService()
    {
        if(permissionsGranted())
        {
            Intent startIntent = new Intent(RideActivity.this, RideTracker.class);
            startIntent.putExtra(Utils.BIKES, mBikeId);
            startIntent.putExtra(Utils.START_SERVICE, true);
            startService(startIntent);
            mDurationText.setBase(SystemClock.elapsedRealtime() + mTimeWhenPaused);
            mDurationText.start();
        }
    }


    private boolean permissionsGranted()
    {
        boolean response = true;
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            response = false;
            if(shouldProvideRationale)
            {
                Log.i(TAG, "Displaying permission rationale to provide additional context.");
                Snackbar.make(findViewById(R.id.lock_button), "location_rationale",
                              Snackbar.LENGTH_INDEFINITE)
                        .setAction("okay", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view){requestLocationPermissions();}
                        })
                        .show();
            }else
            {
                requestLocationPermissions();
            }
        }
        return response;
    }


    private void requestLocationPermissions()
    {
        ActivityCompat.requestPermissions(RideActivity.this,
                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                          REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults)
    {
        Log.i(TAG, "onRequestPermissionResult");
        if(requestCode == REQUEST_CODE)
        {
            if(grantResults.length <= 0)
            {
                Log.i(TAG, "User interaction was cancelled.");
            }else if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                startTrackingRideThroughService();
            }else
            {
                Snackbar.make(
                        findViewById(R.id.lock_button),
                        "location_denied_explanation",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("settings", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }


    private void setButtonState()
    {
        if(!mLockButton.isActivated())
        {
            mLockButton.setImageDrawable(getDrawable(R.drawable.ic_lock_outline_black_24dp));
            mLockButton.setBackgroundColor(Color.GRAY);
            mLockButton.setActivated(true);
            mStartPauseButton.setBackgroundColor(Color.BLACK);
            mStopButton.setBackgroundColor(Color.BLACK);
            mStartPauseButton.setEnabled(false);
            mStartPauseButton.setEnabled(false);
        }else
        {
            mLockButton.setImageDrawable(getDrawable(R.drawable.ic_lock_open_black_24dp));
            mLockButton.setBackgroundColor(Color.GRAY);
            mLockButton.setActivated(false);
            mStartPauseButton.setEnabled(true);
            mStopButton.setEnabled(true);
            mStopButton.setBackgroundColor(Color.RED);
            mStopButton.setTextColor(Color.BLACK);
            mStartPauseButton.setTextColor(Color.BLACK);
            String resume = "Resume";
            if(resume.equals(mStartPauseButton.getText()))
            {
                mStartPauseButton.setBackgroundColor(Color.GREEN);
            }else
            {
                mStartPauseButton.setBackgroundColor(Color.YELLOW);
            }
        }
    }


    private void startPauseRide()
    {
        String resume = "Resume";
        if(resume.equals(mStartPauseButton.getText()))
        {
            startTrackingRideThroughService();
            mStartPauseButton.setText("pause");
            mStartPauseButton.setBackgroundColor(Color.YELLOW);
        }else
        {
            mTimeWhenPaused = mDurationText.getBase() - SystemClock.elapsedRealtime();
            mDurationText.stop();
            Intent pauseIntent = new Intent(RideActivity.this, RideTracker.class);
            pauseIntent.putExtra(Utils.PAUSE_SERVICE, true);
            startService(pauseIntent);
            mStartPauseButton.setText(resume);
            mStartPauseButton.setBackgroundColor(Color.GREEN);
        }
    }


    @SuppressLint ( "DefaultLocale" )
    private class RideReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            if(!mIsServiceCreated)
            {
                mIsServiceCreated = intent.getBooleanExtra(Utils.SERVICE_CREATED, false);
            }
            float accuracy = intent.getFloatExtra(Utils.LOCATION_ACCURACY, 0);
            if(accuracy < 0)
            {
                mGpsStatusView.setTextColor(Color.RED);
            }else if(Utils.MIN_ACCURACY < accuracy)
            {
                mGpsStatusView.setTextColor(Color.YELLOW);
            }else if(Utils.MIN_ACCURACY >= accuracy && accuracy > 0)
            {
                mGpsStatusView.setTextColor(Color.GREEN);
            }
            Ride ride = intent.getParcelableExtra(Utils.CURRENT_RIDE);
            if(ride != null)
            {
                float currentSpeed = intent.getFloatExtra(Utils.CURRENT_SPEED, 0);
                Double currentElevation = intent.getDoubleExtra(Utils.CURRENT_ELEVATION, 0);
                mCurSpeedText.setText(String.format("%.1f", currentSpeed));
                mElevationText.setText(String.format("%.1f", currentElevation));
                mDistanceText.setText(String.format("%.1f", ride.getDistance()));
                mMaxSpeedText.setText(String.format("%.1f", ride.getMaxSpeed()));
                mAvgSpeedText.setText(String.format("%.1f", ride.getAvgSpeed()));
                mElevGainText.setText(String.format("%.1f", ride.getElevGain()));
                mElevLossText.setText(String.format("%.1f", ride.getElevLoss()));
            }
        }
    }
}
