package com.websarva.wings.android.myapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskDetailFragment extends Fragment implements NoticeDialogFragment.NoticeDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "task";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int position;
    private String mParam2;

    private MainActivity activity;

    private TaskManager taskManager;

    private Task task;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskDetailFragment newInstance(int param1, String param2) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
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
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                activity.backToStart();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        taskManager = activity.getTaskManager();
        task = taskManager.getTask(position);
        TextView title_name = view.findViewById(R.id.detail_title_name);
        TextView deadline = view.findViewById(R.id.detail_deadline);
        TextView detail = view.findViewById(R.id.detail_detail);
        title_name.setText(task.getTask_name());
        detail.setText(task.getTask_detail());
        Calendar calendar = task.getDead_line();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE);
        String string = calendar.get(Calendar.YEAR) + getString(R.string.year) + (calendar.get(Calendar.MONTH) + 1) + getString(R.string.month) + calendar.get(Calendar.DAY_OF_MONTH) + getString(R.string.day)
                + " " + (hourOfDay < 10 ? ("0" + hourOfDay) : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute);
        deadline.setText(string);

        NoticeDialogFragment noticeDialogFragment = new NoticeDialogFragment();

        activity.setupBackButton("詳細");
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.task_edit_option, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case android.R.id.home:
                        return activity.backToStart();
                    case R.id.delete_task_button:
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        noticeDialogFragment.show(fragmentManager, "NoticeDialog");
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        taskManager.deleteTask(task);
        Toast toast = new Toast(getContext());
        toast.setText("削除しました");
        toast.show();
        activity.backToStart();
    }
}