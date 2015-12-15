/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024

*/

package ast;


public class MessageSendToSelf extends MessageSend {
    
    private KraClass myKraClass;
    private InstanceVariable myInstanceVariable;
    private MethodDec myMethod;
    private ExprList myExprList;
    
    @Override
    public Type getType() { 
        
        if (myInstanceVariable == null && myMethod == null)         return myKraClass;
        
        else if (myInstanceVariable != null && myMethod == null)    return myInstanceVariable.getType();
        
        else                                                        return myMethod.getType();
        
    }
    
    /*
        ( (int (*)(_class_A *)) _a->vt[0] )(_a);
    */
    
    @Override
    public void genC( PW pw, boolean putParenthesis ) 
    {
        
        if(myInstanceVariable==null && myMethod==null && myExprList==null)      pw.print("this");
        
        else if(myExprList==null && myInstanceVariable == null)     
        {    
            pw.print("( (" + myMethod.getType().getName() + " (*) (_class_" + myKraClass.getName()  + " *)) this->vt[");
            pw.print( Integer.toString( getIndexMethod(myKraClass, myMethod) ) );
            pw.print("]) ((_class_" + myKraClass.getName() + "*) this)");
        }   
        
    }
    
    public int getIndexMethod(KraClass myKra, MethodDec myMeth)
    {
        for (int i=0; i < myKra.getPublicMethods().size(); i++)
            if(myKra.getPublicMethods().get(i).getName().equals(myMeth.getName()))
                return i;
        
        for (int i=0; i < myKra.getPrivateMethods().size(); i++)
            if(myKra.getPrivateMethods().get(i).getName().equals(myMeth.getName()))
                return i;
        
        return -1;
    }
    
    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        
    }

    public MessageSendToSelf(KraClass myKraClass, InstanceVariable myInstanceVariable, MethodDec myMethod, ExprList myExprList) {
        this.myKraClass = myKraClass;
        this.myInstanceVariable = myInstanceVariable;
        this.myMethod = myMethod;
        this.myExprList = myExprList;
    }

    public KraClass getMyKraClass() {
        return myKraClass;
    }

    public void setMyKraClass(KraClass myKraClass) {
        this.myKraClass = myKraClass;
    }

    public InstanceVariable getMyInstanceVariable() {
        return myInstanceVariable;
    }

    public void setMyInstanceVariable(InstanceVariable myInstanceVariable) {
        this.myInstanceVariable = myInstanceVariable;
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
    
}