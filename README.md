# Pacman - Fase 1: Algoritmos de Grafos e IA de Fantasmas

**Disciplina:** Estruturas de Dados II (ED II)  
**Instituicao:** UNESP - Universidade Estadual Paulista "Julio de Mesquita Filho"  
**Curso:** Bacharelado em Sistemas de Informacao  
**Fase:** 1 de 4 (Core Academic Implementation)

---

## Visao Geral

Este projeto implementa um jogo Pacman completo e funcional com foco em **algoritmos de grafos** e **inteligencia artificial** para controle dos fantasmas. A Fase 1 representa a implementacao central dos requisitos academicos de Estruturas de Dados II.

### Principais Caracteristicas

- **Modelagem Completa do Labirinto como Grafo**
  - Vertices representam celulas navegaveis
  - Arestas conectam celulas adjacentes sem paredes
  - Grafo nao-direcionado com pesos uniformes

- **4 Algoritmos de Busca em Grafos**
  - Dijkstra (caminho mais curto com pesos)
  - A* (busca heuristica com Manhattan distance)
  - BFS (busca em largura)
  - DFS (busca em profundidade)

- **4 Fantasmas Inteligentes**
  - Blinky (Vermelho) - Dijkstra - Persegue diretamente
  - Pinky (Rosa) - A* - Tenta interceptar
  - Inky (Azul) - DFS - Comportamento exploratorio
  - Clyde (Laranja) - BFS - Alterna entre perseguir e fugir

- **Desempenho Otimizado**
  - 60 FPS (frames por segundo)
  - Atualizacao de IA a cada 4 frames
  - Codigo completo sem TODOs ou placeholders

---

## Requisitos

- **Java JDK 11 ou superior**
- **Biblioteca Gson** (incluida em `lib/gson-2.8.9.jar`)

---

## Como Compilar e Executar

### Windows

#### Compilar:
```cmd
compile.bat
```

#### Executar o jogo:
```cmd
run.bat
```

#### Executar com menu:
```cmd
run-menu.bat
```

### Linux / macOS

#### Compilar:
```bash
./compile.sh
```

#### Executar o jogo:
```bash
./run.sh
```

#### Executar com menu:
```bash
./run-menu.sh
```

### Comandos Manuais

Se preferir compilar manualmente:

**Windows:**
```cmd
mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin -cp "lib/*" @sources.txt
java -cp "lib/*;bin" App
```

**Linux/macOS:**
```bash
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin -cp "lib/*" @sources.txt
java -cp "lib/*:bin" App
```

---

## Estrutura do Projeto

```
pacman_fase1/
├── src/
│   ├── graph/                   # Estruturas de Grafo
│   │   ├── Graph.java           # Grafo principal
│   │   ├── Node.java            # No/Vertice
│   │   └── algorithms/          # Algoritmos de busca
│   │       ├── Dijkstra.java    # O((V+E) log V)
│   │       ├── AStar.java       # O(E log V)
│   │       ├── BFS.java         # O(V + E)
│   │       └── DFS.java         # O(V + E)
│   │
│   ├── models/                  # Modelos de dados
│   │   ├── Position.java        # Posicao (x, y)
│   │   └── Direction.java       # Enum de direcoes
│   │
│   ├── managers/                # Gerenciadores
│   │   └── PathfindingManager.java  # Facade para pathfinding
│   │
│   ├── entities/                # Entidades do jogo
│   │   ├── Ghost.java           # Classe abstrata base
│   │   ├── Blinky.java          # Vermelho - Dijkstra
│   │   ├── Pinky.java           # Rosa - A*
│   │   ├── Inky.java            # Azul - DFS
│   │   ├── Clyde.java           # Laranja - BFS
│   │   ├── PacMan.java          # Jogo principal
│   │   ├── RankingManager.java  # Gerenciador de ranking
│   │   └── ScoreEntry.java      # Entrada de pontuacao
│   │
│   ├── App.java                 # Ponto de entrada principal
│   └── MainMenu.java            # Menu inicial
│
├── lib/
│   └── gson-2.8.9.jar           # Biblioteca JSON
│
├── Images/                      # Sprites e recursos graficos
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
├── compile.bat                  # Script de compilacao Windows
├── compile.sh                   # Script de compilacao Linux
├── run.bat                      # Script de execucao Windows
├── run.sh                       # Script de execucao Linux
├── run-menu.bat                 # Menu Windows
├── run-menu.sh                  # Menu Linux
└── README.md                    # Este arquivo
```

