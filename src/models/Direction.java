
package models;

/**
 * Enum que representa as 4 direcoes possiveis de movimento.
 */
public enum Direction {
    UP(0, -1, 'U'),
    DOWN(0, 1, 'D'),
    LEFT(-1, 0, 'L'),
    RIGHT(1, 0, 'R'),
    NONE(0, 0, 'N');

    public final int dx;  // Delta X
    public final int dy;  // Delta Y
    public final char code; // Codigo de caractere

    Direction(int dx, int dy, char code) {
        this.dx = dx;
        this.dy = dy;
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    /**
     * Converte um codigo de caractere em Direction.
     */
    public static Direction fromCode(char code) {
        for (Direction dir : values()) {
            if (dir.code == code) return dir;
        }
        return NONE;
    }

    /**
     * Retorna a direcao oposta.
     */
    public Direction opposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return NONE;
        }
    }
}
