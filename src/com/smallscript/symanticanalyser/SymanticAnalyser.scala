package com.smallscript.symanticanalyser

import com.smallscript.symboltable._
import com.smallscript.errorreporting._
import com.smallscript.errorreporting.ErrorReporter
import com.smallscript.`type`._
import com.smallscript.util._
import com.smallscript.parser.SmallScriptParserVisitor
import com.smallscript.symboltable.SymbolTable
import com.smallscript.parser._
import com.smallscript.symboltable.DuplicateElementException
import com.smallscript.errorreporting._

class SymanticAnalyser(val smt: SymbolTable, val errReporter: ErrorReporter)
                       extends SmallScriptParserVisitor {
  
  val stack = new java.util.Stack[Object]
  
  
  
  def visit(node: com.smallscript.parser.ASTArrayAssignment,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTArrayAccess,data: Object): Object = data
  
  def visit(node: com.smallscript.parser.ASTModuloExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    if(exprLeft != null && exprRight != null){
      if(TypeConverter.classToType(exprLeft.getClass) != TypeConverter.classToType(exprRight.getClass)){
        errReporter.addError(new CompilationError("Expression type is not compatible."))
      }
    }
    
    exprLeft
  }
  
  
  def visit(node: com.smallscript.parser.ASTDivideExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    if(exprLeft != null && exprRight != null){
      if(TypeConverter.classToType(exprLeft.getClass) != TypeConverter.classToType(exprRight.getClass)){
        errReporter.addError(new CompilationError("Expression type is not compatible."))
     }
    }
    
    exprLeft
  }
  
  
  def visit(node: com.smallscript.parser.ASTMultiplyExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    if(exprLeft != null && exprRight != null){
      if(TypeConverter.classToType(exprLeft.getClass) != TypeConverter.classToType(exprRight.getClass)){
        errReporter.addError(new CompilationError("Expression type is not compatible."))
      }
    }
    
    exprLeft
  }
  
  
  def visit(node: com.smallscript.parser.ASTSubractExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    if(exprLeft != null && exprRight != null){
      if(TypeConverter.classToType(exprLeft.getClass) != TypeConverter.classToType(exprRight.getClass)){
        errReporter.addError(new CompilationError("Expression type is not compatible."))
      }
    }
    
    exprLeft
  }
  
  def visit(node: com.smallscript.parser.ASTAddExpr,data: Object): Object = {
    var exprLeft = node.jjtGetChild(0).jjtAccept(this, data)
    var exprRight = node.jjtGetChild(1).jjtAccept(this, data)
    
    if(exprLeft != null && exprRight != null){
      println(TypeConverter.classToType(exprLeft.getClass))
      println(TypeConverter.classToType(exprRight.getClass))
      if(TypeConverter.classToType(exprLeft.getClass) != TypeConverter.classToType(exprRight.getClass)){
        errReporter.addError(new CompilationError("Expression type is not compatible."))
      }
    }
    
    exprLeft
  }
  
  def visit(node: com.smallscript.parser.ASTDblEqualNode,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTGENode,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTLENode,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTGTNode,data: Object): Object = node.childrenAccept(this, data)
  def visit(node: com.smallscript.parser.ASTLTNode,data: Object): Object = data
  
  
  
  def visit(node: com.smallscript.parser.ASTReturnStatement,data: Object): Object = data
  
  
  
  def visit(node: com.smallscript.parser.ASTLoopStatement,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTWhileStatement,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTElse,data: Object): Object = node.childrenAccept(this, data)
  
  /* If Statements */
  def visit(node: com.smallscript.parser.ASTIfStatement,data: Object): Object = {
    smt.saveStackIndex()
    smt.createNewScope()
    node.childrenAccept(this, data)
    smt.exitCurrentScope()
    smt.restoreStackIndex()
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
    //println("=> " + id + ", " + idType + ", " + idValue)
    
    if(idValue == null && containsValue) return data
    
    if(idValue != null && !(idType == TypeConverter.classToType(idValue.getClass))){
      /* (*)Add Line Number Later  */
      errReporter.addError(new CompilationError("Expression " + idValue + " is not valid for type " + idType))
    }
    else{
      val newVariable = new Val(id, idValue, idType, smt.getCurrentNestingDepth)
      newVariable.setStackId(smt.nextStackIndex)
      try{
        smt.put(newVariable.getName, newVariable)
      }catch{
        case dex: DuplicateElementException => {
             errReporter.addError(new CompilationError(dex.getMessage))
        }
      }
    }
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTVAR_DELC,data: Object): Object = {
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
    //println("=> " + id + ", " + idType + ", " + idValue)
    
    if(idValue == null && containsValue) return data
    
    if(idValue != null && !(idType == TypeConverter.classToType(idValue.getClass))){
      /* (*)Add Line Number Later  */
      println("ERROR")
      errReporter.addError(new CompilationError("Expression " + idValue + " is not valid for type " + idType))
    }
    else{
      val newVariable = new Var(id, idValue, idType, smt.getCurrentNestingDepth)
      newVariable.setStackId(smt.nextStackIndex)
      try{
        smt.put(newVariable.getName, newVariable)
      }catch{
        case dex: DuplicateElementException => {
             errReporter.addError(new CompilationError(dex.getMessage))
        }
      }
    }
    
    return data
  }
  
  
  
  def visit(node: com.smallscript.parser.ASTPrint,data: Object): Object = {
    
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
        errReporter.addError(new CompilationError(id + " is not defined."))
        return data
      }
    }
    
    if(!variable.isInstanceOf[Function]){
      if(expr == null){
        errReporter.addError(new CompilationError("Expression " + expr + " doesn't exist in the scope."))
        return data
      }
      else if(variable.isInstanceOf[Val]){
        errReporter.addError(new CompilationError(variable.getName + " is a constant and can not be assigned a value"))
        return data
      }
      else if(!(variable.getType == TypeConverter.classToType(expr.getClass))){
        errReporter.addError(new CompilationError("Expression type is not valid for assignment."))
        return data
      }
    }else{
       errReporter.addError(new CompilationError(variable.getName + " is a function and can not be assigned."))
        return data
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
    //print("Expr : ")
    val exprValue = node.jjtGetChild(0).jjtAccept(this, data)
    return exprValue
  }
  
  
  def visit(node: com.smallscript.parser.ASTunaryExpression,data: Object): Object = {
    val exprValue = node.jjtGetChild(0).jjtAccept(this, data)
    return exprValue
  }
  
  
  def visit(node: com.smallscript.parser.ASTStringLiteral,data: Object): Object = {
    
    return node.jjtGetValue().toString.trim
  }
  
  
  def visit(node: ASTCharLiteral, data: Object): Object = 
      new Character(node.jjtGetValue().toString().charAt(1))
  
  
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
        errReporter.addError(new CompilationError(node.jjtGetValue() + " is not defined."))
        return null
      }
    }
    
    if(variable.isInstanceOf[Function]){
      errReporter.addError(new CompilationError(variable.getName + " is a function and can not be used for assignment."))
      return null
    }
    
    //checking whether variable is initialized or not if it is un-inti and local variable
    if(variable.asInstanceOf[VarDeclaration].getValue == null){
        errReporter.addError(new CompilationError(node.jjtGetValue() + " is used without initialization."))
    }
    variable.asInstanceOf[VarDeclaration].getValue
  }
  
  
  def visit(node: ASTNumber, data: Object): Object = new Integer(node.jjtGetValue().toString())
  
  
  def visit(node: com.smallscript.parser.ASTArrayLiteral,data: Object): Object = {
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTNegateExpr,data: Object): Object = {
    return "-" + node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
  }
  
  
  
  //Function
  
  def visit(node: com.smallscript.parser.ASTFunction,data: Object): Object = {
    smt.resetStackIndex()
    var totalChilds = 4
    
    smt.createNewScope() //creating a new scope for function in symbol table
    
    /* Function Name */
    val id = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    
    
    /* Parameters */
    //println("\tParameters : " + node.jjtGetChild(1).jjtGetNumChildren())
    var params: Array[Type] = null
    
    if(node.jjtGetNumChildren() > 3){
    params = new Array[Type](node.jjtGetChild(1).jjtGetNumChildren())
    var num = 1
    for(i <- 0 to node.jjtGetChild(1).jjtGetNumChildren()-1){
      val paramDecl: VarDeclaration = node.jjtGetChild(1).jjtGetChild(i).jjtAccept(this, data).asInstanceOf[VarDeclaration]
      params(i) = paramDecl.getType
      paramDecl.setStackId(smt.nextStackIndex())
      paramDecl.setValue(new Integer(10))
      
      smt.put(paramDecl.getName, paramDecl)
    }
    }
    else
      totalChilds = 3
     
    //println("FFFFFFFFFFFF")
    /* Type Of Functions */
    val fType = node.jjtGetChild(totalChilds-2).jjtAccept(this, data).asInstanceOf[String]
    
    val newFunction = new Function(id, TypeConverter.toType(fType), params)
    
    /* Adding Function Symbol Table */
    try{
      smt.put(newFunction.getName, newFunction)
    } catch{
      case ex: DuplicateElementException => {
                   errReporter.addError(new CompilationError(ex.getMessage))
                 
               }
    }
    node.jjtGetChild(totalChilds-1).jjtAccept(this, data) // visiting statements of the functions
    smt.dump()
    smt.exitCurrentScope()
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTFunctionCall,data: Object): Object = {
    print("Function Call : ")
    
    /* Function Name */
    val id = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    
    
    /* Arguments : a list of expression */
    var params: Array[Type] = null
    
    if(node.jjtGetNumChildren() > 1){
      //println("\tParameters : " + node.jjtGetChild(1).jjtGetNumChildren())
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
         newFunction = smt.getFunctionFromImport(newFunction).asInstanceOf[Function]
         
       }
       else{
         newFunction = smt.get(newFunction).asInstanceOf[Function]
         
       }
    }catch{
      case ex: java.util.NoSuchElementException => {
               errReporter.addError(new CompilationError(ex.getMessage))
      }
    }
   
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