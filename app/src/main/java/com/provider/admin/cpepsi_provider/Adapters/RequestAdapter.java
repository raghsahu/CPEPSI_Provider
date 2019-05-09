package com.provider.admin.cpepsi_provider.Adapters;

import android.app.Activity;
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
import com.provider.admin.cpepsi_provider.R;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private static final String TAG = "ReceiptAdapter";
    private ArrayList<RequestModel> reqilist;
    public Context context;
    String resId = "";
    String finalStatus = "";

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eventName, description,status,eventDate;
        CardView card;
        ImageView recpdeleteBtn;
        int pos;

        public ViewHolder(View view) {
            super(view);

            eventName = (TextView) view.findViewById(R.id.eventName);
            description = (TextView) view.findViewById(R.id.description);
            status = (TextView) view.findViewById(R.id.status);
            eventDate = (TextView) view.findViewById(R.id.eventDate);
            card = (CardView) view.findViewById(R.id.card);
        }
    }

    public static Context mContext;

    public RequestAdapter(Context mContext, ArrayList<RequestModel> reqt_list) {
        context = mContext;
        reqilist = reqt_list;

    }

    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_row, parent, false);

        return new RequestAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestAdapter.ViewHolder viewHolder, final int position) {
        RequestModel requestModel = reqilist.get(position);
        viewHolder.eventName.setText(requestModel.getName());
        viewHolder.description.setText(requestModel.getDiscription());
        viewHolder.eventDate.setText(requestModel.getDate());
        finalStatus = (requestModel.getProstatus());
        if (finalStatus.equals("0")) {
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.orange));
            viewHolder.status.setText("Pending...");
        } else if (requestModel.getProstatus().equals("1")) {
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.color2));
            viewHolder.status.setText("Accept...");
        } else if (requestModel.getProstatus().equals("2")) {
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.color1));
            viewHolder.status.setText("Decline...");
        }else if (requestModel.getProstatus().equals("3")) {
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.newone));
            viewHolder.status.setText("Complete...");
        }
        viewHolder.card.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestModel requestModel1 = reqilist.get(position);
                Intent intent = new Intent(context, AddStatus.class);
                intent.putExtra("RequestModel", requestModel1);
                context.startActivity(intent);
               // ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reqilist.size();
    }

}
