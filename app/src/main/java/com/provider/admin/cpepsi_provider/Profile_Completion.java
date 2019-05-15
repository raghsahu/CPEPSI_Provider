package com.provider.admin.cpepsi_provider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.provider.admin.cpepsi_provider.Model.ProfileModel;
import com.provider.admin.cpepsi_provider.SharedPreferences.AppPreference;
import com.provider.admin.cpepsi_provider.Util.CameraUtils;
import com.provider.admin.cpepsi_provider.Util.CommonUtils;
import com.provider.admin.cpepsi_provider.Util.Connectivity;
import com.provider.admin.cpepsi_provider.Util.Utilities;
import com.provider.admin.cpepsi_provider.Util.Utility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Completion extends AppCompatActivity {
    boolean isShow = false;
    int scrollRange = -1;
    CircleImageView profile_image;
    ImageView badge_notification_1;
    private String userChoosenTask;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final String IMAGE_EXTENSION = "jpg";
    private static String imageStoragePath;
    private Bundle savedInstanceState;
    int Gallery_view = 2;
    String UserId;
    private String communStr = "http://heightsmegamart.com/CPEPSI/uploads/";
 //   private String communStr = "https://www.paramgoa.com/cpepsi/uploads/";
    String image;
    private ProfileModel profileModel;
    Button profile_submit;
    EditText profileName, profileEmail, profileNumber, profilePassword, profileAddress, profileBusiness, profileLocation,
            photoId,cancelCheck,bankDetail,aadharCard,addressProof;
    String ProfileName, ProfileEmail, ProfileNumber, ProfilePassword, ProfileAddress, ProfileBusiness, ProfileLocation,
            PhotoId,CancelCheck,BankDetail,AadharCard,AddressProof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile__completion);
        getSupportActionBar().hide();

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    // showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
                    // hideOption(R.id.action_info);
                }
            }

        });

        UserId = AppPreference.getId(Profile_Completion.this);

        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        badge_notification_1 = (ImageView) findViewById(R.id.badge_notification_1);
        profile_submit = (Button) findViewById(R.id.profile_submit);
        profileName = (EditText) findViewById(R.id.profileName);
        profileEmail = (EditText) findViewById(R.id.profileEmail);
        profileNumber = (EditText) findViewById(R.id.profileNumber);
        profilePassword = (EditText) findViewById(R.id.profilePassword);
        profileAddress = (EditText) findViewById(R.id.profileAddress);
        profileBusiness = (EditText) findViewById(R.id.profileBusiness);
        profileLocation = (EditText) findViewById(R.id.profileLocation);
        photoId = (EditText) findViewById(R.id.photoId);
        cancelCheck = (EditText) findViewById(R.id.cancelCheck);
        bankDetail = (EditText) findViewById(R.id.bankDetail);
        aadharCard = (EditText) findViewById(R.id.aadharCard);
        addressProof = (EditText) findViewById(R.id.addressProof);



        badge_notification_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

