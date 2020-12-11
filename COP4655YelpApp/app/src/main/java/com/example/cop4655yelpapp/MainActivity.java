package com.example.cop4655yelpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    public static String userEmail;
    public static LinkedList<LocationData> favList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        mAuth = FirebaseAuth.getInstance();

        //google signin
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1037212829282-4fra8k5qfi2smk16cp2idoh86hef48mh.apps.googleusercontent.com")
                .requestEmail()
                .build();
        //R.string.default_web_client_id

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        findViewById(R.id.SignInButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onSignInClick(view);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            //get User Email for Favorites
            userEmail = currentUser.getEmail();

            updateFavList();

            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }
        //updateUI(currentUser);
    }

    public static void updateFavList(){
        //get favorites from firestore and store locations in a linkedlist
        favList = new LinkedList<LocationData>();

        //firebase cloud firestore
        FirebaseFirestore myDatabase = FirebaseFirestore.getInstance();
        myDatabase.collection(MainActivity.userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                //test.setText(document.get("name").toString());
                                //store a single location in a LocationData object
                                LocationData aFavorite = new LocationData();
                                /*
                                Map<String, Object> aFav = document.getData();

                                for (Map.Entry<String, Object> entry : map.entrySet()){
                                    String key = entry.getKey();
                                    if (key == "name"){
                                        aFavorite.setName(entry.getValue().toString());
                                    }
                                }
                                */

                                aFavorite.setName(document.get("name").toString());
                                aFavorite.setReviewCount(Integer.parseInt(document.get("reviews").toString()));
                                aFavorite.setRating(Double.parseDouble(document.get("rating").toString()));
                                aFavorite.setAddress(document.get("address").toString());
                                aFavorite.setDistance(Double.parseDouble(document.get("distance").toString()));
                                aFavorite.setIsClosed(Boolean.parseBoolean(document.get("isClosed").toString()));
                                aFavorite.setPhone(document.get("phone").toString());
                                aFavorite.setUrl(document.get("url").toString());
                                aFavorite.setImgUrl(document.get("imgUrl").toString());


                                //add LocationData object to linkedlist
                                favList.add(aFavorite);
                            }
                        }
                    }
                });
    }

    public static LinkedList<LocationData> getFavList(){
        return favList;
    }

    public void onSignInClick(View view){
        //Intent SearchIntent = new Intent (context, MapActivity.class);
        //startActivity(SearchIntent);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onSignUpClick(View view){

    }

    public void updateUI(FirebaseUser currentUse){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            // mBinding.mainLayout
                            Toast.makeText(MainActivity.this, "Authentication failure", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    /*
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    */
}