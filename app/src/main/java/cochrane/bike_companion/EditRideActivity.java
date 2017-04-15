package cochrane.bike_companion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cochrane.bike_companion.Model.Ride;
import cochrane.bike_companion.Utilities.DatabaseHandler;
import cochrane.bike_companion.Utilities.Utils;


public class EditRideActivity extends AppCompatActivity
{
    private EditText mRideDate, mAvgSpeed, mMaxSpeed, mElevGain, mElevLoss, mDistance, mDuration;
    private TextView mBikeText;
    private DatabaseHandler mDb;
    private Ride mRide;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ride);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final boolean isEdit = getIntent().hasExtra(Utils.RIDES);
        if(isEdit)
        {
            mRide = getIntent().getParcelableExtra(Utils.RIDES);
        }else
        {
            mRide = new Ride();
        }
        mRideDate = (EditText) findViewById(R.id.ride_date);
        mRideDate.setText(mRide.getRideDate());
        mDuration = (EditText) findViewById(R.id.duration);
        mDuration.setText(mRide.getDuration());
        mAvgSpeed = (EditText) findViewById(R.id.avg_speed);
        mAvgSpeed.setText(mRide.getAvgSpeed() + "");
        mMaxSpeed = (EditText) findViewById(R.id.max_speed);
        mMaxSpeed.setText(mRide.getMaxSpeed() + "");
        mElevGain = (EditText) findViewById(R.id.elev_gain);
        mElevGain.setText(mRide.getElevGain() + "");
        mElevLoss = (EditText) findViewById(R.id.elev_loss);
        mElevLoss.setText(mRide.getElevLoss() + "");
        mDistance = (EditText) findViewById(R.id.distance);
        mDistance.setText(mRide.getDistance() + "");
        mBikeText = (TextView) findViewById(R.id.bikes);
        mBikeText.setText(mRide.getBikeName());
        mBikeText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                //// TODO: 14/04/2017 add bike
            }
        });
    }


    @Override
    protected void onResume()
    {
        mDb = new DatabaseHandler(this);
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id)
        {
        case android.R.id.home:
            showConformation();
            break;
        case R.id.action_save:
            save();
        }
        return super.onOptionsItemSelected(item);
    }


    private void save()
    {
        mRide.setRideDate(mRideDate.getText().toString());
        mRide.setAvgSpeed(Double.parseDouble(mAvgSpeed.getText().toString()));
        mRide.setMaxSpeed(Double.parseDouble(mMaxSpeed.getText().toString()));
        mRide.setElevGain(Double.parseDouble(mElevGain.getText().toString()));
        mRide.setElevLoss(Double.parseDouble(mElevLoss.getText().toString()));
        mRide.setDistance(Double.parseDouble(mDistance.getText().toString()));
        mDb.addRide(mRide);
        finished();
    }


    private void showConformation()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard Changes?")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        finished();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }


    private void finished()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Utils.RIDES, true);
        startActivity(intent);
    }
}
