package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView; // ✅ ADDED

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.utsa.cs3443.picpick.model.Photo;
import edu.utsa.cs3443.picpick.model.PhotoManager;

public class KeepActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep);
        PhotoManager.loadPhotos(this);

        GridView gridView = findViewById(R.id.gridKeepImages);
        List<Photo> keepPhotos = PhotoManager.getPhotosByStatus(Photo.Status.KEEP);
        gridView.setAdapter(new ImageAdapter(this, keepPhotos));

        // ✅ Set photo counter text
        TextView photoCount = findViewById(R.id.photoCount);
        photoCount.setText("Total photos: " + keepPhotos.size());

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Photo selected = keepPhotos.get(position);
            Intent intent = new Intent(this, FullscreenActivity.class);
            intent.putExtra("imagePath", selected.getFilePath());
            startActivity(intent);
        });

        // Bottom Navigation
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        ImageButton btnFolder = findViewById(R.id.btnFolder);

        btnFolder.setImageResource(R.drawable.ic_folder_dark); // Highlight current tab

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(KeepActivity.this, HomeActivity.class));
        });

        btnGallery.setOnClickListener(v -> {
            startActivity(new Intent(KeepActivity.this, GalleryActivity.class));
        });

        btnFolder.setOnClickListener(v -> {
            startActivity(new Intent(KeepActivity.this, FolderActivity.class));
        });
    }
}
