package com.gnuarmap.mixare.app;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.gnuarmap.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nazunamoe on 2017-12-01.
 */

public class Location {
    /**
     * This class is repsonsible for finding the location, and sending it back to
     * the mixcontext.
     */
    public static interface LocationFinder {

        /**
         * Possible status of LocationFinder
         */
        public enum LocationFinderState {
            Active, // Providing Location Information
            Inactive, // No-Active
            Confused // Same problem in internal state
        }

        /**
         * Finds the location through the providers
         * @return
         */
        void findLocation();

        /**
         * A working location provider has been found: check if
         * the found location has the best accuracy.
         */
        void locationCallback(String provider);

        /**
         * Returns the current location.
         */
        android.location.Location getCurrentLocation();

        /**
         * Gets the location that was used in the last download for
         * datasources.
         * @return
         */
        android.location.Location getLocationAtLastDownload();

        /**
         * Sets the property to the location with the last successfull download.
         */
        void setLocationAtLastDownload(android.location.Location locationAtLastDownload);

        /**
         * Request to active the service
         */
        void switchOn();

        /**
         * Request to deactive the service
         */
        void switchOff();

        /**
         * Status of service
         *
         * @return
         */
        LocationFinderState getStatus();

        /**
         *
         * @return GeomagneticField
         */
        GeomagneticField getGeomagneticField();

    }

    /**
     * Factory Of  LocationFinder
     *
     */
    public static class LocationFinderFactory {

        /**
         * Hide implementation Of LocationFinder
         * @param mixContext
         * @return LocationFinder
         */
        public static LocationFinder makeLocationFinder(MixContext mixContext){
            return new LocationMgrImpl(mixContext);
        }

    }

    /**
     * This class is repsonsible for finding the location, and sending it back to
     * the mixcontext.
     *
     * @author A. Egal
     */
    static class LocationMgrImpl implements LocationFinder {

        private LocationManager lm;
        private String bestLocationProvider;
        private final MixContext mixContext;
        private android.location.Location curLoc;
        private android.location.Location locationAtLastDownload;
        private LocationFinderState state;
        private final LocationObserver lob;
        private List<LocationResolver> locationResolvers;

        // frequency and minimum distance for update
        // this values will only be used after there's a good GPS fix
        // see back-off pattern discussion
        // http://stackoverflow.com/questions/3433875/how-to-force-gps-provider-to-get-speed-in-android
        // thanks Reto Meier for his presentation at gddde 2010
        private final long freq = 5000; // 5 seconds
        private final float dist = 20; // 20 meters