---

## Algoritmos Implementados

### 1. Dijkstra (Caminho Mais Curto)

**Arquivo:** `src/graph/algorithms/Dijkstra.java`

- **Complexidade:** O((V + E) log V)
- **Uso:** Blinky (Fantasma Vermelho)
- **Caracteristica:** Garante caminho otimo, perseguicao direta e agressiva

### 2. A* (Busca Heuristica)

**Arquivo:** `src/graph/algorithms/AStar.java`

- **Complexidade:** O(E log V) em casos medios
- **Heuristica:** Distancia de Manhattan
- **Uso:** Pinky (Fantasma Rosa)
- **Caracteristica:** Rapido e eficiente, tenta emboscar o Pacman

### 3. BFS (Busca em Largura)

**Arquivo:** `src/graph/algorithms/BFS.java`

- **Complexidade:** O(V + E)
- **Uso:** Clyde (Fantasma Laranja)
- **Caracteristica:** Explora em "ondas", comportamento timido (persegue/foge)

### 4. DFS (Busca em Profundidade)

**Arquivo:** `src/graph/algorithms/DFS.java`

- **Complexidade:** O(V + E)
- **Uso:** Inky (Fantasma Azul)
- **Caracteristica:** Exploracao profunda, comportamento imprevisivel

---

## Comportamento dos Fantasmas

### Blinky (Vermelho) - O Perseguidor
- **Algoritmo:** Dijkstra
- **Estrategia:** Persegue o Pacman diretamente pelo caminho mais curto
- **Personalidade:** Agressivo, direto, implacavel
- **Perigo:** ALTO - Sempre usa o caminho otimo

### Pinky (Rosa) - O Emboscador
- **Algoritmo:** A*
- **Estrategia:** Preve a posicao do Pacman (4 tiles a frente) e tenta interceptar
- **Personalidade:** Estrategico, astuto, tatico
- **Perigo:** ALTO - Rapido e surpreendente

### Inky (Azul) - O Patrulheiro
- **Algoritmo:** DFS
- **Estrategia:** Alterna entre patrulhamento aleatorio e perseguicao
- **Personalidade:** Imprevisivel, erratico, explorador
- **Perigo:** MEDIO - Dificil de prever

### Clyde (Laranja) - O Timido
- **Algoritmo:** BFS
- **Estrategia:** Persegue quando longe (> 8 tiles), foge quando perto
- **Personalidade:** Timido, indeciso, covarde
- **Perigo:** BAIXO - Menos ameacador

---

## Como Jogar

### Controles
- **Setas do teclado** - Mover o Pacman
- **ESC** - Sair (na tela de game over)

### Objetivo
- Comer todas as bolinhas brancas
- Evitar os fantasmas
- Fazer a maior pontuacao possivel

### Pontuacao
- Cada bolinha: 10 pontos
- 3 vidas iniciais
- Game Over quando perder todas as vidas

---

## Analise de Complexidade

### Grafo do Labirinto
- **Vertices (V):** ~250 celulas navegaveis
- **Arestas (E):** ~400 conexoes entre celulas adjacentes
- **Tipo:** Grafo nao-direcionado, pesos uniformes (peso = 1)

### Desempenho dos Algoritmos

| Algoritmo | Tempo (Pior Caso) | Espaco | Garante Otimo? |
|-----------|-------------------|--------|----------------|
| Dijkstra  | O((V+E) log V)    | O(V)   | Sim            |
| A*        | O(E log V)        | O(V)   | Sim*           |
| BFS       | O(V + E)          | O(V)   | Sim**          |
| DFS       | O(V + E)          | O(V)   | Nao            |

\* Com heuristica admissivel  
\*\* Para grafos nao-ponderados

