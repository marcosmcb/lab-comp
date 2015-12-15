/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class NullExpr extends Expr {
    
   public void genC( PW pw, boolean putParenthesis ) {
      pw.printIdent("NULL");
   }
   
   public Type getType() {
      //# corrija
      return Type.undefinedType;
   }

    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        pw.print("null;");
        pw.println("");
    }
}