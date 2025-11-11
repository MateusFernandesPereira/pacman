
package graph.algorithms;

import graph.Node;
import models.Direction;
import java.util.*;

/**
 * Implementação do algoritmo de Busca em Largura (BFS - Breadth-First Search).
 * 
 * DESCRIÇÃO DO ALGORITMO:
 * BFS é um algoritmo de busca em grafos que explora todos os vértices a uma determinada
 * distância antes de mover-se para vértices mais distantes. Usa uma fila (FIFO) para
 * processar os nós.
 * 
 * FUNCIONAMENTO:
 * 1. Começa no nó origem e o marca como visitado
 * 2. Adiciona o nó origem em uma fila
 * 3. Enquanto a fila não estiver vazia:
 *    a. Remove o primeiro nó da fila
 *    b. Para cada vizinho não-visitado:
 *       - Marca como visitado
 *       - Registra o predecessor
 *       - Adiciona na fila
 * 4. Reconstrói o caminho usando os predecessores
 * 
 * CARACTERISTICAS:
 * - Sempre encontra o caminho com MENOR NÚMERO DE ARESTAS (não considera pesos)
 * - Explora em "ondas": primeiro nível 1, depois nível 2, etc.
 * - Completo: sempre encontra solução se ela existir
 * - Ótimo: para grafos não-ponderados ou com pesos uniformes
 * 
 * COMPLEXIDADE DE TEMPO: O(V + E)
 * - V = número de vértices visitados
 * - E = número de arestas exploradas
 * - Para o Pacman: O(250 + 400) = O(650)
 * 
 * COMPLEXIDADE DE ESPAÇO: O(V)
 * - Armazena visitados, predecessores e a fila
 * 
 * COMPARAÇÃO COM OUTROS ALGORITMOS:
 * - Dijkstra: BFS é mais rápido mas não considera pesos
 * - DFS: BFS garante caminho mais curto (em número de passos)
 * - A*: BFS não usa heurística, explora uniformemente
 * 
 * USO NO JOGO:
 * - Clyde (fantasma laranja): comportamento misto - persegue quando longe,
 *   foge quando próximo, usando BFS para calcular distâncias
 * 
 */
public class BFS {
    
    /**
     * Encontra o caminho mais curto (em número de passos) usando BFS.
     * 
     * param start nó de origem
     * param goal nó de destino
     * return lista de nós no caminho (vazia se não houver caminho)
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
        
        // Conjunto de nós visitados
        Set<Node> visited = new HashSet<>();
        
        // Mapa de predecessores para reconstruir o caminho
        Map<Node, Node> predecessors = new HashMap<>();
        
        // Inicialização
        queue.offer(start);
        visited.add(start);
        
        // Loop principal do BFS
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            // Se encontramos o objetivo, reconstrói o caminho
            if (current.equals(goal)) {
                return reconstructPath(predecessors, start, goal);
            }
            
            // Explora todos os vizinhos
            for (Node neighbor : current.getNeighbors().keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    predecessors.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }
        
        // Não existe caminho
        return new ArrayList<>();
    }
    
    /**
     * Calcula a distância (em número de passos) entre dois nós usando BFS.
     * 
     * Útil para decisões baseadas em distância, como o comportamento do Clyde.
     * 
     * Complexidade: O(V + E)
     * 
     * param start nó de origem
     * param goal nó de destino
     * return distância em passos, ou -1 se não houver caminho
     */
    public static int getDistance(Node start, Node goal) {
        if (start == null || goal == null) {
            return -1;
        }
        
        if (start.equals(goal)) {
            return 0;
        }
        
        Queue<Node> queue = new LinkedList<>();
        Map<Node, Integer> distances = new HashMap<>();
        
        queue.offer(start);
        distances.put(start, 0);
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int currentDistance = distances.get(current);
            
            if (current.equals(goal)) {
                return currentDistance;
            }
            
            for (Node neighbor : current.getNeighbors().keySet()) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, currentDistance + 1);
                    queue.offer(neighbor);
                }
            }
        }
        
        return -1; // Não existe caminho
    }
    
    /**
     * Calcula a próxima direção usando BFS.
     * 
     * param start nó de origem
     * param goal nó de destino
     * return direção a seguir, ou null se não houver caminho
     */
    public static Direction getNextDirection(Node start, Node goal) {
        List<Node> path = findPath(start, goal);
        
        if (path.size() < 2) {
            return null;
        }
        
        Node nextNode = path.get(1);
        
        int dx = nextNode.getPosition().getX() - start.getPosition().getX();
        int dy = nextNode.getPosition().getY() - start.getPosition().getY();
        
        if (dx > 0) return Direction.RIGHT;
        if (dx < 0) return Direction.LEFT;
        if (dy > 0) return Direction.DOWN;
        if (dy < 0) return Direction.UP;
        
        return null;
    }
    
    /**
     * Encontra todos os nós alcançáveis a partir de um nó origem.
     * 
     * Útil para análise de conectividade do grafo.
     * 
     * Complexidade: O(V + E)
     * 
     * param start nó de origem
     * return conjunto de nós alcançáveis
     */
    public static Set<Node> getReachableNodes(Node start) {
        if (start == null) {
            return new HashSet<>();
        }
        
        Set<Node> reachable = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        
        queue.offer(start);
        reachable.add(start);
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            for (Node neighbor : current.getNeighbors().keySet()) {
                if (!reachable.contains(neighbor)) {
                    reachable.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        return reachable;
    }
    
    /**
     * Reconstrói o caminho usando o mapa de predecessores.
     * 
     * Complexidade: O(L) onde L é o comprimento do caminho
     * 
     * param predecessors mapa de predecessores
     * param start nó inicial
     * param goal nó objetivo
     * return lista de nós no caminho
     */
    private static List<Node> reconstructPath(Map<Node, Node> predecessors, Node start, Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;
        
        while (current != null) {
            path.add(0, current);
            current = predecessors.get(current);
            
            if (path.size() > 1000) {
                break; // Proteção contra loops
            }
        }
        
        if (!path.isEmpty() && path.get(0).equals(start)) {
            return path;
        }
        
        return new ArrayList<>();
    }
}
