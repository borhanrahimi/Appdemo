package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.utsa.cs3443.picpick.model.Photo;
import edu.utsa.cs3443.picpick.model.PhotoManager;

public class GalleryActivity extends AppCompatActivity {

    private List<Photo> unassignedPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Load photos from assets (with saved statuses)
        PhotoManager.loadPhotos(this);

        // Get unassigned photos
        unassignedPhotos = PhotoManager.getUnassignedPhotos();

        // Set up grid
        GridView gridView = findViewById(R.id.gridGallery);
        ImageAdapter adapter = new ImageAdapter(this, unassignedPhotos);
        gridView.setAdapter(adapter);

        // Handle image clicks → open in HomeActivity
        gridView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            Photo clicked = unassignedPhotos.get(position);
            Intent intent = new Intent(GalleryActivity.this, HomeActivity.class);
            intent.putExtra("selectedPhotoPath", clicked.getFilePath());
            startActivity(intent);
        });

        //  Bottom nav buttons
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnFolder = findViewById(R.id.btnFolder);

        btnGallery.setImageResource(R.drawable.ic_gallery_dark); // Highlight active

        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        btnFolder.setOnClickListener(v -> startActivity(new Intent(this, FolderActivity.class)));
        btnGallery.setOnClickListener(v -> {
            // Already in gallery – do nothing
        });
    }
}
