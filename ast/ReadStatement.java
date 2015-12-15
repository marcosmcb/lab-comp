/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/

package ast;

import java.util.ArrayList;

public class ReadStatement extends Statement {

    public ReadStatement(ArrayList<Variable> varList) {
        this.variableList = varList;
    }


    @Override
    public void genC(PW pw) {
        
        if(!this.variableList.isEmpty())
        {
            /*
            {
                char __s[512];
                gets(__s);
                sscanf(__s, "%d", &_b);
            }
            */
            pw.println("{");
            pw.printIdent("char ");
            
            for (int i=0; i < this.variableList.size(); i++) 
            { 
                pw.print("_");
                this.variableList.get(i).genCReadWrite(pw);
                pw.print("[512]");
                
                if ( i < this.variableList.size() - 1 )     pw.print(", ");  
            }
            
            pw.print(";");
            pw.println("");
            pw.println("");


            
            for (int i=0; i < this.variableList.size(); i++) { 
                pw.printIdent("gets(_");
                this.variableList.get(i).genCReadWrite(pw);
                pw.print(");");
                pw.println("");

                
                /* sscanf(__s, "%d", &_b); */
                pw.printIdent("sscanf(_");
                this.variableList.get(i).genCReadWrite(pw);
                pw.print(", \"");
                
                if( this.variableList.get(i).getType() == Type.intType ) pw.print("%d");
                
                else if( this.variableList.get(i).getType() == Type.stringType ) pw.print("%s");
                                
                pw.print("\", &");
                
                this.variableList.get(i).genCReadWrite(pw);
                
                pw.print(");");
                pw.println("");
                pw.println("");
                
            }

            pw.print("}");
            pw.println("");

        }
       
    }

    private ArrayList<Variable> variableList;

    @Override
    public void genkra(PW pw) {
    pw.print("read ( ");
        for (int i=0; i < this.variableList.size(); i++) {
            this.variableList.get(i).genKraReadWrite(pw);
            if ( i < this.variableList.size()-1 ) {
                pw.print(", ");
            }
        }
        pw.print(" );");
        pw.println("");
    }

    @Override
    public String getStatementType() {
        return "read";
    }

}
