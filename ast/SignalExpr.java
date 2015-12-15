/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

import lexer.*;

public class SignalExpr extends Expr {

    public SignalExpr( Symbol oper, Expr expr ) {
       this.oper = oper;
       this.expr = expr;
    }

    @Override
    public void genC( PW pw, boolean putParenthesis ) {
       
       if ( putParenthesis )    pw.print("(");
       
       pw.print( oper == Symbol.PLUS ? "+" : "-" );
       expr.genC(pw, true);
       
       if ( putParenthesis )
          pw.print(")");
    }

    @Override
	public Type getType() {
       return expr.getType();
    }

    private Expr expr;
    private Symbol oper;

    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if ( putParenthesis )
          pw.print("(");
          pw.print( oper == Symbol.PLUS ? "+" : "-" );
          expr.genC(pw, true);
        if ( putParenthesis )
          pw.print(")");
    }
}
