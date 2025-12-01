import java.util.ArrayList;
import java.util.List;

public class Fornecedor {
    private static int ultimoID = 10000;
    private String nome;
    private int documento;
    private List<Produto> produtos;

    public Fornecedor(String nome) {
        if (nome == null || nome.trim().isEmpty() || nome.trim().split("\\s+").length < 2) {
            throw new IllegalArgumentException(
                    "O nome deve conter pelo menos duas palavras.");
        }
        this.nome = nome.trim();

        Fornecedor.ultimoID++;
        this.documento = Fornecedor.ultimoID;

        this.produtos = new ArrayList<>();
    }

    public void adicionarProduto(Produto novo) {
        if (novo == null) {
            throw new IllegalArgumentException("Nã o podem ser armazenados produtos nulos");
        }
        this.produtos.add(novo);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("--- Fornecedor ---\n");
        builder.append("Nome: ").append(nome).append("\n");
        builder.append("Documento (ID): ").append(documento).append("\n");

        builder.append("--- Histórico de Produtos (Total: ").append(produtos.size()).append(") ---\n");

        if (produtos.isEmpty()) {
            builder.append("Nenhum produto registrado.\n");
        } else {

            for (Produto produt : produtos) {

                builder.append("* ").append(produt.toString()).append("\n");
            }
        }
        builder.append("---------------------------------\n");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return this.documento;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto)
            return true;
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        Fornecedor novoFornecedor = (Fornecedor) objeto;
        return documento == novoFornecedor.documento;
    }

}
