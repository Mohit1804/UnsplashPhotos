package com.example.sunbaseTask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sending data via bundle to fragments to tell availability of network
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean msg= (activeNetworkInfo != null && activeNetworkInfo.isConnected());

        Bundle bundle = new Bundle();
        if(msg) {
            bundle.putString("msg", "true");
        }else {

            bundle.putString("msg", "false");
        }
//        SearchFragment fragObj = new SearchFragment();
//        fragObj.setArguments(bundle);

        //we have passed the message


        bottomNavigationView=findViewById(R.id.bottomNavigation);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        selectedFragment=new HomeFragment();
                        break;
                    case R.id.menu_search:
                        selectedFragment=new SearchFragment();
                        selectedFragment.setArguments(bundle);
                        break;
                    default:
                        selectedFragment=new HomeFragment();
                }

                if(selectedFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                            selectedFragment).commit();
                }
                return true;
            }
        });
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}