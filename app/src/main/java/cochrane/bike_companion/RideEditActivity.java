package cochrane.bike_companion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cochrane.bike_companion.Model.Ride;
import cochrane.bike_companion.Utilities.Utils;


public class RideEditActivity extends AppCompatActivity
{
    private Ride mRide;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_edit);
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        final Boolean isEdit = getIntent().hasExtra(Utils.RIDES);
        if(isEdit)
        {
            mRide = getIntent().getParcelableExtra(Utils.GROUPS);
        }
    }
}
