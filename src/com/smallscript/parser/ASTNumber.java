/* Generated By:JJTree: Do not edit this line. ASTNumber.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.smallscript.parser;

public
class ASTNumber extends SimpleNode {
  public ASTNumber(int id) {
    super(id);
  }

  public ASTNumber(SmallScriptParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SmallScriptParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6da964bc6c5224135894548bc39e0e8f (do not edit this line) */
