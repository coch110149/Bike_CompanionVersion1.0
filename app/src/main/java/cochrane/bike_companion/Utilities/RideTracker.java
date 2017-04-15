package cochrane.bike_companion.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Chronometer;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

import cochrane.bike_companion.EditRideActivity;
import cochrane.bike_companion.Model.Ride;
import cochrane.bike_companion.R;
import cochrane.bike_companion.RideActivity;


public class RideTracker extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
                LocationListener
{
    private static final int NOTIFICATION_ID = 4158370;
    private static final long UPDATE_INTERVAL_MS = 5000;
    private static final long FASTEST_INTERVAL_MS = UPDATE_INTERVAL_MS / 2;
    private static final String TAG = RideTracker.class.getSimpleName();
    private NotificationManager mNotificationManager;
    private int mNumberOfLocationUpdates = 0;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private float mSpeedSum = 0;
    private Handler mHandler;
    private Ride mRide;
    private Chronometer mRideTime;


    public RideTracker(){}


    @Nullable
    @Override
    public IBinder onBind(final Intent intent)
    {
        return null;
    }


    @Override
    public void onCreate()
    {
        Log.d(TAG, "Service OnCreate");
        mRide = new Ride();
        buildGoogleApiClient();
        createLocationUpdates();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(NOTIFICATION_ID, getNotification(false));
    }


    private synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                   .addConnectionCallbacks(this)
                                   .addApi(LocationServices.API)
                                   .addOnConnectionFailedListener(this)
                                   .build();
        mGoogleApiClient.connect();
    }


    private void createLocationUpdates()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_MS);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL_MS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId)
    {
        Log.d(TAG, "Service OnStartCommand");
        /*
        set up when onStartCommand Comes through
            send activity Started through the listener to prevent it from being init multiple
            times through the lifecycle
        on Pause command will pause the service (will come through notification and activity)
        on Stop command will kill the service (will come through notification and activity)
        on resume command will come through after a pause (will come through notification and
        activity)
         */
        mRide.setBikeId(intent.getIntExtra(Utils.BIKES,-1));
        boolean pauseService = intent.getBooleanExtra(Utils.PAUSE_SERVICE, false);
        boolean startService = intent.getBooleanExtra(Utils.START_SERVICE, false);
        boolean stopService = intent.getBooleanExtra(Utils.STOP_SERVICE, false);
        if(startService)
        {
            startRide();
        }else if(pauseService)
        {
            pauseRide();
        }else if(stopService)
        {
            stopRide();
        }
        return START_STICKY;
    }


    private void startRide()
    {
        try
        {
            if(mGoogleApiClient.isConnected())
            {
                mCurrentLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, RideTracker.this);
                mNotificationManager.notify(NOTIFICATION_ID, getNotification(false));
                Intent intent = new Intent(Utils.SERVICE_RUNNING);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }catch(SecurityException noPermissions)
        {
            //// TODO: 08/04/2017 implement this to request the permissions again
            Log.e(TAG, "lost permission " + noPermissions);
        }
    }


    private void pauseRide()
    {
//        mRide.setDuration(mRideTime.toString());
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, RideTracker.this);
        mNotificationManager.notify(NOTIFICATION_ID, getNotification(true));
        Intent intent = new Intent(Utils.SERVICE_PAUSED);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }


    private void stopRide()
    {
        pauseRide();
        mGoogleApiClient.disconnect();
        mRide.setRideDate(new Date().toString());
        mHandler.removeCallbacksAndMessages(null);
        stopForeground(true);
        // TODO: 13/04/2017 inflate ride summary
        stopSelf();
        Intent intent = new Intent (getBaseContext(), EditRideActivity.class);
        intent.putExtra(Utils.RIDES,mRide);
        startActivity(intent);
    }


    @Override
    public void onConnected(@Nullable final Bundle bundle)
    {
        startRide();
    }


    @Override
    public void onConnectionSuspended(final int i)
    {
        // TODO: 08/04/2017 what needs to occur here
        Log.e(TAG, "GoogleApiClient connection suspended.");
    }


    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult)
    {
        // TODO: 08/04/2017 what needs to occur here
        Log.e(TAG, "GoogleApiClient connection failed.");
    }


    @Override
    public void onLocationChanged(final Location location)
    {

        Intent intent = new Intent(Utils.SERVICE_BROADCAST);
        intent.putExtra(Utils.LOCATION_ACCURACY, location.getAccuracy());
        if((location.getAccuracy() <= Utils.MIN_ACCURACY) && (location.getAccuracy() > 0.0))
        {
            final Location mPreviousLocation = mCurrentLocation;
            final float currentSpeed, distanceCovered;
            mCurrentLocation = location;
            float elapsedTime = mCurrentLocation.getElapsedRealtimeNanos()
                                - mPreviousLocation.getElapsedRealtimeNanos();
            elapsedTime /= 1000000000.0;
            distanceCovered = mCurrentLocation.distanceTo(mPreviousLocation) / 1000;
            mRide.setDistance(mRide.getDistance() + distanceCovered);
            currentSpeed = calculateSpeed(distanceCovered * 1000, elapsedTime);
            calculateElevationChange(mPreviousLocation);

            intent.putExtra(Utils.CURRENT_RIDE, mRide);
            intent.putExtra(Utils.CURRENT_SPEED, currentSpeed);
            intent.putExtra(Utils.CURRENT_ELEVATION, mCurrentLocation.getAltitude());
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }


    private float calculateSpeed(final float distanceCovered, final float elapsedTime)
    {
        float currentSpeed = 18.0f / 5.0f;
        if(mCurrentLocation.hasSpeed())
        {
            currentSpeed *= mCurrentLocation.getSpeed();
            if(mRide.getMaxSpeed() < currentSpeed)
            {
                mRide.setMaxSpeed(currentSpeed);
            }
        }else
        {
            currentSpeed *= distanceCovered / elapsedTime;
        }
        mSpeedSum += currentSpeed;
        mNumberOfLocationUpdates += 1;
        mRide.setAvgSpeed(mSpeedSum / mNumberOfLocationUpdates);
        return currentSpeed;
    }


    private void calculateElevationChange(final Location previousLocation)
    {
        final double eleChange;
        if((mCurrentLocation.getAltitude() != 0) && (previousLocation.getAltitude() != 0))
        {
            if(mCurrentLocation.getAltitude() > previousLocation.getAltitude())
            {
                eleChange = mCurrentLocation.getAltitude() - previousLocation.getAltitude();
                mRide.setElevGain(mRide.getElevGain() + eleChange);
            }else if(mCurrentLocation.getAltitude() < previousLocation.getAltitude())
            {
                eleChange = previousLocation.getAltitude() - mCurrentLocation.getAltitude();
                mRide.setElevLoss(mRide.getElevLoss() + eleChange);
            }
        }
    }


    private Notification getNotification(boolean isPaused)
    {
        Log.d(TAG, "Service getNotifications");

        Intent activityIntent = new Intent(this, RideActivity.class);
        activityIntent.putExtra(Utils.FROM_NOTIFICATION, true);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(
                this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent(this, RideTracker.class);
        stopIntent.putExtra(Utils.STOP_SERVICE, true);
        PendingIntent stopPendingIntent = PendingIntent.getService(
                this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action stopAction =
                new NotificationCompat.Action(R.drawable.ic_stop_black_24dp, "stop",
                                              stopPendingIntent);

        Intent pauseResumeIntent = new Intent(this, RideTracker.class);
        NotificationCompat.Action pauseResumeAction;
        if(!isPaused)
        {
            pauseResumeIntent.putExtra(Utils.PAUSE_SERVICE, true);
            PendingIntent pausePendingIntent = PendingIntent.getService(
                    this, 0, pauseResumeIntent, 0);
            pauseResumeAction = new NotificationCompat.Action(R.drawable.ic_pause_black_24dp,
                                                              Utils.PAUSE_SERVICE,
                                                              pausePendingIntent);
        }else
        {
            pauseResumeIntent.putExtra(Utils.START_SERVICE, true);
            PendingIntent resumePendingIntent = PendingIntent.getService(
                    this, 0, pauseResumeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pauseResumeAction = new NotificationCompat.Action(R.drawable.ic_pause_black_24dp,
                                                              Utils.START_SERVICE,
                                                              resumePendingIntent);
        }
        return new NotificationCompat.Builder(this)
                       .addAction(stopAction)
                       .addAction(pauseResumeAction)
                       .setContentIntent(activityPendingIntent)
                       .setContentText(getString(R.string.app_name))
                       .setContentText(mRide.getDistance() + "\n" + mRide.getAvgSpeed())
                       .setUsesChronometer(true)
                       .setOngoing(true)
                       .setPriority(Notification.PRIORITY_HIGH)
                       .setSmallIcon(R.mipmap.ic_launcher).build();
    }
}