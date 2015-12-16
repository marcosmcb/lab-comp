#include <stdio.h>
#include <stdlib.h>

typedef int boolean;
#define true 1
#define false 0

typedef void(*Func)();

typedef struct _St_A {
   Func *vt;
} _class_A;

_class_A*new_A(void);
    int _A_get0 () { 
return    0;
}
    int _A_get1 () { 
return    1;
}
    int _A_ident (, int _n) { 
return _n;
}
Func VTclass_A[] = {
( void (*)() ) _A_get0
,( void (*)() ) _A_get1
,( void (*)() ) _A_ident

};

_class_A *new_A()
{
_class_A*t;
if ( (t = malloc(sizeof(_class_A))) != NULL )
t->vt = VTclass_A;
return t;
}

int main() {
   _class_Program *program;
   program = new_Program();
   ( ( void (*)(_class_Program *) ) program->vt[-1]) (program);
   return 0;
}
