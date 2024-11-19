package cadastroserver;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutosJpaController;
import controller.UsuarioJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.ServerSocket;
import java.net.Socket;

public class CadastroServer {

    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
            ProdutosJpaController ctrlProd = new ProdutosJpaController(emf);
            UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
            MovimentoJpaController ctrlMov = new MovimentoJpaController(emf);
            PessoaJpaController ctrlPessoa = new PessoaJpaController(emf);

            ServerSocket server = new ServerSocket(4321);
            System.out.println("Servidor iniciado na porta 4321.");

            while (true) {
                Socket socket = server.accept();
                System.out.println("Cliente conectado.");
                CadastroThread thread = new CadastroThread(ctrlProd, ctrlUsu, ctrlMov, ctrlPessoa, socket);
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
