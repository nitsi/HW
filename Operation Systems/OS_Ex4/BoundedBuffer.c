/* Operating Systems - Exercise 4
 * Matan Gidnian - 200846905
 * Ori Avraham - 201531290
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <pthread.h>
#include "BoundedBuffer.h"

/*
 * Initializes the buffer with the specified capacity.
 * This function should allocate the buffer, initialize its properties
 * and also initialize its mutex and condition variables.
 * It should set its finished flag to 0.
 */
void bounded_buffer_init(BoundedBuffer *buff, int capacity) {
	buff->buffer = malloc(capacity * sizeof(char*));
	buff->size = 0;
	buff->capacity = capacity;
	buff->head = 0;
	buff->tail = 0;
	pthread_mutex_init(&(buff->mutex), NULL);
	pthread_cond_init(&(buff->cv_empty), NULL);
	pthread_cond_init(&(buff->cv_full), NULL);
	buff->finished = 0;
}

/*
 * Enqueue a string (char pointer) to the buffer.
 * This function should add an element to the buffer. If the buffer is full,
 * it should wait until it is not full, or until it has finished.
 * If the buffer has finished (either after waiting or even before), it should
 * simply return 0.
 * If the enqueue operation was successful, it should return 1. In this case it
 * should also signal that the buffer is not empty.
 * This function should be synchronized on the buffer's mutex!
 */
int bounded_buffer_enqueue(BoundedBuffer *buff, char *data) {

	// Lock
	pthread_mutex_lock(&(buff->mutex));

	// Buffer complete
	if (buff->finished) {
		pthread_mutex_unlock(&(buff->mutex));
		return 0;
	}

	// Wait for space
	while (buff->size == buff->capacity) {
		pthread_cond_wait(&(buff->cv_empty), &(buff->mutex));

		// Buffer complete
		if (buff->finished) {
			pthread_mutex_unlock(&(buff->mutex));
			return 0;
		}
	}

	// Load data to buffer and signal complete
	buff->buffer[buff->tail] = data;
	buff->tail = (buff->tail + 1) % buff->capacity;
	buff->size++;
	pthread_cond_signal(&(buff->cv_full));

	// Unlock
	pthread_mutex_unlock(&(buff->mutex));
	return 1;

}

/*
 * Dequeues a string (char pointer) from the buffer.
 * This function should remove the head element of the buffer and return it.
 * If the buffer is empty, it should wait until it is not empty, or until it has finished.
 * If the buffer has finished (either after waiting or even before), it should
 * simply return NULL.
 * If the dequeue operation was successful, it should signal that the buffer is not full.
 * This function should be synchronized on the buffer's mutex!
 */
char *bounded_buffer_dequeue(BoundedBuffer *buff) {
	char* data;

	// Lock
	pthread_mutex_lock(&(buff->mutex));

	while (buff->size == 0) {

		// Buffer complete
		if (buff->finished) {
			pthread_mutex_unlock(&(buff->mutex));
			return NULL;
		}

		// Waiting for action.. 
		pthread_cond_wait(&(buff->cv_full), &(buff->mutex));
	}

	// Save data from buffer and signal complete
	data = buff->buffer[buff->head];
	buff->head = (buff->head + 1) % buff->capacity;
	buff->size--;
	pthread_cond_signal(&(buff->cv_empty));

	// Unlock
	pthread_mutex_unlock(&(buff->mutex));
	return data;
}

/*
 * Sets the buffer as finished.
 * This function sets the finished flag to 1 and then wakes up all threads that are
 * waiting on the condition variables of this buffer.
 * This function should be synchronized on the buffer's mutex!
 */
void bounded_buffer_finish(BoundedBuffer *buff) {

	// Lock
	pthread_mutex_lock(&(buff->mutex));

	// Set finish and notify
	buff->finished = 1;
	pthread_cond_signal(&(buff->cv_empty));
	pthread_cond_signal(&(buff->cv_full));

	// Unlock
	pthread_mutex_unlock(&(buff->mutex));
}

/*
 * Frees the buffer memory and destroys mutex and condition variables.
 */
void bounded_buffer_destroy(BoundedBuffer *buff) {
	free(buff->buffer);
	pthread_mutex_destroy(&(buff->mutex));
	pthread_cond_destroy(&(buff->cv_empty));
	pthread_cond_destroy(&(buff->cv_full));
}
