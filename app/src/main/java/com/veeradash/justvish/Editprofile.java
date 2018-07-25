package com.veeradash.justvish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Editprofile extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;

    EditText name,phone,emailid,location;
    ImageView dp;
    Button savebutton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    DatabaseReference muserdb;
    StorageReference mStorageRef;

    //IMAGE HOLD URI
    Uri imageHoldUri = null;

    //PROGRESS DIALOG
    ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        name=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phone);
        emailid=(EditText)findViewById(R.id.emailid);
        location=(EditText)findViewById(R.id.location);

        dp=(ImageView)findViewById(R.id.dp);


        savebutton=(Button) findViewById(R.id.save);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)

                {
                    Intent intent = new Intent(Editprofile.this, Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

        //PROGRESS DIALOG
        mProgress = new ProgressDialog(this);

        muserdb=FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserProfile();
            }
        });

        //Userimageview

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureSelection();
            }
        });

    }

    private void saveUserProfile() {
        final String mname,mphone,mlocation,memailid;

        mname=name.getText().toString().trim();
        mphone=phone.getText().toString().trim();
        mlocation=location.getText().toString().trim();
        memailid=emailid.getText().toString().trim();

        if(!TextUtils.isEmpty(mname) && !TextUtils.isEmpty(memailid))
        {
            if(imageHoldUri!=null)
            {
                mProgress.setTitle("Saveing Profile");
                mProgress.setMessage("Please wait....");
                mProgress.show();
                StorageReference mChildStorage = mStorageRef.child("Users").child("Displaypic").child(imageHoldUri.getLastPathSegment());
                String profilePicUrl = imageHoldUri.getLastPathSegment();

                mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("deprecation") final Uri imageUrl = taskSnapshot.getDownloadUrl();

                        muserdb.child("Users").child("Name").setValue(mname);
                        muserdb.child("Users").child("Email_Id").setValue(memailid);
                        muserdb.child("Users").child("Phone_No").setValue(mphone);
                        muserdb.child("Users").child("Location").setValue(mlocation);
                        muserdb.child("Users").child("userid").setValue(mAuth.getCurrentUser().getUid());
                        muserdb.child("Users").child("Displaypic").setValue(imageUrl.toString());

                        mProgress.dismiss();

                        finish();
                        Intent moveToHome = new Intent(Editprofile.this, Profile.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);


                    }
                });
            }else
            {

                Toast.makeText(Editprofile.this, "Please select the profile pic", Toast.LENGTH_LONG).show();

            }

        }else
        {

            Toast.makeText(Editprofile.this, "Please enter details", Toast.LENGTH_LONG).show();

        }
            }





    private void pictureSelection() {

        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Editprofile.this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    private void cameraIntent() {

        //CHOOSE CAMERA
        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                dp.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

}


