package edu.utexas.cs.thunt.fcmaps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import com.google.android.gms.maps.model.CameraPosition;
import android.os.Build;
/**
 * Created by thunt on 10/25/16.
 * Holds Maps...What else would it do?
 */

public class MapHolder implements OnMapReadyCallback {
    /* Some from RedFetch some from this example:
    http://theoryapp.com/parse-json-in-java/
     */

    public class NewClass implements NameToLatLngTask.OnLatLngCallback
    {
        @Override
        public void onLatLng(LatLng a) {

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(a)
                    .zoom(15)
                    .bearing(90)
                    .tilt(30)
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
            gMap.setTrafficEnabled(true);
            gMap.getUiSettings().setZoomControlsEnabled( true );

            gMap.clear();
            gMap.addMarker(new MarkerOptions().position(a));

        }
    }
    private static class NameToLatLngTask extends AsyncTask<String, Object, LatLng> {


        public interface OnLatLngCallback {
            public void onLatLng(LatLng a);


        }

        OnLatLngCallback cb;
        Context context;

        URL geocoderURLBuilder(String address) {
            URL result = null;
            final String base = "https://maps.googleapis.com/maps/api/geocode/json?key=";
            try {
                result = new URL(base + context.getResources().getString(R.string.google_maps_key)
                        + "&address=" + URLEncoder.encode(address, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("Geocoder", "Encoding address: " + e.toString());
            } catch (MalformedURLException e) {
                Log.e("Geocoder", "Building URL: " + e.toString());
            }
            return result;
        }

        public NameToLatLngTask(Context ctx, String addr, OnLatLngCallback _cb) {
            context = ctx;
            execute(addr);
            cb = _cb;
        }

        protected LatLng latLngFromJsonString(String json) throws JSONException {
            JSONObject obj = new JSONObject(json);
            LatLng result = null;
            if (!obj.getString("status").equals("OK")) {
                Log.e("URLfetch", "returned status" + obj.getString("status"));
            } else {
                JSONObject loc = obj.getJSONArray("results").getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                double lat = loc.getDouble("lat");
                double lng = loc.getDouble("lng");
                result = new LatLng(lat, lng);
                Log.d("Geocoder", "got lat: " + lat + ", lng: " + lng);
            }
            return result;
        }

        @Override
        protected LatLng doInBackground(String... params) {
            assert (params.length > 1);
            String name = params[0];
            URL url;
            LatLng pos = null;

            /* Try Geocoder first */
            {
                Geocoder geo = new Geocoder(context);

                List<Address> addressList = null;

                try
                {
                    addressList = geo.getFromLocationName(params[0], 5);
                    if (params[0] == null)
                    {
                        return pos;
                    }

                    Address addr0 = addressList.get(0);

                    pos = new LatLng(addr0.getLatitude(), addr0.getLongitude());

                    if (pos != null)
                        return pos;
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

                /* XXX write me
                    Use the Geocoder object for fast(er) geocoding first
                 */

            }

            /* go remote as a last resort*/
            url = geocoderURLBuilder(name);
            if (url == null) {
                cancel(true);
                return null;
            }

            try {
                String result = null;
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.connect();

                if (urlConn.getContentType().startsWith("application/json"))
                    result = fetchJson(urlConn);
                else
                    Log.e("URLfetch", "Result has bad type (not json)");

                if (result != null)
                    pos = latLngFromJsonString(result);
            } catch (IOException e) {
                Log.e("URLfetch", e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("JsonBuild", "JSON malformed");
            }

            if (pos == null) {
                cancel(false);
            }
            return pos;
        }

        protected String readStreamToString(InputStream in) throws IOException {
            int numRead;
            final int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            ByteArrayOutputStream outString = new ByteArrayOutputStream();

            while ((numRead = in.read(buffer)) != -1) {
                outString.write(buffer, 0, numRead);
                if (isCancelled()) {
                    return null;
                }
            }
            return new String(outString.toByteArray(), "UTF-8");
        }

        protected String fetchJson(HttpURLConnection conn) {
            InputStream in = null;
            String result = null;
            try {
                in = new BufferedInputStream(conn.getInputStream());
                result = readStreamToString(in);
                Log.d("fetchJson", "json " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(LatLng result) {
            cb.onLatLng(result);
        }

        @Override
        protected void onCancelled(LatLng result) {
            Log.e("NameToLatLng", "cancelled");
            cb.onLatLng(null);
        }
    }


    private GoogleMap gMap;
    private Context context;

    public MapHolder(Context ctx) {
        context = ctx;
    }

    public boolean warnIfNotReady() {
        if (gMap == null) {
            Toast.makeText(context, "No map yet.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }

    public void showAddress(String address) {
        if (warnIfNotReady())
            return;

        //gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        NewClass nc = new NewClass();
        NameToLatLngTask ntllt = new NameToLatLngTask(context,address, nc);

        /* XXX write me */
    }
}
