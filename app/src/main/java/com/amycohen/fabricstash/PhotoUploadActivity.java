package com.amycohen.fabricstash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoUploadActivity extends AppCompatActivity {

    @BindView(R.id.imagePreview) ImageView mImagePreview;
    @BindView(R.id.nameOfFabric) EditText mNameOfFabric;
    @BindView(R.id.store) EditText mFabricCompany;
    @BindView(R.id.fabricType) EditText mFabricType;


    private static final int REQUEST_SAVE_PHOTO = 1;

    private String mCurrentPhotoPath = null;
    private Bitmap mBitmap = null;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);

        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        dispatchTakePictureIntent();
//        setPictureFromFile();
    }


    //From https://gist.github.com/geluso/8ce147ccfe34671245f3574634d95225
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.amycohen.fabricstash",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_SAVE_PHOTO);
            }
        }
    }

    @OnClick(R.id.upload)
    public void upload() {

        if (mBitmap == null) {
            return;
        }

        StorageReference photoRef = mStorageRef.child("fabricPhotos/" + mCurrentPhotoPath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        photoRef.putBytes(data)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //getDownloadUrl() no longer viable. See below for resource:
                //https://github.com/udacity/and-nd-firebase/issues/46

                // Get a URL to the uploaded content
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//                Uri downloadUrl = taskSnapshot.getUploadSessionUri();

                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String photoLink = uri.toString();
                        //https://stackoverflow.com/questions/50585334/tasksnapshot-getdownloadurl-method-not-working
                        //Had to move this call up here and change the parameter type of the saveImageUrlToDatabase to String
                        PhotoUploadActivity.this.saveImageUrlToDatabase(photoLink);
                    }
                });
                //broken command since I still have to write the new info to replace the getDownloadUrl method.
//                PhotoUploadActivity.this.saveImageUrlToDatabase(downloadUrl);
//                PhotoUploadActivity.this.saveImageUrlToDatabase(task);
            }
        })

        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                exception.printStackTrace();
            }
        });

    }
    //changed the parameter to Task<Uri> instead of Uri
    private void saveImageUrlToDatabase(String storageUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();
        String nameOfFabric = mNameOfFabric.getText().toString();
        String fabricCompany = mFabricCompany.getText().toString();
        String fabricType = mFabricType.getText().toString();

        Log.d("UPLOAD", uid + " " + nameOfFabric);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("fabricInventory");
        String myKey = myRef.getKey().toString();

//        if (myRef.getKey().equals(myRef.child(uid))){
//
//        myRef = database.getReference("fabricInventory").child(uid);
//        DatabaseReference newPhoto = myRef.push();
//
////        myRef.child(uid).setValue(newPhoto);
//        newPhoto.child("uid").setValue(uid);
//        newPhoto.child("fabricName").setValue(nameOfFabric);
//        newPhoto.child("fabricCompany").setValue(fabricCompany);
//        newPhoto.child("fabricType").setValue(fabricType);
//        newPhoto.child("imageUrl").setValue(storageUrl);
//        } else {
//

        //SOURCE: how I figured out to add the reference to a specific user
        //http://www.tothenew.com/blog/custom-ids-in-firebase/
            DatabaseReference newPhoto = myRef.child(uid).push();
            newPhoto.child("uid").setValue(uid);
            newPhoto.child("fabricName").setValue(nameOfFabric);
            newPhoto.child("fabricCompany").setValue(fabricCompany);
            newPhoto.child("fabricType").setValue(fabricType);
            newPhoto.child("imageUrl").setValue(storageUrl);

        populateFeed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SAVE_PHOTO && resultCode == RESULT_OK) {
            setPictureFromFile();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //https://stackoverflow.com/questions/40498380/java-lang-illegalargumentexception-failed-to-find-configured-root-that-contains/42410808
        //also changed the XML file per the above link
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg",  /* suffix */
                storageDir     /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPictureFromFile() {
        // Get the dimensions of the View
        int targetW = mImagePreview.getWidth();
        int targetH = mImagePreview.getHeight();

//        if (targetW == 0 && targetH == 0) {
////            targetW = 400;
//            targetW = mBitmap.getWidth();
//            targetH = mBitmap.getHeight();
////            View.MeasureSpec.getSize();
////            targetH = 400;
//        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 0;
        if (targetW == 0 && targetH == 0) {
        //SOURCE: https://stackoverflow.com/questions/11252067/how-do-i-get-the-screensize-programmatically-in-android
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int ht = displaymetrics.heightPixels;
            int wt = displaymetrics.widthPixels;
            scaleFactor = Math.min(photoH/ht, photoW/wt);
        } else {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImagePreview.setImageBitmap(bitmap);

        mBitmap = bitmap;
    }

    public void populateFeed() {
        Intent intent = new Intent(this, FabricListActivity.class);
        startActivity(intent);
    }
}
