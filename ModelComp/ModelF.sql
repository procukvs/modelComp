BEGIN TRANSACTION;
 drop table if exists fRecursive;
 create table fRecursive
 (id int primary key not null,
  name varchar(30) not null,
  descr varchar(250)
 ); 
 drop table if exists fFunction;
 create table fFunction
 (idModel integer  not null, 
  id integer not null,
  name varchar(30) not null,
  Rank smallint,
  isConst smallint not null,
  txBody varchar(250),
  txComm varchar(250),
  primary key (idModel, id)
 );   
DELETE FROM fRecursive;
INSERT INTO fRecursive VALUES(1,'a','Простий набір');
INSERT INTO fRecursive VALUES(2,'Class07pi','Заняття 07 (прогр.інж.)');
INSERT INTO fRecursive VALUES(3,'RecFunSet01','x1 div x2 = SUM(k:1,x1,nsg(k*x2-:x1))');
INSERT INTO fRecursive VALUES(4,'Test1','Побудова [x/y] - ціла частина від ділення x  на y');

DELETE FROM fFunction;
INSERT INTO fFunction VALUES(1,1,'sub1',1,0,'@R(z1,i21)','New Comments bbbb');
INSERT INTO fFunction VALUES(1,2,'subxy',2,0,'@R(i11,@S(sub1,[i33]))',' ');
INSERT INTO fFunction VALUES(1,3, 'subyx',2,0,'@S(subxy,[i22,i21])','');
INSERT INTO fFunction VALUES(1,4,  'add',2,0,'@R(i11,@S(a1,[i33]))','');
INSERT INTO fFunction VALUES(1,5,  'abs',2,0,'@S(add,[subxy,subyx])','');
INSERT INTO fFunction VALUES(1,6, 'absxyz',3,0,'@S(abs,[i31,@S(add,[i32,i33])])','');
INSERT INTO fFunction VALUES(1,7,  'minus',2,0,'@M(absxyz,100)', 'jhkjhkjh');
INSERT INTO fFunction VALUES(1,8,  'test1',3,0,'@S(sub1,[i33])','');
INSERT INTO fFunction VALUES(1,9,  'CostTwo',1,1,'@S(a1,[@S(a1,[z1])])','Це константа 2 ===!!');
INSERT INTO fFunction VALUES(1,10,  'Zero',1,1,'z1','hhhh');
INSERT INTO fFunction VALUES(1,11,  'BigZero',9,0,'@S(z1,[i93])','-----Testing Arg');


INSERT INTO fFunction VALUES(2,1,'sub1',1,0, '@R(z1,i21)','sub1(x) = if x>0 then x-1 else 0');
INSERT INTO fFunction VALUES(2,2,  'subxy',2,0,'@R(i11,@S(sub1,[i33]))','subxy(x,y) = if x>y then x-y else 0');
INSERT INTO fFunction VALUES(2,3, 'subyx',2,0,'@S(subxy,[i22,i21])','subyx(x,y) = if y>x then y-x else 0');
INSERT INTO fFunction VALUES(2,4,  'add',2,0,'@R(i11,@S(a1,[i33]))','add(x,y) = x+y');
INSERT INTO fFunction VALUES(2,5,  'absMinus',2,0,'@S(add,[subxy,subyx])','absMinus(x,y) = |x-y|');
INSERT INTO fFunction VALUES(2,6,  'const2',1,1,'@S(a1,[@S(a1,[z1])])','const2(x) = 2  ---- константа 2');
INSERT INTO fFunction VALUES(2,7,  'const4',1,1,'@S(a1,[@S(a1,[const2])])','const4(x) = 4  -- константа 4');
INSERT INTO fFunction VALUES(2,8,  'mul',2,0,'@R(z1,@S(add,[i33,i31]))',' mul(x1,x2) = x1*x2 --> mul(x1,0) = 0 / mul(x1,x2+1) = x1*x2+x1');
INSERT INTO fFunction VALUES(2,9,  'multiply',2,0, '@R(z1,@S(add,[i33,i31]))','multiply(x1,x2) = x1*x2 --> multiply(x1,0) = 0/multiply(x1,x2+1) = x1*x2+x1');
INSERT INTO fFunction VALUES(2,10,  'absMinusx4',1,0,'@S(absMinus,[i11,const4])','absMinusx4(x) = |x-4|');

INSERT INTO fFunction VALUES(3,1,'sub1',1,0,'@R(z1,i21)','');

INSERT INTO fFunction VALUES(4,1,'Add',2,0,'@R(i11,@S(a1,[i33]))','f(x,0) = x / f(x,y+1) = (x+y)+1 = s(f(x,y))');

COMMIT;
