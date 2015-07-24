PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
 drop table if exists mAlgorithm;
 create table mAlgorithm
 (id int primary key not null,
  section varchar(30) not null,
  name varchar(30) not null, 
  sMain varchar(30),
  sAdd varchar(30),
  isNumeric smallint not null,
  Rank smallint,
  descr varchar(250),
  unique (section, name)
 ); 
 drop table if exists mRule;
 create table mRule
 (idModel integer  not null, 
  id integer not null,
  num integer not null,
  sLeft varchar(50),
  sRigth varchar(50),
  isEnd smallint not null,
  txComm varchar(250),
  primary key (idModel, id)
 );   
DELETE FROM mAlgorithm;
INSERT INTO mAlgorithm VALUES(1,'base','EqFunct','#|','',1,1,' f(x) = x');
INSERT INTO mAlgorithm VALUES(2,'base','NoExist','#|','',1,1,' f(x) = невизначено');
INSERT INTO mAlgorithm VALUES(3,'base','EqTwo','#|','',1,1,' f(x) = 2');
INSERT INTO mAlgorithm VALUES(4,'base','Sum','#|','',1,2,' f(x,y) = x+y');
INSERT INTO mAlgorithm VALUES(5,'base','InsBegin','ab','',0,0,'Додати abb на початку слова');
INSERT INTO mAlgorithm VALUES(6,'base','InsEnd','ab','c',0,0,'Додати abb в кінець слова');
INSERT INTO mAlgorithm VALUES(7,'base','Multiply','#|','abc',1,2,' f(x,y) = x*y');

DELETE FROM mRule;
INSERT INTO mRule VALUES(1,1,1, '','',1,' нічого не робимо ');
INSERT INTO mRule VALUES(2,1,1, '','',0,'зациклюємося  ');
INSERT INTO mRule VALUES(3,1,1,'','',0,'викидаємо всі | ');
INSERT INTO mRule VALUES(3,2,2, '','||',1,'додаємо || ');
INSERT INTO mRule VALUES(4,1,1, '#','',1,' просто викинули #');
INSERT INTO mRule VALUES(5,1,1, '','abb',1,'просто вставили abb ');
INSERT INTO mRule VALUES(6,1,1, 'ca','ac',0,'пробігаємо a ');
INSERT INTO mRule VALUES(6,2,2, 'cb','bc',0,'пробігаємо b ');
INSERT INTO mRule VALUES(6,3,3, 'c','abb',1,' накінець вставляємо abb');
INSERT INTO mRule VALUES(6,4,4, '','c',0,'нам потрібно с ');
INSERT INTO mRule VALUES(7,1,1, 'a|','|ba',0,'на кожну | додаємо один b ');
INSERT INTO mRule VALUES(7,2,2, 'a','',0,' a вже не потрібне');
INSERT INTO mRule VALUES(7,3,3, 'b|','|b',0,'сортуємо | і b');
INSERT INTO mRule VALUES(7,4,4, '|#','#a',0,'кожна | породить a');
INSERT INTO mRule VALUES(7,5,5, '#','c',0,'потрібно с для завершення ');
INSERT INTO mRule VALUES(7,6,6, 'c|','c',0,'| вже не потрібно ');
INSERT INTO mRule VALUES(7,7,7, 'cb','|c',0,'b перетворюємо в |');
INSERT INTO mRule VALUES(7,8,8, 'c','',1,'с свою справу зробило ! ');

drop table if exists pState;
create table pState
(name varchar(30) primary key not null, 
 value varchar(30)  not null, 
 descr varchar(250)
);

drop table if exists pParameters;
create table pParameters
(name varchar(30) not null, 
 value varchar(30)  not null, 
 descr varchar(250),
 primary key (name, value)
);
DELETE FROM pState;
INSERT INTO pState VALUES('Section','base','Базовий набір моделей');
INSERT INTO pState VALUES('PostVar','duplicate','Допускаються однакові змінні в лівій частині правила виводу');
INSERT INTO pState VALUES('RecurSubst','operation','Підстановка вказується оператором @S');

