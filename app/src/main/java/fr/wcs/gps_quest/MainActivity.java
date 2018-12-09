package fr.wcs.gps_quest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected LocationManager locationManager=null;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // checkPermission();

    }
    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null!=locationListener) locationManager.removeUpdates(locationListener);
        locationManager=null;



    }


    @SuppressLint("MissingPermission")
    private void initLocation() {

        Toast.makeText(
                MainActivity.this,
                getString(R.string.inPreparation),
                Toast.LENGTH_SHORT)
                .show();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Toast.makeText(MainActivity.this,
                        String.format(
                                getString(R.string.getLocation),
                                String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude())),
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
           /*      switch (status){
                     case LocationProvider.TEMPORARILY_UNAVAILABLE:  Toast.makeText(
                             MainActivity.this,
                             getString(R.string.badGpsSignal),
                             Toast.LENGTH_SHORT)
                             .show();
                            break;
                     case LocationProvider.OUT_OF_SERVICE: onProviderDisabled(provider); break;
                     default:
                         break;
                 }

            */

            }

            @Override
            public void onProviderEnabled(String provider) {

                Toast.makeText(MainActivity.this, getString(R.string.yesGpsActivated), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(MainActivity.this, getString(R.string.noGpsActivated), Toast.LENGTH_SHORT).show();
            }

        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1,locationListener);
    }

    private void checkPermission() {

        // vérification de l'autorisation d'accéder à la position GPS
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // l'autorisation n'est pas acceptée


            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // l'autorisation a été refusée précédemment, on peut prévenir l'utilisateur ici
                Toast.makeText(this, getString(R.string.noGpsAutorisation), Toast.LENGTH_SHORT).show();



            } else {

                // l'autorisation n'a jamais été réclamée, on la demande à l'utilisateur

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);
            }
        } else {

            // autorisation déjà acceptée, on peut faire une action ici

            initLocation();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {

                // cas de notre demande d'autorisation

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // l'autorisation a été donnée, nous pouvons agir
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.inPreparation),
                            Toast.LENGTH_LONG)
                            .show();
                    initLocation();

                } else {

                    // l'autorisation a été refusée :(
                    Toast.makeText(this, getString(R.string.noGpsAutorisation), Toast.LENGTH_SHORT).show();


                }
                return;
            }
        }
    }





}
