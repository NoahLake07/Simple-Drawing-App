import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DrawingApp extends JFrame {
    private List<Point> currentLine = new ArrayList<>();
    private List<List<Point>> lines = new ArrayList<>();
    private JPanel drawingPanel;
    private Timer timer;

    private JMenuBar menuBar;

    private int strokeThickness = 5;

    public DrawingApp() {
        setTitle("Continuous Drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(strokeThickness));

                for (List<Point> line : lines) {
                    drawLine(g2d, line);
                }
                drawLine(g2d, currentLine);
            }

            private void drawLine(Graphics g, List<Point> line) {
                if (line.size() < 2) return;
                for (int i = 0; i < line.size() - 1; i++) {
                    Point p1 = line.get(i);
                    Point p2 = line.get(i + 1);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        };

        drawingPanel.setPreferredSize(new Dimension(600, 400));

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentLine.clear();
                currentLine.add(e.getPoint());
                startDrawing();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lines.add(new ArrayList<>(currentLine));
                currentLine.clear();
                repaint();
            }
        });

        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentLine.add(e.getPoint());
                repaint();
            }
        });

        add(drawingPanel);
        pack();
        setLocationRelativeTo(null);

        menuBar = new JMenuBar();
        menuBar.add(new JMenu("File"));
        this.setJMenuBar(this.menuBar);

        setState(JFrame.MAXIMIZED_BOTH);
    }

    private void startDrawing() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, 10); // 10ms interval
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrawingApp app = new DrawingApp();
            app.setVisible(true);
        });
    }
}