DELETE FROM pParameters;
INSERT INTO pParameters VALUES('Section','base','Базовий набір моделей');
INSERT INTO pParameters VALUES('PostVar','unique','Всі змінні в лівій частині правила виводу - різні');
INSERT INTO pParameters VALUES('PostVar','duplicate','Допускаються однакові змінні в лівій частині правила виводу');
INSERT INTO pParameters VALUES('RecurSubst','operation','Підстановка вказується оператором @S');
INSERT INTO pParameters VALUES('RecurSubst','brackets','Підстановка вказується неявно дужками');

 drop table if exists fRecursive;
 create table fRecursive
 (id int primary key not null,
  section varchar(30) not null,
  name varchar(30) not null,
  descr varchar(250),
  unique (section, name)
 ); 
 drop table if exists fFunction;
 create table fFunction
 (idModel integer  not null, 
  id integer not null,
  name varchar(30) not null,
  txBody varchar(250),
  txComm varchar(250),
  primary key (idModel, id)
 );   
DELETE FROM fRecursive;
INSERT INTO fRecursive VALUES(1,'base','a','Простий набір');
INSERT INTO fRecursive VALUES(2,'base','Class07pi','Заняття 07 (прогр.інж.)');
INSERT INTO fRecursive VALUES(3,'base','RecFunSet01','x1 div x2 = SUM(k:1,x1,nsg(k*x2-:x1))');
INSERT INTO fRecursive VALUES(4,'base','Test1','Побудова [x/y] - ціла частина від ділення x  на y');

DELETE FROM fFunction;
INSERT INTO fFunction VALUES(1,1,'sub1','@R(z1,i21)','New Comments bbbb');
INSERT INTO fFunction VALUES(1,2,'subxy','@R(i11,@S(sub1,[i33]))',' ');
INSERT INTO fFunction VALUES(1,3, 'subyx','@S(subxy,[i22,i21])','');
INSERT INTO fFunction VALUES(1,4,  'add','@R(i11,@S(a1,[i33]))','');
INSERT INTO fFunction VALUES(1,5,  'abs','@S(add,[subxy,subyx])','');
INSERT INTO fFunction VALUES(1,6, 'absxyz','@S(abs,[i31,@S(add,[i32,i33])])','');
INSERT INTO fFunction VALUES(1,7,  'minus','@M(absxyz,100)', 'jhkjhkjh');
INSERT INTO fFunction VALUES(1,8,  'test1','@S(sub1,[i33])','');
INSERT INTO fFunction VALUES(1,9,  'CostTwo','@S(a1,[@S(a1,[z1])])','Це константа 2 ===!!');
INSERT INTO fFunction VALUES(1,10,  'Zero','z1','hhhh');
INSERT INTO fFunction VALUES(1,11,  'BigZero','@S(z1,[i93])','-----Testing Arg');


INSERT INTO fFunction VALUES(2,1,'sub1', '@R(z1,i21)','sub1(x) = if x>0 then x-1 else 0');
INSERT INTO fFunction VALUES(2,2,  'subxy','@R(i11,@S(sub1,[i33]))','subxy(x,y) = if x>y then x-y else 0');
INSERT INTO fFunction VALUES(2,3, 'subyx','@S(subxy,[i22,i21])','subyx(x,y) = if y>x then y-x else 0');
INSERT INTO fFunction VALUES(2,4,  'add','@R(i11,@S(a1,[i33]))','add(x,y) = x+y');
INSERT INTO fFunction VALUES(2,5,  'absMinus','@S(add,[subxy,subyx])','absMinus(x,y) = |x-y|');
INSERT INTO fFunction VALUES(2,6,  'const2','@S(a1,[@S(a1,[z1])])','const2(x) = 2  ---- константа 2');
INSERT INTO fFunction VALUES(2,7,  'const4','@S(a1,[@S(a1,[const2])])','const4(x) = 4  -- константа 4');
INSERT INTO fFunction VALUES(2,8,  'mul','@R(z1,@S(add,[i33,i31]))',' mul(x1,x2) = x1*x2 --> mul(x1,0) = 0 / mul(x1,x2+1) = x1*x2+x1');
INSERT INTO fFunction VALUES(2,9,  'multiply', '@R(z1,@S(add,[i33,i31]))','multiply(x1,x2) = x1*x2 --> multiply(x1,0) = 0/multiply(x1,x2+1) = x1*x2+x1');
INSERT INTO fFunction VALUES(2,10,  'absMinusx4','@S(absMinus,[i11,const4])','absMinusx4(x) = |x-4|');

INSERT INTO fFunction VALUES(3,1,'sub1','@R(z1,i21)','');

INSERT INTO fFunction VALUES(4,1,'Add','@R(i11,@S(a1,[i33]))','f(x,0) = x / f(x,y+1) = (x+y)+1 = s(f(x,y))');

