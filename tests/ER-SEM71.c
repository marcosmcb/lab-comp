#include <stdio.h>
#include <stdlib.h>

typedef int boolean;
#define true 1
#define false 0

typedef void(*Func)();

typedef struct _St_Program {
   Func *vt;
   int    _Program_x;   
} _class_Program;

_class_Program*new_Program(void);
    void _Program_set () { 
 =    0;
}
    void _Program_run (_class_Program *this) { 
}
Func VTclass_Program[] = {
( void (*)() ) _Program_set
,( void (*)() ) _Program_run

};

_class_Program *new_Program()
{
_class_Program*t;
if ( (t = malloc(sizeof(_class_Program))) != NULL )
t->vt = VTclass_Program;
return t;
}

int main() {
   _class_Program *program;
   program = new_Program();
   ( ( void (*)(_class_Program *) ) program->vt[1]) (program);
   return 0;
}
