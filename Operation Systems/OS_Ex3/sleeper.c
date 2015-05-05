#include <stdio.h>
#include <unistd.h>

int main() {
	int i;
	for (i = 1; i <= 10; i++) {
		printf("%d\n", i);
		sleep(1);
	}
	return 0;
}
