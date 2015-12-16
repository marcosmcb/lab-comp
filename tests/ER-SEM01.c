#include <stdio.h>
#include <stdlib.h>

typedef int boolean;
#define true 1
#define false 0

typedef void(*Func)();

typedef struct _St_Program {
   Func *vt;
} _class_Program;

_class_Program*new_Program(void);
    int _Program_m (_class_Program *this) { 
int _i;
_i =    1;
while ( (_i <    10) ){
      printf("%d ",_i);
_i = (_i +       1);
;
}
}
    void _Program_run (_class_Program *this) { 
}
Func VTclass_Program[] = {
( void (*)() ) _Program_m
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
