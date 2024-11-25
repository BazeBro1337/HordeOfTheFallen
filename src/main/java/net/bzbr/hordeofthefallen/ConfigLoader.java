package net.bzbr.hordeofthefallen;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {
    private static final File CONFIG_FILE = new File("config/hordeofthefallen.json");
    private static int spawnRadius = 100;
    private static int waveIntervalDays = 7;
    private static final List<MobConfig> waveMobs = new ArrayList<>();

    public static void loadConfig() {
        if (!CONFIG_FILE.exists()) {

            var defaultConfObject = getJsonObject();

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                Gson gson = new Gson();
                gson.toJson(defaultConfObject, writer);  // Запись объекта в JSON
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            JsonObject json = new Gson().fromJson(reader, JsonObject.class);
            spawnRadius = json.get("spawnRadius").getAsInt();
            waveIntervalDays = json.get("waveIntervalDays").getAsInt();

            waveMobs.clear();
            JsonArray mobsArray = json.getAsJsonArray("waveMobs");
            for (int i = 0; i < mobsArray.size(); i++) {
                JsonObject mobObj = mobsArray.get(i).getAsJsonObject();
                String mobId = mobObj.get("mobId").getAsString();
                int count = mobObj.get("count").getAsInt();
                int minDay = mobObj.get("minDay").getAsInt();
                waveMobs.add(new MobConfig(mobId, count, minDay));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static @NotNull JsonObject getJsonObject() {
        var waveMobsJson = new JsonArray();

        JsonObject mob = new JsonObject();
        mob.addProperty("mobId", "minecraft:zombie");
        mob.addProperty("count", 10);
        mob.addProperty("minDay", 1);

        waveMobsJson.add(mob);

        var defaultConfObject = new JsonObject();
        defaultConfObject.addProperty("spawnRadius", spawnRadius);
        defaultConfObject.addProperty("waveIntervalDays", waveIntervalDays);
        defaultConfObject.add("waveMobs", waveMobsJson);
        return defaultConfObject;
    }

    public static int getSpawnRadius() {
        return spawnRadius;
    }

    public static int getWaveIntervalDays() {
        return waveIntervalDays;
    }

    public static List<MobConfig> getWaveMobs() {
        return waveMobs;
    }

    public record MobConfig(String mobId, int count, int minDay) {
    }
}
