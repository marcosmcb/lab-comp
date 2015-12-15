/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class TypeVoid extends Type {
    
    public TypeVoid() {
        super("void");
    }
    
   public String getCname() {
      return "void";
   }

}