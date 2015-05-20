all: wordcounter

clean:
	rm wordcounter *.o

wordcounter: WordCounter.o BoundedBuffer.o
	gcc -o wordcounter WordCounter.o BoundedBuffer.o -pthread

BoundedBuffer.o:
	gcc -c BoundedBuffer.c -Wall

WordCounter.o:
	gcc -c WordCounter.c -Wall
