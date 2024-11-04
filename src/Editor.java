import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor {
    private JTextArea textArea;
    private UndoManager undoManager;
    private JFileChooser fileChooser;

    public Editor() {
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);
        fileChooser = new JFileChooser();

        // Set up shortcuts for undo and redo
        setupUndoRedoShortcuts();
    }

    private void setupUndoRedoShortcuts() {
        // Bind Ctrl+Z to undo
        textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), "Undo");
        textArea.getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        // Bind Ctrl+Y to redo
        textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"), "Redo");
        textArea.getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
    }

    public JPanel getTextAreaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    public void search(String searchText) {
        textArea.getHighlighter().removeAllHighlights();
        Pattern pattern = Pattern.compile(Pattern.quote(searchText));
        Matcher matcher = pattern.matcher(textArea.getText());

        while (matcher.find()) {
            try {
                textArea.getHighlighter().addHighlight(matcher.start(), matcher.end(),
                        new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void replace(String searchText, String replaceText) {
        String content = textArea.getText();
        textArea.setText(content.replaceAll(Pattern.quote(searchText), Matcher.quoteReplacement(replaceText)));
    }

    public void openFile() {
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveFile() {
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
