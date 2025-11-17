# 🕹️ Pacman - Fase 1: Algoritmos de Grafos e IA de Fantasmas

**Disciplina:** Estruturas de Dados II (ED II)  
**Instituição:** UNESP - Universidade Estadual Paulista "Júlio de Mesquita Filho"  
**Curso:** Bacharelado em Sistemas de Informação  
**Fase:** 1 de 4 (Core Academic Implementation)

---

## 📋 Sumário

1. [Visão Geral](#-visão-geral)
2. [Objetivos Acadêmicos](#-objetivos-acadêmicos)
3. [Arquitetura do Sistema](#-arquitetura-do-sistema)
4. [Modelagem em Grafos](#-modelagem-em-grafos)
5. [Algoritmos Implementados](#-algoritmos-implementados)
6. [Hierarquia de Fantasmas](#-hierarquia-de-fantasmas)
7. [Análise de Complexidade](#-análise-de-complexidade)
8. [Como Compilar e Executar](#-como-compilar-e-executar)
9. [Estrutura do Projeto](#-estrutura-do-projeto)
10. [Justificativa Acadêmica](#-justificativa-acadêmica)

---

## 🎯 Visão Geral

Este projeto implementa um jogo Pacman completo e funcional com foco em **algoritmos de grafos** e **inteligência artificial** para controle dos fantasmas. A Fase 1 representa a implementação central dos requisitos acadêmicos de Estruturas de Dados II.

### Principais Características

✅ **Modelagem Completa do Labirinto como Grafo**
- Vértices representam células navegáveis
- Arestas conectam células adjacentes sem paredes
- Grafo não-direcionado com pesos uniformes

✅ **4 Algoritmos de Busca em Grafos**
- Dijkstra (caminho mais curto com pesos)
- A* (busca heurística com Manhattan distance)
- BFS (busca em largura)
- DFS (busca em profundidade)

✅ **4 Fantasmas Inteligentes**
- Blinky (Vermelho) - Dijkstra
- Pinky (Rosa) - A*
- Inky (Azul) - DFS
- Clyde (Laranja) - BFS

✅ **Código Totalmente Funcional**
- Sem TODOs ou implementações incompletas
- Documentação acadêmica completa
- Análise de complexidade detalhada

---

## 🎓 Objetivos Acadêmicos

Este projeto atende aos requisitos da disciplina ED II:

1. **Modelagem de Problemas Reais com Grafos**
   - Transformar um labirinto 2D em estrutura de grafo
   - Compreender vértices, arestas e conectividade

2. **Implementação de Algoritmos Clássicos**
   - Dijkstra para caminhos mais curtos
   - A* para busca heurística
   - BFS para exploração em largura
   - DFS para exploração em profundidade

3. **Análise de Complexidade**
   - Tempo: O(V + E), O(V²), O(E log V)
   - Espaço: O(V)
   - Comparação de desempenho

4. **Aplicação Prática**
   - IA para jogos
   - Pathfinding em tempo real
   - Design patterns (Template Method, Facade)

---

## 🏗️ Arquitetura do Sistema

```
┌─────────────────────────────────────────┐
│         PacMan (JPanel)                 │
│  - Interface gráfica                    │
│  - Game loop (50ms/frame = 20 FPS)      │
│  - Controle do jogador                  │
└──────────────┬──────────────────────────┘
               │
               ├──► PathfindingManager
               │    (Facade Pattern)
               │
               ├──► Graph
               │    └──► Node(s)
               │
               └──► Ghosts (4 instâncias)
                    ├── Blinky (Dijkstra)
                    ├── Pinky (A*)
                    ├── Inky (DFS)
                    └── Clyde (BFS)
```

### Fluxo de Execução

1. **Inicialização**
   - Carregar mapa do labirinto
   - Construir grafo a partir do mapa
   - Criar fantasmas com suas estratégias

2. **Loop do Jogo (20 FPS)**
   - Mover Pacman baseado em input
   - Atualizar IA dos fantasmas (a cada 4 frames)
   - Detectar colisões
   - Renderizar frame

3. **Atualização de IA (5 FPS)**
   - Obter posição do Pacman
   - Calcular caminho usando algoritmo específico
   - Atualizar direção do fantasma

---

## 📊 Modelagem em Grafos

### Transformação do Labirinto

O labirinto 21×19 é transformado em um grafo:

```
Mapa Original:          Grafo Resultante:
X X X X X               ∅ ∅ ∅ ∅ ∅
X     X                 ∅ •━•━• ∅
X X   X                 ∅ ∅ ━• ∅
X     X                 ∅ •━•━• ∅
X X X X X               ∅ ∅ ∅ ∅ ∅

X = parede (não é vértice)
• = célula navegável (vértice)
━ = conexão (aresta)
```

### Propriedades do Grafo

- **Tipo:** Não-direcionado, ponderado (pesos uniformes = 1)
- **Vértices (V):** ~250 células navegáveis
- **Arestas (E):** ~400 conexões entre células adjacentes
- **Grau médio:** ~1.6 vizinhos por vértice
- **Conectividade:** Grafo conexo (todos os vértices alcançáveis)

### Construção do Grafo

**Algoritmo de Construção:** O(V)

```java
buildFromTileMap(String[] tileMap):
    // Primeira passagem: criar nós O(rows × cols)
    for cada célula do mapa:
        if célula navegável (não é parede):
            criar Node(posição)
            adicionar ao Map<Position, Node>
    
    // Segunda passagem: conectar vizinhos O(V × 4)
    for cada nó no grafo:
        for cada direção (UP, DOWN, LEFT, RIGHT):
            if existe nó vizinho naquela direção:
                criar aresta bidirecional
```

---

## 🔍 Algoritmos Implementados

### 1. Dijkstra (Caminho Mais Curto)

**Arquivo:** `src/graph/algorithms/Dijkstra.java`

#### Descrição
Algoritmo guloso que encontra o caminho de custo mínimo entre dois vértices. Garante encontrar o caminho ótimo em grafos com pesos não-negativos.

#### Pseudocódigo
```
Dijkstra(start, goal):
    distances[start] = 0
    distances[outros] = ∞
    
    pq = PriorityQueue()
    pq.add(start, 0)
    
    while pq não vazia:
        current = pq.poll()  // menor distância
        
        if current == goal:
            return reconstruir_caminho()
        
        for cada vizinho de current:
            nova_dist = distances[current] + peso(current, vizinho)
            
            if nova_dist < distances[vizinho]:
                distances[vizinho] = nova_dist
                predecessors[vizinho] = current
                pq.add(vizinho, nova_dist)
```

#### Complexidade
- **Tempo:** O((V + E) log V) com PriorityQueue
  - Para Pacman: ~(250 + 400) × log(250) ≈ 5,200 operações
- **Espaço:** O(V) para armazenar distâncias e predecessores

#### Vantagens
- ✅ Garante caminho ótimo
- ✅ Eficiente para grafos densos
- ✅ Funciona com qualquer peso não-negativo

#### Desvantagens
- ❌ Explora muitos nós desnecessários
- ❌ Mais lento que A* para dois pontos específicos

#### Uso no Jogo
**Blinky (Fantasma Vermelho)** usa Dijkstra para perseguir o Pacman pelo caminho mais curto, tornando-o o fantasma mais agressivo e direto.

---

### 2. A* (A-Star com Heurística)

**Arquivo:** `src/graph/algorithms/AStar.java`

#### Descrição
Extensão do Dijkstra que usa uma função heurística para guiar a busca. Combina custo real g(n) com estimativa heurística h(n).

#### Função de Avaliação
```
f(n) = g(n) + h(n)

onde:
  g(n) = custo real do início até n
  h(n) = Distância de Manhattan até o objetivo
       = |x_n - x_goal| + |y_n - y_goal|
```

#### Heurística: Distância de Manhattan
- **Admissível:** Nunca superestima o custo real
- **Consistente:** h(n) ≤ custo(n, n') + h(n')
- **Ideal para grades 4-direcionais**

#### Pseudocódigo
```
AStar(start, goal):
    gScore[start] = 0
    fScore[start] = heuristic(start, goal)
    
    openSet = PriorityQueue(ordenado por fScore)
    openSet.add(start)
    
    while openSet não vazio:
        current = openSet.poll()  // menor f(n)
        
        if current == goal:
            return reconstruir_caminho()
        
        for cada vizinho de current:
            tentative_g = gScore[current] + peso(current, vizinho)
            
            if tentative_g < gScore[vizinho]:
                predecessors[vizinho] = current
                gScore[vizinho] = tentative_g
                fScore[vizinho] = tentative_g + heuristic(vizinho, goal)
                openSet.add(vizinho)
```

#### Complexidade
- **Tempo:** O(E log V) em casos médios
  - Melhor caso: O(V) com heurística perfeita
  - Pior caso: O(V²) quando h(n) = 0 (vira Dijkstra)
- **Espaço:** O(V)

#### Comparação com Dijkstra
| Aspecto | Dijkstra | A* |
|---------|----------|-----|
| Nós explorados | ~100% | ~30-50% |
| Velocidade | Mais lento | Mais rápido |
| Otimalidade | ✅ Sim | ✅ Sim (com h admissível) |
| Uso | Todos destinos | Um destino específico |

#### Uso no Jogo
**Pinky (Fantasma Rosa)** usa A* para emboscar o Pacman. Prevê onde o Pacman estará (4 tiles à frente) e usa A* para interceptá-lo rapidamente.

---

### 3. BFS (Busca em Largura)

**Arquivo:** `src/graph/algorithms/BFS.java`

#### Descrição
Algoritmo de busca que explora todos os vértices a uma determinada distância antes de avançar. Usa fila FIFO.

#### Características
- Explora em "ondas" concêntricas
- Garante menor número de passos (não considera pesos)
- Ótimo para grafos não-ponderados

#### Pseudocódigo
```
BFS(start, goal):
    queue = Queue()
    queue.enqueue(start)
    visited.add(start)
    
    while queue não vazia:
        current = queue.dequeue()
        
        if current == goal:
            return reconstruir_caminho()
        
        for cada vizinho de current:
            if vizinho não visitado:
                visited.add(vizinho)
                predecessors[vizinho] = current
                queue.enqueue(vizinho)
```

#### Complexidade
- **Tempo:** O(V + E)
  - Para Pacman: O(250 + 400) = O(650)
- **Espaço:** O(V) para fila e visitados

#### Padrão de Exploração
```
Nível 0:    S
Nível 1:    •━S━•
Nível 2:  •━•━•━•━•
Nível 3: •━•━•━•━•━•

S = início
Números = distância do início
```

#### Vantagens
- ✅ Simples e rápido
- ✅ Garante caminho com menos arestas
- ✅ Explora sistematicamente

#### Desvantagens
- ❌ Não considera pesos das arestas
- ❌ Pode explorar muitos nós desnecessários

#### Uso no Jogo
**Clyde (Fantasma Laranja)** usa BFS com comportamento misto:
- Distância > 8 tiles: persegue Pacman
- Distância ≤ 8 tiles: foge para o canto

---

### 4. DFS (Busca em Profundidade)

**Arquivo:** `src/graph/algorithms/DFS.java`

#### Descrição
Algoritmo que explora o máximo possível ao longo de cada ramo antes de retroceder. Usa pilha LIFO (ou recursão).

#### Características
- Explora em "profundidade" antes de "largura"
- NÃO garante caminho mais curto
- Útil para exploração e patrulhamento

#### Pseudocódigo
```
DFS(start, goal):
    stack = Stack()
    stack.push(start)
    visited.add(start)
    
    while stack não vazia:
        current = stack.pop()
        
        if current == goal:
            return reconstruir_caminho()
        
        for cada vizinho de current:
            if vizinho não visitado:
                visited.add(vizinho)
                predecessors[vizinho] = current
                stack.push(vizinho)
```

#### Complexidade
- **Tempo:** O(V + E)
- **Espaço:** O(V) no pior caso (caminho mais longo)

#### Padrão de Exploração
```
BFS (largura):        DFS (profundidade):
    1                     1
   2 2                    2
  3 3 3                   3
 4 4 4 4                  4
                          5
                          6
```

#### Comparação BFS vs DFS
| Característica | BFS | DFS |
|----------------|-----|-----|
| Estrutura | Fila (FIFO) | Pilha (LIFO) |
| Exploração | Em ondas | Em profundidade |
| Caminho | Mais curto | Qualquer |
| Memória | Mais | Menos |
| Uso | Caminho mínimo | Exploração |

#### Uso no Jogo
**Inky (Fantasma Azul)** usa DFS para patrulhamento errático. Alterna entre:
- Modo patrulha: escolhe alvo aleatório, explora com DFS
- Modo perseguição: usa DFS para seguir Pacman

Resultado: comportamento imprevisível e "nervoso"

---

## 👻 Hierarquia de Fantasmas

### Design Pattern: Template Method

```java
abstract class Ghost {
    // Estrutura comum (Template Method)
    public void update() {
        if (isAlignedWithGrid()) {
            Direction dir = chooseDirection();  // Método abstrato
            setDirection(dir);
        }
        move();
    }
    
    // Método abstrato - cada fantasma implementa
    abstract Direction chooseDirection(pacmanX, pacmanY, pacmanDir);
}
```

---

### 🔴 Blinky - O Perseguidor (Dijkstra)

**Classe:** `src/entities/Blinky.java`

#### Personalidade
Agressivo, direto, implacável

#### Estratégia
Persegue o Pacman diretamente usando o caminho mais curto garantido por Dijkstra.

#### Implementação
```java
Direction chooseDirection(pacmanX, pacmanY, pacmanDirection) {
    // Perseguição direta - alvo = posição atual do Pacman
    return pathfindingManager.getNextDirectionDijkstra(
        this.x, this.y, pacmanX, pacmanY
    );
}
```

#### Características
- ✅ Mais perigoso - sempre usa caminho ótimo
- ✅ Consistente e previsível
- ❌ Pode ser evitado com táticas defensivas

---

### 🌸 Pinky - O Emboscador (A*)

**Classe:** `src/entities/Pinky.java`

#### Personalidade
Estratégico, astuto, tático

#### Estratégia
Tenta emboscar o Pacman prevendo sua posição futura:
- Calcula 4 tiles à frente da direção do Pacman
- Usa A* para chegar ao ponto de emboscada rapidamente

#### Implementação
```java
Direction chooseDirection(pacmanX, pacmanY, pacmanDirection) {
    // Previsão: 4 tiles à frente
    int targetX = pacmanX + pacmanDirection.dx * tileSize * 4;
    int targetY = pacmanY + pacmanDirection.dy * tileSize * 4;
    
    // Usa A* para emboscada eficiente
    return pathfindingManager.getNextDirectionAStar(
        this.x, this.y, targetX, targetY
    );
}
```

#### Características
- ✅ Mais rápido que Blinky (A* explora menos nós)
- ✅ Comportamento tático e surpreendente
- ❌ Falha se Pacman mudar de direção repentinamente

---

### 🔵 Inky - O Patrulheiro (DFS)

**Classe:** `src/entities/Inky.java`

#### Personalidade
Imprevisível, errático, explorador

#### Estratégia
Alterna entre patrulhamento e perseguição usando DFS:
- 50% do tempo: patrulha áreas aleatórias
- 50% do tempo: persegue Pacman

#### Implementação
```java
Direction chooseDirection(pacmanX, pacmanY, pacmanDirection) {
    if (shouldPatrol) {
        // Escolhe alvo aleatório e explora com DFS
        int randomX = random.nextInt(19) * tileSize;
        int randomY = random.nextInt(21) * tileSize;
        return pathfindingManager.getNextDirectionDFS(
            this.x, this.y, randomX, randomY
        );
    } else {
        // Persegue usando DFS (caminho não-ótimo)
        return pathfindingManager.getNextDirectionDFS(
            this.x, this.y, pacmanX, pacmanY
        );
    }
}
```

#### Características
- ✅ Altamente imprevisível
- ✅ Cobre áreas do labirinto eficientemente
- ❌ Menos eficiente na perseguição direta

---

### 🟠 Clyde - O Tímido (BFS)

**Classe:** `src/entities/Clyde.java`

#### Personalidade
Tímido, indeciso, covarde

#### Estratégia
Comportamento misto baseado em distância:
- **Longe (> 8 tiles):** Persegue o Pacman
- **Perto (≤ 8 tiles):** Foge para o canto

#### Implementação
```java
Direction chooseDirection(pacmanX, pacmanY, pacmanDirection) {
    // Calcula distância usando BFS
    int distance = pathfindingManager.getDistanceBFS(
        this.x, this.y, pacmanX, pacmanY
    );
    
    if (distance > 8) {
        // Longe: persegue
        return pathfindingManager.getNextDirectionBFS(
            this.x, this.y, pacmanX, pacmanY
        );
    } else {
        // Perto: foge para canto
        return pathfindingManager.getNextDirectionBFS(
            this.x, this.y, cornerX, cornerY
        );
    }
}
```

#### Características
- ✅ Comportamento único e interessante
- ✅ Menos ameaçador que outros fantasmas
- ❌ Previsível uma vez entendido

---

## 📈 Análise de Complexidade

### Resumo Comparativo

| Algoritmo | Tempo (Pior Caso) | Tempo (Caso Médio) | Espaço | Garante Ótimo? |
|-----------|-------------------|-------------------|---------|----------------|
| **Dijkstra** | O((V+E) log V) | O((V+E) log V) | O(V) | ✅ Sim |
| **A*** | O(E log V) | O(E log V) | O(V) | ✅ Sim* |
| **BFS** | O(V + E) | O(V + E) | O(V) | ✅ Sim** |
| **DFS** | O(V + E) | O(V + E) | O(V) | ❌ Não |

\* Com heurística admissível  
\*\* Para grafos não-ponderados

### Para o Labirinto do Pacman (21×19)

| Métrica | Valor |
|---------|-------|
| Vértices (V) | ~250 |
| Arestas (E) | ~400 |
| Dijkstra | ~5,200 ops |
| A* | ~2,000-3,000 ops |
| BFS | ~650 ops |
| DFS | ~650 ops |

### Desempenho em Tempo Real

Com atualização de IA a 5 FPS:
- **Dijkstra:** ~5,200 ops/frame → ~26,000 ops/segundo
- **A*:** ~2,500 ops/frame → ~12,500 ops/segundo
- **BFS/DFS:** ~650 ops/frame → ~3,250 ops/segundo

Todos são executados em **< 1ms** em hardware moderno.

---

## 💻 Como Compilar e Executar

### Pré-requisitos

- **Java JDK 11 ou superior**
- **Biblioteca Gson** (incluída em `lib/gson-2.10.1.jar`)

### Compilação

#### Opção 1: Via terminal (Linux/Mac)

```bash
# Navegar para o diretório do projeto
cd /caminho/para/pacman-refactored

# Compilar todos os arquivos
javac -cp "lib/*:src" -d bin src/**/*.java src/*.java

# Executar o jogo
java -cp "lib/*:bin" App
```

#### Opção 2: Via terminal (Windows)

```cmd
cd C:\caminho\para\pacman-refactored

javac -d bin -cp "lib/*" @sources.txt

java -cp "lib/*;bin" App
```

#### Opção 3: Executar com Menu

```bash
java -cp "lib/*:bin" MainMenu
```

### Executando com Eclipse/IntelliJ

1. Importar projeto como "Existing Java Project"
2. Adicionar `lib/gson-2.10.1.jar` ao Build Path
3. Executar `App.java` ou `MainMenu.java`

---

## 📁 Estrutura do Projeto

```
pacman-refactored/
│
├── src/
│   ├── graph/                          # Estruturas de Grafo
│   │   ├── Graph.java                  # Grafo principal
│   │   ├── Node.java                   # Nó/Vértice
│   │   └── algorithms/                 # Algoritmos de busca
│   │       ├── Dijkstra.java          # O((V+E) log V)
│   │       ├── AStar.java             # O(E log V)
│   │       ├── BFS.java               # O(V + E)
│   │       └── DFS.java               # O(V + E)
│   │
│   ├── models/                         # Modelos de dados
│   │   ├── Position.java              # Posição (x, y)
│   │   └── Direction.java             # Enum de direções
│   │
│   ├── managers/                       # Gerenciadores
│   │   └── PathfindingManager.java    # Facade para pathfinding
│   │
│   ├── entities/                       # Entidades do jogo
│   │   ├── Ghost.java                 # Classe abstrata base
│   │   ├── Blinky.java                # Vermelho - Dijkstra
│   │   ├── Pinky.java                 # Rosa - A*
│   │   ├── Inky.java                  # Azul - DFS
│   │   ├── Clyde.java                 # Laranja - BFS
│   │   ├── PacMan.java                # Jogo principal
│   │   ├── RankingManager.java        # Gerenciador de ranking
│   │   └── ScoreEntry.java            # Entrada de pontuação
│   │
│   ├── App.java                        # Ponto de entrada principal
│   └── MainMenu.java                   # Menu inicial
│
├── lib/
│   └── gson-2.10.1.jar                # Biblioteca JSON
│
├── Images/                             # Sprites e recursos gráficos
│   ├── pacmanUp.png
│   ├── pacmanDown.png
│   ├── pacmanLeft.png
│   ├── pacmanRight.png
│   ├── redGhost.png
│   ├── pinkGhost.png
│   ├── blueGhost.png
│   ├── orangeGhost.png
│   └── wall.png
│
├── README.md                           # Este arquivo
└── ranking.json                        # Ranking de pontuações (criado automaticamente)
```

### Contagem de Arquivos

- **Código-fonte:** 18 arquivos Java
- **Classes de algoritmo:** 4 (Dijkstra, A*, BFS, DFS)
- **Classes de fantasmas:** 5 (1 abstrata + 4 concretas)
- **Linhas de código:** ~2,500 (incluindo documentação)

---

## 🎯 Justificativa Acadêmica

### Por que este projeto é relevante para ED II?

#### 1. Aplicação Prática de Teoria de Grafos

**Conceitos Teóricos:**
- Vértices e arestas
- Grafos direcionados vs não-direcionados
- Pesos e custos
- Conectividade

**Aplicação no Projeto:**
- Labirinto → Grafo
- Células → Vértices
- Passagens → Arestas
- Pathfinding → Busca em grafos

#### 2. Comparação de Algoritmos

O projeto permite **análise comparativa** de:
- **Desempenho:** Dijkstra vs A* vs BFS vs DFS
- **Otimalidade:** Qual garante melhor caminho?
- **Complexidade:** Teoria vs prática
- **Casos de uso:** Quando usar cada um?

#### 3. Design de Software

**Padrões aplicados:**
- **Template Method:** Hierarquia de fantasmas
- **Facade:** PathfindingManager
- **Strategy:** Diferentes algoritmos de busca

#### 4. Análise de Complexidade Real

Comparação entre:
- **Complexidade Teórica:** O(V²), O(E log V)
- **Complexidade Prática:** Medições reais no jogo
- **Trade-offs:** Velocidade vs otimalidade

---

## 🚀 Próximas Fases (Planejamento)

### Fase 2: Otimizações Avançadas
- Algoritmo de Floyd-Warshall para todos os pares
- Jump Point Search (JPS)
- Otimizações de cache para pathfinding

### Fase 3: Power-ups e Modos
- Power pellets com inversão de perseguição
- Modos dos fantasmas (scatter, chase, frightened)
- Sistema de dificuldade progressiva

### Fase 4: Análise e Visualização
- Visualização de caminhos em tempo real
- Estatísticas de desempenho dos algoritmos
- Replay system para análise

---

## 📚 Referências Bibliográficas

1. **Cormen, T. H., et al.** (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.
   - Capítulo 24: Single-Source Shortest Paths (Dijkstra)
   - Capítulo 22: Elementary Graph Algorithms (BFS, DFS)

2. **Hart, P. E., Nilsson, N. J., & Raphael, B.** (1968). A Formal Basis for the Heuristic Determination of Minimum Cost Paths. *IEEE Transactions on Systems Science and Cybernetics*, 4(2), 100-107.
   - Artigo original do algoritmo A*

3. **Russell, S., & Norvig, P.** (2020). *Artificial Intelligence: A Modern Approach* (4th ed.). Pearson.
   - Capítulo 3: Solving Problems by Searching

4. **Sedgewick, R., & Wayne, K.** (2011). *Algorithms* (4th ed.). Addison-Wesley.
   - Seção 4.4: Shortest Paths

---

## 👥 Autores

**Projeto Acadêmico**  
Disciplina: Estruturas de Dados II  
Instituição: UNESP - Bacharelado em Sistemas de Informação  

**Fase 1 - Core Implementation**  
Data: Novembro 2025  

---

## 📄 Licença

Este é um projeto acadêmico desenvolvido para fins educacionais.

---

## 🎮 Como Jogar

### Controles
- **↑ ↓ ← →** - Mover o Pacman
- **ESC** - Sair (na tela de game over)

### Objetivo
- Comer todas as bolinhas brancas
- Evitar os fantasmas
- Fazer a maior pontuação possível

### Fantasmas
- 🔴 **Blinky** - Te persegue pelo caminho mais curto
- 🌸 **Pinky** - Tenta te emboscar prevendo seu movimento
- 🔵 **Inky** - Patrulha áreas de forma imprevisível
- 🟠 **Clyde** - Comportamento misto (persegue/foge)

---

## ❓ FAQ

### Por que usar grafos para um jogo 2D?
Grafos abstraem a estrutura do labirinto, permitindo aplicar algoritmos clássicos de forma eficiente.

### A* é sempre melhor que Dijkstra?
Para um único destino, sim. A* é mais rápido. Mas Dijkstra calcula caminhos para TODOS os destinos de uma vez.

### Por que DFS não garante caminho ótimo?
DFS explora em profundidade primeiro, podendo encontrar caminhos longos antes de caminhos curtos.

### Como os fantasmas decidem quando atualizar direção?
A cada 4 frames (0.2 segundos), e apenas quando estão alinhados com o grid.

---

## 📞 Contato

Para dúvidas sobre o projeto acadêmico, consulte:
- Professor da disciplina ED II
- Documentação no código-fonte
- README.md (este arquivo)

---

**🎓 UNESP - Estruturas de Dados II - Fase 1 Completa** ✅
