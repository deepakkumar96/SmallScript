package com.smallscript.codegenerator

import cafebabe.ClassFile
import cafebabe.CodeHandler
import cafebabe.ByteCodes._
import cafebabe.AbstractByteCodes._
import com.smallscript.errorreporting._
import cafebabe.Flags
//import java.util.List
import scala.Option
//import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC


class CodeGenerator(val fileName: String){
  
    val classFile = new ClassFile(fileName, Option.empty)
    var mainCh: CodeHandler = null
    var staticCh: CodeHandler = null
    
    classFile setSourceFile fileName
    classFile.addDefaultConstructor
    mainCh = classFile.addMainMethod.codeHandler
    
    val method = classFile.addMethod("V", "static_init", List.empty)
    method.setFlags(Flags.FIELD_ACC_STATIC)
    staticCh = method.codeHandler
    
    mainCh << InvokeStatic(fileName, "static_init", "()V")
    
    def saveAndWrite() = {
      mainCh << RETURN
      staticCh << RETURN
      mainCh.freeze
      staticCh.freeze
      classFile.writeToFile(fileName + ".class")
    }
  
}