/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/

package ast;

import java.util.Iterator;

public class WriteStatement extends Statement {

    public WriteStatement(ExprList expr) {
        this.exprl1 = expr;
    }

    @Override
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
                pw.printIdent("printf(\"%d \",");
               
                element.genC(pw, false);
                pw.println(");");
            }     
        }
        
    }
    
    private ExprList exprl1 = new ExprList();

    @Override
    public void genkra(PW pw) {
      pw.print("write ( ");
        this.exprl1.genkra(pw);
        pw.print(" );");
        pw.println("");
    }

    @Override
    public String getStatementType() {
        return "write";
    }
}
