/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

abstract public class Statement {

    abstract public void genC(PW pw);
    abstract public void genkra(PW pw);
    abstract public String getStatementType();
}
