// File: src/main/java/api/ExerciseDBAPI.java
package API;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;

public class ExerciseDBAPI {
    private static final Logger logger = Logger.getLogger(ExerciseDBAPI.class.getName());
    private static final String API_KEY = "5b91f27b0bmsha11aadbecf0a4a6p15d097jsne11345af094b"; // Replace with your actual API key
    private static final String BASE_URL = "https://exercisedb.p.rapidapi.com/exercises";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    // Cache for exercise data to minimize API calls
    private static Map<String, String> exerciseCache = new HashMap<>();

    public static String getExercises(String bodyPart, String equipment) {
        try {
            // Check cache first
            String cacheKey = bodyPart + "-" + equipment;
            if (exerciseCache.containsKey(cacheKey)) {
                return exerciseCache.get(cacheKey);
            }

            // Format body part for API
            bodyPart = formatBodyPart(bodyPart);

            // Construct the endpoint
            String endpoint = String.format("%s/bodyPart/%s", BASE_URL, bodyPart);

            // Build the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("X-RapidAPI-Key", API_KEY)
                    .header("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            // Send request and handle response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Handle different response codes
            switch (response.statusCode()) {
                case 200:
                    String filteredExercises = filterExercisesByEquipment(response.body(), equipment);
                    exerciseCache.put(cacheKey, filteredExercises);
                    return filteredExercises;

                case 400:
                    logger.warning("Bad request - Invalid body part or parameters");
                    return getDefaultExercises(bodyPart, equipment);

                case 401:
                    logger.warning("Unauthorized - Check API key");
                    return "Error: API authentication failed. Please check API key.";

                case 429:
                    logger.warning("Rate limit exceeded");
                    return "Error: Rate limit exceeded. Please try again later.";

                default:
                    logger.warning("API request failed with status code: " + response.statusCode());
                    return getDefaultExercises(bodyPart, equipment);
            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error fetching exercises", e);
            return getDefaultExercises(bodyPart, equipment);
        }
    }

    private static String formatBodyPart(String bodyPart) {
        // Convert internal names to API expected format
        switch (bodyPart.toLowerCase()) {
            case "push":
                return "chest";
            case "pull":
                return "back";
            case "legs":
                return "upper legs";
            case "upper body":
                return "upper arms";
            case "lower body":
                return "lower legs";
            case "core":
                return "waist";
            case "cardio":
                return "cardio";
            default:
                return "full body";
        }
    }

    private static String filterExercisesByEquipment(String jsonResponse, String equipment) {
        try {
            var exercises = mapper.readTree(jsonResponse);
            StringBuilder filteredExercises = new StringBuilder();

            for (var exercise : exercises) {
                if (exercise.get("equipment").asText().equalsIgnoreCase(equipment)) {
                    filteredExercises.append("Exercise: ").append(exercise.get("name").asText()).append("\n");
                    filteredExercises.append("Target: ").append(exercise.get("target").asText()).append("\n");

                    var instructions = exercise.get("instructions");
                    if (instructions != null && instructions.isArray()) {
                        filteredExercises.append("Instructions:\n");
                        for (int i = 0; i < instructions.size(); i++) {
                            filteredExercises.append(i + 1).append(". ")
                                    .append(instructions.get(i).asText()).append("\n");
                        }
                    }
                    filteredExercises.append("\n");
                }
            }

            return filteredExercises.length() > 0 ?
                    filteredExercises.toString() :
                    getDefaultExercises(exercises.get(0).get("bodyPart").asText(), equipment);

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error processing exercises JSON", e);
            return getDefaultExercises("full body", equipment);
        }
    }

    private static String getDefaultExercises(String bodyPart, String equipment) {
        // Provide default exercises when API fails

        StringBuilder defaultExercises = new StringBuilder();
        defaultExercises.append("Default exercises for ")
                .append(bodyPart)
                .append(" with ")
                .append(equipment)
                .append(":\n\n");

        switch (bodyPart.toLowerCase()) {
            case "chest":
                defaultExercises.append("Exercise: Bench Press\n");
                defaultExercises.append("Target: Chest\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Lie on bench with feet flat on the floor\n");
                defaultExercises.append("2. Grip the bar slightly wider than shoulder width\n");
                defaultExercises.append("3. Lower the bar to chest level\n");
                defaultExercises.append("4. Press the bar back up to starting position\n");
                defaultExercises.append("5. Repeat for desired reps\n\n");

                defaultExercises.append("Exercise: Push-Ups\n");
                defaultExercises.append("Target: Chest\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Start in plank position\n");
                defaultExercises.append("2. Lower body until chest nearly touches ground\n");
                defaultExercises.append("3. Push back up to starting position\n");
                defaultExercises.append("4. Maintain straight body throughout movement\n\n");
                break;

            case "back":
                defaultExercises.append("Exercise: Bent Over Rows\n");
                defaultExercises.append("Target: Back\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Bend at hips and knees, keeping back straight\n");
                defaultExercises.append("2. Grip weight with palms facing down\n");
                defaultExercises.append("3. Pull weight to lower chest\n");
                defaultExercises.append("4. Lower weight back down with control\n");
                defaultExercises.append("5. Repeat for desired reps\n\n");

                defaultExercises.append("Exercise: Lat Pulldowns\n");
                defaultExercises.append("Target: Back\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Sit at lat pulldown machine\n");
                defaultExercises.append("2. Grip bar wider than shoulder width\n");
                defaultExercises.append("3. Pull bar down to upper chest\n");
                defaultExercises.append("4. Control the weight back up\n");
                defaultExercises.append("5. Maintain proper posture throughout\n\n");
                break;

            case "legs":
                defaultExercises.append("Exercise: Squats\n");
                defaultExercises.append("Target: Legs\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Stand with feet shoulder-width apart\n");
                defaultExercises.append("2. Lower body by bending knees and hips\n");
                defaultExercises.append("3. Keep chest up and back straight\n");
                defaultExercises.append("4. Push through heels to return to start\n");
                defaultExercises.append("5. Maintain control throughout movement\n\n");

                defaultExercises.append("Exercise: Lunges\n");
                defaultExercises.append("Target: Legs\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Stand with feet hip-width apart\n");
                defaultExercises.append("2. Step forward with one leg\n");
                defaultExercises.append("3. Lower back knee toward ground\n");
                defaultExercises.append("4. Push back to starting position\n");
                defaultExercises.append("5. Alternate legs\n\n");
                break;

            case "shoulders":
                defaultExercises.append("Exercise: Overhead Press\n");
                defaultExercises.append("Target: Shoulders\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Stand with feet shoulder-width apart\n");
                defaultExercises.append("2. Hold weights at shoulder height\n");
                defaultExercises.append("3. Press weights overhead\n");
                defaultExercises.append("4. Lower weights back to shoulders\n");
                defaultExercises.append("5. Maintain core stability\n\n");

                defaultExercises.append("Exercise: Lateral Raises\n");
                defaultExercises.append("Target: Shoulders\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Stand holding dumbbells at sides\n");
                defaultExercises.append("2. Raise arms out to sides\n");
                defaultExercises.append("3. Stop at shoulder height\n");
                defaultExercises.append("4. Lower with control\n");
                defaultExercises.append("5. Keep slight bend in elbows\n\n");
                break;

            case "arms":
                defaultExercises.append("Exercise: Bicep Curls\n");
                defaultExercises.append("Target: Arms\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Stand holding weights at sides\n");
                defaultExercises.append("2. Curl weights toward shoulders\n");
                defaultExercises.append("3. Keep elbows close to body\n");
                defaultExercises.append("4. Lower with control\n");
                defaultExercises.append("5. Maintain proper form\n\n");

                defaultExercises.append("Exercise: Tricep Extensions\n");
                defaultExercises.append("Target: Arms\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Hold weight overhead\n");
                defaultExercises.append("2. Lower weight behind head\n");
                defaultExercises.append("3. Keep elbows pointing forward\n");
                defaultExercises.append("4. Extend arms fully\n");
                defaultExercises.append("5. Control the movement\n\n");
                break;

            case "core":
                defaultExercises.append("Exercise: Planks\n");
                defaultExercises.append("Target: Core\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Start in forearm plank position\n");
                defaultExercises.append("2. Keep body in straight line\n");
                defaultExercises.append("3. Engage core muscles\n");
                defaultExercises.append("4. Hold position\n");
                defaultExercises.append("5. Maintain proper breathing\n\n");

                defaultExercises.append("Exercise: Russian Twists\n");
                defaultExercises.append("Target: Core\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Sit with knees bent\n");
                defaultExercises.append("2. Lean back slightly\n");
                defaultExercises.append("3. Twist torso side to side\n");
                defaultExercises.append("4. Keep feet off ground\n");
                defaultExercises.append("5. Control the movement\n\n");
                break;

            default:
                defaultExercises.append("Exercise: Bodyweight Squats\n");
                defaultExercises.append("Target: Full Body\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Stand with feet shoulder-width apart\n");
                defaultExercises.append("2. Lower body by bending knees and hips\n");
                defaultExercises.append("3. Keep chest up and back straight\n");
                defaultExercises.append("4. Return to starting position\n");
                defaultExercises.append("5. Repeat for desired reps\n\n");

                defaultExercises.append("Exercise: Push-Ups\n");
                defaultExercises.append("Target: Full Body\n");
                defaultExercises.append("Instructions:\n");
                defaultExercises.append("1. Start in plank position\n");
                defaultExercises.append("2. Lower chest to ground\n");
                defaultExercises.append("3. Push back up\n");
                defaultExercises.append("4. Keep body straight\n");
                defaultExercises.append("5. Maintain core tension\n\n");
                break;
        }

        return defaultExercises.toString();
    }

    public static boolean testConnection() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/bodyPart/chest"))
                    .header("X-RapidAPI-Key", API_KEY)
                    .header("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 200;

        } catch (Exception e) {
            logger.log(Level.WARNING, "API connection test failed", e);
            return false;
        }
    }

    // Utility method to clear cache if needed
    public static void clearCache() {
        exerciseCache.clear();
    }
}
