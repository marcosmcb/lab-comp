/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/



package ast;

import java.util.*;

public class LocalVariableList extends Statement{

    public LocalVariableList() {
       localList = new ArrayList<Variable>();
    }

    public void addElement(Variable v) {
       localList.add(v);
    }

    public Iterator<Variable> elements() {
        return localList.iterator();
    }

    public int getSize() {
        return localList.size();
    }

    private ArrayList<Variable> localList;

    @Override
    public void genC(PW pw) {
       if(!this.localList.isEmpty()){
            for (int i=0; i < this.localList.size(); i++) {
                this.localList.get(i).genC(pw);
                
                if(i < this.localList.size()-1){
                    pw.print(",");
                }   
            }
            pw.print(";");
            pw.println("");
        }
    
    }

    @Override
    public void genkra(PW pw) {
        
        if(!this.localList.isEmpty()){
            for (int i=0; i < this.localList.size(); i++) {
                this.localList.get(i).genKra(pw);
                
                if(i < this.localList.size()-1){
                    pw.print(",");
                }   
            }
            pw.print(";");
            pw.println("");
        }
    }

    @Override
    public String getStatementType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
