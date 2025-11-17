package graph;

import models.Position;
import models.Direction;

import java.util.*;

/**
 * Representa o grafo do labirinto.
 * Constroi automaticamente a partir do mapa de tiles.
 * 
 * Complexidade de construcao: O(rows * cols)
 * Espaco: O(V) onde V = numero de celulas navegaveis
 */
public class Graph {
    private final Map<Position, Node> nodes;
    private final int tileSize;

    public Graph(int tileSize) {
        this.nodes = new HashMap<>();
        this.tileSize = tileSize;
    }

    /**
     * Constroi o grafo a partir do mapa de tiles.
     * 
     * @param tileMap Array de strings representando o labirinto
     *                'X' = parede (nao cria vertice)
     *                Qualquer outro caractere = celula navegavel (cria vertice)
     */
    public void buildFromTileMap(String[] tileMap) {
        int rows = tileMap.length;
        int cols = tileMap[0].length();

        // Primeira passagem: criar todos os nos para celulas navegaveis
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char tile = tileMap[row].charAt(col);
                
                // Se nao eh parede, cria um no
                if (tile != 'X') {
                    int x = col * tileSize;
                    int y = row * tileSize;
                    Position pos = new Position(x, y);
                    nodes.put(pos, new Node(pos));
                }
            }
        }

        // Segunda passagem: conectar nos adjacentes (criar arestas)
        for (Node node : nodes.values()) {
            Position pos = node.getPosition();
            
            // Tentar conectar nas 4 direcoes
            for (Direction dir : new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT}) {
                int neighborX = pos.x + (dir.dx * tileSize);
                int neighborY = pos.y + (dir.dy * tileSize);
                Position neighborPos = new Position(neighborX, neighborY);
                
                Node neighbor = nodes.get(neighborPos);
                if (neighbor != null) {
                    node.addNeighbor(dir, neighbor);
                }
            }
        }
    }

    /**
     * Retorna o no na posicao especificada.
     * Procura o no mais proximo se a posicao exata nao existir.
     */
    public Node getNode(int x, int y) {
        // Normalizar para o grid
        int gridX = (x / tileSize) * tileSize;
        int gridY = (y / tileSize) * tileSize;
        
        Position pos = new Position(gridX, gridY);
        return nodes.get(pos);
    }

    /**
     * Retorna o no exatamente na posicao especificada.
     */
    public Node getNodeExact(Position position) {
        return nodes.get(position);
    }

    /**
     * Retorna todos os nos do grafo.
     */
    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    /**
     * Retorna o numero de vertices no grafo.
     */
    public int getVertexCount() {
        return nodes.size();
    }

    /**
     * Retorna o numero de arestas no grafo.
     */
    public int getEdgeCount() {
        int edges = 0;
        for (Node node : nodes.values()) {
            edges += node.getAllNeighbors().size();
        }
        // Cada aresta eh contada duas vezes (bidirecional)
        return edges / 2;
    }

    /**
     * Verifica se uma posicao eh navegavel (existe no grafo).
     */
    public boolean isNavigable(int x, int y) {
        return getNode(x, y) != null;
    }

    @Override
    public String toString() {
        return "Graph{vertices=" + getVertexCount() + ", edges=" + getEdgeCount() + "}";
    }
}
