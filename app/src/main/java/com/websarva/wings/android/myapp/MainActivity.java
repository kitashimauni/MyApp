package com.websarva.wings.android.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;
    private boolean showMainMenu = true;

    public TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // アクションバーの設定
        Toolbar toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ViewPagerの設定
        viewPager2 = findViewById(R.id.pager);
        pagerAdapter = new MainFragmentStateAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(position == 0 ? "タスク" : "時間割")
        ).attach();
        // FABの設定
        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content ,TaskAddFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack("add_task");
            fragmentTransaction.commit();
        });

        addMenuProvider(new MenuProvider() {
            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                MenuProvider.super.onPrepareMenu(menu);
                MenuItem item_1 = menu.findItem(R.id.option1);
                MenuItem item_2 = menu.findItem(R.id.option2);
                item_1.setVisible(showMainMenu);
                item_2.setVisible(showMainMenu);
            }
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_option, menu);
            }
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasks").build();
        taskDao = db.taskDao();
        // List<Task> tasks_not_finished = taskDao.getAllTasksFinished(false);
        // List<Task> tasks_finished = taskDao.getAllTasksFinished(true);


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void setupBackButton(String title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(title);
        showMainMenu = false;
        invalidateMenu();
    }

    public boolean backToStart(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return false;
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
        }
        showMainMenu = true;
        invalidateMenu();
        return true;
    }
}
