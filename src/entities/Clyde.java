
package entities;

import managers.PathfindingManager;
import models.Direction;
import java.awt.Image;

public class Clyde extends Ghost {
    private static final int FLEE_DISTANCE = 8; // tiles
    private final int cornerX;
    private final int cornerY;
    
    /**
     * Construtor do Clyde.
     * 
     * param image imagem do fantasma laranja
     * param x posição inicial x
     * param y posição inicial y
     * param tileSize tamanho do tile
     * param pathfindingManager gerenciador de pathfinding
     */
    public Clyde(Image image, int x, int y, int tileSize, PathfindingManager pathfindingManager) {
        super(image, x, y, tileSize, pathfindingManager);
        // Canto inferior esquerdo como ponto de fuga
        this.cornerX = tileSize;
        this.cornerY = 19 * tileSize;
    }
    
    /**
     * Escolhe a direção usando BFS com comportamento misto.
     * 
     * Clyde usa BFS para calcular a distância até o Pacman e então decide:
     * - Se longe: persegue usando BFS
     * - Se perto: foge para o canto usando BFS
     * 
     * param pacmanX posição x do Pacman
     * param pacmanY posição y do Pacman
     * param pacmanDirection direção do Pacman (não utilizada)
     * return direção escolhida
     */
    @Override
    public Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
        // Calcula distância até o Pacman usando BFS
        int distance = pathfindingManager.getDistanceBFS(x, y, pacmanX, pacmanY);
        
        Direction nextDir = null;
        
        if (distance < 0) {
            // Não há caminho, mantém direção atual
            return direction;
        }
        
        if (distance > FLEE_DISTANCE) {
            // Longe: persegue o Pacman
            nextDir = pathfindingManager.getNextDirectionBFS(x, y, pacmanX, pacmanY);
        } else {
            // Perto: foge para o canto
            nextDir = pathfindingManager.getNextDirectionBFS(x, y, cornerX, cornerY);
        }
        
        // Se não encontrou direção, mantém a atual
        if (nextDir == null) {
            return direction;
        }
        
        // Evita reverter instantaneamente
        if (nextDir == direction.opposite()) {
            return direction;
        }
        
        return nextDir;
    }
    
    @Override
    public String getAlgorithmName() {
        return "BFS (Breadth-First Search)";
    }
}
