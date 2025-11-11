package managers;

import graph.Graph;
import graph.Node;
import graph.algorithms.*;
import models.Position;
import models.Direction;
import java.util.List;

/**
 * Gerenciador de pathfinding que integra os algoritmos de busca com o jogo.
 * 
 * Esta classe atua como uma fachada (Facade Pattern) para os algoritmos de
 * pathfinding, simplificando o uso pelos fantasmas e outras entidades do jogo.
 * 
 * RESPONSABILIDADES:
 * - Converter coordenadas em pixels para nós do grafo
 * - Fornecer interface unificada para todos os algoritmos
 * - Gerenciar o grafo do labirinto
 * - Calcular caminhos e direções para os fantasmas
 * 
 * DESIGN PATTERN: Facade (Fachada)
 * - Simplifica a interface complexa dos algoritmos de pathfinding
 * - Oculta detalhes de implementação dos clientes (fantasmas)
 * 
 */
public class PathfindingManager {
    private final Graph graph;
    private final int tileSize;
    
    /**
     * Construtor do gerenciador.
     * 
     * param tileMap mapa do labirinto
     * param rows número de linhas
     * param columns número de colunas
     * param tileSize tamanho do tile em pixels
     */
    public PathfindingManager(String[] tileMap, int rows, int columns, int tileSize) {
        this.tileSize = tileSize;
        this.graph = new Graph(rows, columns);
        this.graph.buildFromTileMap(tileMap);
    }
    
    /**
     * return o grafo do labirinto
     */
    public Graph getGraph() {
        return graph;
    }
    
    /**
     * Converte coordenadas em pixels para um nó do grafo.
     * 
     * param pixelX coordenada x em pixels
     * param pixelY coordenada y em pixels
     * return nó correspondente, ou null se não existir
     */
    public Node getNodeFromPixels(int pixelX, int pixelY) {
        int gridX = pixelX / tileSize;
        int gridY = pixelY / tileSize;
        return graph.getNode(gridX, gridY);
    }
    
    /**
     * Converte uma posição em grid para nó do grafo.
     * 
     * param position posição em coordenadas de grid
     * return nó correspondente, ou null se não existir
     */
    public Node getNode(Position position) {
        return graph.getNode(position);
    }
    
    // ==================== DIJKSTRA ====================
    
    /**
     * Calcula caminho usando Dijkstra (caminho mais curto considerando pesos).
     * 
     * Usado por: Blinky (fantasma vermelho)
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return lista de nós no caminho
     */
    public List<Node> findPathDijkstra(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return List.of();
        }
        
        return Dijkstra.findPath(start, goal);
    }
    
    /**
     * Retorna a próxima direção usando Dijkstra.
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return direção a seguir, ou null
     */
    public Direction getNextDirectionDijkstra(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return null;
        }
        
        return Dijkstra.getNextDirection(start, goal);
    }
    
    // ==================== A* ====================
    
    /**
     * Calcula caminho usando A* (caminho mais curto com heurística).
     * 
     * Usado por: Pinky (fantasma rosa)
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return lista de nós no caminho
     */
    public List<Node> findPathAStar(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return List.of();
        }
        
        return AStar.findPath(start, goal);
    }
    
    /**
     * Retorna a próxima direção usando A*.
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return direção a seguir, ou null
     */
    public Direction getNextDirectionAStar(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return null;
        }
        
        return AStar.getNextDirection(start, goal);
    }
    
    // ==================== BFS ====================
    
    /**
     * Calcula caminho usando BFS (menor número de passos).
     * 
     * Usado por: Clyde (fantasma laranja)
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return lista de nós no caminho
     */
    public List<Node> findPathBFS(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return List.of();
        }
        
        return BFS.findPath(start, goal);
    }
    
    /**
     * Retorna a próxima direção usando BFS.
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return direção a seguir, ou null
     */
    public Direction getNextDirectionBFS(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return null;
        }
        
        return BFS.getNextDirection(start, goal);
    }
    
    /**
     * Calcula distância em passos usando BFS.
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return número de passos, ou -1 se não houver caminho
     */
    public int getDistanceBFS(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return -1;
        }
        
        return BFS.getDistance(start, goal);
    }
    
    // ==================== DFS ====================
    
    /**
     * Calcula caminho usando DFS (exploração em profundidade).
     * 
     * Usado por: Inky (fantasma azul/ciano)
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return lista de nós no caminho
     */
    public List<Node> findPathDFS(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return List.of();
        }
        
        return DFS.findPath(start, goal);
    }
    
    /**
     * Retorna a próxima direção usando DFS.
     * 
     * param startX x inicial em pixels
     * param startY y inicial em pixels
     * param goalX x objetivo em pixels
     * param goalY y objetivo em pixels
     * return direção a seguir, ou null
     */
    public Direction getNextDirectionDFS(int startX, int startY, int goalX, int goalY) {
        Node start = getNodeFromPixels(startX, startY);
        Node goal = getNodeFromPixels(goalX, goalY);
        
        if (start == null || goal == null) {
            return null;
        }
        
        return DFS.getNextDirection(start, goal);
    }
    
    /**
     * Retorna informações sobre o grafo.
     * 
     * return estatísticas do grafo
     */
    public String getGraphStats() {
        return graph.getStats();
    }
}
