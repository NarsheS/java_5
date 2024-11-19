package cadastroclientv2;

import javax.swing.*;

public class SaidaFrame extends JDialog {
    public JTextArea texto;

    public SaidaFrame(JFrame parent) {
        super(parent, "Saída", true);
        texto = new JTextArea();
        texto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(texto);
        add(scrollPane);
        setBounds(100, 100, 400, 300);
        setModal(false);
    }
}
