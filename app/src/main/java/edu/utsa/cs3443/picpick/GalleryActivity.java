package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Find navigation buttons
        ImageButton homeButton = findViewById(R.id.btnHome);
        ImageButton folderButton = findViewById(R.id.btnFolder);
        ImageButton galleryButton = findViewById(R.id.btnGallery);

        // Darken the Gallery button (Active)
        galleryButton.setImageResource(R.drawable.ic_gallery_dark);

        // Home button click → Navigate to MainActivity (Home)
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(GalleryActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Folder button click → Navigate to FolderActivity
        folderButton.setOnClickListener(v -> {
            Intent intent = new Intent(GalleryActivity.this, FolderActivity.class);
            startActivity(intent);
        });

        // Gallery button click → Do nothing (Already in Gallery)
        galleryButton.setOnClickListener(v -> {
            // Already on Gallery screen, no action needed
        });
    }
}
