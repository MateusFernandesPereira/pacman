
package graph.algorithms;

import graph.Node;
import models.Direction;

import java.util.*;

/**
 * Implementacao do algoritmo de Dijkstra para encontrar o caminho mais curto.
 * 
 * Complexidade:
 * - Tempo: O((V + E) log V) com PriorityQueue
 * - Espaco: O(V) para armazenar distancias e predecessores
 * 
 * Garante encontrar o caminho otimo em grafos com pesos nao-negativos.
 */
public class Dijkstra {
    
    /**
     * Encontra o caminho mais curto entre dois nos usando Dijkstra.
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

        // Mapa de distancias (infinito por padrao)
        Map<Node, Double> distances = new HashMap<>();
        distances.put(start, 0.0);

        // Mapa de predecessores para reconstruir o caminho
        Map<Node, Node> predecessors = new HashMap<>();

        // Fila de prioridade ordenada por distancia
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(
            Comparator.comparingDouble(nd -> nd.distance)
        );
        pq.offer(new NodeDistance(start, 0.0));

        // Conjunto de nos visitados
        Set<Node> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Node currentNode = current.node;

            // Se ja visitamos este no, pular
            if (visited.contains(currentNode)) {
                continue;
            }

            visited.add(currentNode);

            // Se chegamos ao objetivo, reconstruir e retornar o caminho
            if (currentNode.equals(goal)) {
                return reconstructPath(predecessors, start, goal);
            }

            // Explorar todos os vizinhos
            for (Node neighbor : currentNode.getAllNeighbors()) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                // Peso uniforme = 1 para todas as arestas
                double newDistance = distances.get(currentNode) + 1.0;
                double currentDistance = distances.getOrDefault(neighbor, Double.POSITIVE_INFINITY);

                // Se encontramos um caminho melhor, atualizar
                if (newDistance < currentDistance) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, currentNode);
                    pq.offer(new NodeDistance(neighbor, newDistance));
                }
            }
        }

        // Nenhum caminho encontrado
        return new ArrayList<>();
    }

    /**
     * Reconstroi o caminho a partir do mapa de predecessores.
     */
    private static List<Node> reconstructPath(Map<Node, Node> predecessors, Node start, Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;

        while (current != null) {
            path.add(0, current); // Adicionar no inicio
            current = predecessors.get(current);
        }

        return path;
    }

    /**
     * Classe auxiliar para armazenar no e sua distancia na fila de prioridade.
     */
    private static class NodeDistance {
        Node node;
        double distance;

        NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