        public LocationMgrImpl(MixContext mixContext) {
            this.mixContext = mixContext;
            this.lob = new LocationObserver(this);
            this.state = LocationFinderState.Inactive;
            this.locationResolvers = new ArrayList<LocationResolver>();
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * LocationFinder#findLocation(android.content.Context
         * )
         */
        @SuppressWarnings({"ResourceType"})
        public void findLocation() {

            // fallback for the case where GPS and network providers are disabled
            android.location.Location hardFix = new android.location.Location("reverseGeocoded");

            // Frangart, Eppan, Bozen, Italy
            hardFix.setLatitude(46.480302);
            hardFix.setLongitude(11.296005);
            hardFix.setAltitude(300);

            try {
                requestBestLocationUpdates();
                //temporary set the current location, until a good provider is found
                curLoc = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), true));
            } catch (Exception ex2) {
                // ex2.printStackTrace();
                curLoc = hardFix;
                mixContext.doPopUp(R.string.connection_GPS_dialog_text);

            }
        }
        @SuppressWarnings({"ResourceType"})
        private void requestBestLocationUpdates() {
            Timer timer = new Timer();
            for (String p : lm.getAllProviders()) {
                if(lm.isProviderEnabled(p)){
                    LocationResolver lr = new LocationResolver(lm, p, this);
                    locationResolvers.add(lr);
                    lm.requestLocationUpdates(p, 0, 0, lr);
                }
            }
            timer.schedule(new LocationTimerTask(),20* 1000); //wait 20 seconds for the location updates to find the location
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * LocationFinder#locationCallback(android.content
         * .Context)
         */
        @SuppressWarnings({"ResourceType"})
        public void locationCallback(String provider) {
            android.location.Location foundLocation = lm.getLastKnownLocation(provider);
            if (bestLocationProvider != null) {
                android.location.Location bestLocation = lm
                        .getLastKnownLocation(bestLocationProvider);
                if (foundLocation.getAccuracy() < bestLocation.getAccuracy()) {
                    curLoc = foundLocation;
                    bestLocationProvider = provider;
                }
            } else {
                curLoc = foundLocation;
                bestLocationProvider = provider;
            }
            setLocationAtLastDownload(curLoc);
        }

        /*
         * (non-Javadoc)
         *
         * @see LocationFinder#getCurrentLocation()
         */
        public android.location.Location getCurrentLocation() {
            if (curLoc == null) {
                MixView mixView = mixContext.getActualMixView();
                throw new RuntimeException("No GPS Found");
            }
            synchronized (curLoc) {
                return curLoc;
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see LocationFinder#getLocationAtLastDownload()
         */
        public android.location.Location getLocationAtLastDownload() {
            return locationAtLastDownload;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * LocationFinder#setLocationAtLastDownload(android
         * .location.Location)
         */
        public void setLocationAtLastDownload(android.location.Location locationAtLastDownload) {
            this.locationAtLastDownload = locationAtLastDownload;
        }

        /*
         * (non-Javadoc)
         *
         * @see LocationFinder#getGeomagneticField()
         */
        public GeomagneticField getGeomagneticField() {
            android.location.Location location = getCurrentLocation();
            GeomagneticField gmf = new GeomagneticField(
                    (float) location.getLatitude(),
                    (float) location.getLongitude(),
                    (float) location.getAltitude(), System.currentTimeMillis());
            return gmf;
        }

        public void setPosition(android.location.Location location) {
            synchronized (curLoc) {
                curLoc = location;
            }
            mixContext.getActualMixView().refresh();
            android.location.Location lastLoc = getLocationAtLastDownload();
            if (lastLoc == null) {
                setLocationAtLastDownload(location);
            }
        }

        @Override
        public void switchOn() {
            if (!LocationFinderState.Active.equals(state)) {
                lm = (LocationManager) mixContext
                        .getSystemService(Context.LOCATION_SERVICE);
                state = LocationFinderState.Confused;
            }
        }

        @Override
        public void switchOff() {
            if (lm != null) {
                lm.removeUpdates(getObserver());
                state = LocationFinderState.Inactive;
            }
        }

        @Override
        public LocationFinderState getStatus() {
            return state;
        }

        private synchronized LocationObserver getObserver() {
            return lob;
        }

        class LocationTimerTask extends TimerTask {

            @Override
            @SuppressWarnings({"ResourceType"})
            public void run() {
                //remove all location updates
                for(LocationResolver locationResolver: locationResolvers){
                    lm.removeUpdates(locationResolver);
                }
                if(bestLocationProvider != null){
                    lm.removeUpdates(getObserver());
                    state=LocationFinderState.Confused;
                    mixContext.getActualMixView().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lm.requestLocationUpdates(bestLocationProvider, freq, dist, getObserver());
                        }
                    });
                    state=LocationFinderState.Active;
                }
                else{ //no location found
                    mixContext.getActualMixView().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });

                }


            }

        }

    }

    static class LocationObserver implements LocationListener {
        private LocationMgrImpl myController;

        public LocationObserver(LocationMgrImpl myController) {
            super();
            this.myController=myController;
        }

        public void onLocationChanged(android.location.Location location) {
            Log.d(MixContext.TAG, "Normal Location Changed: " + location.getProvider()
                            + " lat: " + location.getLatitude() + " lon: "
                            + location.getLongitude() + " alt: "
                            + location.getAltitude() + " acc: "
                            + location.getAccuracy());
            try {
                addWalkingPathPosition(location);
                Log.v(MixContext.TAG, "Location Changed: " + location.getProvider()
                                + " lat: " + location.getLatitude() + " lon: "
                                + location.getLongitude() + " alt: "
                                + location.getAltitude() + " acc: "
                                + location.getAccuracy());
                myController.setPosition(location);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void addWalkingPathPosition(android.location.Location location) {
            //MixMap.addWalkingPathPosition(new GeoPoint((int) (location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6)));
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    /**
     * This class will be used to start each location provider for 20 seconds
     * and they will then listen for locations. This class will check for updates for
     * the observer.
     * Using this method: http://stackoverflow.com/questions/3145089/
     * @author A. Egal
     */
    public static class LocationResolver implements LocationListener{

        private String provider;
        private LocationMgrImpl locationMgrImpl;
        private LocationManager lm;

        public LocationResolver(LocationManager lm, String provider, LocationMgrImpl locationMgrImpl){
            this.lm = lm;
            this.provider = provider;
            this.locationMgrImpl = locationMgrImpl;
        }

        @Override
        public void onLocationChanged(android.location.Location location) {
            lm.removeUpdates(this);
            locationMgrImpl.locationCallback(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }
}
