package com.smallscript.codegenerator

import com.smallscript.symboltable._
import com.smallscript.errorreporting._
import com.smallscript.errorreporting._
import com.smallscript.errorreporting.ErrorReporter
import com.smallscript.`type`._
import com.smallscript.util._
import com.smallscript.parser.SmallScriptParserVisitor
import com.smallscript.symboltable.SymbolTable
import com.smallscript.parser._
import com.smallscript.symboltable.DuplicateElementException
import com.smallscript.errorreporting._
import cafebabe.AbstractByteCodes;
import cafebabe.CodeHandler;
import cafebabe.Flags;
import cafebabe.MethodHandler;
import cafebabe.ByteCodes._
import cafebabe.AbstractByteCodes._

class CodeGeneratorVisitor(var smt: SymbolTable, val cg: CodeGenerator) extends SmallScriptParserVisitor{
  smt = new SymbolTable()
  val stack = new java.util.Stack[Object]
  var labelCounter = 0;
  var whileLoopLabelCounter = 0
  
  def nextWhileLoopLabelCounter() = { whileLoopLabelCounter += 1; whileLoopLabelCounter}
  
  
  
  def visit(node: com.smallscript.parser.ASTArrayAssignment,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTArrayAccess,data: Object): Object = data
  
  def visit(node: com.smallscript.parser.ASTModuloExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    cg.mainCh << IREM
    
    exprLeft
  }
  
  
  def visit(node: com.smallscript.parser.ASTDivideExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    cg.mainCh << IDIV
    
    exprLeft
  }
  
  
  def visit(node: com.smallscript.parser.ASTMultiplyExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    cg.mainCh << IMUL
    
    exprLeft
  }
  
  
  def visit(node: com.smallscript.parser.ASTSubractExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    cg.mainCh << ISUB
    
    exprLeft
  }
  
  def visit(node: com.smallscript.parser.ASTAddExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    if(exprLeft.isInstanceOf[Integer])
      cg.mainCh << IADD
    else if(exprLeft.isInstanceOf[String]){
      //string concat 
    }
    //println("::ADD")    
    exprLeft
  }
  
  
  
  
  def visit(node: com.smallscript.parser.ASTReturnStatement,data: Object): Object = {
    val expr = node.jjtGetChild(0).jjtAccept(this, data)
    if(expr.isInstanceOf[Integer] || expr.isInstanceOf[Character])
      cg.mainCh << IRETURN
    else if(expr.isInstanceOf[String])
      cg.mainCh << ARETURN
    else
      cg.mainCh << RETURN
    data
  }
  
  
  
  def visit(node: com.smallscript.parser.ASTLoopStatement,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTWhileStatement,data: Object): Object = {
    println("\nWHILE")
    val WHILE_START_LABEL = "WHILE_START" + nextWhileLoopLabelCounter()
    val WHILE_CODE_LABEL = "WHILE_CODE" + nextWhileLoopLabelCounter()
    val WHILE_END_LABEL = "WHILE"+ nextWhileLoopLabelCounter()
    
    cg.mainCh << Label(WHILE_START_LABEL)
    
    node.jjtGetChild(0).jjtAccept(this, WHILE_CODE_LABEL)
    
    cg.mainCh << Goto(WHILE_END_LABEL)
    
    cg.mainCh << Label(WHILE_CODE_LABEL)
    
    for(i <- 1 to node.jjtGetNumChildren()-1){
       node.jjtGetChild(i).jjtAccept(this, data)
    }
    
    cg.mainCh << Goto(WHILE_START_LABEL)
    
    cg.mainCh << Label(WHILE_END_LABEL)
    
    data
  }
  
  
  /* If Statements */
  def visit(node: com.smallscript.parser.ASTIfStatement,data: Object): Object = {
    smt.saveStackIndex()
    smt.createNewScope()
    
    val IF_LABEL = "IF_LABEL" + {labelCounter += 1; labelCounter};
    val ELSE_LABEL = "ELSE_LABEL" + {labelCounter += 1; labelCounter};
    val END_IF_LABEL = "END_IF_LABEL" + {labelCounter += 1; labelCounter};
    
    node.jjtGetChild(0).jjtAccept(this, IF_LABEL)
    
    cg.mainCh << Goto(ELSE_LABEL)
    cg.mainCh << Label(IF_LABEL)
    
    //Executing If statements
    var cnt = 2
    import scala.util.control.Breaks._
    breakable{
      for(i <- cnt to node.jjtGetNumChildren()){
        if(node.jjtGetChild(i-1).isInstanceOf[ASTElse])  {
          println("\nBREAKING")
          break
        }
        else{
          print()
          for(j <- 0 to node.jjtGetChild(i-1).jjtGetNumChildren()-1){
            node.jjtGetChild(i-1).jjtGetChild(j).jjtAccept(this, data);
          }
        }
        cnt = i
      }
    }
    cg.mainCh << Goto(END_IF_LABEL)
    
    cg.mainCh << Label(ELSE_LABEL)
    //Else-Stmt
    node.jjtGetChild(cnt).jjtAccept(this, data);
    
    cg.mainCh << Label(END_IF_LABEL)
    
    smt.exitCurrentScope()
    smt.restoreStackIndex()
    data
  }
  
