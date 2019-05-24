
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


public class PageFound extends AppCompatActivity {


    private EditText mVehicleNumber;
    private EditText mColour;
    private EditText mPlace;
    private EditText mPhoneNumber;
    private Button mAddBtn;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference rootRef1;




    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_page);


        mFirebaseDatabase= FirebaseDatabase.getInstance();

        mAddBtn=(Button)findViewById(R.id.button_found);

        mVehicleNumber=(EditText)findViewById(R.id.vehicle_number_found);
        mColour=(EditText)findViewById(R.id.vehicle_colour_found);
        mPlace=(EditText)findViewById(R.id.place_found);
        mPhoneNumber=(EditText)findViewById(R.id.contact_found);



        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String vehiclenumber = mVehicleNumber.getText().toString();
                String colour = mColour.getText().toString();
                String place = mPlace.getText().toString();
                String phone = mPhoneNumber.getText().toString();


                //String Validation

                if(TextUtils.isEmpty(vehiclenumber)){
                    Toast.makeText(getApplicationContext(), "Enter Vehicle Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(colour)){
                    Toast.makeText(getApplicationContext(), "Enter Color of Vehicle!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(place)){
                    Toast.makeText(getApplicationContext(), "Enter Place Details!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(), "Enter Conatct Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                 final DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference().child("Found Page").push();

                //rootRef1.child("Found Page").setValue(place);
                Map<String, String> userData1 = new HashMap<String, String>();
                userData1.put("Vehicle Number",vehiclenumber);
                userData1.put("Colour",colour);
                userData1.put("Place", place);
                userData1.put("Contact",phone);

//String Validations
                if(vehiclenumber.length() <9 || vehiclenumber.length() >10)
                {
                    mVehicleNumber.setError("Enter Valid Vehicle Number!");
                }

                else if(colour.length() < 3)
                {
                    mColour.setError("Enter Color Name");
                }

                else if(place.length() < 3)
                {
                    mPlace.setError("Enter Correct Place Details");
                }
                else if(phone.length() <10 || phone.length() >10)
                {
                    mPhoneNumber.setError("Enter Valid Mobile No.");
                }


                else {
                    ///////////////////////NEWLY ADDED///////////////////////////////////////////////////////////
                    //For Found page retreived data
                    final String fphoneNumber, fplace, fcolour, fvehicleNumber;

                    final DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference().child("Found Page");
                //    mphone = (TextView) findViewById(R.id.check);
                    rootRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                // Log.e(TAG, "======="+postSnapshot.child("Place").getValue());
                                // Log.e(TAG, "======="+postSnapshot.child("name").getValue());
                                String fphoneNumber = postSnapshot.child("Contact").getValue().toString();
                                String fcolour = postSnapshot.child("Colour").getValue().toString();
                                final String fvehicleNumber = postSnapshot.child("Vehicle Number").getValue().toString();
                                final String fplace = postSnapshot.child("Place").getValue().toString();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Lost Page");

                                Query query = reference.orderByChild("Vehicle Number").equalTo(vehiclenumber);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                        for (DataSnapshot postSnapshot2 : dataSnapshot2.getChildren())
                                            if (postSnapshot2.exists()) {
                                              //////SEND NOTIFICATION
                                                        Intent intent = new Intent(PageFound.this, MainActivity.class);
                                                        PendingIntent pendingIntent = PendingIntent.getActivity(PageFound.this,0,intent,0);

                                                        NotificationCompat.Builder mBuilder =
                                                                  new NotificationCompat.Builder(PageFound.this)
                                                                         .setSmallIcon(R.drawable.common_full_open_on_phone)
                                                                         .setContentTitle("Finder")
                                                                          .setContentText("Someone has complained for vehicle you have found")
                                                                         .setContentIntent(pendingIntent );
                                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                        notificationManager.notify(0, mBuilder.build());

                                                     ///////////////
                                               // String mail=LoginActivity.email;


                                                String email = "kunal.porwal16@vit.edu";//"kunal.porwal16@vit.edu";// editTextEmail.getText().toString().trim();
                                                String subject ="Finder App"; //editTextSubject.getText().toString().trim();
                                                String message ="Congratulations!!!Your Vehicle is found. You can Collect it from Bibwewadi Police Station. PS:You will get your vehicle only after verification of all required documents.   Regards, Team Finder"; //editTextMessage.getText().toString().trim();

                                                //Creating SendMail object
                                                SendMail sm = new SendMail(PageFound.this, email, subject, message);

                                                    //Executing sendmail to send email
                                                    sm.execute();

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

                    //////////////////////////NEWLY ADDED ENDS////////////////////////////////////////
                    rootRef1.child("Vehicle Number").setValue(vehiclenumber);
                    rootRef1.child("Colour").setValue(colour);
                    rootRef1.child("Place").setValue(place);
                    rootRef1.child("Contact").setValue(phone);

                    startActivity(new Intent(PageFound.this, ThankYou.class));
                }
            }

        });







    }

}
