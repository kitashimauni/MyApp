package com.websarva.wings.android.myapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableEditFragment extends Fragment implements NoticeDialogFragment.NoticeDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "dayOfWeek";
    private static final String ARG_PARAM2 = "period";

    // TODO: Rename and change types of parameters
    private int dayOfWeek;
    private int period;

    private MainActivity activity;
    private TimeTableManager timeTableManager;
    private TimeTableItem tableItem;

    public TimeTableEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dayOfWeek 曜日.
     * @param period 時限目.
     * @return A new instance of fragment TimeTableEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeTableEditFragment newInstance(int dayOfWeek, int period) {
        TimeTableEditFragment fragment = new TimeTableEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, dayOfWeek);
        args.putInt(ARG_PARAM2, period);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayOfWeek = getArguments().getInt(ARG_PARAM1);
            period = getArguments().getInt(ARG_PARAM2);
        }
        activity = (MainActivity) getActivity();
        if(activity == null){
            Log.e("null", "activity is null in " + this);
        }
        timeTableManager = activity.getTimeTableManager();
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
        return inflater.inflate(R.layout.fragment_time_table_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        activity.setupBackButton(TimeTableItem.int_to_day_of_week(getContext(), dayOfWeek) + "曜日" +
                (period + 1) + "限目");
        EditText subjectName = view.findViewById(R.id.subject_name);
        EditText place = view.findViewById(R.id.place);
        EditText detail_text = view.findViewById(R.id.detail_text);
        activity.setKeyboardHider(subjectName);
        activity.setKeyboardHider(place);
        activity.setKeyboardHider(detail_text);
        tableItem = timeTableManager.getTableItem(dayOfWeek, period);
        if(tableItem == null){
            tableItem = new TimeTableItem();
            tableItem.setDay_of_week(dayOfWeek);
            tableItem.setPeriod(period);
        } else {
            subjectName.setText(tableItem.getSubject_name());
            place.setText(tableItem.getPlace());
            detail_text.setText(tableItem.getDetail());
        }

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.timetable_edit_option, menu);
                menu.findItem(R.id.delete_timetable_button).setVisible(timeTableManager.getTableItem(dayOfWeek, period) != null);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == android.R.id.home){
                    return activity.backToStart();
                } else if(menuItem.getItemId() == R.id.clear_all){
                    subjectName.setText("");
                    place.setText("");
                    detail_text.setText("");
                } else if(menuItem.getItemId() == R.id.delete_timetable_button){
                    Bundle data = new Bundle();
                    data.putInt("dayOfWeek", dayOfWeek);
                    data.putInt("period", period);
                    NoticeDialogFragment dialogFragment = new NoticeDialogFragment();
                    dialogFragment.setMessage(getString(R.string.ask_delete));
                    dialogFragment.setData(data);
                    dialogFragment.show(getChildFragmentManager(), "notice_delete_item");
                } else if(menuItem.getItemId() == R.id.update_timetable_button){
                    if(subjectName.getText().toString().isEmpty()){
                        Toast toast = Toast.makeText(getContext(), "科目名を入力してください", Toast.LENGTH_SHORT);
                        toast.show();
                        return false;
                    }
                    tableItem.setSubject_name(subjectName.getText().toString());
                    tableItem.setPlace(place.getText().toString());
                    tableItem.setDetail(detail_text.getText().toString());
                    timeTableManager.addItem(tableItem);
                    timeTableManager.notifyDataSetChanged();
                    return activity.backToStart();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, Bundle data) {
        timeTableManager.deleteItem(data.getInt("dayOfWeek"), data.getInt("period"));
        timeTableManager.notifyDataSetChanged();
        activity.backToStart();
    }
}