  def visit(node: com.smallscript.parser.ASTElse,data: Object): Object = {
    println("\nELSE : ")
    node.childrenAccept(this, data)
  }
  
  def visit(node: com.smallscript.parser.ASTDblEqualNode,data: Object): Object = {
    node.childrenAccept(this, data)
    cg.mainCh << If_ICmpEq(data.toString())
    data
  }
  
  def visit(node: com.smallscript.parser.ASTLTNode,data: Object): Object = {
    node.childrenAccept(this, data)
    cg.mainCh << If_ICmpLt(data.toString())
    data
  }
  
  def visit(node: com.smallscript.parser.ASTGENode,data: Object): Object = {
    node.childrenAccept(this, data)
    cg.mainCh << If_ICmpGe(data.toString())
    data
  }
  
  def visit(node: com.smallscript.parser.ASTLENode,data: Object): Object = {
    node.childrenAccept(this, data)
    cg.mainCh << If_ICmpLe(data.toString())
    data
  }
  
  def visit(node: com.smallscript.parser.ASTGTNode,data: Object): Object = {  
    node.childrenAccept(this, data)
    cg.mainCh << If_ICmpGt(data.toString())
    data
  }
  
  
  
  //Declarations
  
  def visit(node: com.smallscript.parser.ASTidentifier,data: Object): Object = {
    //println("\nidentifier : ")
    val variable = node.jjtGetChild(0).jjtAccept(this, data)
    return variable 
  }
  
  def visit(node: com.smallscript.parser.ASTArg_DELC,data: Object): Object = {
    val varId = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    val varType = node.jjtGetChild(1).jjtAccept(this, data).asInstanceOf[String]
    val decl = new Var(varId, null, TypeConverter.toType(varType), smt.getCurrentNestingDepth)
    return decl
  }
  
