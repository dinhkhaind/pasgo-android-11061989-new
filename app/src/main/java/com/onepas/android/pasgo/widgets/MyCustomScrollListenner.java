package com.onepas.android.pasgo.widgets;

import android.widget.AbsListView;
import android.widget.ListView;

import com.onepas.android.pasgo.utils.Utils;

/**
 * Created by Duc Trinh on 13/10/2015.
 */
public abstract class MyCustomScrollListenner implements AbsListView.OnScrollListener{
    private final static String TAG=" MyCustomScroll";
    private boolean isScrollUp = false;
    private float oldFirstVisibleItemHeight;
    private int oldFirstVisibleItem = 0;
    private int oldFirstVisibleItemTop = 0;
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        onMove(computeDy(view,firstVisibleItem));
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            if(isScrollUp){
                onHide();
            }else{
                onShow();
            }
        }
    }
    public int computeDy(AbsListView view,int firstVisibleItem){
        int result = 0;
        int count = view.getChildCount();
        if(count > 1){
            if(oldFirstVisibleItem == firstVisibleItem){
                oldFirstVisibleItemHeight = view.getChildAt(0).getHeight();
                result = view.getChildAt(0).getTop() - oldFirstVisibleItemTop;
                oldFirstVisibleItem = firstVisibleItem;
                oldFirstVisibleItemTop = view.getChildAt(0).getTop();
                if(result > 0) isScrollUp = false;
                if(result < 0) isScrollUp = true;
                Utils.Log(TAG,"computeDy 1:_ "+result+" "+isScrollUp+" "+ firstVisibleItem+" "+oldFirstVisibleItem);
            }else if(oldFirstVisibleItem < firstVisibleItem){
                isScrollUp = true;
                int dividerHeight = ((ListView)view).getDividerHeight();
                int newFirstVisibleItemTop = view.getChildAt(0).getTop();
                result = (int) -(oldFirstVisibleItemTop + oldFirstVisibleItemHeight - newFirstVisibleItemTop + dividerHeight);
                oldFirstVisibleItemTop = newFirstVisibleItemTop;
                oldFirstVisibleItemHeight = view.getChildAt(0).getHeight();
                oldFirstVisibleItem = firstVisibleItem;
                Utils.Log(TAG,"computeDy 2:_"+result+" "+isScrollUp+" "+ firstVisibleItem+" "+oldFirstVisibleItem);
            }else {
                isScrollUp = false;
                int dividerHeight = ((ListView)view).getDividerHeight();
                int newFirstVisibleItemTop = view.getChildAt(0).getTop();
                int newFirstVisibleItemHeight = view.getChildAt(0).getHeight();
                result = (int)(newFirstVisibleItemTop + newFirstVisibleItemHeight + dividerHeight - oldFirstVisibleItemTop);
                oldFirstVisibleItemTop = newFirstVisibleItemTop;
                oldFirstVisibleItemHeight = view.getChildAt(0).getHeight();
                oldFirstVisibleItem = firstVisibleItem;
                Utils.Log(TAG,"computeDy 3:_"+result+" "+isScrollUp+" "+ firstVisibleItem+" "+oldFirstVisibleItem);
            }
        }
        return result;
    }
    public boolean isScrollUp(){
        return isScrollUp;
    }
    public void setScrollUp(boolean isScrollUp){
        this.isScrollUp = isScrollUp;
    }
    public abstract void onMove(float delta);
    public abstract void onShow();
    public abstract void onHide();
}
