/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author poulicer
 */
public class ObjectCreation extends Expr{



	public ObjectCreation(KraClass kra) {
		this.kra = kra;
                
	}
	

	private KraClass kra;

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        pw.print("new ");
        pw.println(kra.getCname()+"();");
    }

    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}