  /* (*) Either ASTVar or ASTVal needs to re-factor */
  def visit(node: com.smallscript.parser.ASTVAL_DELC,data: Object): Object = {
    val mainCh = cg.mainCh
    cg.mainCh = cg.staticCh
    //print("Variable : ")
    val id = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    var idType = TypeConverter.toType(node.jjtGetChild(1).jjtAccept(this, data).toString)
    
    var containsValue = true
    var idValue: Object = null
    
    /* initialization in declaration is optional,
     * so checking whether declaration contains value or not
     */
    if(node.jjtGetNumChildren() > 2)
       idValue = node.jjtGetChild(2).jjtAccept(this, data)
    else
      containsValue = false
    
      //println("Type Of : " + idValue.getClass.getName)
    /* Checking Whether type of variable is same or at least compatible or not */
    
    if(idValue == null && containsValue) return data
    var newVariable: Val = null
    if(idValue != null && !(idType == TypeConverter.classToType(idValue.getClass))){
      /* (*)Add Line Number Later  */
      //errReporter.addError(new CompilationError("Expression " + idValue + " is not valid for type " + idType))
    }
    else{
      newVariable = new Val(id, idValue, idType, smt.getCurrentNestingDepth)
      newVariable.setStackId(smt.nextStackIndex)
      try{
        smt.put(newVariable.getName, newVariable)
      }catch{
        case dex: DuplicateElementException => {
        }
      }
    }
    if(newVariable.isLocal()){
      newVariable.getType match{
        case Type.INT    => cg.mainCh << IStore(newVariable.getStackId)
        case Type.CHAR   => cg.mainCh << IStore(newVariable.getStackId)
        case Type.STRING => cg.mainCh << AStore(newVariable.getStackId)
      }
      cg.mainCh.getFreshVar
    }
    else{
      val mainCh = cg.mainCh
      cg.mainCh = cg.staticCh
      val field = cg.classFile.addField(TypeConverter.toByteCodeType(idType), id)
      field.setFlags(Flags.FIELD_ACC_STATIC)
      cg.mainCh << PutStatic(cg.fileName, id,TypeConverter.toByteCodeType(idType))
      cg.mainCh = mainCh
    }
    
    
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTVAR_DELC,data: Object): Object = {
    val mainCh = cg.mainCh
    if(smt.getCurrentNestingDepth == 0)
      cg.mainCh = cg.staticCh
    //print("Value : ")
    val id = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    var idType = TypeConverter.toType(node.jjtGetChild(1).jjtAccept(this, data).toString)  
    
    var containsValue = true
    var idValue: Object = null
    
    /* initialization in declaration is optional,
     * so checking whether declaration contains value or not
     */
    if(node.jjtGetNumChildren() > 2)
       idValue = node.jjtGetChild(2).jjtAccept(this, data)
    else
      containsValue = false
    
      //println("Type Of : " + idValue.getClass.getName)
    /* Checking Whether type of variable is same or at least compatible or not */
    
    if(idValue == null && containsValue) return data
    
    var newVariable: Var = null
    if(idValue != null && !(idType == TypeConverter.classToType(idValue.getClass))){
      /* (*)Add Line Number Later  */
      //errReporter.addError(new CompilationError("Expression " + idValue + " is not valid for type " + idType))
    }
    
    else{
      newVariable = new Var(id, idValue, idType, smt.getCurrentNestingDepth)
      if(smt.getCurrentNestingDepth != 0)
        newVariable.setStackId(smt.nextStackIndex)
      try{
        smt.put(newVariable.getName, newVariable)
      }catch{
        case dex: DuplicateElementException => {
      }
      }
    }
    
    
    if(newVariable.isLocal()){
      cg.mainCh = mainCh
      newVariable.getType match{
        case Type.INT    => cg.mainCh << IStore(newVariable.getStackId)
        case Type.CHAR   => cg.mainCh << IStore(newVariable.getStackId)
        case Type.STRING => cg.mainCh << AStore(newVariable.getStackId)
      }
      cg.mainCh.getFreshVar
    }
    else{
      val field = cg.classFile.addField(TypeConverter.toByteCodeType(idType), id)
      field.setFlags(Flags.FIELD_ACC_STATIC)
      cg.staticCh << PutStatic(cg.fileName, id,TypeConverter.toByteCodeType(idType))
    }
    cg.mainCh = mainCh
    return data
  }
  
  
  
  def visit(node: com.smallscript.parser.ASTPrint,data: Object): Object = {
    cg.mainCh << GetStatic("java/lang/System", "out", "Ljava/io/PrintStream;")
    
    val expr = node.jjtGetChild(0).jjtAccept(this, data)
    println("PRINT:: " + expr)
    var exprType: String = null
    
    if(expr.isInstanceOf[Integer])
      exprType = "I"
    else if(expr.isInstanceOf[String])
      exprType = "Ljava/lang/String;"
    else if(expr.isInstanceOf[Character])
      exprType = "C"
    cg.mainCh << InvokeVirtual("java/io/PrintStream", "println", "("+exprType+")V")
   
    
    data
    
  }
  
  /* Assignment */
  def visit(node: com.smallscript.parser.ASTAssignment,data: Object): Object = {
    val id = node.jjtGetChild(0).jjtAccept(this, data).toString
    var expr = node.jjtGetChild(1).jjtAccept(this, data)
    
    var variable: Declaration = null
    
    try{ //(*) functions should also be handled
      variable = smt.get(id).asInstanceOf[Declaration]
    }catch{  
      case nex: java.util.NoSuchElementException => {
        return data
      }
    }
    
    if(!variable.isInstanceOf[Function]){
      if(expr == null){
        //errReporter.addError(new CompilationError("Expression " + expr + " doesn't exist in the scope."))
        return data
      }
      else if(variable.isInstanceOf[Val]){
       // errReporter.addError(new CompilationError(variable.getName + " is a constant and can not be assigned a value"))
        return data
      }
      else if(!(variable.getType == TypeConverter.classToType(expr.getClass))){
        return data
      }
    }else{
       return data
    }
    if(variable.isLocal()){
      
    }else{
      cg.mainCh << PutStatic(cg.fileName, variable.getName, TypeConverter.toByteCodeType(variable.getType))
    }
    variable.asInstanceOf[VarDeclaration] setValue expr
    data
  }
  
  
  def visit(node: com.smallscript.parser.ASTBreak,data: Object): Object = {
    return data
  }
  
  def visit(node: com.smallscript.parser.ASTVariable,data: Object): Object = {
    //print(", => Variable")
    return node.jjtGetValue()
  }
  
  
  
  
  //Expression
  
