package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.utsa.cs3443.picpick.model.Photo;
import edu.utsa.cs3443.picpick.model.PhotoManager;

public class SkipActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);
        PhotoManager.loadPhotos(this);

        GridView gridView = findViewById(R.id.gridSkipImages);
        List<Photo> skipPhotos = PhotoManager.getPhotosByStatus(Photo.Status.SKIP);
        gridView.setAdapter(new ImageAdapter(this, skipPhotos));

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Photo selected = skipPhotos.get(position);
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
            startActivity(new Intent(SkipActivity.this, HomeActivity.class));
        });

        btnGallery.setOnClickListener(v -> {
            startActivity(new Intent(SkipActivity.this, GalleryActivity.class));
        });

        btnFolder.setOnClickListener(v -> {
            startActivity(new Intent(SkipActivity.this, FolderActivity.class));
        });
    }
}
