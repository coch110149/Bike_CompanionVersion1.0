package cochrane.bike_companion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import cochrane.bike_companion.Model.Bike;
import cochrane.bike_companion.Utilities.DatabaseHandler;
import cochrane.bike_companion.Utilities.Utils;


public class EditBikeActivity extends AppCompatActivity
{
    private EditText mNameText;
    private EditText mMakeText;
    private EditText mModelText;
    private EditText mYearText;
    private EditText mDescriptionText;
    private EditText mDistanceText;
    private Bike mBike = new Bike();
    private DatabaseHandler mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_edit);
        if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        final Boolean isEdit = getIntent().hasExtra(Utils.BIKES);
        if(isEdit)
        {
            mBike = getIntent().getParcelableExtra(Utils.BIKES);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }
        mNameText = (EditText) findViewById(R.id.name);
        mNameText.setText(mBike.getName());
        mMakeText = (EditText) findViewById(R.id.make);
        mMakeText.setText(mBike.getMake());
        mYearText = (EditText) findViewById(R.id.year);
        mYearText.setText(mBike.getYear());
        mModelText = (EditText) findViewById(R.id.model);
        mModelText.setText(mBike.getModel());
        mDistanceText = (EditText) findViewById(R.id.duration);
        if(mBike.getDistance() > 0){mDistanceText.setText(mBike.getDistance() + "");}
        mDescriptionText = (EditText) findViewById(R.id.description);
        mDescriptionText.setText(mBike.getDescription());
    }


    @Override
    protected void onStart()
    {
        mDb = new DatabaseHandler(this);
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }


    @Override
    protected void onStop()
    {
        mDb.close();
        super.onStop();
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
        // TODO: 12/04/2017 error check if there is nothing entered do not save 
        boolean shouldSave = true;
        mBike.setName(mNameText.getText().toString());
        mBike.setMake(mMakeText.getText().toString());
        mBike.setYear(mYearText.getText().toString());
        mBike.setModel(mModelText.getText().toString());
        mBike.setDescription(mDescriptionText.getText().toString());
        if("".equals(mDistanceText.getText().toString().trim()))
        {
            mBike.setDistance(0);
        }else
        {
            mBike.setDistance(Double.parseDouble(mDistanceText.getText().toString()));

        }

        mDb.addBike(mBike);
        finish();
    }


    private void showConformation()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard Changes?")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}
