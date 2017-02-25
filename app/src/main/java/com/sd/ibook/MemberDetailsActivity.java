package com.sd.ibook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import Helper.FileUtils;
import SQLiteDatabase.SQLiteDbHelper;


public class MemberDetailsActivity extends ActionBarActivity {

    TextView tvName,tvPhone,tvEmail,tvBloodGroup,tvGender,tvInstitute,tvDepartment,tvPresentAddress,
                tvPermanentAddress,tvBirthday;
    SQLiteDbHelper sqLiteDbHlpr;

    private String[] details;
    String phone;
    ImageButton ibDisplayImage;
    String imagePath;

    String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        sqLiteDbHlpr=new SQLiteDbHelper(this);

        tvName=(TextView)findViewById(R.id.tvName);
        tvPhone=(TextView)findViewById(R.id.tvPhone);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvBloodGroup=(TextView)findViewById(R.id.tvBloodGroup);
        tvGender=(TextView)findViewById(R.id.tvGender);
        tvInstitute=(TextView)findViewById(R.id.tvInstitute);
        tvDepartment=(TextView)findViewById(R.id.tvDepartment);
        tvPresentAddress=(TextView)findViewById(R.id.tvPresentAddress);
        tvPermanentAddress=(TextView)findViewById(R.id.tvPermanentAddress);
        tvBirthday=(TextView)findViewById(R.id.tvBirthday);
        ibDisplayImage=(ImageButton)findViewById(R.id.ibDisplayImage);

        this.details=getIntent().getExtras().getStringArray("Details");

        tvName.setText(" "+this.details[1]);
        //String name=this.details[1];
        tvPhone.setText(" "+this.details[2]);
        phone=this.details[2];

        tvEmail.setText(" "+this.details[3]);
        emailAddress=this.details[3];

        tvBloodGroup.setText(" "+this.details[4]);
        tvGender.setText(" "+this.details[5]);
        tvInstitute.setText(" "+this.details[6]);
        tvDepartment.setText(" "+this.details[7]);
        tvPresentAddress.setText(" "+this.details[8]);
        tvPermanentAddress.setText(" "+this.details[9]);
        tvBirthday.setText(" "+this.details[10]);
        imagePath=this.details[11];


//        Bitmap bMap = BitmapFactory.decodeFile(imagePath);
//        ibDisplayImage.setImageBitmap(bMap);

        if(imagePath!=null){
            ibDisplayImage.setImageDrawable(new BitmapDrawable(getResources(),
                    FileUtils.getResizedBitmap(imagePath, 512, 512)));
        }
        else{
            Drawable new_image= getResources().getDrawable(R.drawable.default_avatar);
            ibDisplayImage.setBackgroundDrawable(new_image);
            ///ibDisplayImage.setBackground(R.drawable.default_avatar);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_member_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteNote) {

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sqLiteDbHlpr.deleteInfoModel(MemberDetailsActivity.this.details[1],Integer.valueOf(MemberDetailsActivity.this.details[0]));
                            Toast.makeText(getApplicationContext(),MemberDetailsActivity.this.details[1]+" deleted successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MemberDetailsActivity.this,AllMembers_Activity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog d=builder.create();
            d.setTitle("Are you sure?");
            d.show();
            return true;
        }

        else if (id == R.id.editNotes) {

            String details[] = sqLiteDbHlpr.getMemberDetailsForEdit(MemberDetailsActivity.this.details[1], Integer.valueOf(MemberDetailsActivity.this.details[0]));
            Intent i = new Intent(getApplicationContext(), MemberEditActivity.class);
            i.putExtra("Details", details);
            startActivity(i);

            return true;
        }

        else if (id==R.id.call){

                String url="tel:"+phone;
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse(url));
                startActivity(intent);

            return true;

        }

        else if (id==R.id.sms){

            Uri uri = Uri.parse("smsto:" + phone);

            Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
            try{
                startActivity(smsSIntent);
            } catch (Exception ex) {
                Toast.makeText(MemberDetailsActivity.this, "Your sms has failed...",
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }

            return true;

        }

        else if (id==R.id.email){

            String emailAdd[]={emailAddress};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAdd);
            emailIntent.setType("plain/text");
            startActivity(emailIntent);

            return true;

        }

        else if (id==R.id.share){

            String fullInfo= "Name: "+this.details[1]+
                             "\nPhone: "+this.details[2]+
                             "\nEmail: "+this.details[3]+
                             "\nBlood Group: "+this.details[4]+
                             "\nGender: "+this.details[5]+
                             "\nInstitute: "+this.details[6]+
                             "\nDepartment: "+this.details[7]+
                             "\nPresent Address: "+this.details[8]+
                             "\nPermanent Address: "+this.details[9]+
                             "\nBirthday: "+this.details[10];

            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Information: "+this.details[1]);
            shareIntent.putExtra(Intent.EXTRA_TEXT,fullInfo);
            startActivity(Intent.createChooser(shareIntent,"share_by"));

            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
