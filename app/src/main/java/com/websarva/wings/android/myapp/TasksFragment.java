package com.websarva.wings.android.myapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment tasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TasksFragment newInstance(String param1, String param2) {
        TasksFragment fragment = new TasksFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        /* テスト用
        ArrayList<String> items = new ArrayList<>();
        items.add("あ");
        items.add("い");
        items.add("う");
        */
        ArrayList<Task> items = new ArrayList<>();
        MainActivity activity = (MainActivity)getActivity();
        if(activity==null){
            Log.e("null", "null in TasksFragment");
            return;
        }
        TaskDao taskDao = activity.taskDao;
        // カスタマイズしたアダプターによりTaskからリストを作成可能+リストのカスタマイズが簡単に
        // ArrayAdapter<?> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1 , items);
        TaskListAdapter adapter = new TaskListAdapter(this.getContext(), android.R.layout.simple_list_item_1, items);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        Disposable disposable = taskDao.getAllTasksFinished(false) // データベースクエリを実行し、Singleを取得
            .subscribeOn(Schedulers.io()) // バックグラウンドスレッドで実行
            .observeOn(AndroidSchedulers.mainThread()) // メインスレッドで結果を処理
            .subscribe(
                tasks -> {
                    // 成功時の処理: クエリからのタスクリストを使用
                    // tasksはList<Task>型で、クエリの結果が格納されています
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Task task : tasks) {
                                // タスクを処理するコード
                                // Log.d("", task.task_name);
                                items.add(task);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                    Log.d("Fragment", "2");
                },
                    error -> {
                        // エラー時の処理: エラーハンドリングを行うことが重要です
                        // エラー情報を取得し、適切な対応を行います
                        Log.e("Fragment", error.getMessage());
                    }
            );
        // タップ時詳細を表示
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}