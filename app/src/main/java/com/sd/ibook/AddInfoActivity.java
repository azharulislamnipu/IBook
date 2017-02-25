package com.sd.ibook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Helper.Constants;
import Helper.FileUtils;
import Model.InfoModelClass;
import SQLiteDatabase.SQLiteDbHelper;


public class AddInfoActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    EditText etName,etPhone,etEmail,etInstitute,etDepartment,etPresentAddress,etPermanentAddress;

    EditText etBirthDay;
    Calendar calendar;
    DatePickerDialog.OnDateSetListener dateSet;

    RadioGroup radioGroupGender;
    RadioButton radioButtonMale,radioButtonFemale;
    Spinner spinnerBloodGroup;

    String bloodGroupID,bloodGroup,genderID,gender;

    InfoModelClass infoModel;
    SQLiteDbHelper sqLiteDbHelper;

    ImageButton ibImage;
    //Image properties
    private String mCurrentImagePath = null;
    private Uri mCapturedImageURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        if (savedInstanceState != null) {

            // Get the saved Image uri string.
            String ImageUriString = savedInstanceState.getString(Constants.KEY_IMAGE_URI);

            // Restore the Image uri from the Image uri string.
            if (ImageUriString != null) {
                mCapturedImageURI = Uri.parse(ImageUriString);
            }
            mCurrentImagePath = savedInstanceState.getString(Constants.KEY_IMAGE_URI);
        }

        etName=(EditText)findViewById(R.id.etName);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etInstitute=(EditText)findViewById(R.id.etInstitute);
        etDepartment=(EditText)findViewById(R.id.etDepartment);
        etPresentAddress=(EditText)findViewById(R.id.etPresentAddress);
        etPermanentAddress=(EditText)findViewById(R.id.etPermanentAddress);

        etBirthDay =(EditText)findViewById(R.id.etBirthday);
        calendar=Calendar.getInstance();
        dateSet =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(Calendar.MONTH,monthOfYear);
                calendar.set(Calendar.YEAR,year);

                String dateFormat="dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateFormat, Locale.US);
                etBirthDay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        spinnerBloodGroup=(Spinner)findViewById(R.id.sBloodGroop);
        ArrayAdapter spinner=ArrayAdapter.createFromResource(this,R.array.BloodGroop,android.R.layout.simple_spinner_item);
        spinnerBloodGroup.setAdapter(spinner);
        spinnerBloodGroup.setOnItemSelectedListener(this);

        radioGroupGender=(RadioGroup)findViewById(R.id.rgMaleFemale);
        radioButtonMale=(RadioButton)findViewById(R.id.rbMale);
        radioButtonFemale=(RadioButton)findViewById(R.id.rbFemale);

        sqLiteDbHelper=new SQLiteDbHelper(this);

        //set image
        ibImage=(ImageButton)findViewById(R.id.ibImage);
        ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    private void chooseImage(){

        //We need the customer's name to to save the image file
        if (etName.getText().toString() != null && !etName.getText().toString().isEmpty()) {
            // Determine Uri of camera image to save.
            final File rootDir = new File(Constants.PICTURE_DIRECTORY);

            //noinspection ResultOfMethodCallIgnored
            rootDir.mkdirs();

            // Create the temporary file and get it's URI.

            //Get the customer name
            String name = etName.getText().toString();

            //Remove all white space in the customer name
            name.replaceAll("\\s+", "");

            //Use the customer name to create the file name of the image that will be captured
            File file = new File(rootDir, FileUtils.generateImageName(name));
            mCapturedImageURI = Uri.fromFile(file);

            // Initialize a list to hold any camera application intents.
            final List<Intent> cameraIntents = new ArrayList<Intent>();

            // Get the default camera capture intent.
            final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Get the package manager.
            final PackageManager packageManager = this.getPackageManager();

            // Ensure the package manager exists.
            if (packageManager != null) {

                // Get all available image capture app activities.
                final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

                // Create camera intents for all image capture app activities.
                for(ResolveInfo res : listCam) {

                    // Ensure the activity info exists.
                    if (res.activityInfo != null) {

                        // Get the activity's package name.
                        final String packageName = res.activityInfo.packageName;

                        // Create a new camera intent based on android's default capture intent.
                        final Intent intent = new Intent(captureIntent);

                        // Set the intent data for the current image capture app.
                        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        intent.setPackage(packageName);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                        // Add the intent to available camera intents.
                        cameraIntents.add(intent);
                    }
                }
            }

            // Create an intent to get pictures from the filesystem.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            // Chooser of filesystem options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

            // Add the camera options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

            // Start activity to choose or take a picture.
            startActivityForResult(chooserIntent, Constants.ACTION_REQUEST_IMAGE);
        } else {
            etName.setError("Please enter name");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.memberList) {
            Intent intent=new Intent(this,AllMembers_Activity.class);
            startActivity(intent);
            return true;
        }

        else if (id==R.id.add_one){
            int gender_id_is = radioGroupGender.getCheckedRadioButtonId();
            if (radioButtonMale.getId()==gender_id_is){
                gender="Male";
            }else if (radioButtonFemale.getId()==gender_id_is){
                gender="Female";
            }else {
                gender="";
            }
            genderID=String.valueOf(gender_id_is);

            infoModel =new InfoModelClass();

            infoModel.setName(etName.getText().toString().trim());
            infoModel.setPhone(etPhone.getText().toString().trim());
            infoModel.setEmail(etEmail.getText().toString().trim());
            infoModel.setBloodGroupId(bloodGroupID);
            infoModel.setBloodGroup(bloodGroup);
            infoModel.setGenderId(genderID);
            infoModel.setGender(gender);
            infoModel.setInstitute(etInstitute.getText().toString().trim());
            infoModel.setDepartment(etDepartment.getText().toString().trim());
            infoModel.setPresentAddress(etPresentAddress.getText().toString().trim());
            infoModel.setPermanentAddress(etPermanentAddress.getText().toString().trim());
            infoModel.setBirthDay(etBirthDay.getText().toString());

            //Check to see if there is valid image path temporarily in memory
            //Then save that image path to the database and that becomes the profile
            //Image for this user.
            if (mCurrentImagePath != null && !mCurrentImagePath.isEmpty())
            {
                infoModel.setImage_path(mCurrentImagePath);
            }

            sqLiteDbHelper.addInfoModel(infoModel);
            Toast.makeText(getApplicationContext(), etName.getText().toString()+" successfully added",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(AddInfoActivity.this,AllMembers_Activity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        bloodGroupID=String.valueOf(parent.getAdapter().getItemId(position));
        bloodGroup=String.valueOf(parent.getAdapter().getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void SetBD(View view) {
        new DatePickerDialog(this, dateSet, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            // Get the resultant image's URI.
            final Uri selectedImageUri = (data == null) ? mCapturedImageURI : data.getData();

            // Ensure the image exists.
            if (selectedImageUri != null) {

                // Add image to gallery if this is an image captured with the camera
                //Otherwise no need to re-add to the gallery if the image already exists
                if (requestCode == Constants.ACTION_REQUEST_IMAGE) {
                    final Intent mediaScanIntent =
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(selectedImageUri);

                    //I gave this instead of getActivity
                    this.sendBroadcast(mediaScanIntent);
                    /*Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    File f = new File(Constants.PICTURE_DIRECTORY);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);*/
                }

                //I gave this instead of getActivity
                mCurrentImagePath = FileUtils.getPath(this, selectedImageUri);
                //Bitmap thumbnail = (BitmapFactory.decodeFile(mCurrentImagePath));


                // Update client's picture
                if (mCurrentImagePath != null && !mCurrentImagePath.isEmpty()) {
                    ibImage.setImageDrawable(new BitmapDrawable(getResources(),
                            FileUtils.getResizedBitmap(mCurrentImagePath, 512, 512)));
                    //ibImage.setImageDrawable(new BitmapDrawable(getResources(),thumbnail));
                }else {
                    ibImage.setImageDrawable(infoModel.getImage(this));
                }
            }
        }
    }
}
