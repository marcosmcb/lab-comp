/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/

package ast;

public class ReturnStatement extends Statement {

    public ReturnStatement(Expr expr) {
        this.expr1 = expr;
    }

//  ReturnStat ::= "return" Expression
    @Override
    public void genC(PW pw){
        pw.print("return ");
        expr1.genC(pw, true);
        pw.print(";");
        pw.println("");
    }

    private Expr expr1;

    @Override
    public void genkra(PW pw) {
        pw.print("return ");
        expr1.genC(pw, true);
        pw.print(";");
        pw.println("");
    }

    @Override
    public String getStatementType() {
        return "return";
    }
}
