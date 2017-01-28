package com.smallscript.symanticanalyser

import com.smallscript.errorreporting.ErrorReporter
import com.smallscript.`type`._
import com.smallscript.util._

import com.smallscript.parser.SmallScriptParserVisitor
import com.smallscript.symboltable.SymbolTable
import com.smallscript.parser._
import com.smallscript.symboltable.DuplicateElementException
import com.smallscript.errorreporting._

class FunctionAnalyser(val smt: SymbolTable, val errReporter: ErrorReporter)
                      extends SmallScriptParserVisitor {
  
  val stack = new java.util.Stack[Object]
  
  
  
  def visit(node: com.smallscript.parser.ASTArrayAssignment,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTArrayAccess,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTArrayLiteral,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTNegateExpr,data: Object): Object = data
  
  def visit(node: com.smallscript.parser.ASTModuloExpr,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTDivideExpr,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTMultiplyExpr,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTSubractExpr,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTAddExpr,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTDblEqualNode,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTGENode,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTLENode,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTGTNode,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTLTNode,data: Object): Object = data
  
  
  
  def visit(node: com.smallscript.parser.ASTReturnStatement,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTFunctionCall,data: Object): Object = data
  
  
  
  def visit(node: com.smallscript.parser.ASTLoopStatement,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTWhileStatement,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTElse,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTIfStatement,data: Object): Object = data
  
  def visit(node: com.smallscript.parser.ASTPrint,data: Object): Object = data
  def visit(node: com.smallscript.parser.ASTAssignment,data: Object): Object = data
  
  
  //Declarations
  
  def visit(node: com.smallscript.parser.ASTidentifier,data: Object): Object = {
    //println("\nidentifier : ")
    val variable = node.jjtGetChild(0).jjtAccept(this, data)
    return variable 
  }
  
  def visit(node: com.smallscript.parser.ASTArg_DELC,data: Object): Object = {
    //print("Arg : " + node.jjtGetNumChildren())
    val varId = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    val varType = node.jjtGetChild(1).jjtAccept(this, data).asInstanceOf[String]
    val decl = new Var(varId, null, TypeConverter.toType(varType), smt.getCurrentNestingDepth)
    //println(decl)
    return decl
  }
  
  def visit(node: com.smallscript.parser.ASTVAL_DELC,data: Object): Object = {
    print("Val : ")
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTVAR_DELC,data: Object): Object = {
    print("Var : ")
    return data
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
    print("Expr : ")
    return node.childrenAccept(this, data)
  }
  
  def visit(node: com.smallscript.parser.ASTunaryExpression,data: Object): Object = {
    print(", => Uniary : ")
    return node.childrenAccept(this, data)
  }
  
  def visit(node: com.smallscript.parser.ASTStringLiteral,data: Object): Object = {
    
    return node.jjtGetValue()
  }
  
  def visit(node: ASTnumberExpression, data: Object): Object = {
    print("Num : " + node.jjtGetValue())
    return node.jjtGetValue()          
  }
  
  def visit(node: ASTNumber, data: Object): Object = {
                        data
                      }
  
  def visit(node: com.smallscript.parser.ASTVarialbleValue,data: Object): Object = {
    node.childrenAccept(this, data)
    return data
  }
  
  //Function
  
  def visit(node: com.smallscript.parser.ASTFunction,data: Object): Object = {
    print("\nFunction : " + node.jjtGetNumChildren())
    var totalChilds = 4
    /* Function Name */
    val id = node.jjtGetChild(0).jjtAccept(this, data).asInstanceOf[String]
    println("\n\tid : " + id)
    
    /* Parameters */
    println("\tParameters : " + node.jjtGetChild(1).jjtGetNumChildren())
    var params: Array[Type] = null
    
    if(node.jjtGetNumChildren() > 3){
    params = new Array[Type](node.jjtGetChild(1).jjtGetNumChildren())
    var num = 1
    for(i <- 0 to node.jjtGetChild(1).jjtGetNumChildren()-1){
      val paramDecl: Declaration = node.jjtGetChild(1).jjtGetChild(i).jjtAccept(this, data).asInstanceOf[Declaration]
      println("\t\t"+node.jjtGetChild(1).jjtGetChild(i) + " : " + paramDecl)
      params(i) = paramDecl.getType
    }
    }
    else
      totalChilds = 3
     
    
    /* Type Of Functions */
    val fType = node.jjtGetChild(totalChilds-2).jjtAccept(this, data).asInstanceOf[String]
    println("\tType : " + fType)
    
    val newFunction = new Function(id, TypeConverter.toType(fType), params)
    println("Function => " + newFunction)
    
    try{
      smt.put(newFunction.getName, newFunction)
    } catch{
      case ex: DuplicateElementException => {
                    println(ex.getMessage)
                    errReporter.addError(new CompilationError(ex.getMessage))
                 
               }
    }
    return data
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
    println("statement blocks")
    return data
  }
  
  
  def visit(node: com.smallscript.parser.ASTStatement,data: Object): Object = {
    node.childrenAccept(this, data)
    return data;
  }
  
  def visit(node: com.smallscript.parser.ASTstart,data: Object): Object = {
    println("Starting Function PreScanning...")
    node.childrenAccept(this, data)
    return data;
  }
  
  
  def visit(node: com.smallscript.parser.SimpleNode,data: Object): Object = {
    println("Start :")
    return data;
  }

  def visit(node: ASTImport, data: Object): Object = {
                        data
                      }

  def visit(node: ASTCharLiteral, data: Object): Object = {
                        data
                      }

  def visit(node: ASTExpressionParameters, data: Object): Object = {
                        data
                      }

  

  
  
}