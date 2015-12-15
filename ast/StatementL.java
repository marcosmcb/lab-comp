/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/


package ast;

import java.util.ArrayList;


public class StatementL {
    
    public StatementL(ArrayList<Statement> list) {
        this.list = list;
    }
    
    
    public void genC(PW pw){
        
        if(!this.list.isEmpty())
            for (Statement list1 : this.list)  
                list1.genC(pw);  
        
    }
    
    //StatementList ::= { Statement }
    public void genKrakatoa(PW pw) {
        
        if(!this.list.isEmpty()){
            for (Statement list1 : this.list) {
                list1.genkra(pw);
            }
        }
    }
    
    private ArrayList<Statement> list = new ArrayList<>();
    
}
