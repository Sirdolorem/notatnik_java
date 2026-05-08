import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor {
    private JTextArea textArea;
    private UndoManager undoManager;
    private JFileChooser fileChooser;
    private boolean isModified = false;
    private JLabel statusLabel;

    public Editor() {
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 18)); // Better for code/text
        textArea.setMargin(new Insets(15, 15, 15, 15)); // More padding
        
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);
        
        // Track changes
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { onDocumentChanged(); }
            public void removeUpdate(DocumentEvent e) { onDocumentChanged(); }
            public void changedUpdate(DocumentEvent e) { onDocumentChanged(); }
        });

        // Track cursor position
        textArea.addCaretListener(e -> updateStatus());

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Documents (*.txt)", "txt"));

        // Set up shortcuts for undo and redo
        setupUndoRedoShortcuts();
    }

    private void setupUndoRedoShortcuts() {
        // Bind Ctrl+Z to undo
        int menuShortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuShortcutMask), "Undo");
        textArea.getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        // Bind Ctrl+Y to redo
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, menuShortcutMask), "Redo");
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
        if (searchText == null || searchText.isEmpty()) return; // Guard: skip empty search
        Pattern pattern = Pattern.compile(Pattern.quote(searchText));
        Matcher matcher = pattern.matcher(textArea.getText());

        while (matcher.find()) {
            try {
                textArea.getHighlighter().addHighlight(matcher.start(), matcher.end(),
                        new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void replace(String searchText, String replaceText) {
        if (searchText == null || searchText.isEmpty()) return; // Guard: skip empty search
        // Use document-level replacement to preserve undo history
        String content = textArea.getText();
        String newContent = content.replaceAll(Pattern.quote(searchText), Matcher.quoteReplacement(replaceText));
        try {
            ((AbstractDocument) textArea.getDocument()).replace(0, textArea.getDocument().getLength(), newContent, null);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public void newFile() {
        if (!checkUnsavedChanges()) return;
        try {
            // Replace content silently then reset modified flag
            ((AbstractDocument) textArea.getDocument()).replace(0, textArea.getDocument().getLength(), "", null);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        undoManager.discardAllEdits(); // Clear undo history for fresh document
        setModified(false);
    }

    public void openFile() {
        if (!checkUnsavedChanges()) return;

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
                setModified(false);
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
                setModified(false);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        updateStatus();
    }

    private void updateStatus() {
        if (statusLabel != null) {
            int pos = textArea.getCaretPosition();
            try {
                int line = textArea.getLineOfOffset(pos) + 1;
                int col = pos - textArea.getLineStartOffset(line - 1) + 1;
                String mod = isModified ? " *Modified*" : "";
                statusLabel.setText(" Line: " + line + ", Column: " + col + mod);
            } catch (Exception e) {
                statusLabel.setText(" Line: 1, Column: 1");
            }
        }
    }

    // Called by DocumentListener - package-visible so the inner class can access it unambiguously
    void onDocumentChanged() {
        setModified(true);
    }

    private void setModified(boolean modified) {
        this.isModified = modified;
        updateStatus();
    }

    public boolean checkUnsavedChanges() {
        if (isModified) {
            int option = JOptionPane.showConfirmDialog(null, 
                "You have unsaved changes. Do you want to save them?", 
                "Unsaved Changes", 
                JOptionPane.YES_NO_CANCEL_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
                return !isModified; // Return true if saved successfully
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }
}
