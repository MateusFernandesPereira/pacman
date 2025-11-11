
package graph;

import models.Position;
import java.util.*;

/**
 * Representa um nó (vértice) no grafo que modela o labirinto do Pacman.
 * Cada nó corresponde a uma célula navegável do labirinto.
 * 
 * Estrutura de Dados:
 * - Position: armazena coordenadas (x, y) da célula
 * - Map<Node, Integer>: armazena vizinhos e pesos das arestas
 * 
 * Complexidade de Espaço: O(k) onde k é o número de vizinhos (máximo 4)
 * 
 */
public class Node {
    private final Position position;
    private final Map<Node, Integer> neighbors;
    
    /**
     * Construtor do nó.
     * param position posição do nó no tabuleiro
     */
    public Node(Position position) {
        this.position = position;
        this.neighbors = new HashMap<>();
    }
    
    /**
     * return posição do nó
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Adiciona uma aresta bidirecional entre este nó e outro.
     * Por padrão, todas as arestas têm peso 1 (custo uniforme).
     * 
     * Complexidade: O(1)
     * 
     * param neighbor nó vizinho
     */
    public void addNeighbor(Node neighbor) {
        addNeighbor(neighbor, 1);
    }
    
    /**
     * Adiciona uma aresta bidirecional com peso específico.
     * 
     * Complexidade: O(1)
     * 
     * param neighbor nó vizinho
     * param weight peso da aresta
     */
    public void addNeighbor(Node neighbor, int weight) {
        this.neighbors.put(neighbor, weight);
        neighbor.neighbors.put(this, weight);
    }
    
    /**
     * return mapa de vizinhos e seus pesos
     */
    public Map<Node, Integer> getNeighbors() {
        return neighbors;
    }
    
    /**
     * Retorna o peso da aresta entre este nó e o vizinho.
     * 
     * Complexidade: O(1)
     * 
     * param neighbor nó vizinho
     * return peso da aresta, ou Integer.MAX_VALUE se não houver conexão
     */
    public int getWeight(Node neighbor) {
        return neighbors.getOrDefault(neighbor, Integer.MAX_VALUE);
    }
    
    /**
     * Verifica se este nó é adjacente a outro.
     * 
     * Complexidade: O(1)
     * 
     * param other outro nó
     * return true se são adjacentes
     */
    public boolean isAdjacentTo(Node other) {
        return neighbors.containsKey(other);
    }
    
    /**
     * return número de vizinhos deste nó (grau do vértice)
     */
    public int getDegree() {
        return neighbors.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return position.equals(node.position);
    }
    
    @Override
    public int hashCode() {
        return position.hashCode();
    }
    
    @Override
    public String toString() {
        return "Node" + position.toString();
    }
}
