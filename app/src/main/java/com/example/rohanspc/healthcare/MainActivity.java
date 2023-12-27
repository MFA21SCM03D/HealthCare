package com.example.rohanspc.healthcare;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.rohanspc.healthcare.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mRef;
    private CollectionReference usersCollection;
    private static final String TAG = "MainActivity";

    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ViewPager viewpager;
    private DrawerLayout drawerLayout;
    private TextView drawer_username,drawer_email;
   // private Button btnSignout;
    private User user_info;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user_info = new User();

        mRef = FirebaseFirestore.getInstance();
        usersCollection = mRef.collection("Users");



        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView)findViewById(R.id.drawer);
        View headerView = navigationView.getHeaderView(0);
        drawer_username = headerView.findViewById(R.id.drawer_username);
        drawer_email = headerView.findViewById(R.id.drawer_email);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        FloatingActionButton floatbtn = (FloatingActionButton)findViewById(R.id.floatbtn);

        mAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth();

        tabLayout = (TabLayout)findViewById(R.id.tab);
        viewpager = (ViewPager)findViewById(R.id.view);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragments(new FragmentNearBy(),"NearBy");
        adapter.AddFragments(new FragmentHome(),"Home");
        adapter.AddFragments(new FragmentMaps(),"Appointments");
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);

        viewpager.setCurrentItem(1);

       floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });
    }


    private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: Signed in");
                    getData(user);


                }
                else{
                    Log.d(TAG, "onAuthStateChanged: Signed out");
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void getData(FirebaseUser user1){
        usersCollection.document(user1.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    user_info = task.getResult().toObject(User.class);
                    Log.d(TAG, "onComplete: " + user_info.getName());
                    drawer_username.setText(user_info.getName());
                    drawer_email.setText(user_info.getEmail());
                }
                else{
                    Log.d(TAG, "onComplete: failed to retrieve data");
                }
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        closeDrawer();
        
        switch(item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                finish();
                break;
            case R.id.home:
                viewpager.setCurrentItem(1);
                break;
            case R.id.near_by:
                viewpager.setCurrentItem(0);
                break;
            case R.id.appointments:
                viewpager.setCurrentItem(2);
                break;
            default:
                Log.d(TAG, "onNavigationItemSelected: default");
                break;
        }
        return true;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void openDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            closeDrawer();
        }
        super.onBackPressed();
    }
}
