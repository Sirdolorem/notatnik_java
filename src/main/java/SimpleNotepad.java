import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleNotepad extends JFrame {
    private Editor editor;
    private MenuBar menuBar;

    public SimpleNotepad() {
        // Set up the frame
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        editor = new Editor();
        menuBar = new MenuBar(editor);

        // Add components to frame
        setJMenuBar(menuBar.getMenuBar());

        // Status Bar
        JLabel statusLabel = new JLabel(" Line: 1, Column: 1");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        editor.setStatusLabel(statusLabel);
        add(statusLabel, BorderLayout.SOUTH);

        add(editor.getTextAreaPanel(), BorderLayout.CENTER);

        // Handle closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (editor.checkUnsavedChanges()) {
                    dispose();
                    System.exit(0);
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            SimpleNotepad notepad = new SimpleNotepad();
            notepad.setVisible(true);
        });
    }
}
