package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find navigation buttons
        ImageButton homeButton = findViewById(R.id.btnHome);
        ImageButton folderButton = findViewById(R.id.btnFolder);
        ImageButton galleryButton = findViewById(R.id.btnGallery);

        // Darken the Home button (Active)
        homeButton.setImageResource(R.drawable.ic_home_dark);

        // Home button click → Do nothing (Already in Home)
        homeButton.setOnClickListener(v -> {
            // Already in Home, no action needed
        });

        // Gallery button click → Navigate to GalleryActivity
        galleryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        });

        // Folder button click → Navigate to FolderActivity
        folderButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FolderActivity.class);
            startActivity(intent);
        });
    }
}
