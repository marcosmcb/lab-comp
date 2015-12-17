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
    int _A_m1 (_class_A *this, boolean _ok) { 
return    0;
}
    void _A_m2 (_class_A *this) { 
}
    String _A_m3 (_class_A *this, String _sboolean _ok) { 
return A;
}
    String _A_m4 (_class_A *this, int _iboolean _ok) { 
return Am4;
}
Func VTclass_A[] = {
( void (*)() ) _A_m1
,( void (*)() ) _A_m2
,( void (*)() ) _A_m3
,( void (*)() ) _A_m4

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
       int _B_m1 (_class_B *this, boolean _ok) { 
return       1;
}
       void _B_m2 (_class_B *this) { 
}
       int _B_mB (_class_B *this) { 
return       1;
}
Func VTclass_B[] = {
( void (*)() ) _B_m1
,( void (*)() ) _B_m2
,( void (*)() ) _A_m4
,( void (*)() ) _B_mB

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
          int _C_m1 (_class_C *this, boolean _ok) { 
return          2;
}
          String _C_m4 (_class_C *this, int _iboolean _ok) { 
return C;
}
          String _C_m5 (_class_C *this) { 
return finally;
}
Func VTclass_C[] = {
( void (*)() ) _C_m1
,( void (*)() ) _C_m1
,( void (*)() ) _A_m2
,( void (*)() ) _C_m4
,( void (*)() ) _C_m5

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
Func VTclass_D[] = {
( void (*)() ) _A_m1
,( void (*)() ) _A_m2
,( void (*)() ) _A_m4

};

_class_D *new_D()
{
_class_D*t;
if ( (t = malloc(sizeof(_class_D))) != NULL )
t->vt = VTclass_D;
return t;
}

typedef struct _St_E {
               Func *vt;
} _class_E;

_class_E*new_E(void);
                int _E_m1 (_class_E *this, boolean _ok) { 
return                5;
}
                void _E_m2 (_class_E *this) { 
}
                String _E_m4 (_class_E *this, int _iboolean _ok) { 
return Em4;
}
Func VTclass_E[] = {
( void (*)() ) _E_m1
,( void (*)() ) _E_m2
,( void (*)() ) _E_m4

};

_class_E *new_E()
{
_class_E*t;
if ( (t = malloc(sizeof(_class_E))) != NULL )
t->vt = VTclass_E;
return t;
}

typedef struct _St_Program {
                  Func *vt;
} _class_Program;

_class_Program*new_Program(void);
                   void _Program_run (_class_Program *this) { 
_class_C *_c;
_c = new_C();
                  printf("%d ",( (int (*) (_class_C *, int) ) _c->vt[0]) (_c, 1));
                  ( (void (*) (_class_C *) ) _c->vt[2]) (_c);
                  puts("( (String (*) (_class_C *, char *, int) ) _c->vt[-1]) (_c, ok, 0)");
                  puts("( (String (*) (_class_C *, int, int) ) _c->vt[3]) (_c,                   0, 0)");
                  puts("( (String (*) (_class_C *) ) _c->vt[4]) (_c)");
                  printf("%d ",( (int (*) (_class_C *) ) _c->vt[-1]) (_c) +                   1);
_class_B *_b;
_b = new_B();
                  printf("%d ",( (int (*) (_class_B *, int) ) _b->vt[0]) (_b, 1));
                  ( (void (*) (_class_B *) ) _b->vt[1]) (_b);
                  puts("( (String (*) (_class_B *, char *, int) ) _b->vt[-1]) (_b, ok, 0)");
                  puts("( (String (*) (_class_B *, int, int) ) _b->vt[2]) (_b,                   0, 0)");
                  printf("%d ",( (int (*) (_class_C *) ) _c->vt[-1]) (_c) +                   1);
_class_A *_a;
_a = new_A();
                  printf("%d ",( (int (*) (_class_A *, int) ) _a->vt[0]) (_a, 1));
                  ( (void (*) (_class_A *) ) _a->vt[1]) (_a);
                  puts("( (String (*) (_class_A *, char *, int) ) _a->vt[2]) (_a, ok, 0)");
                  puts("( (String (*) (_class_A *, int, int) ) _a->vt[3]) (_a,                   0, 0)");
_class_D *_d;
_d = new_D();
                  printf("%d ",( (int (*) (_class_D *, int) ) _d->vt[0]) (_d, 1));
                  ( (void (*) (_class_D *) ) _d->vt[1]) (_d);
                  puts("( (String (*) (_class_D *, char *, int) ) _d->vt[-1]) (_d, ok, 0)");
                  puts("( (String (*) (_class_D *, int, int) ) _d->vt[2]) (_d,                   0, 0)");
_class_E *_e;
_e = new_E();
                  printf("%d ",( (int (*) (_class_E *, int) ) _e->vt[0]) (_e, 1));
                  ( (void (*) (_class_E *) ) _e->vt[1]) (_e);
                  puts("( (String (*) (_class_E *, char *, int) ) _e->vt[-1]) (_e, ok, 0)");
                  puts("( (String (*) (_class_E *, int, int) ) _e->vt[2]) (_e,                   0, 0)");
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
