/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<KraClass> classList, ArrayList<MetaobjectCall> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}


	
        public void genKra(PW pw) {
            
            for (KraClass classList1 : classList)   classList1.genKrakatoa(pw);
            
        }
        
        /*
        genC de Program da ASA deve gerar a fun ̧c~ao main exatamente como abaixo. int main() {
            _class_Program *program;
           crie objeto da classe Program e envie a mensagem run para ele. Nem sempre o n ́umero de run no vetor  ́e 0.
            program = new_Program();
            ( ( void (*)(_class_Program *) ) program->vt[0] )(program);
            return 0;
          }

        
        */
        
	public void genC(PW pw) {
            pw.println("#include <stdio.h>");
            pw.println("#include <stdlib.h>");
            pw.println("");

            pw.println("typedef int boolean;");
            pw.println("#define true 1");
            pw.println("#define false 0");
            pw.println("");
            pw.println("typedef void(*Func)();");
            pw.println("");
            for (KraClass classList1 : classList)   
                classList1.genC(pw);
            
            pw.println("int main() {");
            
            pw.printlnIdent("_class_Program *program;");
            pw.printlnIdent("program = new_Program();");
            
            pw.printIdent("( ( void (*)(_class_Program *) ) program->vt[");
            
            pw.print(Integer.toString( getIndexRunMethod() ));
            
            pw.print("]) (program);");
             pw.println("");
            pw.printlnIdent("return 0;");
            pw.println("}");
            
          
	}
        
        public int getIndexRunMethod()
        {
            for (KraClass classList1 : classList)   
            {
                if(classList1.getName().equals("Program"))
                    for(int i=0; i < classList1.getPublicMethods().size() ; i++)
                        if(classList1.getPublicMethods().get(i).getName().equals("run"))
                            return i;                            
            }
            
            return -1;
        }
	
	public ArrayList<KraClass> getClassList() {
		return classList;
	}


	public ArrayList<MetaobjectCall> getMetaobjectCallList() {
		return metaobjectCallList;
	}
	

	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}

	
	private ArrayList<KraClass> classList;
	private ArrayList<MetaobjectCall> metaobjectCallList;
	
	ArrayList<CompilationError> compilationErrorList;

	
}