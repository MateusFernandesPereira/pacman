
package graph.algorithms;

import graph.Node;
import models.Direction;
import java.util.*;

/**
 * Implementação do Algoritmo de Dijkstra para encontrar o caminho mais curto.
 * 
 * DESCRIÇÃO DO ALGORITMO:
 * O algoritmo de Dijkstra é um algoritmo guloso que encontra o caminho de custo mínimo
 * entre um vértice de origem e todos os outros vértices em um grafo com pesos não-negativos.
 * 
 * FUNCIONAMENTO:
 * 1. Inicializa a distância do nó origem como 0 e todos os outros como infinito
 * 2. Usa uma fila de prioridade para sempre processar o nó com menor distância
 * 3. Para cada nó processado, relaxa suas arestas (atualiza distâncias dos vizinhos)
 * 4. Marca o nó como visitado após processar
 * 5. Reconstrói o caminho usando os predecessores armazenados
 * 
 * COMPLEXIDADE DE TEMPO:
 * - Com fila de prioridade (PriorityQueue): O((V + E) log V)
 *   onde V = número de vértices, E = número de arestas
 * - Sem fila de prioridade (array): O(V²)
 * 
 * Para o Pacman (21x19 = ~250 células navegáveis):
 * - V ≈ 250, E ≈ 400
 * - Tempo: O(650 × log(250)) ≈ O(650 × 8) = ~5200 operações
 * 
 * COMPLEXIDADE DE ESPAÇO: O(V) para armazenar distâncias, predecessores e visitados
 * 
 * USO NO JOGO:
 * - Blinky (fantasma vermelho): persegue o Pacman usando o caminho mais curto
 * 
 */
public class Dijkstra {
    
    /**
     * Encontra o caminho mais curto entre dois nós usando o algoritmo de Dijkstra.
     * 
     * param start nó de origem
     * param goal nó de destino
     * return lista de nós representando o caminho (vazia se não existir caminho)
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
        
        // Mapa de distâncias: distância mínima conhecida do start até cada nó
        Map<Node, Double> distances = new HashMap<>();
        
        // Mapa de predecessores: usado para reconstruir o caminho
        Map<Node, Node> predecessors = new HashMap<>();
        
        // Conjunto de nós visitados
        Set<Node> visited = new HashSet<>();
        
        // Fila de prioridade: ordena nós por distância (menor distância = maior prioridade)
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(
            Comparator.comparingDouble(nd -> nd.distance)
        );
        
        // Inicialização
        distances.put(start, 0.0);
        pq.offer(new NodeDistance(start, 0.0));
        
        // Loop principal do algoritmo
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Node currentNode = current.node;
            
            // Se já visitamos este nó, pula (otimização)
            if (visited.contains(currentNode)) {
                continue;
            }
            
            visited.add(currentNode);
            
            // Se chegamos ao destino, reconstrói e retorna o caminho
            if (currentNode.equals(goal)) {
                return reconstructPath(predecessors, start, goal);
            }
            
            // Relaxamento: verifica todos os vizinhos
            for (Map.Entry<Node, Integer> entry : currentNode.getNeighbors().entrySet()) {
                Node neighbor = entry.getKey();
                int weight = entry.getValue();
                
                if (visited.contains(neighbor)) {
                    continue;
                }
                
                // Calcula nova distância através do nó atual
                double newDistance = distances.get(currentNode) + weight;
                double currentDistance = distances.getOrDefault(neighbor, Double.POSITIVE_INFINITY);
                
                // Se encontramos um caminho melhor, atualiza
                if (newDistance < currentDistance) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, currentNode);
                    pq.offer(new NodeDistance(neighbor, newDistance));
                }
            }
        }
        
        // Não existe caminho
        return new ArrayList<>();
    }
    
    /**
     * Calcula a próxima direção para mover-se em direção ao objetivo usando Dijkstra.
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
        
        // Próximo nó no caminho
        Node nextNode = path.get(1);
        
        // Determina a direção do movimento
        int dx = nextNode.getPosition().getX() - start.getPosition().getX();
        int dy = nextNode.getPosition().getY() - start.getPosition().getY();
        
        if (dx > 0) return Direction.RIGHT;
        if (dx < 0) return Direction.LEFT;
        if (dy > 0) return Direction.DOWN;
        if (dy < 0) return Direction.UP;
        
        return null;
    }
    
    /**
     * Reconstrói o caminho a partir do mapa de predecessores.
     * 
     * Complexidade: O(L) onde L é o comprimento do caminho
     * 
     * param predecessors mapa de predecessores
     * param start nó de origem
     * param goal nó de destino
     * return lista de nós no caminho
     */
    private static List<Node> reconstructPath(Map<Node, Node> predecessors, Node start, Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;
        
        while (current != null) {
            path.add(0, current); // Adiciona no início
            current = predecessors.get(current);
            
            // Evita loops infinitos
            if (path.size() > 1000) {
                break;
            }
        }
        
        // Verifica se o caminho é válido (começa no start)
        if (!path.isEmpty() && path.get(0).equals(start)) {
            return path;
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Classe auxiliar para armazenar nó e sua distância na fila de prioridade.
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
