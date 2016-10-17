package com.netforceinfotech.tripsplit.Profile.editprofile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


public class EditPofileFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
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
    private String countryCode;

    public EditPofileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        context = getActivity();
        initView(view);
        setuptoolbar(view);
        return view;
    }

    private void initView(View view) {
        buttonDone = (Button) view.findViewById(R.id.buttonDone);
        buttonEditDp = (Button) view.findViewById(R.id.buttonEditDp);
        etDoB = (EditText) view.findViewById(R.id.etdob);
        etAddress = (EditText) view.findViewById(R.id.etaddress);
        etCountry = (EditText) view.findViewById(R.id.etcountry);
        etAge = (EditText) view.findViewById(R.id.etAge);
        textviewName = (TextView) view.findViewById(R.id.textviewName);
        imageViewDp = (ImageView) view.findViewById(R.id.imageviewDP);
        buttonEditDp.setOnClickListener(this);
        buttonDone.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        etCountry.setOnClickListener(this);
        etDoB.setOnClickListener(this);
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

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
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

        SimpleDateFormat outDate = new SimpleDateFormat("dd-MMM-yyyy");
        etDoB.setText(outDate.format(date2));


    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.etcountry:
                showEditCountryPopup();
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

    private void showEditCountryPopup() {


    }

    private void getLocation(final int select) {
        showMessage("called get loaction");
        Log.i("klocation", "called get location");
        SmartLocation.with(context).location()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        showMessage("inside onloaction changed");
                        getAddress(location.getLongitude(), location.getLatitude(), select);
                        Log.i("klocation", "inisde onlocation changed");
                    }
                });
    }

    private void getAddress(double longitude, double latitude, int select) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String finalAddress = "";
        String address, city, state, country = "", postalCode, knownName;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            try {
                knownName = addresses.get(0).getFeatureName();
                finalAddress += knownName;
            } catch (Exception ex) {

            }
            try {
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                finalAddress += address;
            } catch (Exception ex) {
            }
            try {
                city = addresses.get(0).getLocality();
                finalAddress += city;
            } catch (Exception ex) {

            }
            try {
                state = addresses.get(0).getAdminArea();
                finalAddress += state;

            } catch (Exception ex) {

            }
            try {
                country = addresses.get(0).getCountryName();
                countryCode = addresses.get(0).getCountryCode();

            } catch (Exception ex) {
                country = "";

            }
            try {
                postalCode = addresses.get(0).getPostalCode();

            } catch (Exception ex) {
                postalCode = "";
            }

        } catch (IOException e) {
            Log.i("klocation", "unknown location");
        }

        if (select == 0) {
            etaddressPopUp.setText(finalAddress);
        } else {
            etaddressPopUp.setText(country);
        }
        linearLayoutProgress.setVisibility(View.GONE);
        linearLayoutAddress.setVisibility(View.VISIBLE);
        showMessage("reached last");

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
                }
                break;
            case PICK_IMAGE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri uri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = getPath(uri);
                    Log.i("result picture", "picked");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

}
