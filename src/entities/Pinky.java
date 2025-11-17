
package entities;

import managers.PathfindingManager;
import models.Direction;

import java.awt.*;

/**
 * Pinky - O Emboscador (Fantasma Rosa)
 * 
 * Personalidade: Estrategico, astuto, tatico
 * Algoritmo: A*
 * Estrategia: Tenta emboscar o Pacman prevendo sua posicao futura
 * 
 * Pinky calcula 4 tiles a frente da direcao do Pacman e usa A* 
 * para chegar la rapidamente.
 */
public class Pinky extends Ghost {

    public Pinky(Image image, int x, int y, int width, int height, int tileSize, 
                 PathfindingManager pathfindingManager) {
        super(image, x, y, width, height, tileSize, pathfindingManager, 
              "Pinky", Color.PINK);
    }

    @Override
    protected Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
        // Previsao: calcular 4 tiles a frente da direcao do Pacman
        int targetX = pacmanX + (pacmanDirection.dx * tileSize * 4);
        int targetY = pacmanY + (pacmanDirection.dy * tileSize * 4);

        // Usar A* para emboscada eficiente
        Direction nextDir = pathfindingManager.getNextDirectionAStar(
            this.x, this.y, targetX, targetY
        );

        // Se A* nao retornou uma direcao valida, tentar ir direto ao Pacman
        if (nextDir == Direction.NONE) {
            nextDir = pathfindingManager.getNextDirectionAStar(
                this.x, this.y, pacmanX, pacmanY
            );
        }

        // Se ainda nao temos uma direcao valida, manter a atual
        if (nextDir == Direction.NONE) {
            return this.direction;
        }

        return nextDir;
    }
}
