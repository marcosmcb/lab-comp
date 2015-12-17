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
        
        if (myInstanceVariable == null && myMethod == null)         
            return myKraClass;
        
        else if (myInstanceVariable != null && myMethod == null)    
            return myInstanceVariable.getType();
        
        else                                                        
            return myMethod.getType();
        
    }
    
    /*
        ( (int (*)(_class_A *)) _a->vt[0] )(_a);
    */
    
    @Override
    public void genC( PW pw, boolean putParenthesis ) 
    {
                    System.out.println("CHEGUEI AQUI AQUI AQUI CHEGUEI TO AKI CHEGUEI CHEGUEI - ");

        if(myInstanceVariable==null && myMethod==null && myExprList==null)      
            pw.print("this");
        
        else if(myInstanceVariable!=null && myKraClass!=null)
        {
            //   this->_A_k = 1;

            pw.print("this->_"+myKraClass.getCname()+"_"+myInstanceVariable.getName());
        }
        
        else if(myInstanceVariable == null)     
        {    
            //   ( (void (*)(_class_C *,int )) this->vt[2])( (_class_C *) this, 3 );
            
            pw.print( "( (" + myMethod.getType().getCname() + " (*)" + "(_class_" + myKraClass.getName() + " *" );
            
            if(myExprList!=null)
            for(int i=0; i < myExprList.getSize(); i++)
            {
                pw.print(",");
                pw.print(myExprList.getAt(i).getType().getCname());
            }
            
            pw.print(" )) ");
            
            pw.print(" this->vt[" + getIndexMethod(myKraClass, myMethod) + "])");
            
            pw.print(" ( (_class_" + myKraClass.getName() + " *) this"  );
            
            if(myExprList!=null)
            for(int i=0; i < myExprList.getSize(); i++)
            {
                pw.print(",");
                myExprList.getAt(i).genC(pw, putParenthesis);
            }
            
            pw.print(")");
            
        }   
        
    }
    
    public int getIndexMethod(KraClass myKra, MethodDec myMeth)
    {
        System.out.println("TAmanho da lista  - --------- " + myKra.getPublicMethodsAllPrint().size());

        for (int i=0; i < myKra.getPublicMethodsAllPrint().size() ; i++)
        {    
            System.out.println("NOME DO METOD NO GET INDEX METHOD  - --------- " + myKra.getPublicMethodsAllPrint().get(i).getName());

            if(myKra.getPublicMethodsAllPrint().get(i).getName().equals(myMeth.getName()))
            {
                return i;
            }
        }
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