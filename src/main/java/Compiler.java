import antlr.*;
import assembly.instructions.Instruction;
import ast.Program;
import java.io.FileWriter;
import java.util.List;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Path;

import static java.lang.System.exit;

public class Compiler {

  public static CommonTokenStream tokenize(CharStream input) {
    BasicLexer lexer = new BasicLexer(input);
    return new CommonTokenStream(lexer);
  }

  public static String parseTree(CommonTokenStream tokens) {
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();
    return tree.toStringTree(parser);
  }

  public static ParseTree parse(CommonTokenStream tokens) {
    BasicParser parser = new BasicParser(tokens);
    ParseTree tree = parser.prog();

    if (parser.getNumberOfSyntaxErrors() > 0) {
      exit(100);
    }

    return tree;
  }
  
  public static String lexAnalyse(String program) {
    CharStream input = CharStreams.fromString(program);
    return parseTree(tokenize(input));
  }

  public static Program compile(String filename) throws IOException {

    Path filepath = Path.of(filename);

    CharStream input = CharStreams.fromPath(filepath);

    ParseTree tree = parse(tokenize(input));

    ASTBuilder astBuilder = new ASTBuilder();
    Program ast = (Program) astBuilder.visit(tree);

    SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
    semanticAnalysis.traverse(ast);

    if (semanticAnalysis.getNumberOfErrors() > 0) {
      for (String errorMsg : semanticAnalysis.getErrorMsgs()) {
        System.out.println(errorMsg);
      }
      exit(200);
    }

    return ast;
  }

  public static Program compileProgram(String program) {

    CharStream input = CharStreams.fromString(program);

    ParseTree tree = parse(tokenize(input));

    ASTBuilder astBuilder = new ASTBuilder();
    Program ast = (Program) astBuilder.visit(tree);

    SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
    semanticAnalysis.traverse(ast);

    if (semanticAnalysis.getNumberOfErrors() > 0) {
      for (String errorMsg : semanticAnalysis.getErrorMsgs()) {
        System.out.println(errorMsg);
      }
      exit(200);
    }

    return ast;
  }

  public static void main(String[] args) throws IOException {

    Path filename = Path.of(args[0]);

    Program ast = compile(new String(args[0]));

    Evaluator evaluator = new Evaluator();
    evaluator.visitProgram(ast);

    Converter converter = new Converter();
    List<Instruction> instructions = converter.visitProgram(ast);

    String fileNameWithExtension = filename.getFileName().toString();
    String fileName = fileNameWithExtension.substring(0,fileNameWithExtension.lastIndexOf("."))
        + ".s";
    FileWriter binFileWriter = new FileWriter(fileName);
    for (Instruction instruction: instructions){
      binFileWriter.write(instruction.toString() + '\n');
    }
    binFileWriter.close();

  }

}
