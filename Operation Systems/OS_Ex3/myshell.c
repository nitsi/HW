/* Operating Systems - Exercise 3
 * Matan Gidnian
 * 200846905
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#define SHELL_INPUT ">"
#define BACKGROUND '&'
#define PIPELINE '|'
#define COMMAND_SEPARATOR "&&"
#define TOKEN_SEPARATOR " \n"
#define MAX_LINE_LENGTH 255
#define MAX_LINE_ARGS 255
#define MAX_LINE_COMMANDS 16
#define EXIT_COMMAND "exit"
#define CD_COMMAND "cd"
#define CD_ERROR "No such file or directory"
#define COMMAND_ERROR "command not found"

/*
 * Reads a single line from the shell. The line is then parsed into commands, 
 * where every command is made up of the command call and its relevant arguments.
 * Returns the total amount of commands read and whether the final process should 
 * be run in the background. If the line is empty or an invalid call is made, the
 * function returns -1.
 */
int getAndParseInput(char *commands[], int *tokensSize, int *backgroundFlag) {
  char buffer[MAX_LINE_LENGTH], *commandPointer, *command, *token;
  int bufferLength, numOfCommands = 0, index = 0;

  // Read commands from terminal
  if (fgets(buffer, MAX_LINE_LENGTH, stdin) != NULL ) {

    bufferLength = strlen(buffer);

    // Empty line, do nothing
    if (bufferLength == 1) {
      return -1;
    }

    // Check if command is to be run on background
    if (buffer[bufferLength - 2] == BACKGROUND) {
      *backgroundFlag = 1;
    } else {
      *backgroundFlag = 0;
    }

    // Separate commands in line. Stop before pointer exceeds buffer size
    command = strtok(buffer, COMMAND_SEPARATOR);
    while (command != NULL && index < bufferLength) {
      tokensSize[numOfCommands] = 0;
      index += strlen(command) + strlen(COMMAND_SEPARATOR);

      // Move pointer to beginning of the next command
      commandPointer = buffer + index;

      // Separate tokens in command
      token = strtok(command, TOKEN_SEPARATOR);
      while (token != NULL ) {

        // If token is a pipe, put it in its own command
        // And treat it as a command separator
        if (*token == PIPELINE) {

          // Prevent use of pipeline when it is the first argument in command
          if (tokensSize[numOfCommands] == 0) {
            printf("%s: %s\n", token, COMMAND_ERROR);
            return -1;
          }

          // Prevent use of pipeline when it is the last argument in command
          token = strtok(NULL, TOKEN_SEPARATOR);
          if (token == NULL) {
            printf("%c: %s\n", PIPELINE, COMMAND_ERROR);
            return -1;
          }

          // Insert pipe into its own command
          numOfCommands++;
          commands[numOfCommands * MAX_LINE_ARGS] = malloc(2 * sizeof(char));
          strcpy(commands[numOfCommands * MAX_LINE_ARGS], "|");
          tokensSize[numOfCommands] = 1;

          // Prepare for the the next command
          numOfCommands++;
          tokensSize[numOfCommands] = 0;
          continue;
        }

        // Allocate memory for token and store it
        commands[(numOfCommands * MAX_LINE_ARGS)
            + tokensSize[numOfCommands]] = malloc(
            (strlen(token) + 1) * sizeof(char));
        strcpy(commands[(numOfCommands * MAX_LINE_ARGS) 
            + tokensSize[numOfCommands]], token);

        // Move onto next token
        tokensSize[numOfCommands]++;
        token = strtok(NULL, TOKEN_SEPARATOR);
      }
      
      // Move onto next command
      numOfCommands++;
      command = strtok(commandPointer, COMMAND_SEPARATOR);
    }
  }
  return numOfCommands;
}

/*
 * Frees all of the memory allocated towards storing the commands.
 */
void freeCommands(char **commands, int numOfCommands, int *tokensSize) {
  int i, j;

  for (i = 0; i < numOfCommands; i++) {
    for (j = 0; j < tokensSize[i]; j++) {
      free(commands[(i * MAX_LINE_ARGS) + j]);
      commands[(i * MAX_LINE_ARGS) + j] = NULL;
    }
  }
}

