package arashrasoulzadeh.codepack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by arashrasoulzadeh on 8/28/2016 AD.
 * www.mrarash.ir
 */
public class offlineAdapter extends RecyclerView.Adapter<offlineAdapter.ViewHolder> {
    Activity c;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ViewHolder(View v) {
            super(v);

         }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public offlineAdapter(Activity cc) {
        this.c = cc;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public offlineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        v = LayoutInflater.from(c).inflate(R.layout.offline, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    View v;

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element




    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1;
    }
}
