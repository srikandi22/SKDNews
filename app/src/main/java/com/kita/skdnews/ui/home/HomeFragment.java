package com.kita.skdnews.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kita.skdnews.MainActivity;
import com.kita.skdnews.R;
import com.kita.skdnews.adapter.NewsAdapter;
import com.kita.skdnews.api.ApiClient;
import com.kita.skdnews.api.ApiInterface;
import com.kita.skdnews.helper.Extra;
import com.kita.skdnews.models.News;
import com.kita.skdnews.ui.NewsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {
    private static String TAG = HomeFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;

    public ConstraintLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;

    private RecyclerView newsList;
    private RecyclerView.LayoutManager layoutManager;

    private TextView topHeadline;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        errorLayout = root.findViewById(R.id.errorLayout);
        errorImage = root.findViewById(R.id.errorImage);
        errorTitle = root.findViewById(R.id.errorTitle);
        errorMessage = root.findViewById(R.id.errorMessage);
        btnRetry = root.findViewById(R.id.btnRetry);

        newsList = root.findViewById(R.id.news_list);
        layoutManager = new LinearLayoutManager(getContext());
        newsList.setLayoutManager(layoutManager);
        newsList.setItemAnimator(new DefaultItemAnimator());
        newsList.setNestedScrollingEnabled(false);

        topHeadline = root.findViewById(R.id.topheadelines);

        errorLayout.setVisibility(View.GONE);

        MainActivity.btnSearch.setVisibility(View.VISIBLE);

        onLoadingSwipeRefresh("");
        swipeRefreshLayout.setProgressViewOffset(false, 0,100);

        return root;
    }

    @Override
    public void onRefresh() {
        topHeadline.setText("Top Headlines");
        LoadJson("");
    }

    public void onLoadingSwipeRefresh(final String keyword){
        topHeadline.setText("Top Headlines");
        if (!keyword.equals("")) topHeadline.setText("Result Search For - "+keyword);
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );
    }

    public void LoadJson(final String keyword){
        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<News> call;

        if (keyword.length() > 0 ){
            //call = apiInterface.getNewsSearch(MainActivity.Category, keyword, MainActivity.Language, "publishedAt", MainActivity.API_KEY);
            call = apiInterface.getNewsSearch(MainActivity.Category, keyword, MainActivity.Language, MainActivity.API_KEY);
        } else {
            call = apiInterface.getNews(MainActivity.Country, MainActivity.Category, MainActivity.API_KEY);
        }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){
                    if (!MainActivity.articles.isEmpty())
                        MainActivity.articles.clear();

                    MainActivity.articles = response.body().getArticle();
                    MainActivity.newsAdapter = new NewsAdapter(MainActivity.articles, getContext());
                    newsList.setAdapter(MainActivity.newsAdapter);
                    MainActivity.newsAdapter.notifyDataSetChanged();

                    Extra.newsToDB(getContext(),MainActivity.articles);



                    topHeadline.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    topHeadline.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                    MainActivity.articles.clear();
                    MainActivity.newsAdapter.notifyDataSetChanged();

                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }

                    showErrorMessage(
                            R.drawable.error,
                            "No Result",
                            "Please Try Again!\n"+
                                    errorCode);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                MainActivity.articles.clear();
                MainActivity.newsAdapter.notifyDataSetChanged();
                topHeadline.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.error,
                        "Oops..",
                        "Network failure, Please Try Again\n"+
                                t.toString());
            }
        });
    }



    private void showErrorMessage(int imageView, String title, String message){
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");
            }
        });

    }
}