package API;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NutritionixAPI {
    private static final String APP_ID = "62305c96";
    private static final String API_KEY = "\n" +
            "48b3c047f6bd7a2756a7fd2a162b55c4";
    private static final String BASE_URL = "https://trackapi.nutritionix.com/v2";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String getMealNutrition(String query) {
        try {
            String jsonBody = String.format("{\"query\":\"%s\"}", query);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/natural/nutrients"))
                    .header("x-app-id", APP_ID)
                    .header("x-app-key", API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return formatNutritionResponse(response.body());
        } catch (Exception e) {
            return "Error fetching nutrition info: " + e.getMessage();
        }
    }

    private static String formatNutritionResponse(String jsonResponse) {
        try {
            var nutrition = mapper.readTree(jsonResponse).get("foods");
            StringBuilder nutritionInfo = new StringBuilder();

            for (var food : nutrition) {
                nutritionInfo.append("Food: ").append(food.get("food_name").asText()).append("\n");
                nutritionInfo.append("Calories: ").append(food.get("nf_calories").asText()).append("\n");
                nutritionInfo.append("Protein: ").append(food.get("nf_protein").asText()).append("g\n");
                nutritionInfo.append("Carbs: ").append(food.get("nf_total_carbohydrate").asText()).append("g\n");
                nutritionInfo.append("Fat: ").append(food.get("nf_total_fat").asText()).append("g\n\n");
            }

            return nutritionInfo.toString();
        } catch (Exception e) {
            return "Error processing nutrition info: " + e.getMessage();
        }
    }
}
