package SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Model.InfoModelClass;

/**
 * Created by Sajib Chandra Das on 6/10/2015.
 */
public class SQLiteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="ibook.db";
    private static final String DATABASE_TABLE="ibook_table";

    private static final String IBOOK_ID="ID";
    private static final String IBOOK_NAME="NAME";
    private static final String IBOOK_PHONE="PHONE";
    private static final String IBOOK_EMAIL="EMAIL";
    private static final String IBOOK_BLOOD_GROOP_ID="BLOOD_GROOP_ID";
    private static final String IBOOK_BLOOD_GROOP="BLOOD_GROOP";
    private static final String IBOOK_GENDER_ID="GENDER_ID";
    private static final String IBOOK_GENDER="GENDER";
    private static final String IBOOK_INSTITUTE="INSTITUTE";
    private static final String IBOOK_DEPARTMENT="DEPARTMENT";
    private static final String IBOOK_PRESENT_ADDRESS="PRESENT_ADDRESS";
    private static final String IBOOK_PERMANENT_ADDRESS="PERMANENT_ADDRESS";
    private static final String IBOOK_FF_BIRTHDAY="BIRTHDAY";
    private static final String IBOOK_IMAGE_PATH = "IMAGE_PATH";

    private static final String TAG = "ColumnSize";

    public SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + DATABASE_TABLE + "(" +
                IBOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IBOOK_NAME + " TEXT, " +
                IBOOK_PHONE + " TEXT, " +
                IBOOK_EMAIL + " TEXT, " +
                IBOOK_BLOOD_GROOP_ID + " TEXT, " +
                IBOOK_BLOOD_GROOP + " TEXT, " +
                IBOOK_GENDER_ID + " TEXT, " +
                IBOOK_GENDER + " TEXT, " +
                IBOOK_INSTITUTE + " TEXT, " +
                IBOOK_DEPARTMENT + " TEXT, " +
                IBOOK_PRESENT_ADDRESS + " TEXT, " +
                IBOOK_PERMANENT_ADDRESS + " TEXT, " +
                IBOOK_FF_BIRTHDAY + " TEXT, " +
                IBOOK_IMAGE_PATH + " TEXT " +
                "); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
        db.close();

    }

    public void addInfoModel(InfoModelClass infoModelClass){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(IBOOK_NAME,infoModelClass.getName());
        contentValues.put(IBOOK_PHONE,infoModelClass.getPhone());
        contentValues.put(IBOOK_EMAIL,infoModelClass.getEmail());
        contentValues.put(IBOOK_BLOOD_GROOP_ID,infoModelClass.getBloodGroupId());
        contentValues.put(IBOOK_BLOOD_GROOP,infoModelClass.getBloodGroup());
        contentValues.put(IBOOK_GENDER_ID,infoModelClass.getGenderId());
        contentValues.put(IBOOK_GENDER,infoModelClass.getGender());
        contentValues.put(IBOOK_INSTITUTE,infoModelClass.getInstitute());
        contentValues.put(IBOOK_DEPARTMENT,infoModelClass.getDepartment());
        contentValues.put(IBOOK_PRESENT_ADDRESS,infoModelClass.getPresentAddress());
        contentValues.put(IBOOK_PERMANENT_ADDRESS,infoModelClass.getPermanentAddress());
        contentValues.put(IBOOK_FF_BIRTHDAY,infoModelClass.getBirthDay());
        contentValues.put(IBOOK_IMAGE_PATH,infoModelClass.getImage_path());

        db.insert(DATABASE_TABLE, null, contentValues);
        db.close();
    }

    public long insertContact(InfoModelClass values){

        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(IBOOK_NAME,values.getName());
        contentValues.put(IBOOK_PHONE,values.getPhone());

        long ret=db.insert(DATABASE_TABLE,null,contentValues);
        return ret;

    }

    public void updateInfoModel(InfoModelClass infoModelClass, String id){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(IBOOK_NAME,infoModelClass.getName());
        contentValues.put(IBOOK_PHONE,infoModelClass.getPhone());
        contentValues.put(IBOOK_EMAIL,infoModelClass.getEmail());
        contentValues.put(IBOOK_BLOOD_GROOP_ID,infoModelClass.getBloodGroupId());
        contentValues.put(IBOOK_BLOOD_GROOP,infoModelClass.getBloodGroup());
        contentValues.put(IBOOK_GENDER_ID,infoModelClass.getGenderId());
        contentValues.put(IBOOK_GENDER,infoModelClass.getGender());
        contentValues.put(IBOOK_INSTITUTE,infoModelClass.getInstitute());
        contentValues.put(IBOOK_DEPARTMENT,infoModelClass.getDepartment());
        contentValues.put(IBOOK_PRESENT_ADDRESS,infoModelClass.getPresentAddress());
        contentValues.put(IBOOK_PERMANENT_ADDRESS,infoModelClass.getPermanentAddress());
        contentValues.put(IBOOK_FF_BIRTHDAY,infoModelClass.getBirthDay());
        contentValues.put(IBOOK_IMAGE_PATH,infoModelClass.getImage_path());

        db.update(DATABASE_TABLE, contentValues, IBOOK_ID + "=" + id, null);
        db.close();

    }

    public String[] getMemberDetails(String memberName, int id){
        String[] memberDetail=new String[12];
        SQLiteDatabase db=getWritableDatabase();
        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " +
                IBOOK_ID + "=\"" + id + "\" AND " + IBOOK_NAME + "=\"" + memberName + "\";";
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if (cursor.getCount()==1){
            if (cursor.getString(cursor.getColumnIndex(IBOOK_NAME))!=null &&
                    cursor.getString(cursor.getColumnIndex(IBOOK_ID))!=null){


                memberDetail[0]=cursor.getString(cursor.getColumnIndex(IBOOK_ID));
                memberDetail[1]=cursor.getString(cursor.getColumnIndex(IBOOK_NAME));
                memberDetail[2]=cursor.getString(cursor.getColumnIndex(IBOOK_PHONE));
                memberDetail[3]=cursor.getString(cursor.getColumnIndex(IBOOK_EMAIL));
                memberDetail[4]=cursor.getString(cursor.getColumnIndex(IBOOK_BLOOD_GROOP));
                memberDetail[5]=cursor.getString(cursor.getColumnIndex(IBOOK_GENDER));
                memberDetail[6]=cursor.getString(cursor.getColumnIndex(IBOOK_INSTITUTE));
                memberDetail[7]=cursor.getString(cursor.getColumnIndex(IBOOK_DEPARTMENT));
                memberDetail[8]=cursor.getString(cursor.getColumnIndex(IBOOK_PRESENT_ADDRESS));
                memberDetail[9]=cursor.getString(cursor.getColumnIndex(IBOOK_PERMANENT_ADDRESS));
                memberDetail[10]=cursor.getString(cursor.getColumnIndex(IBOOK_FF_BIRTHDAY));
                memberDetail[11]=cursor.getString(cursor.getColumnIndex(IBOOK_IMAGE_PATH));
            }
        }
        db.close();
        return memberDetail;
    }

    public String[] getMemberDetailsForEdit(String memberName, int id){
        String[] memberDetail=new String[12];
        SQLiteDatabase db=getWritableDatabase();
        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " +
                IBOOK_ID + "=\"" + id + "\" AND " + IBOOK_NAME + "=\"" + memberName + "\";";

        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();

        if (cursor.getCount()==1){
            if (cursor.getString(cursor.getColumnIndex(IBOOK_NAME))!=null &&
                    cursor.getString(cursor.getColumnIndex(IBOOK_ID))!=null){


                memberDetail[0]=cursor.getString(cursor.getColumnIndex(IBOOK_ID));
                memberDetail[1]=cursor.getString(cursor.getColumnIndex(IBOOK_NAME));
                memberDetail[2]=cursor.getString(cursor.getColumnIndex(IBOOK_PHONE));
                memberDetail[3]=cursor.getString(cursor.getColumnIndex(IBOOK_EMAIL));
                memberDetail[4]=cursor.getString(cursor.getColumnIndex(IBOOK_BLOOD_GROOP_ID));
                memberDetail[5]=cursor.getString(cursor.getColumnIndex(IBOOK_GENDER_ID));
                memberDetail[6]=cursor.getString(cursor.getColumnIndex(IBOOK_INSTITUTE));
                memberDetail[7]=cursor.getString(cursor.getColumnIndex(IBOOK_DEPARTMENT));
                memberDetail[8]=cursor.getString(cursor.getColumnIndex(IBOOK_PRESENT_ADDRESS));
                memberDetail[9]=cursor.getString(cursor.getColumnIndex(IBOOK_PERMANENT_ADDRESS));
                memberDetail[10]=cursor.getString(cursor.getColumnIndex(IBOOK_FF_BIRTHDAY));
                memberDetail[11]=cursor.getString(cursor.getColumnIndex(IBOOK_IMAGE_PATH));
            }
        }
        db.close();
        return memberDetail;
    }

    public void deleteInfoModel(String memberName, int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + DATABASE_TABLE + " WHERE " +
                IBOOK_ID + "=\"" + id + "\" AND " + IBOOK_NAME + "=\"" + memberName + "\";";
        db.execSQL(query);
        db.close();
    }


    public boolean isEmpty(){

        String query="SELECT "+IBOOK_ID+" FROM "+DATABASE_TABLE +";";
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        int columnCount=cursor.getCount();
        if (columnCount>=1){
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }

    }


    public List<InfoModelClass> getAllContactNames(){

        List<InfoModelClass> infoModelClassArrayList=new ArrayList<InfoModelClass>();
        SQLiteDatabase db=getReadableDatabase();


        // Ordering by Name
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, IBOOK_NAME+" ASC");
        cursor.moveToFirst();

        for (int i=0;i<cursor.getCount();i++){

            int id=cursor.getInt(cursor.getColumnIndex(IBOOK_ID));
            String name=cursor.getString(cursor.getColumnIndex(IBOOK_NAME));
            String image_path=cursor.getString(cursor.getColumnIndex(IBOOK_IMAGE_PATH));

            InfoModelClass contactHolder=new InfoModelClass(id,name,image_path);
            infoModelClassArrayList.add(contactHolder);
            cursor.moveToNext();
        }
        return infoModelClassArrayList;

    }
}
