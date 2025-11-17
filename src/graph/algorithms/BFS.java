
package graph.algorithms;

import graph.Node;

import java.util.*;

/**
 * Implementacao do algoritmo de Busca em Largura (Breadth-First Search).
 * 
 * Complexidade:
 * - Tempo: O(V + E)
 * - Espaco: O(V) para fila e conjunto de visitados
 * 
 * Garante encontrar o caminho com menor numero de arestas (nao considera pesos).
 * Explora em "ondas" concentricas a partir do no inicial.
 */
public class BFS {
    
    /**
     * Encontra um caminho usando busca em largura.
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

        // Fila FIFO para BFS
        Queue<Node> queue = new LinkedList<>();
        queue.offer(start);

        // Conjunto de nos visitados
        Set<Node> visited = new HashSet<>();
        visited.add(start);

        // Mapa de predecessores para reconstruir o caminho
        Map<Node, Node> predecessors = new HashMap<>();

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Se chegamos ao objetivo, reconstruir caminho
            if (current.equals(goal)) {
                return reconstructPath(predecessors, start, goal);
            }

            // Explorar todos os vizinhos
            for (Node neighbor : current.getAllNeighbors()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    predecessors.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }

        // Nenhum caminho encontrado
        return new ArrayList<>();
    }

    /**
     * Calcula a distancia (numero de passos) entre dois nos usando BFS.
     * 
     * @param start No inicial
     * @param goal No objetivo
     * @return Numero de passos, ou -1 se nao houver caminho
     */
    public static int getDistance(Node start, Node goal) {
        if (start == null || goal == null) {
            return -1;
        }

        if (start.equals(goal)) {
            return 0;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(start);

        Set<Node> visited = new HashSet<>();
        visited.add(start);

        Map<Node, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int currentDist = distances.get(current);

            if (current.equals(goal)) {
                return currentDist;
            }

            for (Node neighbor : current.getAllNeighbors()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distances.put(neighbor, currentDist + 1);
                    queue.offer(neighbor);
                }
            }
        }

        return -1; // Nenhum caminho encontrado
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
}
