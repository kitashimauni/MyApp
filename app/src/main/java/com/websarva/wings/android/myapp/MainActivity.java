package com.websarva.wings.android.myapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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
    private TimeTableManager timeTableManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // アクションバーの設定
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
            fragmentTransaction.add(android.R.id.content, TaskAddFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack("add_task");
            fragmentTransaction.commit();
        });
        // 時間割に移動したらFABを消す
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fab.setVisibility(View.INVISIBLE);
                        break;
                }
            }
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

        checkNotificationPermission();
        postNotify("タイトル", "メッセージ");
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
        if(timeTableManager == null){
            timeTableManager = new TimeTableManager(this);
            timeTableManager.setTimeTableGridAdapter(getApplicationContext(), R.layout.timetable_item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // 戻るボタンを追加する
    public void setupBackButton(String title){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(title);
        showMainMenu = false;
        invalidateMenu();
    }

    // Fragmentを1つ戻る
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

    public TimeTableManager getTimeTableManager() {
        return timeTableManager;
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

    private void checkNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel =
                    new NotificationChannel("1", getString(R.string.app_name), importance);

            channel.setDescription("説明");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public boolean postNotify(String title, String message){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "1")
                        .setSmallIcon(android.R.drawable.ic_menu_info_details)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED){
                if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)){
                    Toast toast = Toast.makeText(this,"通知の権限がありません", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ActivityResultLauncher<String> requestPermissionLauncher =
                            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                                Toast toast;
                                if(isGranted){
                                    toast = Toast.makeText(this, "通知が許可されました", Toast.LENGTH_SHORT);
                                } else {
                                    toast = Toast.makeText(this, "通知が許可されませんでした", Toast.LENGTH_SHORT);
                                }
                                toast.show();
                            });
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }
        notificationManager.notify(R.string.app_name, builder.build());
        return true;
    }
}
