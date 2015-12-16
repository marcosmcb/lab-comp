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
Func VTclass_A[] = {

};

_class_A *new_A()
{
_class_A*t;
if ( (t = malloc(sizeof(_class_A))) != NULL )
t->vt = VTclass_A;
return t;
}

typedef struct _St_B {
      Func *vt;
} _class_B;

_class_B*new_B(void);
Func VTclass_B[] = {

};

_class_B *new_B()
{
_class_B*t;
if ( (t = malloc(sizeof(_class_B))) != NULL )
t->vt = VTclass_B;
return t;
}

int main() {
      _class_Program *program;
      program = new_Program();
      ( ( void (*)(_class_Program *) ) program->vt[-1]) (program);
      return 0;
}
