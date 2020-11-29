package com.kita.skdnews.ui.archive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kita.skdnews.MainActivity;
import com.kita.skdnews.R;
import com.kita.skdnews.adapter.ArchiveAdapter;
import com.kita.skdnews.db.DSNews;
import com.kita.skdnews.ui.NewsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArchiveFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static String TAG = ArchiveFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<DSNews> archSource = new ArrayList<>();
    private ArchiveAdapter archAdapter;

    private RecyclerView archList;
    private RecyclerView.LayoutManager archLayoutManager;
    private TextView archTitle;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_archive, container, false);

        swipeRefreshLayout = root.findViewById(R.id.arch_swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        archSource = new ArrayList<DSNews>();
        archAdapter = new ArchiveAdapter(getContext(),archSource);

        archAdapter.setOnItemClickListener(new ArchiveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle args = new Bundle();
                args.putString("src_name", String.valueOf(archSource.get(position).src_name));
                args.putString("url", String.valueOf(archSource.get(position).url));

                Fragment frag = new NewsFragment();
                frag.setArguments(args);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, frag, frag.getClass().getSimpleName());
                fragmentTransaction.addToBackStack(TAG);
                fragmentTransaction.commit();
            }
        });

        archList = root.findViewById(R.id.arch_list);
        archLayoutManager = new LinearLayoutManager(getContext());
        archList.setLayoutManager(archLayoutManager);
        archList.setAdapter(archAdapter);

        archTitle = root.findViewById(R.id.arch_title);

        onLoadingSwipeRefresh("");
        swipeRefreshLayout.setProgressViewOffset(false, 0,100);

        MainActivity.btnSearch.setVisibility(View.VISIBLE);

        return root;
    }

    public void reInitData(String keyword){
        swipeRefreshLayout.setRefreshing(true);

        archSource.clear();
        if (keyword == null || keyword.equals(""))
            archSource.addAll(Arrays.asList(MainActivity.db.getDAONews().getAllNews()));
        else archSource.addAll(Arrays.asList(MainActivity.db.getDAONews().getNewsByKeyword("%"+keyword.toUpperCase()+"%")));

        archAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        archTitle.setText("Arsip Berita");
        reInitData("");
    }

    private void onLoadingSwipeRefresh(final String keyword){
        archTitle.setText("Arsip Berita");
        if (!keyword.equals("")) archTitle.setText("Arsip Berita "+keyword);
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        reInitData(keyword);
                    }
                }
        );
    }
}