//        profileModel = new ProfileModel();
        profileModel = new ProfileModel(image);

        //    previewCapturedImage();

        profile_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileName = profileName.getText().toString();
                ProfileEmail = profileEmail.getText().toString();
                ProfileNumber = profileNumber.getText().toString();
                ProfilePassword = profilePassword.getText().toString();
                ProfileAddress = profileAddress.getText().toString();
                ProfileBusiness = profileBusiness.getText().toString();
                ProfileLocation = profileLocation.getText().toString();
                PhotoId = photoId.getText().toString();
                CancelCheck = cancelCheck.getText().toString();
                BankDetail = bankDetail.getText().toString();
                AadharCard = aadharCard.getText().toString();
                AddressProof = addressProof.getText().toString();

                new PostSave().execute();
            }
        });

        if (Connectivity.isNetworkAvailable(Profile_Completion.this)) {
            new PostReceiptUpdate().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    //---------------------------------------------------------------
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Completion.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Profile_Completion.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        if (CameraUtils.checkPermissions(getApplicationContext())) {
                            captureImage();
                            restoreFromBundle(savedInstanceState);
                        } else {
                            requestCameraPermission(MEDIA_TYPE_IMAGE);
                        }
                    //captureImage();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //------------ private void galleryIntent()
    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, Gallery_view);
    }

 /*   private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }
    //--------------------------------------------------------------------

    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }
                }
            }
        }
    }

    private void previewCapturedImage() {

        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            new ImageCompression().execute(imageStoragePath);

            profile_image.setImageBitmap(bitmap);
            File imgFile = new File(imageStoragePath);
/*
            if (imgFile.exists()) {
                //  imgPolicyNo.setImageURI(Uri.fromFile(imgFile));
                //  show(imgFile);
                new ImageUploadTask(imgFile).execute();
                //  Toast.makeText(ClaimActivity.this,"data:="+imgFile,Toast.LENGTH_LONG).show();
            }*/

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(Profile_Completion.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;
        String result = "";
        File Image;

        public ImageUploadTask(File imgFile) {
            this.Image = imgFile;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Profile_Completion.this);
            dialog.setMessage("Processing");

            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            try {

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                entity.addPart("user_id", new StringBody(UserId));
                entity.addPart("image", new FileBody(Image));


                result = Utilities.postEntityAndFindJson("http://heightsmegamart.com/CPEPSI/api/upload_profile", entity);
            //    result = Utilities.postEntityAndFindJson("https://www.paramgoa.com/cpepsi/api/upload_profile", entity);

                return result;

            } catch (Exception e) {
                // something went wrong. connection with the server error
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Log.v("profile",result);

            String result1 = result;
            if (result != null) {

                dialog.dismiss();
                Log.e("result_Image", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String data = object.getString("data");
                    image = object.getString("image");
                    String responce = object.getString("responce");

                    if (responce.equals("true")) {
                        Toast.makeText(Profile_Completion.this, data, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile_Completion.this, data, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(Profile_Completion.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            } else {
                dialog.dismiss();
                //  Toast.makeText(Registration.this, "No Response From Server", Toast.LENGTH_LONG).show();
            }
        }
    }

    //.............................................................................
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                previewCapturedImage();

                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == Gallery_view && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imageStoragePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            profile_image.setImageBitmap(BitmapFactory.decodeFile(imageStoragePath));
            if (imageStoragePath != null) {
                new ImageCompression().execute(imageStoragePath);
            }

            cursor.close();
        }
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    //---------------------------

    public class ImageCompression extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return CommonUtils.compressImage(strings[0]);
        }

        protected void onPostExecute(String imagePath) {
            // imagePath is path of new compressed image.
//            mivImage.setImageBitmap(BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath()));
            if (imagePath != null) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    new ImageUploadTask(imgFile).execute();
                }
            } else {
                //  AlertDialogCreate();
            }

        }
    }

    //-------------------------------------------------

    public class PostSave extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Profile_Completion.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Provider_Ragistration");
             //   URL url = new URL("https://www.paramgoa.com/cpepsi/api/Provider_Ragistration");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", ProfileName);
                postDataParams.put("email", ProfileEmail);
                postDataParams.put("number", ProfileNumber);
                postDataParams.put("password", ProfilePassword);
                postDataParams.put("address", ProfileAddress);
                postDataParams.put("business", ProfileBusiness);
                postDataParams.put("location", ProfileLocation);
                postDataParams.put("user_id", UserId);

                postDataParams.put("user_id", UserId);
                postDataParams.put("user_id", UserId);
                postDataParams.put("user_id", UserId);
                postDataParams.put("user_id", UserId);
                postDataParams.put("user_id", UserId);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                String s = result.toString();
                try {
                    jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    String name = dataObj.getString("name");
                    String number = dataObj.getString("number");
                    String place = dataObj.getString("place");
                    String emailid = dataObj.getString("emailid");
                    String password = dataObj.getString("password");
                    String Designation = dataObj.getString("Designation");
                    String business = dataObj.getString("business");
                    String status = dataObj.getString("status");

                    if (responce.equals("true")) {
                        profileName.setText("");
                        profileEmail.setText("");
                        profileNumber.setText("");
                        profilePassword.setText("");
                        profileAddress.setText("");
                        profileBusiness.setText("");
                        profileLocation.setText("");
                        Toast.makeText(Profile_Completion.this, responce, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Profile_Completion.this, Home_navigation.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Profile_Completion.this, responce, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }

    //-------------------------------------------------

    public class PostReceiptUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Profile_Completion.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_profile");
              //  URL url = new URL("https://www.paramgoa.com/cpepsi/api/Get_profile");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id",UserId);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                Log.e("PostPIinsuranceDetails", result.toString());
                try {

                    jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    String user_id = dataObj.getString("user_id");
                    String TypeofFirm = dataObj.getString("TypeofFirm");
                    String Designation = dataObj.getString("Designation");
                    String business = dataObj.getString("business");
                    String City = dataObj.getString("City");
                    String state = dataObj.getString("state");
                    String place = dataObj.getString("place");
                    String number = dataObj.getString("number");
                    String name = dataObj.getString("name");
                    String dob = dataObj.getString("dob");
                    String adharno = dataObj.getString("adharno");
                    String middle = dataObj.getString("middle");
                    String sirname = dataObj.getString("sirname");
                    String emailid = dataObj.getString("emailid");
                    String password = dataObj.getString("password");
                    String service = dataObj.getString("service");
                    String sub_service = dataObj.getString("sub_service");
                    String status = dataObj.getString("status");
                    String image = dataObj.getString("image");

                    profileName.setText(name);
                    profileEmail.setText(emailid);
                    profileNumber.setText(number);
                    profilePassword.setText(password);
                    profileAddress.setText(City);
                    profileBusiness.setText(business);
                    profileLocation.setText(Designation);

                    Picasso.with(Profile_Completion.this)
                            .load(communStr + image)
                            .placeholder(R.drawable.noimg)
                            .into(profile_image);
                    Log.e("communStr + image>>>", communStr + image);

                    if (responce.equals("true")){
                        Toast.makeText(Profile_Completion.this, responce, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Profile_Completion.this, responce, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }
    //---------------------------------------------------------------

    @Override
    public void onBackPressed() {
        Intent to_log_in = new Intent(Profile_Completion.this, LOG_in_Service_provider.class);
        startActivity(to_log_in);
        finish();

        super.onBackPressed();
    }
}
