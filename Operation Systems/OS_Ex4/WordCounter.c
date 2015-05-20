/* Operating Systems - Exercise 4
 * Matan Gidnian - 200846905
 * Ori Avraham - 201531290
 */

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "WordCounter.h"

#define MAX_BOUNDED_BUFF_CAPACITY 50
#define MAX_LOG_LINE_SIZE 1024
#define MAX_LINE_SIZE 1024
#define MAX_DATE_SIZE 25

#define DATE_FORMAT "%Y-%m-%d %H:%M:%S"
#define token_SPACE "\n"

#define SHOW_LOG "Logging results to file %s\n"
#define EXIT_MSG "So long, and thanks for all the fish.\n"
#define WAIT_MSG "Waiting for a connection\n"
#define INFO_USAGE "USAGE: WordCounter pipe_file_name destination_log_file_name\n"
#define INFO_CONNECTED "Connection received\n"
#define INFO_COUNT "Counting words for file\n"
#define INFO_EXSITANCE "File requested to be read from does not exist\n"

#define CMD_EXIT "exit\n"
#define ERR_CANT_CONNECT "Error: could not connect!\n"
#define ERR_CANT_OPEN "Error: could not open file %s!\n"
#define ERR_CANT_COUNT "Error: could not open file %s for counting!\n"

/*
 * A helper function that prints current time to a string.
 * Time is printed in the format required by exercise specification.
 */
void dateprintf(char *buff, int max_size, const char *format) {
	time_t timer;
	struct tm *tm_info;
	time(&timer);
	tm_info = localtime(&timer);
	strftime(buff, max_size, format, tm_info);
}

/*
 * Function to determine if a character is alphabetic
 * returns 1 if alphabetic, 0 if not
 */
int is_alphabetic(unsigned char c) {
	if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
		return 1;
	} else {
		return 0;
	}
}

/*
 * Listener thread starting point.
 * Creates a named pipe (the name should be supplied by the main function) and waits for
 * a connection on it. On connection, when received, reads the data from it and
 * parses the file names out of the data buffer. Each file name is copied to a new string
 * and then enqueued to the files queue.
 * On enqueue fails (returns 0), the application is trying to exit.
 * Therefore the Listener thread should stop. Before stopping, it should remove the pipe file and
 * free the memory of the filename it failed to enqueue.
 */
void *run_listener(void *param) {
	ListenerData *data;
	BoundedBuffer *buff;
	int fd, num;
	char messages[READ_BUFF_SIZE], *token, *copy_name;

	data = (ListenerData*) param;
	buff = data->buff;

	// Create pipe with r/w permission
	mknod(data->pipe, S_IFIFO | FILE_ACCESS_RW, 0);

	while (1) {

		// Wait for a connection on the pipe
		printf(WAIT_MSG);
		fd = open(data->pipe, O_RDONLY);
		if (fd <= 0) {
			printf(ERR_CANT_CONNECT);
			return NULL;
		}
		printf(INFO_CONNECTED);

		// Read data from pipe
		while ((num = read(fd, messages, READ_BUFF_SIZE)) > 0) {

			messages[num] = '\0';

			// Parse messages into filename tokens
			token = strtok(messages, token_SPACE);
			while (token != NULL) {

				// Get a copy of the file name
				copy_name = malloc((strlen(token) + 1) * sizeof(char));
				strcpy(copy_name, token);

				// Try to enqueue, and check if the application tried to exit
				if (bounded_buffer_enqueue(buff, copy_name) == 0) {
					// Clean: Free & Close resources.
					free(copy_name);
					remove(data->pipe);
					close(fd);
					return NULL; // Finish
				}
				token = strtok(NULL, token_SPACE);
			}
		}
		close(fd);
	}
	free(copy_name);
	return NULL;
}

/*
 * Entry point: WordCounter
 * The word counter reads file names from the files queue, one by one, and counts the words in them.
 * It will write the result to a log file.
 * Then it should free the memory of the dequeued file name string (it was allocated by the Listener thread).
 * If the dequeue operation fails (returns NULL), it means that the application is trying
 * to exit and therefore the thread should simply terminate.
 */
void *run_wordcounter(void *param) {
	WordCounterData *data;
	BoundedBuffer *buff;
	int word_count;
	char *dequeued_file;

	data = (WordCounterData*) param;
	buff = data->buff;

	// Keep dequeuing until signal is given to exit application (NULL)
	while ((dequeued_file = bounded_buffer_dequeue(buff)) != NULL) {

		// Count words in file
		word_count = count_words_in_file(dequeued_file);

		// Log
		log_count(data, dequeued_file, word_count);

		// Free buffer holding the file name
		free(dequeued_file);
	}
	return NULL;
}

