package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.utsa.cs3443.picpick.model.Photo;
import edu.utsa.cs3443.picpick.model.PhotoManager;

public class GalleryActivity extends AppCompatActivity {

    private List<Photo> unassignedPhotos;
    private GridView gridView;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        PhotoManager.loadPhotos(this);

        // Load unassigned photos
        unassignedPhotos = PhotoManager.getUnassignedPhotos();

        // Set photo count
        TextView photoCount = findViewById(R.id.photoCount);
        photoCount.setText(unassignedPhotos.size() + " Photos");

        // Set up grid
        gridView = findViewById(R.id.gridGallery);
        adapter = new ImageAdapter(this, unassignedPhotos);
        gridView.setAdapter(adapter);

        // Open image fullscreen on click
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Photo selected = unassignedPhotos.get(position);
            Intent intent = new Intent(GalleryActivity.this, HomeActivity.class);
            intent.putExtra("imagePath", selected.getFilePath());
            startActivity(intent);
        });

        // Bottom navigation
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        ImageButton btnFolder = findViewById(R.id.btnFolder);

        btnGallery.setImageResource(R.drawable.ic_gallery_dark); // highlight current tab

        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        btnFolder.setOnClickListener(v -> startActivity(new Intent(this, FolderActivity.class)));
    }
}
