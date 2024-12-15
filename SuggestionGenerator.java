// File: src/main/java/suggestions/SuggestionGenerator.java
package Suggestions;

import models.User;
import models.PerformanceStats;

public class SuggestionGenerator {
    public static String generateWorkoutSuggestion(User user, PerformanceStats stats) {
        StringBuilder suggestions = new StringBuilder();
        String goal = user.getGoal();
        String activityLevel = user.getActivityLevel();

        switch (goal) {
            case "Weight Loss":
                appendWeightLossSuggestions(suggestions, activityLevel);
                break;
            case "Muscle Gain":
                appendMuscleGainSuggestions(suggestions, activityLevel);
                break;
            case "Cardio":
                appendCardioSuggestions(suggestions, activityLevel);
                break;
        }
        return suggestions.toString();
    }

    private static void appendWeightLossSuggestions(StringBuilder suggestions, String activityLevel) {
        suggestions.append("Focus on fat-burning exercises with a mix of strength and cardio.\n");
        if ("Moderate".equals(activityLevel)) {
            suggestions.append("For moderate activity level, try a combination of HIIT for 30 minutes and strength training 3 times a week.\n");
        } else if ("High".equals(activityLevel)) {
            suggestions.append("Since you're highly active, aim for 5 days of HIIT or circuit training and 2 days of strength training.\n");
        }
        suggestions.append("Include core exercises like planks, leg raises, and crunches for 10-15 minutes post-cardio.\n");
        suggestions.append("Focus on calorie burning, not muscle growth, so exercises like running, cycling, and rowing are key.\n");
    }

    private static void appendMuscleGainSuggestions(StringBuilder suggestions, String activityLevel) {
        suggestions.append("Focus on strength and hypertrophy workouts with heavy weights.\n");
        if ("Moderate".equals(activityLevel)) {
            suggestions.append("Do 4 days of strength training with compound lifts like squats, deadlifts, bench press, and rows.\n");
            suggestions.append("Add accessory movements like bicep curls, tricep dips, and leg extensions to build muscle.\n");
        } else if ("High".equals(activityLevel)) {
            suggestions.append("Go for 5-6 days of heavy lifting with 3 days focusing on strength and 3 days on hypertrophy.\n");
            suggestions.append("Incorporate supersets to increase muscle endurance and time under tension.\n");
        }
        suggestions.append("Remember to progressively overload your muscles to induce growth.\n");
    }

    private static void appendCardioSuggestions(StringBuilder suggestions, String activityLevel) {
        suggestions.append("Focus on improving cardiovascular health and stamina.\n");
        suggestions.append("For beginners, start with 3-4 days of moderate cardio (30-45 minutes).\n");
        if ("High".equals(activityLevel)) {
            suggestions.append("For high activity level, aim for 5-6 days of varied cardio—mix steady-state cardio with HIIT.\n");
        }
        suggestions.append("Consider adding low-impact activities like swimming or cycling for variety.\n");
    }

    public static String generateNutritionSuggestion(User user, PerformanceStats stats) {
        StringBuilder suggestions = new StringBuilder();
        String goal = user.getGoal();

        switch (goal) {
            case "Weight Loss":
                appendWeightLossNutrition(suggestions);
                break;
            case "Muscle Gain":
                appendMuscleGainNutrition(suggestions);
                break;
            case "Cardio":
                appendCardioNutrition(suggestions);
                break;
            default:
                appendGeneralNutrition(suggestions);
        }
        return suggestions.toString();
    }

    private static void appendWeightLossNutrition(StringBuilder suggestions) {
        suggestions.append("Aim for a calorie deficit by consuming fewer calories than you burn.\n")
                .append("Include lean proteins like chicken breast, turkey, and fish.\n")
                .append("Focus on vegetables, fruits, and complex carbs.\n")
                .append("Avoid sugary foods and drinks.\n")
                .append("Track calories using apps like MyFitnessPal.\n");
    }

    private static void appendMuscleGainNutrition(StringBuilder suggestions) {
        suggestions.append("Focus on a calorie surplus with high-protein foods.\n")
                .append("Consume beef, chicken, eggs, and legumes for muscle growth.\n")
                .append("Include complex carbs like brown rice and sweet potatoes.\n")
                .append("Add healthy fats from avocados, olive oil, and nuts.\n")
                .append("Aim for 5-6 protein-rich meals throughout the day.\n");
    }

    private static void appendCardioNutrition(StringBuilder suggestions) {
        suggestions.append("Balance protein and carbs for energy and recovery.\n")
                .append("Focus on whole grains and lean proteins.\n")
                .append("Stay hydrated with water and electrolyte drinks.\n");
    }

    private static void appendGeneralNutrition(StringBuilder suggestions) {
        suggestions.append("Maintain a balanced diet with varied whole foods.\n")
                .append("Include lean proteins, complex carbs, and healthy fats.\n")
                .append("Stay hydrated and limit processed foods.\n");
    }
}
