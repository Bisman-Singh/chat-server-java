JAVAC = javac
JAVA = java
SRC = src
OUT = out

all: compile

compile:
	mkdir -p $(OUT)
	$(JAVAC) -d $(OUT) $(SRC)/*.java

run-server: compile
	$(JAVA) -cp $(OUT) ChatServer

run-client: compile
	$(JAVA) -cp $(OUT) ChatClient

clean:
	rm -rf $(OUT)

.PHONY: all compile run-server run-client clean
