
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

    /**
     * Carrega o ranking do arquivo JSON.
     * 
     * return lista de entradas do ranking
     */
    public static List<ScoreEntry> loadRanking() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type listType = new TypeToken<List<ScoreEntry>>(){}.getType();
            List<ScoreEntry> ranking = gson.fromJson(reader, listType);
            return ranking != null ? ranking : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Salva uma pontuação no ranking.
     * 
     * param name nome do jogador (até 3 letras)
     * param score pontuação obtida
     */
    public static void saveScore(String name, int score) {
        List<ScoreEntry> ranking = loadRanking();

        boolean updated = false;

        // Atualiza se já existe entrada para este jogador
        for (ScoreEntry entry : ranking) {
            if (entry.name.equalsIgnoreCase(name)) {
                if (score > entry.score) {
                    entry.score = score;
                    updated = true;
                }
                break;
            }
        }

        // Adiciona nova entrada se não existe
        if (!updated && ranking.stream().noneMatch(e -> e.name.equalsIgnoreCase(name))) {
            ranking.add(new ScoreEntry(name, score));
        }

        // Ordena por pontuação decrescente
        ranking.sort((a, b) -> b.score - a.score);

        // Limita a 10 entradas
        if (ranking.size() > MAX_ENTRIES) {
            ranking = ranking.subList(0, MAX_ENTRIES);
        }

        // Salva no arquivo
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(ranking, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpa todo o ranking.
     */
    public static void clearRanking() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            writer.write("[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
