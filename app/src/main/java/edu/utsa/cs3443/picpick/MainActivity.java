package edu.utsa.cs3443.picpick;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Immediately launch HomeActivity and finish MainActivity
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Close MainActivity so it's not in the back stack
    }
}
