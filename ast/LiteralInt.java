/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/


package ast;

public class LiteralInt extends Expr {
    
    public LiteralInt( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genC( PW pw, boolean putParenthesis ) {
        pw.printIdent("" + value);
    }
    
    public Type getType() {
        return Type.intType;
    }
    
    private int value;

    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        pw.print( Integer.toString(this.value) );
    }
}
