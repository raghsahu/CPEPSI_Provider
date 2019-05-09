package com.provider.admin.cpepsi_provider.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.provider.admin.cpepsi_provider.AddStatus;
import com.provider.admin.cpepsi_provider.Model.RequestModel;
import com.provider.admin.cpepsi_provider.Model.ReviewsModel;
import com.provider.admin.cpepsi_provider.R;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private static final String TAG = "ReviewsAdapter";
    private ArrayList<ReviewsModel> rewlist;
    public Context context;
    String resId = "";
    String finalStatus = "";

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eventName, email,address,ratting;
        CardView card;
        ImageView recpdeleteBtn;
        int pos;

        public ViewHolder(View view) {
            super(view);

            eventName = (TextView) view.findViewById(R.id.eventName);
            email = (TextView) view.findViewById(R.id.email);
            address = (TextView) view.findViewById(R.id.address);
            ratting = (TextView) view.findViewById(R.id.ratting);
            card = (CardView) view.findViewById(R.id.card);
        }
    }

    public static Context mContext;

    public ReviewsAdapter(Context mContext, ArrayList<ReviewsModel> rew_list) {
        context = mContext;
        rewlist = rew_list;

    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_row, parent, false);

        return new ReviewsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReviewsAdapter.ViewHolder viewHolder, final int position) {
        ReviewsModel reviewsModel = rewlist.get(position);
        viewHolder.eventName.setText(reviewsModel.getName());
        viewHolder.email.setText(reviewsModel.getEmail());
        viewHolder.address.setText(reviewsModel.getAddress());
        viewHolder.ratting.setText(reviewsModel.getFeedbackservice());
        finalStatus = (reviewsModel.getFeedbackservice());
        if (finalStatus.equals("good")) {
            viewHolder.ratting.setText(" "+"Rating 5.0");
        } else if (reviewsModel.getFeedbackservice().equals("average")) {
            viewHolder.ratting.setText(" "+"Rating 3.0");
        } else if (reviewsModel.getFeedbackservice().equals("bad")) {
            viewHolder.ratting.setText(" "+"Rating 1.0");
        }
        viewHolder.card.setTag(viewHolder);
        viewHolder.pos = position;

    }

    @Override
    public int getItemCount() {
        return rewlist.size();
    }

}
