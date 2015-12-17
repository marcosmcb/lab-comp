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
    void _A_m (_class_A *this) { 
}
Func VTclass_A[] = {
( void (*)() ) _A_m

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
       void _B_m (_class_B *this) { 
}
Func VTclass_B[] = {
( void (*)() ) _B_m

};

_class_B *new_B()
{
_class_B*t;
if ( (t = malloc(sizeof(_class_B))) != NULL )
t->vt = VTclass_B;
return t;
}

typedef struct _St_C {
         Func *vt;
} _class_C;

_class_C*new_C(void);
          void _C_p (_class_C *this) { 
}
Func VTclass_C[] = {
( void (*)() ) _B_m
,( void (*)() ) _A_m
,( void (*)() ) _C_p

};

_class_C *new_C()
{
_class_C*t;
if ( (t = malloc(sizeof(_class_C))) != NULL )
t->vt = VTclass_C;
return t;
}

typedef struct _St_Program {
            Func *vt;
} _class_Program;

_class_Program*new_Program(void);
             void _Program_run (_class_Program *this) { 
_class_C *_c;
_c = new_C();
            ( (void (*) (_class_C *) ) _c->vt[0]) (_c);
}
Func VTclass_Program[] = {
( void (*)() ) _Program_run

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
            ( ( void (*)(_class_Program *) ) program->vt[0]) (program);
            return 0;
}
