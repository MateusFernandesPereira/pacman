
package models;

import java.util.Objects;

/**
 * Representa uma posição no tabuleiro do jogo Pacman.
 * Utilizada para representar coordenadas de células do labirinto.
 * 
 * author UNESP - Estruturas de Dados II
 */
public class Position {
    private final int x;
    private final int y;
    
    /**
     * Construtor da posição.
     * param x coordenada x (coluna)
     * param y coordenada y (linha)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * return coordenada x (coluna)
     */
    public int getX() {
        return x;
    }
    
    /**
     * return coordenada y (linha)
     */
    public int getY() {
        return y;
    }
    
    /**
     * Calcula a distância de Manhattan entre esta posição e outra.
     * A distância de Manhattan é a soma das diferenças absolutas das coordenadas.
     * É utilizada como heurística no algoritmo A*.
     * 
     * Complexidade: O(1)
     * 
     * param other outra posição
     * return distância de Manhattan
     */
    public int manhattanDistance(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }
    
    /**
     * Calcula a distância euclidiana ao quadrado (evita raiz quadrada para performance).
     * 
     * Complexidade: O(1)
     * 
     * param other outra posição
     * return distância euclidiana ao quadrado
     */
    public double distanceSquared(Position other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return dx * dx + dy * dy;
    }
    
    /**
     * Cria uma nova posição movendo na direção especificada.
     * 
     * param direction direção do movimento
     * return nova posição após o movimento
     */
    public Position move(Direction direction) {
        return new Position(x + direction.getDx(), y + direction.getDy());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
