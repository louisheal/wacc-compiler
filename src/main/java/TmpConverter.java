import ast.Function;
import ast.Program;
import ast.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO: Change to ASTVisitor<List<Instruction>>
public class TmpConverter extends ASTVisitor<List<String>> {

    @Override
    public List<String> visitProgram(Program program) {
        List<String> functionInstructions = new ArrayList<>();

        /* Generate the assembly instructions for each function. */
        for (Function function : program.getFunctions()) {
            functionInstructions.addAll(visitFunction(function));
        }

        /* Generate the assembly instructions for the program body. */
        List<String> statementInstructions = visitStatement(program.getStatement());

        /* Return the function assembly instructions concatenated with the assembly instructions
           for the program body. */
        return Stream.of(functionInstructions, statementInstructions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> visitConcatStatement(Statement statement) {
        /* Generate, concatenate and return the assembly instructions for both statements either
           side of the semicolon */
        return Stream.of(visitStatement(statement.getStatement1()),
                        visitStatement(statement.getStatement2()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
