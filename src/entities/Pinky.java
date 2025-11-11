
package entities;

import managers.PathfindingManager;
import models.Direction;
import java.awt.Image;

public class Pinky extends Ghost {
    private static final int PREDICTION_TILES = 4;
    
    /**
     * Construtor do Pinky.
     * 
     * param image imagem do fantasma rosa
     * param x posição inicial x
     * param y posição inicial y
     * param tileSize tamanho do tile
     * param pathfindingManager gerenciador de pathfinding
     */
    public Pinky(Image image, int x, int y, int tileSize, PathfindingManager pathfindingManager) {
        super(image, x, y, tileSize, pathfindingManager);
    }
    
    /**
     * Escolhe a direção usando A* para emboscar o Pacman.
     * 
     * Pinky prevê onde o Pacman estará baseado em sua direção atual
     * e usa A* para chegar ao ponto de emboscada de forma eficiente.
     * 
     * param pacmanX posição x do Pacman
     * param pacmanY posição y do Pacman
     * param pacmanDirection direção atual do Pacman
     * return direção para emboscar o Pacman
     */
    @Override
    public Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
        // Calcula posição de emboscada (4 tiles à frente do Pacman)
        int targetX = pacmanX;
        int targetY = pacmanY;
        
        if (pacmanDirection != null) {
            targetX += pacmanDirection.getDx() * tileSize * PREDICTION_TILES;
            targetY += pacmanDirection.getDy() * tileSize * PREDICTION_TILES;
        }
        
        // Usa A* para calcular caminho até o ponto de emboscada
        Direction nextDir = pathfindingManager.getNextDirectionAStar(x, y, targetX, targetY);
        
        // Se não encontrou caminho para a emboscada, persegue diretamente
        if (nextDir == null) {
            nextDir = pathfindingManager.getNextDirectionAStar(x, y, pacmanX, pacmanY);
        }
        
        // Se ainda não encontrou, mantém direção atual
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
        return "A* (A-Star)";
    }
}
