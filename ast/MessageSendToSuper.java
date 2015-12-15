/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024

*/

package ast;

public class MessageSendToSuper extends MessageSend { 
    
    private MethodDec myMethod;
    private ExprList myExprList;
    private KraClass myKraClass;

    public MessageSendToSuper(MethodDec myMethodMessage, ExprList myExprList, KraClass myKraClass) {
        this.myMethod = myMethodMessage;
        this.myExprList = myExprList;
        this.myKraClass = myKraClass;
    }

    public MethodDec getMyMethod() {
        return myMethod;
    }

    public void setMyMethod(MethodDec myMethod) {
        this.myMethod = myMethod;
    }

    public ExprList getMyExprList() {
        return myExprList;
    }

    public void setMyExprList(ExprList myExprList) {
        this.myExprList = myExprList;
    }

    public KraClass getMyKraClass() {
        return myKraClass;
    }

    public void setMyKraClass(KraClass myKraClass) {
        this.myKraClass = myKraClass;
    }

    
    public Type getType() { 
        return null;
    }

    public void genC( PW pw, boolean putParenthesis ) {
        
        pw.print("_"+myKraClass.getName()+"_"+myMethod.getName()+"( (_class_"+myKraClass.getName()+" *) this");
        
        if(myMethod.getPl().getSize() > 0)
        { 
            pw.print(", ");
            myMethod.getPl().genC(pw);
        }
        
        if(myExprList.getExprList().size() > 0)
        {
            pw.print(",");
            
            for(int i=0; i < myExprList.getExprList().size(); i++)  
                myExprList.getExprList().get(i).genC(pw, putParenthesis);
            
        }
        
        pw.print(" );");
    }

    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}