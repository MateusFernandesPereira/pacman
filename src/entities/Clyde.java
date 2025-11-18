
package entities;

import managers.PathfindingManager;
import models.Direction;

import java.awt.*;

/**
 * Clyde - O Timido (Fantasma Laranja)
 * 
 * Personalidade: Timido, indeciso, covarde
 * Algoritmo: BFS
 * Estrategia: Comportamento misto baseado em distancia
 * 
 * - Longe do Pacman (> 8 tiles): Persegue usando BFS
 * - Perto do Pacman (<= 8 tiles): Foge para o canto inferior esquerdo
 * 
 * Este comportamento cria um fantasma menos ameacador e mais interessante.
 */
public class Clyde extends Ghost {
    private static final int FLEE_DISTANCE = 8; // Distancia em tiles para comecar a fugir
    private int cornerX;
    private int cornerY;

    public Clyde(Image image, int x, int y, int width, int height, int tileSize, 
                 PathfindingManager pathfindingManager, int level) {
        super(image, x, y, width, height, tileSize, pathfindingManager, 
              "Clyde", Color.ORANGE, level);
        // Canto inferior esquerdo como ponto de fuga
        this.cornerX = tileSize;
        this.cornerY = tileSize * 19;
    }

    @Override
    protected Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
        // Calcular distancia ate o Pacman usando BFS
        int distance = pathfindingManager.getDistanceBFS(this.x, this.y, pacmanX, pacmanY);

        Direction nextDir;

        if (distance > FLEE_DISTANCE || distance == -1) {
            // Longe: perseguir o Pacman
            nextDir = pathfindingManager.getNextDirectionBFS(
                this.x, this.y, pacmanX, pacmanY
            );
        } else {
            // Perto: fugir para o canto
            nextDir = pathfindingManager.getNextDirectionBFS(
                this.x, this.y, cornerX, cornerY
            );
        }

        // Se BFS nao retornou uma direcao valida, manter a atual
        if (nextDir == Direction.NONE) {
            return this.direction;
        }

        return nextDir;
    }
}
