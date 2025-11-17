package parser;

/**
 * Resumo: Entrada da tabela de sÃ­mbolos representando uma variÃ¡vel global.
 * ObservaÃ§Ãµes:
 * - Suporta ints simples, arrays de int, variÃ¡veis struct e arrays de structs.
 * - Para variÃ¡veis/arrays de struct, {@code structName} guarda o nome do tipo.
 */
public class TS_entry {
    private String id;
    private int tipo;
    private int nElem;
    private int tipoBase;
    private String structName; // if variable is a struct, its type name

    /**
     * Resumo: Cria uma entrada base.
     * @param umId Identificador
     * @param umTipo CÃ³digo de token/tipo (INT/FLOAT/BOOL) ou -1 para entradas de struct
     * @param ne NÃºmero de elementos para arrays (ou -1 se escalar)
     * @param umTBase Tipo base para arrays (ex.: INT) ou -1
     */
    public TS_entry(String umId, int umTipo, int ne, int umTBase) {
        id = umId;
        tipo = umTipo;
        nElem = ne;
        tipoBase = umTBase;
        structName = null;
    }

    /**
     * Resumo: Cria uma entrada de variÃ¡vel escalar (nÃ£o-array) e nÃ£o-struct.
     */
    public TS_entry(String umId, int umTipo) {
        this(umId, umTipo, -1, -1);
    }

    /**
     * Resumo: Cria uma entrada de variÃ¡vel do tipo struct.
     * @param umId Identificador
     * @param umStructName Nome do tipo struct
     */
    public TS_entry(String umId, String umStructName) {
        this(umId, -1, -1, -1);
        this.structName = umStructName;
    }

    /**
     * Resumo: Cria uma entrada de array de structs.
     * @param umId Identificador
     * @param umStructName Nome do tipo struct
     * @param nElem NÃºmero de elementos no array
     */
    public TS_entry(String umId, String umStructName, int nElem) {
        this(umId, -1, nElem, -1);
        this.structName = umStructName;
    }

    /** @return Nome do identificador. */
    public String getId() { return id; }
    /** @return CÃ³digo de token/tipo (ou -1 para entradas de struct). */
    public int getTipo() { return tipo; }
    /** @return NÃºmero de elementos para arrays, ou -1 se escalar. */
    public int getNumElem() { return nElem; }
    /** @return Tipo base para arrays (ex.: INT), ou -1. */
    public int getTipoBase() { return tipoBase; }
    /** @return Nome do tipo struct se for variÃ¡vel/array de struct; caso contrÃ¡rio, null. */
    public String getStructName() { return structName; }
    /** @return Verdadeiro se representar uma variÃ¡vel struct ou array de structs. */
    public boolean isStruct() { return structName != null; }

    /**
     * Resumo: RepresentaÃ§Ã£o legÃ­vel para saÃ­da de depuraÃ§Ã£o.
     */
    public String toString() {
        String aux = (nElem != -1) ? "\t array(" + nElem + "): " + tipoBase : "";
        String saux = (structName != null) ? "\t struct: " + structName : "";
        return "Id: " + id + "\t tipo: " + tipo + aux + saux;
    }
}
