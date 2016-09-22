package arashrasoulzadeh.codepack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by arashrasoulzadeh on 9/21/2016 AD.
 * www.mrarash.ir
 */
public class f3 extends Fragment {
    RecyclerView mRecyclerView;
    ArrayList<Pack> items;
    View v;
    MyDatabase mydb;
    SQLiteDatabase sql;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        items = new ArrayList<>();
        mydb = new MyDatabase(getActivity());
        sql = mydb.getWritableDatabase();
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.s1, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        refreshItems();
        // specify an adapter (see also next example)
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });


        return v;

    }

    public void refreshItems() {
        Cursor c = sql.rawQuery("select * from archive", null);
        if (c.moveToFirst()) {
            Pack p = new Pack();
            p.id = c.getString(1);
            p.title = decode(c.getString(2));
            p.text = decode(c.getString(3));
            p.views = c.getString(4);
            p.offline = true;
            items.add(p);

            while (c.moveToNext()) {
                Pack p2 = new Pack();
                p2.id = c.getString(1);
                p2.title = decode(c.getString(2));
                p2.text = decode(c.getString(3));
                p2.views = c.getString(4);
                p2.offline = true;
                items.add(p2);

            }

        }


        MyAdapter mAdapter = new MyAdapter(items, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    public String decode(String code) {
        try {
            byte[] data = Base64.decode(code, Base64.DEFAULT);
            String text = new String(data, "UTF-8");
            return text;
        } catch (Exception err) {
            return "cant decrypt";
        }
    }
}
