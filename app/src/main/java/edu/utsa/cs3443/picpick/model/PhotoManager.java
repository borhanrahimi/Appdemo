package edu.utsa.cs3443.picpick.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhotoManager {

    private static HashMap<String, Photo> photoMap = new HashMap<>();
    private static HashMap<Photo.Status, List<Photo>> statusMap = new HashMap<>();
    private static boolean isLoaded = false;

    public static void loadPhotos(Context context) {
        if (isLoaded) return;

        photoMap.clear();
        int id = 0;

        try {
            String[] files = context.getAssets().list("");
            for (String file : files) {
                if (file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".jpeg") || file.endsWith(".webp")) {
                    Photo p = new Photo(id++, file, file, Photo.Status.UNASSIGNED);
                    photoMap.put(file, p);
                }
            }
            loadSavedStatuses(context);
            categorizePhotos();
            isLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void categorizePhotos() {
        statusMap.clear();
        for (Photo.Status status : Photo.Status.values()) {
            statusMap.put(status, new ArrayList<>());
        }

        for (Photo p : photoMap.values()) {
            statusMap.get(p.getStatus()).add(p);
        }
    }

    public static List<Photo> getAllPhotos() {
        return new ArrayList<>(photoMap.values());
    }

    public static List<Photo> getUnassignedPhotos() {
        return statusMap.getOrDefault(Photo.Status.UNASSIGNED, new ArrayList<>());
    }

    public static List<Photo> getPhotosByStatus(Photo.Status status) {
        return statusMap.getOrDefault(status, new ArrayList<>());
    }

    public static Photo getPhotoByPath(String filePath) {
        return photoMap.get(filePath);
    }

    // AUTO EXPORT CSV ON STATUS CHANGE
    public static void setPhotoStatus(Photo photo, Photo.Status newStatus, Context context) {
        photo.setStatus(newStatus);
        saveStatuses(context);            // Save to SharedPreferences
        saveStatusesToCSV(context);       // Save to CSV automatically
        categorizePhotos();
    }

    public static void saveStatuses(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("photo_statuses", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (Photo p : photoMap.values()) {
            editor.putString(p.getFilePath(), p.getStatus().name());
        }
        editor.apply();
    }

    public static void loadSavedStatuses(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("photo_statuses", Context.MODE_PRIVATE);
        for (Photo p : photoMap.values()) {
            String saved = prefs.getString(p.getFilePath(), null);
            if (saved != null) {
                p.setStatus(Photo.Status.valueOf(saved));
            }
        }
    }

    public static void resetAll(Context context) {
        for (Photo p : photoMap.values()) {
            p.setStatus(Photo.Status.UNASSIGNED);
        }
        saveStatuses(context);
        saveStatusesToCSV(context);  // Also export CSV after reset
        categorizePhotos();
    }

    public static void forceReload(Context context) {
        isLoaded = false;
        loadPhotos(context);
    }

    // CSV Export method
    public static void saveStatusesToCSV(Context context) {
        File file = new File(context.getFilesDir(), "photo_statuses.csv");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("fileName,status\n");

            for (Photo p : photoMap.values()) {
                writer.write(p.getFilePath() + "," + p.getStatus().name() + "\n");
            }

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  Move skipped photos back to unassigned
    public static void moveSkippedToUnassigned(Context context) {
        List<Photo> skippedPhotos = getPhotosByStatus(Photo.Status.SKIP);
        for (Photo p : skippedPhotos) {
            p.setStatus(Photo.Status.UNASSIGNED);
        }
        saveStatuses(context);
        categorizePhotos(); // Refresh status map
    }
}
