/* Generated By:JJTree: Do not edit this line. ASTArrayLiteral.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.smallscript.parser;
import java.util.*;

public
class ASTArrayLiteral extends SimpleNode {
	
  public List<Node> literals = new ArrayList<>();	
	
  public ASTArrayLiteral(int id) {
    super(id);
  }

  public ASTArrayLiteral(SmallScriptParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SmallScriptParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5472bb52f6badaddd54a8b434e0a58f9 (do not edit this line) */