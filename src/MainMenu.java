import entities.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Pac-Man - Menu Inicial");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton startButton = new JButton("Iniciar Jogo");
        JButton rankingButton = new JButton("Ver Ranking");
        JButton exitButton = new JButton("Sair");

        panel.add(startButton);
        panel.add(rankingButton);
        panel.add(exitButton);

        add(panel);

        // Ações dos botões
        startButton.addActionListener(e -> {
            dispose(); // Fecha o menu
            iniciarJogo();
        });

        rankingButton.addActionListener(e -> {
            exibirRanking();
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    private void iniciarJogo() {
        JFrame frame = new JFrame("Pac-Man");
        PacMan pacman = new PacMan();
        frame.add(pacman);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void exibirRanking() {
        java.util.List<ScoreEntry> ranking = RankingManager.loadRanking();

        if (ranking.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum dado no ranking ainda!");
            return;
        }

        StringBuilder sb = new StringBuilder("🏆 Ranking 🏆\n\n");
        int pos = 1;
        for (ScoreEntry entry : ranking) {
            sb.append(pos++).append(". ")
            .append(entry.name).append(" - ")
            .append(entry.score).append("\n");
        }

        sb.append("\nDeseja apagar todos os dados do ranking?");

        int option = JOptionPane.showConfirmDialog(
            this,
            sb.toString(),
            "Ranking",
            JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            RankingManager.clearRanking();
            JOptionPane.showMessageDialog(this, "Ranking apagado com sucesso!");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }
}
