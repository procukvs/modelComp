BEGIN TRANSACTION;
 drop table if exists eCalculus;
 create table eCalculus
 (id int primary key not null,
  section varchar(30) not null,
  name varchar(30) not null,
  descr varchar(250),
  unique (section, name)
 ); 
 drop table if exists eLambda;
 create table eLambda
 (idModel integer  not null, 
  id integer not null,
  num int not null,
  name varchar(30) not null,
  txBody varchar(250),
  txComm varchar(250),
  primary key (idModel, id),
  unique (idModel, num)
 );   
DELETE FROM eCalculus;
INSERT INTO eCalculus VALUES(1,'base','test','Тестовий набір');
INSERT INTO eCalculus VALUES(2,'base','Pratt','Набір виразів  із Пратта');
INSERT INTO eCalculus VALUES(3,'base','Fix','Спроба рекурсії');

DELETE FROM eLambda;
INSERT INTO eLambda VALUES(1,1,1,'I','\x.x','');
INSERT INTO eLambda VALUES(1,2,2,'K','\x y. x ',' ');
INSERT INTO eLambda VALUES(1,3,3, 'S','\f g x . f x (g x)','');
INSERT INTO eLambda VALUES(1,4,4,  'skk','S K K','');
INSERT INTO eLambda VALUES(1,5,5,  'zero','\f x . x','');

INSERT INTO eLambda VALUES(2,1,1,'true', '\x y . x','-- Boolean');
INSERT INTO eLambda VALUES(2,2,2,  'false','\x y . y','-- Boolean');
INSERT INTO eLambda VALUES(2,3,3, 'iszero','\n . n (\x . false) true','');
INSERT INTO eLambda VALUES(2,4, 4, 'test','\t l n . t l n','');
INSERT INTO eLambda VALUES(2,5, 5, 'pair','\f s x . x f s',' -- Pair');
INSERT INTO eLambda VALUES(2,6, 6, 'fst','\p . p true',' -- Pair');
INSERT INTO eLambda VALUES(2,7, 7, 'snd','\p . p false',' -- Pair');

INSERT INTO eLambda VALUES(3,1,2,'fix','\f . (\x.f(x x)) (\x.f(x x))','-- Fix point');
INSERT INTO eLambda VALUES(3,2,1,'fixM','(\f .(\x. f(\y. x x y)) (\x. f (\y . x x y)))','-- Fix point');

COMMIT;
