package com.sd.ibook;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import CustomAdapter.CustomerAdapter;
import Helper.Constants;
import Model.InfoModelClass;
import SQLiteDatabase.SQLiteDbHelper;


public class AllMembers_Activity extends ActionBarActivity {

    ListView lvAllMembers;
    SQLiteDbHelper sqLiteDbHelper;
    EditText search;
    CustomerAdapter arrayAdapter;

    private List<InfoModelClass> infoModelClassNameWithImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_members_);
        sqLiteDbHelper=new SQLiteDbHelper(this);

        //get phone numbers from sim card
        if (sqLiteDbHelper.isEmpty()){
            Toast.makeText(getApplicationContext(),"Contacts", Toast.LENGTH_SHORT).show();

        }else {
            getContactsFromSimCard();
        }

        lvAllMembers=(ListView) findViewById(R.id.lvAllMembers);
        infoModelClassNameWithImage = sqLiteDbHelper.getAllContactNames();
        arrayAdapter=new CustomerAdapter(this,infoModelClassNameWithImage);
        lvAllMembers.setAdapter(arrayAdapter);
        
        lvAllMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InfoModelClass tempInfoModelClass=infoModelClassNameWithImage.get(position);
                String name = tempInfoModelClass.getName().toString();
                int friendsID = Integer.valueOf(tempInfoModelClass.getId());

                String[] details=sqLiteDbHelper.getMemberDetails(name,friendsID);

                Intent intent=new Intent(getApplicationContext(),MemberDetailsActivity.class);
                intent.putExtra("Details",details);
                startActivity(intent);
                
                
            }
        });

        search=(EditText)findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                arrayAdapter.filter(text);

            }
        });



    }

    private void getContactsFromSimCard() {
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection=new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Email.ADDRESS,

                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        };


        Cursor people=getContentResolver().query(uri, projection, null, null, null);

        int indexName=people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber=people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int indexEmail=people.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
        int indexImage=people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

        people.moveToFirst();
        do{

            InfoModelClass value=new InfoModelClass();

            String name=people.getString(indexName);
            String number=people.getString(indexNumber);
            String image_path=people.getString(indexImage);

            String email=people.getString(indexEmail);


            value.setName(name);
            value.setPhone(number);

            String data="is not available";
            value.setEmail(email);
            value.setBloodGroupId("1");
            value.setBloodGroup("is not available");
            value.setGenderId("1");

            value.setGender("is not available");
            value.setInstitute("is not available");

            value.setDepartment("is not available");
            value.setPresentAddress("is not available");

            value.setPermanentAddress("is not available");
            value.setBirthDay("is not available");

            value.setImage_path(image_path);

            sqLiteDbHelper.addInfoModel(value);

        }while (people.moveToNext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_members_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addNote) {
            Intent intent=new Intent(this,AddInfoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
