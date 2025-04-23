package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import edu.utsa.cs3443.picpick.model.Photo;
import edu.utsa.cs3443.picpick.model.PhotoManager;

public class HomeActivity extends AppCompatActivity {

    private List<Photo> unassignedPhotos;
    private int currentIndex = 0;
    private ImageView imageViewMain;

    private Photo lastUndoPhoto = null;
    private Photo.Status lastUndoStatus = null;
    private boolean revisitingSkipped = false; // 🔁 Flag to track skip round

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.loadPhotos(this);
        setContentView(R.layout.activity_home);

        unassignedPhotos = PhotoManager.getUnassignedPhotos();
        imageViewMain = findViewById(R.id.imageViewMain);

        ImageButton btnUndo = findViewById(R.id.btnUndo);
        btnUndo.setOnClickListener(v -> performUndo());

        String selectedPath = getIntent().getStringExtra("imagePath");
        if (selectedPath != null) {
            for (int i = 0; i < unassignedPhotos.size(); i++) {
                if (unassignedPhotos.get(i).getFilePath().equals(selectedPath)) {
                    currentIndex = i;
                    break;
                }
            }
        }

        showCurrentPhoto();

        findViewById(R.id.btnKeep).setOnClickListener(v -> handleChoice(Photo.Status.KEEP));
        findViewById(R.id.btnTrash).setOnClickListener(v -> handleChoice(Photo.Status.TRASH));
        findViewById(R.id.btnSkip).setOnClickListener(v -> handleChoice(Photo.Status.SKIP));

        ImageButton btnGallery = findViewById(R.id.btnGallery);
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnFolder = findViewById(R.id.btnFolder);

        btnHome.setImageResource(R.drawable.ic_home_dark);

        btnGallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));
        btnFolder.setOnClickListener(v -> startActivity(new Intent(this, FolderActivity.class)));
    }

    private void handleChoice(Photo.Status newStatus) {
        if (currentIndex < unassignedPhotos.size()) {
            Photo photo = unassignedPhotos.get(currentIndex);
            lastUndoPhoto = photo;
            lastUndoStatus = photo.getStatus();

            PhotoManager.setPhotoStatus(photo, newStatus, this);

            // Reload list and adjust index
            unassignedPhotos = PhotoManager.getUnassignedPhotos();
            if (unassignedPhotos.isEmpty()) {
                List<Photo> skipped = PhotoManager.getPhotosByStatus(Photo.Status.SKIP);
                if (!skipped.isEmpty()) {
                    PhotoManager.moveSkippedToUnassigned(this);
                    unassignedPhotos = PhotoManager.getUnassignedPhotos();
                    currentIndex = 0;
                    Toast.makeText(this, "Reviewing skipped photos again", Toast.LENGTH_SHORT).show();
                } else {
                    showCurrentPhoto(); // still empty
                }
            } else if (currentIndex >= unassignedPhotos.size()) {
                currentIndex = Math.max(0, unassignedPhotos.size() - 1);
            }

            showCurrentPhoto();
        }
    }

    private void performUndo() {
        if (lastUndoPhoto != null && lastUndoStatus != null) {
            PhotoManager.setPhotoStatus(lastUndoPhoto, lastUndoStatus, this);
            unassignedPhotos = PhotoManager.getUnassignedPhotos();

            boolean found = false;
            for (int i = 0; i < unassignedPhotos.size(); i++) {
                if (unassignedPhotos.get(i).getFilePath().equals(lastUndoPhoto.getFilePath())) {
                    currentIndex = i;
                    found = true;
                    break;
                }
            }

            // 🔁 If photo isn’t found in unassigned, check all photos
            if (!found) {
                List<Photo> allPhotos = PhotoManager.getAllPhotos();
                for (int i = 0; i < allPhotos.size(); i++) {
                    if (allPhotos.get(i).getFilePath().equals(lastUndoPhoto.getFilePath())) {
                        if (lastUndoStatus == Photo.Status.UNASSIGNED) {
                            unassignedPhotos = PhotoManager.getUnassignedPhotos();
                            currentIndex = unassignedPhotos.indexOf(lastUndoPhoto);
                        }
                        found = true;
                        break;
                    }
                }
            }

            showCurrentPhoto();
            Toast.makeText(this, "Undo successful", Toast.LENGTH_SHORT).show();

            lastUndoPhoto = null;
            lastUndoStatus = null;
        } else {
            Toast.makeText(this, "Nothing to undo", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCurrentPhoto() {
        if (currentIndex >= unassignedPhotos.size()) {
            imageViewMain.setImageResource(R.drawable.ic_launcher_foreground);
            return;
        }

        Photo current = unassignedPhotos.get(currentIndex);
        try {
            InputStream is = getAssets().open(current.getFilePath());
            imageViewMain.setImageBitmap(android.graphics.BitmapFactory.decodeStream(is));
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

