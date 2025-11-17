import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ABB<K, V> implements IMapeamento<K, V> {

	private No<K, V> raiz; // referência à raiz da árvore.
	private Comparator<K> comparador; // comparador empregado para definir "menores" e "maiores".
	private int tamanho;
	private long comparacoes;
	private long inicio;
	private long termino;

	/**
	 * Método auxiliar para inicialização da árvore binária de busca.
	 * 
	 * Este método define a raiz da árvore como {@code null} e seu tamanho como 0.
	 * Utiliza o comparador fornecido para definir a organização dos elementos na
	 * árvore.
	 * 
	 * @param comparador o comparador para organizar os elementos da árvore.
	 */
	private void init(Comparator<K> comparador) {
		raiz = null;
		tamanho = 0;
		this.comparador = comparador;
	}

	/**
	 * Construtor da classe.
	 * O comparador padrão de ordem natural será utilizado.
	 */
	@SuppressWarnings("unchecked")
	public ABB() {
		init((Comparator<K>) Comparator.naturalOrder());
	}

	/**
	 * Construtor da classe.
	 * Esse construtor cria uma nova árvore binária de busca vazia.
	 * 
	 * @param comparador o comparador a ser utilizado para organizar os elementos da
	 *                   árvore.
	 */
	public ABB(Comparator<K> comparador) {
		init(comparador);
	}

	/**
	 * Construtor da classe.
	 * Esse construtor cria uma nova árvore binária a partir de uma outra árvore
	 * binária de busca,
	 * com os mesmos itens, mas usando uma nova chave.
	 * 
	 * @param original    a árvore binária de busca original.
	 * @param funcaoChave a função que irá extrair a nova chave de cada item para a
	 *                    nova árvore.
	 */
	public ABB(ABB<?, V> original, Function<V, K> funcaoChave) {
		ABB<K, V> nova = new ABB<>();
		nova = copiarArvore(original.raiz, funcaoChave, nova);
		this.raiz = nova.raiz;
	}

	/**
	 * Recursivamente, copia os elementos da árvore original para esta, num processo
	 * análogo ao caminhamento em ordem.
	 * 
	 * @param <T>         Tipo da nova chave.
	 * @param raizArvore  raiz da árvore original que será copiada.
	 * @param funcaoChave função extratora da nova chave para cada item da árvore.
	 * @param novaArvore  Nova árvore. Parâmetro usado para permitir o retorno da
	 *                    recursividade.
	 * @return A nova árvore com os itens copiados e usando a chave indicada pela
	 *         função extratora.
	 */
	private <T> ABB<T, V> copiarArvore(No<?, V> raizArvore, Function<V, T> funcaoChave, ABB<T, V> novaArvore) {

		if (raizArvore != null) {
			novaArvore = copiarArvore(raizArvore.getEsquerda(), funcaoChave, novaArvore);
			V item = raizArvore.getItem();
			T chave = funcaoChave.apply(item);
			novaArvore.inserir(chave, item);
			novaArvore = copiarArvore(raizArvore.getDireita(), funcaoChave, novaArvore);
		}
		return novaArvore;
	}

	/**
	 * Método booleano que indica se a árvore está vazia ou não.
	 * 
	 * @return
	 *         verdadeiro: se a raiz da árvore for null, o que significa que a
	 *         árvore está vazia.
	 *         falso: se a raiz da árvore não for null, o que significa que a árvore
	 *         não está vazia.
	 */
	public Boolean vazia() {
		return (this.raiz == null);
	}

	@Override
	/**
	 * Método que encapsula a pesquisa recursiva de itens na árvore.
	 * 
	 * @param chave a chave do item que será pesquisado na árvore.
	 * @return o valor associado à chave.
	 */
	public V pesquisar(K chave) {
		comparacoes = 0;
		inicio = System.nanoTime();
		V procurado = pesquisar(raiz, chave);
		termino = System.nanoTime();
		return procurado;
	}

	private V pesquisar(No<K, V> raizArvore, K procurado) {

		int comparacao;

		comparacoes++;
		if (raizArvore == null)
			/// Se a raiz da árvore ou sub-árvore for null, a árvore/sub-árvore está vazia e
			/// então o item não foi encontrado.
			throw new NoSuchElementException("O item não foi localizado na árvore!");

		comparacao = comparador.compare(procurado, raizArvore.getChave());

		if (comparacao == 0)
			/// O item procurado foi encontrado.
			return raizArvore.getItem();
		else if (comparacao < 0)
			/// Se o item procurado for menor do que o item armazenado na raiz da árvore:
			/// pesquise esse item na sub-árvore esquerda.
			return pesquisar(raizArvore.getEsquerda(), procurado);
		else
			/// Se o item procurado for maior do que o item armazenado na raiz da árvore:
			/// pesquise esse item na sub-árvore direita.
			return pesquisar(raizArvore.getDireita(), procurado);
	}

	@Override
	/**
	 * Método que encapsula a adição recursiva de itens à árvore, associando-o à
	 * chave fornecida.
	 * 
	 * @param chave a chave associada ao item que será inserido na árvore.
	 * @param item  o item que será inserido na árvore.
	 * 
	 * @return o tamanho atualizado da árvore após a execução da operação de
	 *         inserção.
	 */
	public int inserir(K chave, V item) {
		raiz = inserir(raiz, chave, item);
		tamanho++;
		return tamanho;
	}

	private No<K, V> inserir(No<K, V> i, K chave, V item) {
		if (i == null) {
			return new No<>(chave, item);
		}
		int comp = comparador.compare(chave, i.getChave());
		if (comp < 0) {
			i.setEsquerda(inserir(i.getEsquerda(), chave, item));
		} else if (comp > 0) {
			i.setDireita(inserir(i.getDireita(), chave, item));
		} else {
			i.setItem(item);
		}
		return i;
	}

	@Override
	public String toString() {
		return percorrer();
	}

	@Override
	public String percorrer() {
		return caminhamentoEmOrdem(raiz).trim();
	}

	private String caminhamentoEmOrdem(No<K, V> i) {
		if (i == null) {
			return "";
		}
		return caminhamentoEmOrdem(i.getEsquerda()) +
				i.getItem() + " " +
				caminhamentoEmOrdem(i.getDireita());
	}

	@Override
	/**
	 * Método que encapsula a remoção recursiva de um item da árvore.
	 * 
	 * @param chave a chave do item que deverá ser localizado e removido da árvore.
	 * @return o valor associado ao item removido.
	 */
	public V remover(K chave) {
		if (vazia()) {
			throw new NoSuchElementException("A árvore está vazia!");
		}
		V removido = pesquisar(chave);
		raiz = remover(raiz, chave);
		tamanho--;
		return removido;
	}

	private No<K, V> remover(No<K, V> raizArvore, K chave) {
		if (raizArvore == null) {
			throw new NoSuchElementException("Chave não encontrada!");
		}

		int comparacao = comparador.compare(chave, raizArvore.getChave());

		if (comparacao < 0) {
			raizArvore.setEsquerda(remover(raizArvore.getEsquerda(), chave));
		} else if (comparacao > 0) {
			raizArvore.setDireita(remover(raizArvore.getDireita(), chave));
		} else {
			if (raizArvore.getEsquerda() == null && raizArvore.getDireita() == null) {
				return null;
			} else if (raizArvore.getEsquerda() == null) {
				return raizArvore.getDireita();
			} else if (raizArvore.getDireita() == null) {
				return raizArvore.getEsquerda();
			} else {
				No<K, V> sucessor = encontrarMinimo(raizArvore.getDireita());
				raizArvore.setChave(sucessor.getChave());
				raizArvore.setItem(sucessor.getItem());
				raizArvore.setDireita(remover(raizArvore.getDireita(), sucessor.getChave()));
			}
		}
		return raizArvore;
	}

	private No<K, V> encontrarMinimo(No<K, V> raizArvore) {
		while (raizArvore.getEsquerda() != null) {
			raizArvore = raizArvore.getEsquerda();
		}
		return raizArvore;
	}

	@Override
	public int tamanho() {
		return tamanho;
	}

	@Override
	public long getComparacoes() {
		return comparacoes;
	}

	@Override
	public double getTempo() {
		return (termino - inicio) / 1_000_000;
	}
	public long getInicio(){
		return inicio;
	}
	public long getTermino(){
		return termino;
	}
}