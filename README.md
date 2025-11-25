# 🎮 Pac-Man com Algoritmos de Grafos

<div align="center">

**Trabalho Final - Estrutura de Dados II**  
*UNESP - Universidade Estadual Paulista "Júlio de Mesquita Filho"*  
*Bacharelado em Sistemas de Informação*

</div>

---

## 📋 Sobre o Projeto

Este repositório apresenta o desenvolvimento de um jogo no estilo clássico Pac-Man, utilizando a linguagem **Java** e conceitos de **Orientação a Objetos**. O diferencial deste projeto está na aplicação prática de **4 Algoritmos de Busca em Grafos** para controlar a inteligência artificial dos fantasmas, cada um com comportamento único e estratégias distintas.

### 🎯 Objetivos Acadêmicos

- Aplicar conceitos de **Teoria dos Grafos** em um problema real
- Implementar e comparar **algoritmos clássicos de busca**
- Desenvolver **inteligência artificial** para NPCs (fantasmas)
- Analisar **complexidade computacional** em tempo real
- Utilizar **Design Patterns** (Facade, Template Method)

---

## 🚀 Como Executar

### Requisitos
- **Java JDK 11** ou superior
- **Biblioteca Gson** (incluída em `lib/gson-2.8.9.jar`)

### Windows
```cmd
compile.bat
run.bat
```

### Linux / macOS
Não é necessário compilação manual - o projeto já está pronto para execução. Se necessário, use os comandos Java padrão.

---

## 🧠 Algoritmos Implementados

O projeto utiliza 4 algoritmos clássicos de busca em grafos, cada um controlando um fantasma diferente:

### 1️⃣ Dijkstra - Blinky (Fantasma Vermelho) 🔴

**Algoritmo de Dijkstra** garante encontrar o **caminho mais curto** entre dois pontos em um grafo com pesos não-negativos.

#### 📊 Características
- **Complexidade:** O((V + E) log V)
- **Estrutura:** Priority Queue (fila de prioridade)
- **Garantia:** Sempre encontra o caminho ótimo

#### 💻 Implementação do Algoritmo

```java
public static List<Node> findPath(Node start, Node goal) {
    // Mapa de distâncias (infinito por padrão)
    Map<Node, Double> distances = new HashMap<>();
    distances.put(start, 0.0);

    // Mapa de predecessores para reconstruir o caminho
    Map<Node, Node> predecessors = new HashMap<>();

    // Fila de prioridade ordenada por distância
    PriorityQueue<NodeDistance> pq = new PriorityQueue<>(
        Comparator.comparingDouble(nd -> nd.distance)
    );
    pq.offer(new NodeDistance(start, 0.0));

    Set<Node> visited = new HashSet<>();

    while (!pq.isEmpty()) {
        NodeDistance current = pq.poll();
        Node currentNode = current.node;

        if (visited.contains(currentNode)) continue;
        visited.add(currentNode);

        // Se chegamos ao objetivo, reconstruir e retornar o caminho
        if (currentNode.equals(goal)) {
            return reconstructPath(predecessors, start, goal);
        }

        // Explorar todos os vizinhos
        for (Node neighbor : currentNode.getAllNeighbors()) {
            if (visited.contains(neighbor)) continue;

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

    return new ArrayList<>(); // Nenhum caminho encontrado
}
```

#### 🎮 Integração no Fantasma Blinky

```java
@Override
protected Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
    // Perseguição direta usando Dijkstra - sempre o caminho mais curto
    Direction nextDir = pathfindingManager.getNextDirectionDijkstra(
        this.x, this.y, pacmanX, pacmanY
    );

    // Se Dijkstra não retornou uma direção válida, manter a direção atual
    if (nextDir == Direction.NONE) {
        return this.direction;
    }

    return nextDir;
}
```

**🎯 Comportamento:** Blinky é o fantasma mais perigoso! Ele sempre persegue o Pac-Man pelo caminho mais curto possível, sendo agressivo, direto e implacável.

---

### 2️⃣ A* (A-Star) - Pinky (Fantasma Rosa) 🩷

**Algoritmo A*** combina o custo real do caminho com uma **heurística** (distância de Manhattan) para encontrar caminhos de forma eficiente.

#### 📊 Características
- **Complexidade:** O(E log V) em casos médios
- **Heurística:** Distância de Manhattan
- **Função:** f(n) = g(n) + h(n)
  - g(n) = custo real do início até n
  - h(n) = estimativa heurística até o objetivo

#### 💻 Implementação do Algoritmo

