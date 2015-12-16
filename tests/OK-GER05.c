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
int _a;
int _b;
int _c;
int _d;
int _e;
int _f;
{
   char __a[512], __b[512], __c[512], __d[512], __e[512], __f[512];

   gets(__a);
   sscanf(__a, "%d", &_a);

   gets(__b);
   sscanf(__b, "%d", &_b);

   gets(__c);
   sscanf(__c, "%d", &_c);

   gets(__d);
   sscanf(__d, "%d", &_d);

   gets(__e);
   sscanf(__e, "%d", &_e);

   gets(__f);
   sscanf(__f, "%d", &_f);

}
   printf("%d ",_a);
   printf("%d ",_b);
   printf("%d ",_c);
   printf("%d ",_d);
   printf("%d ",_e);
   printf("%d ",_f);
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

typedef struct _St_Program {
      Func *vt;
} _class_Program;

_class_Program*new_Program(void);
       void _Program_run (_class_Program *this) { 
_class_A *_a;
      puts("");
      puts("Ok-ger05");
      puts("The output should be what you give as input.");
      puts("Type in six numbers");
_a = new_A();
      ( (void (*) (_class_A *) ) _a->vt[0]) (_a);
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
