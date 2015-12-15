/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class TypeUndefined extends Type {
    // variables that are not declared have this type
    
   public TypeUndefined() { super("undefined"); }
   
   public String getCname() {
      return "int";
   }
   
}
