
package entities;

import managers.PathfindingManager;
import models.Direction;

import java.awt.*;
import java.util.Random;

/**
 * Inky - O Patrulheiro (Fantasma Azul/Ciano)
 * 
 * Personalidade: Imprevisivel, erratico, explorador
 * Algoritmo: DFS
 * Estrategia: Alterna entre patrulhamento aleatorio e perseguicao
 * 
 * Inky cobre areas do labirinto de forma nao-otima, criando comportamento
 * imprevisivel que dificulta ao jogador prever seus movimentos.
 */
public class Inky extends Ghost {
    private Random random;
    private boolean isPatrolling;
    private int patrolTimer;
    private static final int PATROL_DURATION = 60; // Frames

    public Inky(Image image, int x, int y, int width, int height, int tileSize, 
                PathfindingManager pathfindingManager, int level) {
        super(image, x, y, width, height, tileSize, pathfindingManager, 
              "Inky", Color.CYAN, level);
        this.random = new Random();
        this.isPatrolling = true;
        this.patrolTimer = 0;
    }

    @Override
    protected Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
        // Alternar entre patrulha e perseguicao a cada PATROL_DURATION frames
        patrolTimer++;
        if (patrolTimer >= PATROL_DURATION) {
            isPatrolling = !isPatrolling;
            patrolTimer = 0;
        }

        Direction nextDir;

        if (isPatrolling) {
            // Modo patrulha: escolher um alvo aleatorio no mapa
            int randomX = random.nextInt(19) * tileSize;
            int randomY = random.nextInt(21) * tileSize;

            nextDir = pathfindingManager.getNextDirectionDFS(
                this.x, this.y, randomX, randomY
            );
        } else {
            // Modo perseguicao: usar DFS para seguir o Pacman
            nextDir = pathfindingManager.getNextDirectionDFS(
                this.x, this.y, pacmanX, pacmanY
            );
        }

        // Se DFS nao retornou uma direcao valida, manter a atual
        if (nextDir == Direction.NONE) {
            return this.direction;
        }

        return nextDir;
    }

    @Override
    public void reset() {
        super.reset();
        this.isPatrolling = true;
        this.patrolTimer = 0;
    }
}
