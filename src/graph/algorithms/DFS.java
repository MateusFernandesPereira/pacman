
package graph.algorithms;

import graph.Node;

import java.util.*;

/**
 * Implementacao do algoritmo de Busca em Profundidade (Depth-First Search).
 * 
 * Complexidade:
 * - Tempo: O(V + E)
 * - Espaco: O(V) para pilha e conjunto de visitados
 * 
 * NAO garante o caminho mais curto.
 * Explora o maximo possivel ao longo de cada ramo antes de retroceder.
 * Util para exploracao e patrulhamento.
 */
public class DFS {
    
    /**
     * Encontra um caminho usando busca em profundidade.
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

        // Pilha LIFO para DFS
        Stack<Node> stack = new Stack<>();
        stack.push(start);

        // Conjunto de nos visitados
        Set<Node> visited = new HashSet<>();
        visited.add(start);

        // Mapa de predecessores para reconstruir o caminho
        Map<Node, Node> predecessors = new HashMap<>();

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            // Se chegamos ao objetivo, reconstruir caminho
            if (current.equals(goal)) {
                return reconstructPath(predecessors, start, goal);
            }

            // Explorar todos os vizinhos (em ordem aleatoria para mais imprevisibilidade)
            List<Node> neighbors = new ArrayList<>(current.getAllNeighbors());
            Collections.shuffle(neighbors); // Aleatoriza a exploracao

            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    predecessors.put(neighbor, current);
                    stack.push(neighbor);
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
            path.add(0, current);
            current = predecessors.get(current);
        }

        return path;
    }
}
