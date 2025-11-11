
import entities.PacMan;
import javax.swing.JFrame;

/**
 * Classe principal do jogo Pacman.
 * Inicia o jogo diretamente (sem menu).
 * 
 * Para iniciar com menu, use MainMenu.java
 * 
 */
public class App {
    public static void main(String[] args) {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pac Man - Phase 1 (Graph Algorithms)");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
        
        System.out.println("\n=== PACMAN - FASE 1 ===");
        System.out.println("Projeto de Estruturas de Dados II");
        System.out.println("UNESP - Bacharelado em Sistemas de Informação");
        System.out.println("\nFantasmas Inteligentes:");
        System.out.println("   Blinky (Vermelho) - Dijkstra");
        System.out.println("   Pinky (Rosa) - A* (A-Star)");
        System.out.println("   Inky (Azul) - DFS");
        System.out.println("   Clyde (Laranja) - BFS");
        System.out.println("\nBom jogo!\n");
    }
}
