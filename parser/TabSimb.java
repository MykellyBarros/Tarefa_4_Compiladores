package parser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

/**
 * Resumo: Tabela de sÃ­mbolos para variÃ¡veis e tipos struct.
 * ObservaÃ§Ãµes:
 * - Armazena variÃ¡veis globais (ints, arrays e variÃ¡veis/arrays de struct) e
 *   emite sua alocaÃ§Ã£o na seÃ§Ã£o de dados.
 * - MantÃ©m definiÃ§Ãµes de tipos struct com offsets de campos e tamanho total.
 */
public class TabSimb {
    /** Lista base de sÃ­mbolos globais declarados. */
    private ArrayList<TS_entry> lista;
    /** Mapa do nome do tipo struct para seus metadados (offsets de campos e tamanho). */
    private HashMap<String, StructType> structTypes;

    /**
     * Resumo: Cria uma tabela de sÃ­mbolos vazia.
     */
    public TabSimb() {
        lista = new ArrayList<TS_entry>();
        structTypes = new HashMap<String, StructType>();
    }

    /**
     * Resumo: Insere um novo sÃ­mbolo na tabela.
     * @param nodo Entrada de sÃ­mbolo a inserir.
     */
    public void insert(TS_entry nodo) {
        lista.add(nodo);
    }

    /**
     * Resumo: Imprime um resumo legÃ­vel da tabela de sÃ­mbolos e tipos struct.
     * ObservaÃ§Ãµes: Ãštil para depuraÃ§Ã£o e saÃ­da didÃ¡tica.
     */
    public void listar() {
        System.out.println("\n\n# Listagem da tabela de simbolos:\n");
        for (TS_entry nodo : lista) {
            System.out.println("# " + nodo);
        }
        if (!structTypes.isEmpty()) {
            System.out.println("\n# Tipos struct definidos:");
            for (String k : structTypes.keySet()) {
                StructType st = structTypes.get(k);
                System.out.println("# struct " + k + " (size=" + st.size + ")");
                for (String f : st.fieldOffsets.keySet()) {
                    System.out.println("#   ." + f + " @ " + st.fieldOffsets.get(f));
                }
            }
        }
    }

    /**
     * Resumo: Procura um sÃ­mbolo pelo identificador.
     * @param umId Identificador a pesquisar.
     * @return A entrada do sÃ­mbolo ou null se nÃ£o encontrado.
     */
    public TS_entry pesquisa(String umId) {
        for (TS_entry nodo : lista) {
            if (nodo.getId().equals(umId)) {
                return nodo;
            }
        }
        return null;
    }

    /**
     * Resumo: Emite alocaÃ§Ãµes na seÃ§Ã£o .data para todos os sÃ­mbolos globais.
     * ObservaÃ§Ãµes: Arrays de int ocupam 4*n bytes; variÃ¡veis struct ocupam structSize; arrays de structs ocupam structSize*n.
     */
    public void geraGlobais() {
        for (TS_entry nodo : lista) {
            int ne = nodo.getNumElem();
            if (nodo.isStruct()) {
                int sz = getStructSize(nodo.getStructName());
                if (sz <= 0) sz = 4; // fallback safe-guard
                int total = (ne != -1) ? (sz * ne) : sz;
                System.out.println("_" + nodo.getId() + ":" + "\t.zero " + total);
            } else if (ne != -1) {
                // array of ints: allocate 4*ne bytes
                System.out.println("_" + nodo.getId() + ":" + "\t.zero " + (4 * ne));
            } else {
                System.out.println("_" + nodo.getId() + ":" + "\t.zero 4");
            }
        }
    }

    // ====== structs ======

    /**
     * Resumo: Metadados de um tipo struct.
     * Campos: mapeia nome do campo -> offset em bytes; tamanho total em bytes.
     */
    private static class StructType {
        HashMap<String, Integer> fieldOffsets = new HashMap<>();
        int size = 0;
    }

    /**
     * Resumo: DescriÃ§Ã£o de um campo de struct.
     * @param name Identificador do campo.
     * @param length NÃºmero de elementos int (1 para escalar, >1 para arrays de int).
     */
    public static class StructField {
        public final String name;
        public final int length; // 1 = scalar, >1 = int array
        public StructField(String name, int length) {
            this.name = name;
            this.length = length;
        }
    }

    /**
     * Resumo: Registra um tipo struct com o layout de seus campos.
     * @param name Nome do tipo struct.
     * @param fields Lista de campos (escalares ou arrays de int) na ordem de declaraÃ§Ã£o.
     */
    public void registerStructType(String name, ArrayList<StructField> fields) {
        StructType st = new StructType();
        int off = 0;
        for (StructField f : fields) {
            st.fieldOffsets.put(f.name, off);
            off += 4 * Math.max(1, f.length); // each int is 4 bytes
        }
        st.size = off;
        structTypes.put(name, st);
    }

    /**
     * Resumo: Verifica se um tipo struct estÃ¡ definido.
     * @param name Nome do tipo struct.
     * @return Verdadeiro se existir.
     */
    public boolean hasStructType(String name) {
        return structTypes.containsKey(name);
    }

    /**
     * Resumo: ObtÃ©m o tamanho total (bytes) de um tipo struct.
     * @param name Nome do tipo struct.
     * @return Tamanho em bytes, ou -1 se desconhecido.
     */
    public int getStructSize(String name) {
        StructType st = structTypes.get(name);
        return (st == null) ? -1 : st.size;
    }

    /**
     * Resumo: ObtÃ©m o offset (bytes) de um campo para uma variÃ¡vel de tipo struct.
     * @param var Identificador da variÃ¡vel do tipo struct.
     * @param field Nome do campo dentro da struct.
     * @return Offset em bytes ou -1 se nÃ£o encontrado.
     */
    public int getFieldOffsetForVar(String var, String field) {
        TS_entry v = pesquisa(var);
        if (v == null || !v.isStruct()) return -1;
        StructType st = structTypes.get(v.getStructName());
        if (st == null) return -1;
        Integer off = st.fieldOffsets.get(field);
        return off == null ? -1 : off.intValue();
    }

    /**
     * Resumo: ObtÃ©m o offset (bytes) de um campo pelo nome do tipo struct.
     * ObservaÃ§Ãµes: Ãštil para arrays de structs onde apenas o nome do tipo Ã© conhecido.
     * @param structName Nome do tipo struct.
     * @param field Nome do campo.
     * @return Offset em bytes ou -1 se nÃ£o encontrado.
     */
    public int getFieldOffsetForType(String structName, String field) {
        StructType st = structTypes.get(structName);
        if (st == null) return -1;
        Integer off = st.fieldOffsets.get(field);
        return off == null ? -1 : off.intValue();
    }

    /**
     * Resumo: Auxiliar opcional para comprimento de arrays de campo.
     * ObservaÃ§Ãµes: NÃ£o Ã© necessÃ¡rio pela geraÃ§Ã£o de cÃ³digo; retorna 1 por simplicidade.
     */
    public int getFieldArrayLenForType(String structName, String field) {
        return 1;
    }
}