  def visit(node: com.smallscript.parser.ASTexpression,data: Object): Object = {
    //println("\nExpr : ")
    val exprValue = node.jjtGetChild(0).jjtAccept(this, data)
    return exprValue
  }
  
  
  def visit(node: com.smallscript.parser.ASTunaryExpression,data: Object): Object = {
    val exprValue = node.jjtGetChild(0).jjtAccept(this, data)
    return exprValue
  }
  
  
  def visit(node: com.smallscript.parser.ASTStringLiteral,data: Object): Object = {
    //println("STRING")
    val str = node.jjtGetValue().toString.substring(1, node.jjtGetValue().toString.length -1)
    cg.mainCh << Ldc(str)
    return str
  }
  
  
  def visit(node: ASTCharLiteral, data: Object): Object = {
    cg.mainCh << Ldc(node.jjtGetValue.toString.charAt(1))
    return new Character(node.jjtGetValue.toString.charAt(1))
  }
  
  
  def visit(node: ASTnumberExpression, data: Object): Object = {
    return node.jjtGetChild(0).jjtAccept(this, data)
  }
  
  
  /**
   * It returns either a value of declaration or null if id is
   * not present in symbol table
   */
  def visit(node: com.smallscript.parser.ASTVarialbleValue,data: Object): Object = {
    //println("VariabeValue : " + node.jjtGetValue)
    var variable: Declaration = null
    
    try{ //(*) functions should also be handled
      variable = smt.get(node.jjtGetValue.toString).asInstanceOf[Declaration]
    }catch{
      case nex: java.util.NoSuchElementException => {
        return null
      }
    }
    
    if(variable.isInstanceOf[Function]){
      //errReporter.addError(new CompilationError(variable.getName + " is a function and can not be used for assignment."))
      return null
    }
    
    //checking whether variable is initialized or not if it is un-inti and local variable
    if(variable.asInstanceOf[VarDeclaration].getValue == null){
    }
    if(variable.isLocal()){
      variable.getType match {
        case Type.INT =>    cg.mainCh << ILoad(variable.asInstanceOf[VarDeclaration].getStackId)
        case Type.CHAR =>   cg.mainCh << ILoad(variable.asInstanceOf[VarDeclaration].getStackId)
        case Type.STRING => cg.mainCh << ALoad(variable.asInstanceOf[VarDeclaration].getStackId)
      }
    }else{
      cg.mainCh << GetStatic(cg.fileName, variable.getName, TypeConverter.toByteCodeType(variable.getType))
    }
    variable.asInstanceOf[VarDeclaration].getValue
  }
  
  
  def visit(node: com.smallscript.parser.ASTNumber, data: Object): Object = {
    val num = Integer.parseInt(node.jjtGetValue.toString)
    print("NUM :: "+num)
    cg.mainCh << Ldc(num)
    return  new Integer(num)
  }
  
  
  def visit(node: com.smallscript.parser.ASTArrayLiteral,data: Object): Object = {
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTNegateExpr,data: Object): Object = {
    return "-" + node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
  }
  
  
  
  //Function
  