```java
public static List<Node> findPath(Node start, Node goal) {
    // gScore: custo real do início até cada nó
    Map<Node, Double> gScore = new HashMap<>();
    gScore.put(start, 0.0);

    // fScore: gScore + heurística
    Map<Node, Double> fScore = new HashMap<>();
    fScore.put(start, heuristic(start, goal));

    // Predecessores para reconstruir o caminho
    Map<Node, Node> predecessors = new HashMap<>();

    // Open set: nós a serem avaliados
    PriorityQueue<NodeScore> openSet = new PriorityQueue<>(
        Comparator.comparingDouble(ns -> ns.fScore)
    );
    openSet.offer(new NodeScore(start, fScore.get(start)));

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
            if (closedSet.contains(neighbor)) continue;

            // Calcular novo gScore
            double tentativeGScore = gScore.get(currentNode) + 1.0;
            double currentGScore = gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY);

            // Se encontramos um caminho melhor
            if (tentativeGScore < currentGScore) {
                predecessors.put(neighbor, currentNode);
                gScore.put(neighbor, tentativeGScore);
                double newFScore = tentativeGScore + heuristic(neighbor, goal);
                fScore.put(neighbor, newFScore);

                openSet.offer(new NodeScore(neighbor, newFScore));
            }
        }
    }

    return new ArrayList<>();
}

// Heurística de Manhattan
private static double heuristic(Node from, Node to) {
    Position fromPos = from.getPosition();
    Position toPos = to.getPosition();
    return fromPos.manhattanDistance(toPos);
}
```

#### 🎮 Integração no Fantasma Pinky

```java
@Override
protected Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
    // Previsão: calcular 4 tiles à frente da direção do Pacman
    int targetX = pacmanX + (pacmanDirection.dx * tileSize * 4);
    int targetY = pacmanY + (pacmanDirection.dy * tileSize * 4);

    // Usar A* para emboscada eficiente
    Direction nextDir = pathfindingManager.getNextDirectionAStar(
        this.x, this.y, targetX, targetY
    );

    // Se A* não retornou uma direção válida, tentar ir direto ao Pacman
    if (nextDir == Direction.NONE) {
        nextDir = pathfindingManager.getNextDirectionAStar(
            this.x, this.y, pacmanX, pacmanY
        );
    }

    if (nextDir == Direction.NONE) {
        return this.direction;
    }

    return nextDir;
}
```

**🎯 Comportamento:** Pinky é estratégico e astuto! Ele não persegue diretamente, mas tenta **emboscar** o Pac-Man prevendo sua posição futura (4 tiles à frente), usando A* para chegar lá rapidamente.

---

### 3️⃣ BFS (Busca em Largura) - Clyde (Fantasma Laranja) 🟠

**BFS (Breadth-First Search)** explora o grafo em "ondas" concêntricas, garantindo encontrar o caminho com menor número de arestas.

#### 📊 Características
- **Complexidade:** O(V + E)
- **Estrutura:** Queue FIFO (fila)
- **Exploração:** Camada por camada

#### 💻 Implementação do Algoritmo

```java
public static List<Node> findPath(Node start, Node goal) {
    // Fila FIFO para BFS
    Queue<Node> queue = new LinkedList<>();
    queue.offer(start);

    // Conjunto de nós visitados
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

    return new ArrayList<>();
}

// Método auxiliar para calcular distância
public static int getDistance(Node start, Node goal) {
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
```

#### 🎮 Integração no Fantasma Clyde

```java
@Override
protected Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
    // Calcular distância até o Pacman usando BFS
    int distance = pathfindingManager.getDistanceBFS(this.x, this.y, pacmanX, pacmanY);

    Direction nextDir;

    if (distance > FLEE_DISTANCE || distance == -1) {
        // Longe: perseguir o Pacman
        nextDir = pathfindingManager.getNextDirectionBFS(
            this.x, this.y, pacmanX, pacmanY
        );
    } else {
        // Perto: fugir para o canto
        nextDir = pathfindingManager.getNextDirectionBFS(
            this.x, this.y, cornerX, cornerY
        );
    }

    if (nextDir == Direction.NONE) {
        return this.direction;
    }

    return nextDir;
}
```

**🎯 Comportamento:** Clyde é tímido e indeciso! Quando está **longe** do Pac-Man (> 8 tiles), ele persegue. Mas quando fica **perto** (≤ 8 tiles), ele foge para o canto inferior esquerdo, criando um comportamento menos ameaçador.

---

### 4️⃣ DFS (Busca em Profundidade) - Inky (Fantasma Azul) 🔵

