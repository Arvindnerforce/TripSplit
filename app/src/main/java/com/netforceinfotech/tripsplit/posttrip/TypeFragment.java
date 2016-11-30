package com.netforceinfotech.tripsplit.posttrip;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appyvet.rangebar.RangeBar;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.netforceinfotech.tripsplit.R;
import com.netforceinfotech.tripsplit.general.UserSessionManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class TypeFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, RoutingListener, CompoundButton.OnCheckedChangeListener {

    private static final String IMAGE_DIRECTORY_NAME = "tripsplit";

    private static final int PICK_IMAGE = 1234;
    private static final int TAKE_PHOTO_CODE = 1235;
    private static final int DESTINATION = 420;
    private static final int SOURCE = 421;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    AbstractRouting.TravelMode mode;
    private PolylineOptions polylineOptions = new PolylineOptions();
    private ArrayList<LatLng> arrayPoints = new ArrayList<>();
    LinearLayout linearLayoutReturn;
    Context context;
    TextView textViewETD, pass_txt, space_txt, textviewETA, textviewAgeGroup, textViewReturnETA, textViewReturnETD;
    Button increamentPass, decreamentPass, increamentSpace, decreamentSpace, male, female, mixed, buttonPost, buttonAddImage;
    int pass_number = 1;
    int space_number = 1;
    boolean etdclicked = true;
    private Uri fileUri;
    private String filePath;
    private MaterialDialog dialog;
    Button buttonCurrency;
    RangeBar rangeBar;
    RadioButton radioButtonOneway, radioButtonReturn;
    TextView textViewDepartureAddress, textViewDestinationAddress;
    private Intent google_intent;
    String TAG = "googleplace";
    private MapView mMapView;
    private boolean mapReady = false;
    private LatLng destinationLatLang, sourceLatLng;
    private boolean destinationFlag = false, sourceFlag = false;
    private Place place;
    private List<Polyline> polylines = new ArrayList<>();
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimaryLight, R.color.colorAccent, R.color.primary_dark_material_light};
    private GoogleMap mMap;
    String type = "car";
    ImageView imageViewOneWay, imageViewReturn;
    private boolean returnFlag = true;

    public TypeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_type, container, false);
        context = getActivity();
        mMapView = (MapView) view.findViewById(R.id.mapView);
        context = getActivity();
        try {
            type = this.getArguments().getString("type");
            ////aeroplane,car,bus,ship

        } catch (Exception ex) {
            showMessage("bundle error");
            Log.i("kunsang_exception", "paramenter not yet set");
        }
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        linearLayoutReturn = (LinearLayout) view.findViewById(R.id.linearlayoutReturn);
        textViewReturnETA = (TextView) view.findViewById(R.id.textviewReturnETA);
        textViewReturnETD = (TextView) view.findViewById(R.id.textviewReturnETD);
        textViewDepartureAddress = (TextView) view.findViewById(R.id.textViewDepartureAddress);
        textViewDestinationAddress = (TextView) view.findViewById(R.id.textViewDestinationAddress);
        textViewDestinationAddress.setOnClickListener(this);
        textViewDepartureAddress.setOnClickListener(this);
        imageViewOneWay = (ImageView) view.findViewById(R.id.imageViewOneway);
        imageViewReturn = (ImageView) view.findViewById(R.id.imageViewReturn);
        imageViewOneWay.setOnClickListener(this);
        imageViewReturn.setOnClickListener(this);
        radioButtonOneway = (RadioButton) view.findViewById(R.id.radioButtonOneway);
        radioButtonReturn = (RadioButton) view.findViewById(R.id.radioButtonReturn);
        radioButtonOneway.setOnCheckedChangeListener(this);
        radioButtonReturn.setOnCheckedChangeListener(this);
        radioButtonOneway.setChecked(false);
        radioButtonOneway.setChecked(true);

        rangeBar = (RangeBar) view.findViewById(R.id.rangebar);
        textviewAgeGroup = (TextView) view.findViewById(R.id.textviewAgeGroup);
        buttonCurrency = (Button) view.findViewById(R.id.buttonCurrency);
        buttonCurrency.setOnClickListener(this);
        final String currency[] = {"INR", "USD", "EURO", "YEN"};

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                textviewAgeGroup.setText(leftPinValue + "-" + rightPinValue);
            }
        });
        buttonAddImage = (Button) view.findViewById(R.id.buttonAddImage);
        buttonAddImage.setOnClickListener(this);
        buttonPost = (Button) view.findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(this);
        increamentPass = (Button) view.findViewById(R.id.increament_pass);
        decreamentPass = (Button) view.findViewById(R.id.decreament_pass);
        increamentSpace = (Button) view.findViewById(R.id.increament_Space);
        decreamentSpace = (Button) view.findViewById(R.id.decreament_Space);
        male = (Button) view.findViewById(R.id.maleButton);
        female = (Button) view.findViewById(R.id.femaleButton);
        mixed = (Button) view.findViewById(R.id.mixedButton);
        mixed.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mixed.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        pass_txt = (TextView) view.findViewById(R.id.pass_txt);
        space_txt = (TextView) view.findViewById(R.id.space_txt);
        textviewETA = (TextView) view.findViewById(R.id.textviewETA);
        textViewETD = (TextView) view.findViewById(R.id.textviewETD);
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/GothamRoundedBook.ttf");
        textViewETD.setTypeface(custom_font);
        textviewETA.setOnClickListener(this);
        textViewETD.setOnClickListener(this);
        increamentPass.setOnClickListener(this);
        decreamentPass.setOnClickListener(this);
        increamentSpace.setOnClickListener(this);
        decreamentSpace.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        mixed.setOnClickListener(this);

    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.textViewDestinationAddress:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
                    startActivityForResult(intent, DESTINATION);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }


                break;
            case R.id.textViewDepartureAddress:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
                    startActivityForResult(intent, SOURCE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
            case R.id.buttonAddImage:
                showEditPicPopup();
                break;
            case R.id.buttonPost:
                postTrip();
                break;
            case R.id.textviewETD:
                etdclicked = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        TypeFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");


                break;
            case R.id.textviewETA:
                etdclicked = false;
                Calendar now1 = Calendar.getInstance();
                DatePickerDialog dpd1 = DatePickerDialog.newInstance(
                        TypeFragment.this,
                        now1.get(Calendar.YEAR),
                        now1.get(Calendar.MONTH),
                        now1.get(Calendar.DAY_OF_MONTH)
                );
                dpd1.show(getActivity().getFragmentManager(), "Datepickerdialog");


                break;

            case R.id.increament_pass:

                pass_number = pass_number + 1;
                String p_n = String.valueOf(pass_number);
                pass_txt.setText(p_n);


                break;

            case R.id.decreament_pass:

                if (pass_number >= 1) {
                    pass_number = pass_number - 1;
                    String dp_n = String.valueOf(pass_number);
                    pass_txt.setText(dp_n);
                }
                break;

            case R.id.increament_Space:

                space_number = space_number + 1;
                String sp_n = String.valueOf(space_number);
                space_txt.setText(sp_n);

                break;

            case R.id.decreament_Space:
                if (space_number >= 1) {
                    space_number = space_number - 1;
                    String dsp_n = String.valueOf(space_number);
                    space_txt.setText(dsp_n);
                }

                break;

            case R.id.maleButton:

                female.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image));
                mixed.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image2));
                male.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                male.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                female.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                mixed.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                break;

            case R.id.femaleButton:

                male.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image));
                mixed.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image2));
                female.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                female.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                male.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                mixed.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));


                break;

            case R.id.mixedButton:

                male.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image));
                female.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_image));
                mixed.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                mixed.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                male.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                female.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                break;

            case R.id.linearLayoutPicture:
                takePictureIntent();
                dialog.dismiss();
                break;
            case R.id.linearLayoutGalary:
                pickPictureIntent();
                dialog.dismiss();
                break;
            case R.id.buttonCurrency:
                showCountryPopUp();
                break;
            case R.id.imageViewOneway:
                radioButtonOneway.performClick();
                break;
            case R.id.imageViewReturn:
                radioButtonReturn.performClick();
                break;
        }


    }

    private void postTrip() {
        validateDate();
    }

    private void validateDate() {
        Date etd, eta;
        String date1 = textViewETD.getText().toString();
        String date2 = textviewETA.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yy HH:mm");
        Log.i("datecheck", date1 + ":" + date2);
        try {
            etd = dateFormat.parse(date1);
        } catch (ParseException e) {
            showMessage("Departure date not set");
            return;
        }
        try {
            eta = dateFormat.parse(date2);
        } catch (ParseException e) {
            showMessage("Arival date not set");
            return;
        }
        Log.i("datecheck", etd.toString() + ":" + eta.toString());
        if (etd.after(eta)) {

            showMessage("Arival time should be after Departure time");
        }
    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s";
        if (etdclicked) {
            textViewETD.append(" " + hourString + ":" + minuteString);
        } else {
            textviewETA.append(" " + hourString + ":" + minuteString);
        }
    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Date date2 = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date2 = date_format.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outDate = new SimpleDateFormat("EEE dd MMM yy");

        if (etdclicked) {
            textViewETD.setText(outDate.format(date2));
        } else {
            textviewETA.setText(outDate.format(date2));
        }
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true
        );
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
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
        dialog = new MaterialDialog.Builder(context)
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
        dialog.findViewById(R.id.linearLayoutGalary).setOnClickListener(this);
        dialog.findViewById(R.id.linearLayoutPicture).setOnClickListener(this);

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

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o2);
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

    private void setupSourceDestinationMarker() {
        switch (type) {
            case "aeroplane":
                mode = Routing.TravelMode.TRANSIT;
                break;
            case "car":
                mode = Routing.TravelMode.DRIVING;
                break;
            case "ship":
                mode = Routing.TravelMode.TRANSIT;
                break;
            case "bus":
                mode = Routing.TravelMode.TRANSIT;
                break;
        }
        Routing routing = new Routing.Builder()
                .travelMode(mode)
                .withListener(this)
                .waypoints(sourceLatLng, destinationLatLang)
                .build();
        routing.execute();

        MarkerOptions options = new MarkerOptions();
        options.position(sourceLatLng);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_source_location));
        mMap.addMarker(options);
        options.position(destinationLatLang);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_destination_location));
        mMap.addMarker(options);
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(sourceLatLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        showMessage(center.toString());
        double dummy_radius = distance(sourceLatLng.latitude, sourceLatLng.longitude, destinationLatLang.latitude, destinationLatLang.longitude);
        dummy_radius = dummy_radius / 2;
        double circleRad = dummy_radius * 1000;//multiply by 1000 to make units in KM
        float zoomLevel = getZoomLevel(circleRad);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourceLatLng, zoomLevel));

    }

    private int getZoomLevel(double radius) {
        double scale = radius / 500;
        return ((int) (16 - Math.log(scale) / Math.log(2)));
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Log.i("result picture", "clicked");
                    buttonAddImage.setText(filePath);
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
                    try {
                        buttonAddImage.setText(filePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("result picture", "picked");
                }
                break;
            case DESTINATION:
                destinationFlag = true;
                if (resultCode == getActivity().RESULT_OK) {
                    place = PlaceAutocomplete.getPlace(getApplicationContext(), data);
                    textViewDestinationAddress.setText(place.getName());
                    destinationLatLang = place.getLatLng();
                    if (sourceFlag) {
                        setupSourceDestinationMarker();
                    }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    // TODO: Handle the error.
                    Log.i(TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            case SOURCE:
                sourceFlag = true;
                if (resultCode == RESULT_OK) {
                    place = PlaceAutocomplete.getPlace(getApplicationContext(), data);
                    textViewDepartureAddress.setText(place.getName());
                    sourceLatLng = place.getLatLng();
                    if (destinationFlag) {
                        setupSourceDestinationMarker();
                    }
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    // TODO: Handle the error.
                    Log.i(TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    place = PlaceAutocomplete.getPlace(getActivity(), data);
                    Log.i(TAG, "Place: " + place.getName());
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    // TODO: Handle the error.
                    Log.i(TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void drawPolyLine() {
        arrayPoints.clear();
        arrayPoints.add(sourceLatLng);
        arrayPoints.add(destinationLatLang);
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        polylineOptions.addAll(arrayPoints);
        mMap.addPolyline(polylineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayPoints.get(0), 15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
        drawPolyLine();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }


        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(ContextCompat.getColor(context, COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    public LatLng midPoint(double lat1, double lon1, double lat2, double lon2) {

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        return new LatLng(lat3, lon3);
    }

    private void showCountryPopUp() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getChildFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                try {
                    Currency currency = picker.getCurrencyCode(code);
                    String currencyCode = currency.getCurrencyCode();
                    String currencySymbol = currency.getSymbol();
                    buttonCurrency.setText(currencySymbol + "   " + currencyCode);
                } catch (Exception ex) {
                    showMessage(getString(R.string.currency_not_found));
                }
                picker.dismiss();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.radioButtonOneway:
                showMessage("called radioOneway "+b);
                if(b) {
                    returnFlag = false;
                    linearLayoutReturn.setVisibility(View.GONE);
                    imageViewOneWay.setImageResource(R.drawable.checked);
                    imageViewReturn.setImageResource(R.drawable.unchecked);
                }else {
                    returnFlag = true;
                    linearLayoutReturn.setVisibility(View.VISIBLE);
                    imageViewOneWay.setImageResource(R.drawable.unchecked);
                    imageViewReturn.setImageResource(R.drawable.checked);
                }
                break;
            case R.id.radioButtonReturn:
                showMessage("called radioReturn "+b);

                if(b) {
                    linearLayoutReturn.setVisibility(View.VISIBLE);
                    imageViewOneWay.setImageResource(R.drawable.unchecked);
                    imageViewReturn.setImageResource(R.drawable.checked);
                    returnFlag = true;
                }else {
                    returnFlag = false;
                    linearLayoutReturn.setVisibility(View.GONE);
                    imageViewOneWay.setImageResource(R.drawable.checked);
                    imageViewReturn.setImageResource(R.drawable.unchecked);
                }
                break;

        }
    }
}
