import ast.Program;
import ast.Function;
import ast.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ASTVisitor {

    //Returns a list of assembly instructions that represent the given program
    public List<String> visitProgram(Program program) {

        List<String> functionInstructions = new ArrayList<>();

        //Generate the assembly instructions for each function
        for (Function function : program.getFunctions()) {
            functionInstructions.addAll(visitFunction(function));
        }

        //Generate the assembly instructions for the
        List<String> statementInstructions = visitStatement(program.getStatement());
        return Stream.of(functionInstructions, statementInstructions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<String> visitFunction(Function function) {

    }

    public List<String> visitStatement(Statement statement) {

    }
}
