package cadastroclientv2;

import java.io.ObjectInputStream;
import java.util.List;
import javax.swing.*;
import model.Produtos;

public class ThreadClient extends Thread {
    private ObjectInputStream in;
    private JTextArea textArea;

    public ThreadClient(ObjectInputStream in, JTextArea textArea) {
        this.in = in;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof String) {
                    String msg = (String) obj;
                    SwingUtilities.invokeLater(() -> textArea.append(msg + "\n"));
                } else if (obj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Produtos> produtos = (List<Produtos>) obj;
                    StringBuilder sb = new StringBuilder();
                    for (Produtos p : produtos) {
                        sb.append(p.getNome()).append(" - ").append(p.getEstoque()).append("\n");
                    }
                    SwingUtilities.invokeLater(() -> textArea.append(sb.toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
