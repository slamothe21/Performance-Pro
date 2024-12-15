package AI;
import models.User;
import models.PerformanceStats;

public class NutritionAI {
    public static String generateAINutritionPlan(User user, PerformanceStats stats) {
        StringBuilder plan = new StringBuilder();

        // Calculate caloric needs and macros
        double targetCalories = calculateDailyCalories(user, stats);
        MacroSplit macros = calculateMacroSplit(user, targetCalories);

        // Generate comprehensive nutrition plan
        plan.append(generateDailyNutritionOverview(user, targetCalories, macros));
        plan.append("\n").append(generateMealTimingPlan(user));
        plan.append("\n").append(generateSupplementPlan(user, stats));
        plan.append("\n").append(generateProgressionGuidelines(user, stats));

        return plan.toString();
    }

    private static double calculateDailyCalories(User user, PerformanceStats stats) {
        // Calculate BMR using Katch-McArdle Formula (uses lean body mass)
        double bmr = 370 + (21.6 * calculateLeanMass(user));

        // Apply activity multiplier
        double activityMultiplier;
        switch (user.getActivityLevel()) {
            case "Low":
                activityMultiplier = 1.2;
                break;
            case "Moderate":
                activityMultiplier = 1.55;
                break;
            case "High":
                activityMultiplier = 1.725;
                break;
            default:
                activityMultiplier = 1.2;
                break;
        }

        double tdee = bmr * activityMultiplier;

        // Adjust based on goal and workout intensity
        if ("Weight Loss".equals(user.getGoal())) {
            return tdee * 0.8; // 20% deficit
        } else if ("Muscle Gain".equals(user.getGoal())) {
            return tdee * 1.1 + (stats.getStrengthMax() / 10); // Surplus + adjustment for strength
        } else {
            return tdee;
        }
    }

    private static class MacroSplit {
        final double protein;
        final double carbs;
        final double fats;

        MacroSplit(double protein, double carbs, double fats) {
            this.protein = protein;
            this.carbs = carbs;
            this.fats = fats;
        }
    }

    private static MacroSplit calculateMacroSplit(User user, double calories) {
        double proteinPerKg, carbPercentage, fatPercentage;

        switch (user.getGoal()) {
            case "Weight Loss":
                proteinPerKg = 2.2; // Higher protein for muscle preservation
                carbPercentage = 0.30;
                fatPercentage = 0.30;
                break;
            case "Muscle Gain":
                proteinPerKg = 2.4; // Higher protein for muscle building
                carbPercentage = 0.45;
                fatPercentage = 0.25;
                break;
            default:
                proteinPerKg = 1.8;
                carbPercentage = 0.40;
                fatPercentage = 0.30;
        }

        double protein = user.getWeight() * proteinPerKg; // grams
        double proteinCalories = protein * 4;
        double remainingCalories = calories - proteinCalories;
        double carbCalories = calories * carbPercentage;
        double fatCalories = calories * fatPercentage;

        return new MacroSplit(
                protein,
                carbCalories / 4, // Convert to grams
                fatCalories / 9   // Convert to grams
        );
    }

    private static String generateDailyNutritionOverview(User user, double calories, MacroSplit macros) {
        StringBuilder overview = new StringBuilder("Daily Nutrition Plan:\n\n");

        overview.append(String.format("Target Calories: %.0f calories\n\n", calories));
        overview.append("Macronutrient Targets:\n");
        overview.append(String.format("- Protein: %.0fg (%.0f calories)\n", macros.protein, macros.protein * 4));
        overview.append(String.format("- Carbohydrates: %.0fg (%.0f calories)\n", macros.carbs, macros.carbs * 4));
        overview.append(String.format("- Fats: %.0fg (%.0f calories)\n\n", macros.fats, macros.fats * 9));

        overview.append("Key Focus Areas:\n");
        if ("Weight Loss".equals(user.getGoal())) {
            overview.append("- Maintain high protein intake for muscle preservation\n");
            overview.append("- Focus on fiber-rich foods for satiety\n");
            overview.append("- Time carbohydrates around workouts\n");
        } else if ("Muscle Gain".equals(user.getGoal())) {
            overview.append("- Prioritize protein distribution throughout the day\n");
            overview.append("- Include fast-digesting carbs post-workout\n");
            overview.append("- Ensure adequate healthy fats for hormone production\n");
        }

        return overview.toString();
    }

