package com.example.cop4655yelpapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private LinkedList<LocationData> localData;
    Context orgContext;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView ratingView;
        private final TextView addressView;
        private final TextView distanceView;
        private final TextView isClosedView;
        private final RelativeLayout listLayout;

        public ViewHolder(View view){
            super(view);
            //Define click listener for the ViewHolder's View

            nameView = (TextView) view.findViewById(R.id.nameText);
            ratingView = (TextView) view.findViewById(R.id.ratingText);
            addressView = (TextView) view.findViewById(R.id.addressText);
            distanceView = (TextView) view.findViewById(R.id.distanceText);
            isClosedView = (TextView) view.findViewById(R.id.isClosedText);
            listLayout = view.findViewById(R.id.listLayout);
        }

        public TextView getTextView(){
            return nameView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(LinkedList<LocationData> dataSet, Context context){
        localData = dataSet;
        orgContext = context;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    //Replace the contents of a view (invoked by layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position){
        //Get element from your dataset at this position and replace the
        //contents of the view with that element
        viewHolder.getTextView().setText(localData.get(position).getName());
        viewHolder.ratingView.setText("Rating: " + localData.get(position).getRating());
        viewHolder.addressView.setText(localData.get(position).getAddress());
        //convert meters to miles
        viewHolder.distanceView.setText(0.000621371 * localData.get(position).getDistance() + "mi");
        if (localData.get(position).getIsClosed()){
            viewHolder.isClosedView.setText("Closed");
            viewHolder.isClosedView.setTextColor(Color.RED);
        } else {
            viewHolder.isClosedView.setText("Open");
            viewHolder.isClosedView.setTextColor(Color.GREEN);
        }

        //set listener
        viewHolder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save data in location object
                LocationData clickedLocation = localData.get(position);

                Intent intent = new Intent(orgContext, DetailsActivity.class);
                intent.putExtra("locName", clickedLocation.getName());
                intent.putExtra("locReviews", clickedLocation.getReviewCount());
                intent.putExtra("locRating", clickedLocation.getRating());
                intent.putExtra("locAddress", clickedLocation.getAddress());
                intent.putExtra("locDistance", clickedLocation.getDistance());
                intent.putExtra("locIsClosed", clickedLocation.getIsClosed());
                intent.putExtra("locPhone", clickedLocation.getPhone());
                intent.putExtra("locUrl", clickedLocation.getUrl());
                intent.putExtra("locImgUrl", clickedLocation.getImgUrl());

                view.getContext().startActivity(intent);
            }
        });

    }

    //Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount(){
        return localData.size();
    }

}
