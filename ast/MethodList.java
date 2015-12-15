/*
Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024
*/


package ast;

import java.util.ArrayList;


public class MethodList {

    private ArrayList<MethodDec> methodsList = new ArrayList<>();

    public MethodList() {
        this.methodsList = new ArrayList<>();
    }

    
    public ArrayList<MethodDec> getMethodDecList() {
        return methodsList;
    }

    public void addMethod(MethodDec methodDec) {
        methodsList.add(methodDec);
    }

    public void genKrakatoa(PW pw) {
        for (MethodDec m : methodsList) {
            m.genKrakatoa(pw);
        }
    }
}