  def visit(node: com.smallscript.parser.ASTFunction,data: Object): Object = {
    //println("::Function : ")
    //javax.swing.JOptionPane.showMessageDialog(null, "FUNCTION")
    val mainCh = cg.mainCh
    
    smt.resetStackIndex()
    var totalChilds = 4
    
    smt.createNewScope() //creating a new scope for function in symbol table
    
    /* Function Name */
    val id = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    
    
    /* Parameters */
    var params: Array[Type] = null
    
    if(node.jjtGetNumChildren() > 3){
    params = new Array[Type](node.jjtGetChild(1).jjtGetNumChildren())
    var num = 1
    for(i <- 0 to node.jjtGetChild(1).jjtGetNumChildren()-1){
      val paramDecl: VarDeclaration = node.jjtGetChild(1).jjtGetChild(i).jjtAccept(this, data).asInstanceOf[VarDeclaration]
      paramDecl.getType match{
        case Type.INT    => paramDecl setValue new Integer(10)
        case Type.STRING => paramDecl setValue new String
        case Type.CHAR   => paramDecl setValue new Character('c')
      }
      params(i) = paramDecl.getType
      paramDecl.setStackId(smt.nextStackIndex())
      //println("FFFFFFFFFF")
      
      smt.put(paramDecl.getName, paramDecl)
    }
    }
    else
      totalChilds = 3
     
    
    /* Type Of Functions */
    val fType = node.jjtGetChild(totalChilds-2).jjtAccept(this, data).asInstanceOf[String]
     
    val newFunction = new Function(id, TypeConverter.toType(fType), params)
    
    /* Adding Function Symbol Table */
    try{
      smt.put(newFunction.getName, newFunction)
    } catch{
      case ex: DuplicateElementException => {
                    println(ex.getMessage)
                    //errReporter.addError(new CompilationError(ex.getMessage))
                 
               }
    }
    
    
    var parameters: String = ""
    if(params != null){
      for(i <- params){
        parameters += TypeConverter.toByteCodeType(i)
      }
    }
    
    val newMethod = cg.classFile.addMethod(
                              TypeConverter.toByteCodeType(TypeConverter.toType(fType)),
                              id, 
                              parameters
                    )
     
     newMethod.setFlags(Flags.FIELD_ACC_STATIC)
    
    cg.mainCh = newMethod.codeHandler
   // println("param")
    
    node.jjtGetChild(totalChilds-1).jjtAccept(this, data) // visiting statements of the functions
    
    if(TypeConverter.toType(fType) == Type.VOID)
      cg.mainCh << RETURN
    
    cg.mainCh.freeze
    cg.mainCh = mainCh //restoring
    
    
    smt.dump()
    smt.exitCurrentScope()
    ///println("djfhjahflsdjfljsdfioewjfiji")
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTFunctionCall,data: Object): Object = {
    
    /* Function Name */
    val id = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    
    
    /* Arguments : a list of expression */
    var params: Array[Type] = null
    
    if(node.jjtGetNumChildren() > 1){
      params = new Array[Type](node.jjtGetChild(1).jjtGetNumChildren())
      var num = 1
      for(i <- 0 to node.jjtGetChild(1).jjtGetNumChildren()-1){
         val paramExpr = node.jjtGetChild(1).jjtGetChild(i).jjtAccept(this, data) //expr
         params(i) = TypeConverter.classToType(paramExpr.getClass)
      }
    }
    var newFunction = new Function(id, Type.INT, params)
    try{
       if(id.contains(".")){
         //println("Out Function")
         newFunction = smt.getFunctionFromImport(newFunction).asInstanceOf[Function]
         
       }
       else{
         //println("Inline Function")
         newFunction = smt.get(newFunction).asInstanceOf[Function]
         
       }
    }catch{
      case ex: java.util.NoSuchElementException => {
      }
    }
    var parameters: String = ""
    if(params != null){
      for(i <- params){
        parameters += TypeConverter.toByteCodeType(i)
      }
    }
     //println("TYPE : " + "("+parameters+")"+TypeConverter.toByteCodeType(newFunction.getType))
    cg.mainCh << InvokeStatic(cg.fileName, id, "("+parameters+")"+TypeConverter.toByteCodeType(newFunction.getType))
    data
  }
  
  
  
  def visit(node: com.smallscript.parser.ASTType,data: Object): Object = {
    //print(":Type : " + node.jjtGetValue())
    return node.jjtGetValue().asInstanceOf[String]
  }
  
  def visit(node: com.smallscript.parser.ASTParameters,data: Object): Object = {
    //println("Parameters : ")
    node.childrenAccept(this, data)
    return data
  }
  
  def visit(node: com.smallscript.parser.ASTParameter,data: Object): Object = {
    //println("Parameter : ")
    val decl = node.jjtGetChild(0).jjtAccept(this, data)
    //println(decl)
    return decl 
  }
  
  def visit(node: com.smallscript.parser.ASTStatementBlock,data: Object): Object = {
    node.childrenAccept(this, data)
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTStatement,data: Object): Object = {
    node.childrenAccept(this, data)
    return data;
  }
  
  def visit(node: com.smallscript.parser.ASTstart,data: Object): Object = {
    node.childrenAccept(this, data)
    return data;
  }
  
  
  def visit(node: com.smallscript.parser.SimpleNode,data: Object): Object = {
    return data;
  }
  

  def isValueCompatible(idType: Type, value: String): Boolean = TypeConverter.typeOf(value) == idType

  
  def visit(node: ASTImport, data: Object): Object = {
      val module = node.jjtGetChild(0).jjtAccept(this, data).toString().trim
      smt.importModule(module.substring(1, module.length()-1))
      data
  }

  def visit(node: ASTExpressionParameters, data: Object): Object = data

  

  
       
       
       /*
       if(TypeConverter.getTypePriority(valType) == TypeConverter.getTypePriority(idType))
         true
       else if(TypeConverter.getTypePriority(valType) <= TypeConverter.getTypePriority(idType)){
         true //(*) With Warning
       }
       else
         false*/
  
  
  
}
  
//}