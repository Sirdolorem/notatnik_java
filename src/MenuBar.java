import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class MenuBar {
    private JMenuBar menuBar;
    private Editor editor;
    private JFileChooser fileChooser;

    public MenuBar(Editor editor) {
        this.editor = editor;
        menuBar = new JMenuBar();
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Documents (*.txt)", "txt"));

        createFileMenu();
        createEditMenu();
        createSearchReplaceMenu();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> openFile());

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveFile());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
    }

    private void createEditMenu() {
        JMenu editMenu = new JMenu("Edit");

        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(e -> editor.undo());

        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(e -> editor.redo());

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        menuBar.add(editMenu);
    }

    private void createSearchReplaceMenu() {
        JMenu searchReplaceMenu = new JMenu("Search & Replace");

        JMenuItem searchItem = new JMenuItem("Search");
        searchItem.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog("Enter text to search:");
            if (searchText != null) {
                editor.search(searchText);
            }
        });

        JMenuItem replaceItem = new JMenuItem("Replace");
        replaceItem.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog("Enter text to replace:");
            String replaceText = JOptionPane.showInputDialog("Enter replacement text:");
            if (searchText != null && replaceText != null) {
                editor.replace(searchText, replaceText);
            }
        });

        searchReplaceMenu.add(searchItem);
        searchReplaceMenu.add(replaceItem);
        menuBar.add(searchReplaceMenu);
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                editor.getTextArea().read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                editor.getTextArea().write(writer);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
