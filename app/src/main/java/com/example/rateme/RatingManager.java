package com.example.rateme;

import android.util.Log;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RatingManager {
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    public RatingManager() {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void saveOrUpdateRating(String productTitle, float rating) {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        if (userEmail == null) {
            return; // Kein Benutzer eingeloggt
        }

        CollectionReference ratingsRef = db.collection("ProductRatings").document(productTitle).collection("Ratings");

        ratingsRef.whereEqualTo("userEmail", userEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // Neues Rating hinzufügen
                    Map<String, Object> ratingData = new HashMap<>();
                    ratingData.put("userEmail", userEmail);
                    ratingData.put("rating", rating);
                    ratingsRef.add(ratingData);
                } else {
                    // Vorhandene Bewertung aktualisieren
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().update("rating", rating);
                    }
                }
            }
        });
    }

    public void getAverageRatingFromFirebase(String productTitle, RatingBar ratingBar) {
        CollectionReference ratingsRef = db.collection("ProductRatings").document(productTitle).collection("Ratings");

        ratingsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                float total = 0;
                int count = 0;

                for (QueryDocumentSnapshot document : querySnapshot) {
                    Number rating = (Number) document.getData().get("rating");
                    if (rating != null) {
                        total += rating.floatValue();
                        count++;
                    }
                }

                float average = count > 0 ? total / count : 0;
                ratingBar.setRating(average);
            } else {
                Log.w("Firestore", "Error getting ratings.", task.getException());
            }
        });
    }
}
