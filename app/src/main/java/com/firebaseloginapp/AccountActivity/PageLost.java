
package com.firebaseloginapp.AccountActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebaseloginapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Poorva on 4/3/2018.
 */

public class PageLost extends AppCompatActivity {


    private EditText mValueField;
    private EditText mPassword;
    private EditText mVehicleModel;
    private EditText mChassisNo;
    private EditText mLastSeen;             //It is Email of user

    private EditText mDate;

    private EditText mKeyValue;


    public Button mAddBtn;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mMessagesDatabaseReference;



    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_page);




        mFirebaseDatabase= FirebaseDatabase.getInstance();

        mValueField=(EditText)findViewById(R.id.name_lost);
        mPassword=(EditText)findViewById(R.id.password_lost);
        mVehicleModel=(EditText)findViewById(R.id.vehiclemodel_lost);
        mChassisNo=(EditText)findViewById(R.id.chassisno_lost);
        mLastSeen=(EditText)findViewById(R.id.lastseen_lost);
        mDate = (EditText)findViewById(R.id.date_lost);
        mKeyValue=(EditText)findViewById(R.id.mobileNo_lost);
        mAddBtn=(Button)findViewById(R.id.button_lost);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                final String value = mValueField.getText().toString();
                final String password = mPassword.getText().toString();
                final String vehicleModel = mVehicleModel.getText().toString();
                final String chassisNo = mChassisNo.getText().toString();
                final String lastSeen = mLastSeen.getText().toString();
                final String key = mKeyValue.getText().toString();
                final String Date  = mDate.getText().toString();


      //String Validation for Empty
                if(TextUtils.isEmpty(value)){
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(key)){
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Enter Vehicle Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(vehicleModel)){
                    Toast.makeText(getApplicationContext(), "Enter Vehicle Model!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(chassisNo)){
                    Toast.makeText(getApplicationContext(), "Enter Chassis Number !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(lastSeen)){
                    Toast.makeText(getApplicationContext(), "Enter Your Email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(Date)){
                    Toast.makeText(getApplicationContext(), "Enter Registration Date of Vehicle !", Toast.LENGTH_SHORT).show();
                    return;
                }
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Lost Page").push();

                //    rootRef.push().setValue("User");

                //Store data in database
                //   FirebaseDatabase usersRef = ref.child("Users");
                Map<String, String> userData = new HashMap<String, String>();

                userData.put("Name", value);
                userData.put("Vehicle Number", password);
                userData.put("Vehicle Model", vehicleModel);
                userData.put("Chassis No.", chassisNo);
                userData.put("Email", lastSeen);
                userData.put("Mobile No.", key);
                userData.put("Registration Date",Date);

 //String Validation
               if(value.length() <3)
               {
                   mValueField.setError("Name Should contain atleast 3letters");
               }
               else if(key.length() <10 || key.length() >10)
               {
                   mKeyValue.setError("Enter Valid Mobile No.");
               }

                else if(password.length() <9 || password.length() >10)
                {
                    mPassword.setError("Enter Valid Vehicle Number!");
                }
               else if(vehicleModel.length() <3 )
               {
                   mVehicleModel.setError("Enter Correct Vehicle Model");
               }

               else if(chassisNo.length() <12 || chassisNo.length() >12)
               {
                   mChassisNo.setError("Enter first 12letters from chassis no.");
               }
               else if(lastSeen.length() < 3)
               {
                   mLastSeen.setError("Enter Correct Email");
               }


               else if(Date.length() !=10)
               {
                   mDate.setError("Enter Date in Proper Format!");

               }
               else {
                   ///////////////////////////////////NEWLY ADDED/////////////////////////////////////
                   //////////////Authetication for data in Lost Page
                   final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Vehicle Database");

                   Query query = reference1.orderByChild("Vehicle Number").equalTo(password);
                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot3) {

                           if (dataSnapshot3.exists()) {

                               //////////////FOR SECOND PARAMETER EQUALITY CHECKING
                               Query query = reference1.orderByChild("Chassis No").equalTo(chassisNo);
                               query.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot4) {

                                       if (dataSnapshot4.exists()) {

                                            //////////////FOR THIRD PARAMETER EQUALITY CHECKING
                                            Query query = reference1.orderByChild("Registration Date").equalTo(Date);
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot5) {

                                                    if (dataSnapshot5.exists()) {

                                                        ///////////fOR STORING INTO THE DATABASE
                                                        rootRef.child("Name").setValue(value);
                                                        rootRef.child("Vehicle Number").setValue(password);
                                                        rootRef.child("Vehicle Model").setValue(vehicleModel);
                                                        rootRef.child("Chassis No").setValue(chassisNo);
                                                        rootRef.child("Email").setValue(lastSeen);
                                                        rootRef.child("Mobile No").setValue(key);
                                                        rootRef.child("Registration Date").setValue(Date);

                                                        //////////////FOR NOTIFICATION SENDING
                                           Intent intent = new Intent(PageLost.this, MainActivity.class);
                                           PendingIntent pendingIntent = PendingIntent.getActivity(PageLost.this,0,intent,0);

                                           NotificationCompat.Builder mBuilder =
                                                   new NotificationCompat.Builder(PageLost.this)
                                                           .setSmallIcon(R.drawable.common_full_open_on_phone)
                                                           .setContentTitle("Finder")
                                                           .setContentText("Your vehicle is autheticated")
                                                           .setContentIntent(pendingIntent );

                                           NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                           notificationManager.notify(0, mBuilder.build());

                                                        startActivity(new Intent(PageLost.this, ThankYou.class));
                                                    }
                                                    else
                                                    {
                                                        startActivity(new Intent(PageLost.this, ThankYou.class));
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                       }

                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {

                                   }
                               });

                           }

                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

                   ////////////////////////////////////NEWLY ADDED END////////////////////////////////
                }
            }
        });

    }



}


