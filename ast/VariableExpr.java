/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v) {
        this.v = v;
        //this.isThereNew = isThereNew;
    }

    public Variable getV() {
        return v;
    }

    public void setV(Variable v) {
        this.v = v;
    }
    
    @Override
    public void genC( PW pw, boolean putParenthesis ) {
        
        //if(this.isThereNew == false)   
            if(v.getType().getName().equals(v.getName())){
                pw.print("new_");   
                pw.print(v.getName() );
                pw.print("()");
            }else{
                 pw.print("_"+v.getName() );
            }
        
        //else    pw.print("new_"+v.getType().getName()+"()");
        
    }
    
    @Override
    public Type getType() {         
        return v.getType();
    }

    
    private Variable v;




    public boolean isIsThereNew() {
        return isThereNew;
    }

    public void setIsThereNew(boolean isThereNew) {
        this.isThereNew = isThereNew;
    }

    
    @Override
    public void genKra(PW pw, boolean putParenthesis) {
        pw.print( v.getName() ); 
    }
    
    
    
    private boolean isThereNew = false;
    
}