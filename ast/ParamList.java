/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

import java.util.*;

public class ParamList {

    public ParamList() {
       paramList = new ArrayList<>();
    }

    public void addElement(Variable v) {
       paramList.add(v);
    }

    public Iterator<Variable> elements() {
        return paramList.iterator();
    }

    public int getSize() {
        return paramList.size();
    }
    
    public ArrayList<Variable> getList() {
        return paramList;
    }

    private ArrayList<Variable> paramList ;

    void genKrakatoa(PW pw) {
        
        if(!this.getList().isEmpty()){
            
            for (Variable paramList1 : this.paramList) {
                paramList1.genKra(pw);
            }
        }
        
    }

    void genC(PW pw) {
        
        if(!this.getList().isEmpty())
            for (Variable var1 : this.paramList) 
                var1.genC(pw);
        
    }
    
    void genCNameVar(PW pw) {
        
        if(!this.getList().isEmpty())
            for (Variable var1 : this.paramList) 
                var1.genCReadWrite(pw);
        
    }
    

}