/*
 * A word-counting function. Counts the words in file_name and returns the number.
 */
int count_words_in_file(char *file_name) {
	unsigned char buffer[FILE_READ_BUFFER_SIZE];
	int i, is_alpha, chars_read, num_words = 0, in_word = 0;
	FILE *file;

	// Open file, and return -1 on error
	file = fopen(file_name, "r");
	if (!file) {
		printf(ERR_CANT_COUNT, file_name);
		return -1;
	}

	printf("%s %s\n", INFO_COUNT, file_name);

	// Read file in chunks until EOF is reached
	while (fgets((char*) buffer, FILE_READ_BUFFER_SIZE, file) != NULL) {
		chars_read = strlen((char*) buffer);

		for (i = 0; i < chars_read; i++) {
			is_alpha = is_alphabetic(buffer[i]);

			// Match, but does not interrupt. counts.
			if (is_alpha && !in_word) {
				num_words++;
				in_word = 1;

			// Otherwise if it's whitespace and we were in a word
			} else if (!is_alpha && in_word) {
				in_word = 0; // change flag to false
			}
		}
	}
	fclose(file);
	return num_words;
}

/*
 * logs the number of words in the file to the output log file.
 */
void log_count(WordCounterData *counter_data, char *file_name, int count) {
	
	char buffer[FILE_READ_BUFFER_SIZE], time_buff[MAX_DATE_SIZE];

	// Exit if file could not counted
	if (count == -1)
		return;

	// Get time log
	dateprintf(time_buff, MAX_DATE_SIZE, DATE_FORMAT);

	// Save log line to string
	sprintf(buffer, "%s File: %s || Number of words: %d\n", time_buff, file_name, count);

	printf("%s File: %s || Number of words: %d\n", time_buff, file_name, count);

	// Output line to log file
	fputs(buffer, counter_data->log_file);
}

/*
 * Main function.
 * Reads command line arguments in the format:
 * 		./WordCounter pipe_name destination_log_file_name
 * Where pipe_name is the name of FIFO pipe that the Listener should create and
 * destination_log_file_name is the destination file name where the log should be written.
 * This function should create the files queue and prepare the parameters to the Listener and
 * WordCounter threads. Then, it should create these threads.
 * After threads are created, this function should control them as follows:
 * it should read input from user, and if the input line is "exit" (or "exit\n"), it should
 * set the files queue as "finished". This should make the threads terminate (possibly only
 * when the next connection is received).
 * At the end the function should join the threads and exit.
 */
int main(int argc, char *argv[]) {
	pthread_t wordcounter, listener;
	char *pipe_file_name, *destination_log_file;
	char input_buffer[STDIN_READ_BUFF_SIZE];
	BoundedBuffer bounded_buff;
	WordCounterData counter_data;
	ListenerData listener_data;

	// Check argument count
	if (argc != 3) {
		printf(INFO_USAGE);
		return 1;
	}

	// Get arguments
	pipe_file_name = argv[1];
	destination_log_file = argv[2];

	// Verify given file to readfrom exists
	int result;
	result = access (pipe_file_name, F_OK);
	
	if (result != 0)
	{
	    printf(INFO_EXSITANCE);
		return 1;
	}

	// Initialize bounded buffer
	bounded_buffer_init(&bounded_buff, MAX_BOUNDED_BUFF_CAPACITY);

	// Initialize word counter data
	counter_data.buff = &bounded_buff;
	counter_data.log_file = fopen(destination_log_file, "w");

	if (!counter_data.log_file) {
		printf(ERR_CANT_OPEN, destination_log_file);
		end_bounded_buffer(&bounded_buff);
		terminate_bounded_buffer(&bounded_buff);

		//Finish
		return 1;
	}

	// Listener initialization
	listener_data.buff = &bounded_buff;
	listener_data.pipe = pipe_file_name;

	printf(SHOW_LOG, destination_log_file);

	// Start threads
	pthread_create(&listener, NULL, run_listener, (void*)(&listener_data));
	pthread_create(&wordcounter, NULL, run_wordcounter, (void*)(&counter_data));

	// Read input in a loop until exit command is given
	do {

		// Get entered inout
		fgets(input_buffer, STDIN_READ_BUFF_SIZE, stdin);
	
	} while (strcmp(input_buffer, CMD_EXIT) != 0);

	// Mark end
	end_bounded_buffer(&bounded_buff);

	// Join threads
	pthread_join(wordcounter, NULL);
	pthread_join(listener, NULL);

	// Close log file
	fclose(counter_data.log_file);

	// Termination
	terminate_bounded_buffer(&bounded_buff);

	// END & EXIT
	printf(EXIT_MSG);
	return 0;
}


