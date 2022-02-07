import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FunctionTest {

    @Test
    public void basicFunctionTokenizesAndParsesCorrectly(){

        String treeResult = "(prog begin (func (type (baseType int)) "
                          + "foo ( ) is (stat skip) end) (stat skip) end <EOF>)";
        String program = "begin\n"
                       + "int foo ( ) is skip end\n"
                       + "skip\n"
                       + "end";

        assertEquals(treeResult, Compiler.lexAnalyse(program));
    }

    @Test
    public void functionWithParamsTokenizesAndParsesCorrectly(){

        String treeResult = "(prog begin (func (type (baseType int)) "
                          + "foo ( (paramList (param (type (baseType int)) x) , "
			  + "(param (type (baseType int)) y)) ) is (stat skip) end) " 
			  + "(stat skip) end <EOF>)";
        String program = "begin\n"
                       + "int foo (int x, int y) is skip end\n"
                       + "skip\n"
                       + "end";

	System.out.println(Compiler.lexAnalyse(program));

        assertEquals(treeResult, Compiler.lexAnalyse(program));
    }

}

