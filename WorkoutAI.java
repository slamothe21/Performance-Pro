package AI;

import API.ExerciseDBAPI;
import models.PerformanceStats;
import models.User;

public class WorkoutAI {
    public static String generateAIWorkoutPlan(User user, PerformanceStats stats) {
        StringBuilder plan = new StringBuilder();

        // Determine workout split based on user's goal and activity level
        String[] workoutSplit = determineWorkoutSplit(user);

        // Generate periodization plan
        plan.append(generatePeriodizationPlan(user, stats));

        // Generate specific workouts for each day
        for (String day : workoutSplit) {
            plan.append("\n").append(day).append(":\n");
            String exercises = ExerciseDBAPI.getExercises(day.toLowerCase(), determineEquipment(user));
            plan.append(exercises);
            plan.append(generateIntensityGuidelines(user, stats));
        }

        // Add progression recommendations
        plan.append("\nProgression Plan:\n");
        plan.append(generateProgressionPlan(user, stats));

        return plan.toString();
    }

    private static String[] determineWorkoutSplit(User user) {
        switch (user.getGoal()) {
            case "Muscle Gain":
                return new String[]{"Push", "Pull", "Legs", "Upper Body", "Lower Body", "Rest", "Rest"};
            case "Weight Loss":
                return new String[]{"Full Body", "HIIT", "Full Body", "HIIT", "Full Body", "LISS", "Rest"};
            default:
                return new String[]{"Upper Body", "Lower Body", "Cardio", "Full Body", "Core", "Active Recovery", "Rest"};
        }
    }

    private static String determineEquipment(User user) {
        return user.getActivityLevel().equals("High") ? "barbell" : "dumbbell";
    }

    private static String generatePeriodizationPlan(User user, PerformanceStats stats) {
        StringBuilder periodization = new StringBuilder("4-Week Periodization Plan:\n\n");

        if ("Muscle Gain".equals(user.getGoal())) {
            periodization.append("Week 1: Hypertrophy Focus (4 sets of 8-12 reps)\n");
            periodization.append("Week 2: Strength Focus (5 sets of 4-6 reps)\n");
            periodization.append("Week 3: Power Focus (3 sets of 2-3 reps)\n");
            periodization.append("Week 4: Deload (3 sets of 12-15 reps)\n");
        } else if ("Weight Loss".equals(user.getGoal())) {
            periodization.append("Week 1: Endurance Focus (3 sets of 15-20 reps)\n");
            periodization.append("Week 2: Circuit Training (4 rounds, minimal rest)\n");
            periodization.append("Week 3: HIIT Focus (30s work, 30s rest)\n");
            periodization.append("Week 4: Active Recovery (2 sets of 12-15 reps)\n");
        }

        return periodization.toString();
    }

    private static String generateIntensityGuidelines(User user, PerformanceStats stats) {
        StringBuilder guidelines = new StringBuilder("\nIntensity Guidelines:\n");

        double strengthLevel = stats.getStrengthMax() / 150.0; // Normalized strength score
        double enduranceLevel = stats.getEnduranceDuration() / 60.0; // Normalized endurance score

        if (strengthLevel < 0.4) {
            guidelines.append("- Focus on form with lighter weights\n");
            guidelines.append("- Rest 2-3 minutes between sets\n");
        } else if (strengthLevel < 0.7) {
            guidelines.append("- Moderate weights with controlled tempo\n");
            guidelines.append("- Rest 1-2 minutes between sets\n");
        } else {
            guidelines.append("- Challenge yourself with heavier weights\n");
            guidelines.append("- Rest 3-4 minutes for compound exercises\n");
        }

        return guidelines.toString();
    }

    private static String generateProgressionPlan(User user, PerformanceStats stats) {
        StringBuilder progression = new StringBuilder();

        // Calculate current fitness level
        double fitnessScore = (stats.getStrengthMax() / 150.0 +
                stats.getEnduranceDuration() / 60.0 +
                stats.getAgilityScore() / 10.0) / 3;

        if (fitnessScore < 0.4) {
            progression.append("Beginner Progression:\n");
            progression.append("- Increase weight by 2.5-5% when you can complete all sets\n");
            progression.append("- Add 1 rep per set each week\n");
        } else if (fitnessScore < 0.7) {
            progression.append("Intermediate Progression:\n");
            progression.append("- Implement double progression method\n");
            progression.append("- Alternate between volume and intensity weeks\n");
        } else {
            progression.append("Advanced Progression:\n");
            progression.append("- Use periodization with deload weeks\n");
            progression.append("- Implement advanced techniques like clusters and drop sets\n");
        }

        return progression.toString();
    }
}

