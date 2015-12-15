/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/


package ast;


public class NullStatement extends Statement {

    @Override
    public void genC(PW pw) {
       pw.print("NULL");
    }

    @Override
    public void genkra(PW pw) {
        pw.print("null;");
    }

    @Override
    public String getStatementType() {
        return "null";
    }
}
