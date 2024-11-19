package cadastroclient;

import model.Produtos;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class CadastroClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4321);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Enviar login e senha
            out.writeObject("op1");
            out.writeObject("op1");

            // Receber resposta de autenticação
            System.out.println("Resposta do servidor: " + in.readObject());

            // Enviar comando para listar produtos
            out.writeObject("L");

            // Receber lista de produtos
            List<Produtos> produtos = (List<Produtos>) in.readObject();
            for (Produtos produto : produtos) {
                System.out.println(produto.getNome());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
