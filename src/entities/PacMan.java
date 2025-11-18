package entities;

import graph.Graph;
import managers.PathfindingManager;
import models.Direction;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;

/**
 * Classe principal do jogo Pacman.
 * Integra grafo, pathfinding e IA dos fantasmas.
 * 
 * Atualizacao de IA: A cada 4 frames (~15 vezes por segundo)
 */
public class PacMan extends JPanel implements ActionListener, KeyListener {

    /**
     * TOLERÂNCIA DE POSICIONAMENTO:
     * Margem de pixels permitida para considerar o Pacman "alinhado" com o grid.
     */
    private static final int ALIGNMENT_TOLERANCE = 6;
    
    /**
     * TEMPO DE INPUT BUFFERING:
     * Quantos frames (ciclos do game loop) manter o input buffered.
     * Com game loop de 1000/60 ms (~16.67 ms por frame, 60 FPS):
     * - 18 frames = 300 ms
     */
    private static final int INPUT_BUFFER_FRAMES = 18;
    
    /**
     * AUTO-ALINHAMENTO:
     * Quando true, o Pacman se alinha automaticamente ao grid ao fazer curvas.
     */
    private static final boolean AUTO_ALIGN_ON_TURN = true;
    
    /**
     * Última direção pressionada pelo jogador.
     */
    private Direction bufferedDirection = Direction.NONE; // ' ' significa sem direção buffered
    
    /**
     * Contador de frames restantes para tentar o input buffered.
     * Decrementa a cada frame, quando chega a 0 o buffer expira.
     */
    private int bufferFramesRemaining = 0;
    
    // Dimensoes do tabuleiro
    private static final int ROW_COUNT = 21;
    private static final int COLUMN_COUNT = 19;
    private static final int TILE_SIZE = 32;
    private static final int BOARD_WIDTH = COLUMN_COUNT * TILE_SIZE;
    private static final int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;
    
    // FPS e velocidade
    private static final int FPS = 60;
    private static final int FRAME_TIME = 1000 / FPS;
    private static final int AI_UPDATE_INTERVAL = 4; // Atualizar IA a cada 4 frames
    
