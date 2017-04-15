package cochrane.bike_companion.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cochrane.bike_companion.Model.Ride;
import cochrane.bike_companion.R;


/**
 * Created by Clint on 12/04/2017.
 */
public class RideAdapter extends RecyclerView.Adapter<RideAdapter.Holder>
{
    private ArrayList<Ride> mRides;


    public RideAdapter(final ArrayList<?> dataset, final Context context)
    {
        //noinspection unchecked
        mRides = (ArrayList<Ride>) dataset;
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_manage_ride, parent, false);
        return new Holder(inflatedView);
    }


    @Override
    public void onBindViewHolder(final Holder holder, final int position)
    {
        final Ride ride = mRides.get(position);
        holder.bind(ride);
    }


    @Override
    public int getItemCount()
    {
        return mRides.size();
    }


    public static class Holder extends RecyclerView.ViewHolder
    {
        private TextView mDate;
        private TextView mDistance;
        private TextView mDuration;
        private TextView mBikeName;


        public Holder(final View itemView)
        {
            super(itemView);
            mDate = (TextView) itemView.findViewById(R.id.ride_date);
            mDistance = (TextView) itemView.findViewById(R.id.duration);
            mDuration = (TextView) itemView.findViewById(R.id.duration);
            mBikeName = (TextView) itemView.findViewById(R.id.name);
        }


        public void bind(final Ride ride)
        {
            mDate.setText(ride.getRideDate());
            mDistance.setText(String.valueOf(ride.getDistance()));
            mDuration.setText(ride.getDuration());
//            mBikeName.setText(ride.getBikeName());
        }
    }
}
