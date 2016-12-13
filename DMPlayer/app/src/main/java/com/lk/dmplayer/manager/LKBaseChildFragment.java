package com.lk.dmplayer.manager;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lk.dmplayer.R;
import com.lk.dmplayer.adapter.MyRecyclerAdapter;
import com.lk.dmplayer.phonemidea.DMPlayerUtility;


/**
 * Created by dlkham on 12/2/2016.
 */
public abstract class LKBaseChildFragment extends Fragment {
    public static final String TAG = "LKBaseChildFragment";
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter = null;
    private Cursor cursorChild = null;
    private AsyncQueryHandler asyncQueryHandler;
     protected View view;
    public interface OnClickListener {
        public void onClick(long id, String title);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.asyncQueryHandler = new QueryHandler(getContext().getContentResolver());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragmentchild, null);
        setUpView(view);
        return view;
    }
    private class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            changeCursor(cursor);
        }
    }
    private void setUpView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        populateData();
    }
    private void populateData() {
        adapter = (MyRecyclerAdapter) getActivity().getLastCustomNonConfigurationInstance();
        if (adapter == null) {
            adapter = new MyRecyclerAdapter(getContext(), cursorChild, onClickListener);
            adapter.setFragment(getFragment());
            mRecyclerView.setAdapter(adapter);
            onCursor(asyncQueryHandler);
        } else {
            mRecyclerView.setAdapter(adapter);
            cursorChild = adapter.getCursor();
            if(cursorChild != null)
                changeCursor(cursorChild);
        }
    }
    public abstract Cursor onCursor(AsyncQueryHandler asyncQueryHandler);
    public abstract int getIndexLine1(Cursor cursor);
    public abstract int getIndexLine2(Cursor cursor);
    public abstract int getIndexLine3(Cursor cursor);
    public abstract String getContentURI(Cursor cursor);
    public abstract Fragment getFragment();
    public abstract void onMoveDetail(long id, String title);
    private void changeCursor(Cursor cursor) {
        if (getActivity() == null)
            return;
        if (getActivity().isFinishing() && cursor != null) {
            cursor.close();
            cursor = null;
        }
        adapter.setContentURI(getContentURI(cursor));
        adapter.setIndexCursorLine1(getIndexLine1(cursor));
        adapter.setIndexCursorLine2(getIndexLine2(cursor));
        adapter.setIndexCursorLine3(getIndexLine3(cursor));
        if (cursor != cursorChild) {
            cursorChild = cursor;
            adapter.changeCursor(cursor);
        }
    }
    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(long id, String title) {
            onMoveDetail(id, title);
        }

    };
}