**DFS (Depth-First Search)** explora o máximo possível ao longo de cada ramo antes de retroceder, criando comportamento imprevisível.

#### 📊 Características
- **Complexidade:** O(V + E)
- **Estrutura:** Stack LIFO (pilha)
- **Exploração:** Profunda e aleatória
- **Garantia:** NÃO garante caminho ótimo

#### 💻 Implementação do Algoritmo

```java
public static List<Node> findPath(Node start, Node goal) {
    // Pilha LIFO para DFS
    Stack<Node> stack = new Stack<>();
    stack.push(start);

    // Conjunto de nós visitados
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

        // Explorar todos os vizinhos (em ordem aleatória para mais imprevisibilidade)
        List<Node> neighbors = new ArrayList<>(current.getAllNeighbors());
        Collections.shuffle(neighbors); // Aleatoriza a exploração

        for (Node neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                predecessors.put(neighbor, current);
                stack.push(neighbor);
            }
        }
    }

    return new ArrayList<>();
}
```

#### 🎮 Integração no Fantasma Inky

```java
@Override
protected Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection) {
    // Alternar entre patrulha e perseguição a cada PATROL_DURATION frames
    patrolTimer++;
    if (patrolTimer >= PATROL_DURATION) {
        isPatrolling = !isPatrolling;
        patrolTimer = 0;
    }

    Direction nextDir;

    if (isPatrolling) {
        // Modo patrulha: escolher um alvo aleatório no mapa
        int randomX = random.nextInt(19) * tileSize;
        int randomY = random.nextInt(21) * tileSize;

        nextDir = pathfindingManager.getNextDirectionDFS(
            this.x, this.y, randomX, randomY
        );
    } else {
        // Modo perseguição: usar DFS para seguir o Pacman
        nextDir = pathfindingManager.getNextDirectionDFS(
            this.x, this.y, pacmanX, pacmanY
        );
    }

    if (nextDir == Direction.NONE) {
        return this.direction;
    }

    return nextDir;
}
```

**🎯 Comportamento:** Inky é imprevisível e errático! Ele alterna entre **patrulhamento aleatório** (explorando o mapa) e **perseguição** usando DFS, criando movimentos difíceis de prever e cobrindo áreas do labirinto de forma não-ótima.

---

## 🏗️ Arquitetura do Sistema

### PathfindingManager (Facade Pattern)

O `PathfindingManager` atua como uma **Facade** (fachada), simplificando o uso dos algoritmos de pathfinding:

```java
public class PathfindingManager {
    private final Graph graph;

    // Métodos simplificados para cada algoritmo
    public Direction getNextDirectionDijkstra(int startX, int startY, int goalX, int goalY) {
        return getNextDirection(startX, startY, goalX, goalY, PathAlgorithm.DIJKSTRA);
    }

    public Direction getNextDirectionAStar(int startX, int startY, int goalX, int goalY) {
        return getNextDirection(startX, startY, goalX, goalY, PathAlgorithm.ASTAR);
    }

    public Direction getNextDirectionBFS(int startX, int startY, int goalX, int goalY) {
        return getNextDirection(startX, startY, goalX, goalY, PathAlgorithm.BFS);
    }

    public Direction getNextDirectionDFS(int startX, int startY, int goalX, int goalY) {
        return getNextDirection(startX, startY, goalX, goalY, PathAlgorithm.DFS);
    }

    // Método genérico que executa o algoritmo e retorna a próxima direção
    private Direction getNextDirection(int startX, int startY, int goalX, int goalY, 
                                      PathAlgorithm algorithm) {
        Node start = graph.getNode(startX, startY);
        Node goal = graph.getNode(goalX, goalY);

        if (start == null || goal == null) return Direction.NONE;
        if (start.equals(goal)) return Direction.NONE;

        // Encontrar caminho usando o algoritmo especificado
        List<Node> path;
        switch (algorithm) {
            case DIJKSTRA: path = Dijkstra.findPath(start, goal); break;
            case ASTAR: path = AStar.findPath(start, goal); break;
            case BFS: path = BFS.findPath(start, goal); break;
            case DFS: path = DFS.findPath(start, goal); break;
            default: return Direction.NONE;
        }

        if (path.isEmpty() || path.size() < 2) return Direction.NONE;

        // O próximo nó é o segundo elemento do caminho (índice 1)
        Node nextNode = path.get(1);

        // Determinar a direção para o próximo nó
        return start.getDirectionTo(nextNode);
    }
}
```

