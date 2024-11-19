package cadastroserver;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutosJpaController;
import controller.UsuarioJpaController;
import model.Movimento;
import model.Produtos;

import java.io.*;
import java.net.Socket;
import java.math.BigDecimal;
import java.util.List;

public class CadastroThread extends Thread {

    private ProdutosJpaController ctrlProd;
    private UsuarioJpaController ctrlUsu;
    private MovimentoJpaController ctrlMov;
    private PessoaJpaController ctrlPessoa;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public CadastroThread(ProdutosJpaController ctrlProd, UsuarioJpaController ctrlUsu, MovimentoJpaController ctrlMov, PessoaJpaController ctrlPessoa, Socket socket) {
        this.ctrlProd = ctrlProd;
        this.ctrlUsu = ctrlUsu;
        this.ctrlMov = ctrlMov;
        this.ctrlPessoa = ctrlPessoa;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Inicializando os streams de entrada e saída
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            // Realizar o login
            String login = (String) in.readObject();
            String senha = (String) in.readObject();

            // Autenticar o usuário
            if (login.equals("op1") && senha.equals("op1")) {
                out.writeObject("Login bem-sucedido!");
            } else {
                out.writeObject("Falha no login!");
                return; // Encerrar a conexão se o login falhar
            }

            // Lidar com os comandos recebidos
            while (true) {
                String comando = (String) in.readObject();
                if (comando.equals("X")) {
                    break; // Encerra a thread se o comando for "X"
                }

                if (comando.equals("L")) {
                    // Comando para listar produtos
                    listarProdutos();
                } else if (comando.equals("E") || comando.equals("S")) {
                    // Comando para entrada ou saída de produtos
                    realizarMovimento(comando);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para listar produtos
    private void listarProdutos() throws IOException {
        try {
            // Recupera a lista de produtos do banco
            List<Produtos> produtos = ctrlProd.findProdutosEntities();
            out.writeObject(produtos); // Envia a lista de produtos para o cliente
        } catch (Exception e) {
            out.writeObject("Erro ao listar produtos: " + e.getMessage());
        }
    }

    // Método para realizar a entrada ou saída de produtos
    private void realizarMovimento(String tipo) throws IOException, ClassNotFoundException {
        try {
            // Recebe os dados para o movimento
            int idPessoa = (int) in.readObject();
            int idProduto = (int) in.readObject();
            int quantidade = (int) in.readObject();
            BigDecimal valorUnitario = (BigDecimal) in.readObject();

            // Criar o objeto Movimento
            Movimento movimento = new Movimento();
            // Alteração do código no método realizarMovimento:
            movimento.setTipo(tipo.equals("E") ? 'E' : 'S'); // Passando char ao invés de String
            movimento.setId(ctrlMov.generateId());
            movimento.setIdPessoa(ctrlPessoa.findPessoa(idPessoa));
            movimento.setIdProduto(ctrlProd.findProduto(idProduto));
            movimento.setQuantidade(quantidade);
            movimento.setValorUni(valorUnitario);

            // Persistir o movimento no banco
            ctrlMov.create(movimento);

            // Atualizar o estoque do produto
            Produtos produto = ctrlProd.findProduto(idProduto);
            if (tipo.equals("E")) {
                produto.setEstoque(produto.getEstoque() + quantidade);
            } else if (tipo.equals("S")) {
                produto.setEstoque(produto.getEstoque() - quantidade);
            }
            ctrlProd.edit(produto); // Atualiza o produto no banco

            out.writeObject("Movimento realizado com sucesso.");
        } catch (Exception e) {
            out.writeObject("Erro ao processar movimento: " + e.getMessage());
        }
    }
}