/*
 * Executes command. The method forks, checks for child processes and 
 * if the process to be run is a background thread. Supports pipelines.
 */
int executeCommand(char *command[], int backgroundFlag, char *nextCommand[], int lastCommand) {
  int status, pipeArray[2];
  pid_t processID, pipeID;

  // Pipe can only be called if a command follows it
  if (!lastCommand) {

    // Pipe was called
    if (nextCommand != NULL) {
      pipe(pipeArray);
    }

    // Prepare for a pipe
    else if (*command[MAX_LINE_ARGS] == PIPELINE) {
      return -1;
    }
  }
  processID = fork();

  // Child ID, execute
  if (!processID) {

    // Create writing descriptor for pipe
    if (nextCommand != NULL ) {

      // Duplicate stdout and execute first command
      close(1);
      dup(pipeArray[1]);
      close(pipeArray[0]);

    }

    // Execute command
    execvp(command[0], command);
    exit(1);
  }

  // Parent ID, wait
  if (!backgroundFlag) {

    // Create reader descriptor for pipe
    if (nextCommand != NULL ) {

      // Let first process end, then fork again
      waitpid(processID, &status, 0);
      pipeID = fork();
      if (!pipeID) {

        // Duplicate stdin and execute second command
        close(0);
        dup(pipeArray[0]);
        close(pipeArray[1]);
        execvp(nextCommand[0], nextCommand);
      } else {

        // Close pipes
        close(pipeArray[0]);
        close(pipeArray[1]);
      }

    } else {

      // Wait for process to end
      waitpid(processID, &status, 0);
      return WEXITSTATUS(status);
    }
  }
  return 0;
}

int main(int argc, char *argv[]) {

  int errorFlag, tokensSize[MAX_LINE_ARGS], i, numOfCommands, backgroundFlag;
  char *commands[MAX_LINE_COMMANDS * MAX_LINE_ARGS];
  char *directory = getcwd(0, 0);

  printf("Welcome to MG-Shell!\n");
  printf("Bonus: IMPLEMENTED, but only for A SINGLE pipe :( \n");

  // Loop forever until 'exit' command is given
  while (1) {

    // Print shell prompt
    printf("%s%s ", directory, SHELL_INPUT);

    // Read from standard input and parse commands
    numOfCommands = getAndParseInput(commands, tokensSize, &backgroundFlag);

    // Empty line or improper value check
    if (numOfCommands == -1) {
      freeCommands(commands, numOfCommands, tokensSize);
      continue;
    }

    // Exit check
    if (strcmp(commands[0], EXIT_COMMAND) == 0) {
      freeCommands(commands, numOfCommands, tokensSize);
      break;
    }

    // Change directory check
    else if (strcmp(commands[0], CD_COMMAND) == 0) {

      // If chdir() fails throw error
      if (chdir(commands[1]) != 0) {
        printf("%s: %s\n", commands[1], CD_ERROR);
      } else {
        directory = getcwd(0, 0);
      }
      freeCommands(commands, numOfCommands, tokensSize);
      continue;

    } else {

      // Execute all commands other than last, none of which run in background
      for (i = 0; i < numOfCommands - 1; i++) {

        // If executeCommand() fails throw error, if it's a pipe increment
        errorFlag = executeCommand(&commands[i * MAX_LINE_ARGS], 0, NULL, 0);
        if (errorFlag != 0) {

          // Pipeline
          if (errorFlag == -1) {
            i++;
            executeCommand(&commands[(i - 1) * MAX_LINE_ARGS], 0,
                     &commands[(i + 1) * MAX_LINE_ARGS], 0);
            i++;

          }

          // Error executing command check
          else {
            printf("%s: %s\n", commands[i * MAX_LINE_ARGS], COMMAND_ERROR);
          }
        }
      }
    }

    // Execute final command, which might run in background
    errorFlag = executeCommand(&commands[i * MAX_LINE_ARGS], backgroundFlag, NULL, 1);

    // Error executing command check
    if (errorFlag != 0) {
      printf("%s: %s\n", commands[i * MAX_LINE_ARGS], COMMAND_ERROR);
    }

    // Free memory allocated to commands
    freeCommands(commands, numOfCommands, tokensSize);
  }
  return 0;
}
