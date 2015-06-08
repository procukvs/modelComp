PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
 drop table if exists tMachine;
 create table tMachine
 (id int primary key not null,
  name varchar(30) not null,
  sMain varchar(30),
  sAdd varchar(30),
  sInitial char(3),
  sFinal char(3),
  isNumeric smallint not null,
  Rank smallint,
  descr varchar(250)
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
INSERT INTO tMachine VALUES(1,'Right','#|','','@z0','@zz',0,1,' *?1..1_ => ?1..1*_');
INSERT INTO tMachine VALUES(2,'NoExist','#|','','@a0','@ag',1,1,' *x_ y=> *x_yx <a0..ag>');
INSERT INTO tMachine VALUES(3,'Left','|','bc','@z0','@zz',0,1,' _X..X*? => *_X..X?');

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
COMMIT;
