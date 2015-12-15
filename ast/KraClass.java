/*
Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024
*/
////////////////////////////////////////////////////////
package ast;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * Krakatoa Class
 */
public class KraClass extends Type {
	
   public KraClass( String name ) {
      super(name);
   }
   
   public String getCname() {
      return getName();
   }
   
   private String name;
   private KraClass superclass ;
   private boolean isFinal;
   private boolean hasSuper;
   private String qualifier;
   private ArrayList<InstanceVariable> instanceVariableList = new ArrayList<>();
   private ArrayList<MethodDec> privateMethods =  new ArrayList<>();
   private ArrayList<MethodDec> publicMethods =  new ArrayList<>();

    public ArrayList<InstanceVariable> getInstanceVariableList() {
        return instanceVariableList;
    }
    
    public InstanceVariable getInstanceVariable(String ins){
 
        for (InstanceVariable instanceVariableList1 : instanceVariableList)
            if (instanceVariableList1.getName().equals(ins))  return instanceVariableList1;
           
        return null;
    }
    
    public MethodDec getPublicMethod(String pub){
 
        for (MethodDec publicMethod : publicMethods) 
           if (publicMethod.getName().equals(pub))  return publicMethod;
               
        return null;
    }
    
    public MethodDec getPrivateMethod(String pub){
 
        for (MethodDec privateMethod : publicMethods) 
           if (privateMethod.getName().equals(pub))  return privateMethod;
               
        return null;
    }
    
    public void setInstanceVariableList(ArrayList<InstanceVariable> instanceVariableList) {
        this.instanceVariableList = instanceVariableList;
    }
    
    public void addInstanceVariable(InstanceVariable instance){
        this.instanceVariableList.add(instance);
    }
   
    public ArrayList<MethodDec> getPrivateMethods() {
        return privateMethods;
    }

    public void setPrivateMethods(ArrayList<MethodDec> privateMethods) {
        this.privateMethods = privateMethods;
    }
   

    public ArrayList<MethodDec> getPublicMethods() {
        return publicMethods;
    }

    public void setPublicMethods(ArrayList<MethodDec> publicMethods) {
        this.publicMethods = publicMethods;
    }
    
    public void addMethodToPublicList(MethodDec publicMethod){
        this.publicMethods.add(publicMethod);
    }
    
    public void addMethodToPrivateList(MethodDec privateMethod){
        this.privateMethods.add(privateMethod);
    }
   
    public boolean getHasSuper() {
        return hasSuper;
    }

    public void setHasSuper(boolean hasSuper) {
        this.hasSuper = hasSuper;
    }

    public KraClass getSuperclass() {
        return superclass;
    }

    public void setSuperclass(KraClass superclass) {
        this.superclass = superclass;
    }
    
    public boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
    void genC(PW pw){
        
        pw.println("typedef struct _St_" + this.getName() + " {");
        pw.add();
        pw.printlnIdent("Func *vt;");
        
        if(getHasSuper())   
        {
            if( !this.superclass.getInstanceVariableList().isEmpty() )
                for (InstanceVariable instanceVariableList1 : this.superclass.instanceVariableList) {
                    pw.printIdent( instanceVariableList1.getType().getName() );
                    pw.printIdent(" _" + this.superclass.name + "_"+ instanceVariableList1.getName() +";");
                    pw.printlnIdent("");
                }
        }
        
        if(!this.getInstanceVariableList().isEmpty())
            for (InstanceVariable instanceVariableList1 : this.getInstanceVariableList()) 
            {
                pw.printIdent( instanceVariableList1.getType().getName() );
                pw.printIdent(" _" + this.getName() + "_" + instanceVariableList1.getName() + ";");
                pw.printlnIdent("");
            }
        
        pw.println("} _class_"+ this.getName()+";");
        pw.println("");
        //if class is not abstract, falta verificar
        pw.println("_class_"+ this.getName() + "*new_"+this.getName() +"(void);");
        /*definindo estrutura para a classe,
        variáveis de instancia devem ser declaradas nesta estrutura
        */
        
      
           
   
          
        
        
        //if (this.superclass != null) {
            //pw.print("extends " + this.superclass.getName());
        //}
 
        if ( !this.privateMethods.isEmpty() ){
            for (MethodDec privateMethod : this.privateMethods) {
                privateMethod.genC(pw);
            }
        }
        
        if ( !this.publicMethods.isEmpty() ) {
           for (MethodDec publicMethod : this.publicMethods) {
               publicMethod.genC(pw);
           }
        }
        
        
        pw.println("Func VTclass_"+this.getCname()+"[] = {");
        pw.add();
        
        if( this.hasSuper )     printSuperClassesMethods(pw);
        
        int quant = 0;
        for (MethodDec publicMethod : this.publicMethods) {
               if(quant == 0)
                   pw.println("( void (*)() ) _"+this.getCname()+"_"+publicMethod.getName());
               else{
               pw.println(",( void (*)() ) _"+this.getCname()+"_"+publicMethod.getName());
     
               }               
            quant++;
        }
        
        
        
        
        pw.println("");
                
        pw.sub();
        pw.println("};");
        
        
           
        pw.println("");
        pw.println("_class_"+ this.getCname()+" *new_"+ this.getCname() +"()");
        pw.println("{");
        pw.println("_class_" + this.getCname() + "*t;");
        pw.println("if ( (t = malloc(sizeof(_class_"+this.getCname()+"))) != NULL )");
        // o texto explica porque vt  ́e inicializado
        pw.println("t->vt = VTclass_"+this.getCname() + ";");
        pw.println("return t;");
        pw.println("}");
        pw.println("");
        
        
          
    }
    
    void printSuperClassesMethods(PW pw)
    {
        KraClass kraAux = this.superclass;
        
        do
        {
            if( !kraAux.getPublicMethods().isEmpty() )
                for (MethodDec publicMethod : kraAux.getPublicMethods()) 
                    if(publicMethod.getIsFinal() == null)   
                        pw.printlnIdent("( void (*)() ) _"+kraAux.getName()+"_"+publicMethod.getName());
            
            if(kraAux.hasSuper)     kraAux = kraAux.superclass;
            else    break;
            
        }while(true);
    }
    
    void genKrakatoa(PW pw) {
        
        if ( this.qualifier != null ){
            pw.print(this.qualifier);
        }
        
        if (this.isFinal == true){
            pw.print(" final ");
        }
        
        pw.print("class " + this.getName());
        
        if (this.superclass != null) {
            pw.print("extends " + this.superclass.getName());
        }
        pw.println(" {");
        
     
        //biribibi
        
        if ( !this.privateMethods.isEmpty() ){
            for (MethodDec privateMethod : this.privateMethods) {
                privateMethod.genKrakatoa(pw);
            }
        }
        
        if ( !this.publicMethods.isEmpty() ) {
           for (MethodDec publicMethod : this.publicMethods) {
               publicMethod.genKrakatoa(pw);
           }
        }
        
       
    }

   
}
