package com.websarva.wings.android.myapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskAddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MainActivity activity;
    private TaskManager taskManager;
    private ConstraintLayout layout;

    public TaskAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment TaskAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskAddFragment newInstance(String param1, String param2) {
        TaskAddFragment fragment = new TaskAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        activity = (MainActivity) getActivity();
        if(activity==null){
            Log.e("null", "null in TasksFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        activity = (MainActivity) getActivity();
        activity.setupBackButton("タスクを追加");
        taskManager = activity.getTaskManager();

        EditText text_name = (EditText) view.findViewById(R.id.title_name);
        EditText text_detail = (EditText) view.findViewById(R.id.detail_text);
        activity.setKeyboardHider(text_name);
        activity.setKeyboardHider(text_detail);

        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 8,  9);
        // datePickerDialogFragment.setCalender(calendar);
        // datePickerDialogFragment.show(activity.getSupportFragmentManager(), "datePicker");


        AppCompatTextView deadline_date = view.findViewById(R.id.date);
        datePickerDialogFragment.setAppCompatTextView(deadline_date);
        deadline_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogFragment.show(activity.getSupportFragmentManager(), "datePicker");
            }
        });

        TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
        AppCompatTextView deadline_time = view.findViewById(R.id.time);
        timePickerDialogFragment.setTextView(deadline_time);
        deadline_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogFragment.show(activity.getSupportFragmentManager(), "TimePicker");
            }
        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.task_add_option, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case android.R.id.home:
                        return activity.backToStart();
                    case R.id.add_to_task_button:
                        // addするTaskの作成
                        Task task = new Task();
                        task.task_name = text_name.getText().toString();
                        task.task_detail = text_detail.getText().toString();
                        task.dead_line = datePickerDialogFragment.getCalendar();
                        taskManager.addTask(task);
                        return activity.backToStart();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
}