package entities;

import managers.PathfindingManager;
import models.Direction;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {
    
    /**
     * Classe interna para representar blocos (comida e paredes).
     * Mantida para compatibilidade com o código original.
     */
    class Block {
        int x, y, width, height;
        Image image;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    // Dimensões do tabuleiro
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    // Imagens
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    // Mapa do labirinto
    private String[] tileMap = {
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

    // Elementos do jogo
    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private List<Ghost> ghosts; // Nova lista de fantasmas inteligentes
    
    // Pacman
    private int pacmanX, pacmanY;
    private Image pacmanImage;
    private Direction pacmanDirection;
    private int pacmanVelocityX, pacmanVelocityY;

    // Gerenciador de pathfinding
    private PathfindingManager pathfindingManager;

    // Estado do jogo
    private Timer gameLoop;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private int updateCounter = 0;

    /**
     * Construtor do jogo Pacman.
     */
    public PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        loadImages();
        loadMap();
        
        // Inicia o loop do jogo (20 FPS)
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    /**
     * Carrega todas as imagens do jogo.
     */
    private void loadImages() {
        try {
            wallImage = new ImageIcon(getClass().getResource("/Images/wall.png")).getImage();
            blueGhostImage = new ImageIcon(getClass().getResource("/Images/blueGhost.png")).getImage();
            orangeGhostImage = new ImageIcon(getClass().getResource("/Images/orangeGhost.png")).getImage();
            pinkGhostImage = new ImageIcon(getClass().getResource("/Images/pinkGhost.png")).getImage();
            redGhostImage = new ImageIcon(getClass().getResource("/Images/redGhost.png")).getImage();

            pacmanUpImage = new ImageIcon(getClass().getResource("/Images/pacmanUp.png")).getImage();
            pacmanDownImage = new ImageIcon(getClass().getResource("/Images/pacmanDown.png")).getImage();
            pacmanLeftImage = new ImageIcon(getClass().getResource("/Images/pacmanLeft.png")).getImage();
            pacmanRightImage = new ImageIcon(getClass().getResource("/Images/pacmanRight.png")).getImage();
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagens: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega o mapa e inicializa os elementos do jogo.
     */
    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new ArrayList<>();

        // Inicializa o gerenciador de pathfinding
        pathfindingManager = new PathfindingManager(tileMap, rowCount, columnCount, tileSize);

        // Processa o mapa
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') {
                    // Parede
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'r') {
                    // Blinky - Fantasma Vermelho (Dijkstra)
                    Ghost blinky = new Blinky(redGhostImage, x, y, tileSize, pathfindingManager);
                    ghosts.add(blinky);
                } else if (tileMapChar == 'p') {
                    // Pinky - Fantasma Rosa (A*)
                    Ghost pinky = new Pinky(pinkGhostImage, x, y, tileSize, pathfindingManager);
                    ghosts.add(pinky);
                } else if (tileMapChar == 'b') {
                    // Inky - Fantasma Azul (DFS)
                    Ghost inky = new Inky(blueGhostImage, x, y, tileSize, pathfindingManager);
                    ghosts.add(inky);
                } else if (tileMapChar == 'o') {
                    // Clyde - Fantasma Laranja (BFS)
                    Ghost clyde = new Clyde(orangeGhostImage, x, y, tileSize, pathfindingManager);
                    ghosts.add(clyde);
                } else if (tileMapChar == 'P') {
                    // Pacman
                    pacmanX = x;
                    pacmanY = y;
                    pacmanImage = pacmanRightImage;
                    pacmanDirection = Direction.RIGHT;
                    pacmanVelocityX = 0;
                    pacmanVelocityY = 0;
                } else if (tileMapChar == ' ') {
                    // Comida
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
        
        System.out.println("=== Pacman Game Initialized (Phase 1) ===");
        System.out.println(pathfindingManager.getGraphStats());
        System.out.println("Ghosts created: " + ghosts.size());
        for (Ghost ghost : ghosts) {
            System.out.println("  - " + ghost.getClass().getSimpleName() + " using " + ghost.getAlgorithmName());
        }
    }

    /**
     * Desenha todos os elementos do jogo.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Renderiza o jogo.
     */
    public void draw(Graphics g) {
        // Desenha Pacman
        g.drawImage(pacmanImage, pacmanX, pacmanY, tileSize, tileSize, null);

        // Desenha fantasmas
        for (Ghost ghost : ghosts) {
            g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), tileSize, tileSize, null);
        }

        // Desenha paredes
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        // Desenha comida
        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        // Desenha HUD
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);
        } else {
            g.drawString("x" + lives + " Score: " + score, tileSize / 2, tileSize / 2);
        }
    }

    /**
     * Atualiza o estado do jogo (chamado pelo Timer).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        } else {
            gameLoop.stop();
            handleGameOver();
        }
    }

    /**
     * Move o Pacman e os fantasmas.
     */
    public void move() {
        // Move Pacman
        movePacman();

        // Move fantasmas com IA
        moveGhosts();

        // Verifica colisões
        checkCollisions();

        // Verifica vitória
        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    /**
     * Move o Pacman.
     */
    private void movePacman() {
        int newX = pacmanX + pacmanVelocityX;
        int newY = pacmanY + pacmanVelocityY;

        // Verifica colisão com paredes
        boolean collision = false;
        for (Block wall : walls) {
            if (checkCollision(newX, newY, tileSize, tileSize, wall.x, wall.y, wall.width, wall.height)) {
                collision = true;
                break;
            }
        }

        if (!collision) {
            pacmanX = newX;
            pacmanY = newY;
        }
    }

    /**
     * Move os fantasmas usando IA baseada em algoritmos de busca.
     */
    private void moveGhosts() {
        updateCounter++;

        for (Ghost ghost : ghosts) {
            // Atualiza direção dos fantasmas periodicamente (a cada 4 frames)
            if (updateCounter % 4 == 0 && ghost.isAlignedWithGrid()) {
                Direction newDirection = ghost.chooseDirection(pacmanX, pacmanY, pacmanDirection);
                if (newDirection != null) {
                    ghost.setDirection(newDirection);
                }
            }

            // Move o fantasma
            int newX = ghost.getX() + ghost.getVelocityX();
            int newY = ghost.getY() + ghost.getVelocityY();

            // Verifica colisão com paredes
            boolean collision = false;
            for (Block wall : walls) {
                if (checkCollision(newX, newY, tileSize, tileSize, wall.x, wall.y, wall.width, wall.height)) {
                    collision = true;
                    break;
                }
            }

            // Verifica limites do tabuleiro
            if (newX < 0 || newX + tileSize > boardWidth) {
                collision = true;
            }

            if (!collision) {
                ghost.setX(newX);
                ghost.setY(newY);
            } else {
                // Se colidiu, escolhe nova direção imediatamente
                Direction newDirection = ghost.chooseDirection(pacmanX, pacmanY, pacmanDirection);
                if (newDirection != null) {
                    ghost.setDirection(newDirection);
                }
            }
        }
    }

    /**
     * Verifica colisões entre elementos do jogo.
     */
    private void checkCollisions() {
        // Colisão Pacman com fantasmas
        for (Ghost ghost : ghosts) {
            if (checkCollision(pacmanX, pacmanY, tileSize, tileSize, 
                              ghost.getX(), ghost.getY(), tileSize, tileSize)) {
                lives--;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
        }

        // Colisão Pacman com comida
        Block foodEaten = null;
        for (Block food : foods) {
            if (checkCollision(pacmanX, pacmanY, tileSize, tileSize, 
                              food.x - 14, food.y - 14, tileSize, tileSize)) {
                foodEaten = food;
                score += 10;
                break;
            }
        }
        if (foodEaten != null) {
            foods.remove(foodEaten);
        }
    }

    /**
     * Verifica colisão entre dois retângulos.
     */
    private boolean checkCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    /**
     * Reseta as posições do Pacman e fantasmas.
     */
    public void resetPositions() {
        // Reseta Pacman
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                if (tileMap[r].charAt(c) == 'P') {
                    pacmanX = c * tileSize;
                    pacmanY = r * tileSize;
                    pacmanVelocityX = 0;
                    pacmanVelocityY = 0;
                    pacmanDirection = Direction.RIGHT;
                    pacmanImage = pacmanRightImage;
                }
            }
        }

        // Reseta fantasmas
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }

    /**
     * Trata o fim de jogo.
     */
    private void handleGameOver() {
        String name = JOptionPane.showInputDialog(this, "GAME OVER!\nDigite seu nome (até 3 letras):");
        if (name != null && !name.trim().isEmpty()) {
            name = name.trim().toUpperCase();
            if (name.length() > 3) name = name.substring(0, 3);
            RankingManager.saveScore(name, score);
        }

        List<ScoreEntry> ranking = RankingManager.loadRanking();
        StringBuilder sb = new StringBuilder("Ranking\n\n");
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
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        } else {
            System.exit(0);
        }
    }

    // Métodos do KeyListener

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
            return;
        }

        Direction newDirection = null;
        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            newDirection = Direction.UP;
            pacmanImage = pacmanUpImage;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            newDirection = Direction.DOWN;
            pacmanImage = pacmanDownImage;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            newDirection = Direction.LEFT;
            pacmanImage = pacmanLeftImage;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            newDirection = Direction.RIGHT;
            pacmanImage = pacmanRightImage;
        }

        if (newDirection != null) {
            pacmanDirection = newDirection;
            pacmanVelocityX = newDirection.getDx() * (tileSize / 4);
            pacmanVelocityY = newDirection.getDy() * (tileSize / 4);
        }
    }
}
