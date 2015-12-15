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


import java.util.ArrayList;
import java.util.Iterator;

public class Composite extends Statement {

	public Composite(ArrayList<Statement> stmtList) {
		this.stmtList = stmtList;
	}

	@Override
	public void genC(PW pw) {
		Iterator<Statement> istmt = stmtList.iterator();
		while ( istmt.hasNext() ) {
			istmt.next().genC(pw);
		}
//		pw.println(";");
	}
	
	

	
	
	private ArrayList<Statement> stmtList;

    @Override
    public void genkra(PW pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getStatementType() {
       return "undefined";
        //To change body of generated methods, choose Tools | Templates.
    }
}
