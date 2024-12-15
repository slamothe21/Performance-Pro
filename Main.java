import AI.NutritionAI;
import AI.WorkoutAI;
import Suggestions.SuggestionGenerator;
import database.Database;
import models.PerformanceStats;
import models.User;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        try {
            initializeSystem();
            runMainProgram();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Critical error in main program", e);
            System.out.println("A critical error occurred. Please check the logs.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void initializeSystem() {
        System.out.println("Initializing Performance Pro System...\n");
        initializeDatabase();
        createSampleData();
    }

    private static void initializeDatabase() {
        try {
            Database.createUserTable();
            Database.createPerformanceStatsTable();
            Database.createWorkoutsTable();
            Database.createNutritionTable();
            System.out.println("Database initialization successful.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database initialization failed", e);
            throw new RuntimeException("Failed to initialize database: " + e.getMessage());
        }
    }

    private static void createSampleData() {
        try {
            System.out.println("\nCreating sample users...");

            // Create sample users with diverse goals
            User[] sampleUsers = {
                    new User(0, "John Doe", "Weight Loss", 85.0, 1.75, 25, "Male", "Moderate"),
                    new User(0, "Jane Smith", "Muscle Gain", 65.0, 1.65, 28, "Female", "High"),
                    new User(0, "Mike Johnson", "Cardio", 75.0, 1.80, 35, "Male", "Low")
            };

            // Create corresponding performance stats
            PerformanceStats[] sampleStats = {
                    new PerformanceStats(0, 1, 120.0, 12.5, 30.0, 7.8),
                    new PerformanceStats(0, 2, 95.0, 10.0, 45.0, 8.5),
                    new PerformanceStats(0, 3, 80.0, 11.2, 35.0, 7.0)
            };

            // Insert sample data
            for (int i = 0; i < sampleUsers.length; i++) {
                Database.insertUser(sampleUsers[i]);
                sampleStats[i].setUserId(sampleUsers[i].getId());
                Database.insertPerformanceStats(sampleStats[i]);
            }

            System.out.println("Sample data created successfully.");
            System.out.println("Available User IDs: 1, 2, 3");

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error creating sample data", e);
            System.out.println("Warning: Some sample data may not have been created.");
        }
    }

    private static void runMainProgram() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            running = processMenuChoice();
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Performance Pro Menu ===");
        System.out.println("1. View User Profile");
        System.out.println("2. Update User Details");
        System.out.println("3. Generate Workout Plan");
        System.out.println("4. Generate Nutrition Plan");
        System.out.println("5. View Performance Stats");
        System.out.println("6. Exit");
        System.out.print("\nChoose an option (1-6): ");
    }

    private static boolean processMenuChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: viewUserProfile(); break;
                case 2: updateUserDetails(); break;
                case 3: generateWorkoutPlan(); break;
                case 4: generateNutritionPlan(); break;
                case 5: viewPerformanceStats(); break;
                case 6:
                    System.out.println("Thank you for using Performance Pro!");
                    return false;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number between 1 and 6.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error processing menu choice", e);
            System.out.println("An error occurred: " + e.getMessage());
        }
        return true;
    }

    private static void viewUserProfile() {
        try {
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            User user = Database.fetchUserById(userId);

            if (user != null) {
                System.out.println("\nUser Profile:");
                System.out.println("Name: " + user.getName());
                System.out.println("Goal: " + user.getGoal());
                System.out.println("Weight: " + user.getWeight() + " kg");
                System.out.println("Height: " + user.getHeight() + " m");
                System.out.println("Age: " + user.getAge());
                System.out.println("Gender: " + user.getGender());
                System.out.println("Activity Level: " + user.getActivityLevel());
            } else {
                System.out.println("User not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid user ID.");
            scanner.nextLine();
        }
    }

    private static void updateUserDetails() {
        try {
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            User user = Database.fetchUserById(userId);

            if (user != null) {
                System.out.print("Enter new weight (kg): ");
                double weight = scanner.nextDouble();
                System.out.print("Enter new age: ");
                int age = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter new activity level (Low/Moderate/High): ");
                String activityLevel = scanner.nextLine();

                user.setWeight(weight);
                user.setAge(age);
                user.setActivityLevel(activityLevel);

                Database.updateUser(user);
                System.out.println("User details updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter valid values.");
            scanner.nextLine();
        }
    }

    private static void generateWorkoutPlan() {
        try {
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            User user = Database.fetchUserById(userId);
            PerformanceStats stats = Database.fetchPerformanceStats(userId);

            if (user != null && stats != null) {
                String workoutPlan = WorkoutAI.generateAIWorkoutPlan(user, stats);
                String exerciseSuggestions = SuggestionGenerator.generateWorkoutSuggestion(user, stats);

                System.out.println("\nAI-Generated Workout Plan:");
                System.out.println(workoutPlan);
                System.out.println("\nExercise Suggestions:");
                System.out.println(exerciseSuggestions);
            } else {
                System.out.println("User or performance stats not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid user ID.");
            scanner.nextLine();
        }
    }

    private static void generateNutritionPlan() {
        try {
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            User user = Database.fetchUserById(userId);
            PerformanceStats stats = Database.fetchPerformanceStats(userId);

            if (user != null) {
                String nutritionPlan = NutritionAI.generateAINutritionPlan(user, stats);
                String nutritionSuggestions = SuggestionGenerator.generateNutritionSuggestion(user, stats);

                System.out.println("\nAI-Generated Nutrition Plan:");
                System.out.println(nutritionPlan);
                System.out.println("\nNutrition Suggestions:");
                System.out.println(nutritionSuggestions);
            } else {
                System.out.println("User not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid user ID.");
            scanner.nextLine();
        }
    }

    private static void viewPerformanceStats() {
        try {
            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            PerformanceStats stats = Database.fetchPerformanceStats(userId);

            if (stats != null) {
                System.out.println("\nPerformance Statistics:");
                System.out.println("Strength Max: " + stats.getStrengthMax() + " kg");
                System.out.println("Speed Time: " + stats.getSpeedTime() + " seconds");
                System.out.println("Endurance Duration: " + stats.getEnduranceDuration() + " minutes");
                System.out.println("Agility Score: " + stats.getAgilityScore());
            } else {
                System.out.println("Performance stats not found for this user.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid user ID.");
            scanner.nextLine();
        }
    }
}








