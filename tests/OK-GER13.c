#include <stdio.h>
#include <stdlib.h>

typedef int boolean;
#define true 1
#define false 0

typedef void(*Func)();

typedef struct _St_A {
   Func *vt;
   int    _A_n;   
} _class_A;

_class_A*new_A(void);
    int _A_get (_class_A *this) { 
return ;
}
    void _A_set (_class_A *this, int _n) { 
 = _n;
}
    void _A_m1 (_class_A *this) { 
   printf("%d ",);
}
Func VTclass_A[] = {
( void (*)() ) _A_get
,( void (*)() ) _A_set
,( void (*)() ) _A_m1

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
      int       _null_n;      
} _class_B;

_class_B*new_B(void);
       void _B_m2 (_class_B *this) { 
}
Func VTclass_B[] = {
         ( void (*)() ) _A_get
,         ( void (*)() ) _A_set
,         ( void (*)() ) _A_m1
,( void (*)() ) _B_m2

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
          void _C_m1 (_class_C *this) { 
         printf("%d ",         8);
}
          void _C_teste (_class_C *this) { 
         _B_m1( (_class_B *) this,  );
}
Func VTclass_C[] = {
            ( void (*)() ) _B_m2
,            ( void (*)() ) _A_get
,            ( void (*)() ) _A_set
,            ( void (*)() ) _A_m1
,( void (*)() ) _C_m1
,( void (*)() ) _C_teste

};

_class_C *new_C()
{
_class_C*t;
if ( (t = malloc(sizeof(_class_C))) != NULL )
t->vt = VTclass_C;
return t;
}

typedef struct _St_D {
            Func *vt;
} _class_D;

_class_D*new_D(void);
             void _D_m1 (_class_D *this) { 
            printf("%d ",            9);
}
Func VTclass_D[] = {
               ( void (*)() ) _C_m1
,               ( void (*)() ) _C_teste
,               ( void (*)() ) _B_m2
,               ( void (*)() ) _A_get
,               ( void (*)() ) _A_set
,               ( void (*)() ) _A_m1
,( void (*)() ) _D_m1

};

_class_D *new_D()
{
_class_D*t;
if ( (t = malloc(sizeof(_class_D))) != NULL )
t->vt = VTclass_D;
return t;
}

typedef struct _St_Program {
               Func *vt;
} _class_Program;

_class_Program*new_Program(void);
                void _Program_run (_class_Program *this) { 
_class_D *_d;
               puts("");
               puts("Ok-ger09");
               puts("The output should be :");
               puts("0");
_d = new_D();
               ( (void (*) (_class_D *, int) ) _d->vt[-1]) (_d,                0);
               ( (void (*) (_class_D *) ) _d->vt[-1]) (_d);
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
