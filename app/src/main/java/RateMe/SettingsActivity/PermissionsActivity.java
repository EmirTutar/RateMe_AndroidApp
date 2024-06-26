package RateMe.SettingsActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.rateme.R;

/**
 * Verwaltet Benutzerberechtigungen, speziell für die Kamera. Erlaubt Benutzern, die
 * Kameraberechtigung zu aktivieren. Eine Deaktivierung über die App ist nicht möglich.
 */

public class PermissionsActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchCameraPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_permissions);

        switchCameraPermission = findViewById(R.id.switch_camera_permission);

        switchCameraPermission.setChecked(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

        switchCameraPermission.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Permission cannot be revoked here.", Toast.LENGTH_LONG).show();
                switchCameraPermission.setChecked(true);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Kameraberechtigung gewährt", Toast.LENGTH_SHORT).show();
                switchCameraPermission.setChecked(true);
            } else {
                Toast.makeText(this, "Kameraberechtigung verweigert", Toast.LENGTH_SHORT).show();
                switchCameraPermission.setChecked(false);
            }
        }
    }
}
