package managers;

import graph.Graph;
import graph.Node;
import graph.algorithms.*;
import models.Direction;

import java.util.List;

/**
 * Gerenciador de pathfinding que fornece uma interface simplificada
 * para os algoritmos de busca em grafos.
 * 
 * Pattern: Facade - simplifica o uso dos algoritmos de pathfinding.
 */
public class PathfindingManager {
    private final Graph graph;

    public PathfindingManager(Graph graph) {
        this.graph = graph;
    }

    /**
     * Retorna a proxima direcao para ir de (startX, startY) ate (goalX, goalY)
     * usando o algoritmo de Dijkstra.
     */
    public Direction getNextDirectionDijkstra(int startX, int startY, int goalX, int goalY) {
        return getNextDirection(startX, startY, goalX, goalY, PathAlgorithm.DIJKSTRA);
    }

    /**
     * Retorna a proxima direcao usando A*.
     */
    public Direction getNextDirectionAStar(int startX, int startY, int goalX, int goalY) {
        return getNextDirection(startX, startY, goalX, goalY, PathAlgorithm.ASTAR);
    }

    /**
     * Retorna a proxima direcao usando BFS.
     */
    public Direction getNextDirectionBFS(int startX, int startY, int goalX, int goalY) {
        return getNextDirection(startX, startY, goalX, goalY, PathAlgorithm.BFS);
    }

    /**
     * Retorna a proxima direcao usando DFS.
     */
    public Direction getNextDirectionDFS(int startX, int startY, int goalX, int goalY) {
        return getNextDirection(startX, startY, goalX, goalY, PathAlgorithm.DFS);
    }

    /**
     * Calcula a distancia (em passos) entre duas posicoes usando BFS.
     */
    public int getDistanceBFS(int startX, int startY, int goalX, int goalY) {
        Node start = graph.getNode(startX, startY);
        Node goal = graph.getNode(goalX, goalY);
        
        if (start == null || goal == null) {
            return -1;
        }

        return BFS.getDistance(start, goal);
    }

    /**
     * Metodo generico para obter a proxima direcao usando qualquer algoritmo.
     */
    private Direction getNextDirection(int startX, int startY, int goalX, int goalY, PathAlgorithm algorithm) {
        Node start = graph.getNode(startX, startY);
        Node goal = graph.getNode(goalX, goalY);

        if (start == null || goal == null) {
            return Direction.NONE;
        }

        if (start.equals(goal)) {
            return Direction.NONE;
        }

        // Encontrar caminho usando o algoritmo especificado
        List<Node> path;
        switch (algorithm) {
            case DIJKSTRA:
                path = Dijkstra.findPath(start, goal);
                break;
            case ASTAR:
                path = AStar.findPath(start, goal);
                break;
            case BFS:
                path = BFS.findPath(start, goal);
                break;
            case DFS:
                path = DFS.findPath(start, goal);
                break;
            default:
                return Direction.NONE;
        }

        // Se nao encontrou caminho ou o caminho tem menos de 2 nos, retornar NONE
        if (path.isEmpty() || path.size() < 2) {
            return Direction.NONE;
        }

        // O proximo no eh o segundo elemento do caminho (indice 1)
        Node nextNode = path.get(1);

        // Determinar a direcao para o proximo no
        return start.getDirectionTo(nextNode);
    }

    /**
     * Retorna o grafo gerenciado por este PathfindingManager.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Enum para especificar qual algoritmo de pathfinding usar.
     */
    private enum PathAlgorithm {
        DIJKSTRA, ASTAR, BFS, DFS
    }
}
