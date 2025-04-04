package edu.utsa.cs3443.picpick;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        ImageView fullImageView = findViewById(R.id.fullImageView);
        String imagePath = getIntent().getStringExtra("imagePath");

        if (imagePath != null) {
            try {
                InputStream inputStream = getAssets().open(imagePath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                fullImageView.setImageBitmap(bitmap);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
