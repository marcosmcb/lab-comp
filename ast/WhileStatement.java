/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/

package ast;


public class WhileStatement extends Statement {

    public WhileStatement(Expr e, Statement s) {
        this.exp = e;
       // if(s==null)
         //   System.out.println("abobrinha");
        this.state1 = s;
         
        statementType = "while";
        
    }
//  WhileStat ::= "while" "(" Expression ")" Statement
    @Override
    public void genC(PW pw) {
        
        pw.print("while ( ");
        this.exp.genC(pw, true);
        pw.println(" ){");
        pw.add();
        
        if(state1!=null)
            this.state1.genC(pw);
      
        
        pw.println(";");
	pw.sub();
        pw.println("}");    
    }
    private Expr exp;
    private Statement state1;
    private String statementType;
    
    
    @Override
    public void genkra(PW pw) {
        pw.print("while ( ");
        this.exp.genC(pw, true);
        pw.println(" ){");
        
        if(this.state1 != null)
            this.state1.genC(pw);
        
        pw.println("}");
        
    }

    @Override
    public String getStatementType() {
        return "while";
    }
}