### Performance em Tempo Real
- **FPS:** 60 (16ms por frame)
- **Atualizacao de IA:** A cada 4 frames (~15 vezes por segundo)
- **Tempo de pathfinding:** < 1ms por fantasma

---

## Detalhes Tecnicos

### Sistema de Grafos
1. O labirinto eh convertido em um grafo onde:
   - Cada celula navegavel = 1 vertice
   - Celulas adjacentes sem parede = aresta bidirecional
2. Os algoritmos trabalham sobre este grafo para calcular caminhos
3. O PathfindingManager atua como Facade para simplificar o uso dos algoritmos

### Integracao com Gameplay
1. **Inicializacao:** Grafo eh construido a partir do mapa de tiles
2. **Loop do Jogo (60 FPS):** 
   - Mover Pacman baseado em input do usuario
   - Mover fantasmas (fisica a 60 FPS)
   - Atualizar IA dos fantasmas (a cada 4 frames)
   - Detectar colisoes
   - Renderizar frame
3. **Atualizacao de IA (15 vezes/segundo):**
   - Cada fantasma calcula proximo movimento usando seu algoritmo
   - Direcao eh atualizada apenas quando alinhado com o grid

---

## Resolucao de Problemas

### Erro de compilacao
- Verifique se o Java JDK 11+ esta instalado: `java -version`
- Verifique se a biblioteca gson esta em `lib/gson-2.8.9.jar`
- Execute o script de compilacao correspondente ao seu sistema

### Imagens nao aparecem
- Certifique-se de que a pasta `Images/` existe
- Verifique se todos os arquivos PNG estao presentes
- Se faltar imagens, o jogo usara cores solidas como fallback

### Fantasmas nao se movem corretamente
- Verifique se o grafo foi construido corretamente (mensagem no console)
- O console deve mostrar: "Grafo: Graph{vertices=~250, edges=~400}"
- Se os valores forem muito diferentes, pode haver problema no mapa

### Performance baixa
- O jogo foi otimizado para 60 FPS
- Se estiver lento, verifique a carga da CPU
- Os algoritmos sao eficientes e nao devem causar lentidao

---

## Objetivos Academicos

Este projeto atende aos requisitos da disciplina ED II:

1. **Modelagem de Problemas Reais com Grafos**
   - Transformar um labirinto 2D em estrutura de grafo
   - Compreender vertices, arestas e conectividade

2. **Implementacao de Algoritmos Classicos**
   - Dijkstra para caminhos mais curtos
   - A* para busca heuristica
   - BFS para exploracao em largura
   - DFS para exploracao em profundidade

3. **Analise de Complexidade**
   - Tempo: O(V + E), O(V^2), O(E log V)
   - Espaco: O(V)
   - Comparacao de desempenho

4. **Aplicacao Pratica**
   - IA para jogos
   - Pathfinding em tempo real
   - Design patterns (Template Method, Facade)

---

## Proximas Fases (Planejamento)

### Fase 2: Otimizacoes Avancadas
- Algoritmo de Floyd-Warshall para todos os pares
- Jump Point Search (JPS)
- Otimizacoes de cache para pathfinding

### Fase 3: Power-ups e Modos
- Power pellets com inversao de perseguicao
- Modos dos fantasmas (scatter, chase, frightened)
- Sistema de dificuldade progressiva

### Fase 4: Analise e Visualizacao
- Visualizacao de caminhos em tempo real
- Estatisticas de desempenho dos algoritmos
- Sistema de replay para analise

---

## Autores

**Projeto Academico**  
Disciplina: Estruturas de Dados II  
Instituicao: UNESP - Bacharelado em Sistemas de Informacao  

**Fase 1 - Core Implementation**  
Data: Novembro 2024  

---

## Licenca

Este eh um projeto academico desenvolvido para fins educacionais.

---

## Contato

Para duvidas sobre o projeto academico, consulte:
- Professor da disciplina ED II
- Documentacao no codigo-fonte
- README.md (este arquivo)

---

**UNESP - Estruturas de Dados II - Fase 1 Completa**
