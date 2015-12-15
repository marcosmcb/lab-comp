/*

Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024



*/
package ast;

public class Variable {

    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }

    public Type getType() {
        return type;
    }

    private String name;
    private Type type;

    void genC(PW pw){
        if(this.type != Type.booleanType && this.type != Type.intType && this.type != Type.stringType){
            pw.print("_class_"+this.type.getName()+" "+ "*_"+this.getName());


        }else{
            pw.print(this.type.getName()+" "+ "_"+this.getName());

        }
    }
    
    void genCReadWrite(PW pw){
        pw.print("_"+this.getName());
    }
    
    void genKra(PW pw) {
        
        pw.print(this.type.getName()+ " "+this.getName());

        pw.print(this.type.getName()+" "+this.getName());

    }
    
    void genKraReadWrite(PW pw) {
        pw.print(this.getName());
    }
    
}