package edu.utsa.cs3443.picpick.model;

public class Photo {

    private int id;               // Optional unique ID
    private String filePath;      // Path to the image (from storage or assets)
    private String label;         // Name or description
    private Status status;        // KEEP, TRASH, or SKIP

    public enum Status {
        UNASSIGNED,
        KEEP,
        TRASH,
        SKIP
    }

    public Photo(int id, String filePath, String label, Status status) {
        this.id = id;
        this.filePath = filePath;
        this.label = label;
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getLabel() {
        return label;
    }

    public Status getStatus() {
        return status;
    }

    // Setters
    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
