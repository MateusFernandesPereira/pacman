
package graph.algorithms;

import graph.Node;
import models.Position;

import java.util.*;

/**
 * Implementacao do algoritmo A* (A-Star) com heuristica de Manhattan.
 * 
 * Complexidade:
 * - Tempo: O(E log V) em casos medios
 * - Espaco: O(V)
 * 
 * Usa funcao de avaliacao: f(n) = g(n) + h(n)
 * - g(n) = custo real do inicio ate n
 * - h(n) = distancia de Manhattan ate o objetivo (heuristica admissivel)
 */
public class AStar {
    
    /**
     * Encontra o caminho usando A* com heuristica de Manhattan.
     * 
     * @param start No inicial
     * @param goal No objetivo
     * @return Lista de nos representando o caminho, ou lista vazia se nao houver caminho
     */
    public static List<Node> findPath(Node start, Node goal) {
        if (start == null || goal == null) {
            return new ArrayList<>();
        }

        if (start.equals(goal)) {
            List<Node> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        // gScore: custo real do inicio ate cada no
        Map<Node, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        // fScore: gScore + heuristica
        Map<Node, Double> fScore = new HashMap<>();
        fScore.put(start, heuristic(start, goal));

        // Predecessores para reconstruir o caminho
        Map<Node, Node> predecessors = new HashMap<>();

        // Open set: nos a serem avaliados
        PriorityQueue<NodeScore> openSet = new PriorityQueue<>(
            Comparator.comparingDouble(ns -> ns.fScore)
        );
        openSet.offer(new NodeScore(start, fScore.get(start)));

        // Conjunto de nos ja avaliados
        Set<Node> closedSet = new HashSet<>();

        while (!openSet.isEmpty()) {
            NodeScore current = openSet.poll();
            Node currentNode = current.node;

            // Se chegamos ao objetivo, reconstruir caminho
            if (currentNode.equals(goal)) {
                return reconstructPath(predecessors, start, goal);
            }

            closedSet.add(currentNode);

            // Explorar vizinhos
            for (Node neighbor : currentNode.getAllNeighbors()) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                // Calcular novo gScore
                double tentativeGScore = gScore.get(currentNode) + 1.0;
                double currentGScore = gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY);

                // Se encontramos um caminho melhor
                if (tentativeGScore < currentGScore) {
                    predecessors.put(neighbor, currentNode);
                    gScore.put(neighbor, tentativeGScore);
                    double newFScore = tentativeGScore + heuristic(neighbor, goal);
                    fScore.put(neighbor, newFScore);

                    // Adicionar ao open set se ainda nao estiver
                    openSet.offer(new NodeScore(neighbor, newFScore));
                }
            }
        }

        // Nenhum caminho encontrado
        return new ArrayList<>();
    }

    /**
     * Calcula a heuristica (distancia de Manhattan) entre dois nos.
     * Esta heuristica eh admissivel e consistente para grids 4-direcionais.
     */
    private static double heuristic(Node from, Node to) {
        Position fromPos = from.getPosition();
        Position toPos = to.getPosition();
        return fromPos.manhattanDistance(toPos);
    }

    /**
     * Reconstroi o caminho a partir do mapa de predecessores.
     */
    private static List<Node> reconstructPath(Map<Node, Node> predecessors, Node start, Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;

        while (current != null) {
            path.add(0, current);
            current = predecessors.get(current);
        }

        return path;
    }

    /**
     * Classe auxiliar para armazenar no e seu fScore na fila de prioridade.
     */
    private static class NodeScore {
        Node node;
        double fScore;

        NodeScore(Node node, double fScore) {
            this.node = node;
            this.fScore = fScore;
        }
    }
}