drop table if exists tMachine;
 create table tMachine
 (id int primary key not null,
  section varchar(30) not null, 
  name varchar(30) not null,
  sMain varchar(30),
  sAdd varchar(30),
  sInitial char(3),
  sFinal char(3),
  isNumeric smallint not null,
  Rank smallint,
  descr varchar(250),
  unique (section, name)
 ); 
 drop table if exists tProgram;
 create table tProgram
 (idModel integer  not null, 
  id integer not null,
  sState char(3),
  txComm varchar(250),
  primary key (idModel, id)
 );   
 drop table if exists tMove;
 create table tMove
 (idModel integer  not null, 
  id integer not null,
  sIn char(1),
  sOut char(1),
  sNext char(3),
  sGo char(1),
  primary key (idModel, id, sIn)
 );   
DELETE FROM tMachine;
INSERT INTO tMachine VALUES(1,'base','Right','#|','','@z0','@zz',0,1,' *?1..1_ => ?1..1*_');
INSERT INTO tMachine VALUES(2,'base','NoExist','#|','','@a0','@ag',1,1,' *x_ y=> *x_yx <a0..ag>');
INSERT INTO tMachine VALUES(3,'base','Left','|','bc','@z0','@zz',0,1,' _X..X*? => *_X..X?');

DELETE FROM tProgram;
INSERT INTO tProgram VALUES(1,1,'@z0',' r ');
INSERT INTO tProgram VALUES(1,2,'@z1', ' _->!, |->>  ');
INSERT INTO tProgram VALUES(1,3,'@zz', '');

DELETE FROM tMove;
INSERT INTO tMove VALUES(1,1,'_','_','@1 ','>');
INSERT INTO tMove VALUES(1,1,'|','|','@  ','>');
INSERT INTO tMove VALUES(1,2,'_','_','zz ','.');
INSERT INTO tMove VALUES(1,2,'|','|','@z1','=');

INSERT INTO tProgram VALUES(2,1,'@a0',' 0.. ');
INSERT INTO tProgram VALUES(2,2,'@a1', ' _ ');
INSERT INTO tProgram VALUES(2,3,'@a2', 'Right *?1..1_ => ?1..1*_   r');
INSERT INTO tProgram VALUES(2,4,'@a3',' R    _->!, |->>');
INSERT INTO tProgram VALUES(2,5,'@a4', 'Right *?1..1_ => ?1..1*_   r');
INSERT INTO tProgram VALUES(2,6,'@a5', 'R    _->!, |->>');
INSERT INTO tProgram VALUES(2,7,'@a6',' | ');
INSERT INTO tProgram VALUES(2,8,'@a7', '  Left _1..1*? => *_1..1?  l ');
INSERT INTO tProgram VALUES(2,9,'@a8', 'L    _->!, |-><');
INSERT INTO tProgram VALUES(2,10,'@a9',' Left _1..1*? => *_1..1?  l');
INSERT INTO tProgram VALUES(2,11,'@aa', 'L    _->!, |-><');
INSERT INTO tProgram VALUES(2,12,'@ab', '|');
INSERT INTO tProgram VALUES(2,13,'@ac', ' r ');
INSERT INTO tProgram VALUES(2,14,'@ad', 'Left _1..1*? => *_1..1?  l');
INSERT INTO tProgram VALUES(2,15,'@ae',' L    _->!, |-><');
INSERT INTO tProgram VALUES(2,16,'@af', 'r');
INSERT INTO tProgram VALUES(2,17,'@ag', '');

INSERT INTO tMove VALUES(2,1,'_','_','@ad','.');
INSERT INTO tMove VALUES(2,1,'|','|','@a1','.');
INSERT INTO tMove VALUES(2,2,'|','_','@a2','.');
INSERT INTO tMove VALUES(2,3,'_','_','@a3','>');
INSERT INTO tMove VALUES(2,3,'|','|','@a3','>');

INSERT INTO tProgram VALUES(3,1,'@z0',' l ');
INSERT INTO tProgram VALUES(3,2,'@z1', ' _->!, |->< ');
INSERT INTO tProgram VALUES(3,3,'@zz', '');

