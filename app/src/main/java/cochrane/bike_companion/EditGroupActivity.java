package cochrane.bike_companion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import cochrane.bike_companion.Model.Group;
import cochrane.bike_companion.Utilities.DatabaseHandler;
import cochrane.bike_companion.Utilities.Utils;


public class EditGroupActivity extends AppCompatActivity implements
        Switch.OnCheckedChangeListener, View.OnClickListener
{
    private EditText mName, mAlertMoveInterval, mAlertIdleInterval, mMovementWaitTime;
    private Switch mPeriodicSwitch, mStoppedSwitch;
    private Group mGroup ;
    private DatabaseHandler mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        mName = (EditText) findViewById(R.id.name);
        mAlertMoveInterval = (EditText) findViewById(R.id.alert_move_interval);
        mAlertIdleInterval = (EditText) findViewById(R.id.alert_idle_interval);
        mMovementWaitTime = (EditText) findViewById(R.id.movement_wait_time);
        TextView periodicAlerts = (TextView) findViewById(R.id.periodic_alerts);
        TextView stoppedAlerts = (TextView) findViewById(R.id.stopped_alerts);
        periodicAlerts.setOnClickListener(this);
        stoppedAlerts.setOnClickListener(this);
        mPeriodicSwitch = (Switch) findViewById(R.id.periodic_switch);
        mStoppedSwitch = (Switch) findViewById(R.id.stopped_switch);
        mPeriodicSwitch.setOnCheckedChangeListener(this);
        mStoppedSwitch.setOnCheckedChangeListener(this);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final Boolean isEdit = getIntent().hasExtra(Utils.GROUPS);
        if(isEdit)
        {
            mGroup = getIntent().getParcelableExtra(Utils.GROUPS);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }else
        {
            mGroup = new Group();
        }
        setValues();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        mDb = new DatabaseHandler(this);
    }


    @Override
    protected void onPause()
    {
        mDb.close();
        mDb.CloseConnection();
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        getMenuInflater().inflate(R.menu.group_edit, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        switch(item.getItemId())
        {
        case android.R.id.home:
            showConformation();
            break;
        case R.id.action_save:
            save();
            break;
        case R.id.action_delete:
            delete();
            break;
        case R.id.action_manage_contacts:
            manageContacts();
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void manageContacts()
    {
        //// TODO: 14/04/2017 actually do this
        Toast.makeText(this,"I'm working on it",Toast.LENGTH_SHORT).show();
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
                        finish();
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


    private void delete()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete")
                .setPositiveButton("Yup", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        mDb.deleteGroup(mGroup);
                        finish();
                    }
                })
                .setNegativeButton("nah fam", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }


    private void save()
    {
        if(idleAlertIsAcceptable() && moveAlertIsAcceptable() && moveDelayIsAcceptable())
        {
            mGroup.setName(mName.getText().toString());
            mDb.addGroup(mGroup);
            finish();
        }
    }


    private boolean moveAlertIsAcceptable()
    {
        boolean output = true;
        String text = null;
        try
        {
            text = mAlertMoveInterval.getText().toString();
            final int intInput = "".equals(text) ? 0 : Integer.parseInt(text);
            mGroup.setAlertMoveInterval(intInput, mPeriodicSwitch.isChecked());
        }catch(final NumberFormatException _ex)
        {
            mAlertMoveInterval.setError("Please enter a whole number");
            Log.e(Utils.TAG, "AlertMoveIntervalIsNotAcceptable" + text + _ex.getMessage());
            output = false;
        }
        return output;
    }


    private boolean moveDelayIsAcceptable()
    {
        boolean output = true;
        String text = null;
        try
        {
            text = mMovementWaitTime.getText().toString();
            final int intInput = "".equals(text) ? 0 : Integer.parseInt(text);
            mGroup.setMovementWaitTime(intInput, mStoppedSwitch.isChecked());
        }catch(final NumberFormatException _ex)
        {
            mMovementWaitTime.setError("Please enter a whole number");
            Log.e(Utils.TAG, "moveWaitTimeIsNotAcceptable" + text + _ex.getMessage());
            output = false;
        }
        return output;
    }


    private boolean idleAlertIsAcceptable()
    {
        boolean output = true;
        String text = null;
        try
        {
            text = mAlertIdleInterval.getText().toString();
            final int intInput = "".equals(text) ? 0 : Integer.parseInt(text);
            mGroup.setAlertIdleInterval(intInput, mStoppedSwitch.isChecked());
        }catch(final NumberFormatException _ex)
        {
            mAlertIdleInterval.setError("Please enter a whole number");
            Log.e(Utils.TAG, "IdleAlertIsNotAcceptable" + text + _ex.getMessage());
            output = false;
        }
        return output;
    }


    private void setValues()
    {
        mName.setText(mGroup.getName());
        mPeriodicSwitch.setChecked(mGroup.getAlertMoveInterval() > 0);
        mStoppedSwitch.setChecked(
                mGroup.getMovementWaitTime() > 0 || mGroup.getAlertIdleInterval() > 0);
        String text;
        text = (mGroup.getAlertMoveInterval() > 0) ? mGroup.getAlertMoveInterval() +"" : "";
        mAlertMoveInterval.setText(text);
        text = (mGroup.getAlertIdleInterval() > 0) ? mGroup.getAlertIdleInterval() +"" : "";
        mAlertIdleInterval.setText(text);
        text = (mGroup.getMovementWaitTime() > 0) ? mGroup.getMovementWaitTime() +"" : "";
        mMovementWaitTime.setText(text);
    }


    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
    {
        switch(buttonView.getId())
        {
        case R.id.periodic_switch:
            mAlertMoveInterval.setEnabled(isChecked);
            break;
        case R.id.stopped_switch:
            mAlertIdleInterval.setEnabled(isChecked);
            mMovementWaitTime.setEnabled(isChecked);
            break;
        }
    }


    @Override
    public void onClick(final View v)
    {
        switch(v.getId())
        {
        case R.id.periodic_alerts:
            // TODO: 14/04/2017 show dialog of information
            break;
        case R.id.stopped_alerts:
            // TODO: 14/04/2017 show dialog of information
            break;
        }
    }
}
