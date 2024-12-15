package models;

public class PerformanceStats {
    private int id;
    private int userId;
    private double strengthMax;
    private double speedTime;
    private double enduranceDuration;
    private double agilityScore;

    public PerformanceStats(int id, int userId, double strengthMax, double speedTime,
                            double enduranceDuration, double agilityScore) {
        this.id = id;
        this.userId = userId;
        this.strengthMax = strengthMax;
        this.speedTime = speedTime;
        this.enduranceDuration = enduranceDuration;
        this.agilityScore = agilityScore;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public double getStrengthMax() { return strengthMax; }
    public double getSpeedTime() { return speedTime; }
    public double getEnduranceDuration() { return enduranceDuration; }
    public double getAgilityScore() { return agilityScore; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setStrengthMax(double strengthMax) { this.strengthMax = strengthMax; }
    public void setSpeedTime(double speedTime) { this.speedTime = speedTime; }
    public void setEnduranceDuration(double enduranceDuration) { this.enduranceDuration = enduranceDuration; }
    public void setAgilityScore(double agilityScore) { this.agilityScore = agilityScore; }
}


