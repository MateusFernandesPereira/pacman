
package entities;

import managers.PathfindingManager;
import models.Direction;

import java.awt.*;

/**
 * Blinky - O Perseguidor (Fantasma Vermelho)
 * 
 * Personalidade: Agressivo, direto, implacavel
 * Algoritmo: Dijkstra
 * Estrategia: Persegue o Pacman diretamente pelo caminho mais curto
 * 
 * Blinky eh o fantasma mais perigoso porque sempre usa o caminho otimo.
 */
public class Blinky extends Ghost {

    public Blinky(Image image, int x, int y, int width, int height, int tileSize, 
                  PathfindingManager pathfindingManager, int level) {
        super(image, x, y, width, height, tileSize, pathfindingManager, 
              "Blinky", Color.RED, level);
    }

    @Override
    protected Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
        // Perseguicao direta usando Dijkstra - sempre o caminho mais curto
        Direction nextDir = pathfindingManager.getNextDirectionDijkstra(
            this.x, this.y, pacmanX, pacmanY
        );

        // Se Dijkstra nao retornou uma direcao valida, manter a direcao atual
        if (nextDir == Direction.NONE) {
            return this.direction;
        }

        return nextDir;
    }
}
