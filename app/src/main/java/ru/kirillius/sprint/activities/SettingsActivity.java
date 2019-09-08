package ru.kirillius.sprint.activities;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.activities.fragments.BasicSettingsFragment;
import ru.kirillius.sprint.activities.fragments.LabelsFragment;
import ru.kirillius.sprint.interfaces.OnCompleteAction;
import ru.kirillius.sprint.interfaces.OnSaveLabel;
import ru.kirillius.sprint.interfaces.OnSelectLabel;
import ru.kirillius.sprint.models.Labels;
import ru.kirillius.sprint.service.CommonHelper;
import ru.kirillius.sprint.service.NotificationsHelper;
import ru.kirillius.sprint.service.UserInformationInPhone;

public class SettingsActivity extends AppCompatActivity implements OnSelectLabel {

    Context context;
    BasicSettingsFragment basicSettingsFragment;
    LabelsFragment labelsFragment;
    ViewPager vpMainActivity;
    TabLayout tabLayout;
    ViewPagerAdapter adapterVP;
    UserInformationInPhone userInformationInPhone;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = SettingsActivity.this;
        basicSettingsFragment = new BasicSettingsFragment();
        labelsFragment = new LabelsFragment();
        fab = findViewById(R.id.fab);
        fab.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationsHelper.DialogLabel(context, getLayoutInflater(), "Создание метки", null, new OnSaveLabel() {
                    @Override
                    public void onSaveLabel(Labels label) {
                        labelsFragment.addLabelToList(label);
                    }

                    @Override
                    public void onNotSaveLabel() {

                    }
                });
            }
        });

        vpMainActivity = (ViewPager) findViewById(R.id.vpSettingsActivity);
        setupViewPager(vpMainActivity);
        vpMainActivity.setOffscreenPageLimit(2);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vpMainActivity);

        setTitle(getResources().getString(R.string.SettingsActivityTitle));
        CommonHelper.setStatusBarColor(this);

        userInformationInPhone = new UserInformationInPhone(context);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapterVP = new ViewPagerAdapter(getSupportFragmentManager());
        adapterVP.addFragment(basicSettingsFragment, "Основные настройки");
        adapterVP.addFragment(labelsFragment, "Метки");
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
    public void onSelectLabel(Labels label) {
        NotificationsHelper.DialogLabel(context, getLayoutInflater(), "Редактирование метки", label, new OnSaveLabel() {
            @Override
            public void onSaveLabel(Labels label) {
                labelsFragment.editLabelToList(label);
            }

            @Override
            public void onNotSaveLabel() {

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
