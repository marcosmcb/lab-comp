#include <stdio.h>
#include <stdlib.h>

typedef int boolean;
#define true 1
#define false 0

typedef void(*Func)();

int main() {
_class_Program *program;
program = new_Program();
( ( void (*)(_class_Program *) ) program->vt[-1]) (program);
return 0;
}
