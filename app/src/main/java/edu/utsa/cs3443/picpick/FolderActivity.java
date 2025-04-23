package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import edu.utsa.cs3443.picpick.model.Photo;
import edu.utsa.cs3443.picpick.model.PhotoManager;

public class FolderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        // Bottom navigation
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        ImageButton btnFolder = findViewById(R.id.btnFolder);
        btnFolder.setImageResource(R.drawable.ic_folder_dark);

        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        btnGallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));

        // Folder cards
        CardView cardKeep = findViewById(R.id.cardKeep);
        CardView cardTrash = findViewById(R.id.cardTrash);
        CardView cardSkip = findViewById(R.id.cardSkip);
        TextView keepCount = findViewById(R.id.keepCount);
        TextView trashCount = findViewById(R.id.trashCount);
        TextView skipCount = findViewById(R.id.skipCount);

        // Set counts
        int keep = PhotoManager.getPhotosByStatus(Photo.Status.KEEP).size();
        int trash = PhotoManager.getPhotosByStatus(Photo.Status.TRASH).size();
        int skip = PhotoManager.getPhotosByStatus(Photo.Status.SKIP).size();

        keepCount.setText(String.valueOf(keep));
        trashCount.setText(String.valueOf(trash));
        skipCount.setText(String.valueOf(skip));

        // Card click actions
        cardKeep.setOnClickListener(v -> startActivity(new Intent(this, KeepActivity.class)));
        cardTrash.setOnClickListener(v -> startActivity(new Intent(this, TrashActivity.class)));
        cardSkip.setOnClickListener(v -> startActivity(new Intent(this, SkipActivity.class)));

        // Reset
        findViewById(R.id.btnReset).setOnClickListener(v -> {
            PhotoManager.resetAll(this);
            Toast.makeText(this, "All photo statuses have been reset.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GalleryActivity.class));
        });
    }
}