    private static String generateMealTimingPlan(User user) {
        StringBuilder timing = new StringBuilder("Meal Timing Strategy:\n\n");

        if ("Weight Loss".equals(user.getGoal())) {
            timing.append("Recommended Meal Schedule:\n");
            timing.append("1. Breakfast (25%): Focus on protein and fiber\n");
            timing.append("2. Lunch (35%): Largest meal with complex carbs\n");
            timing.append("3. Pre-workout snack (10%): Light, easily digestible\n");
            timing.append("4. Post-workout (15%): Protein and simple carbs\n");
            timing.append("5. Dinner (15%): Protein and vegetables\n");
        } else if ("Muscle Gain".equals(user.getGoal())) {
            timing.append("Recommended Meal Schedule:\n");
            timing.append("1. Breakfast (20%): High protein and carbs\n");
            timing.append("2. Mid-morning (15%): Protein and fats\n");
            timing.append("3. Lunch (25%): Complete meal\n");
            timing.append("4. Pre-workout (15%): Carb-focused\n");
            timing.append("5. Post-workout (15%): Protein and fast carbs\n");
            timing.append("6. Dinner (10%): Protein and slow carbs\n");
        }

        return timing.toString();
    }

    private static String generateSupplementPlan(User user, PerformanceStats stats) {
        StringBuilder supplements = new StringBuilder("Supplement Recommendations:\n\n");

        // Base supplements for all
        supplements.append("Core Supplements:\n");
        supplements.append("- Multivitamin: Daily with breakfast\n");
        supplements.append("- Omega-3: 2-3g daily with meals\n");

        // Goal-specific supplements
        supplements.append("\nGoal-Specific Supplements:\n");
        if ("Weight Loss".equals(user.getGoal())) {
            supplements.append("- Whey Protein: 25-30g post-workout\n");
            supplements.append("- L-Carnitine: 2g pre-workout\n");
            supplements.append("- Green Tea Extract: Morning and afternoon\n");
        } else if ("Muscle Gain".equals(user.getGoal())) {
            supplements.append("- Whey/Casein Blend: 30-40g post-workout\n");
            supplements.append("- Creatine Monohydrate: 5g daily\n");
            supplements.append("- BCAAs: During workouts\n");
            supplements.append("- Beta-Alanine: 3-5g daily\n");
        }

        return supplements.toString();
    }

    private static String generateProgressionGuidelines(User user, PerformanceStats stats) {
        StringBuilder guidelines = new StringBuilder("Nutrition Progression Guidelines:\n\n");

        guidelines.append("Weekly Adjustments:\n");
        if ("Weight Loss".equals(user.getGoal())) {
            guidelines.append("- Monitor weight loss (target: 0.5-1% body weight per week)\n");
            guidelines.append("- Adjust calories down 10% if plateau occurs\n");
            guidelines.append("- Increase protein if strength decreases\n");
        } else if ("Muscle Gain".equals(user.getGoal())) {
            guidelines.append("- Target weight gain: 0.25-0.5% body weight per week\n");
            guidelines.append("- Increase calories 5-10% if weight plateaus\n");
            guidelines.append("- Adjust carbs based on workout performance\n");
        }

        guidelines.append("\nMonitoring Metrics:\n");
        guidelines.append("- Weekly weight measurements\n");
        guidelines.append("- Progress photos every 2-4 weeks\n");
        guidelines.append("- Strength tracking\n");
        guidelines.append("- Energy levels and recovery quality\n");

        return guidelines.toString();
    }

    private static double calculateLeanMass(User user) {
        if (user == null) {
            return 0.0;
        }

        // Estimate body fat percentage based on activity level
        double estimatedBodyFat;
        switch (user.getActivityLevel()) {
            case "High":
                estimatedBodyFat = 0.15;    // 15% body fat
                break;
            case "Moderate":
                estimatedBodyFat = 0.20;    // 20% body fat
                break;
            default:
                estimatedBodyFat = 0.25;    // 25% body fat
                break;
        }

        double weight = user.getWeight();
        double leanMass = weight * (1.0 - estimatedBodyFat);

        // Ensure we don't return negative values
        return Math.max(0.0, leanMass);
    }
}

