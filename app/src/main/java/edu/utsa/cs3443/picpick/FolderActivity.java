package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        btnFolder.setImageResource(R.drawable.ic_folder_dark); // Highlight active

        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        btnGallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));
        btnFolder.setOnClickListener(v -> {
            // Already here
        });

        // Folder buttons
        Button btnKeep = findViewById(R.id.btnKeep);
        Button btnTrash = findViewById(R.id.btnTrash);
        Button btnSkip = findViewById(R.id.btnSkip);
        Button btnReset = findViewById(R.id.btnReset);

        btnKeep.setOnClickListener(v -> startActivity(new Intent(this, KeepActivity.class)));
        btnTrash.setOnClickListener(v -> startActivity(new Intent(this, TrashActivity.class)));
        btnSkip.setOnClickListener(v -> startActivity(new Intent(this, SkipActivity.class)));

        btnReset.setText("RESET");
        btnReset.setOnClickListener(v -> {
            PhotoManager.resetAll(this);
            Toast.makeText(this, "All photo statuses have been reset.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GalleryActivity.class));
        });

        // 🔥 Count TextViews
        TextView folderCounts = findViewById(R.id.folderCounts);
        int keepCount = PhotoManager.getPhotosByStatus(Photo.Status.KEEP).size();
        int trashCount = PhotoManager.getPhotosByStatus(Photo.Status.TRASH).size();
        int skipCount = PhotoManager.getPhotosByStatus(Photo.Status.SKIP).size();
        folderCounts.setText("Keep: " + keepCount + "   Trash: " + trashCount + "   Skip: " + skipCount);
    }
}
