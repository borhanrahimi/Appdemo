package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoManager.loadPhotos(this); // Ensure photos are loaded
        setContentView(R.layout.activity_home);

        unassignedPhotos = PhotoManager.getUnassignedPhotos();
        imageViewMain = findViewById(R.id.imageViewMain);

        // ✅ Get selected image path from intent (if any)
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

        // Bottom nav
        findViewById(R.id.btnKeep).setOnClickListener(v -> handleChoice(Photo.Status.KEEP));
        findViewById(R.id.btnTrash).setOnClickListener(v -> handleChoice(Photo.Status.TRASH));
        findViewById(R.id.btnSkip).setOnClickListener(v -> handleChoice(Photo.Status.SKIP));

        ImageButton btnGallery = findViewById(R.id.btnGallery);
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnFolder = findViewById(R.id.btnFolder);

        btnHome.setImageResource(R.drawable.ic_home_dark); // highlight

        btnGallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));
        btnFolder.setOnClickListener(v -> startActivity(new Intent(this, FolderActivity.class)));
    }

    private void handleChoice(Photo.Status status) {
        if (currentIndex < unassignedPhotos.size()) {
            Photo photo = unassignedPhotos.get(currentIndex);
            PhotoManager.setPhotoStatus(photo, status, this);
            currentIndex++;
            showCurrentPhoto();
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
