package ru.kirillius.sprint.activities;

import android.content.Context;
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
import ru.kirillius.sprint.activities.fragments.CurrentSprintChoiceFragment;
import ru.kirillius.sprint.interfaces.OnSelectSprint;
import ru.kirillius.sprint.models.Sprint;
import ru.kirillius.sprint.service.CommonHelper;
import ru.kirillius.sprint.service.UserInformationInPhone;

public class SettingsActivity extends AppCompatActivity implements OnSelectSprint {

    Context context;
    BasicSettingsFragment basicSettingsFragment;
    CurrentSprintChoiceFragment currentSprintChoiceFragment;
    ViewPager vpMainActivity;
    TabLayout tabLayout;
    ViewPagerAdapter adapterVP;
    UserInformationInPhone userInformationInPhone;

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
        currentSprintChoiceFragment = new CurrentSprintChoiceFragment();

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
        adapterVP.addFragment(currentSprintChoiceFragment, "Текущий спринт");
        viewPager.setAdapter(adapterVP);
    }

    @Override
    public void onSelectSprint(Sprint currentSprint) {
        userInformationInPhone.setCurrentSprint(currentSprint.id);
        Toast.makeText(context, "Спринт успешно выбран", Toast.LENGTH_SHORT).show();
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
