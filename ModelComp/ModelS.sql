PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
 drop table if exists pSystem;
 drop table if exists pPost;
 create table pPost
 (id int primary key not null,
  name varchar(30) not null,
  sMain varchar(30),
  sAdd varchar(30),
  isNumeric smallint not null,
  Rank smallint,
  descr varchar(250)
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
INSERT INTO pPost VALUES(1,'Const2','#|','',1,1,' f(x) = 2');
INSERT INTO pPost VALUES(2,'Sum','#|','',1,2,' f(x,y) = x+y');
INSERT INTO pPost VALUES(3,'Multiply','#|','',1,2,' f(x,y) = x*y');
INSERT INTO pPost VALUES(4,'AbsMinusx4','#|','abc',1,1,' f(x) = |x-4|');


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

COMMIT;