    // Mapa do labirinto
    private final String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };
    
    // Estruturas de dados do jogo
    private class Block {
        int x, y, width, height;
        Image image;
        int startX, startY;
        Direction direction = Direction.NONE;
        int velocityX = 0, velocityY = 0;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(Direction dir, int[][] walls) {
            Direction prevDirection = this.direction;
            this.direction = dir;
            updateVelocity();
            
            int newX = this.x + this.velocityX;
            int newY = this.y + this.velocityY;
            
            if (wouldCollide(newX, newY, walls)) {
                this.direction = prevDirection;
                updateVelocity();
            } else {
                this.x = newX;
                this.y = newY;
            }
        }

        void updateVelocity() {
            this.velocityX = direction.dx * (TILE_SIZE / 4);
            this.velocityY = direction.dy * (TILE_SIZE / 4);
        }

        boolean wouldCollide(int newX, int newY, int[][] walls) {
            if (newX < 0 || newX + width > BOARD_WIDTH || 
                newY < 0 || newY + height > BOARD_HEIGHT) {
                return true;
            }
            
            int[][] corners = {
                {newX, newY},
                {newX + width - 1, newY},
                {newX, newY + height - 1},
                {newX + width - 1, newY + height - 1}
            };
            
            for (int[] corner : corners) {
                int cX = corner[0] / TILE_SIZE;
                int cY = corner[1] / TILE_SIZE;
                if (cY >= 0 && cY < walls.length && cX >= 0 && cX < walls[0].length) {
                    if (walls[cY][cX] == 1) return true;
                }
            }
            return false;
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
            this.direction = Direction.NONE;
            this.velocityX = 0;
            this.velocityY = 0;
        }
    }
    private int boardWidth = COLUMN_COUNT * TILE_SIZE;

    // Imagens
    private Image wallImage;
    private Image pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;
    
    // Entidades do jogo
    private Block pacman;
    private List<Ghost> ghosts;
    private HashSet<Block> wallBlocks;
    private HashSet<Block> foods;
    private int[][] wallGrid; // Grid de paredes para deteccao de colisao rapida
    
    // Sistema de grafos e pathfinding
    private Graph graph;
    private PathfindingManager pathfindingManager;
    
    // Estado do jogo
    private Timer gameLoop;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private int frameCount = 0;
    private int level;


    public PacMan(int level) {
        this.level = level;
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        loadImages();
        initializeGame();
        
        gameLoop = new Timer(FRAME_TIME, this);
        gameLoop.start();
    }

    public PacMan() {
        this.level = 4;
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        loadImages();
        initializeGame();
        
        gameLoop = new Timer(FRAME_TIME, this);
        gameLoop.start();
    }

    /**
     * Carrega todas as imagens do jogo.
     */
    private void loadImages() {
        try {
            wallImage = new ImageIcon(getClass().getResource("/Images/wall.png")).getImage();
            pacmanUpImage = new ImageIcon(getClass().getResource("/Images/pacmanUp.png")).getImage();
            pacmanDownImage = new ImageIcon(getClass().getResource("/Images/pacmanDown.png")).getImage();
            pacmanLeftImage = new ImageIcon(getClass().getResource("/Images/pacmanLeft.png")).getImage();
            pacmanRightImage = new ImageIcon(getClass().getResource("/Images/pacmanRight.png")).getImage();
        } catch (Exception e) {
            System.err.println("ERRO: Nao foi possivel carregar as imagens!");
            System.err.println("Verifique se a pasta 'Images' existe e contem todos os arquivos necessarios.");
            e.printStackTrace();
        }
    }

    /**
     * Inicializa todos os componentes do jogo.
     */
    private void initializeGame() {
        // Construir o grafo a partir do mapa
        graph = new Graph(TILE_SIZE);
        graph.buildFromTileMap(tileMap);
        pathfindingManager = new PathfindingManager(graph);
        
        System.out.println("=== PACMAN FASE 1 - INICIALIZADO ===");
        System.out.println("Grafo: " + graph);
        
        loadMap();
    }

    /**
     * Carrega o mapa e cria todas as entidades.
     */
    private void loadMap() {
        wallBlocks = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new ArrayList<>();
        wallGrid = new int[ROW_COUNT][COLUMN_COUNT];
        
        // Criar imagens dos fantasmas (simples quadrados coloridos se nao houver imagens)
        Image redGhostImg = createGhostImage(Color.RED);
        Image pinkGhostImg = createGhostImage(Color.PINK);
        Image cyanGhostImg = createGhostImage(Color.CYAN);
        Image orangeGhostImg = createGhostImage(Color.ORANGE);

        for (int r = 0; r < ROW_COUNT; r++) {
            for (int c = 0; c < COLUMN_COUNT; c++) {
                String row = tileMap[r];
                char tileChar = row.charAt(c);
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;

                switch (tileChar) {
                    case 'X': // Parede
                        Block wall = new Block(wallImage, x, y, TILE_SIZE, TILE_SIZE);
                        wallBlocks.add(wall);
                        wallGrid[r][c] = 1;
                        break;
                        
                    case 'r': // Fantasma vermelho (Blinky)
                        ghosts.add(new Blinky(redGhostImg, x, y, TILE_SIZE, TILE_SIZE, 
                                             TILE_SIZE, pathfindingManager, level));
                        break;
                        
                    case 'p': // Fantasma rosa (Pinky)
                        ghosts.add(new Pinky(pinkGhostImg, x, y, TILE_SIZE, TILE_SIZE, 
                                            TILE_SIZE, pathfindingManager, level));
                        break;
                        
                    case 'b': // Fantasma azul (Inky)
                        ghosts.add(new Inky(cyanGhostImg, x, y, TILE_SIZE, TILE_SIZE, 
                                           TILE_SIZE, pathfindingManager, level));
                        break;
                        
                    case 'o': // Fantasma laranja (Clyde)
                        ghosts.add(new Clyde(orangeGhostImg, x, y, TILE_SIZE, TILE_SIZE, 
                                            TILE_SIZE, pathfindingManager, level));
                        break;
                        
                    case 'P': // Pacman
                        pacman = new Block(pacmanRightImage, x, y, TILE_SIZE, TILE_SIZE);
                        break;
                        
                    case ' ': // Comida
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                        break;
                        
                    case 'O': // Espaco vazio (sem comida)
                        break;
                }
            }
        }
        
        System.out.println("Mapa carregado: " + ghosts.size() + " fantasmas, " + 
                          foods.size() + " comidas");
    }

    /**
     * Cria uma imagem simples de fantasma (fallback se nao houver imagens).
     */
    private Image createGhostImage(Color color) {
        try {
            // Tentar carregar a imagem real primeiro
            String imageName = "";
            if (color == Color.RED) imageName = "/Images/redGhost.png";
            else if (color == Color.PINK) imageName = "/Images/pinkGhost.png";
            else if (color == Color.CYAN) imageName = "/Images/blueGhost.png";
            else if (color == Color.ORANGE) imageName = "/Images/orangeGhost.png";
            
            return new ImageIcon(getClass().getResource(imageName)).getImage();
        } catch (Exception e) {
            // Se falhar, criar uma imagem simples
            return null;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Desenha todos os elementos do jogo.
     */
    private void draw(Graphics g) {
        // Desenhar paredes
        for (Block wall : wallBlocks) {
            if (wall.image != null) {
                g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
            } else {
                g.setColor(Color.BLUE);
                g.fillRect(wall.x, wall.y, wall.width, wall.height);
            }
        }
        
        // Desenhar comidas
        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        
        // Desenhar fantasmas
        for (Ghost ghost : ghosts) {
            if (ghost.getImage() != null) {
                ghost.draw(g);
            } else {
                // Fallback: desenhar quadrado colorido
                g.setColor(ghost.getColor());
                g.fillRect(ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight());
            }
        }
        
        // Desenhar Pacman
        if (pacman.image != null) {
            g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(pacman.x, pacman.y, pacman.width, pacman.height);
        }
        
        // Desenhar HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, TILE_SIZE / 2, TILE_SIZE / 2);
        } else {
            g.drawString("x" + lives + " Score: " + score, TILE_SIZE / 2, TILE_SIZE / 2);
        }
    }

    /**
     * Atualiza o estado do jogo (chamado 60 vezes por segundo).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            frameCount++;
            repaint();
        }
    }

    // ============================================================================
    // SISTEMA DE CONTROLE FLUIDO - MÉTODOS PRINCIPAIS
    // ============================================================================
    
    /**
     * Verifica se o Pacman está alinhado com o grid em um eixo específico.
     * 
     * @param axis 'x' para horizontal, 'y' para vertical
     * @return true se está alinhado (dentro da tolerância)
     */
    private boolean isAlignedWithGrid(char axis) {
        if (axis == 'x') {
            // Calcula a posição ideal no grid
            int idealX = (pacman.x / TILE_SIZE) * TILE_SIZE;
            int nextIdealX = idealX + TILE_SIZE;
            
            // Verifica se está dentro da tolerância de alguma posição ideal
            return Math.abs(pacman.x - idealX) <= ALIGNMENT_TOLERANCE ||
                   Math.abs(pacman.x - nextIdealX) <= ALIGNMENT_TOLERANCE;
        } else if (axis == 'y') {
            int idealY = (pacman.y / TILE_SIZE) * TILE_SIZE;
            int nextIdealY = idealY + TILE_SIZE;
            
            return Math.abs(pacman.y - idealY) <= ALIGNMENT_TOLERANCE ||
                   Math.abs(pacman.y - nextIdealY) <= ALIGNMENT_TOLERANCE;
        }
        return false;
    }
    
    /**
     * AUTO-ALINHAMENTO: Alinha o Pacman ao grid no eixo especificado.
     * Isto é o que cria o efeito "magnético" de curvas suaves.
     * 
     * @param axis 'x' para horizontal, 'y' para vertical
     */
    private void alignToGrid(char axis) {
        if (!AUTO_ALIGN_ON_TURN) return;
        
        if (axis == 'x') {
            // Encontra a posição ideal mais próxima
            int idealX = Math.round((float) pacman.x / TILE_SIZE) * TILE_SIZE;
            pacman.x = idealX;
        } else if (axis == 'y') {
            int idealY = Math.round((float) pacman.y / TILE_SIZE) * TILE_SIZE;
            pacman.y = idealY;
        }
    }
    
    /**
     * Testa se uma direção é válida (não colide com paredes).
     * Cria um Block temporário para testar a colisão.
     * 
     * @param testDirection direção a testar ('U', 'D', 'L', 'R')
     * @return true se a direção é válida (sem colisão)
     */
    private boolean canMoveInDirection(Direction testDirection) {
        // Cria um Block temporário para teste
        Block testBlock = new Block(null, pacman.x, pacman.y, pacman.width, pacman.height);
        testBlock.direction = testDirection;
        testBlock.updateVelocity();
        
        // Simula o movimento
        testBlock.x += testBlock.velocityX;
        testBlock.y += testBlock.velocityY;
        
        // Verifica colisão com paredes
        for (Block wall : wallBlocks) {
            if (collision(testBlock, wall)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Verifica se mudar para uma nova direção é uma "curva" (mudança de eixo).
     * 
     * @param currentDir direção atual
     * @param newDir nova direção desejada
     * @return true se é uma curva (horizontal → vertical ou vice-versa)
     */
    private boolean isTurn(Direction currentDir, Direction newDir) {
        boolean currentIsVertical = (currentDir.getCode() == 'U' || currentDir.getCode() == 'D');
        boolean newIsVertical = (newDir.getCode() == 'U' || newDir.getCode() == 'D');
        
        return currentIsVertical != newIsVertical;
    }
    
    /**
     * CORNER CUTTING: Tenta aplicar a direção buffered.
     * Este método é chamado a cada frame para verificar se a direção
     * guardada se tornou válida.
     */
    private void tryApplyBufferedDirection() {
        // Se não há direção buffered, retorna
        if (bufferedDirection == Direction.NONE || bufferFramesRemaining <= 0) {
            return;
        }
        
        // Decrementa o contador de frames
        bufferFramesRemaining--;
        
        // Verifica se a direção buffered agora é válida
        boolean isValidMove = canMoveInDirection(bufferedDirection);
        
        if (isValidMove) {
            // Verifica se é uma curva (mudança de eixo)
            boolean isCurve = isTurn(pacman.direction, bufferedDirection);
            
            if (isCurve) {
                // Para curvas, verifica alinhamento com tolerância
                char axisToCheck = (bufferedDirection == Direction.LEFT || bufferedDirection == Direction.RIGHT) ? 'y' : 'x';
                
                if (isAlignedWithGrid(axisToCheck)) {
                    // AUTO-ALINHAMENTO: Alinha ao grid antes de virar
                    alignToGrid(axisToCheck);
                    
                    // Aplica a nova direção
                    pacman.direction = bufferedDirection;
                    pacman.updateVelocity();
                    
                    // Atualiza a imagem do Pacman
                    updatePacmanImage(bufferedDirection);
                    
                    // Limpa o buffer
                    bufferedDirection = Direction.NONE;
                    bufferFramesRemaining = 0;
                }
            } else {
                // Para reversões (180°), aplica imediatamente
                pacman.direction = bufferedDirection;
                pacman.updateVelocity();
                updatePacmanImage(bufferedDirection);
                
                bufferedDirection = Direction.NONE;
                bufferFramesRemaining = 0;
            }
        }
    }
    
    /**
     * Atualiza a imagem do Pacman baseada na direção.
     * 
     * @param direction direção ('U', 'D', 'L', 'R')
     */
    private void updatePacmanImage(Direction direction) {
        if (direction == Direction.UP) {
            pacman.image = pacmanUpImage;
        } else if (direction == Direction.DOWN) {
            pacman.image = pacmanDownImage;
        } else if (direction == Direction.LEFT) {
            pacman.image = pacmanLeftImage;
        } else if (direction == Direction.RIGHT) {
            pacman.image = pacmanRightImage;
        }
    }
    

    /**
     * Movimenta Pacman, fantasmas e detecta colisoes.
     */
    private void move() {
        // Tenta aplicar direção buffered a cada frame
        tryApplyBufferedDirection();

        // Mover Pacman
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
        
        // Teletransporte horizontal quando Pacman sai pelas bordas
        if (pacman.x + pacman.width < 0) {
            pacman.x = boardWidth;
        } else if (pacman.x > boardWidth) {
            pacman.x = -pacman.width;
        }


        // Verificar colisao com paredes
        for (Block wall : wallBlocks) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
        
        // Atualizar fantasmas (IA a cada AI_UPDATE_INTERVAL frames)
        boolean updateAI = (frameCount % AI_UPDATE_INTERVAL == 0);
        
        for (Ghost ghost : ghosts) {
            if (updateAI) {
                ghost.update(pacman.x, pacman.y, pacman.direction, 
                           wallGrid, BOARD_WIDTH, BOARD_HEIGHT);
            } else {
                // Apenas mover, sem atualizar direcao
                ghost.move(wallGrid, BOARD_WIDTH, BOARD_HEIGHT);
            }
            
            // Verificar colisao com Pacman
            if (ghost.collidesWith(pacman.x, pacman.y, pacman.width, pacman.height)) {
                lives--;
                if (lives == 0) {
                    gameOver = true;
                    handleGameOver();
                    return;
                }
                resetPositions();
            }
        }
        
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);
        
        // Se comeu todas as comidas, recarregar o mapa
        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    /**
     * Verifica colisao entre dois blocos.
     */
    private boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    /**
     * Reseta as posicoes de Pacman e fantasmas.
     */
    private void resetPositions() {
        pacman.reset();
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }

    /**
     * Trata o fim do jogo.
     */
    private void handleGameOver() {
        gameLoop.stop();
        
        String name = JOptionPane.showInputDialog(this, 
            "GAME OVER!\nDigite seu nome (ate 3 letras):");
        
        if (name != null && !name.trim().isEmpty()) {
            name = name.trim().toUpperCase();
            if (name.length() > 3) name = name.substring(0, 3);
            RankingManager.saveScore(name, score);
        }
        
        showRanking();
    }

    /**
     * Exibe o ranking e pergunta se quer jogar novamente.
     */
    private void showRanking() {
        List<ScoreEntry> ranking = RankingManager.loadRanking();
        StringBuilder sb = new StringBuilder("=== RANKING ===\n\n");
        int pos = 1;
        for (ScoreEntry entry : ranking) {
            sb.append(pos++).append(". ")
              .append(entry.name).append(" - ")
              .append(entry.score).append("\n");
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            sb.toString() + "\nDeseja jogar novamente?",
            "Fim de Jogo",
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    /**
     * Reinicia o jogo.
     */
    private void restartGame() {
        loadMap();
        resetPositions();
        lives = 3;
        score = 0;
        gameOver = false;
        frameCount = 0;
        gameLoop.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            return; // Não processa input durante game over
        }
        
        Direction newDirection = Direction.NONE;
        
        // Mapeia tecla para direção
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            newDirection = Direction.UP;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            newDirection = Direction.DOWN;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            newDirection = Direction.LEFT;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            newDirection = Direction.RIGHT;
        }
        
        // Se uma tecla de direção foi pressionada
        if (newDirection != Direction.NONE) {
            // Tenta aplicar imediatamente
            if (canMoveInDirection(newDirection)) {
                boolean isCurve = isTurn(pacman.direction, newDirection);
                
                if (isCurve) {
                    // É uma curva - verifica alinhamento
                    char axisToCheck = (newDirection == Direction.LEFT || newDirection == Direction.RIGHT) ? 'y' : 'x';
                    
                    if (isAlignedWithGrid(axisToCheck)) {
                        // Está alinhado - aplica imediatamente
                        alignToGrid(axisToCheck);
                        pacman.direction = newDirection;
                        pacman.updateVelocity();
                        updatePacmanImage(newDirection);
                        
                        // Limpa buffer (já aplicamos)
                        bufferedDirection = Direction.NONE;
                        bufferFramesRemaining = 0;
                    } else {
                        // Não está alinhado - guarda no buffer
                        bufferedDirection = newDirection;
                        bufferFramesRemaining = INPUT_BUFFER_FRAMES;
                    }
                } else {
                    // Não é curva (reversão ou mesma direção) - aplica imediatamente
                    pacman.direction = newDirection;
                    pacman.updateVelocity();
                    updatePacmanImage(newDirection);
                    
                    bufferedDirection = Direction.NONE;
                    bufferFramesRemaining = 0;
                }
            } else {
                // Direção inválida agora - guarda no buffer para tentar depois
                bufferedDirection = newDirection;
                bufferFramesRemaining = INPUT_BUFFER_FRAMES;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
