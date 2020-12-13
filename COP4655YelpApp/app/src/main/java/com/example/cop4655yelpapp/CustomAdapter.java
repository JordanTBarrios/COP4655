package com.example.cop4655yelpapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private LinkedList<LocationData> localData;
    Context orgContext;
    private int listItemLayout;
    private FirebaseFirestore myDatabase = FirebaseFirestore.getInstance();

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
        private Button deleteButton;

        public ViewHolder(View view){
            super(view);
            //Define click listener for the ViewHolder's View

            nameView = (TextView) view.findViewById(R.id.nameText);
            ratingView = (TextView) view.findViewById(R.id.ratingText);
            addressView = (TextView) view.findViewById(R.id.addressText);
            distanceView = (TextView) view.findViewById(R.id.distanceText);
            isClosedView = (TextView) view.findViewById(R.id.isClosedText);
            listLayout = view.findViewById(R.id.listLayout);
            //only if delete button exists, will it be initialized
            if (view.findViewById(R.id.deleteButton) != null){
                deleteButton = view.findViewById(R.id.deleteButton);
            }
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
    public CustomAdapter(LinkedList<LocationData> dataSet, Context context, int layout){
        localData = dataSet;
        orgContext = context;
        listItemLayout = layout;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(listItemLayout, viewGroup, false);

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
        //convert meters to miles and round to 2 decimal places
        viewHolder.distanceView.setText((Math.round(0.000621371 * localData.get(position).getDistance() * 100.0)/100.0) + "mi");
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


        if (viewHolder.deleteButton != null){

            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String toBeDeleted = localData.get(position).getName();

                    myDatabase.collection(MainActivity.userEmail)
                            .document(toBeDeleted)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //call favorites activity again
                                    MainActivity.updateFavList();
                                    Toast.makeText(orgContext, "Succesfully Deleted", Toast.LENGTH_SHORT);
                                    Intent favIntent = new Intent(orgContext, FavoritesActivity.class);
                                    orgContext.startActivity(favIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //on failure, post toast message
                                    Toast.makeText(orgContext, "Error: did not delete", Toast.LENGTH_SHORT);

                                }
                            });
                }
            });
        }

    }

    //Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount(){
        return localData.size();
    }

}
