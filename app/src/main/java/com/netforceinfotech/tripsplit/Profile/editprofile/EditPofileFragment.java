package com.netforceinfotech.tripsplit.Profile.editprofile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.netforceinfotech.tripsplit.login.CountryData;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.netforceinfotech.tripsplit.R.id.etcountry;

public class EditPofileFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    View view;
    Context context;
    EditText etAge, etDoB, etAddress, etCountry, etaddressPopUp;
    ImageView imageViewDp;
    Button buttonEditDp, buttonDone;
    TextView textviewName;
    private MaterialDialog dialogPic, dialogAddress, dialogCountry;
    private static final String IMAGE_DIRECTORY_NAME = "tripsplit";

    private static final int PICK_IMAGE = 1234;
    private static final int TAKE_PHOTO_CODE = 1235;
    private Uri fileUri;
    private String filePath;
    private LinearLayout linearLayoutProgress, linearLayoutAddress;
    private String countryCode, country;
    private Uri imageToUploadUri;
    UserSessionManager userSessionManager;
    ArrayList<CountryData> countries = new ArrayList<>();
    private File finalFile;
    private MaterialDialog progressDialog;

    public EditPofileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        context = getActivity();
        userSessionManager = new UserSessionManager(context);
        initView(view);
        getUserInfo();
        setuptoolbar(view);
        getPermission();
        return view;
    }

    private File savebitmap(String filePath) {
        File file = new File(filePath);
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            outStream = new FileOutputStream(file);
            if (extension.equalsIgnoreCase("png")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
            } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
            } else {
                return null;
            }
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;


    }

    private void initView(View view) {
        progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0).build();
        buttonDone = (Button) view.findViewById(R.id.buttonDone);
        buttonEditDp = (Button) view.findViewById(R.id.buttonEditDp);
        etDoB = (EditText) view.findViewById(R.id.etdob);
        etAddress = (EditText) view.findViewById(R.id.etaddress);
        etCountry = (EditText) view.findViewById(etcountry);
        etAge = (EditText) view.findViewById(R.id.etAge);
        textviewName = (TextView) view.findViewById(R.id.textviewName);
        imageViewDp = (ImageView) view.findViewById(R.id.imageviewDP);
        buttonEditDp.setOnClickListener(this);
        buttonDone.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        etDoB.setOnClickListener(this);
        etCountry.setOnClickListener(this);
    }


    private void setuptoolbar(View view) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ImageView home = (ImageView) toolbar.findViewById(R.id.homeButton);
        ImageView icon = (ImageView) toolbar.findViewById(R.id.image_appicon);
        TextView textViewLogout = (TextView) toolbar.findViewById(R.id.textviewLogout);
        textViewLogout.setVisibility(View.GONE);
        home.setVisibility(View.VISIBLE);
        icon.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date2 = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            monthOfYear = monthOfYear + 1;
            date2 = date_format.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outDate = new SimpleDateFormat("yyyy-MM-dd");
        etDoB.setText(outDate.format(date2));


    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.etcountry:
                showCountryPopUp();
                break;
            case R.id.buttonDone:
                showPopUp();
                break;
            case R.id.etaddress:
                showEditAddressPopup();
                break;
            case R.id.buttonEditDp:
                showEditPicPopup();
                break;
            case R.id.linearLayoutPicture:

                takePictureIntent();
                dialogPic.dismiss();
                break;
            case R.id.linearLayoutGalary:
                pickPictureIntent();
                dialogPic.dismiss();
                break;
            case R.id.etdob:
                Calendar now1 = Calendar.getInstance();
                DatePickerDialog dpd1 = DatePickerDialog.newInstance(
                        EditPofileFragment.this,
                        now1.get(Calendar.YEAR),
                        now1.get(Calendar.MONTH),
                        now1.get(Calendar.DAY_OF_MONTH)
                );
                dpd1.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.buttonAddressDone:
                etAddress.setText(etaddressPopUp.getText());
                dialogAddress.dismiss();
                break;


        }


    }

    private void showCountryPopUp() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                etCountry.setText(name);
                countryCode = code;
                country = name;
                picker.dismiss();
            }
        });
    }

    private void editProfile() {
        progressDialog.show();
        //services.php?opt=updateprofile&user_id=11&profile_image=photo.jpg
        // &dob=1987-09-12&country=India&country_code=91&address=J 207 Ist Floor
        String addressString = etAddress.getText().toString();
        try {
            addressString = URLEncoder.encode(addressString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            addressString = "";
            showMessage("address parsing address");
        }

        String url = getResources().getString(R.string.url);
        String uploadurl = "services.php?opt=updateprofile&user_id=" + userSessionManager.getUserId() + "&country_code="
                + countryCode + "&country=" + etCountry.getText().toString() + "&dob=" + etDoB.getText().toString() + "&address=" + addressString;
        url = url + uploadurl;

        Log.i("result_url", url);
        Log.i("result_url", filePath + "   ");
        File file = null;
        if (filePath == null) {
            Ion.with(context)
                    .load(url)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            if (result == null) {
                                showMessage("nothing is happening");
                            } else {
                                Log.i("result_kunsang", result.toString());
                                String status = result.get("status").getAsString();
                                if (status.equalsIgnoreCase("success")) {
                                    showMessage("Uploaded successfully");
                                    getUserInfo();
                                } else {
                                    showMessage("something went wrong");
                                }
                            }
                        }
                    });
        } else {

            file = savebitmap(filePath);
            if (file == null) {
                showMessage("Image not valid");
                return;
            }
            Ion.with(context)
                    .load(url)
                    .setHeader("ENCTYPE", "multipart/form-data")
                    .setTimeout(60 * 60 * 1000)
                    .setMultipartParameter("goop", "noop")
                    .setMultipartFile("upload", "image/*", file)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            if (result == null) {
                                showMessage("nothing is happening");
                            } else {
                                Log.i("result_kunsang", result.toString());
                                String status = result.get("status").getAsString();
                                if (status.equalsIgnoreCase("success")) {
                                    showMessage("uploaded successfully");
                                }
                            }
                        }
                    });
        }

    }

    private void pickPictureIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void takePictureIntent() {
        UserSessionManager userSessionManager = new UserSessionManager(context);
        String name = userSessionManager.getName();
        Intent cameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri();
        filePath = fileUri.getPath();

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    private void showEditPicPopup() {
        boolean wrapInScrollView = true;
        dialogPic = new MaterialDialog.Builder(context)
                .title(R.string.editpic)
                .customView(R.layout.editpic, wrapInScrollView)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
        dialogPic.findViewById(R.id.linearLayoutGalary).setOnClickListener(this);
        dialogPic.findViewById(R.id.linearLayoutPicture).setOnClickListener(this);

    }

    private void showEditAddressPopup() {
     /*   getPermission();
        getLocation(0);
     */
        boolean wrapInScrollView = true;
        dialogAddress = new MaterialDialog.Builder(context)
                .title(R.string.editaddress)
                .customView(R.layout.editaddress, wrapInScrollView)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
        linearLayoutProgress = (LinearLayout) dialogAddress.findViewById(R.id.linearlayoutProgress);
        linearLayoutAddress = (LinearLayout) dialogAddress.findViewById(R.id.linearlayoutAddress);
        linearLayoutProgress.setVisibility(View.GONE);
        linearLayoutAddress.setVisibility(View.VISIBLE);
        etaddressPopUp = (EditText) dialogAddress.findViewById(R.id.etaddressPopUp);
        dialogAddress.findViewById(R.id.buttonAddressDone).setOnClickListener(this);


    }


    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void getPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            String[] permission = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            ActivityCompat.requestPermissions(getActivity(),
                    permission, 1);


        }
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            } else {
                Log.d(IMAGE_DIRECTORY_NAME, mediaStorageDir.getAbsolutePath());
            }

        } else {
            Log.d(IMAGE_DIRECTORY_NAME, mediaStorageDir.getAbsolutePath());
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = null;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        Log.i("result imagepath", mediaFile.getAbsolutePath());


        return mediaFile;
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Log.i("result picture", "clicked");
                    filePath = fileUri.getPath();
                    Glide.with(context).load(fileUri).into(imageViewDp);
                }
                break;
            case PICK_IMAGE:
                if (resultCode == getActivity().RESULT_OK) {

                    Uri uri = data.getData();
                    filePath = getPath(uri);
                    Log.i("kresult", filePath);
                    Glide.with(context).load(filePath).into(imageViewDp);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor;
        if (Build.VERSION.SDK_INT > 19) {
            // Will return "image:x*"
            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, sel, new String[]{id}, null);
        } else {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
        }
        String path = null;
        try {
            int column_index = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index).toString();
            cursor.close();
        } catch (NullPointerException e) {

        }
        return path;
    }

    private void getUserInfo() {
        progressDialog.show();
        //services.php?opt=viewprofile&user_id=11
        String baseUrl = getString(R.string.url);
        String viewProfile = "services.php?opt=viewprofile&user_id=" + userSessionManager.getUserId();
        String url = baseUrl + viewProfile;
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressDialog.dismiss();
                        // do stuff with the result or error
                        if (result != null) {
                            Log.i("kresult", result.toString());
                            setupUserData(result);
                        } else {
                            showMessage("Something went wrong");
                        }
                    }
                });
    }

    private void setupUserData(JsonObject result) {
        if (result.get("status").getAsString().equalsIgnoreCase("success")) {
            JsonArray data = result.getAsJsonArray("data");
            String name = "", email = "", profile_image = "", dob = "", country = "", country_code = "", address = "";
            JsonObject jsonObject = data.get(0).getAsJsonObject();
            if (!jsonObject.get("firstname").isJsonNull()) {
                name = jsonObject.get("firstname").getAsString();
            }
            if (!jsonObject.get("email").isJsonNull()) {
                email = jsonObject.get("email").getAsString();
            }
            if (!jsonObject.get("profile_image").isJsonNull()) {
                profile_image = jsonObject.get("profile_image").getAsString();
            }
            if (!jsonObject.get("dob").isJsonNull()) {
                dob = jsonObject.get("dob").getAsString();
            }
            if (!jsonObject.get("country").isJsonNull()) {
                country = jsonObject.get("country").getAsString();
            }
            if (!jsonObject.get("country_code").isJsonNull()) {
                country_code = jsonObject.get("country_code").getAsString();
            }
            if (!jsonObject.get("address").isJsonNull()) {
                address = jsonObject.get("address").getAsString();
            }

            try {
                Glide.with(context).load(profile_image).error(R.drawable.ic_error).into(imageViewDp);
            } catch (Exception ex) {
            }
            textviewName.setText(name);
            etDoB.setText(dob);
            etAddress.setText(address);
            etCountry.setText(country);
            setAge(dob);

        }
    }

    private void setAge(String dob) {
        if (dob.equalsIgnoreCase("0000-00-00")) {
            etAge.setText("NA");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(simpleDateFormat.parse(dob));// all done
                String age = getAge(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                etAge.setText(age);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private void showPopUp() {
        new MaterialDialog.Builder(context)
                .title("Edit Profile")
                .content("Are you sure you want to Edit your profile?")
                .positiveText("Yes")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        editProfile();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
