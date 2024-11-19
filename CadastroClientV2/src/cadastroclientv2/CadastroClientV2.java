package cadastroclientv2;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import model.Produtos;

public class CadastroClientV2 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4321);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            // Login inicial
            out.writeObject("op1");
            out.writeObject("op1");

            String loginResponse = (String) in.readObject();
            System.out.println(loginResponse);

            boolean running = true;

            while (running) {
                // Menu de opções
                System.out.println("\nDigite um comando:");
                System.out.println("L - Listar produtos");
                System.out.println("E - Entrada de produtos");
                System.out.println("S - Saida de produtos");
                System.out.println("X - Sair");
                System.out.print("Comando: ");
                String comando = scanner.nextLine();

                out.writeObject(comando);

                switch (comando.toUpperCase()) {
                    case "L":
                        // Listar produtos
                        List<Produtos> produtos = (List<Produtos>) in.readObject();

                        // Gerar conteúdo para exibição
                        StringBuilder listaProdutos = new StringBuilder("Lista de Produtos:\n");
                        for (Produtos p : produtos) {
                            listaProdutos.append("ID: ").append(p.getProdutoId())
                                         .append(", Nome: ").append(p.getNome())
                                         .append(", Quantidade: ").append(p.getEstoque())
                                         .append("\n");
                        }

                        // Exibir a lista em um JFrame
                        exibirJanela("Produtos", listaProdutos.toString());
                        break;

                    case "E":
                    case "S":
                        // Entrada ou saída de produtos
                        System.out.print("Digite o ID da pessoa: ");
                        int idPessoa = Integer.parseInt(scanner.nextLine());
                        out.writeObject(idPessoa);

                        System.out.print("Digite o ID do produto: ");
                        int idProduto = Integer.parseInt(scanner.nextLine());
                        out.writeObject(idProduto);

                        System.out.print("Digite a quantidade: ");
                        int quantidade = Integer.parseInt(scanner.nextLine());
                        out.writeObject(quantidade);

                        System.out.print("Digite o valor unitário: ");
                        BigDecimal valorUnitario = new BigDecimal(scanner.nextLine());
                        out.writeObject(valorUnitario);
                        break;

                    case "X":
                        // Sair do cliente
                        System.out.println("Encerrando o cliente...");
                        running = false;
                        break;

                    default:
                        System.out.println("Comando inválido. Tente novamente.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para exibir uma mensagem em um JFrame.
     *
     * @param titulo  Título da janela
     * @param mensagem Mensagem a ser exibida
     */
    private static void exibirJanela(String titulo, String mensagem) {
        JFrame frame = new JFrame(titulo);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea(mensagem);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
