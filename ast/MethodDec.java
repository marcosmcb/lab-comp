/*
Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024
*/

package ast;

import java.util.ArrayList;
import lexer.Symbol;


public class MethodDec {

    public MethodDec(String name, Type type, Symbol qualifier) {
        this.name = name;
        this.type = type;
        this.qualifier = qualifier;
        this.qualifierStatic = null;
        this.paramlist = null;
        this.statementlist = null;
    }

    private String name;
    private Type type;
    private Symbol qualifier;
    private Symbol qualifierStatic;
    private ParamList paramlist;
    private StatementL statementlist;
    private KraClass myClass;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public Type getKra() {
        return myClass;
    }
    
    public void setClass(KraClass kc) {
        this.myClass = kc;
    }
	
    public void setType(Type type) {
        this.type = type;
    }

    public Symbol getQualifier() {
        return qualifier;
    }

    public void setQualifier(Symbol qualifier) {
        this.qualifier = qualifier;
    }
    
     public Symbol getQualifierStatic() {
        return qualifierStatic;
    }

    public void setQualifierStatic(Symbol qualifierStatic) {
        this.qualifierStatic = qualifierStatic;
    }
    
        private Symbol isFinal;

    public Symbol getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(Symbol isFinal) {
        this.isFinal = isFinal;
    }


    public ParamList getPl() {
        return paramlist;
    }

    public void setPl(ParamList pl) {
        this.paramlist = pl;
    }

    public StatementL getSl() {
        return statementlist;
    }

    public void setSl(StatementL sl) {
        this.statementlist = sl;
    }

    void genKrakatoa(PW pw) {
                
        pw.printIdent(this.qualifier.toString());
        
        pw.printIdent(" "+ this.type.getName() + " " + this.getName() + " (");

        if (this.paramlist.getSize() > 0) {
            this.paramlist.genKrakatoa(pw);
        }
        
        pw.println(") { ");
        
       this.statementlist.genKrakatoa(pw);
        
        pw.println("}");
        
    }

    void genC(PW pw) {
        
        pw.printIdent(" "+ this.type.getName() + " " + "_" + this.getKra().getName()+ "_"+this.getName() + " (");
        
        if (this.qualifierStatic == null)    
            pw.print("_class_" + this.getKra().getName()+ " *this");
        
        if (this.paramlist.getSize()>0){
            pw.print(", ");
            this.paramlist.genC(pw);
        }
        
        pw.println(") { ");
        
        if(statementlist != null)
            this.statementlist.genC(pw);
        
        pw.println("}");

    }
}
