/* Generated By:JJTree: Do not edit this line. ASTStringLiteral.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.smallscript.parser;

public
class ASTStringLiteral extends SimpleNode {
  public ASTStringLiteral(int id) {
    super(id);
  }

  public ASTStringLiteral(SmallScriptParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SmallScriptParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ff3b56be82621698f7fd9ac80b983811 (do not edit this line) */
