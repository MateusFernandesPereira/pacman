package entities;

public class ScoreEntry {
    public String name;
    public int score;

    /**
     * Construtor da entrada de pontuação.
     * 
     * @param name nome do jogador
     * @param score pontuação
     */
    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
