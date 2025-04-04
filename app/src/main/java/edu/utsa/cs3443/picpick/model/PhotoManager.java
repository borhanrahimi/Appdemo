package edu.utsa.cs3443.picpick.model;

import android.content.Context;
import android.content.SharedPreferences;

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

    // Categorize photos by their status
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

    public static void setPhotoStatus(Photo photo, Photo.Status newStatus, Context context) {
        photo.setStatus(newStatus);
        saveStatuses(context);
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
        categorizePhotos();
    }

    public static void forceReload(Context context) {
        isLoaded = false;
        loadPhotos(context);
    }
}
