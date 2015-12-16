/*
Nomes: Marcos Cavalcante Barboza    RA 408336
       Renato Angelo Poulicer       RA 380024





*/



package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {

	// compile must receive an input with an character less than
	// p_input.lenght
    
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new SignalError(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}
        
        /*
        Program ::= { MOCall } ClassDec { ClassDec }
        MOCall ::= “@” Id [ “(” { MOParam } “)” ]
        ClassDec ::= “class” Id [ “extends” Id ] “{” MemberList “}”
        
        //Program ::= KraClass { KraClass }
        */

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
		ArrayList<KraClass> kraClassList = new ArrayList<>();
		Program program = new Program(kraClassList, metaobjectCallList, compilationErrorList);
                
                
		try 
                {
                    while ( lexer.token == Symbol.MOCall )  
                        metaobjectCallList.add(metaobjectCall());

                    kraClassList.add(classDec());
                     
                    while ( lexer.token == Symbol.CLASS || lexer.token == Symbol.FINAL )    
                        kraClassList.add(classDec());
                    

                    if(symbolTable.getInGlobal("Program") == null)  
                        signalError.show("Source code without a class '"+"Program"+"'");
                    

                    if ( lexer.token != Symbol.EOF )                
                        signalError.show("End of file expected");
                    
		}
		catch( RuntimeException e) 
                {
                    
                    for(CompilationError er : signalError.getCompilationErrorList())    
                        System.out.println(er.getMessage());
                    //e.printStackTrace(); // caso pegue um erro geral, diferente dos emitidos pelo compilador 
                }
		return program;
	}

	/**  parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
     * <code>
     * @ce(5, "'class' expected") <br>
     * clas Program <br>
     *     public void run() { } <br>
     * end <br>
     * </code>
     * 
	   
	 */
        
        /*
            MOCall ::= “@” Id [ “(” { MOParam } “)” ]
            MOParam ::= IntValue | StringValue | Id
            Id ::= Letter{Letter|Digit|“”}
        */
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {
                ArrayList<Object> metaobjectParamList = new ArrayList<>();
		String name = lexer.getMetaobjectName();

                lexer.nextToken();
		
                if ( lexer.token == Symbol.LEFTPAR ) 
                {
			// metaobject call with parameters
			lexer.nextToken();
			
                        while ( lexer.token == Symbol.LITERALINT || lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT ) 
                        {
				
                            switch ( lexer.token ) 
                            {
                                case LITERALINT:
                                    metaobjectParamList.add(lexer.getNumberValue());
                                    break;
                                case LITERALSTRING:
                                    metaobjectParamList.add(lexer.getLiteralStringValue());
                                    break;
                                case IDENT:
                                    metaobjectParamList.add(lexer.getStringValue());
                            }
                            
                            lexer.nextToken();

                            if ( lexer.token == Symbol.COMMA )      
                                lexer.nextToken();
                            
                            else    
                                break;
                                
			}
                        
			if ( lexer.token != Symbol.RIGHTPAR )       
                            signalError.show("')' expected after metaobject call with parameters");
                        
                        else        
                            lexer.nextToken();
		}
                
		if ( name.equals("nce") ) 
                {
                    if ( metaobjectParamList.size() != 0 )  signalError.show("Metaobject 'nce' does not take parameters");
		}
		else if ( name.equals("ce") ) 
                {
                    if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
                        signalError.show("Metaobject 'ce' take three or four parameters");

                    if ( !( metaobjectParamList.get(0) instanceof Integer)  )
                        signalError.show("The first parameter of metaobject 'ce' should be an integer number");

                    if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
                       signalError.show("The second and third parameters of metaobject 'ce' should be literal strings");

                    if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )  
                        signalError.show("The fourth parameter of metaobject 'ce' should be a literal string");
		}
			
		return new MetaobjectCall(name, metaobjectParamList);
	}
        /*
            ClassDec ::= “class” Id [ “extends” Id ] “{” MemberList “}” 
            MemberList ::= { Qualifier Member }
            Qualifier ::= [ “final” ] [ “static” ] ( “private” | “public”) 
            Member ::= InstVarDec | MethodDec
            InstVarDec ::= Type IdList “;”
            MethodDec ::= Type Id “(” [ FormalParamDec ] “)” “{” StatementList “}”

        */
        // Note que os m�todos desta classe n�o correspondem exatamente �s
            // regras
            // da gram�tica. Este m�todo classDec, por exemplo, implementa
            // a produ��o KraClass (veja abaixo) e partes de outras produ��es.

            /*
             * KraClass ::= ``class'' Id [ ``extends'' Id ] "{" MemberList "}"
             * MemberList ::= { Qualifier Member } 
             * Member ::= InstVarDec | MethodDec
             * InstVarDec ::= Type IdList ";" 
             * MethodDec ::= Qualifier Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 
             * Qualifier ::= [ "static" ]  ( "private" | "public" )
             */
	private KraClass classDec() {
            KraClass myKraClass;
            
            this.currentKRA = null;
            
            boolean isFinal = false, qualifierPrivateOrPublic = false;
            Symbol op = null, qualifierStatic = null, qualifierFinal=null;
            
            if (lexer.token == Symbol.PUBLIC || lexer.token == Symbol.PRIVATE)
            {
                op = lexer.token;
                qualifierPrivateOrPublic = true;
                lexer.nextToken();
            }
            
            if( lexer.token == Symbol.FINAL )
            {
                isFinal = true;
                lexer.nextToken();
            }
            
            if ( lexer.token != Symbol.CLASS )  signalError.show("'class' expected");

            lexer.nextToken();
            String className = lexer.getStringValue();
            
            System.out.println("Nome da Classe em KraClass ["+className+"]");

            if ( lexer.token != Symbol.IDENT && lexer.existKey(className) )     signalError.show("Identifier expected");
             
            myKraClass = new KraClass(className);
            this.currentKRA = myKraClass;
            myKraClass.setIsFinal(isFinal);
            
            if(qualifierPrivateOrPublic)    myKraClass.setQualifier(op.name());
            
            
            /* Nos colocamos na nossa tabela de variáveis globais o nome do IDENT (leia-se - classe) encontrado */
            symbolTable.putInGlobal(className, myKraClass);
         
            lexer.nextToken();
            
            if ( lexer.token == Symbol.EXTENDS ) 
            {
                lexer.nextToken();

                if(lexer.getStringValue().equals(Type.booleanType.getCname()) ||
                   lexer.getStringValue().equals(Type.intType.getCname()) ||
                   lexer.getStringValue().equals(Type.stringType.getCname()) ||
                   lexer.getStringValue().equals(Type.voidType.getCname()) 
                   )
                {
                    signalError.show("Class expected");
                }

                if ( lexer.token != Symbol.IDENT )  signalError.show("Identifier Expected");
                

                if( !lexer.getStringValue().equals(className) )
                {
                    
                    String superclassName = lexer.getStringValue();
                    
                    KraClass superClassAux = symbolTable.getInGlobal(superclassName);
                    
                    if(superClassAux != null && !superClassAux.getIsFinal() )
                    {
                        myKraClass.setSuperclass(superClassAux);
                        //if the class inherits from some other class, we set true
                        myKraClass.setHasSuper(true);
                    }else
                    {
                        signalError.show("Class '"+myKraClass.getName()+"' is inheriting from final class '"+superClassAux.getName()+"'");
                    }
                    
                }else
                {
                    signalError.show("Class '"+ className + "' is inheriting from itself");
                }
                
                

                lexer.nextToken();
            }else
            {
                //case the class inherits from some other class
                myKraClass.setHasSuper(false);
            }

            if ( lexer.token != Symbol.LEFTCURBRACKET )     signalError.show("{ expected", true);
            

            lexer.nextToken();
            
            if(lexer.token == Symbol.STATIC){
                qualifierStatic = Symbol.STATIC;
                lexer.nextToken();
            }
            else if(lexer.token == Symbol.FINAL){
                qualifierFinal = Symbol.FINAL;
                lexer.nextToken();
            }
                
            while (lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {

                Symbol qualifier;
                
                switch (lexer.token) {

                    case PRIVATE:
                            lexer.nextToken();
                            qualifier = Symbol.PRIVATE;
                            break;
                        
                    case PUBLIC:
                            lexer.nextToken();
                            qualifier = Symbol.PUBLIC;
                            break;
                   
                    default:
                            signalError.show("private, or public expected");
                            qualifier = Symbol.PUBLIC;
                }
                
                //public type variable
                Type t = type();

                if ( lexer.token != Symbol.IDENT ){
                    signalError.show("Identifier expected");
                }

                String name = lexer.getStringValue();

                lexer.nextToken();
                
                if ( lexer.token == Symbol.LEFTPAR ){
                    //case there is a parenthesis, it is a method
                    if (qualifier == Symbol.PRIVATE){
                        MethodDec myPrivateMethod = methodDec(className, t, name, qualifier, qualifierStatic, qualifierFinal);
                        myPrivateMethod.setClass(myKraClass);
			//	myPrivateMethod.setFinal(isFinal);  
                        myKraClass.addMethodToPrivateList(myPrivateMethod);
                        
                    }
                   
                    if (qualifier == Symbol.PUBLIC){
                        MethodDec myPublicMethod = methodDec(className, t, name, qualifier, qualifierStatic, qualifierFinal);
                         myPublicMethod.setClass(myKraClass);
                        myKraClass.addMethodToPublicList(myPublicMethod);
                    }
                   
                }else if ( qualifier != Symbol.PRIVATE ){
                    
                    signalError.show("Attempt to declare a public instance variable");
                    
                }else{
                    //it is therefore a (class variable)
                    myKraClass.setInstanceVariableList(instanceVarDec(t, name, qualifierStatic));
                }
                
                if(lexer.token == Symbol.STATIC){
                    qualifierStatic = Symbol.STATIC;
                    qualifierFinal = null;
                    lexer.nextToken();
                }else if(lexer.token == Symbol.FINAL){
                    qualifierFinal = Symbol.FINAL;
                    qualifierStatic = null;
                    lexer.nextToken();
                }else{
                    qualifierFinal = null;
                    qualifierStatic = null;
                }
                

            }
            
            if(myKraClass.getHasSuper()){
                checkMethodSuper(myKraClass);
            }
                    
            if ( lexer.token != Symbol.RIGHTCURBRACKET ){
                    signalError.show("public/private or \"}\" expected");
            }

            lexer.nextToken();
            
            return myKraClass;
	}
        
        private void checkMethodSuper(KraClass subClass){
            boolean checkParamTypes = false;
            KraClass auxSuper = symbolTable.getInGlobal( subClass.getSuperclass().getName());
            
            //Only publicMethods can be inherited
            if(auxSuper!=null)
            if( auxSuper.getPublicMethods() != null && subClass.getPublicMethods() != null  ){
                
                for (MethodDec publicMethod : auxSuper.getPublicMethods()) {    
                    //search for the method in the SubCLass
                    for (MethodDec publicSubMethod : subClass.getPublicMethods()) {
                        
                        //check to see if the methods have the same name
                        if(publicMethod.getName().equals(publicSubMethod.getName())){
                            
                            //check to see if the method is final
                            if(publicMethod.getIsFinal() == null){

                                //check to see if the methods have the same type
                                if(publicMethod.getType() == publicSubMethod.getType() ){
                                    
                                    if (publicMethod.getPl() != null && publicSubMethod.getPl() != null){
                                        if(publicMethod.getPl().getSize() == publicSubMethod.getPl().getSize()){
                                            for( int k=0; k < publicMethod.getPl().getSize()  && k < publicSubMethod.getPl().getSize() ; k++){
                                                if(publicMethod.getPl().getList().get(k).getType() != publicSubMethod.getPl().getList().get(k).getType()){
                                                    checkParamTypes = true;
                                                }
                                            }
                                        }else{
                                            signalError.show("Method '"+ publicSubMethod.getName()+"' of the subclass '"+subClass.getName()+"' has a signature different from the same method of superclass '"+auxSuper.getName()+"'");
                                        }
                                    }else{
                                        if ( (publicMethod.getPl() == null && publicSubMethod.getPl() != null)
                                            || (publicMethod.getPl() != null && publicSubMethod.getPl() == null)

                                            )
                                        signalError.show("Method '"+ publicSubMethod.getName()+"' of the subclass '"+subClass.getName()+"' has a signature different from the same method of superclass '"+auxSuper.getName()+"'");

                                    }

                                    if(checkParamTypes){
                                        signalError.show("Method '"+ publicSubMethod.getName()+"' is being redefined in subclass '"+subClass.getName()+"' with a signature different from the method of superclass '"+auxSuper.getName()+"'");
                                    }  

                                }else{
                                    signalError.show("Method '"+publicSubMethod.getName()+"' of subclass '"+subClass.getName()+"' has a signature different from method inherited from superclass '"+auxSuper.getName()+"'");
                                }
                            }else{
                                signalError.show("Redeclaration of final method '"+publicMethod.getName()+ "'");
                            }
                        }    
                    }    
                }
            }
        }
        /*
            InstVarDec ::= Type IdList “;”
            Type ::= BasicType | Id
            IdList ::= Id{“,”Id}
            Id ::= Letter{Letter|Digit|“”}
            InstVarDec ::= [ "static" ] "private" Type IdList ";"
        */

	private ArrayList<InstanceVariable> instanceVarDec(Type type, String name, Symbol qualifierStatic) {
            ArrayList<InstanceVariable> myInstanceList = new ArrayList<>();
            Type myType = type;
            Variable myVar;
            boolean found = false;
            
            
            
            //We still gotta check whether the variable is part of our instances variable or not,
            //if it is from a different class, thats ok the declaration
             
            for (InstanceVariable myInstanceList1 : myInstanceList) {
                if (myInstanceList1.getName().equals(name)) {
                    signalError.show("Variable '"+ name +"' is being redeclared");
                    found = true;
                }
            }
            
            if(found==false){
                for(int j=0; j < this.currentKRA.getInstanceVariableList().size(); j++){
                    if(this.currentKRA.getInstanceVariableList().get(j).getName().equals(name)){
                        signalError.show("Variable '"+ name +"' is being redeclared");
                        found = true;
                    }
                }
            }
            
            myInstanceList.add(new InstanceVariable(name, myType)  );
           
            
            while (lexer.token == Symbol.COMMA) {
                found = false;

                lexer.nextToken();

                if ( lexer.token != Symbol.IDENT )  signalError.show("Identifier expected");
                
                
                name = lexer.getStringValue();
                                
                for (InstanceVariable myInstanceList1 : myInstanceList) {
                    if (myInstanceList1.getName().equals(name)) {
                        signalError.show("Variable '"+ name +"' is being redeclared");
                        found = true;
                    }
                }
                
                if(found==false){
                    for(int j=0; j < this.currentKRA.getInstanceVariableList().size(); j++){
                        if(this.currentKRA.getInstanceVariableList().get(j).getName().equals(name)){
                            signalError.show("Variable '"+ name +"' is being redeclared");
                            found = true;
                        }     
                    }
                }
                
                myInstanceList.add(new InstanceVariable(name, myType)  );
                   
                lexer.nextToken();
            }

            if ( lexer.token != Symbol.SEMICOLON )  signalError.show(SignalError.semicolon_expected);

            lexer.nextToken();
            
            return myInstanceList;
	}
        
        
        /*
            MethodDec ::= Type Id “(” [ FormalParamDec ] “)” “{” StatementList “}”
            Type ::= BasicType | Id
            Id ::= Letter{Letter|Digit|“”}
            FormalParamDec ::= ParamDec { “,” ParamDec } 
            ParamDec ::= Type Id
            StatementList ::= { Statement }
            Statement ::= AssignExprLocalDec “;” | 
			  IfStat | 
			  WhileStat | 
			  ReturnStat “;” |
			  ReadStat “;” | 
			  WriteStat “;” | 
			  “break” “;” | 
			  “;” | 
			  CompStatement 
        

            MethodDec ::= Qualifier Return Id "("[ FormalParamDec ] ")" "{"
                          StatementList "}"
        */
	private MethodDec methodDec(String className, Type type, String name, Symbol qualifier, Symbol qualifierStatic, Symbol qualifierFinal) {
            MethodDec myMethod = null;
            ParamList myParamList = new ParamList();
            
            KraClass myKraClass = symbolTable.getInGlobal(className);

            ArrayList<MethodDec> privatePublicJoined = new ArrayList<>(myKraClass.getPrivateMethods());
            privatePublicJoined.addAll(myKraClass.getPublicMethods());
            
            
            if(name.equals("run") && qualifierStatic!=null){
                signalError.show("Method 'run' cannot be static");
            }
            
            if( qualifierFinal != null && myKraClass.getIsFinal() ){
                signalError.show("'final' method in a 'final' class");
            }
            
            if(privatePublicJoined != null){
                for (MethodDec privatePublicJoined1 : privatePublicJoined) {
                    if(privatePublicJoined1.getName().equals(name) && privatePublicJoined1.getType() == type 
                       && privatePublicJoined1.getQualifier() == qualifier){
                        
                        //If none of the static qualifiers are static, it is because noth are statics
                        if((privatePublicJoined1.getQualifierStatic() != null) && (qualifierStatic != null)){
                            signalError.show("Redefinition of static method '"+name+"'");
                        }else{ 
                            signalError.show("Method '" + name + "' is being redeclared");  
                        }
                    }
                }
            }

            if(myKraClass.getInstanceVariableList()  != null){
                for(int k=0; k < myKraClass.getInstanceVariableList().size(); k++){
                    if( myKraClass.getInstanceVariableList().get(k).getName().equals(name) ){
                        signalError.show("Method '" + name + "' has name equal to an instance variable");
                    }
                }
                
            }
            
            
            if(myKraClass.getPrivateMethods() != null){
                if (qualifier == Symbol.PUBLIC) {                    
                    for(MethodDec methodDecPrivate : myKraClass.getPrivateMethods()){
                        if(methodDecPrivate.getName().equals(name)){
                            signalError.show("Method '" + name  + "' is being redeclared");
                        }
                    }
                }
            }
            
            if(myKraClass.getPublicMethods()!= null){
                if (qualifier == Symbol.PRIVATE) {
                    for(MethodDec methodDecPublic : myKraClass.getPublicMethods()){
                        if(methodDecPublic.getName().equals(name)){
                            signalError.show("Method '" + name + "' is being redeclared");        
                        }
                    }
                }
            }
               
            
            
            
            if(name.equals("run") && !type.getName().equals("void")){
                signalError.show("Method 'run' of class 'Program' with a return value type different from 'void'");
            }
            
            if(name.equals("run") && (qualifier != Symbol.PUBLIC) && className.equals("Program")){
                signalError.show("Method 'run' of class 'Program' cannot be private");
            }
            
            lexer.nextToken();
            
            if ( lexer.token != Symbol.RIGHTPAR ){
                myParamList = formalParamDec();
            }
            
            if(name.equals("run") && className.equals("Program") && !myParamList.getList().isEmpty() ){
                signalError.show("Method 'run' of class 'Program' cannot take parameters");
            }
            
            myMethod = new MethodDec(name, type, qualifier);

            myMethod.setPl(myParamList);
            myMethod.setQualifierStatic(qualifierStatic);
            myMethod.setIsFinal(qualifierFinal);
            
            this.currentMethod = myMethod;
           
            if ( lexer.token != Symbol.RIGHTPAR ){ 
                signalError.show(") expected");
            }

            lexer.nextToken();

            if ( lexer.token != Symbol.LEFTCURBRACKET ){
                signalError.show("{ expected");
            }

            lexer.nextToken();
                        
            ArrayList<Statement> myStatementArray = statementList();
            
            StatementL myStatementList =  new StatementL(myStatementArray);
            
            myMethod.setSl(myStatementList);
            
            symbolTable.removeLocalIdent();
            
            if((!type.getName().equals("void")) && (lexer.token != Symbol.RETURN )){
            //    signalError.show("Missing return statement");
            }

            if(type.getName().equals("void") && lexer.token.equals(lexer.token.RETURN)){
               signalError.show("Return inside a void method");
            }

            if ( lexer.token != Symbol.RIGHTCURBRACKET ){ 
                signalError.show("} expected");
            }

            lexer.nextToken();
            
            this.currentMethod = null;
            
            return myMethod;

	}
        
        /*
        LocalDec ::= Type IdList ";"
        */
	private LocalVariableList localDec() {
           
            LocalVariableList myLocalVarList = new LocalVariableList();
            
            Type type = type();                
            String typ = type.getName();
            
            if ( lexer.token != Symbol.IDENT ) {
                signalError.show("Identifier expected");
            }
                        
            Variable v = new Variable(lexer.getStringValue(), type);
            
            if( lexer.existKey(v.getName()) ){
                signalError.show("key word can´t be used as variable");
            }
             
            if(symbolTable.getInLocal(lexer.getStringValue()) != null){
                signalError.show("Variable '" + lexer.getStringValue() + "' is being redeclared");
            }else{
                symbolTable.putInLocal(lexer.getStringValue(), v);
                myLocalVarList.addElement(v);   
            }
            
            lexer.nextToken();

            while (lexer.token == Symbol.COMMA) {

                lexer.nextToken();

                if ( lexer.token != Symbol.IDENT ){
                    signalError.show("Identifier expected");
                }

                v = new Variable(lexer.getStringValue(), type);
                
                if(symbolTable.getInLocal(lexer.getStringValue()) != null){
                    signalError.show("Variable '" + lexer.getStringValue() + "' is being redeclared");
                }else{
                    symbolTable.putInLocal(lexer.getStringValue(), v);
                    myLocalVarList.addElement(v);
                }
                
                lexer.nextToken();
            }
            
            return myLocalVarList;
	}
        
        /*
        FormalParamDec ::= ParamDec { “,” ParamDec } 
        ParamDec ::= Type Id
        */
	private ParamList formalParamDec() {
            ParamList myParamList = new ParamList();
                        
            Variable myVar = (Variable) paramDec();
            
            System.out.println("Valor de Variable em PARAMLIST - Variable [" + myVar.getName() + "] e Tipo ["+myVar.getType().getName()+"]");

            myParamList.addElement(myVar);
            
            while (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
                myVar = (Variable) paramDec();
                
                System.out.println("Valor de Variable em PARAMLIST - Variable [" + myVar.getName() + "] e Tipo ["+myVar.getType().getName()+"]");

                
                for(int i=0; i < myParamList.getSize(); i++){
                    if(myParamList.getList().get(i) == myVar){
                        signalError.show("Parameter '" + myVar.getName() + "' was already declared");
                    }
                }
                
                myParamList.addElement(myVar);
            }
            
            return myParamList;
	}
        
        /*
        ParamDec ::= Type Id
        */
	private Parameter paramDec() {
            
            Type t = type();
                                   
            if(lexer.token == Symbol.COMMA){
                signalError.show("missing parameter");
            }
            
            if ( lexer.token != Symbol.IDENT ){ 
                signalError.show("Identifier expected");
            }
            
            String str = lexer.getStringValue();
            lexer.nextToken();
            
            Parameter myParam = new Parameter(str, t);
            
            symbolTable.putInLocal(str, myParam );
            
            return myParam;
        }


        /*
        Type ::= BasicType | Id
        BasicType ::= “void” | “int” | “boolean” | “String”
        Id ::= Letter{Letter|Digit|“”}
        */
	private Type type() {
		Type result;
                
		switch (lexer.token) {
                    case VOID:
                            result = Type.voidType;
                            break;
                    case INT:
                            result = Type.intType;
                            break;
                    case BOOLEAN:
                            result = Type.booleanType;
                            break;
                    case STRING:
                            result = Type.stringType;
                            break;
                    case IDENT:
                            // # corrija: fa�a uma busca na TS para buscar a classe
                            // IDENT deve ser uma classe.
                            result = symbolTable.getInGlobal( lexer.getStringValue() ); 
                            
                            if(result == null)  signalError.show(lexer.getStringValue() + "is not a type");
                            
                            break;
                    default:
                            signalError.show("Type expected");
                            result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;
	}

        
        /*
        It gets called by Statement
        */
	private Composite compositeStatement() {

            lexer.nextToken();
            ArrayList<Statement> myStatementList = statementList();
            
            if ( lexer.token != Symbol.RIGHTCURBRACKET )
                signalError.show("} expected");
            else
                lexer.nextToken();
            
           return  new Composite(myStatementList);     
                
	}
        
        /*
        StatementList ::= { Statement }
        Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” | ReadStat “;” | WriteStat “;” | “break” “;” | “;” | CompStatement 
        */
	private ArrayList<Statement> statementList() {
            ArrayList<Statement> myStatementList = new ArrayList<>();
      
            Symbol tk;
            // statements always begin with an identifier, if, read, write, ...
            while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET && tk != Symbol.ELSE){                
                Statement st =  statement();
                if(st != null)
                    myStatementList.add( st );
            }

            return myStatementList;
	}
        
        /*
        Statement ::= AssignExprLocalDec “;” | IfStat | WhileStat | ReturnStat “;” | ReadStat “;” | 
                      WriteStat “;” | “break” “;” | “;” | CompStatement 

        AssignExprLocalDec ::= Expression [ “=” Expression ] | LocalDec
        Expression ::= SimpleExpression [ Relation SimpleExpression ]
        LocalDec ::= Type IdList “;”

        SimpleExpression ::= Term { LowOperator Term }
        Term ::= SignalFactor { HighOperator SignalFactor }
        LowOperator ::= “+” | “−” | “||”
        
        Relation ::= “==” | “<” | “>” | “<=” | “>=” | “! =” 
        SignalFactor ::= [ Signal ] Factor
        Signal ::= “+” | “−”
        Factor ::= BasicValue | “(” Expression “)” | “!” Factor | “null” | ObjectCreation | PrimaryExpr
        HighOperator ::= “∗”|“/”|“&&”

        
        IfStat ::= “if ” “(” Expression “)” Statement [ “else” Statement ]
        WhileStat ::= “while” “(” Expression “)” Statement
        ReturnStat ::= “return” Expression
        ReadStat ::= “read” “(” LeftValue { “,” LeftValue } “)”
        WriteStat ::= “write” “(” ExpressionList “)”
        CompStatement ::= “{” { Statement } “}”
        */
	private Statement statement() {
            Statement myStatement = null;
                
		switch (lexer.token){
                    case THIS:
                    case IDENT:
                            //System.out.println("Valor de IDENT "+lexer.getStringValue());

                            myStatement = assignExprLocalDec();
                            break;
                    case SUPER:
                    case INT:  
                            myStatement = assignExprLocalDec();
                            break;
                    case BOOLEAN:
                    case STRING:
                            myStatement = assignExprLocalDec();
                            break;
                    case RETURN:
                            myStatement = (ReturnStatement) returnStatement();
                            break;
                    case READ:
                            myStatement = (ReadStatement) readStatement();
                            break;
                    case WRITE:
                            myStatement = writeStatement();
                            
                            System.out.println("teste Write");
                            
                            if(myStatement == null)
                                 System.out.println("WriteStatement is null");
                            
                            break;
                    case WRITELN:
                            myStatement = (WriteLnStatement) writelnStatement();
                            break;
                    case IF:
                            myStatement = (IfStatement) ifStatement();
                            break;
                    case BREAK:
                            
                            if(this.isInWhile == false){
                                signalError.show("'break' statement found outside a 'while' statement");
                            }
                            
                            myStatement = (BreakStatement) breakStatement();
                            break;
                    case WHILE:
                            this.isInWhile = false;
                            myStatement = (WhileStatement) whileStatement();
                            this.isInWhile = true;
                            break;
                    case SEMICOLON:
                            myStatement = (NullStatement) nullStatement();
                            break;
                    case LEFTCURBRACKET:
                            myStatement =  compositeStatement();
                            this.isInWhile = true;

                            break;
                    default:
                            signalError.show("Statement expected");
                            break;
		}
               
                return myStatement;
	}

	/*
	 * retorne true se 'name' � uma classe declarada anteriormente. � necess�rio
	 * fazer uma busca na tabela de s�mbolos para isto.
	 */
	private boolean isType(String name) {
            return this.symbolTable.getInGlobal(name) != null;
	}

	/*
	 *  AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec
            LocalDec ::= Type IdList “;”
            Expression ::= SimpleExpression [ Relation SimpleExpression ]

	 */
         /*
        * uma declara��o de vari�vel. 'lexer.token' � o tipo da vari�vel
        * 
        * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec 
        * LocalDec ::= Type IdList ``;''
        */
                
	private Statement assignExprLocalDec() {
            Statement ce = null;
            Expr leftie = null; 
            Expr rightie = null;
            
            if ( lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
                || lexer.token == Symbol.STRING ||
                // token � uma classe declarada textualmente antes desta
                // instru��o
                (lexer.token == Symbol.IDENT && isType(lexer.getStringValue())
                 
                ) ) {
                
               
                ce = localDec();
                lexer.nextToken();
                
           
               
            }
            
            else {
               
                /*
                 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ]
                 */
                String typeToken = lexer.getStringValue();
                               
                Expr left = expr();
               // if(left == null){
                   //System.out.println("jhfdhjfhbshfsjh"+typeToken);
            //    }
                if ( lexer.token == Symbol.ASSIGN ) {

                    Symbol op = lexer.token;

                    lexer.nextToken();

                    Expr right =  expr();
                    
                    AssignmentStatement as = new AssignmentStatement(left, right);
                    
                    ce = as;
                    
                    if( left != null && right != null){
                        if(left.getType()!=null)
                        if( isBasicType(left.getType().getName()) && (right.getType() == Type.undefinedType) ){
                            signalError.show("Type error: 'null' cannot be assigned to a variable of a basic type");
                        }
                        else if( !isBasicType(left.getType().getName()) && !isBasicType(right.getType().getName()) ){

                            if( left.getType() != right.getType()  ){
                                if(symbolTable.getInGlobal(right.getType().getName()) != null){
                                    if( !isSubClass(right.getType().getName(), left.getType().getName()) ){
                                        signalError.show("Type error: type of the right-hand side of the assignment is not a subclass of the left-hand side");
                                    }
                                }
                            }

                        }
                        else if(  isBasicType(left.getType().getName()) && !isBasicType(right.getType().getName()) ){
                            //signalError.show("Type error: type of the left-hand side of the assignment is a basic type and the type of the right-hand side is a class");
                        }
                        else if(left.getType() != null){
                            if(right.getType() != null){
                                if(left.getType() != right.getType()){
                                //    signalError.show("'" + right.getType().getName() + "' cannot be assigned to '" + left.getType().getName() + "'" );
                                }
                            }
                        }
                    }


                    if ( lexer.token != Symbol.SEMICOLON )
                        signalError.show("';' expected", true);
                    else
                        lexer.nextToken();
                }else if(lexer.token == Symbol.IDENT && !isType(typeToken)&& !typeToken.equals("int")&& !typeToken.equals("String")&& !typeToken.equals("boolean")){
                    signalError.show("type "+ typeToken + " was not found");
                }else{
                  ce = new MessageSendStatement((MessageSend) left);
                  
                   //if(left == null) 
                    //System.out.print("mimimi");
                lexer.nextToken();
                }      
            }
            return ce;
	}
        
        
        /*
        It gets called by factor
        */
	private ExprList realParameters() {
            ExprList anExprList = null;

            if ( lexer.token != Symbol.LEFTPAR ){
                signalError.show("( expected");
            }

            lexer.nextToken();

            if ( startExpr(lexer.token) ) {
                anExprList = exprList();
            }

            if ( lexer.token != Symbol.RIGHTPAR ) {
                signalError.show(") expected");
            }

            lexer.nextToken();
            return anExprList;
	}
        
        
        
        private boolean isSubClass(String right, String left){
            KraClass rightKra,leftKra;
            boolean found = false;
            
            rightKra = symbolTable.getInGlobal(right);
            leftKra = symbolTable.getInGlobal(left);
            
            if(leftKra != null){
                while( rightKra.getHasSuper() ){

                    if( rightKra.getSuperclass() == leftKra ){
                        found = true;
                        break;
                    }else{
                        rightKra = rightKra.getSuperclass();
                    }
                }
            }
            
            return found;
        }
        
        private boolean isBasicType(String checkType){

            return ( checkType.equals(Type.booleanType.getName()) || checkType.equals(Type.intType.getName())
                     ||checkType.equals(Type.stringType.getName())|| checkType.equals(Type.voidType.getName()));
            
        }
        
        
        /*
        WhileStat ::= “while” “(” Expression “)” Statement
        */
	private WhileStatement whileStatement() {

            lexer.nextToken();
            
            if ( lexer.token != Symbol.LEFTPAR ) {
                signalError.show("( expected");
            }

            lexer.nextToken();
            Expr e = expr();
 
            /*ER-SEM11.KRA*/
            if(!e.getType().getName().equals("boolean")){
                signalError.show("non-boolean expression in  'while' command");
            }
            if ( lexer.token != Symbol.RIGHTPAR ) {
                signalError.show(") expected");
            }

            lexer.nextToken();
            System.out.println("testando o token");
                System.out.println(lexer.token); 
            
            Statement myStatement = statement();
            System.out.print("testeWhile");
            if(myStatement == null){
                System.out.println("Is null");
                System.out.println(lexer.token);

            }
            
            return new WhileStatement(e, myStatement);
	}

        
        /*
        IfStat ::= “if ” “(” Expression “)” Statement [ “else” Statement ]
        */
	private IfStatement ifStatement() {
            Statement myStatement2 = null;
            
            lexer.nextToken();
            if ( lexer.token != Symbol.LEFTPAR ) {
                signalError.show("( expected");
            }

            lexer.nextToken();
            Expr myExpr = expr();

            if ( lexer.token != Symbol.RIGHTPAR ) {
                signalError.show(") expected");
            }

            lexer.nextToken();
            
             if ( lexer.token == Symbol.LEFTCURBRACKET ){
                 lexer.nextToken();
              
             }
            Statement myStatement = statement();
            if ( lexer.token == Symbol.RIGHTCURBRACKET ){
                 lexer.nextToken();
              
             }
            
            if ( lexer.token == Symbol.ELSE ) {
                    lexer.nextToken();
                    myStatement2 = statement();
            }
            
            return new IfStatement(myExpr, myStatement, myStatement2);
	}
        
        /*
        ReturnStat ::= “return” Expression
        */
	private ReturnStatement returnStatement() {
            
            lexer.nextToken();
            Expr myExpr = expr();

            if ( lexer.token != Symbol.SEMICOLON ){
                signalError.show(SignalError.semicolon_expected);
            }

            lexer.nextToken();
            
            if((this.currentMethod.getType() == Type.voidType) &&  isBasicType(myExpr.getType().getName()) ){
                signalError.show("Illegal 'return' statement. Method returns 'void'");
            }
            
            if(myExpr != null){
                if( (this.currentMethod.getType() != myExpr.getType()) && !isSubClass(myExpr.getType().toString(), this.currentMethod.getType().getName())){
                    signalError.show("Type error: type of the expression returned is not subclass of the method return type");
                }
            }
            
            return new ReturnStatement(myExpr);
	}
        
        
        /*
        ReadStat ::= “read” “(” LeftValue { “,” LeftValue } “)”
        LeftValue ::= [ (“this” | Id ) “.” ] Id
        */
	private ReadStatement readStatement() {
            ArrayList<Variable> myVarList = new ArrayList<>();
            
            lexer.nextToken();

            if ( lexer.token != Symbol.LEFTPAR ) {
                signalError.show("( expected");
            }

            lexer.nextToken();

            while (true) {
                
                /*We still have to get the instance Variable in here*/
                if ( lexer.token == Symbol.THIS ) {
                    lexer.nextToken();

                    if ( lexer.token != Symbol.DOT ){
                        signalError.show(". expected");
                    }
                    
                    lexer.nextToken();
                }
                
                
                if ( lexer.token != Symbol.IDENT ){
                    signalError.show("Identfier expected");
                }

                String name = lexer.getStringValue();
                
                Variable myVar = symbolTable.getInLocal(name);

                if(myVar != null){
                    
                    if(myVar.getType() == Type.booleanType){
                        signalError.show("Command 'read' does not accept 'boolean' variables");
                    }

                    if( myVar.getType() != Type.stringType || myVar.getType() != Type.intType ){
                    //    signalError.show("'int' or 'String' expression expected");
                    }
                    
                }else{
                    signalError.show("Command 'read' expects a variable");
                }
                
                myVarList.add(myVar);
                
                lexer.nextToken();

                if ( lexer.token == Symbol.COMMA ){
                    lexer.nextToken();
                }else{
                    break;
                }
            }

            if ( lexer.token != Symbol.RIGHTPAR ){
                signalError.show(") expected");
            }

            lexer.nextToken();

            if ( lexer.token != Symbol.SEMICOLON ){
                    signalError.show(SignalError.semicolon_expected);
            }

            lexer.nextToken();
            
            return new ReadStatement(myVarList);
	}
        
        
        /*
        WriteStat ::= “write” “(” ExpressionList “)”
        */
	private WriteStatement writeStatement() {

            lexer.nextToken();

            if ( lexer.token != Symbol.LEFTPAR ) {
                signalError.show("( expected");
            }

            lexer.nextToken();
            System.out.println("EXPRLIST TAMANHO - [" +1+ "]"+ "Valor de lexer" + lexer.token.name());

            ExprList myExprList = exprList();
            
            Iterator<Expr> myIteratorExpr = myExprList.elements();
            while (myIteratorExpr.hasNext()) {
                Expr el = myIteratorExpr.next();
                
                System.out.println("Estou pegando os elementos ? Valor de EL ["+el.toString()+"]");
                
                if( el.getType() == Type.booleanType ){
                    signalError.show("Command 'write' does not accept 'boolean' expressions");
                }

                if(!isBasicType(el.getType().getName())){
                    signalError.show("Command 'write' does not accept objects");
                }

            }
            
            System.out.println("Valor de lexer " + lexer.token);

            if ( lexer.token != Symbol.RIGHTPAR ){
                signalError.show(") expected");
            }

            lexer.nextToken();

            if ( lexer.token != Symbol.SEMICOLON ){
                signalError.show(SignalError.semicolon_expected);
            }
           
            lexer.nextToken();
            
            //System.out.println("Tamanho de ExprList ["+myExprList.getSize()+"] e valor do token ["+lexer.token.name()+"]");
            
            return new WriteStatement(myExprList);
	}
        
        
        /*
        WriteStat ::= “write” “(” ExpressionList “)”
        */
	private WriteLnStatement writelnStatement() {

            lexer.nextToken();
            if ( lexer.token != Symbol.LEFTPAR ) {
                signalError.show("( expected");
            }

            lexer.nextToken();
            ExprList myExprList = exprList();
            
            for (Expr exprList : myExprList.getExprList()) {
                
                if( exprList.getType() == Type.booleanType ){
                    signalError.show("Command 'write' does not accept 'boolean' expressions");
                }
                
                if(!isBasicType(exprList.getType().getName())){
                //    signalError.show("Command 'write' does not accept objects");
                }
                
            }


            if ( lexer.token != Symbol.RIGHTPAR ) {
                signalError.show(") expected");
            }

            lexer.nextToken();

            if ( lexer.token != Symbol.SEMICOLON ){
                signalError.show(SignalError.semicolon_expected);
            }

            lexer.nextToken();

            return new WriteLnStatement(myExprList);
	}
        
        
        /*
        Just check the token
        */
	private BreakStatement breakStatement() {
		
            lexer.nextToken();

            if ( lexer.token != Symbol.SEMICOLON ){
                signalError.show(SignalError.semicolon_expected);
            }

            lexer.nextToken();

            return new BreakStatement();
	}
        
        /*
        Just escape the token
        */
	private NullStatement nullStatement() {
            lexer.nextToken();
            return null;
	}
        
        /*
        ExpressionList ::= Expression { “,” Expression }
        Expression ::= SimpleExpression [ Relation SimpleExpression ]
        */
	private ExprList exprList() {
            // ExpressionList ::= Expression { "," Expression }
            Expr e;
            ExprList anExprList = new ExprList();
            
            e = expr();
            //if(e!=null) System.out.println("Valor de E em EXPRLIST - [" +e.toString()+ "]");

            anExprList.addElement(e);

            while (lexer.token == Symbol.COMMA) {
                    lexer.nextToken();
                    e = expr();
                    anExprList.addElement(e);
            }
                    
            return anExprList;
	}
        /*
        Expression ::= SimpleExpression [ Relation SimpleExpression ]
        SimpleExpression ::= Term { LowOperator Term }
        Relation ::= “==” | “<” | “>” | “<=” | “>=” | “! =” 
        Term ::= SignalFactor { HighOperator SignalFactor }
        LowOperator ::= “+” | “−” | “||”
        HighOperator ::= “∗”|“/”|“&&”
        SignalFactor ::= [ Signal ] Factor
        Signal ::= “+” | “−”
        Factor ::= BasicValue | “(” Expression “)” | “!” Factor | “null” | ObjectCreation | PrimaryExpr
        */
	private Expr expr() {
            
            Expr left = simpleExpr();
            
            Symbol op = lexer.token;

            if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
                || op == Symbol.LT || op == Symbol.GE || op == Symbol.GT ) {

                lexer.nextToken();
                Expr right = simpleExpr();
                
                if(op == Symbol.EQ || op == Symbol.NEQ)
                    if(left.getType() != right.getType()){
                        signalError.show("Incompatible types cannot be compared with '"+op.toString()+"' because the result will always be 'false'");
                    }
                
                
                left = new CompositeExpr(left, op, right);
            }
            
            return left;
	}
        
        /*
        SimpleExpression ::= Term { LowOperator Term }
        Term ::= SignalFactor { HighOperator SignalFactor }
        LowOperator ::= “+” | “−” | “||”
        HighOperator ::= “∗”|“/”|“&&”
        SignalFactor ::= [ Signal ] Factor
        Signal ::= “+” | “−”
        Factor ::= BasicValue | “(” Expression “)” | “!” Factor | “null” | ObjectCreation | PrimaryExpr
        */
	private Expr simpleExpr() {
            Symbol op;

            Expr left = term();
            while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS || op == Symbol.OR) {
                lexer.nextToken();

                Expr right = term();

                if( left.getType() == Type.booleanType && right.getType() == Type.booleanType ){
                    if(op == Symbol.PLUS){
                        signalError.show( "type " + left.getType().getName()  + " does not support operator '" + op.toString() + "'");
                    }
                }

                if( op == Symbol.MINUS || op == Symbol.PLUS ){
                    if( (left.getType() != Type.intType) || (right.getType() != Type.intType) ){
                        signalError.show("operator '" + op.toString() + "' of '" + Type.intType.getName()  + "' expects an '" + Type.intType.getName() + "' value");
                    }
                }

                left = new CompositeExpr(left, op, right);
            }
            return left;
	}
        
        /*
        Term ::= SignalFactor { HighOperator SignalFactor }
        SignalFactor ::= [ Signal ] Factor
        Signal ::= “+” | “−”
        Factor ::= BasicValue | “(” Expression “)” | “!” Factor | “null” | ObjectCreation | PrimaryExpr
        HighOperator ::= “∗”|“/”|“&&”
        */

	private Expr term() {
            Symbol op;

            Expr left = signalFactor();

            while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
                            || op == Symbol.AND) {
                lexer.nextToken();
                Expr right = signalFactor();

                if( left.getType() == Type.intType && right.getType() == Type.intType ){

                    if(op == Symbol.AND){
                        signalError.show("type '" + left.getType().getName() + "' does not support operator '" + op.toString() + "'");
                    }
                }


                left = new CompositeExpr(left, op, right);
            }
            return left;
	}
        
        
        /*
           SignalFactor ::= [ Signal ] Factor
           Signal ::= “+” | “−”
           Factor ::= BasicValue | “(” Expression “)” | “!” Factor | “null” | ObjectCreation | PrimaryExpr
        */
	private Expr signalFactor() {
            Symbol op;

            
            if ( (op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {
                lexer.nextToken();
                
                Expr myExpr =  factor();
                
                if( myExpr.getType() == Type.booleanType  ){
                    signalError.show("operator '" +op.toString()+ "' does not accepts boolean expressions" );
                }
                
                return new SignalExpr(op, myExpr);
            }
            else{
                return factor();
            }
	}

	/*
	 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
	 *      ObjectCreation | PrimaryExpr
	 * 
	 * BasicValue ::= IntValue | BooleanValue | StringValue 
	 * BooleanValue ::=  "true" | "false" 
	 * ObjectCreation ::= "new" Id "(" ")" 
	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 Id  |
	 *                 Id "." Id | 
	 *                 Id "." Id "(" [ ExpressionList ] ")" |
	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
	 *                 "this" | 
	 *                 "this" "." Id | 
	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
	 */
	private Expr factor() {

		Expr e;
		ExprList exprList;
		String messageName, ident;
                MessageSendToVariable msgToVar = null;

		switch (lexer.token) {
		// IntValue
		case LITERALINT:
			return literalInt();
			// BooleanValue
		case FALSE:
			lexer.nextToken();
			return LiteralBoolean.False;
			// BooleanValue
		case TRUE:
			lexer.nextToken();
			return LiteralBoolean.True;
			// StringValue
		case LITERALSTRING:
			String literalString = lexer.getLiteralStringValue();
			lexer.nextToken();
			return new LiteralString(literalString);
			// "(" Expression ")" |
		case LEFTPAR:
			lexer.nextToken();
			e = expr();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(e);

			// "null"
		case NULL:
			lexer.nextToken();
			return new NullExpr();
			// "!" Factor
		case NOT:
			lexer.nextToken();
			
                        e = expr();
                        
                        if(e.getType() != Type.booleanType){
                            signalError.show("operator '"+Symbol.NOT.toString()+"' does not accept '"+ e.getType().getName() +"' values");
                        }
                        return new UnaryExpr(e, Symbol.NOT);
			// ObjectCreation ::= "new" Id "(" ")"
		case NEW:
			
                        lexer.nextToken();
			
                        if ( lexer.token != Symbol.IDENT)   signalError.show("Identifier expected");
                        
                        
			String className = lexer.getStringValue();
			
                        // encontre a classe className in symbol table KraClass 
                        KraClass   aClass = symbolTable.getInGlobal(className); 
                        if ( aClass == null )   signalError.show("Class doesn´t exist");
			
                        KraClass auxNew = symbolTable.getInGlobal(className);
                        
                        if(auxNew == null)  signalError.show("Class type not found");
                        
                     //   ObjectCreation object = new ObjectCreation(auxNew)
                      
                        VariableExpr myVe = new VariableExpr(new Variable(auxNew.getName(), auxNew));
                        
			lexer.nextToken();
			
                        if ( lexer.token != Symbol.LEFTPAR ) signalError.show("( expected");
                            lexer.nextToken();
			
                        if ( lexer.token != Symbol.RIGHTPAR ) signalError.show(") expected");
                            lexer.nextToken();
			/*
			 * return an object representing the creation of an object
			 */
			return myVe;
			/*
          	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
		case SUPER:
			// "super" "." Id "(" [ ExpressionList ] ")"
			lexer.nextToken();
                        
                        if(!this.currentKRA.getHasSuper())
                            signalError.show( "'super' used in class '"+this.currentKRA.getName()+"' that does not have a superclass");
                        
                        
			if ( lexer.token != Symbol.DOT )    signalError.show("'.' expected");
			
			else            lexer.nextToken();
                        
			if ( lexer.token != Symbol.IDENT )  signalError.show("Identifier expected");
                        
			messageName = lexer.getStringValue();
                        
                        if(this.currentKRA.getHasSuper())
                            if (!isThereMethodInClass(this.currentKRA.getSuperclass(), messageName))
                                signalError.show("Method '"+messageName+"' was not found in class '"+this.currentKRA.getName()+"' or its superclasses");
                        
			/*
			 * para fazer as confer�ncias sem�nticas, procure por 'messageName'
			 * na superclasse/superclasse da superclasse etc
			 */
			lexer.nextToken();
			exprList = realParameters();
			break;
                /*
                * PrimaryExpr ::=  
                * Id  |
                * Id "." Id | 
                * Id "." Id "(" [ ExpressionList ] ")" |
                * Id "." Id "." Id "(" [ ExpressionList ] ")" |
                 */
		case IDENT:
                    
			String firstId = lexer.getStringValue();

                        lexer.nextToken();
                        
			if ( lexer.token != Symbol.DOT ) {
                            
                            //System.out.println("Lista de identificadores - " + firstId);
                            
                            Variable v;
                            v = symbolTable.getInLocal(firstId);
                            //if( v == null) signalError.show("Variable '" + firstId + "' was not declared ");
                            
                            VariableExpr ve = new VariableExpr(v);
                            // Id
                            // retorne um objeto da ASA que representa um identificador
                            return ve;
			}
			else { 
                            
                                // Id "."
                                Variable vAux;
                                //System.out.println("Erro 7");
                                if( ( vAux = symbolTable.getInLocal(firstId)) != null ){
                                    if(vAux.getType() == Type.intType){
                                        //signalError.show("Message send to a non-object receiver");
                                    }
                                }
                                
				lexer.nextToken(); // coma o "."
				if ( lexer.token != Symbol.IDENT ) {
					signalError.show("Identifier expected");
				}
				else {
					// Id "." Id
					lexer.nextToken();
					ident = lexer.getStringValue();
                                      //  System.out.print(ident);
					if ( lexer.token == Symbol.DOT ) 
                                        {
                                            
						// Id "." Id "." Id "(" [ ExpressionList ] ")"
						/*
						 * se o compilador permite vari�veis est�ticas, � poss�vel
						 * ter esta op��o, como
						 *     Clock.currentDay.setDay(12);
						 * Contudo, se vari�veis est�ticas n�o estiver nas especifica��es,
						 * sinalize um erro neste ponto.
						 */
						lexer.nextToken();
						if ( lexer.token != Symbol.IDENT ){
                                                    signalError.show("Identifier expected");
                                                }
                                                
                                                
						messageName = lexer.getStringValue();
                                                System.out.println("Valor de mensagem = " + messageName);
						lexer.nextToken();
						exprList = this.realParameters();

					}
					else if ( lexer.token == Symbol.LEFTPAR ) 
                                        {
                                             
       
                                            //System.out.println("till here");

                                            // Id "." Id "(" [ ExpressionList ] ")"
                                            //System.out.println("valor de token = " + lexer.getStringValue() );

                                            KraClass identClass = symbolTable.getInGlobal(symbolTable.getInLocal(firstId).getType().getName());

                                            MethodDec myMethod = findMethod(identClass, ident);
                                            //a.m();
                                            Variable vv = symbolTable.getInLocal(firstId);
                                            
                                            exprList = realParameters();
                                            
                                            //if(vv != null)  System.out.println("Valor de VV - " + vv.getName() + " Tipo - [" + vv.getType().getName() + "]");
                                            
                                            //System.out.println("Arrived in MessageSendToVariable, what should I do? - Value of FIRST ID ["+firstId+"] then, IDENT["+ident+"]");
                                            if ( myMethod != null ) {
                                                
                                                //System.out.println("Arrived in MessageSendToVariable, what should I do? - Value of FIRST ID ["+firstId+"] then, IDENT["+ident+"]");
                                                //ParamList p = myMethod.getPl();
                                                return new MessageSendToVariable( vv, myMethod, exprList);
                                            }
                                           

                                            /*
                                             * para fazer as confer�ncias sem�nticas, procure por
                                             * m�todo 'ident' na classe de 'firstId'
                                             */
					}
					else {
                                            KraClass identClass = symbolTable.getInGlobal(symbolTable.getInLocal(firstId).getType().getName());
                                            
                                            return new MessageSendToInstanceVariable(identClass.getInstanceVariable(ident), true);
                                        }
                                        
				}
			}
			break;
		case THIS:
			/*
			 * Este 'case THIS:' trata os seguintes casos: 
                         * PrimaryExpr ::= 
                         * "this" | 
                         * "this" "." Id | 
                         * "this" "." Id "(" [ ExpressionList ] ")"  | 
                         * "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
                            // only 'this'
                            // retorne um objeto da ASA que representa 'this'
                            // confira se n�o estamos em um m�todo est�tico
                     
                            return new MessageSendToSelf(currentKRA, null, null, null);
			}
			else {
				lexer.nextToken();
				
                                if ( lexer.token != Symbol.IDENT )  signalError.show("Identifier expected");
				
                                ident = lexer.getStringValue();
                                
				lexer.nextToken();
				
                                // j� analisou "this" "." Id
				if ( lexer.token == Symbol.LEFTPAR ) {
                                    // "this" "." Id "(" [ ExpressionList ] ")"
                                    /*
                                     * Confira se a classe corrente possui um m�todo cujo nome �
                                     * 'ident' e que pode tomar os par�metros de ExpressionList
                                     */
                                    exprList = this.realParameters();

                                    MethodDec myNewMethod = findMethod(currentKRA, ident);

                                    return new MessageSendToSelf (currentKRA, null, myNewMethod , exprList);
				}
				else if ( lexer.token == Symbol.DOT ) {
					// "this" "." Id "." Id "(" [ ExpressionList ] ")"
					lexer.nextToken();
                                        
					if ( lexer.token != Symbol.IDENT )  signalError.show("Identifier expected");
					
                                        lexer.nextToken();
					
                                        String newIdent = lexer.getStringValue();
                                        
                                        lexer.nextToken();
                                        
                                        InstanceVariable instVar = currentKRA.getInstanceVariable(newIdent);
                                        
                                        exprList = this.realParameters();
                                        
                                        return new MessageSendToSelf(currentKRA, instVar, currentMethod, exprList);
				}
				else {
                                    // retorne o objeto da ASA que representa "this" "." Id
                                    /*
                                     * confira se a classe corrente realmente possui uma
                                     * vari�vel de inst�ncia 'ident'
                                     */
                                    String nameOfIdent = lexer.getStringValue();

                        //            if (currentKRA.getInstanceVariable(nameOfIdent) != null)    signalError.show("Variable not found");

                                    InstanceVariable instvar = (InstanceVariable) currentKRA.getInstanceVariable(nameOfIdent);

                                    return new MessageSendToSelf(currentKRA, instvar, null, null);
				}
			}
		default:
			//signalError.show("Expression expected");
		}
                
                return null;
	}
        
        private boolean isThereMethodInClass(KraClass myKra, String myMethod){
            boolean found = false;
            
            for(int i=0; i < myKra.getPublicMethods().size(); i++){
                if(myKra.getPublicMethods().get(i).getName().equals(myMethod)){
                    found = true;
                    break;
                }
            }
            
            if(found == false){
                
                while( myKra.getHasSuper() == true ){
                    
                    if( !isThereMethodInClass(myKra.getSuperclass(), myMethod) ){
                        myKra = myKra.getSuperclass();
                    } 
                    else{
                        found = true;
                        break;
                    }   
                }
            }
            
            return found;
        }
        
        private MethodDec findMethod(KraClass myKra, String myMethod){
            MethodDec found = null;
            
            for(int i=0; i < myKra.getPublicMethods().size(); i++){
                if(myKra.getPublicMethods().get(i).getName().equals(myMethod)){
                    found = myKra.getPublicMethods().get(i);
                    break;
                }
            }
            
            for(int i=0; i < myKra.getPrivateMethods().size(); i++){
                if(myKra.getPrivateMethods().get(i).getName().equals(myMethod)){
                    found = myKra.getPrivateMethods().get(i);
                    break;
                }
            }
            
            if(found == null){
                while( myKra.getHasSuper() == true )
                {
                    found = findMethod(myKra.getSuperclass(), myMethod);
                    
                    if( found == null ) myKra = myKra.getSuperclass();
                    
                    else                break;
                }
            }
            
            return found;
        }
        
        
	private LiteralInt literalInt() {
            // the number value is stored in lexer.getToken().value as an object of
            // Integer.
            // Method intValue returns that value as a value of type int.
            int value = lexer.getNumberValue();
            lexer.nextToken();
            return new LiteralInt(value);
	}

	private static boolean startExpr(Symbol token) {

            return token == Symbol.FALSE || token == Symbol.TRUE
                            || token == Symbol.NOT || token == Symbol.THIS
                            || token == Symbol.LITERALINT || token == Symbol.SUPER
                            || token == Symbol.LEFTPAR || token == Symbol.NULL
                            || token == Symbol.IDENT || token == Symbol.LITERALSTRING;

	}
        
        private boolean     isInWhile;    
        private KraClass    currentKRA;
        private MethodDec   currentMethod;
	private SymbolTable symbolTable;
	private Lexer       lexer;
	private SignalError signalError;

    private void ObjectCreation(KraClass auxNew) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
}
