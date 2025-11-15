import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;


public class TabSimb
{
    private ArrayList<TS_entry> lista;
  // tipos de struct definidos: nome -> tipo (offsets e tamanho)
  private HashMap<String, StructType> structTypes;
    
    public TabSimb( )
    {
    lista = new ArrayList<TS_entry>();
    structTypes = new HashMap<String, StructType>();
    }
    
    public void insert( TS_entry nodo ) {
      lista.add(nodo);
    }    
    
    public void listar() {
      System.out.println("\n\n# Listagem da tabela de simbolos:\n");
      for (TS_entry nodo : lista) {
          System.out.println("# " + nodo);
      }
    if (!structTypes.isEmpty()) {
      System.out.println("\n# Tipos struct definidos:");
      for (String k: structTypes.keySet()) {
        StructType st = structTypes.get(k);
        System.out.println("# struct "+k+" (size="+st.size+")");
        for (String f: st.fieldOffsets.keySet()) {
          System.out.println("#   ."+f+" @ "+st.fieldOffsets.get(f));
        }
      }
    }
    }
      
    public TS_entry pesquisa(String umId) {
      for (TS_entry nodo : lista) {
          if (nodo.getId().equals(umId)) {
	      return nodo;
            }
      }
      return null;
    }

	public void geraGlobais() {
          // assume que todas variáveis são globais e inteiras.
	      for (TS_entry nodo : lista) {
	            int ne = nodo.getNumElem();
	            if (nodo.isStruct()) {
	                int sz = getStructSize(nodo.getStructName());
	                if (sz <= 0) sz = 4; // fallback
	                System.out.println("_"+nodo.getId()+":"+"\t.zero "+sz);
	            } else if (ne != -1) {
	                // array de inteiros: alocar 4*ne bytes
	                System.out.println("_"+nodo.getId()+":"+"\t.zero "+(4*ne));
	            } else {
	                System.out.println("_"+nodo.getId()+":"+"\t.zero 4");
	            }
	      }
	      }
     

  // ====== structs ======
  private static class StructType {
    HashMap<String,Integer> fieldOffsets = new HashMap<>();
    int size = 0;
  }

  public void registerStructType(String name, ArrayList<String> fields) {
    StructType st = new StructType();
    int off = 0;
    for (String f: fields) {
      st.fieldOffsets.put(f, off);
      off += 4; // apenas int
    }
    st.size = off;
    structTypes.put(name, st);
  }

  public boolean hasStructType(String name) {
    return structTypes.containsKey(name);
  }

  public int getStructSize(String name) {
    StructType st = structTypes.get(name);
    return (st == null) ? -1 : st.size;
  }

  public int getFieldOffsetForVar(String var, String field) {
    TS_entry v = pesquisa(var);
    if (v == null || !v.isStruct()) return -1;
    StructType st = structTypes.get(v.getStructName());
    if (st == null) return -1;
    Integer off = st.fieldOffsets.get(field);
    return off == null ? -1 : off.intValue();
  }


}



