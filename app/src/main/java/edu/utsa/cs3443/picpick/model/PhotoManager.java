package edu.utsa.cs3443.picpick.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoManager {

    private static List<Photo> photoList = new ArrayList<>();
    private static boolean isLoaded = false;

    // Load all images from assets folder
    public static void loadPhotos(Context context) {
        if (isLoaded) return;

        photoList.clear();
        try {
            String[] files = context.getAssets().list("");
            int id = 0;
            for (String file : files) {
                if (file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".jpeg") || file.endsWith(".webp")) {
                    photoList.add(new Photo(id++, file, file, Photo.Status.UNASSIGNED));
                }
            }
            loadSavedStatuses(context);
            isLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all photos
    public static List<Photo> getAllPhotos() {
        return photoList;
    }

    // Get only unassigned photos
    public static List<Photo> getUnassignedPhotos() {
        List<Photo> result = new ArrayList<>();
        for (Photo p : photoList) {
            if (p.getStatus() == Photo.Status.UNASSIGNED) {
                result.add(p);
            }
        }
        return result;
    }

    // Get photos by specific status (KEEP, TRASH, SKIP)
    public static List<Photo> getPhotosByStatus(Photo.Status status) {
        List<Photo> result = new ArrayList<>();
        for (Photo p : photoList) {
            if (p.getStatus() == status) {
                result.add(p);
            }
        }
        return result;
    }

    // Set photo status and save it
    public static void setPhotoStatus(Photo photo, Photo.Status newStatus, Context context) {
        photo.setStatus(newStatus);
        saveStatuses(context);
    }

    // Save all statuses to SharedPreferences
    public static void saveStatuses(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("photo_statuses", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (Photo p : photoList) {
            editor.putString(p.getFilePath(), p.getStatus().name());
        }
        editor.apply();
    }

    // Load saved statuses into photoList
    public static void loadSavedStatuses(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("photo_statuses", Context.MODE_PRIVATE);
        for (Photo p : photoList) {
            String saved = prefs.getString(p.getFilePath(), null);
            if (saved != null) {
                p.setStatus(Photo.Status.valueOf(saved));
            }
        }
    }

    // Reset everything (set all to UNASSIGNED)
    public static void resetAll(Context context) {
        for (Photo p : photoList) {
            p.setStatus(Photo.Status.UNASSIGNED);
        }
        saveStatuses(context);
    }

    // Call this only if user wipes app storage or does a manual reset
    public static void forceReload(Context context) {
        isLoaded = false;
        loadPhotos(context);
    }
}
