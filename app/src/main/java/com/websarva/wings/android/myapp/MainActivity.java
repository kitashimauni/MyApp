package com.websarva.wings.android.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    public FragmentStateAdapter pagerAdapter;
    private boolean showMainMenu = true;

    public TaskDao taskDao;

    private TaskManager taskManager;
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
        if(taskDao == null) {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasks").build();
        taskDao = db.taskDao();
        taskManager = new TaskManager(this);
        taskManager.setTaskDao(taskDao);
        }
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
        }
        Log.d("Check", Integer.toString(fragmentManager.getBackStackEntryCount()));
        if(fragmentManager.getBackStackEntryCount() == 1) {
            actionBar.setTitle(R.string.app_name);
            actionBar.setDisplayHomeAsUpEnabled(false);
            showMainMenu = true;
            invalidateMenu();
        }
        return true;
    }

    public TaskManager getTaskManager(){
        return taskManager;
    }

    public void setKeyboardHider(EditText editText){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b && !(getCurrentFocus() instanceof EditText)){
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        ConstraintLayout layout = findViewById(R.id.main_const_layout);
        // InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // if(!(getCurrentFocus() instanceof EditText)){
        layout.requestFocus();
        return super.dispatchTouchEvent(event);
    }
}
