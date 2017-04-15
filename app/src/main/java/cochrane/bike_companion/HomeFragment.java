package cochrane.bike_companion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cochrane.bike_companion.Model.Bike;
import cochrane.bike_companion.Model.Group;
import cochrane.bike_companion.Utilities.DatabaseHandler;
import cochrane.bike_companion.Utilities.Utils;


public class HomeFragment extends Fragment
{
    private TextView mActivatedGroups, mBikeName, mDistance;
    private Button mSwapBikes, mManageGroups, mTestNotifications;
    private FloatingActionButton mFab;
    private DatabaseHandler mDb;
    private Bike mBike;


    public HomeFragment()
    {
        // Required empty public constructor
    }


    public static HomeFragment newInstance()
    {
        return new HomeFragment();
    }


    public int getBikeId()
    {
        return (mBike != null) ? mBike.getId(): -1;

    }


    @Override
    public void onStart()
    {
        super.onStart();
        mDb = new DatabaseHandler(getContext());
        setBikeInformationCard();
        setGroupInformationCard();
    }


    @Override
    public void onStop()
    {
        super.onStop();
        mDb.close();
        mDb.CloseConnection();
    }





    private void setGroupInformationCard()
    {
        String activatedGroupNames = "No riding buddies have been found, add one now";
        mManageGroups.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if(mDb.getGroupCount() > 1)
                {
                    swapGroups();
                }else
                {
                    startActivity(new Intent(getContext(), EditGroupActivity.class));
                    // TODO: 14/04/2017 automatically add to card a flag might work
                }
            }
        });
        if(mDb.getGroupCount() > 0)
        {
            // TODO: 13/04/2017 put in separate thread
            final ArrayList<Group> activatedGroups = mDb.getActivatedGroups();
            if(activatedGroups != null && !activatedGroups.isEmpty())
            {

                for(final Group group : activatedGroups)
                {
                    activatedGroupNames += group.getName() + " ";
                }
            }else
            {
                activatedGroupNames = "No groups have been selected. Click Manage Groups to " +
                                      "select some";
            }
        }
        mActivatedGroups.setText(activatedGroupNames);
    }


    private void swapGroups()
    {
        final Cursor cursor = mDb.getAllGroupsCursor();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select your riding buddies")
                .setMultiChoiceItems(cursor, DatabaseHandler.KEY_GROUP_IS_ACTIVATED,
                                     DatabaseHandler.KEY_GROUP_NAME,
                                     new DialogInterface.OnMultiChoiceClickListener()
                                     {
                                         @Override
                                         public void onClick(final DialogInterface dialog,
                                                             final int which,
                                                             final boolean isChecked)
                                         {
                                             cursor.moveToPosition(which);
                                             mDb.swapGroupActivation(cursor.getInt(0), isChecked);
                                         }
                                     })
                .setPositiveButton("Finished", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        setGroupInformationCard();
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }


    /**
     * sets up button. If there is less than one bike in the database, the button should allow the
     * user to add a new bike. If there is more than one then the button behaviour will change to
     * selecting which bike to ride
     * Upon first loading the fragment, we will pick the last ridden bike (or last added bike) the
     * user is free to ride that bike or switch the bike. If there are no bikes in the garage at all
     * tell the user that.
     */
    private void setBikeInformationCard()
    {

        if(mDb.getBikeCount() > 1)
        {
            mSwapBikes.setText("Swap Bikes");
            mSwapBikes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    swapBikes();
                }
            });
        }else
        {
            mSwapBikes.setText("Add Bike");
            mSwapBikes.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    startActivity(new Intent(getContext(), EditBikeActivity.class));
                }
            });
        }
        if(mDb.getBikeCount() > 0)
        {
            if(mBike == null)
            {
                mBike = mDb.getMostRecentlyRiddenBike();
            }
            mBikeName.setText(mBike.getName());
            mDistance.setVisibility(View.VISIBLE);
            mDistance.setText(mBike.getDistance() + "");
        }else
        {
            mBikeName.setText("No Bikes Were Found in your garage, add one now");
            mDistance.setVisibility(View.GONE);
        }
    }


    /**
     * Create a single choice dialog to allow users to change out which bike he/she
     * is going to ride. Retrieves a cursor from the database and upon selection will dismiss
     * the dialog and apply the changes to the card.
     */
    private void swapBikes()
    {
        final Cursor cursor = mDb.getAllBikesCursor();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("swap bikes")
                .setSingleChoiceItems(cursor, (mBike.getId())-1, "Name",
                                      new DialogInterface.OnClickListener()
                                      {
                                          @Override
                                          public void onClick(final DialogInterface dialog,
                                                              final int which)
                                          {
                                              cursor.moveToPosition(which);
                                              mBike = mDb.getBike(cursor.getInt(0));
                                              setBikeInformationCard();
                                              dialog.dismiss();
                                          }
                                      });
        builder.create().show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mDistance = (TextView) view.findViewById(R.id.duration);
        mBikeName = (TextView) view.findViewById(R.id.bike_name);
        mSwapBikes = (Button) view.findViewById(R.id.swap_bikes);
        mManageGroups = (Button) view.findViewById(R.id.manage_groups);
        mActivatedGroups = (TextView) view.findViewById(R.id.activated_groups);
        mTestNotifications = (Button) view.findViewById(R.id.test_notifications);
        mFab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        mFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v)
            {
                Intent intent = new  Intent(getContext(),RideActivity.class);
                if(mBike != null)
                {
                    intent.putExtra(Utils.BIKES,mBike.getId());
                }
                startActivity(intent);
            }
        });
        return view;
    }
}
