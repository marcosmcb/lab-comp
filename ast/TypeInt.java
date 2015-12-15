/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class TypeInt extends Type {
    
    public TypeInt() {
        super("int");
    }
    
   public String getCname() {
      return "int";
   }

}