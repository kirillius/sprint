package ru.kirillius.sprint.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.activities.fragments.AnalyticsFragment;
import ru.kirillius.sprint.activities.fragments.ScheduleEventFragment;
import ru.kirillius.sprint.activities.fragments.TodayTasksFragment;
import ru.kirillius.sprint.interfaces.OnCompleteAction;
import ru.kirillius.sprint.interfaces.OnCompleteRequest;
import ru.kirillius.sprint.interfaces.OnSaveTask;
import ru.kirillius.sprint.interfaces.OnSaveTimesTask;
import ru.kirillius.sprint.interfaces.OnSelectTask;
import ru.kirillius.sprint.models.Tasks;
import ru.kirillius.sprint.service.CommonHelper;
import ru.kirillius.sprint.service.NotificationsHelper;
import ru.kirillius.sprint.service.RequestHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnSelectTask {

    Context context;
    TodayTasksFragment todayTasksFragment;
    ScheduleEventFragment scheduleEventFragment;
    AnalyticsFragment analyticsFragment;
    ViewPager vpMainActivity;
    TabLayout tabLayout;
    ViewPagerAdapter adapterVP;
    FloatingActionButton fab;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

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
        analyticsFragment = new AnalyticsFragment();
        fab = findViewById(R.id.fab);
        fab.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationsHelper.DialogTask(context, getLayoutInflater(), "Создание задачи", null, new OnSaveTask() {
                    @Override
                    public void onSaveTask(Tasks task) {
                        RequestHelper requestHelper = new RequestHelper(context);
                        requestHelper.executePostRequest("/api/tasks",  gson.toJson(task), new OnCompleteRequest() {
                            @Override
                            public void onComplete(String json) {
                                todayTasksFragment.addLabelToList(gson.fromJson(json, Tasks.class));
                            }
                        });
                    }

                    @Override
                    public void onNotSaveTask() {

                    }
                });
            }
        });

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
    }

    private void setupViewPager(ViewPager viewPager) {
        adapterVP = new ViewPagerAdapter(getSupportFragmentManager());
        adapterVP.addFragment(analyticsFragment, "Аналитика");
        adapterVP.addFragment(todayTasksFragment, "Задачи");
        //adapterVP.addFragment(scheduleEventFragment, "События");
        viewPager.setAdapter(adapterVP);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fab.hide();
                        break;
                    case 1:
                        fab.show();
                        break;
                    default:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    @Override
    public void onSelectTask(final Tasks task) {
        //запросим с сервера информацию о статусе задачи (в работе или нет)
        RequestHelper requestHelper = new RequestHelper(context);
        HashMap<String, String> params = new HashMap<>();
        params.put("taskId", String.valueOf(task.id));
        requestHelper.executeGetRequest("/api/getStatusTask?"+CommonHelper.mapToString(params), params , new OnCompleteRequest() {
            @Override
            public void onComplete(String json) {
                JSONObject result = null;
                try {
                    result = new JSONObject(json);
                    boolean startFlag = result.get("status").equals("taskStoped");
                    NotificationsHelper.DialogTimes(context, getLayoutInflater(), "Время работы над задачей", task, startFlag, new OnSaveTimesTask() {
                        @Override
                        public void onSaveTimesTask(final Tasks task, final boolean start) {
                            //отправим данные на сервер
                            RequestHelper requestHelper = new RequestHelper(context);
                            HashMap<String, String> params = new HashMap<>();
                            params.put("taskId", String.valueOf(task.id));
                            params.put("start", String.valueOf(start));
                            requestHelper.executeGetRequest("/api/startStopTask?"+CommonHelper.mapToString(params), params , new OnCompleteRequest() {
                                @Override
                                public void onComplete(String json) {
                                    String message = start ? "Задача успешно начата" : "Задача успешно закончена";
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLongSelectTask(Tasks task) {
        NotificationsHelper.DialogTask(context, getLayoutInflater(), "Редактирование задачи", task, new OnSaveTask() {
            @Override
            public void onSaveTask(Tasks task) {
                RequestHelper requestHelper = new RequestHelper(context);
                requestHelper.executePutRequest("/api/tasks/"+task.id,  gson.toJson(task), new OnCompleteRequest() {
                    @Override
                    public void onComplete(String json) {
                        todayTasksFragment.editLabelToList(gson.fromJson(json, Tasks.class));
                    }
                });
            }

            @Override
            public void onNotSaveTask() {

            }
        });
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
