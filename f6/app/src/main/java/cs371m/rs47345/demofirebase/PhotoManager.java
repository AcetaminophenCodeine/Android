package cs371m.rs47345.demofirebase;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoManager {
    public interface searchByNameListener {
        void searchByNameCallback(String name, List<PhotoObject> photos);
    }
    public interface searchByDateListener {
        void searchByDateCallback(long beginDate, long endDate, List<PhotoObject> photos);
    }

    protected Activity parent;
    protected static String TAG = "FPhotoManager";
    protected String baseDir;
    protected String userName;
    protected DatabaseReference userDB;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public PhotoManager(Activity parent) {
        this.parent = parent;
        // NB: For now use internal storage as cache.
        //   TODO: Should scan cache dir on startup and clear files
        //verifyStoragePermissions(parent);
        //baseDir = Environment.getExternalStorageDirectory().getPath() + "/demofirebase";
    }

    // NB: Must be called on every authentication change
    // Input: Display name  Output: sanitized name for root of Firebase data
    public void updateCurrentUserName(String _userName) {
        if( _userName != null ) {
            // . is illegal in Firebase key, and more than one @ is illegal in email address
            _userName = _userName.replaceAll("\\.", "@"); // . illegal in Firebase key
            if (_userName != userName || userDB == null) {
                userDB = FirebaseDatabase.getInstance().getReference(_userName);
            }
        } else {
            userDB = null;
        }
        userName = _userName;
    }

    public byte[] convertResourceIDToBytes(int resourceID, int compression) {
        //compression should be between 0 and 100 (inclusive)
        Drawable drawable = ContextCompat.getDrawable(this.parent.getApplicationContext(), resourceID);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compression, stream);
        byte[] bitmapdata = stream.toByteArray();
        Log.d("PhotoManager", "size of byte stream: " + bitmapdata.length);
        return bitmapdata;
    }

    public byte[] convertBitmapToBytes(Bitmap bitmap, int compression) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compression, stream);
        byte[] bitmapdata = stream.toByteArray();
        Log.d("PhotoManager", "size of byte stream: " + bitmapdata.length);
        return bitmapdata;
    }

    public void uploadPhoto(String name, String comment, byte[] data) {
        if (data == null) return;
        Log.d("PhotoManager", "uploading photo titled " + name + " user: " + userName);
        //File file = File.createTempFile(name, "jpg", parent.getCacheDir());
        //try {
        //    FileOutputStream outputStream = FileOutputStream(file);
        //   outputStream.write(data);
        //    outputStream.close();
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        PhotoObject photo = new PhotoObject();
        photo.name = name;
        photo.comment = comment;
        //  Should put this in a file
        photo.encodedBytes = Base64.encodeToString(data, Base64.DEFAULT);
        photo.date = new Long(System.currentTimeMillis());
        if (userDB != null) {
            // XXX Write me: store photo in firebase (one line)

           userDB.child("photos").push().setValue(photo);

        } else {
            Log.d(TAG, "userDB is null!");
        }
    }

    public void searchByName(final String name, final searchByNameListener listener) {
        if (userDB == null) {
            Log.d(TAG, "userDB is null!");
            return;
        }
        // XXX Write a query that returns all of the photos with a given name
        // and calls the listener with the results

        if (userDB == null) {
            Log.d(TAG, "userDB is null!");
            return;
        }
        else
        {
            Log.d(TAG, "userDB is not null!");

        }

        Query query = userDB.child("photos")
                .orderByChild("name")
                        .equalTo(name);


        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<PhotoObject> l = new ArrayList<PhotoObject>();
                        if (dataSnapshot.hasChildren())
                        {
                            Log.d("photoByDate ", "dataSnapshot has children " );
                        }
                        else
                        {
                            Log.d("photoByDate ", "dataSnapshot has not children " );
                        }
                        for (DataSnapshot photoSnapshot : dataSnapshot.getChildren()) {
                            //Getting the data from snapshot
                            PhotoObject photo = photoSnapshot.getValue(PhotoObject.class);
                            Log.d("photoByName ", "n: " + photo.name + " c: " + photo.comment);
                            l.add(photo);
                        }
                        listener.searchByNameCallback(name,l);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.d(TAG, "Name query cancelled");
                    }
                });

    }

    public void searchByDate(final long beginDate, final long endDate,
                                final searchByDateListener listener) {
        if (userDB == null) {
            Log.d(TAG, "userDB is null!");
            return;
        }
        else
        {
            Log.d(TAG, "userDB is not null!");

        }

        Query query = userDB.child("photos")
                .orderByChild("date")
                .startAt(beginDate)
                .endAt(endDate);

        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        List<PhotoObject> l = new ArrayList<PhotoObject>();

                        if (dataSnapshot.hasChildren())
                        {
                            Log.d("photoByDate ", "dataSnapshot has child " );
                        }
                        else
                        {
                            Log.d("photoByDate ", "dataSnapshot has not children " );
                        }

                        for (DataSnapshot photoSnapshot : dataSnapshot.getChildren()) {
                            //Getting the data from snapshot
                            PhotoObject photo = photoSnapshot.getValue(PhotoObject.class);
                            Log.d("photoByDate ", "n: " + photo.name + " c: " + photo.comment);
                            l.add(photo);
                        }
                        listener.searchByDateCallback(beginDate, endDate, l);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        Log.d(TAG, "Date query cancelled");
                    }
                });
    }

    public void deleteAllRows(String permission) {
        if(!permission.equals("I understand what I am about to do.")) {
            Log.e("PhotoManager", "You must type the permission string \"I understand what I am about to do.\"");
            return;
        }
        if (userDB != null) {
            userDB.child("photos").setValue(null);
        }
    }
}
