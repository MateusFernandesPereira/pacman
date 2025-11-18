
package entities;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Gerenciador de ranking de pontuacoes.
 * Usa GSON para persistir dados em JSON.
 */
public class RankingManager {
    private static final String FILE_NAME = "ranking.json";
    private static final int MAX_ENTRIES = 10;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Carrega o ranking do arquivo JSON.
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
     * Salva uma nova pontuacao no ranking.
     */
    public static void saveScore(String name, int score) {
        List<ScoreEntry> ranking = loadRanking();

        boolean updated = false;

        // Verificar se o jogador ja existe no ranking
        for (ScoreEntry entry : ranking) {
            if (entry.name.equalsIgnoreCase(name)) {
                if (score > entry.score) {
                    entry.score = score; // Atualizar se a nova pontuacao for maior
                    updated = true;
                }
                break;
            }
        }

        // Se nao existia, adicionar novo
        if (!updated && ranking.stream().noneMatch(e -> e.name.equalsIgnoreCase(name))) {
            ranking.add(new ScoreEntry(name, score));
        }

        // Ordenar por pontuacao decrescente
        ranking.sort((a, b) -> b.score - a.score);

        // Limitar a 10 entradas
        if (ranking.size() > MAX_ENTRIES) {
            ranking = ranking.subList(0, MAX_ENTRIES);
        }

        // Salvar no arquivo
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
