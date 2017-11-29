package com.narkang.util.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/11/2.
 */

public class NarDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "DividerItemDecoration";

    private Paint mPaint;
    private DividerLookup dividerLookup;

    public NarDividerItemDecoration() {
        mPaint = new Paint();
        dividerLookup = new NarDividerItemDecoration.DefaultDividerLookup();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount-1; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            NarDivider leftDivider = dividerLookup.getVerticalDivider(position);
            NarDivider bottomDivider = dividerLookup.getHorizontalDivider(position);
            if (leftDivider != null) drawLeftDivider(c, child, leftDivider);
            if (bottomDivider != null) drawBottomDivider(c, child, bottomDivider);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int itemCount = state.getItemCount()-1;
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);
            int spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount);
            int lastSpanGroupIndex = spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount);
            NarDivider vd = dividerLookup.getVerticalDivider(position);
            NarDivider hd = dividerLookup.getHorizontalDivider(position);
            outRect.left = vd == null ? 0 : vd.size;
            outRect.bottom = hd == null ? 0 : hd.size;
            if (spanIndex == 0) outRect.left = 0;
            if (lastSpanGroupIndex == spanGroupIndex) outRect.bottom = 0;
            return;
        } else if (layoutManager instanceof LinearLayoutManager) {
            NarDivider hd = dividerLookup.getHorizontalDivider(position);
            outRect.bottom = hd == null ? 0 : hd.size;
            if (position == itemCount) outRect.bottom = 0;
            return;
        }
        throw new IllegalArgumentException("It is not currently supported StaggeredGridLayoutManager");
    }

    private void drawBottomDivider(Canvas c, View child, NarDivider bd) {
        mPaint.setColor(bd.color);
        c.drawRect(
                child.getLeft() + bd.marginStart,
                child.getBottom(),
                child.getRight() - bd.marginEnd,
                child.getBottom() + bd.size,
                mPaint
        );
    }

    private void drawLeftDivider(Canvas c, View child, NarDivider ld) {
        mPaint.setColor(ld.color);
        c.drawRect(
                child.getRight(),
                child.getTop() + ld.marginStart,
                child.getRight() + ld.size,
                child.getBottom() - ld.marginEnd,
                mPaint
        );
    }


    private class DefaultDividerLookup implements DividerLookup {

        private NarDivider mDivider;

        public DefaultDividerLookup() {
            mDivider = new NarDivider.Builder().color(Color.GRAY).size(2).build();
        }

        @Override
        public NarDivider getVerticalDivider(int position) {
            return mDivider;
        }

        @Override
        public NarDivider getHorizontalDivider(int position) {
            return mDivider;
        }
    }

    public void setDividerLookup(DividerLookup dividerLookup) {
        this.dividerLookup = dividerLookup;
    }

    public interface DividerLookup {
        NarDivider getVerticalDivider(int position);

        NarDivider getHorizontalDivider(int position);
    }

    public static class SimpleDividerLookup implements DividerLookup {

        @Override
        public NarDivider getVerticalDivider(int position) {
            return null;
        }

        @Override
        public NarDivider getHorizontalDivider(int position) {
            return null;
        }
    }

}
