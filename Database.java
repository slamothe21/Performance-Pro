package database;

import models.User;
import models.PerformanceStats;
import org.sqlite.SQLiteDataSource;
import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:performance_tracker.db";

    public static Connection connect() {
        try {
            SQLiteDataSource dataSource = new SQLiteDataSource();
            dataSource.setUrl(URL);
            return dataSource.getConnection();
        } catch (SQLException e) {
            logSQLException(e);
            return null;
        }
    }

    // Create users table
    public static void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "goal TEXT NOT NULL, " +
                "weight REAL NOT NULL, " +
                "height REAL NOT NULL, " +
                "age INTEGER NOT NULL, " +
                "gender TEXT NOT NULL, " +
                "activity_level TEXT NOT NULL" +
                ");";
        executeSQL(sql, "models.User table created.");
    }

    // Create performance_stats table
    public static void createPerformanceStatsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS performance_stats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "strength_max REAL NOT NULL, " +
                "speed_time REAL NOT NULL, " +
                "endurance_duration REAL NOT NULL, " +
                "agility_score REAL NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ");";
        executeSQL(sql, "Performance Stats table created.");
    }

    // Create workouts table
    public static void createWorkoutsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS workouts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "goal TEXT NOT NULL, " +
                "activity_level TEXT NOT NULL, " +
                "exercise_name TEXT NOT NULL, " +
                "duration_minutes INTEGER NOT NULL, " +
                "intensity TEXT NOT NULL" +
                ");";
        executeSQL(sql, "Workouts table created.");
    }

    // Create nutrition table
    public static void createNutritionTable() {
        String sql = "CREATE TABLE IF NOT EXISTS nutrition (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "goal TEXT NOT NULL, " +
                "calorie_range TEXT NOT NULL, " +
                "protein_grams INTEGER NOT NULL, " +
                "carb_grams INTEGER NOT NULL, " +
                "fat_grams INTEGER NOT NULL" +
                ");";
        executeSQL(sql, "Nutrition table created.");
    }

    public static User fetchUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("goal"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("activity_level")
                );
            }
        } catch (SQLException e) {
            logSQLException(e);
        }
        return null;
    }

    // Insert user
    public static void insertUser(User user) {
        String sql = "INSERT INTO users(name, goal, weight, height, age, gender, activity_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getGoal());
            stmt.setDouble(3, user.getWeight());
            stmt.setDouble(4, user.getHeight());
            stmt.setInt(5, user.getAge());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getActivityLevel());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getInt(1));
            }
            System.out.println("models.User inserted: " + user.getName() + " with ID: " + user.getId());
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    // Update user
    public static void updateUser(User user) {
        String sql = "UPDATE users " +
                "SET name = ?, goal = ?, weight = ?, height = ?, age = ?, gender = ?, activity_level = ? " +
                "WHERE id = ?;";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getGoal());
            stmt.setDouble(3, user.getWeight());
            stmt.setDouble(4, user.getHeight());
            stmt.setInt(5, user.getAge());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getActivityLevel());
            stmt.setInt(8, user.getId());
            stmt.executeUpdate();
            System.out.println("models.User updated: " + user.getName() + " with ID: " + user.getId());
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    // Insert performance stats
    public static void insertPerformanceStats(PerformanceStats stats) {
        String sql = "INSERT INTO performance_stats (user_id, strength_max, speed_time, endurance_duration, agility_score) " +
                "VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stats.getUserId());
            stmt.setDouble(2, stats.getStrengthMax());
            stmt.setDouble(3, stats.getSpeedTime());
            stmt.setDouble(4, stats.getEnduranceDuration());
            stmt.setDouble(5, stats.getAgilityScore());
            stmt.executeUpdate();
            System.out.println("Performance stats inserted for user ID: " + stats.getUserId());
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    // Fetch performance stats
    public static PerformanceStats fetchPerformanceStats(int userId) {
        String sql = "SELECT * FROM performance_stats WHERE user_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PerformanceStats(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("strength_max"),
                        rs.getDouble("speed_time"),
                        rs.getDouble("endurance_duration"),
                        rs.getDouble("agility_score")
                );
            }
        } catch (SQLException e) {
            logSQLException(e);
        }
        return null;
    }

    // Insert workout
    public static void insertWorkout(String goal, String activityLevel, String exerciseName, int duration, String intensity) {
        String sql = "INSERT INTO workouts(goal, activity_level, exercise_name, duration_minutes, intensity) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, goal);
            stmt.setString(2, activityLevel);
            stmt.setString(3, exerciseName);
            stmt.setInt(4, duration);
            stmt.setString(5, intensity);
            stmt.executeUpdate();
            System.out.println("Workout inserted: " + exerciseName);
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    // Fetch workouts
    public static void fetchWorkoutSuggestions(String goal, String activityLevel) {
        String sql = "SELECT exercise_name, duration_minutes, intensity " +
                "FROM workouts " +
                "WHERE goal = ? AND activity_level = ?;";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, goal);
            stmt.setString(2, activityLevel);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Workout Suggestions:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("exercise_name") + ": " +
                        rs.getInt("duration_minutes") + " minutes (" + rs.getString("intensity") + ")");
            }
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    // Insert nutrition
    public static void insertNutrition(String goal, String calorieRange, int protein, int carbs, int fat) {
        String sql = "INSERT INTO nutrition (goal, calorie_range, protein_grams, carb_grams, fat_grams) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, goal);
            stmt.setString(2, calorieRange);
            stmt.setInt(3, protein);
            stmt.setInt(4, carbs);
            stmt.setInt(5, fat);
            stmt.executeUpdate();
            System.out.println("Nutrition suggestion inserted for goal: " + goal);
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    // Fetch nutrition
    public static void fetchNutritionSuggestions(String goal) {
        String sql = "SELECT calorie_range, protein_grams, carb_grams, fat_grams " +
                "FROM nutrition " +
                "WHERE goal = ?;";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, goal);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Nutrition Suggestions:");
                System.out.println("- Calories: " + rs.getString("calorie_range"));
                System.out.println("- Protein: " + rs.getInt("protein_grams") + "g");
                System.out.println("- Carbs: " + rs.getInt("carb_grams") + "g");
                System.out.println("- Fat: " + rs.getInt("fat_grams") + "g");
            } else {
                System.out.println("No nutrition suggestions found for goal: " + goal);
            }
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    private static void executeSQL(String sql, String successMessage) {
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            System.out.println(successMessage);
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    private static void logSQLException(SQLException e) {
        System.out.println("SQL error: " + e.getMessage());
        System.out.println("SQL state: " + e.getSQLState());
        System.out.println("Error code: " + e.getErrorCode());
    }
}
