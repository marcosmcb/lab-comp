/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class TypeString extends Type {
    
    public TypeString() {
        super("String");
    }
    
   public String getCname() {
      return "char *";
   }

}