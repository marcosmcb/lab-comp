/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/





package ast;


public class IfStatement extends Statement {
    
    public IfStatement(Expr e, Statement s1, Statement s2) {
        this.exp = e;
        this.st = s1;
        this.optional = s2;
    }

    @Override
    public void genC(PW pw) {    
        
        pw.print("if ( ");
        exp.genC(pw, false);
        pw.println(" ){");
        if(st!=null){
        System.out.println(" statement genc dentro do if");
            st.genC(pw);
        }else{
             System.out.println(" statement genc dentro do nulll");
        }
         pw.println("}");
         pw.println("");
        if (optional != null) {
            pw.print("else{");
            optional.genC(pw);
             pw.println("}");
             pw.println("");
        }  
    }
    
    
    private Expr exp;
    private Statement st;
    private Statement optional;

    @Override
    public void genkra(PW pw) {
      pw.print("if ( ");
        exp.genKra(pw, true);
        pw.println(" )");
        if(this.st!=null)
        st.genkra(pw);
        if (optional != null) {
            pw.print("else");
            optional.genkra(pw);
        }  
    
    }

    @Override
    public String getStatementType() {
        return "if";
    }
}
