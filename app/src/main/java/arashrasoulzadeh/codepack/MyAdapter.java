package arashrasoulzadeh.codepack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arashrasoulzadeh on 8/28/2016 AD.
 * www.mrarash.ir
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Pack> mDataset;
    Activity c;
    ProgressDialog pd;
    LayoutInflater inflater;
    String textTransitionName = "";
    String textTransitionName2 = "";


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle, mText, mView;
        public String t, c, id, views, date;
        public LinearLayout parent;
        public ImageView offline;

        public ViewHolder(View v) {
            super(v);

            mTitle = (TextView) v.findViewById(R.id.info_text);
            mText = (TextView) v.findViewById(R.id.info_short);
            mView = (TextView) v.findViewById(R.id.info_view);
            parent = (LinearLayout) v.findViewById(R.id.parentll);
            offline = (ImageView) v.findViewById(R.id.imageView2);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Pack> datas, Activity cc) {
        this.c = cc;
        mDataset = datas;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        v = LayoutInflater.from(c).inflate(R.layout.my_text_view, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    View v;

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.t = mDataset.get(position).title;
        holder.c = mDataset.get(position).text;
        holder.id = mDataset.get(position).id;
        holder.views = mDataset.get(position).views;
        holder.date = mDataset.get(position).date;
        if (mDataset.get(position).views.equals("null")) {
            holder.mView.setText("0");
        } else {
            holder.mView.setText(mDataset.get(position).views);
        }

        holder.mView.setText(holder.mView.getText() + "         #" + holder.id);
        holder.mTitle.setText(mDataset.get(position).title);
        holder.mText.setText(mDataset.get(position).text.substring(0, 30) + "...".replaceAll("^\\s+", ""));

        if (mDataset.get(position).offline) {
            holder.offline.setVisibility(View.VISIBLE);
        } else {
            holder.offline.setVisibility(View.GONE);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mTitle.setTransitionName("transtitle" + position);
        }


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetails(holder.mTitle, holder.t, holder.c, holder.id, holder.views, holder.date);
                Log.e(c.getPackageName(), "errr");
            }
        });


    }

    TextView _tv1;
    String _id, _title, _code, _view, _date;

    public void showDetails(TextView tv1, String title, String code, String id, String view, String date) {
        _tv1 = tv1;
        _title = title;
        _code = code;
        _id = id;
        _view = view;
        _date = date;
//        f2 safhe2 = new f2();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textTransitionName = tv1.getTransitionName();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            safhe2.setSharedElementEnterTransition(TransitionInflater.from(c).inflateTransition(android.R.transition.move));
//            safhe2.setEnterTransition(TransitionInflater.from(
//                    c).inflateTransition(android.R.transition.move));
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString("ACTION", tv1.getText().toString());
//        bundle.putString("TRANS_TEXT", textTransitionName);
////        safhe2.setArguments(bundle);
//
//        FragmentTransaction t = MainActivity.gsfm.beginTransaction();
//        t.addToBackStack("home");
//        t.addSharedElement(tv1, textTransitionName);
//        t.replace(R.id.frame, safhe2);
//        t.commit();

        runClick();


    }

    public void runClick() {
        Intent i = new Intent(c, f2.class);


        i.putExtra("title", _title);
        i.putExtra("code", _code);
        i.putExtra("TRANS_TEXT", textTransitionName);
        i.putExtra("id", _id);
        i.putExtra("view", _view);
        i.putExtra("date", _date);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(c, _tv1, textTransitionName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            c.startActivity(i, options.toBundle());
        }else{
            c.startActivity(i);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
