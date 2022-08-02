package com.example.farmadminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmadminpanel.Api.AppConfig;
import com.example.farmadminpanel.Fragment.AdvertismentFragment;
import com.example.farmadminpanel.Fragment.GardenFragment;
import com.example.farmadminpanel.Fragment.HomeFragment;
import com.example.farmadminpanel.Fragment.HoneyFragment;
import com.example.farmadminpanel.Fragment.ManageProfileFragment;
import com.example.farmadminpanel.Fragment.SchemeFragment;
import com.example.farmadminpanel.Fragment.VideoFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private AppConfig appConfig;
    //ApiInterface apiInterface;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private Dialog dialog;
    private Button btn_yes, btn_no;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        //Toolbar set
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//Remove title from toolbar

//Drawer
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this); //Make Drawer clickable
        replaceFragment(new HomeFragment());//Default Fragment
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id = item.getItemId();
                item.setChecked(true); //Highlight
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {
                    case R.id.nav_home:
                        replaceFragment(new HomeFragment());
                        break;

                    case R.id.nav_adver:
                        replaceFragment(new AdvertismentFragment());
                        break;

                    case R.id.nav_manprofile:
                        replaceFragment(new ManageProfileFragment());
                        break;

                    case R.id.nav_video:
                        replaceFragment(new VideoFragment());
                        break;

                    case R.id.nav_garden:
                        replaceFragment(new GardenFragment());
                        break;

                    case R.id.nav_honey:
                        replaceFragment(new HoneyFragment());
                        break;

                    case R.id.nav_scheme:
                        replaceFragment(new SchemeFragment());
                        break;

                    case R.id.nav_logout:
                        showDialog();
                        break;

                    default:
                        return true;

                }
                return true;
            }
        });

    }

    private void showDialog()
    {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog);
        btn_yes = dialog.findViewById(R.id.btn_logout_yes);
        btn_no = dialog.findViewById(R.id.btn_logout_no);
        btn_yes.setText("Logout");
        btn_no.setText("Cancel");
        TextView text_msg = (TextView) dialog.findViewById(R.id.text_msg);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_image);
        iv_image.setImageDrawable(getResources().getDrawable(R.drawable.logout));
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text_msg.setText("Are you sure you want to Logout");
        text.setText("Logout...!");
        appConfig = new AppConfig(this);
        appConfig = new AppConfig(this);
        btn_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                appConfig.updateUserLogin(false);
//                startActivity(new Intent(MainActivity.this,UserLogin.class));
//                finish();
                Toast.makeText(MainActivity.this, "Logout Successfully...", Toast.LENGTH_SHORT).show();

            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            replaceFragment(new HomeFragment());

            if (doubleBackToExitPressedOnce)
            {
                //super.onBackPressed();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                } else {
                    System.exit(0);
                }
                return;
            }

            this.doubleBackToExitPressedOnce = true;


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        return true;
    }


}