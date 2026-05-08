import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MenuBar {
    private JMenuBar menuBar;
    private Editor editor;

    public MenuBar(Editor editor) {
        this.editor = editor;
        menuBar = new JMenuBar();

        createFileMenu();
        createEditMenu();
        createSearchReplaceMenu();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem newItem = new JMenuItem("New");
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newItem.addActionListener(e -> {
            if (editor.checkUnsavedChanges()) {
                editor.getTextArea().setText("");
            }
        });

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openItem.addActionListener(e -> editor.openFile());

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveItem.addActionListener(e -> editor.saveFile());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exitItem.addActionListener(e -> {
            if (editor.checkUnsavedChanges()) {
                System.exit(0);
            }
        });

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
    }

    private void createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undoItem.addActionListener(e -> editor.undo());

        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        redoItem.addActionListener(e -> editor.redo());

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        menuBar.add(editMenu);
    }

    private void createSearchReplaceMenu() {
        JMenu searchReplaceMenu = new JMenu("Search & Replace");
        searchReplaceMenu.setMnemonic(KeyEvent.VK_S);

        JMenuItem searchItem = new JMenuItem("Search");
        searchItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        searchItem.addActionListener(e -> {
            String searchText = JOptionPane.showInputDialog("Enter text to search:");
            if (searchText != null) {
                editor.search(searchText);
            }
        });

        JMenuItem replaceItem = new JMenuItem("Replace");
        replaceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
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
}
