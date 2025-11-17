package entities;

import managers.PathfindingManager;
import models.Direction;

import java.awt.*;

/**
 * Classe abstrata base para todos os fantasmas.
 * 
 * Design Pattern: Template Method
 * - O metodo update() define a estrutura geral de atualizacao
 * - O metodo abstrato chooseDirection() eh implementado por cada fantasma
 */
public abstract class Ghost {
    protected int x;
    protected int y;
    protected int startX;
    protected int startY;
    protected int width;
    protected int height;
    protected Image image;
    protected Direction direction;
    protected int velocityX;
    protected int velocityY;
    protected int tileSize;
    protected PathfindingManager pathfindingManager;
    protected String name;
    protected Color color;

    /**
     * Construtor base para fantasmas.
     */
    public Ghost(Image image, int x, int y, int width, int height, int tileSize, 
                 PathfindingManager pathfindingManager, String name, Color color) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        this.pathfindingManager = pathfindingManager;
        this.direction = Direction.NONE;
        this.velocityX = 0;
        this.velocityY = 0;
        this.name = name;
        this.color = color;
    }

    /**
     * Metodo template que define o fluxo de atualizacao do fantasma.
     * Chama o metodo abstrato chooseDirection() que cada fantasma implementa.
     */
    public void update(int pacmanX, int pacmanY, Direction pacmanDirection, 
                      int[][] walls, int boardWidth, int boardHeight) {
        // Apenas atualizar direcao quando estiver alinhado com o grid
        if (isAlignedWithGrid()) {
            Direction newDirection = chooseDirection(pacmanX, pacmanY, pacmanDirection);
            if (newDirection != Direction.NONE) {
                setDirection(newDirection);
            }
        }

        // Mover o fantasma
        move(walls, boardWidth, boardHeight);
    }

    /**
     * Metodo abstrato que cada fantasma deve implementar
     * para definir sua estrategia de escolha de direcao.
     */
    protected abstract Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection);

    /**
     * Define a direcao do fantasma e atualiza a velocidade.
     */
    protected void setDirection(Direction newDirection) {
        this.direction = newDirection;
        updateVelocity();
    }

    /**
     * Atualiza a velocidade baseado na direcao atual.
     */
    protected void updateVelocity() {
        velocityX = direction.dx * (tileSize / 4);
        velocityY = direction.dy * (tileSize / 4);
    }

    /**
     * Move o fantasma na direcao atual.
     */
    protected void move(int[][] walls, int boardWidth, int boardHeight) {
        int newX = x + velocityX;
        int newY = y + velocityY;

        // Verificar colisao com paredes
        if (!wouldCollideWithWall(newX, newY, walls, boardWidth, boardHeight)) {
            x = newX;
            y = newY;
        }
    }

    /**
     * Verifica se o fantasma colidira com uma parede na nova posicao.
     */
    protected boolean wouldCollideWithWall(int newX, int newY, int[][] walls, 
                                          int boardWidth, int boardHeight) {
        // Verificar se esta fora dos limites
        if (newX < 0 || newX + width > boardWidth || newY < 0 || newY + height > boardHeight) {
            return true;
        }

        // Verificar colisao com paredes do grid
        int gridX = newX / tileSize;
        int gridY = newY / tileSize;

        // Verificar os 4 cantos da hitbox do fantasma
        int[][] corners = {
            {newX, newY},                           // Top-left
            {newX + width - 1, newY},               // Top-right
            {newX, newY + height - 1},              // Bottom-left
            {newX + width - 1, newY + height - 1}   // Bottom-right
        };

        for (int[] corner : corners) {
            int cX = corner[0] / tileSize;
            int cY = corner[1] / tileSize;
            
            if (cY >= 0 && cY < walls.length && cX >= 0 && cX < walls[0].length) {
                if (walls[cY][cX] == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Verifica se o fantasma esta alinhado com o grid.
     * Usado para decidir quando atualizar a direcao.
     */
    protected boolean isAlignedWithGrid() {
        return (x % tileSize == 0) && (y % tileSize == 0);
    }

    /**
     * Reseta o fantasma para sua posicao inicial.
     */
    public void reset() {
        this.x = startX;
        this.y = startY;
        this.direction = Direction.NONE;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
     * Desenha o fantasma na tela.
     */
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
        
        // Debug: desenhar nome e posicao do fantasma
        // g.setColor(color);
        // g.setFont(new Font("Arial", Font.PLAIN, 10));
        // g.drawString(name, x, y - 5);
    }

    /**
     * Verifica colisao com o Pacman.
     */
    public boolean collidesWith(int pacmanX, int pacmanY, int pacmanWidth, int pacmanHeight) {
        return x < pacmanX + pacmanWidth &&
               x + width > pacmanX &&
               y < pacmanY + pacmanHeight &&
               y + height > pacmanY;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Image getImage() { return image; }
    public String getName() { return name; }
    public Color getColor() { return color; }
}
