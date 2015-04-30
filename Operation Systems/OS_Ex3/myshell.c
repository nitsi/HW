// #include <stdio.h>
// #include <stdlib.h>
// #include <unistd.h>
// #include <sys/types.h>
//
// char *inputString(FILE* fp, size_t size){
// //The size is extended by the input with the value of the provisional
//     char *str;
//     int ch;
//     size_t len = 0;
//     str = realloc(NULL, sizeof(char)*size);//size is start size
//     if(!str)return str;
//     while(EOF!=(ch=fgetc(fp)) && ch != '\n'){
//         str[len++]=ch;
//         if(len==size){
//             str = realloc(str, sizeof(char)*(size+=16));
//             if(!str)return str;
//         }
//     }
//     str[len++]='\0';
//
//     return realloc(str, sizeof(char)*len);
// }
//
// int main() {
// 	printf("Welcome to MyShell v0.1, Thank you and Fuck off\n");
// 	char *m;
//
// 	    printf("input string : ");
// 	    m = inputString(stdin, 10);
// 	    printf("%s\n", m);
//
// 	    free(m);
// 	    return 0;
// }

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char* readinput()
{
#define CHUNK 200
   char* input = NULL;
   char tempbuf[CHUNK];
   size_t inputlen = 0, templen = 0;
   do {
       fgets(tempbuf, CHUNK, stdin);
       templen = strlen(tempbuf);
       inputlen += templen;
       input = realloc(input, inputlen+1);
       strcat(input, tempbuf);
    } while (templen==CHUNK-1 && tempbuf[CHUNK-2]!='\n');
    return input;
}

int main()
{
    char* result = readinput();
    printf("And the result is [%s]\n", result);
    free(result);
    return 0;
}