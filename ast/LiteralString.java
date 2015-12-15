/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/



package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print(literalString);
    }
    
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;

    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        pw.print(literalString);
    }
}
