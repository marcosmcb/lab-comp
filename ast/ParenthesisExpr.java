/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class ParenthesisExpr extends Expr {
    
    public ParenthesisExpr( Expr expr ) {
        this.expr = expr;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print("(");
        expr.genC(pw, false);
        pw.printIdent(")");
    }
    
    public Type getType() {
        return expr.getType();
    }
    
    private Expr expr;

    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        pw.print("(");
        expr.genC(pw, false);
        pw.printIdent(")");
    
    }
}