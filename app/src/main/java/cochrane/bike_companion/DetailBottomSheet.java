package cochrane.bike_companion;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import cochrane.bike_companion.Model.Bike;
import cochrane.bike_companion.Model.Group;
import cochrane.bike_companion.Utilities.DatabaseHandler;
import cochrane.bike_companion.Utilities.Utils;


/**
 * Created by Clint on 12/04/2017.
 */
public class DetailBottomSheet extends BottomSheetDialogFragment
{
    private String mTypeOfObject;
    private int mItemId;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback()
            {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState)
                {
                    if(newState == BottomSheetBehavior.STATE_HIDDEN)
                    {
                        dismiss();
                    }
                }


                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset){}
            };


    public static DetailBottomSheet newInstance(String typeOfObject, int itemId)
    {
        DetailBottomSheet bottomSheetFragment = new DetailBottomSheet();
        Bundle args = new Bundle();
        args.putString(Utils.OBJECT, typeOfObject);
        args.putInt("itemId", itemId);
        bottomSheetFragment.setArguments(args);
        return bottomSheetFragment;
    }


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mItemId = getArguments().getInt("itemId");
        mTypeOfObject = getArguments().getString(Utils.OBJECT);
    }


    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        DatabaseHandler db = new DatabaseHandler(getContext());
        View contentView = null;
        switch(mTypeOfObject)
        {
        case Utils.BIKES:
            final Bike bike = db.getBike(mItemId);
            db.CloseConnection();
            db.close();
            contentView = setupBikeDialog(bike);
            break;
        case Utils.GROUPS:
            final Group group = db.getGroup(mItemId);
            db.CloseConnection();
            db.close();
            contentView = setUpGroupDialog(group);
        }
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if(behavior != null && behavior instanceof BottomSheetBehavior)
        {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }


    private View setUpGroupDialog(final Group group)
    {

        View contentView = View.inflate(getContext(), R.layout.group_bottom_sheet, null);
        TextView idleAlertRule = (TextView) contentView.findViewById(R.id.idle_alert_rule);
        TextView moveAlertRule = (TextView) contentView.findViewById(R.id.move_alert_rule);
        ImageView activate = (ImageView) contentView.findViewById(R.id.activate);
        TextView contact = (TextView) contentView.findViewById(R.id.contact);
        TextView name = (TextView) contentView.findViewById(R.id.name);
        TextView edit = (TextView) contentView.findViewById(R.id.edit);
        final TextView menu = (TextView) contentView.findViewById(R.id.menu);

        idleAlertRule.setText(group.getAlertIdleRule());
        moveAlertRule.setText(group.getAlertMoveRule());
        name.setText(group.getName());
        contact.setText("Contact Count: " + group.getContactCount());
        if(group.isActive())
        {
            activate.setImageResource(R.drawable.ic_group_minus_24px);
        }else
        {
            activate.setImageResource(R.drawable.ic_group_add_black_24dp);
        }
        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v)
            {
                Intent intent = new Intent(getContext(),EditGroupActivity.class);
                intent.putExtra(Utils.GROUPS,group);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v)
            {
                final PopupMenu popupMenu = new PopupMenu(getContext(), menu);
                popupMenu.inflate(R.menu.bike_bottom_sheet_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item)
                    {
                        switch(item.getItemId())
                        {
                        case R.id.view_rides:
                            //// TODO: 13/04/2017 add intent
                            break;
                        case R.id.view_components:
                            // TODO: 13/04/2017 add components
                        }
                        return true;
                    }
                });
            }
        });
        return contentView;

    }


    private View setupBikeDialog(final Bike bike)
    {
        View contentView = View.inflate(getContext(), R.layout.bike_bottom_sheet, null);
        TextView name = (TextView) contentView.findViewById(R.id.name);
        TextView year = (TextView) contentView.findViewById(R.id.year);
        TextView make = (TextView) contentView.findViewById(R.id.make);
        TextView edit = (TextView) contentView.findViewById(R.id.edit);
        TextView ride = (TextView) contentView.findViewById(R.id.ride);
        final TextView menu = (TextView) contentView.findViewById(R.id.menu);
        TextView model = (TextView) contentView.findViewById(R.id.model);
        TextView distance = (TextView) contentView.findViewById(R.id.duration);
        TextView description = (TextView) contentView.findViewById(R.id.description);
        name.setText(bike.getName());
        year.setText(bike.getYear());
        make.setText(bike.getMake());
        model.setText(bike.getModel());
        distance.setText(bike.getDistance() + "");
        description.setText(bike.getDescription());
        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                Intent intent = new Intent(getContext(), EditBikeActivity.class);
                intent.putExtra(Utils.BIKES, bike);
                startActivity(intent);
            }
        });
        ride.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
//                Intent intent = new Intent(getContext(), RideActivity.class);
//                intent.putExtra(Utils.BIKES, bike);
//                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                final PopupMenu popupMenu = new PopupMenu(getContext(), menu);
                popupMenu.inflate(R.menu.bike_bottom_sheet_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item)
                    {
                        switch(item.getItemId())
                        {
                        case R.id.view_rides:
                            //// TODO: 13/04/2017 add intent
                            break;
                        case R.id.view_components:
                            // TODO: 13/04/2017 add components
                        }
                        return true;
                    }
                });
            }
        });
        return contentView;
    }
}