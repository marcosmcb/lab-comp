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
    void _A_set (_class_A *this, int _pn) { 
 = _pn;
}
    int _A_get (_class_A *this) { 
return ;
}
Func VTclass_A[] = {
( void (*)() ) _A_set
,( void (*)() ) _A_get

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
       void _B_set (_class_B *this, int _pn) { 
      printf("%d ",_pn);
      _A_set( (_class_A *) this, _pn );
}
Func VTclass_B[] = {
         ( void (*)() ) _A_set
,         ( void (*)() ) _A_get
,( void (*)() ) _B_set

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
