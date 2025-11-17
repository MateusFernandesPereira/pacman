
package graph.algorithms;

import graph.Node;
import models.Direction;
import models.Position;
import java.util.*;

/**
 * Implementação do Algoritmo A* (A-Star) para busca de caminho com heurística.
 * 
 * DESCRIÇÃO DO ALGORITMO:
 * A* é uma extensão do algoritmo de Dijkstra que usa uma função heurística para
 * guiar a busca de forma mais eficiente. É um dos algoritmos mais populares para
 * pathfinding em jogos e robótica.
 * 
 * FUNCIONAMENTO:
 * 1. Usa uma função de avaliação f(n) = g(n) + h(n), onde:
 *    - g(n) = custo real do caminho do início até n
 *    - h(n) = estimativa heurística do custo de n até o objetivo
 * 2. A heurística usada é a Distância de Manhattan: |x1-x2| + |y1-y2|
 *    - Admissível: nunca superestima o custo real
 *    - Consistente: satisfaz a desigualdade triangular
 * 3. Sempre expande o nó com menor valor f(n)
 * 4. Garante encontrar o caminho ótimo se a heurística for admissível
 * 
 * COMPLEXIDADE DE TEMPO:
 * - Pior caso: O(E) = O(V²) quando h(n) = 0 (equivale a Dijkstra)
 * - Caso médio: O(E log V) com boa heurística
 * - Melhor caso: O(V) quando a heurística é perfeita
 * 
 * Para o Pacman (grade 21x19):
 * - Com Manhattan distance: muito mais rápido que Dijkstra
 * - Expande apenas ~30-50% dos nós em casos típicos
 * 
 * COMPLEXIDADE DE ESPAÇO: O(V) para armazenar custos e predecessores
 * 
 * VANTAGENS SOBRE DIJKSTRA:
 * - Mais rápido: explora menos nós usando a heurística
 * - Ideal para encontrar caminho entre dois pontos específicos
 * - Ainda garante caminho ótimo com heurística admissível
 * 
 * USO NO JOGO:
 * - Pinky (fantasma rosa): tenta emboscar o Pacman prevendo sua posição futura
 * 
 *
 */
public class AStar {
    
    /**
     * Encontra o caminho mais curto usando A* com heurística de Manhattan.
     * 
     * param start nó de origem
     * param goal nó de destino
     * return lista de nós representando o caminho (vazia se não existir)
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
        
        // g(n): custo real do início até o nó n
        Map<Node, Double> gScore = new HashMap<>();
        
        // f(n) = g(n) + h(n): custo estimado total
        Map<Node, Double> fScore = new HashMap<>();
        
        // Mapa de predecessores para reconstruir o caminho
        Map<Node, Node> predecessors = new HashMap<>();
        
        // Conjunto de nós já avaliados
        Set<Node> closedSet = new HashSet<>();
        
        // Fila de prioridade ordenada por f(n)
        PriorityQueue<NodeScore> openSet = new PriorityQueue<>(
            Comparator.comparingDouble(ns -> ns.fScore)
        );
        
        // Inicialização
        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));
        openSet.offer(new NodeScore(start, fScore.get(start)));
        
        // Loop principal do A*
        while (!openSet.isEmpty()) {
            NodeScore current = openSet.poll();
            Node currentNode = current.node;
            
            // Se já processamos este nó, pula
            if (closedSet.contains(currentNode)) {
                continue;
            }
            
            // Se chegamos ao objetivo, reconstrói o caminho
            if (currentNode.equals(goal)) {
                return reconstructPath(predecessors, start, goal);
            }
            
            closedSet.add(currentNode);
            
            // Examina todos os vizinhos
            for (Map.Entry<Node, Integer> entry : currentNode.getNeighbors().entrySet()) {
                Node neighbor = entry.getKey();
                int weight = entry.getValue();
                
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                
                // Calcula g(neighbor) através do caminho atual
                double tentativeGScore = gScore.get(currentNode) + weight;
                double currentGScore = gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY);
                
                // Se encontramos um caminho melhor para o vizinho
                if (tentativeGScore < currentGScore) {
                    // Atualiza o predecessor
                    predecessors.put(neighbor, currentNode);
                    
                    // Atualiza g(neighbor)
                    gScore.put(neighbor, tentativeGScore);
                    
                    // Calcula f(neighbor) = g(neighbor) + h(neighbor)
                    double fScoreValue = tentativeGScore + heuristic(neighbor, goal);
                    fScore.put(neighbor, fScoreValue);
                    
                    // Adiciona à fila de prioridade
                    openSet.offer(new NodeScore(neighbor, fScoreValue));
                }
            }
        }
        
        // Não existe caminho
        return new ArrayList<>();
    }
    
    /**
     * Função heurística: Distância de Manhattan.
     * 
     * A distância de Manhattan é admissível para grades onde só podemos mover
     * nas 4 direções cardeais (cima, baixo, esquerda, direita).
     * 
     * Fórmula: h(n) = |x_n - x_goal| + |y_n - y_goal|
     * 
     * Complexidade: O(1)
     * 
     * param node nó atual
     * param goal nó objetivo
     * return estimativa de distância
     */
    private static double heuristic(Node node, Node goal) {
        Position p1 = node.getPosition();
        Position p2 = goal.getPosition();
        return p1.manhattanDistance(p2);
    }
    
    /**
     * Calcula a próxima direção usando A*.
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
     * Reconstrói o caminho a partir dos predecessores.
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
    
    /**
     * Classe auxiliar para armazenar nó e seu f-score.
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
