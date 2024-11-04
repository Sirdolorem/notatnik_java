import javax.swing.*;
import java.awt.*;

public class SimpleNotepad extends JFrame {
    private Editor editor;
    private MenuBar menuBar;

    public SimpleNotepad() {
        // Set up the frame
        setTitle("Notepad");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        editor = new Editor();
        menuBar = new MenuBar(editor);

        // Add components to frame
        setJMenuBar(menuBar.getMenuBar());

        add(editor.getTextAreaPanel(), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleNotepad notepad = new SimpleNotepad();
            notepad.setVisible(true);
        });
    }
}
