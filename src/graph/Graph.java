
package graph;

import models.Position;
import models.Direction;
import java.util.*;

/**
 * Representa o grafo que modela o labirinto do Pacman.
 * 
 * Modelagem:
 * - Vértices (V): células navegáveis do labirinto
 * - Arestas (E): conexões entre células adjacentes (sem paredes entre elas)
 * - Grafo não-direcionado com pesos uniformes (peso = 1)
 * 
 * Complexidade de Construção: O(V + E) onde V = número de células navegáveis
 * Complexidade de Espaço: O(V + E)
 * 
 * Para um labirinto típico 21x19:
 * - V ≈ 250 células navegáveis
 * - E ≈ 300-400 arestas (cada célula tem até 4 vizinhos)
 * 
 */
public class Graph {
    private final Map<Position, Node> nodes;
    private final int rows;
    private final int columns;
    
    /**
     * Construtor do grafo.
     * param rows número de linhas do labirinto
     * param columns número de colunas do labirinto
     */
    public Graph(int rows, int columns) {
        this.nodes = new HashMap<>();
        this.rows = rows;
        this.columns = columns;
    }
    
    /**
     * Constrói o grafo a partir do mapa do labirinto.
     * 
     * Algoritmo:
     * 1. Para cada célula navegável (não-parede), cria um nó
     * 2. Para cada par de células adjacentes sem parede entre elas, cria uma aresta
     * 
     * Complexidade: O(rows × columns) = O(V)
     * 
     * param tileMap mapa do labirinto (matriz de caracteres)
     */
    public void buildFromTileMap(String[] tileMap) {
        // Primeira passagem: cria os nós para todas as células navegáveis
        for (int row = 0; row < tileMap.length; row++) {
            for (int col = 0; col < tileMap[row].length(); col++) {
                char tile = tileMap[row].charAt(col);
                
                // Células navegáveis: espaços, Pacman, fantasmas
                if (tile != 'X' && tile != 'O') {
                    Position pos = new Position(col, row);
                    Node node = new Node(pos);
                    nodes.put(pos, node);
                }
            }
        }
        
        // Segunda passagem: conecta os nós adjacentes
        for (Node node : nodes.values()) {
            Position pos = node.getPosition();
            
            // Verifica cada uma das 4 direções
            for (Direction dir : Direction.all()) {
                Position neighborPos = pos.move(dir);
                
                // Se existe um nó na posição vizinha, conecta
                Node neighbor = nodes.get(neighborPos);
                if (neighbor != null && !node.isAdjacentTo(neighbor)) {
                    node.addNeighbor(neighbor);
                }
            }
        }
    }
    
    /**
     * Retorna o nó na posição especificada.
     * 
     * Complexidade: O(1) - acesso via HashMap
     * 
     * param position posição desejada
     * return nó na posição, ou null se não existir
     */
    public Node getNode(Position position) {
        return nodes.get(position);
    }
    
    /**
     * Retorna o nó nas coordenadas especificadas.
     * 
     * Complexidade: O(1)
     * 
     * param x coordenada x
     * param y coordenada y
     * return nó na posição, ou null se não existir
     */
    public Node getNode(int x, int y) {
        return nodes.get(new Position(x, y));
    }
    
    /**
     * return todos os nós do grafo
     */
    public Collection<Node> getAllNodes() {
        return nodes.values();
    }
    
    /**
     * return número de vértices (nós) no grafo
     */
    public int getNodeCount() {
        return nodes.size();
    }
    
    /**
     * Calcula o número de arestas no grafo.
     * Como o grafo é não-direcionado, cada aresta é contada uma vez.
     * 
     * Complexidade: O(V) onde V é o número de vértices
     * 
     * return número de arestas
     */
    public int getEdgeCount() {
        int sum = 0;
        for (Node node : nodes.values()) {
            sum += node.getDegree();
        }
        return sum / 2; // Divide por 2 porque cada aresta é contada duas vezes
    }
    
    /**
     * Verifica se existe um caminho direto (aresta) entre duas posições.
     * 
     * Complexidade: O(1)
     * 
     * param from posição de origem
     * param to posição de destino
     * return true se existe aresta direta
     */
    public boolean hasEdge(Position from, Position to) {
        Node fromNode = getNode(from);
        Node toNode = getNode(to);
        return fromNode != null && toNode != null && fromNode.isAdjacentTo(toNode);
    }
    
    /**
     * Retorna o nó mais próximo de uma posição em pixels.
     * Útil para converter coordenadas do jogo em nós do grafo.
     * 
     * Complexidade: O(V) - busca linear por todos os nós
     * 
     * param pixelX coordenada x em pixels
     * param pixelY coordenada y em pixels
     * param tileSize tamanho do tile em pixels
     * return nó mais próximo, ou null se o grafo estiver vazio
     */
    public Node getClosestNode(int pixelX, int pixelY, int tileSize) {
        int gridX = pixelX / tileSize;
        int gridY = pixelY / tileSize;
        return getNode(gridX, gridY);
    }
    
    /**
     * return número de linhas do labirinto
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * return número de colunas do labirinto
     */
    public int getColumns() {
        return columns;
    }
    
    /**
     * Retorna estatísticas do grafo para análise.
     * 
     * return string com informações sobre o grafo
     */
    public String getStats() {
        return String.format(
            "Grafo: %d vértices, %d arestas, densidade = %.2f%%",
            getNodeCount(),
            getEdgeCount(),
            (200.0 * getEdgeCount()) / (getNodeCount() * (getNodeCount() - 1))
        );
    }
    
    @Override
    public String toString() {
        return String.format("Graph[%dx%d, nodes=%d, edges=%d]", 
            rows, columns, getNodeCount(), getEdgeCount());
    }
}
