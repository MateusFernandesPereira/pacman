
package entities;

import managers.PathfindingManager;
import models.Direction;
import java.awt.Image;
import java.util.Random;

public class Inky extends Ghost {
    private static final Random random = new Random();
    private int patrolCounter;
    private static final int PATROL_DURATION = 100; // frames em modo patrulha
    
    /**
     * Construtor do Inky.
     * 
     * param image imagem do fantasma azul/ciano
     * param x posição inicial x
     * param y posição inicial y
     * param tileSize tamanho do tile
     * param pathfindingManager gerenciador de pathfinding
     */
    public Inky(Image image, int x, int y, int tileSize, PathfindingManager pathfindingManager) {
        super(image, x, y, tileSize, pathfindingManager);
        this.patrolCounter = 0;
    }
    
    /**
     * Escolhe a direção usando DFS para patrulhamento errático.
     * 
     * Inky alterna entre patrulhar (usando DFS) e perseguir o Pacman.
     * O uso de DFS cria um comportamento de exploração que parece
     * aleatório, tornando Inky imprevisível.
     * 
     * param pacmanX posição x do Pacman
     * param pacmanY posição y do Pacman
     * param pacmanDirection direção do Pacman (não utilizada)
     * return direção escolhida
     */
    @Override
    public Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
        patrolCounter++;
        
        // Alterna entre patrulhamento e perseguição
        boolean shouldPatrol = (patrolCounter % (PATROL_DURATION * 2)) < PATROL_DURATION;
        
        Direction nextDir = null;
        
        if (shouldPatrol) {
            // Modo patrulha: escolhe alvo aleatório e usa DFS
            int randomX = random.nextInt(19) * tileSize;
            int randomY = random.nextInt(21) * tileSize;
            nextDir = pathfindingManager.getNextDirectionDFS(x, y, randomX, randomY);
        } else {
            // Modo perseguição: usa DFS para perseguir Pacman
            nextDir = pathfindingManager.getNextDirectionDFS(x, y, pacmanX, pacmanY);
        }
        
        // Se não encontrou direção, mantém a atual
        if (nextDir == null) {
            return direction;
        }
        
        // Evita reverter (mas permite ocasionalmente para mais aleatoriedade)
        if (nextDir == direction.opposite() && random.nextInt(100) > 20) {
            return direction;
        }
        
        return nextDir;
    }
    
    @Override
    public String getAlgorithmName() {
        return "DFS (Depth-First Search)";
    }
    
    @Override
    public void reset() {
        super.reset();
        patrolCounter = 0;
    }
}
