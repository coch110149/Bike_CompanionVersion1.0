package cochrane.bike_companion.Model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Clint on 12/04/2017.
 */
public class Ride implements Parcelable
{
    public static final Creator<Ride> CREATOR = new Creator<Ride>()
    {
        @Override
        public Ride createFromParcel(Parcel in)
        {
            return new Ride(in);
        }


        @Override
        public Ride[] newArray(int size)
        {
            return new Ride[size];
        }
    };
    private int mId;
    private int mBikeId;
    private double mAvgSpeed;
    private double mMaxSpeed;
    private double mDistance;
    private double mElevLoss;
    private double mElevGain;
    private String mBikeName;
    private String mDuration;
    private String mRideDate;


    public Ride()
    {
    }


    public Ride(final int id, final int bikeId, final double avgSpeed, final double maxSpeed,
                final double distance, final double elevLoss,
                final double elevGain, final String duration,
                final String rideDate)
    {
        mId = id;
        mBikeId = bikeId;
        mAvgSpeed = avgSpeed;
        mMaxSpeed = maxSpeed;
        mDistance = distance;
        mElevLoss = elevLoss;
        mElevGain = elevGain;
        mDuration = duration;
        mRideDate = rideDate;
    }


    public Ride(Parcel in)
    {
        mId = in.readInt();
        mBikeId = in.readInt();
        mAvgSpeed = in.readDouble();
        mMaxSpeed = in.readDouble();
        mDistance = in.readDouble();
        mElevLoss = in.readDouble();
        mElevGain = in.readDouble();
        mBikeName = in.readString();
        mDuration = in.readString();
        mRideDate = in.readString();
    }


    public static Creator<Ride> getCREATOR()
    {
        return CREATOR;
    }


    public int getId()
    {
        return mId;
    }


    public void setId(final int id)
    {
        mId = id;
    }


    public int getBikeId()
    {
        return mBikeId;
    }


    public void setBikeId(final int bikeId)
    {
        mBikeId = bikeId;
    }


    public double getAvgSpeed()
    {
        return mAvgSpeed;
    }


    public void setAvgSpeed(final double avgSpeed)
    {
        mAvgSpeed = avgSpeed;
    }


    public double getMaxSpeed()
    {
        return mMaxSpeed;
    }


    public void setMaxSpeed(final double maxSpeed)
    {
        mMaxSpeed = maxSpeed;
    }


    public double getDistance()
    {
        return mDistance;
    }


    public void setDistance(final double distance)
    {
        mDistance = distance;
    }


    public double getElevLoss()
    {
        return mElevLoss;
    }


    public void setElevLoss(final double elevLoss)
    {
        mElevLoss = elevLoss;
    }


    public double getElevGain()
    {
        return mElevGain;
    }


    public void setElevGain(final double elevGain)
    {
        mElevGain = elevGain;
    }


    public String getBikeName()
    {
        return mBikeName;
    }


    public void setBikeName(final String bikeName)
    {
        mBikeName = bikeName;
    }


    public String getDuration()
    {
        return mDuration;
    }


    public void setDuration(final String duration)
    {
        mDuration = duration;
    }


    public String getRideDate()
    {
        return mRideDate;
    }


    public void setRideDate(final String rideDate)
    {
        mRideDate = rideDate;
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
        dest.writeInt(mBikeId);
        dest.writeDouble(mAvgSpeed);
        dest.writeDouble(mMaxSpeed);
        dest.writeDouble(mDistance);
        dest.writeDouble(mElevLoss);
        dest.writeDouble(mElevGain);
        dest.writeString(mBikeName);
        dest.writeString(mDuration);
        dest.writeString(mRideDate);
    }
}
