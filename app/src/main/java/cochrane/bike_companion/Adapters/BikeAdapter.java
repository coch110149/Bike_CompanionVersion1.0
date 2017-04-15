package cochrane.bike_companion.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cochrane.bike_companion.DetailBottomSheet;
import cochrane.bike_companion.Model.Bike;
import cochrane.bike_companion.R;
import cochrane.bike_companion.Utilities.Utils;


/**
 * Created by Clint on 12/04/2017.
 */
public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.Holder>
{
    private ArrayList<Bike> mBikes;
    private FragmentManager mManager;


    public BikeAdapter(final ArrayList<?> dataset, final Context context)
    {

        mManager = ((AppCompatActivity)context).getSupportFragmentManager();
        //noinspection unchecked
        mBikes = (ArrayList<Bike>) dataset;
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_manage_bike, parent, false);
        return new Holder(inflatedView);
    }


    @Override
    public void onBindViewHolder(final Holder holder, final int position)
    {
        final Bike bike = mBikes.get(position);
        holder.bind(bike,mManager);
    }


    @Override
    public int getItemCount()
    {
        return mBikes.size();
    }


    public static class Holder extends RecyclerView.ViewHolder
    {
        private TextView mName;
        private TextView mDistance;
        private TextView mLastRideDate;
        // TODO: 12/04/2017 add alerts and icon


        public Holder(final View itemView)
        {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mDistance = (TextView) itemView.findViewById(R.id.duration);
            mLastRideDate = (TextView) itemView.findViewById(R.id.last_ride);
        }


        public void bind(final Bike bike, final FragmentManager manager)
        {
            mName.setText(bike.getName());
            mDistance.setText(String.valueOf(bike.getDistance()));
            mLastRideDate.setText(bike.getLastRideDate());
            View parent = (View) mName.getParent();
            parent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    final DetailBottomSheet sheet = DetailBottomSheet.newInstance(Utils.BIKES,
                                                                                  bike.getId());
                    sheet.show(manager,sheet.getTag());

                }
            });
        }



    }
}