INSERT INTO tMove VALUES(3,1,'_','_','@z1','<');
INSERT INTO tMove VALUES(3,1,'|','|','@z1','<');
INSERT INTO tMove VALUES(3,1,'a','a','@z1','<');
INSERT INTO tMove VALUES(3,1,'b','y','@z1',' ');
INSERT INTO tMove VALUES(3,1,'c','c','@z1','<');
INSERT INTO tMove VALUES(3,2,'_','_','@zz','.');
INSERT INTO tMove VALUES(3,2,'d','|','@z1','<');
INSERT INTO tMove VALUES(3,2,'a','a','zjj','<');
INSERT INTO tMove VALUES(3,2,'b','b','@z1','<');
INSERT INTO tMove VALUES(3,2,'c','c','@1','<');
INSERT INTO tMove VALUES(3,3,'c','c','@1','<');
INSERT INTO tMove VALUES(2,4,'u','c','@1','<');
INSERT INTO tMove VALUES(2,6,'c','c','@1','<');
INSERT INTO tMove VALUES(2,7,'c','c','@1==','<');
INSERT INTO tMove VALUES(2,10,'c','c','@zzz1','<');
INSERT INTO tMove VALUES(2,9,'c','c','@1','<');

 drop table if exists rComputer;
 create table rComputer
 (id int primary key not null,
  section varchar(30) not null, 
  name varchar(30) not null,
  rank smallint not null,
  descr varchar(250),
  unique (section, name)
 ); 
 drop table if exists rInstruction;
 create table rInstruction
 (idModel integer  not null, 
  id integer not null,
  num integer not null,
  cod char(1) not null,
  reg1 smallint,
  reg2 smallint,
  next integer,
  txComm varchar(250),
  primary key (idModel, id)
 );   
DELETE FROM rComputer;
INSERT INTO rComputer VALUES(1,'base','const2',1,'f(x) = 2');
INSERT INTO rComputer VALUES(2,'base','Sum',2,'x+y => r1 <r1-r3>');
INSERT INTO rComputer VALUES(3,'base','Multiply',2,'x*y');

DELETE FROM rInstruction;
INSERT INTO rInstruction VALUES(1,1,1,'Z',3,0,0,' r1 =0');
INSERT INTO rInstruction VALUES(1,2,2,'S',1,0,0,' r1 =r1+1');
INSERT INTO rInstruction VALUES(1,3,3,'S',1,0,0,' r1 =r1+1');

INSERT INTO rInstruction VALUES(2,1,1,'Z',3,0,0,' 0=>r3');
INSERT INTO rInstruction VALUES(2,2,2,'J',3,2,9,'');
INSERT INTO rInstruction VALUES(2,3,3,'S',1,0,0,'');
INSERT INTO rInstruction VALUES(2,4,4,'S',3,0,0,'');
INSERT INTO rInstruction VALUES(2,5,5,'J',1,1,2,'');

INSERT INTO rInstruction VALUES(3,1,1,'T',2,4,0,' r1 =0');
INSERT INTO rInstruction VALUES(3,2,2,'T',1,2,0,' r1 =0');
INSERT INTO rInstruction VALUES(3,3,3,'Z',1,0,0,' r1 =0');
INSERT INTO rInstruction VALUES(3,4,4,'Z',5,0,0,' r1 =0');
INSERT INTO rInstruction VALUES(3,5,5,'J',4,5,13,' r1 =0');
INSERT INTO rInstruction VALUES(3,6,6,'Z',3,0,0,' r1 =0');
INSERT INTO rInstruction VALUES(3,7,7,'J',3,2,11,' r1 =0');
INSERT INTO rInstruction VALUES(3,8,8,'S',1,0,0,' r1 =0');
INSERT INTO rInstruction VALUES(3,9,9,'S',3,0,0,' r1 =0');
INSERT INTO rInstruction VALUES(3,10,10,'J',1,1,7,' r1 =0');
INSERT INTO rInstruction VALUES(3,11,11,'S',5,0,0,' r1 =0');
INSERT INTO rInstruction VALUES(3,12,12,'J',1,1,5,' r1 =0');

drop table if exists pSystem;
 drop table if exists pPost;
 create table pPost
 (id int primary key not null,
  section varchar(30) not null, 
  name varchar(30) not null,
  sMain varchar(30),
  sAdd varchar(30),
  isNumeric smallint not null,
  Rank smallint,
  descr varchar(250),
  unique (section, name)
 ); 
 drop table if exists pRule;
 drop table if exists pDerive;
 create table pDerive
 (idModel integer  not null, 
  id integer not null,
  num integer not null,
  sLeft varchar(50),
  sRigth varchar(50),
  isAxiom smallint not null,
  txComm varchar(250),
  primary key (idModel, id)
 );   
