package com.applex.payo_assignment.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.applex.payo_assignment.Adapters.RecyclerAdapter;
import com.applex.payo_assignment.Helpers.RetrofitHelper;
import com.applex.payo_assignment.Models.DataModel;
import com.applex.payo_assignment.Models.UserModel;
import com.applex.payo_assignment.R;
import com.applex.payo_assignment.SharedPreferences.IntroPref;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private IntroPref introPref;
    private ArrayList<UserModel> userModelArrayList, temporaryList;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ProgressBar progress_more;
    private ShimmerFrameLayout shimmerFrameLayout;
    private CoordinatorLayout main_layout;
    private ImageView no_data, error;
    private int checkGetMore = -1;
    private boolean doubleBackPressed = false;
    private final int page = 1;
    private int lastSize;
    private boolean more = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        introPref = new IntroPref(MainActivity.this);
        userModelArrayList = new ArrayList<>();
        temporaryList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recycler_list);
        progress_more = findViewById(R.id.progress_more);
        main_layout = findViewById(R.id.main_layout);
        no_data = findViewById(R.id.no_data);
        error = findViewById(R.id.error);

        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView linkedIn = findViewById(R.id.prajata_samanta_linkedIn);
        linkedIn.setOnClickListener(view -> new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, WebViewActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, 200));

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu_24, getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toggle.setToolbarNavigationClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);
        new Handler().postDelayed(() -> buildRecyclerView(String.valueOf(page)), 1500);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)(v, scrollX, scrollY, oldScrollX, oldScrollY) ->{
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                if((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if(checkGetMore != -1) {
                        if(progress_more.getVisibility() == View.GONE) {
                            checkGetMore = -1;
                            progress_more.setVisibility(View.VISIBLE);
                            fetchMore(String.valueOf(page + 2));
                        }
                    }
                }
            }
        });
    }

    private void buildRecyclerView(String page) {
        Call<DataModel> call1 = RetrofitHelper.getInstance().getApiInterface().getUserList(page);
        call1.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(@NonNull Call<DataModel> call, @NonNull Response<DataModel> response) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);

                ArrayList<UserModel> tempList = new ArrayList<>(response.body().getData());
                if(tempList != null && tempList.size() > 0) {
                    no_data.setVisibility(View.GONE);
                    main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                    Collections.sort(tempList, (userModel1, userModel2) -> {
                        String name1 = userModel1.getFirst_name() + " " + userModel1.getLast_name();
                        String name2 = userModel2.getFirst_name() + " " + userModel2.getLast_name();
                        return name1.compareTo(name2);
                    });
                    userModelArrayList.addAll(tempList);

                    Call<DataModel> call2 = RetrofitHelper.getInstance().getApiInterface().getUserList(String.valueOf(Integer.parseInt(page) + 1));
                    call2.enqueue(new Callback<DataModel>() {
                        @Override
                        public void onResponse(@NonNull Call<DataModel> call, @NonNull Response<DataModel> response) {
                            ArrayList<UserModel> tempList = new ArrayList<>(response.body().getData());
                            if(tempList != null && tempList.size() > 0) {
                                more = true;
                                lastSize = userModelArrayList.size();

                                Collections.sort(tempList, (userModel1, userModel2) -> {
                                    String name1 = userModel1.getFirst_name() + " " + userModel1.getLast_name();
                                    String name2 = userModel2.getFirst_name() + " " + userModel2.getLast_name();
                                    return name1.compareTo(name2);
                                });
                                userModelArrayList.add(lastSize, tempList.get(0));
                                userModelArrayList.add(lastSize + 1, tempList.get(1));
                                temporaryList.addAll(tempList);

                                recyclerAdapter = new RecyclerAdapter(userModelArrayList);
                                mRecyclerView.setAdapter(recyclerAdapter);

                                if(userModelArrayList.size() < 8) {
                                    checkGetMore = -1;
                                } else {
                                    checkGetMore = 0;
                                }

                                recyclerAdapter.setOnItemLongClickListener(position -> {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Delete this item")
                                            .setMessage("Are you sure?")
                                            .setPositiveButton("Delete", (dialog, which) -> {
                                                userModelArrayList.remove(position);
                                                recyclerAdapter.notifyItemRemoved(position);
                                            })
                                            .setNegativeButton("Cancel",null)
                                            .setCancelable(true);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                });
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DataModel> call, @NonNull Throwable t) {
                            Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if(more) {
                        more = false;
                    }
                    else {
                        recyclerAdapter = new RecyclerAdapter(userModelArrayList);
                        mRecyclerView.setAdapter(recyclerAdapter);

                        recyclerAdapter.setOnItemLongClickListener(position -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Delete this item")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Delete", (dialog, which) -> {
                                        userModelArrayList.remove(position);
                                        recyclerAdapter.notifyItemRemoved(position);
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .setCancelable(true);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        });
                    }
                }
                else {
                    checkGetMore = -1;
                    if(userModelArrayList.size() < 8) {
                        no_data.setVisibility(View.VISIBLE);
                        main_layout.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataModel> call, @NonNull Throwable t) {
                main_layout.setBackgroundColor(getResources().getColor(R.color.white));
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMore(String page) {
        progress_more.setVisibility(View.VISIBLE);
        for(int i = 2; i < temporaryList.size(); i++) {
            userModelArrayList.add(temporaryList.get(i));
        }
        new Handler().postDelayed(() -> {
            recyclerAdapter.notifyItemRangeInserted(lastSize + 2, temporaryList.size() - 2);
            progress_more.setVisibility(View.GONE);
            recyclerAdapter.setOnItemLongClickListener(position -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete this item")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            userModelArrayList.remove(position);
                            recyclerAdapter.notifyItemRemoved(position);
                        })
                        .setNegativeButton("Cancel",null)
                        .setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });
            buildRecyclerView(page);
        }, 200);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            if(doubleBackPressed) {
                super.onBackPressed();
                return;
            }
            doubleBackPressed = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackPressed = false, 3000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profile) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }, 200);
        }
        else if(id == R.id.logout) {
            new Handler().postDelayed(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Log out")
                        .setMessage("Do you want to continue?")
                        .setPositiveButton("Log out", (dialog, which) -> {
                            introPref.setIsLoggedIn(false);
                            introPref.setAddress(null);
                            introPref.setContact(null);
                            introPref.setName(null);
                            introPref.setEmail(null);
                            introPref.setPassword(null);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setCancelable(true)
                        .show();
            }, 200);
        }
        return true;
    }
}