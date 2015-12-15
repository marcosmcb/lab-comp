/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class TypeBoolean extends Type {

   public TypeBoolean() { 
       super("boolean"); 
   }

   @Override
   public String getCname() {
      return "int";
   }

}
