package cochrane.bike_companion.Model;

import android.os.Parcel;
import android.os.Parcelable;

import static java.lang.Math.abs;


/**
 * Created by Clint on 12/04/2017.
 */
public class Group implements Parcelable
{
    public static final Creator<Group> CREATOR = new Creator<Group>()
    {
        @Override
        public Group createFromParcel(Parcel in)
        {
            return new Group(in);
        }


        @Override
        public Group[] newArray(int size)
        {
            return new Group[size];
        }
    };
    private int mId;
    private int mContactCount;
    private int mMovementWaitTime;
    private int mAlertIdleInterval;
    private int mAlertMoveInterval;
    private boolean isActive;
    private String mName;
    private String mAlertMoveRule;
    private String mAlertIdleRule;
    private boolean mPauseControl;


    public Group(final int id, final String name, final int contactCount,
                 final int alertMoveInterval, final int movementWaitTime,
                 final int alertIdleInterval, final boolean pauseControl, final boolean isActive)
    {
        mId = id;
        mContactCount = contactCount;
        mMovementWaitTime = movementWaitTime;
        mAlertIdleInterval = alertIdleInterval;
        mAlertMoveInterval = alertMoveInterval;
        this.isActive = isActive;
        mName = name;
        mPauseControl = pauseControl;
    }


    public Group()
    {
        this(-1, "", 0, 0, 0, 0, false, false);
    }


    protected Group(Parcel in)
    {
        mId = in.readInt();
        mContactCount = in.readInt();
        mMovementWaitTime = in.readInt();
        mAlertIdleInterval = in.readInt();
        mAlertMoveInterval = in.readInt();
        isActive = in.readByte() != 0;
        mName = in.readString();
        mAlertMoveRule = in.readString();
        mAlertIdleRule = in.readString();
    }


    public int getId()
    {
        return mId;
    }


    public void setId(final int id)
    {
        mId = id;
    }


    public int getContactCount()
    {
        return mContactCount;
    }


    public void setContactCount(final int contactCount)
    {
        mContactCount = contactCount;
    }


    public int getMovementWaitTime()
    {
        return mMovementWaitTime;
    }


    public void setMovementWaitTime(final int movementWaitTime)
    {
        mMovementWaitTime = movementWaitTime;
    }


    public int getAlertIdleInterval()
    {
        return mAlertIdleInterval;
    }


    public void setAlertIdleInterval(final int alertIdleInterval)
    {
        mAlertIdleInterval = alertIdleInterval;
    }


    public int getAlertMoveInterval()
    {
        return mAlertMoveInterval;
    }


    public void setAlertMoveInterval(final int alertMoveInterval)
    {
        mAlertMoveInterval = alertMoveInterval;
    }


    public boolean isActive()
    {
        return isActive;
    }


    public void setActive(final boolean active)
    {
        isActive = active;
    }


    public String getName()
    {
        return mName;
    }


    public void setName(final String name)
    {
        mName = name;
    }


    public String getAlertMoveRule()
    {
        String output = "Periodic Alerts are Turned Off";
        if(mAlertMoveInterval > 0)
        {
            output = "Notify every" + mAlertMoveInterval + " min";
        }
        return output;
    }


    public String getAlertIdleRule()
    {

        String output = "Stopped Alerts are turned off";
        if((mAlertIdleInterval > 0) && (mMovementWaitTime > 0))
        {
            output = "Notify every " + mAlertIdleInterval + " min after waiting " +
                     (mMovementWaitTime + 2) + " min from detecting idleness";
        }
        return output;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }


    @Override
    public void writeToParcel(final Parcel dest, final int flags)
    {
        dest.writeInt(mId);
        dest.writeInt(mContactCount);
        dest.writeInt(mMovementWaitTime);
        dest.writeInt(mAlertIdleInterval);
        dest.writeInt(mAlertMoveInterval);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeString(mName);
        dest.writeString(mAlertMoveRule);
        dest.writeString(mAlertIdleRule);
    }


    public void setAlertMoveInterval(final int intInput, final boolean isChecked)
    {
        mAlertMoveInterval = isChecked ? abs(intInput) : abs(intInput) *-1;
    }


    public void setMovementWaitTime(final int intInput, final boolean checked)
    {
        mMovementWaitTime = checked ? abs(intInput) : abs(intInput) * -1;
    }


    public void setAlertIdleInterval(final int intInput, final boolean checked)
    {
        mAlertIdleInterval = checked ? abs(intInput) : abs(intInput) * -1;
    }
}
