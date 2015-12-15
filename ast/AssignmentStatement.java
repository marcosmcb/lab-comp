/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author marcoscavalcante
 */
public class AssignmentStatement extends Statement {

    @Override
    public void genC(PW pw) {
        
        if(exp1 != null)    this.exp1.genC(pw, true);
        
        pw.print(" = ");
        
        if(exp2 != null)    this.exp2.genC(pw, true);
        
        pw.print(";");
        pw.println(""); 
    
    
    }

    @Override
    public void genkra(PW pw) {
        
        if(exp1!=null)
            this.exp1.genKra(pw, true);
        
        pw.print(" = ");
        
        if(exp2!=null)
            this.exp2.genKra(pw, true);
        
        pw.print(";");
        pw.println("");
        
    }

    @Override
    public String getStatementType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    
    public AssignmentStatement(Expr exp1, Expr exp2){
        this.exp1 = exp1;
        this.exp2 = exp2;
    }
    
    private Expr exp1, exp2;

    public Expr getExp1() {
        return exp1;
    }

    public void setExp1(Expr exp1) {
        this.exp1 = exp1;
    }

    public Expr getExp2() {
        return exp2;
    }

    public void setExp2(Expr exp2) {
        this.exp2 = exp2;
    }
    
    
}
