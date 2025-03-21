package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class FolderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        // Navigation Buttons
        ImageButton homeButton = findViewById(R.id.btnHome);
        ImageButton folderButton = findViewById(R.id.btnFolder);
        ImageButton galleryButton = findViewById(R.id.btnGallery);

        // Darken Folder Button (Active)
        folderButton.setImageResource(R.drawable.ic_folder_dark);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(FolderActivity.this, MainActivity.class);
            startActivity(intent);
        });

        galleryButton.setOnClickListener(v -> {
            Intent intent = new Intent(FolderActivity.this, GalleryActivity.class);
            startActivity(intent);
        });

        folderButton.setOnClickListener(v -> {
            // Already in Folder, no action needed
        });
    }
}

