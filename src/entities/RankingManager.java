package entities;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class RankingManager {
    private static final String FILE_NAME = "ranking.json";
    private static final int MAX_ENTRIES = 10;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<ScoreEntry> loadRanking() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type listType = new TypeToken<List<ScoreEntry>>(){}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void saveScore(String name, int score) {
        List<ScoreEntry> ranking = loadRanking();

        boolean updated = false;

        for (ScoreEntry entry : ranking) {
            if (entry.name.equalsIgnoreCase(name)) {
                if (score > entry.score) {
                    entry.score = score; // atualiza a pontuação
                    updated = true;
                }
                break;
            }
        }

        if (!updated && ranking.stream().noneMatch(e -> e.name.equalsIgnoreCase(name))) {
            ranking.add(new ScoreEntry(name, score));
        }

        // Ordena por pontuação decrescente
        ranking.sort((a, b) -> b.score - a.score);

        // Limita a 10 entradas
        if (ranking.size() > MAX_ENTRIES) {
            ranking = ranking.subList(0, MAX_ENTRIES);
        }

        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(ranking, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void clearRanking() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            writer.write("[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
