package cochrane.bike_companion.Model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Clint on 12/04/2017.
 */
public class Bike implements Parcelable
{
    static final Creator<Bike> CREATOR = new Creator<Bike>()
    {
        @Override
        public Bike createFromParcel(Parcel in)
        {
            return new Bike(in);
        }


        @Override
        public Bike[] newArray(int size)
        {
            return new Bike[size];
        }
    };
    private int mId;
    private String mName;
    private String mMake;
    private String mYear;
    private String mModel;
    private double mDistance;
    private String mDescription;
    private String mLastRideDate;


    public Bike(final int id, final String name, final String make, final String year,
                final String model, final double distance,
                final String description, final String lastRideDate)
    {
        mId = id;
        mName = name;
        mMake = make;
        mYear = year;
        mModel = model;
        mDistance = distance;
        mDescription = description;
        mLastRideDate = lastRideDate;
    }


    public Bike()
    {
        this(-1,"","","","",0,"","");
    }


    public Bike(Parcel in)
    {
        mId = in.readInt();
        mName = in.readString();
        mMake = in.readString();
        mYear = in.readString();
        mModel = in.readString();
        mDistance = in.readDouble();
        mDescription = in.readString();
        mLastRideDate = in.readString();
    }


    public int getId()
    {
        return mId;
    }


    public void setId(final int id)
    {
        mId = id;
    }


    public String getName()
    {
        return mName;
    }


    public void setName(final String name)
    {
        mName = name;
    }


    public String getMake()
    {
        return mMake;
    }


    public void setMake(final String make)
    {
        mMake = make;
    }


    public String getYear()
    {
        return mYear;
    }


    public void setYear(final String year)
    {
        mYear = year;
    }


    public String getModel()
    {
        return mModel;
    }


    public void setModel(final String model)
    {
        mModel = model;
    }


    public double getDistance()
    {
        return mDistance;
    }


    public void setDistance(final double distance)
    {
        mDistance = distance;
    }


    public String getDescription()
    {
        return mDescription;
    }


    public void setDescription(final String description)
    {
        mDescription = description;
    }


    public String getLastRideDate()
    {
        return mLastRideDate;
    }


    public void setLastRideDate(final String lastRideDate)
    {
        mLastRideDate = lastRideDate;
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
        dest.writeString(mName);
        dest.writeString(mMake);
        dest.writeString(mYear);
        dest.writeString(mModel);
        dest.writeDouble(mDistance);
        dest.writeString(mDescription);
        dest.writeString(mLastRideDate);
    }
}
