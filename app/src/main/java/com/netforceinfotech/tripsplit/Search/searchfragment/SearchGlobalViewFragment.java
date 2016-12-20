package com.netforceinfotech.tripsplit.Search.searchfragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.netforceinfotech.tripsplit.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGlobalViewFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, RoutingListener {

    private GoogleMap mMap;
    private MaterialDialog progressDialog;
    Context context;
    ArrayList<CarData> carDatas = new ArrayList<>();
    private MapView mMapView;
    private boolean mapReady = false;
    private float zoomlevel;
    private Marker sourceMarker;
    private CameraUpdate cu;

    public SearchGlobalViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_global_view, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        context = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        progressDialog = new MaterialDialog.Builder(context)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0).build();
    }

    private void searchTrip() {
        /*
        * source_lat=55.946302847171445&source_lon=-3.1891679763793945
        * &dest_lat=28.599072519302414&dest_lon=77.32198219746351&etd=2016-05-11&range=4000&type=car
        * */
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("dest_lat", "28.599072519302414");
        json.addProperty("dest_lon", "77.32198219746351");
        json.addProperty("source_lat", "55.946302847171445");
        json.addProperty("source_lon", "-3.1891679763793945");
        json.addProperty("etd", "2016-05-11");
        json.addProperty("range", "4000");
        json.addProperty("sort", "sort");
        json.addProperty("type", "car");
        String baseUrl = getString(R.string.url);
        //http://netforce.biz/tripesplit/mobileApp/api/services.php?opt=search_trip
        String url = baseUrl + "services.php?opt=search_trip";
        Log.i("url", url);
        Log.i("type1", "type");
        Ion.with(context)
                .load("POST", url)
                .setBodyParameter("dest_lat", 28.599072519302414 + "")
                .setBodyParameter("dest_lon", 77.32198219746351 + "")
                .setBodyParameter("source_lat", 55.946302847171445 + "")
                .setBodyParameter("source_lon", -3.1891679763793945 + "")
                .setBodyParameter("etd", "2016-05-11")
                .setBodyParameter("range", "4000")
                .setBodyParameter("sort", "")
                .setBodyParameter("type", "car")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        progressDialog.dismiss();
                        // do stuff with the result or error
                        if (result == null) {
                            showMessage("something wrong");
                        } else {
                            if (result.get("status").getAsString().equalsIgnoreCase("success")) {
                                Log.i("result", result.toString());

                                JsonArray data = result.getAsJsonArray("data");
                                setupData(data);
                            }

                        }
                    }
                });
    }

    private void setupData(JsonArray data) {
        int size = data.size();
        if (size == 0) {
            showMessage(getString(R.string.no_split_found));
        }
        for (int i = 0; i < size; i++) {
            try {
                JsonObject object = data.get(i).getAsJsonObject();
                String tour_id = object.get("tour_id").getAsString();
                String start_price = object.get("start_price").getAsString();
                String date_created = object.get("date_created").getAsString();
                String user_id = object.get("user_id").getAsString();
                String cartype = object.get("cartype").getAsString();
                String pax = object.get("pax").getAsString();
                String space = object.get("space").getAsString();
                String trip_group = object.get("trip_group").getAsString();
                String age_group_lower = object.get("age_group_lower").getAsString();
                String age_group_upper = object.get("age_group_upper").getAsString();
                String trip = object.get("trip").getAsString();
                String vehical_type = object.get("vehical_type").getAsString();
                String depart_address = object.get("depart_address").getAsString();
                String country_code = object.get("country_code").getAsString();
                String etd = object.get("etd").getAsString();
                String eta = object.get("eta").getAsString();
                String iteinerary = object.get("iteinerary").getAsString();
                String image = object.get("image").getAsString();
                String currency = object.get("currency").getAsString();
                String depart_lat = object.get("depart_lat").getAsString();
                String depart_lon = object.get("depart_lon").getAsString();
                String return_eta = object.get("return_eta").getAsString();
                String return_etd = object.get("return_etd").getAsString();
                String dest_address = object.get("dest_address").getAsString();
                String dest_lat = object.get("dest_lat").getAsString();
                String dest_lon = object.get("dest_lon").getAsString();
                CarData carData = new CarData(tour_id, start_price, date_created, user_id, cartype, pax, space, trip_group, age_group_lower, age_group_upper, trip, vehical_type, depart_address, country_code, etd, eta, iteinerary, image, currency, depart_lat, depart_lon, return_eta, return_etd, dest_address, dest_lat, dest_lon);
                if (!carDatas.contains(carData)) {
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_source);

                    sourceMarker = mMap.addMarker(new MarkerOptions().snippet(depart_address).icon(icon).title(tour_id).position(new LatLng(Double.parseDouble(depart_lat), Double.parseDouble(depart_lon))));
                    carDatas.add(carData);
                }
            } catch (Exception ex) {
                Log.i("position", i + "");
                ex.printStackTrace();
            }
        }
        setupbound(carDatas);

    }

    private void setupbound(ArrayList<CarData> carDatas) {
       /* LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int size = carDatas.size();
        for (int i = 0; i < size; i++) {
            CarData carData = carDatas.get(i);
            builder.include(new LatLng(Double.parseDouble(carData.depart_lat), Double.parseDouble(carData.depart_lon)));
        }
        LatLngBounds bounds = builder.build();
        int padding = 10; // offset from edges of the map in pixels
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);
            }
        });
*/
        LatLng searchLatLng = new LatLng(55.946302847171445, -3.1891679763793945);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLatLng, 17));

        mMap.addCircle(new CircleOptions()
                .center(searchLatLng)
                .radius(3 * 1000)
                .strokeWidth(0f)
                .fillColor(ContextCompat.getColor(context, R.color.greentranparent)));

    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                zoomlevel = mMap.getCameraPosition().zoom;
            }
        });
        searchTrip();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showMessage(marker.getTitle());
                return false;
            }
        });
    }
}
