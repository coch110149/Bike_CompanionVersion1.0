package cochrane.bike_companion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cochrane.bike_companion.Adapters.BikeAdapter;
import cochrane.bike_companion.Adapters.GroupAdapter;
import cochrane.bike_companion.Adapters.RideAdapter;
import cochrane.bike_companion.Utilities.DatabaseHandler;
import cochrane.bike_companion.Utilities.Utils;


public class ManagementFragment extends Fragment
{
    private static final String TAG = "ManagementFrag";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mObjectType;


    public ManagementFragment()
    {
        // Required empty public constructor
    }


    public static ManagementFragment newInstance(String objectType)
    {
        ManagementFragment fragment = new ManagementFragment();
        Bundle args = new Bundle();
        args.putString(Utils.OBJECT, objectType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            mObjectType = getArguments().getString(Utils.OBJECT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View rootView = inflater.inflate(R.layout.fragment_management, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        rootView.setTag(TAG);
        return rootView;
    }


    @Override
    public void onResume()
    {
        super.onResume();
        DatabaseHandler db = new DatabaseHandler(getContext());
        ArrayList<?> dataSet = db.getObjects(mObjectType);
        switch(mObjectType)
        {
        case Utils.RIDES:
            mAdapter = new RideAdapter(dataSet, getContext());
            break;
        case Utils.GROUPS:
            mAdapter = new GroupAdapter(dataSet, getContext());
            break;
        case Utils.BIKES:
            mAdapter = new BikeAdapter(dataSet, getContext());
        }
        mRecyclerView.setAdapter(mAdapter);
    }
}
