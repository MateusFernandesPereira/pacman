
import entities.PacMan;
import javax.swing.JFrame;

/**
 * Classe principal para iniciar o jogo diretamente.
 */
public class App {
    public static void main(String[] args) {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pac Man - Fase 1: Grafos e IA");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
        
        System.out.println("=================================");
        System.out.println("   PACMAN - FASE 1: GRAFOS E IA");
        System.out.println("=================================");
        System.out.println("FPS: 60");
        System.out.println("Controles: Setas do teclado");
        System.out.println("Objetivo: Comer todas as bolinhas");
        System.out.println("=================================");
    }
}
