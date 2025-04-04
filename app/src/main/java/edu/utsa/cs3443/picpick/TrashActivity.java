package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.utsa.cs3443.picpick.model.Photo;
import edu.utsa.cs3443.picpick.model.PhotoManager;

public class TrashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        PhotoManager.loadPhotos(this);

        // Load and show TRASH photos
        GridView gridView = findViewById(R.id.gridTrashImages);
        List<Photo> trashPhotos = PhotoManager.getPhotosByStatus(Photo.Status.TRASH);
        gridView.setAdapter(new ImageAdapter(this, trashPhotos));

        // ✅ Add total count
        TextView photoCount = findViewById(R.id.photoCount);
        photoCount.setText("Total photos: " + trashPhotos.size());

        // Click to view full image
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Photo selected = trashPhotos.get(position);
            Intent intent = new Intent(this, FullscreenActivity.class);
            intent.putExtra("imagePath", selected.getFilePath());
            startActivity(intent);
        });

        // Bottom Navigation Buttons
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        ImageButton btnFolder = findViewById(R.id.btnFolder);

        // Optional: Highlight current tab
        btnFolder.setImageResource(R.drawable.ic_folder_dark);

        // Nav actions
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(TrashActivity.this, HomeActivity.class));
        });

        btnGallery.setOnClickListener(v -> {
            startActivity(new Intent(TrashActivity.this, GalleryActivity.class));
        });

        btnFolder.setOnClickListener(v -> {
            startActivity(new Intent(TrashActivity.this, FolderActivity.class));
        });
    }
}
