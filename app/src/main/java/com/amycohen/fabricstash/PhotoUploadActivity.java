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
import android.util.Log;
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

                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String photoLink = uri.toString();
                    }
                });
                //broken command since I still have to write the new info to replace the getDownloadUrl method.
//                PhotoUploadActivity.this.saveImageUrlToDatabase(downloadUrl);
                PhotoUploadActivity.this.saveImageUrlToDatabase(task);
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
    private void saveImageUrlToDatabase(Task<Uri> storageUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();
        String description = mNameOfFabric.getText().toString();

        Log.d("UPLOAD", uid + " " + description);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("fabricInventory");

        DatabaseReference newPhoto = myRef.push();
//        newPhoto.child("uid").setValue(uid);
        newPhoto.child("description").setValue(description);
        newPhoto.child("imageUrl").setValue(storageUrl.toString());

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

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth/2;
        int photoH = bmOptions.outHeight/2;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

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
