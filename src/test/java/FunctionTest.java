import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FunctionTest {

    @Test
    public void unaryOperBangTokenizesCorrectly(){

        String treeResult = "(prog begin (func (type (baseType int)) "
                          + "foo ( ) is (stat skip) end) (stat skip) end <EOF>)";
        String program = "begin\n"
                       + "int foo ( ) is skip end\n"
                       + "skip\n"
                       + "end";

        assertEquals(treeResult, Compiler.lexAnalyse(program));
    }

}
