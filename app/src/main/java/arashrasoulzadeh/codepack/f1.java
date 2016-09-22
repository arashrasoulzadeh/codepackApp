package arashrasoulzadeh.codepack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by arashrasoulzadeh on 8/24/2016 AD.
 * www.mrarash.ir
 */

public class f1 extends Fragment {

    LayoutInflater inflater;
    LinearLayoutManager mLayoutManager;
    MyAdapter mAdapter;
    View v;
    Context c;
    RecyclerView mRecyclerView;
    AQuery aq;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String url = "";
    MyDatabase mydb;
    SQLiteDatabase sql;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        url = getArguments().getString("url");
        v = inflater.inflate(R.layout.s1, container, false);
        mydb = new MyDatabase(getContext());
        sql = mydb.getWritableDatabase();
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            setSharedElementReturnTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.change_trans));
            setExitTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.fade));

        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems("");
            }
        });

        refreshItems("");


        return v;


    }


    public void refreshItems(String q) {
        // String url = "";
        if (q.length() > 0)
            url = "http://www.codepack.ir/api/search/" + q;
//        else
//            url = "http://www.codepack.ir/api/latest";

        final ArrayList<Pack> items = new ArrayList<>();
        aq = new AQuery(getActivity());
//        mSwipeRefreshLayout.setRefreshing(true);

        aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>()


                {
                    @Override
                    public void callback(String url, JSONArray object, AjaxStatus status) {

                        if (object != null) {
                            try {
                                for (int i = 0; i < object.length(); i++) {

                                    JSONObject j = new JSONObject(object.get(i).toString());
                                    Pack pack = new Pack();
                                    pack.id = j.getString("id");
                                    pack.title = j.getString("title");

                                    if (j.getString("text").length() > 30)
                                        pack.text = j.getString("text");

                                    else
                                        pack.text = j.getString("text") + "                                    ";

                                    pack.views = j.getString("views");
                                    String txt = "-------------123------------";
                                    try {
                                        byte[] data = pack.title.getBytes("UTF-8");
                                        txt = Base64.encodeToString(data, Base64.DEFAULT);
                                        Cursor c = sql.rawQuery("select * from archive where posttitle = '" + txt + "'", null);
                                        if (c.getCount()>0)
                                            pack.offline=true;

                                    } catch (Exception err) {

                                    }


                                    items.add(pack);

                                }


                                MyAdapter mAdapter = new MyAdapter(items, getActivity());
                                mRecyclerView.setAdapter(mAdapter);


                            } catch (Exception e) {
                                Log.e("arashrasoulzadeh.codepack", "errrrrooorrrr = > " + e.getMessage().toLowerCase());

                                e.printStackTrace();
                            }
                        }

                        mSwipeRefreshLayout.setRefreshing(false);

                        super.callback(url, object, status);
                    }
                }


        );
    }
}
