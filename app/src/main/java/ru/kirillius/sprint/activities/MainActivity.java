package ru.kirillius.sprint.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.activities.fragments.AllTasksFragment;
import ru.kirillius.sprint.activities.fragments.ScheduleEventFragment;
import ru.kirillius.sprint.activities.fragments.TodayTasksFragment;
import ru.kirillius.sprint.service.CommonHelper;
import ru.kirillius.sprint.service.RequestHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    TodayTasksFragment todayTasksFragment;
    ScheduleEventFragment scheduleEventFragment;
    AllTasksFragment allTasksFragment;
    ViewPager vpMainActivity;
    TabLayout tabLayout;
    ViewPagerAdapter adapterVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = MainActivity.this;
        todayTasksFragment = new TodayTasksFragment();
        scheduleEventFragment = new ScheduleEventFragment();
        allTasksFragment = new AllTasksFragment();

        vpMainActivity = (ViewPager) findViewById(R.id.vpMainActivity);
        setupViewPager(vpMainActivity);
        vpMainActivity.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vpMainActivity);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        CommonHelper.setStatusBarColor(this);
        setTitle(getResources().getString(R.string.MainActivityTitle));
        //new RequestHelper(context).executeGetRequest("boards/Bp4UKfQC/lists", null, null);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapterVP = new ViewPagerAdapter(getSupportFragmentManager());
        adapterVP.addFragment(todayTasksFragment, "Задачи на сегодня");
        adapterVP.addFragment(scheduleEventFragment, "События");
        adapterVP.addFragment(allTasksFragment, "Все задачи спринта");
        viewPager.setAdapter(adapterVP);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = CommonHelper.onClickMainMenu(id, context);

        if(intent!=null) {
            startActivity(intent);
            if(intent.getBooleanExtra("finish", false))
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
