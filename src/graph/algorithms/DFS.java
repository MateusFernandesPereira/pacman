
package graph.algorithms;

import graph.Node;
import models.Direction;
import java.util.*;

/**
 * Implementação do algoritmo de Busca em Profundidade (DFS - Depth-First Search).
 * 
 * DESCRIÇÃO DO ALGORITMO:
 * DFS é um algoritmo de busca em grafos que explora o máximo possível ao longo de
 * cada ramo antes de retroceder (backtracking). Pode ser implementado recursivamente
 * ou usando uma pilha (LIFO).
 * 
 * FUNCIONAMENTO:
 * 1. Começa no nó origem e o marca como visitado
 * 2. Adiciona o nó origem em uma pilha
 * 3. Enquanto a pilha não estiver vazia:
 *    a. Remove o topo da pilha
 *    b. Para cada vizinho não-visitado:
 *       - Marca como visitado
 *       - Registra o predecessor
 *       - Adiciona na pilha
 * 4. Reconstrói o caminho usando os predecessores
 * 
 * CARACTERISTICAS:
 * - Explora em PROFUNDIDADE: vai até o fim de um ramo antes de explorar outros
 * - NÃO garante caminho mais curto (nem em distância, nem em número de passos)
 * - Usa menos memória que BFS em grafos com alto fator de ramificação
 * - Completo para grafos finitos
 * - Útil para exploração e patrulhamento
 * 
 * COMPLEXIDADE DE TEMPO: O(V + E)
 * - V = número de vértices visitados
 * - E = número de arestas exploradas
 * - Mesma complexidade que BFS, mas comportamento diferente
 * 
 * COMPLEXIDADE DE ESPAÇO: O(V)
 * - Pior caso: O(V) se o grafo for uma linha
 * - Na prática: pode usar menos memória que BFS
 * 
 * COMPARAÇÃO COM BFS:
 * - BFS: explora em "ondas" (largura)
 * - DFS: explora em "linhas" (profundidade)
 * - BFS garante caminho mais curto, DFS não
 * - DFS é melhor para exploração e patrulhamento
 * 
 * USO NO JOGO:
 * - Inky (fantasma azul/ciano): patrulha áreas do labirinto usando DFS,
 *   criando um comportamento de exploração mais errático e imprevisível
 * 
 */
public class DFS {
    
    /**
     * Encontra UM caminho (não necessariamente o mais curto) usando DFS.
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
        
        // Pilha LIFO para DFS
        Stack<Node> stack = new Stack<>();
        
        // Conjunto de nós visitados
        Set<Node> visited = new HashSet<>();
        
        // Mapa de predecessores
        Map<Node, Node> predecessors = new HashMap<>();
        
        // Inicialização
        stack.push(start);
        visited.add(start);
        
        // Loop principal do DFS
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            
            // Se encontramos o objetivo
            if (current.equals(goal)) {
                return reconstructPath(predecessors, start, goal);
            }
            
            // Explora vizinhos (ordem pode afetar o caminho encontrado)
            List<Node> neighbors = new ArrayList<>(current.getNeighbors().keySet());
            
            // Embaralha para criar variação no comportamento
            Collections.shuffle(neighbors);
            
            for (Node neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    predecessors.put(neighbor, current);
                    stack.push(neighbor);
                }
            }
        }
        
        // Não existe caminho
        return new ArrayList<>();
    }
    
    /**
     * Realiza DFS limitada em profundidade - explora até uma certa profundidade.
     * 
     * Útil para criar comportamento de patrulhamento em área limitada.
     * 
     * Complexidade: O(b^d) onde b = fator de ramificação, d = profundidade
     * 
     * param start nó de origem
     * param maxDepth profundidade máxima
     * return lista de nós visitados na ordem DFS
     */
    public static List<Node> exploreLimited(Node start, int maxDepth) {
        if (start == null || maxDepth < 0) {
            return new ArrayList<>();
        }
        
        List<Node> visited = new ArrayList<>();
        Set<Node> visitedSet = new HashSet<>();
        
        exploreLimitedHelper(start, maxDepth, visited, visitedSet);
        
        return visited;
    }
    
    /**
     * Auxiliar recursivo para DFS limitada.
     */
    private static void exploreLimitedHelper(Node node, int depth, 
                                            List<Node> visited, Set<Node> visitedSet) {
        if (node == null || depth < 0 || visitedSet.contains(node)) {
            return;
        }
        
        visited.add(node);
        visitedSet.add(node);
        
        if (depth > 0) {
            List<Node> neighbors = new ArrayList<>(node.getNeighbors().keySet());
            Collections.shuffle(neighbors);
            
            for (Node neighbor : neighbors) {
                exploreLimitedHelper(neighbor, depth - 1, visited, visitedSet);
            }
        }
    }
    
    /**
     * Calcula próxima direção usando DFS.
     * 
     * Nota: DFS não garante o caminho mais curto, criando movimento mais errático.
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
     * Retorna um vizinho aleatório não-visitado para patrulhamento.
     * 
     * Usado por Inky para criar comportamento de exploração contínua.
     * 
     * Complexidade: O(k) onde k = número de vizinhos (máx 4)
     * 
     * param current nó atual
     * param visited conjunto de nós já visitados
     * return vizinho aleatório não-visitado, ou vizinho aleatório qualquer
     */
    public static Node getRandomUnvisitedNeighbor(Node current, Set<Node> visited) {
        if (current == null) {
            return null;
        }
        
        List<Node> unvisitedNeighbors = new ArrayList<>();
        
        for (Node neighbor : current.getNeighbors().keySet()) {
            if (!visited.contains(neighbor)) {
                unvisitedNeighbors.add(neighbor);
            }
        }
        
        // Se há vizinhos não-visitados, escolhe um aleatório
        if (!unvisitedNeighbors.isEmpty()) {
            return unvisitedNeighbors.get(new Random().nextInt(unvisitedNeighbors.size()));
        }
        
        // Se todos foram visitados, escolhe qualquer vizinho
        List<Node> allNeighbors = new ArrayList<>(current.getNeighbors().keySet());
        if (!allNeighbors.isEmpty()) {
            return allNeighbors.get(new Random().nextInt(allNeighbors.size()));
        }
        
        return null;
    }
    
    /**
     * Reconstrói o caminho usando predecessores.
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
