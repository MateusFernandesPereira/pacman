
package models;

/**
 * Enumeração das direções possíveis no jogo Pacman.
 * Cada direção possui um deslocamento (dx, dy) e um caractere associado.
 * 
 */
public enum Direction {
    UP('U', 0, -1),
    DOWN('D', 0, 1),
    LEFT('L', -1, 0),
    RIGHT('R', 1, 0);
    
    private final char code;
    private final int dx;
    private final int dy;
    
    /**
     * Construtor privado do enum.
     * param code código da direção (U, D, L, R)
     * param dx deslocamento no eixo x
     * param dy deslocamento no eixo y
     */
    Direction(char code, int dx, int dy) {
        this.code = code;
        this.dx = dx;
        this.dy = dy;
    }
    
    /**
     * return código da direção (U, D, L, R)
     */
    public char getCode() {
        return code;
    }
    
    /**
     * return deslocamento no eixo x
     */
    public int getDx() {
        return dx;
    }
    
    /**
     * return deslocamento no eixo y
     */
    public int getDy() {
        return dy;
    }
    
    /**
     * Retorna a direção oposta à atual.
     * Útil para evitar que fantasmas voltem instantaneamente.
     * 
     * return direção oposta
     */
    public Direction opposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return this;
        }
    }
    
    /**
     * Converte um caractere em uma direção.
     * 
     * param code código da direção (U, D, L, R)
     * return direção correspondente, ou null se inválido
     */
    public static Direction fromCode(char code) {
        for (Direction dir : values()) {
            if (dir.code == code) {
                return dir;
            }
        }
        return null;
    }
    
    /**
     * Retorna todas as direções possíveis como array.
     * 
     * return array com todas as 4 direções
     */
    public static Direction[] all() {
        return values();
    }
}
