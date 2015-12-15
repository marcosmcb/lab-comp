/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024

*/

package ast;

public class MessageSendStatement extends Statement { 


    public void genC( PW pw ) {
       pw.printIdent("");
       if(messageSend!=null)
       messageSend.genC(pw, false);
       pw.println(";");
    }
    
    public MessageSendStatement(Expr messageSend)
    {
        this.messageSend = messageSend;
    }

    private Expr messageSend;

    @Override
    public void genkra(PW pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getStatementType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}


