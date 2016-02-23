package com.example.qutaraga.myapplicationtestparsedelete.Utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private static boolean loading = false;

    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 2;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

       if(!isLoading() && totalItemCount <= visibleItemCount+firstVisibleItem) {
           setLoading(true);
           onLoadMore(current_page);
           current_page++;
       }
    }
    public static boolean isLoading() {
        return loading;
    }

    public static void setLoading(boolean loading) {
        EndlessRecyclerOnScrollListener.loading = loading;
    }

    public abstract void onLoadMore(int current_page);

}