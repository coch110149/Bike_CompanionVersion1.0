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
import cochrane.bike_companion.Model.Group;
import cochrane.bike_companion.R;
import cochrane.bike_companion.Utilities.Utils;


/**
 * Created by Clint on 12/04/2017.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.Holder>
{
    private ArrayList<Group> mGroups;
    private FragmentManager mManager;


    public GroupAdapter(final ArrayList<?> dataset, final Context context)
    {
        //noinspection unchecked
        mGroups = (ArrayList<Group>) dataset;
        mManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }


    @Override
    public GroupAdapter.Holder onCreateViewHolder(final ViewGroup parent, final int viewType)
    {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_manage_group, parent, false);
        return new Holder(inflatedView);
    }


    @Override
    public void onBindViewHolder(final GroupAdapter.Holder holder, final int position)
    {
        final Group group = mGroups.get(position);
        holder.bind(group, mManager);
    }


    @Override
    public int getItemCount()
    {
        return mGroups.size();
    }

    public static class Holder extends RecyclerView.ViewHolder
    {
        private TextView mName;
        private TextView mAlertMoveRule;
        private TextView mAlertIdleRule;
        private TextView mContactCount;

        public Holder (final View itemView)
        {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mAlertIdleRule = (TextView) itemView.findViewById(R.id.alert_idle_rule);
            mAlertMoveRule = (TextView) itemView.findViewById(R.id.alert_move_rule);
            mContactCount = (TextView) itemView.findViewById(R.id.contact_count);
        }

        public void bind(final Group group, final FragmentManager manager)
        {
            mName.setText(group.getName());
            // TODO: 12/04/2017 get the propper string
            mAlertMoveRule.setText(group.getAlertMoveRule());
            mAlertIdleRule.setText(group.getAlertIdleRule());
           mContactCount.setText(group.getContactCount() + "");
            View parent = (View) mName.getParent();
            parent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v)
                {
                    final DetailBottomSheet sheet = DetailBottomSheet.newInstance(Utils.GROUPS,
                                                                                  group.getId());
                    sheet.show(manager,sheet.getTag());
                }
            });

        }
    }
}
