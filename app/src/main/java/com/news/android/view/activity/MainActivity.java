package com.news.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.news.android.R;
import com.news.android.model.Articles;
import com.news.android.prasenter.TopHeadlinesPresenter;
import com.news.android.util.Constants;
import com.news.android.view.adapter.NewsListAdapter;

import java.util.ArrayList;

/**
 * View Display the top headline list
 */
public class MainActivity extends AppCompatActivity implements TopHeadlinesPresenter.TopHeadlineListener, Constants.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TopHeadlinesPresenter mHeadlinesPresenter;
    private NewsListAdapter mNewsListAdapter;
    private int mPage;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidget();
        mHeadlinesPresenter = new TopHeadlinesPresenter(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mHeadlinesPresenter.getHeadlines(++mPage);
    }

    private void getWidget() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mProgressBar = findViewById(R.id.progress_bar);
    }


    @Override
    public void TopHeadlineSuccessListener(ArrayList<Articles> articles) {
        if (mNewsListAdapter != null) {
            mNewsListAdapter.updateList(articles);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mNewsListAdapter = new NewsListAdapter(this, articles, this);
            mRecyclerView.setAdapter(mNewsListAdapter);
        }
    }

    public void loadMoreNews() {
        mHeadlinesPresenter.getHeadlines(++mPage);
    }

    @Override
    public void TopHeadlinesErrorListener(String msg) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemClick(View view, int position, Object value) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        intent.putExtra(getString(R.string.data), (Articles) value);
        startActivity(intent, options.toBundle());

    }
}
