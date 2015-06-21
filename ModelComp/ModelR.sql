BEGIN TRANSACTION;
 drop table if exists rComputer;
 create table rComputer
 (id int primary key not null,
  name varchar(30) not null,
  rank smallint not null,
  descr varchar(250)
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
INSERT INTO rComputer VALUES(1,'const2',1,'f(x) = 2');
INSERT INTO rComputer VALUES(2,'Sum',2,'x+y => r1 <r1-r3>');
INSERT INTO rComputer VALUES(3,'Multiply',2,'x*y');

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


COMMIT;
