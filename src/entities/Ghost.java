
package entities;

import managers.PathfindingManager;
import models.Direction;
import java.awt.Image;

public abstract class Ghost {
    protected int x;
    protected int y;
    protected int startX;
    protected int startY;
    protected Image image;
    protected Direction direction;
    protected int velocityX;
    protected int velocityY;
    protected PathfindingManager pathfindingManager;
    protected int tileSize;
    
    /**
     * Construtor do fantasma.
     * 
     * param image imagem do fantasma
     * param x posição inicial x em pixels
     * param y posição inicial y em pixels
     * param tileSize tamanho do tile
     * param pathfindingManager gerenciador de pathfinding
     */
    public Ghost(Image image, int x, int y, int tileSize, PathfindingManager pathfindingManager) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.tileSize = tileSize;
        this.pathfindingManager = pathfindingManager;
        this.direction = Direction.UP;
        updateVelocity();
    }
    
    /**
     * Método abstrato que define como o fantasma escolhe sua direção.
     * Cada tipo de fantasma implementa sua própria estratégia.
     * 
     * param pacmanX posição x do Pacman
     * param pacmanY posição y do Pacman
     * param pacmanDirection direção atual do Pacman
     * return direção escolhida
     */
    public abstract Direction chooseDirection(int pacmanX, int pacmanY, Direction pacmanDirection);
    
    /**
     * Retorna o nome do algoritmo usado por este fantasma.
     * Usado para fins educacionais e debugging.
     * 
     * return nome do algoritmo
     */
    public abstract String getAlgorithmName();
    
    /**
     * Atualiza a velocidade baseada na direção atual.
     */
    protected void updateVelocity() {
        velocityX = direction.getDx() * (tileSize / 4);
        velocityY = direction.getDy() * (tileSize / 4);
    }
    
    /**
     * Move o fantasma aplicando a velocidade.
     */
    public void move() {
        x += velocityX;
        y += velocityY;
    }
    
    /**
     * Define uma nova direção e atualiza a velocidade.
     * 
     * param newDirection nova direção
     */
    public void setDirection(Direction newDirection) {
        if (newDirection != null) {
            this.direction = newDirection;
            updateVelocity();
        }
    }
    
    /**
     * Reseta o fantasma para a posição inicial.
     */
    public void reset() {
        this.x = startX;
        this.y = startY;
        this.direction = Direction.UP;
        updateVelocity();
    }
    
    /**
     * Verifica se o fantasma está alinhado com o grid.
     * Importante para fazer decisões de pathfinding apenas em posições válidas.
     * 
     * return true se está alinhado
     */
    public boolean isAlignedWithGrid() {
        return (x % tileSize == 0) && (y % tileSize == 0);
    }
    
    // Getters
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public Image getImage() {
        return image;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public int getVelocityX() {
        return velocityX;
    }
    
    public int getVelocityY() {
        return velocityY;
    }
    
    // Setters
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }
    
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
}
