
package graph;

import models.Position;
import models.Direction;

import java.util.*;

/**
 * Representa um vertice (no) no grafo.
 * Cada no corresponde a uma celula navegavel do labirinto.
 */
public class Node {
    private final Position position;
    private final Map<Direction, Node> neighbors;

    public Node(Position position) {
        this.position = position;
        this.neighbors = new HashMap<>();
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Adiciona um vizinho em uma direcao especifica.
     */
    public void addNeighbor(Direction direction, Node neighbor) {
        neighbors.put(direction, neighbor);
    }

    /**
     * Retorna o vizinho em uma direcao especifica, ou null se nao existir.
     */
    public Node getNeighbor(Direction direction) {
        return neighbors.get(direction);
    }

    /**
     * Retorna todos os vizinhos conectados a este no.
     */
    public Collection<Node> getAllNeighbors() {
        return neighbors.values();
    }

    /**
     * Retorna o mapa completo de vizinhos (direcao -> no).
     */
    public Map<Direction, Node> getNeighborsMap() {
        return new HashMap<>(neighbors);
    }

    /**
     * Retorna a direcao para chegar a um vizinho especifico.
     */
    public Direction getDirectionTo(Node neighbor) {
        for (Map.Entry<Direction, Node> entry : neighbors.entrySet()) {
            if (entry.getValue().equals(neighbor)) {
                return entry.getKey();
            }
        }
        return Direction.NONE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(position, node.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "Node" + position.toString();
    }
}
