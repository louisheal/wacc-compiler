# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler

# Useful locations

ANTLR_DIR	 := antlr_config
SOURCE_DIR	 := src/main/java
ANTLR_SOURCE_DIR := $(SOURCE_DIR)/antlr
OUTPUT_DIR	 := bin

# Project tools

ANTLR	:= antlrBuild
MKDIR	:= mkdir -p
JAVAC	:= javac
RM	:= rm -rf

# Configure project Java flags

JFLAGS	:= -sourcepath $(SOURCE_DIR) -d $(OUTPUT_DIR) -cp lib/antlr-4.9.3-complete.jar 


# The make rules:

# run the antlr build script then attempts to compile all .java files within src/antlr
all: antlr
	mvn package

compile: antlr
	mvn compile

testAll: antlr
	mvn test

testAnalysis: antlr
	mvn test -Dtest="BinaryLexerTest"
	mvn test -Dtest="BinOpPrecedenceTest"
	mvn test -Dtest="FunctionTest"
	mvn test -Dtest="IntLiterTest"
	mvn test -Dtest="StatTest"
	mvn test -Dtest="UnaryOperLexerTest"

testValid: antlr
	mvn test -Dtest="ValidTest"

testSyntax: antlr
	mvn test -Dtest="InvalidSyntaxTest"#

testSemantics: antlr
	mvn test -Dtest="InvalidSemanticTest"

antlr:
	cd $(ANTLR_DIR) && ./$(ANTLR) 
	$(MKDIR) $(OUTPUT_DIR)
	$(JAVAC) $(JFLAGS) $(ANTLR_SOURCE_DIR)/*.java

# clean up all of the compiled files
clean:
	$(RM) $(OUTPUT_DIR) $(SOURCE_DIR)/antlr target

.PHONY: all compile test antlr clean 
