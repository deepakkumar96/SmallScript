/* Generated By:JJTree: Do not edit this line. ASTMultiplyExpr.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.smallscript.parser;

public
class ASTMultiplyExpr extends SimpleNode {
  public ASTMultiplyExpr(int id) {
    super(id);
  }

  public ASTMultiplyExpr(SmallScriptParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SmallScriptParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7bce6fb5c793e190a0e2888996091e41 (do not edit this line) */
