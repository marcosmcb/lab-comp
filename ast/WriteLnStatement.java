/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/

package ast;

import java.util.Iterator;


public class WriteLnStatement extends Statement{

    public WriteLnStatement(ExprList expr) {
        this.exprl1 = expr;
    }
    
    public void genC(PW pw) {
        Iterator<Expr> iterator = exprl1.elements();
        
        while(iterator.hasNext())
        {
            Expr element = iterator.next();
            
            if(element.getType().equals(Type.stringType))
            {
                pw.printIdent("puts(\"");
                element.genC(pw, false);
                pw.println("\");");
            }
            else
            {
                pw.printIdent("printf(\"%d \", ");
                element.genC(pw, false);
                pw.println("\n);");
            }     
        }
    }
    
    public void genkra(PW pw) {
        pw.print("writeln ( ");
        
        exprl1.genkra(pw);
        
        pw.print(" );");
        pw.println("");
     } 
    
    private ExprList exprl1 = new ExprList();

    @Override
    public String getStatementType() {
        return "writeln";
    }
    
}
