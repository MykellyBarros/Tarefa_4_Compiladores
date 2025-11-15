
/**
 * Write a description of class Paciente here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TS_entry
{
   private String id;
   private int tipo;
   private int nElem;
   private int tipoBase;
    private String structName; // se variavel for struct, nome do tipo


    public TS_entry(String umId, int umTipo, int ne, int umTBase) {
      id = umId;
      tipo = umTipo;
      nElem = ne;
      tipoBase = umTBase;
        structName = null;
   }

    public TS_entry(String umId, int umTipo) {
        this(umId, umTipo, -1, -1);
    }

    // construtor para variavel de struct
    public TS_entry(String umId, String umStructName) {
        this(umId, -1, -1, -1);
        this.structName = umStructName;
    }


   public String getId() {
       return id; 
   }

   public int getTipo() {
       return tipo; 
   }
   
   public int getNumElem() {
       return nElem; 
   }

   public int getTipoBase() {
       return tipoBase; 
   }

   public String getStructName() {
       return structName;
   }

   public boolean isStruct() {
       return structName != null;
   }

   
   public String toString() {
       String aux = (nElem != -1) ? "\t array(" + nElem + "): "+tipoBase : "";
       String saux = (structName != null) ? "\t struct: "+structName : "";
       return "Id: " + id + "\t tipo: " + tipo + aux + saux;
   }


}
