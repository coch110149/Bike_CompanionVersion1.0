package cochrane.bike_companion.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cochrane.bike_companion.Model.Bike;
import cochrane.bike_companion.Model.Group;
import cochrane.bike_companion.Model.Ride;


/**
 * Created by Clint on 12/04/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rideManager.mDb";
    //Rides Table
    private static final String TABLE_RIDES = "rides";
    private static final String KEY_RIDE_ID = "_id";
    private static final String KEY_BIKE_USED_ID = "Bike_Used_Id";
    private static final String KEY_AVG_SPEED = "Average_Speed";
    private static final String KEY_MAX_SPEED = "Max_Speed";
    private static final String KEY_DURATION = "Ride_Duration";
    private static final String KEY_DISTANCE = "Ride_Distance";
    private static final String KEY_ELEV_LOSS = "Elevation_Loss";
    private static final String KEY_ELEV_GAIN = "Elevation_Gain";
    private static final String KEY_RIDE_DATE = "Ride_Date";
    //Bikes Table
    private static final String TABLE_BIKES = "bikes";
    private static final String KEY_BIKE_ID = "_id";
    private static final String KEY_BIKE_NAME = "Name";
    private static final String KEY_BIKE_MAKE = "Make";
    private static final String KEY_BIKE_YEAR = "Year";
    private static final String KEY_BIKE_MODEL = "Model";
    private static final String KEY_BIKE_DESCRIPTION = "Description";
    private static final String KEY_BIKE_TOTAL_DISTANCE = "Total_Distance";
    private static final String KEY_BIKE_LAST_RIDE_DATE = "Last_Ride_Date";
    //Components Table
    private static final String TABLE_COMPONENTS = "components";
    private static final String KEY_COMPONENT_ID = "_id";
    private static final String KEY_COMPONENT_YEAR = "Year";
    private static final String KEY_COMPONENT_TYPE = "Type";
    private static final String KEY_COMPONENT_MAKE = "Make";
    private static final String KEY_COMPONENT_MODEL = "Model";
    private static final String KEY_COMPONENT_PART_NUMBER = "Part_Number";
    private static final String KEY_COMPONENT_TOTAL_DISTANCE = "Total_Distance";
    private static final String KEY_COMPONENT_DATE_INSTALLED = "Date_Installed";
    private static final String KEY_COMPONENT_BIKE_INSTALLED = "Bike_Installed";
    private static final String KEY_COMPONENT_LAST_DATE_INSPECTED = "Last_Inspected_Date";
    //Group Table
    private static final String TABLE_GROUPS = "groups";
    private static final String KEY_GROUP_ID = "_id";
    public static final String KEY_GROUP_NAME = "Group_Name";
    private static final String KEY_CONTACT_COUNT = "contact_count";
    private static final String KEY_ALERT_IDLE_INTERVAL = "Stop_Periodic_Delay";
    private static final String KEY_MOVEMENT_WAIT_TIME = "Movement_Wait_Time";
    private static final String KEY_ALERT_MOVE_INTERVAL = "Periodic_Delay";
    private static final String KEY_PAUSE_BUTTON_STOPS_SERVICE = "Pause_Button_Stops_Service";
    public static final String KEY_GROUP_IS_ACTIVATED = "Is_Group_Activated";
    //Contact Table
    private static final String TABLE_CONTACT = "contacts";
    private static final String KEY_CONTACT_ID = "_id";
    private static final String KEY_CONTACT_NAME = "Contact_Name";
    private static final String KEY_CONTACT_PHONE = "Phone_Number";
    //Group + Contact Relation Table
    private static final String RELATION_CONTACT_GROUP = "Group_Contact_Table";
    private static final String FOREIGN_KEY_CONTACT_ID = "contact_id";
    private static final String FOREIGN_KEY_GROUP_ID = "group_id";
    private SQLiteDatabase mDb;


    public DatabaseHandler(final Context context)
    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDb = getWritableDatabase();
    }


    public void CloseConnection()
    {
        mDb.close();
    }


    @Override
    public void onCreate(final SQLiteDatabase db)
    {
        db.execSQL("PRAGMA foreign_keys=ON");
        final String CREATE_BIKE_TABLE = "CREATE TABLE " + TABLE_BIKES + " ("
                                         + KEY_BIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                         + KEY_BIKE_NAME + " TEXT, "
                                         + KEY_BIKE_MAKE + " TEXT, "
                                         + KEY_BIKE_YEAR + " TEXT, "
                                         + KEY_BIKE_MODEL + " TEXT, "
                                         + KEY_BIKE_DESCRIPTION + " TEXT, "
                                         + KEY_BIKE_TOTAL_DISTANCE + " TEXT, "
                                         + KEY_BIKE_LAST_RIDE_DATE + " TEXT);";
        db.execSQL(CREATE_BIKE_TABLE);
        final String CREATE_RIDE_TABLE = "CREATE TABLE " + TABLE_RIDES + " ("
                                         + KEY_RIDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                         + KEY_BIKE_USED_ID + " TEXT, "
                                         + KEY_AVG_SPEED + " TEXT, "
                                         + KEY_MAX_SPEED + " TEXT, "
                                         + KEY_DISTANCE + " TEXT, "
                                         + KEY_ELEV_GAIN + " TEXT, "
                                         + KEY_ELEV_LOSS + " TEXT, "
                                         + KEY_DURATION + " TEXT, "
                                         + KEY_RIDE_DATE + " TEXT, "
                                         + "FOREIGN KEY (" + KEY_BIKE_USED_ID + ") "
                                         + "REFERENCES " + TABLE_BIKES
                                         + "(" + KEY_BIKE_ID + ")); ";
        db.execSQL(CREATE_RIDE_TABLE);
        final String CREATE_COMPONENT = "CREATE TABLE " + TABLE_COMPONENTS + " ("
                                        + KEY_COMPONENT_ID + " INTEGER PRIMARY KEY " +
                                        "AUTOINCREMENT, "
                                        + KEY_COMPONENT_YEAR + " TEXT, "
                                        + KEY_COMPONENT_TYPE + " TEXT, "
                                        + KEY_COMPONENT_MAKE + " TEXT, "
                                        + KEY_COMPONENT_MODEL + " TEXT, "
                                        + KEY_COMPONENT_PART_NUMBER + " TEXT, "
                                        + KEY_COMPONENT_TOTAL_DISTANCE + " TEXT, "
                                        + KEY_COMPONENT_DATE_INSTALLED + " TEXT, "
                                        + KEY_COMPONENT_BIKE_INSTALLED + "TEXT, "
                                        + KEY_COMPONENT_LAST_DATE_INSPECTED + " TEXT);";
        db.execSQL(CREATE_COMPONENT);
        final String CREATE_GROUP_TABLE = "CREATE TABLE " + TABLE_GROUPS + " ("
                                          + KEY_GROUP_ID +
                                          " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                          + KEY_GROUP_NAME + " TEXT, "
                                          + KEY_CONTACT_COUNT + " TEXT, "
                                          + KEY_ALERT_MOVE_INTERVAL + " TEXT, "
                                          + KEY_MOVEMENT_WAIT_TIME + " TEXT, "
                                          + KEY_ALERT_IDLE_INTERVAL + " TEXT, "
                                          + KEY_PAUSE_BUTTON_STOPS_SERVICE + " BOOLEAN, "
                                          + KEY_GROUP_IS_ACTIVATED + " BOOLEAN);";
        db.execSQL(CREATE_GROUP_TABLE);
        final String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACT + " ("
                                            + KEY_CONTACT_ID
                                            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + KEY_CONTACT_NAME + " TEXT, "
                                            + KEY_CONTACT_PHONE + " TEXT); ";
        db.execSQL(CREATE_CONTACT_TABLE);
        final String CREATE_GROUP_CONTACT_RELATION = "CREATE TABLE " + RELATION_CONTACT_GROUP + " ("
                                                     + KEY_CONTACT_ID
                                                     + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                     + FOREIGN_KEY_CONTACT_ID + " TEXT, "
                                                     + FOREIGN_KEY_GROUP_ID + " TEXT, "
                                                     + "FOREIGN KEY ("
                                                     + FOREIGN_KEY_CONTACT_ID + ") "
                                                     + "REFERENCES " + TABLE_CONTACT
                                                     + "(" + KEY_CONTACT_ID + "), "
                                                     + " FOREIGN KEY" + " ("
                                                     + FOREIGN_KEY_GROUP_ID + ")"
                                                     + " REFERENCES " + TABLE_GROUPS
                                                     + "(" + KEY_GROUP_ID + "));";
        db.execSQL(CREATE_GROUP_CONTACT_RELATION);
    }


    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIKES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + RELATION_CONTACT_GROUP);
        onCreate(db);
    }


    public void addRide(final Ride ride)
    {
        final ContentValues values = new ContentValues();
        values.put(KEY_DURATION, ride.getDuration());
        values.put(KEY_DISTANCE, ride.getDistance());
        values.put(KEY_AVG_SPEED, ride.getAvgSpeed());
        values.put(KEY_MAX_SPEED, ride.getMaxSpeed());
        values.put(KEY_ELEV_LOSS, ride.getElevLoss());
        values.put(KEY_ELEV_GAIN, ride.getElevGain());
        values.put(KEY_RIDE_DATE, ride.getRideDate());
        values.put(KEY_BIKE_USED_ID, ride.getBikeId());
        if(getRide(ride.getId()) == null)
        {
            mDb.insert(TABLE_RIDES, null, values);
        }else
        {
            mDb.update(TABLE_RIDES, values, KEY_RIDE_ID + "=?",
                       new String[]{String.valueOf(ride.getId())});
        }

        if(ride.getBikeId() > 0)
        {
            addBike(getBike(ride.getBikeId()));
        }
    }


    public Ride getRide(final int id)
    {
        final String selectQuery =
                "SELECT * FROM " + TABLE_RIDES + " WHERE " + KEY_RIDE_ID + " = " +
                String.valueOf(id);
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        Ride ride = null;
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                ride = new Ride(cursor.getInt(0),    //id
                                cursor.getInt(1),    //bike id
                                cursor.getDouble(2), //avgSpeed
                                cursor.getDouble(3), //MaxSpeed
                                cursor.getDouble(4), //Distance
                                cursor.getDouble(5), //EleGain
                                cursor.getDouble(6), //EleLoss
                                cursor.getString(7), //Duration
                                cursor.getString(8));//Date
            }
            cursor.close();
        }
        return ride;
    }


    public void deleteRide(final Ride ride)
    {
        mDb.delete(TABLE_RIDES, KEY_RIDE_ID + "=?", new String[]{String.valueOf(ride.getId())});
    }


    public ArrayList<Ride> getAllRides()
    {
        final ArrayList<Ride> rideArrayList = new ArrayList<>();
        final String selectQuery =
                "SELECT * FROM " + TABLE_RIDES + " ORDER BY " + KEY_RIDE_DATE + " COLLATE NOCASE;";
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        if(cursor.moveToFirst())
        {
            do
            {
                final Ride ride = new Ride(cursor.getInt(0),       //id
                                           cursor.getInt(1),       //bike id
                                           cursor.getDouble(2),    //avgSpeed
                                           cursor.getDouble(3),    //MaxSpeed
                                           cursor.getDouble(4),    //Distance
                                           cursor.getDouble(5),    //EleGain
                                           cursor.getDouble(6),    //EleLoss
                                           cursor.getString(7),    //Duration
                                           cursor.getString(8));   //Date
                rideArrayList.add(ride);
            } while(cursor.moveToNext());
            cursor.close();
        }
        return rideArrayList;
    }


    public void addBike(final Bike bike)
    {
        final ContentValues values = new ContentValues();
        values.put(KEY_BIKE_NAME, bike.getName());
        values.put(KEY_BIKE_MAKE, bike.getMake());
        values.put(KEY_BIKE_YEAR, bike.getYear());
        values.put(KEY_BIKE_MODEL, bike.getModel());
        values.put(KEY_BIKE_DESCRIPTION, bike.getDescription());
        values.put(KEY_BIKE_LAST_RIDE_DATE, bike.getLastRideDate());
        values.put(KEY_BIKE_TOTAL_DISTANCE, bike.getDistance());

        if(getBike(bike.getId()) == null)
        {
            mDb.insert(TABLE_BIKES, null, values);
        }else
        {
            mDb.update(TABLE_BIKES, values, KEY_BIKE_ID + " =?",
                       new String[]{String.valueOf(bike.getId())});
        }
    }


    public Bike getBike(final int id)
    {
        final String selectQuery =
                "SELECT * FROM " + TABLE_BIKES + " WHERE " + KEY_BIKE_ID + " = " +
                String.valueOf(id);
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        Bike bike = null;
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                bike = new Bike(cursor.getInt(0),    //id
                                cursor.getString(1), //name
                                cursor.getString(2), //make
                                cursor.getString(3), //year
                                cursor.getString(4), //model
                                cursor.getDouble(6), //Distance
                                cursor.getString(5), //Description
                                cursor.getString(7)); //Last Ride
            }
            cursor.close();
        }
        return bike;
    }


    public void deleteBike(final Bike bike)
    {
        removeBikeFromRide(bike.getId());
        mDb.delete(TABLE_BIKES, KEY_BIKE_ID + "=?", new String[]{String.valueOf(bike.getId())});
    }


    public ArrayList<Bike> getAllBikes()
    {
        final ArrayList<Bike> bikeArrayList = new ArrayList<>();
        final String selectQuery =
                "SELECT * FROM " + TABLE_BIKES + " ORDER BY " + KEY_BIKE_NAME + " COLLATE NOCASE;";
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        if(cursor.moveToFirst())
        {
            do
            {
                final Bike bike = new Bike(cursor.getInt(0),    //id
                                           cursor.getString(1), //name
                                           cursor.getString(2), //make
                                           cursor.getString(3), //year
                                           cursor.getString(4), //model
                                           cursor.getDouble(6), //Distance
                                           cursor.getString(5), //Description
                                           cursor.getString(7)); //Last Ride
                bikeArrayList.add(bike);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return bikeArrayList;
    }


    private void removeBikeFromRide(final int id)
    {
        final String selectQuery = "SELECT * FROM " + TABLE_RIDES +
                                   " WHERE " + KEY_BIKE_USED_ID + " = " + id;
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        {
            if(cursor.moveToFirst())
            {
                do
                {
                    addRide(new Ride(cursor.getInt(0),       //id
                                     -1,       //bike id //
                                     cursor.getDouble(2),    //avgSpeed
                                     cursor.getDouble(3),    //MaxSpeed
                                     cursor.getDouble(4),    //Distance
                                     cursor.getDouble(5),    //EleGain
                                     cursor.getDouble(6),    //EleLoss
                                     cursor.getString(7),    //Duration
                                     cursor.getString(8)));   //Date
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
    }


    public Bike getMostRecentlyRiddenBike()
    {
        int bikeId = 1;
        final String selectQuery = "SELECT " + KEY_BIKE_USED_ID + " FROM " + TABLE_RIDES +
                                   " ORDER BY " + KEY_RIDE_DATE + " DESC LIMIT 1";
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        if(cursor.moveToFirst())
        {
            bikeId = cursor.getInt(0);
        }
        cursor.close();
        return getBike(bikeId);
    }


    public void addGroup(final Group group)
    {
        final ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME, group.getName());
        values.put(KEY_GROUP_IS_ACTIVATED, group.isActive());
        values.put(KEY_MOVEMENT_WAIT_TIME, group.getMovementWaitTime());
        values.put(KEY_ALERT_IDLE_INTERVAL, group.getAlertIdleInterval());
        values.put(KEY_ALERT_MOVE_INTERVAL, group.getAlertMoveInterval());
        values.put(KEY_CONTACT_COUNT, getContactsInGroup(group));

        if(getGroup(group.getId()) == null)
        {
            mDb.insert(TABLE_GROUPS, null, values);
        }else
        {
            mDb.update(TABLE_GROUPS, values, KEY_GROUP_ID + " =?",
                       new String[]{String.valueOf(group.getId())});
        }
    }


    private int getContactsInGroup(final Group group)
    {
        String countQuery = "SELECT COUNT (*) FROM " + RELATION_CONTACT_GROUP + " WHERE " +
                            FOREIGN_KEY_GROUP_ID + "=" + group.getId();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }


    public Group getGroup(final int id)
    {
        final String selectQuery = "SELECT * FROM " + TABLE_GROUPS +
                                   " WHERE " + KEY_GROUP_ID + " = " + String.valueOf(id);
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        Group group = null;
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                group = new Group(cursor.getInt(0),                    //id
                                  cursor.getString(1),
                                  cursor.getInt(2),
                                  cursor.getInt(3),
                                  cursor.getInt(4),
                                  cursor.getInt(5),
                                  getBoolean(cursor.getInt(6)),
                                  getBoolean(cursor.getInt(7)));
            }
            cursor.close();
        }
        return group;
    }


    public void deleteGroup(final Group group)
    {
        mDb.delete(TABLE_GROUPS, KEY_GROUP_ID + "=?",
                   new String[]{String.valueOf(group.getId())});
        mDb.delete(RELATION_CONTACT_GROUP, FOREIGN_KEY_GROUP_ID + "=?",
                   new String[]{String.valueOf(group.getId())});
    }


    public ArrayList<Group> getAllGroups()
    {
        final ArrayList<Group> groupArrayList = new ArrayList<>();
        final String selectQuery =
                "SELECT * FROM " + TABLE_GROUPS + " ORDER BY " + KEY_GROUP_NAME +
                " COLLATE NOCASE;";
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        if(cursor.moveToFirst())
        {
            do
            {
                final Group group = new Group(cursor.getInt(0),                    //id
                                              cursor.getString(1),
                                              cursor.getInt(2),
                                              cursor.getInt(3),
                                              cursor.getInt(4),
                                              cursor.getInt(5),
                                              getBoolean(cursor.getInt(6)),
                                              getBoolean(cursor.getInt(7)));
                ; // TODO: 14/04/2017
                // column 6 is for pause button
                group.setContactCount(cursor.getInt(6));
                groupArrayList.add(group);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return groupArrayList;
    }


    public ArrayList<?> getObjects(final String typeOfRequest)
    {
        switch(typeOfRequest)
        {
        case Utils.BIKES:
            return getAllBikes();
        case Utils.GROUPS:
            return getAllGroups();
        case Utils.RIDES:
            return getAllRides();
        }
        return null;
    }


    private boolean getBoolean(final int value)
    {
        return value == 1;
    }


    public int getBikeCount()
    {
        final String countQuery = "SELECT * FROM " + TABLE_BIKES;
        return mDb.rawQuery(countQuery, null).getCount();
    }


    public ArrayList<Group> getActivatedGroups()
    {
        final ArrayList<Group> groupList = new ArrayList<>();
        final String selectQuery = "SELECT * FROM " + TABLE_GROUPS +
                                   " WHERE " + KEY_GROUP_IS_ACTIVATED + "=" + "1";
        final Cursor cursor = mDb.rawQuery(selectQuery, null);
        if(cursor.moveToFirst())
        {
            do
            {
                final Group group = new Group(cursor.getInt(0),                    //id
                                              cursor.getString(1),
                                              cursor.getInt(2),
                                              cursor.getInt(3),
                                              cursor.getInt(4),
                                              cursor.getInt(5),
                                              getBoolean(cursor.getInt(6)),
                                              getBoolean(cursor.getInt(7)));
                groupList.add(group);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return groupList;
    }


    public int getGroupCount()
    {
        final String countQuery = "SELECT * FROM " + TABLE_GROUPS;
        return mDb.rawQuery(countQuery, null).getCount();
    }


    public Cursor getAllBikesCursor()
    {
        final String selectQuery =
                "SELECT * FROM " + TABLE_BIKES + " ORDER BY " + KEY_BIKE_ID + " COLLATE NOCASE;";
        return mDb.rawQuery(selectQuery, null);
    }


    public Cursor getAllGroupsCursor()
    {
        final String selectQuery =
                "SELECT * FROM " + TABLE_GROUPS + " ORDER BY " + KEY_GROUP_NAME + " COLLATE NOCASE;";
        return mDb.rawQuery(selectQuery, null);
    }

    public void swapGroupActivation(final int id, boolean isChecked)
    {
        final ContentValues values = new ContentValues();
        values.put(KEY_GROUP_IS_ACTIVATED, isChecked);
        mDb.update(TABLE_GROUPS, values, KEY_GROUP_ID + " =?",
                   new String[]{String.valueOf(id)});
    }
}