**Vantagens do Facade:**
- Interface simplificada para os fantasmas
- Encapsula a complexidade dos algoritmos
- Facilita manutenção e testes
- Converte automaticamente caminhos em direções

---

### Direction (Enum)

O enum `Direction` representa as 4 direções possíveis de movimento no jogo:

```java
public enum Direction {
    UP(0, -1, 'U'),
    DOWN(0, 1, 'D'),
    LEFT(-1, 0, 'L'),
    RIGHT(1, 0, 'R'),
    NONE(0, 0, 'N');

    public final int dx;  // Delta X
    public final int dy;  // Delta Y
    public final char code; // Código de caractere

    Direction(int dx, int dy, char code) {
        this.dx = dx;
        this.dy = dy;
        this.code = code;
    }

    // Retorna a direção oposta
    public Direction opposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return NONE;
        }
    }
}
```

**Funcionalidades:**
- `dx, dy`: Vetores de movimento (ex: UP = (0, -1))
- `code`: Código de caractere para serialização
- `opposite()`: Retorna direção oposta (útil para evitar reversões)
- `NONE`: Representa ausência de movimento

---

## 📊 Análise de Complexidade

### Grafo do Labirinto
- **Vértices (V):** ~250 células navegáveis
- **Arestas (E):** ~400 conexões entre células adjacentes
- **Tipo:** Grafo não-direcionado, pesos uniformes (peso = 1)

### Comparação dos Algoritmos

| Algoritmo | Complexidade Tempo | Complexidade Espaço | Garante Ótimo? | Uso no Jogo |
|-----------|-------------------|---------------------|----------------|-------------|
| **Dijkstra** | O((V+E) log V) | O(V) | ✅ Sim | Blinky - Perseguição direta |
| **A*** | O(E log V) | O(V) | ✅ Sim* | Pinky - Emboscada inteligente |
| **BFS** | O(V + E) | O(V) | ✅ Sim** | Clyde - Comportamento tímido |
| **DFS** | O(V + E) | O(V) | ❌ Não | Inky - Patrulhamento errático |

\* Com heurística admissível (Manhattan)  
\*\* Para grafos não-ponderados

### Performance em Tempo Real
- **FPS:** 60 (16ms por frame)
- **Atualização de IA:** A cada 4 frames (~15 vezes por segundo)
- **Tempo de pathfinding:** < 1ms por fantasma
- **Total de cálculos:** ~60 pathfindings por segundo (4 fantasmas × 15 updates)

---

## 🎮 Resumo dos Fantasmas

| Fantasma | Cor | Algoritmo | Personalidade | Estratégia | Nível de Perigo |
|----------|-----|-----------|---------------|------------|-----------------|
| **Blinky** | 🔴 Vermelho | Dijkstra | Agressivo, direto | Perseguição pelo caminho mais curto | 🔥🔥🔥 ALTO |
| **Pinky** | 🩷 Rosa | A* | Estratégico, astuto | Emboscada prevendo posição futura | 🔥🔥🔥 ALTO |
| **Inky** | 🔵 Azul | DFS | Imprevisível, errático | Patrulhamento + perseguição aleatória | 🔥🔥 MÉDIO |
| **Clyde** | 🟠 Laranja | BFS | Tímido, covarde | Persegue longe, foge perto | 🔥 BAIXO |

---

## 🎯 Como Jogar

### Controles
- **Setas do teclado** ⬆️⬇️⬅️➡️ - Mover o Pac-Man
- **ESC** - Sair do jogo

### Objetivo
- 🟡 Comer todas as bolinhas brancas
- 👻 Evitar os 4 fantasmas
- 🏆 Fazer a maior pontuação possível

### Pontuação
- Cada bolinha: **10 pontos**
- Vidas iniciais: **3**
- Game Over: quando perder todas as vidas

---

## 📁 Estrutura do Projeto

