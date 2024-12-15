package models;

import database.Database;
import java.util.Scanner;

public class UserPerformanceInputProcessor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== User Performance Input System ===");

        try {
            // User Details Input
            System.out.print("Enter user name: ");
            String name = scanner.nextLine();

            System.out.print("Enter user goal (Weight Loss/Muscle Gain/Cardio): ");
            String goal = scanner.nextLine();

            System.out.print("Enter weight (kg): ");
            double weight = scanner.nextDouble();

            System.out.print("Enter height (m): ");
            double height = scanner.nextDouble();

            System.out.print("Enter age: ");
            int age = scanner.nextInt();

            scanner.nextLine(); // Consume newline
            System.out.print("Enter gender (Male/Female): ");
            String gender = scanner.nextLine();

            System.out.print("Enter activity level (Low/Moderate/High): ");
            String activityLevel = scanner.nextLine();

            // Create and insert user
            User user = new User(0, name, goal, weight, height, age, gender, activityLevel);
            Database.insertUser(user);

            // Performance Stats Input
            System.out.print("Enter strength max (kg): ");
            double strengthMax = scanner.nextDouble();

            System.out.print("Enter speed time (seconds): ");
            double speedTime = scanner.nextDouble();

            System.out.print("Enter endurance duration (minutes): ");
            double enduranceDuration = scanner.nextDouble();

            System.out.print("Enter agility score (1-10): ");
            double agilityScore = scanner.nextDouble();

            // Create and insert performance stats
            PerformanceStats stats = new PerformanceStats(0, user.getId(), strengthMax,
                    speedTime, enduranceDuration, agilityScore);
            Database.insertPerformanceStats(stats);

            // Display recorded stats
            displayPerformanceStats(user.getId());

        } catch (Exception e) {
            System.out.println("Error processing input: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void displayPerformanceStats(int userId) {
        PerformanceStats stats = Database.fetchPerformanceStats(userId);
        if (stats != null) {
            System.out.println("\nRecorded Performance Stats:");
            System.out.println("Strength Max: " + stats.getStrengthMax() + " kg");
            System.out.println("Speed Time: " + stats.getSpeedTime() + " seconds");
            System.out.println("Endurance Duration: " + stats.getEnduranceDuration() + " minutes");
            System.out.println("Agility Score: " + stats.getAgilityScore());
        } else {
            System.out.println("No stats found for user ID: " + userId);
        }
    }
}

