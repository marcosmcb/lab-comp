/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/

package ast;

public class BreakStatement extends Statement{

    public BreakStatement(){
        statementType = "break";
    }
    
    @Override
    public void genC(PW pw) {
         pw.println("break;");
    }

    @Override
    public void genkra(PW pw) {
        pw.println("break;");
    }
    
    
    private String statementType;

    @Override
    public String getStatementType() {
        return "break";
    }

    
}
