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
   
    private ArrayList<MethodDec> publicMethodsAllPrint =  new ArrayList<>();

    public ArrayList<MethodDec> getPublicMethodsAllPrint() {
        return publicMethodsAllPrint;
    }

    public void setPublicMethodsAllPrint(ArrayList<MethodDec> publicMethodsAllPrint) {
        this.publicMethodsAllPrint = publicMethodsAllPrint;
    }

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
           if (publicMethod.getName().equals(pub))  
               return publicMethod;
               
        return null;
    }
    
    public MethodDec getPrivateMethod(String pub){
 
        for (MethodDec privateMethod : publicMethods) 
           if (privateMethod.getName().equals(pub))  
               return privateMethod;
               
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
            
            printSuperClassesInstanceVariables(pw);
            
        }
        
        if(!this.getInstanceVariableList().isEmpty())
        {
            for (InstanceVariable instanceVariableList1 : this.getInstanceVariableList()) 
            {
                
                pw.printIdent( instanceVariableList1.getType().getName() );
                pw.printIdent(" _" + this.getCname() + "_" + instanceVariableList1.getName() + ";");
                pw.printlnIdent("");
            }
        }
        pw.println("} _class_"+ this.getName()+";");
        pw.println("");
        //if class is not abstract, falta verificar
        pw.println("_class_"+ this.getName() + "*new_"+this.getName() +"(void);");
        /*definindo estrutura para a classe,
        variáveis de instancia devem ser declaradas nesta estrutura
        */
        
      
        if( this.hasSuper )     
            addSuperClassesMethods();
        
        addCurrentClassMethods();
          
        
        
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
        
        //if( this.hasSuper ){
        //    printSuperClassesMethods(pw);
        //}
        
        
        int quant = 0;
        for (MethodDec publicMethod : this.publicMethodsAllPrint) 
        {            
            //if (this.hasSuper) 
            //    quant++;
            
            if(quant == 0)
                pw.println("( void (*)() ) _"+publicMethod.getKra().getCname()+"_"+publicMethod.getName());
            else
            {
                pw.print(",");
                pw.println("( void (*)() ) _"+publicMethod.getKra().getCname()+"_"+publicMethod.getName());
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
    
    
    public int findOccurrences( MethodDec me )
    {
        int count=0;
        for (MethodDec publicMethodsAllPrint1 : this.publicMethodsAllPrint) {
            if (publicMethodsAllPrint1.getName().equals(me.getName())) {
                count++;
            }
        }
        
        return count;
    }
    
    
    void printSuperClassesInstanceVariables(PW pw)
    {
        KraClass kraAux = this.superclass;
        int quant=0;
        do
        {
            if( !kraAux.getInstanceVariableList().isEmpty() )
            {
                for (InstanceVariable instance : kraAux.getInstanceVariableList()) 
                {
                    pw.printIdent( instance.getType().getName() );
                    pw.printIdent(" _" + kraAux.getCname() + "_"+ instance.getName() +";");
                    pw.printlnIdent("");     
                }
            }
            
            
            if(kraAux.hasSuper)     kraAux = kraAux.superclass;
            else    break;
            
        }while(true);
    }
    
    
    void printSuperClassesMethods(PW pw)
    {
        KraClass kraAux = this.superclass;
        int quant=0;
        do
        {
            if( !kraAux.getPublicMethods().isEmpty() )
                for (MethodDec publicMethod : kraAux.getPublicMethods()) 
                {
                    System.out.println("Nome do METODO == === = = == == ==="+publicMethod.getName());
                    if(publicMethod.getIsFinal() == null)
                    {
                        
                        //if(findOccurrences(publicMethod) == 1 )
                        //{
                            if(quant == 0)
                                pw.printlnIdent("( void (*)() ) _"+kraAux.getName()+"_"+publicMethod.getName());
                            else
                            {
                                pw.print(",");
                                pw.printlnIdent("( void (*)() ) _"+kraAux.getName()+"_"+publicMethod.getName());
                            }
                            quant++;
                        //}
                    }
                }
            
            if(kraAux.hasSuper)     kraAux = kraAux.superclass;
            else    break;
            
        }while(true);
    }
    
    void addCurrentClassMethods()
    {
    
        for (MethodDec publicMethod : this.publicMethods) 
        {
            //publicMethodsAllPrint.add( publicMethod );
            boolean found = false;
            for(int i=0; i < this.publicMethodsAllPrint.size(); i++)
            {
                if(this.publicMethodsAllPrint.get(i).getName().equals(publicMethod.getName()))
                {
                    found = true;
                    this.publicMethodsAllPrint.remove(i);
                    this.publicMethodsAllPrint.add(i, publicMethod);
                }
            }
            
            if( !found ) 
            {
                this.publicMethodsAllPrint.add(publicMethod);
            }
        }
    }
    
    void addSuperClassesMethods()
    {
        KraClass kraAux = this.superclass;
        int quant=0;
        do
        {
            if( !kraAux.getPublicMethods().isEmpty() )
                for (MethodDec publicMethod : kraAux.getPublicMethods()) 
                {
                    if(publicMethod.getIsFinal() == null){
                        publicMethodsAllPrint.add( publicMethod );
                    }
                }
            
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
