package com.websarva.wings.android.myapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.appcompat.app.ActionBar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskEditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int position;
    private String mParam2;

    private MainActivity activity;

    private TaskManager taskManager;

    private Task task;

    public TaskEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskEditFragment newInstance(int position, String param2) {
        TaskEditFragment fragment = new TaskEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        taskManager = activity.getTaskManager();
        task = taskManager.getTask(position);

        EditText text_name = (EditText) view.findViewById(R.id.title_name);
        EditText text_detail = (EditText) view.findViewById(R.id.detail_text);
        text_name.setText(task.getTask_name());
        text_detail.setText(task.getTask_detail());
        activity.setKeyboardHider(text_name);
        activity.setKeyboardHider(text_detail);

        // 期限設定用
        Calendar calendar;
        if(!task.has_deadline()){
            calendar = Calendar.getInstance();
        } else {
            calendar = task.getDead_line();
        }

        // 期限日付設定
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.setString(getString(R.string.year), getString(R.string.month), getString(R.string.day));
        AppCompatTextView deadline_date = view.findViewById(R.id.date);
        datePickerDialogFragment.setTextView(deadline_date);
        datePickerDialogFragment.setCalender(calendar);
        deadline_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogFragment.show(activity.getSupportFragmentManager(), "datePicker");
            }
        });

        // 期限時間設定
        TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
        AppCompatTextView deadline_time = view.findViewById(R.id.time);
        timePickerDialogFragment.setTextView(deadline_time);
        timePickerDialogFragment.setCalendar(calendar);
        deadline_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogFragment.show(activity.getSupportFragmentManager(), "TimePicker");
            }
        });

        // 終日スイッチの設定
        SwitchCompat deadline_all_day_switch = view.findViewById(R.id.deadline_time_switch);
        deadline_all_day_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    deadline_time.setVisibility(View.INVISIBLE);
                }else{
                    deadline_time.setVisibility(View.VISIBLE);
                }
            }
        });
        deadline_all_day_switch.setChecked(task.isAll_day());
        // 期限スイッチの設定
        SwitchCompat deadline_switch = view.findViewById(R.id.dead_line_switch);
        deadline_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    view.findViewById(R.id.date_layout).setVisibility(View.VISIBLE);
                }else{
                    // GONEを設定すると詰められる
                    view.findViewById(R.id.date_layout).setVisibility(View.GONE);
                }
            }
        });
        deadline_switch.setChecked(true);
        deadline_switch.setChecked(task.has_deadline());

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) activity.getSupportFragmentManager().findFragmentByTag("task_detail_fragment");
        if(taskDetailFragment != null){
            taskDetailFragment.setShowMenu(false);
        }
        activity.setupBackButton("編集");
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.task_edit_option, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case android.R.id.home:
                        return activity.backToStart();
                    case R.id.update_task_button:
                        task.task_name = text_name.getText().toString();
                        task.task_detail = text_detail.getText().toString();
                        if(deadline_all_day_switch.isChecked()){
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                            task.all_day = true;
                        } else {
                            task.all_day = false;
                        }
                        task.dead_line = calendar;
                        task.has_deadline = deadline_switch.isChecked();
                        taskManager.updateTask(task);
                        activity.backToStart();
                        break;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("詳細");
            TaskDetailFragment taskDetailFragment = (TaskDetailFragment) activity.getSupportFragmentManager().findFragmentByTag("task_detail_fragment");
            if(taskDetailFragment != null){
                taskDetailFragment.setShowMenu(true);
                taskDetailFragment.loadTask();
            }
            Log.d("Check", this + " : ActionBar is not null");
        }
    }
}