```
pacman/
├── src/
│   ├── graph/                      # 📊 Estruturas de Grafo
│   │   ├── Graph.java              # Grafo principal do labirinto
│   │   ├── Node.java               # Nó/Vértice do grafo
│   │   └── algorithms/             # 🧠 Algoritmos de busca
│   │       ├── Dijkstra.java       # Caminho mais curto
│   │       ├── AStar.java          # Busca heurística
│   │       ├── BFS.java            # Busca em largura
│   │       └── DFS.java            # Busca em profundidade
│   │
│   ├── models/                     # 📐 Modelos de dados
│   │   ├── Position.java           # Posição (x, y)
│   │   └── Direction.java          # Enum de direções
│   │
│   ├── managers/                   # 🎛️ Gerenciadores
│   │   └── PathfindingManager.java # Facade para pathfinding
│   │
│   ├── entities/                   # 👾 Entidades do jogo
│   │   ├── Ghost.java              # Classe abstrata base
│   │   ├── Blinky.java             # 🔴 Vermelho - Dijkstra
│   │   ├── Pinky.java              # 🩷 Rosa - A*
│   │   ├── Inky.java               # 🔵 Azul - DFS
│   │   ├── Clyde.java              # 🟠 Laranja - BFS
│   │   ├── Pacman.java             # 🟡 Jogador
│   │   ├── RankingManager.java     # Sistema de ranking
│   │   └── ScoreEntry.java         # Entrada de pontuação
│   │
│   ├── App.java                    # 🚀 Ponto de entrada
│   └── MainMenu.java               # 📋 Menu inicial
│
├── lib/
│   └── gson-2.8.9.jar              # Biblioteca JSON
│
├── Images/                         # 🎨 Sprites e recursos gráficos
│
├── compile.bat                     # Script de compilação Windows
└── run.bat                         # Script de execução Windows
```

---

## 🔬 Detalhes Técnicos

### Sistema de Grafos

1. **Construção do Grafo:**
   - O labirinto 2D é convertido em um grafo
   - Cada célula navegável = 1 vértice
   - Células adjacentes sem parede = aresta bidirecional
   - Peso uniforme = 1 para todas as arestas

2. **Pathfinding em Tempo Real:**
   - Algoritmos trabalham sobre o grafo para calcular caminhos
   - PathfindingManager converte caminhos em direções
   - Atualização a cada 4 frames para otimização

3. **Integração com Gameplay:**
   ```
   Inicialização → Construir Grafo do Labirinto
                ↓
   Loop 60 FPS → Mover Pac-Man (input do usuário)
                → Mover Fantasmas (física)
                → Atualizar IA (a cada 4 frames)
                → Detectar colisões
                → Renderizar frame
   ```

---

## 🎓 Conceitos de Estrutura de Dados II Aplicados

### ✅ Grafos
- Modelagem de labirinto como grafo não-direcionado
- Vértices, arestas e conectividade
- Representação por lista de adjacências

### ✅ Algoritmos de Busca
- **Dijkstra:** Caminho mais curto com pesos
- **A*:** Busca heurística informada
- **BFS:** Busca em largura (não-informada)
- **DFS:** Busca em profundidade (não-informada)

### ✅ Estruturas de Dados
- **Priority Queue:** Dijkstra e A*
- **Queue (FIFO):** BFS
- **Stack (LIFO):** DFS
- **HashMap:** Distâncias e predecessores
- **HashSet:** Nós visitados

### ✅ Análise de Complexidade
- Notação Big O
- Complexidade de tempo e espaço
- Trade-offs entre algoritmos

### ✅ Design Patterns
- **Facade:** PathfindingManager
- **Template Method:** Classe abstrata Ghost
- **Enum:** Direction

---

## 🏆 Diferenciais do Projeto

- ✨ **4 algoritmos diferentes** implementados e funcionais
- 🎮 **Comportamentos únicos** para cada fantasma
- 📊 **Análise comparativa** de desempenho
- 🏗️ **Arquitetura limpa** com design patterns
- 📝 **Código bem documentado** e didático
- ⚡ **Performance otimizada** (60 FPS)
- 🎯 **Aplicação prática** de conceitos teóricos

---

## 📚 Referências

- Cormen, T. H., et al. (2009). *Introduction to Algorithms* (3rd ed.)
- Dijkstra, E. W. (1959). *A note on two problems in connexion with graphs*
- Hart, P. E., et al. (1968). *A Formal Basis for the Heuristic Determination of Minimum Cost Paths*
- Russell, S., & Norvig, P. (2020). *Artificial Intelligence: A Modern Approach* (4th ed.)

---

## 👨‍💻 Autor

**Projeto Acadêmico**  
Disciplina: Estrutura de Dados II  
Instituição: UNESP - Bacharelado em Sistemas de Informação  
Ano: 2025

Alunos: 
Marcelo Henrique Ayala
Mateus Fernandes Pereira
Thales Hirose Nakandakari
Gleivan Afonso Bezerra

---

## 📄 Licença

Este é um projeto acadêmico desenvolvido para fins educacionais.

---

<div align="center">

**🎮 Desenvolvido com Java e ❤️ para Estrutura de Dados II**

*UNESP - Universidade Estadual Paulista "Júlio de Mesquita Filho"*

</div>
