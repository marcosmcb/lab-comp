#include <stdio.h>
#include <stdlib.h>

typedef int boolean;
#define true 1
#define false 0

typedef void(*Func)();

typedef struct _St_A {
   Func *vt;
   int    _A_k;   
} _class_A;

_class_A*new_A(void);
    int _A_get_A (_class_A *this) { 
return ;
}
    void _A_init (_class_A *this) { 
 =    1;
}
Func VTclass_A[] = {
( void (*)() ) _A_get_A
,( void (*)() ) _A_init

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
      int       _null_k;      
      int       _B_k;      
} _class_B;

_class_B*new_B(void);
       int _B_get_B (_class_B *this) { 
return ;
}
       void _B_init (_class_B *this) { 
      ;
 =       2;
}
Func VTclass_B[] = {
         ( void (*)() ) _A_get_A
         ( void (*)() ) _A_init
( void (*)() ) _B_get_B
,( void (*)() ) _B_init

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
         int          _null_k;         
         int          _C_k;         
} _class_C;

_class_C*new_C(void);
          int _C_get_C (_class_C *this) { 
return ;
}
          void _C_init (_class_C *this) { 
         ;
 =          3;
}
Func VTclass_C[] = {
            ( void (*)() ) _B_get_B
            ( void (*)() ) _B_init
            ( void (*)() ) _A_get_A
            ( void (*)() ) _A_init
( void (*)() ) _C_get_C
,( void (*)() ) _C_init

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
            int             _null_k;            
            int             _D_k;            
} _class_D;

_class_D*new_D(void);
             int _D_get_D (_class_D *this) { 
return ;
}
             void _D_init (_class_D *this) { 
            ;
 =             4;
}
Func VTclass_D[] = {
               ( void (*)() ) _C_get_C
               ( void (*)() ) _C_init
               ( void (*)() ) _B_get_B
               ( void (*)() ) _B_init
               ( void (*)() ) _A_get_A
               ( void (*)() ) _A_init
( void (*)() ) _D_get_D
,( void (*)() ) _D_init

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
_class_A *_a;
_class_B *_b;
_class_C *_c;
_class_D *_d;
               puts("");
               puts("Ok-ger14");
               puts("The output should be :");
               puts("4 3 2 1");
_d = new_D();
               ( (void (*) (_class_D *) ) _d->vt[1]) (_d);
               printf("%d ",( (int (*) (_class_D *) ) _d->vt[0]) (_d));
_c = _d;
               printf("%d ",( (int (*) (_class_C *) ) _c->vt[0]) (_c));
_b = _c;
               printf("%d ",( (int (*) (_class_B *) ) _b->vt[0]) (_b));
_a = _b;
               printf("%d ",( (int (*) (_class_A *) ) _a->vt[0]) (_a));
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
