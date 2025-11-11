
package entities;

import managers.PathfindingManager;
import models.Direction;
import java.awt.Image;

public class Blinky extends Ghost {
    
    /**
     * Construtor do Blinky.
     * 
     * param image imagem do fantasma vermelho
     * param x posição inicial x
     * param y posição inicial y
     * param tileSize tamanho do tile
     * param pathfindingManager gerenciador de pathfinding
     */
    public Blinky(Image image, int x, int y, int tileSize, PathfindingManager pathfindingManager) {
        super(image, x, y, tileSize, pathfindingManager);
    }
    
    /**
     * Escolhe a direção usando Dijkstra para perseguir o Pacman.
     * 
     * O algoritmo de Dijkstra garante que Blinky sempre tome o caminho
     * mais curto até o Pacman, tornando-o o fantasma mais perigoso.
     * 
     * param pacmanX posição x do Pacman
     * param pacmanY posição y do Pacman
     * param pacmanDirection direção do Pacman (não utilizada por Blinky)
     * return direção para perseguir o Pacman
     */
    @Override
    public Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
        // Blinky persegue diretamente o Pacman usando Dijkstra
        Direction nextDir = pathfindingManager.getNextDirectionDijkstra(x, y, pacmanX, pacmanY);
        
        // Se não encontrou caminho, continua na direção atual
        if (nextDir == null) {
            return direction;
        }
        
        // Evita reverter instantaneamente (comportamento mais natural)
        if (nextDir == direction.opposite()) {
            return direction;
        }
        
        return nextDir;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Dijkstra";
    }
}