DELETE FROM pPost;
INSERT INTO pPost VALUES(1,'base','Const2','#|','',1,1,' f(x) = 2');
INSERT INTO pPost VALUES(2,'base','Sum','#|','',1,2,' f(x,y) = x+y');
INSERT INTO pPost VALUES(3,'base','Multiply','#|','',1,2,' f(x,y) = x*y');
INSERT INTO pPost VALUES(4,'base','AbsMinusx4','#|','abc',1,1,' f(x) = |x-4|');

DELETE FROM pDerive;
INSERT INTO pDerive VALUES(1,1,1, '','#||',1,' ');
INSERT INTO pDerive VALUES(1,2,2, '@S#@T','@S|#@T',0,' ');

INSERT INTO pDerive VALUES(2,1,1, '','##',1,'');
INSERT INTO pDerive VALUES(2,2,2, '@R#@S#@T','@R|#@S#@T|',0,'');
INSERT INTO pDerive VALUES(2,3,3, '@R#@S#@T','@R#@S|#@T|',0,'');

INSERT INTO pDerive VALUES(3,1,1, '','##',1,'');
INSERT INTO pDerive VALUES(3,2,2, '#@R#','#@R|#',0,' ');
INSERT INTO pDerive VALUES(3,3,3, '@R#@S#@T','@R|#@S#@T@S',0,'(x+1)*y = x*y +y  ');

INSERT INTO pDerive VALUES(4,1,1, '','||||#||||#b',1,' ');
INSERT INTO pDerive VALUES(4,2,2, '','||||#||||#c',1,' ');
INSERT INTO pDerive VALUES(4,3,3, '@S#@R#b@T','@S|#@R#b@T|',0,'==================== ');
INSERT INTO pDerive VALUES(4,4,4, '@S|#@R#b@T','@S#@R#b@T|',0,' ------------------');
INSERT INTO pDerive VALUES(4,5,5, '@Sb@R','@S@R',0,'-------');
INSERT INTO pDerive VALUES(4,6,6, '@Sc@R','@S@R',0,'');


INSERT INTO pPost VALUES(5,'base','AbsMinusx4Test','#|','abc',1,1,' f(x) = |x-4|');


DELETE FROM pDerive;
INSERT INTO pDerive VALUES(1,1,1, '','#||',1,' ');
INSERT INTO pDerive VALUES(1,2,2, '@S#@T','@S|#@T',0,' ');

INSERT INTO pDerive VALUES(2,1,1, '','##',1,'');
INSERT INTO pDerive VALUES(2,2,2, '@R#@S#@T','@R|#@S#@T|',0,'');
INSERT INTO pDerive VALUES(2,3,3, '@R#@S#@T','@R#@S|#@T|',0,'');

INSERT INTO pDerive VALUES(3,1,1, '','##',1,'');
INSERT INTO pDerive VALUES(3,2,2, '#@R#','#@R|#',0,' ');
INSERT INTO pDerive VALUES(3,3,3, '@R#@S#@T','@R|#@S#@T@S',0,'(x+1)*y = x*y +y  ');

INSERT INTO pDerive VALUES(4,1,1, '','||||#||||#b',1,' ');
INSERT INTO pDerive VALUES(4,2,2, '','||||#||||#c',1,' ');
INSERT INTO pDerive VALUES(4,3,3, '@S#@R#b@T','@S|#@R#b@T|',0,'==================== ');
INSERT INTO pDerive VALUES(4,4,4, '@S|#@R#b@T','@S#@R#b@T|',0,' ------------------');
INSERT INTO pDerive VALUES(4,5,5, '@Sb@R','@S@R',0,'-------');
INSERT INTO pDerive VALUES(4,6,6, '@Sc@R','@S@R',0,'');


INSERT INTO pDerive VALUES(5,1,1, '','||||#||@T||#b',1,' ');
INSERT INTO pDerive VALUES(5,2,2, '','||||#||||#c',1,' ');
INSERT INTO pDerive VALUES(5,3,3, '@S#@R#b@T','@S|#@R#b@T|',0,'==================== ');
INSERT INTO pDerive VALUES(5,4,4, '@S|#@R#b@T','@S#@R#b@T|',0,' ------------------');
INSERT INTO pDerive VALUES(5,5,5, '@Sb@R','@S@U@R',0,'-------');
INSERT INTO pDerive VALUES(5,6,6, '@Sc@S@R','@S@R',0,'');


COMMIT;
