package com.example.rateme;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.rateme.databinding.ActivityMainBinding;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    FirebaseFirestore firebaseFirestore;
    TextView currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore=FirebaseFirestore.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentUser=findViewById(R.id.currentUser);

        //ohne toolbar ist app abgestürzt
        Toolbar toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Setup für die Navigation
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_scan).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        String userId = FirebaseAuth.getInstance().getUid();
// Pfad zur Benutzerdokument in Firestore
        DocumentReference documentReferenceRef = firebaseFirestore.collection("User").document(userId);

// Daten aus Firestore abrufen
        documentReferenceRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    currentUser.setText(value.getString("username"));
                }
                else{
                    Log.d("Tag", "onEvent: Document do not exist");
                }
            }
        });


        // Navigation View
        Button scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starte den Barcode-Scan
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });



    }

    // Diese Methode wird aufgerufen, wenn der Barcode-Scan abgeschlossen ist
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Hier kannst du die gescannten Daten verwenden
                String scannedData = result.getContents();
                // Verarbeite die gescannten Daten hier weiter
                Toast.makeText(this, "Gescannter Barcode: " + scannedData, Toast.LENGTH_LONG).show();
            } else {
                // Handle den Fall, dass der Scan abgebrochen wurde
            }
        }
    }

    // Füge diese Methode hinzu, um die Up-Navigation zu unterstützen
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public void logout (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Welcome.class));
        finish();
    }
}
