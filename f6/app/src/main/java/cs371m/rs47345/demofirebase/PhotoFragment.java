package cs371m.rs47345.demofirebase;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class PhotoFragment
        extends Fragment
        implements PhotoManager.searchByNameListener,
        PhotoManager.searchByDateListener {

    protected PhotoManager photos;
    protected View myRootView;

    //for taking the photo and uploading it
    protected ImageView theImageView;
    protected EditText titleEditText;
    protected EditText commentEditText;
    protected Bitmap photoFromCamera;
    boolean pictureHasBeenTaken;
    Button takeAPictureButton;
    Button uploadButton;

    //for searching by name
    protected ImageView findImage;
    protected EditText searchByNameEditText;
    protected TextView searchCommentText; // Comment from retrieved photo
    Button searchByNameButton;

    //for searching by time
    protected EditText searchByTimeEditText;
    protected List<PhotoObject> photosFound;
    protected int currentPhoto;
    Button searchByTimeButton;
    Button nextImageButton;
    Button clearDatabaseButton;

    //no idea why I need this but the example had it...
    private static final int CAMERA_REQUEST = 1888;

    protected Toast mainToast;

    protected void doToast(String message) {
        if (this.mainToast != null) {
            this.mainToast.cancel();
        }
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photo_fragment, container, false);
        myRootView = v;
        return v;
    }

    // NB: Must be called on every authentication change
    public void updateCurrentUserName(String _userName) {
        if (photos != null) {
            photos.updateCurrentUserName(_userName);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = myRootView;
        this.photos = new PhotoManager(getActivity());

        //set up things for uploading photos
        this.theImageView = (ImageView) v.findViewById(R.id.theImage);
        this.titleEditText = (EditText) v.findViewById(R.id.titleEditText);
        this.commentEditText = (EditText) v.findViewById(R.id.commentEditText);
        this.photoFromCamera = null;
        this.pictureHasBeenTaken = false;

        //set up things for searching by name
        this.findImage = (ImageView) v.findViewById(R.id.findByNameImage);
        this.searchByNameEditText = (EditText) v.findViewById(R.id.searchByNameEditText);

        //set up things for searching by time
        this.searchByTimeEditText = (EditText) v.findViewById(R.id.searchByTimeEditText);

        this.currentPhoto = -1;

        this.takeAPictureButton = (Button) v.findViewById(R.id.takeAPictureButton);
        this.uploadButton = (Button) v.findViewById(R.id.uploadButton);
        this.searchByNameButton = (Button) v.findViewById(R.id.searchByNameButton);
        this.searchByTimeButton = (Button) v.findViewById(R.id.searchByTimeButton);
        this.nextImageButton = (Button) v.findViewById(R.id.nextImageButton);
        this.searchCommentText = (TextView) v.findViewById(R.id.searchCommentText);
        this.clearDatabaseButton = (Button) v.findViewById(R.id.clearDatabaseButton);

        final PhotoFragment t = this;

        this.takeAPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.pictureButtonPressed(v);
            }
        });
        this.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.uploadButtonPressed(v);
            }
        });
        this.searchByNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {t.searchByNamePressed(v);
            }
        });
        this.searchByTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {t.searchByTimePressed(v);
            }
        });
        this.nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.nextButtonPressed(v);
            }
        });
        this.clearDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {t.clearDatabasePressed(v);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            this.theImageView.setImageBitmap(photo);
            this.photoFromCamera = photo;
            this.pictureHasBeenTaken = true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    public void pictureButtonPressed(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void uploadButtonPressed(View view) {
        if (!this.pictureHasBeenTaken) {
            this.doToast("You must take a picture first!");
            return;
        }

        String name = this.titleEditText.getText().toString();
        String comment = this.commentEditText.getText().toString();

        if (name == null || name == "") {
            this.doToast("You must give a name!");
            return;
        }

        this.photos.uploadPhoto(name, comment,
                this.photos.convertBitmapToBytes(this.photoFromCamera, 100));

        this.doToast("Photo Uploaded");
    }

    ///////////////////////////////////////////////////////////////////////////////////

    public void searchByNamePressed(View view) {
        String name = this.searchByNameEditText.getText().toString();
        // Initialize
        this.photosFound = null;
        this.currentPhoto = -1;
        findImage.setImageDrawable(null);

        this.photos.searchByName(name, this);
    }

    @Override
    public void searchByNameCallback(String name, List<PhotoObject> photos) {
        Log.d("PhotoFragment", "got some photos for name query " + name);
        if (photos == null || photos.size() == 0) {
            this.doToast("Could not find any photo with name " + name);
            Log.d("main", "No Photos");
            return;
        }
        Log.d("PhotoFragment",
                "n: " + photos.get(0).getName()
                        + " c: " + photos.get(0).getComment()
                        + " d: " + photos.get(0).getDate());

        this.photosFound = photos;
        this.currentPhoto = -1;
        this.nextButtonPressed(null);
    }


    ///////////////////////////////////////////////////////////////////////////////////////

    public void searchByTimePressed(View view) {
        long seconds = 0;
        try {
            seconds = Long.parseLong(this.searchByTimeEditText.getText().toString());
        } catch (Exception e) {
            this.doToast("Invalid entry!");
            return;
        }
        long now = System.currentTimeMillis();
        long beginTime = now - 1000 * seconds;
        // Initialize
        this.photosFound = null;
        this.currentPhoto = -1;
        findImage.setImageDrawable(null);
        this.photos.searchByDate(beginTime, now, this);
    }

    @Override
    public void searchByDateCallback(long beginDate, long endDate, List<PhotoObject> photos) {
        Log.d("main", "callback photos between dates " + beginDate + " and " + endDate);
        if (photos == null || photos.size() == 0) {
            this.doToast("Could not find any photos in given time frame");
            Log.d("main", "No Photos");
            return;
        }
        for (PhotoObject po : photos) {
            Log.d("main", "name: " + po.getName());
            Log.d("main", "comment: " + po.getComment());
        }
        this.photosFound = photos;
        this.currentPhoto = -1;
        this.nextButtonPressed(null);
    }

    public void nextButtonPressed(View view) {
        if (this.photosFound == null) return;
        this.currentPhoto = (this.currentPhoto + 1) % this.photosFound.size();
        PhotoObject photo = this.photosFound.get(this.currentPhoto);
        this.searchCommentText.setText(photo.getComment());
        byte[] data = Base64.decode(photo.encodedBytes, Base64.DEFAULT);
        findImage.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public void clearDatabasePressed(View view) {
        this.photos.deleteAllRows("I understand what I am about to do.");
        this.doToast("All photos have been deleted.");
    }
}
