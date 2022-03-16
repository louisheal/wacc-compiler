package assembly.instructions;

public class Directive extends Instruction {

    private final DirectiveType type;

    public Directive(DirectiveType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    public enum DirectiveType {
        DATA{
            @Override
            public String toString() {
                return ".data";
            }
        },
        GLOBAL{
            @Override
            public String toString() {
                return ".global main";
            }
        },
        LTORG{
            @Override
            public String toString() {
                return ".ltorg";
            }
        },
        TEXT{
            @Override
            public String toString() {
                return ".text";
            }
        }
    }
}
