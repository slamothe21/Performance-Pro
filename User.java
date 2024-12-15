// File: src/main/java/models/User.java
package models;

public class User {
    private int id;
    private String name;
    private String goal;
    private double weight;
    private double height;
    private int age;
    private String gender;
    private String activityLevel;

    public User(int id, String name, String goal, double weight, double height, int age, String gender, String activityLevel) {
        this.id = id;
        this.name = name;
        this.goal = goal;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.activityLevel = activityLevel;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getGoal() { return goal; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getActivityLevel() { return activityLevel; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGoal(String goal) { this.goal = goal; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }
}
