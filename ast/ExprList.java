/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/



package ast;

import java.util.*;

public class ExprList {

    public ExprList() {
        exprList = new ArrayList<Expr>();
    }
    /*Renato inseriu pra retornar um iterador pra lista de Expr*/
    public Iterator<Expr> elements() {
        return exprList.iterator();
    }
    
    public boolean addElement( Expr expr ) {
        
        return exprList.add(expr);
    }
    
    public int getSize(){
        return exprList.size();
    }
    
    public void genCOnlyType( PW pw ) {

        int size = exprList.size();
        for ( Expr e : exprList ) 
        {
            pw.print(e.getType().getName());
            //e.genC(pw, false);
            
            if ( --size > 0 )   pw.print(", ");
        }
    }
    
    public void genC( PW pw ) {

        int size = exprList.size();
        for ( Expr e : exprList ) {
        	e.genC(pw, false);
            if ( --size > 0 )
                pw.print(", ");
        }
    }
    
    public void genkra( PW pw ) {

        int size = this.exprList.size();
        for ( Expr e : exprList ) {
        	e.genKra(pw, false);
            if ( --size > 0 )
                pw.print(", ");
        }
    }
    
    private ArrayList<Expr> exprList;

    public ArrayList<Expr> getExprList() {
        return exprList;
    }
    
    public Expr getAt(int i){
    
        if(exprList.get(i) != null) return exprList.get(i);
        else return null;
    }

    public void setExprList(ArrayList<Expr> exprList) {
        this.exprList = exprList;
    }

}
