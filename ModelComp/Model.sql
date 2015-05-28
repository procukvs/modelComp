PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
 drop table if exists mAlgorithm;
 create table mAlgorithm
 (id int primary key not null,
  name varchar(30) not null,
  sMain varchar(30),
  sAdd varchar(30),
  isNumeric smallint not null,
  Rank smallint,
  descr varchar(250)
 ); 
 drop table if exists mRule;
 create table mRule
 (idModel integer  not null, 
  id integer not null,
  sLeft varchar(50),
  sRigth varchar(50),
  isEnd smallint not null,
  txComm varchar(250),
  primary key (idModel, id)
 );   
DELETE FROM mAlgorithm;
INSERT INTO mAlgorithm VALUES(1,'EqFunct','#|','',1,1,' f(x) = x');
INSERT INTO mAlgorithm VALUES(2,'NoExist','#|','',1,1,' f(x) = невизначено');
INSERT INTO mAlgorithm VALUES(3,'EqTwo','#|','',1,1,' f(x) = 2');
INSERT INTO mAlgorithm VALUES(4,'Sum','#|','',1,2,' f(x,y) = x+y');
INSERT INTO mAlgorithm VALUES(5,'InsBegin','ab','',0,0,'Додати abb на початку слова');
INSERT INTO mAlgorithm VALUES(6,'InsEnd','ab','c',0,0,'Додати abb в кінець слова');
INSERT INTO mAlgorithm VALUES(7,'Multiply','#|','abc',1,2,' f(x,y) = x*y');

DELETE FROM mRule;
INSERT INTO mRule VALUES(1,1, '','',1,' нічого не робимо ');
INSERT INTO mRule VALUES(2,1, '','',0,'зациклюємося  ');
INSERT INTO mRule VALUES(3,1, '','',0,'викидаємо всі | ');
INSERT INTO mRule VALUES(3,2, '','||',1,'додаємо || ');
INSERT INTO mRule VALUES(4,1, '#','',1,' просто викинули #');
INSERT INTO mRule VALUES(5,1, '','abb',1,'просто вставили abb ');
INSERT INTO mRule VALUES(6,1, 'ca','ac',0,'пробігаємо a ');
INSERT INTO mRule VALUES(6,2, 'cb','bc',0,'пробігаємо b ');
INSERT INTO mRule VALUES(6,3, 'c','abb',1,' накінець вставляємо abb');
INSERT INTO mRule VALUES(6,4, '','c',0,'нам потрібно с ');
INSERT INTO mRule VALUES(7,1, 'a|','|ba',0,'на кожну | додаємо один b ');
INSERT INTO mRule VALUES(7,2, 'a','',0,' a вже не потрібне');
INSERT INTO mRule VALUES(7,3, 'b|','|b',0,'сортуємо | і b');
INSERT INTO mRule VALUES(7,4, '|#','#a',0,'кожна | породить a');
INSERT INTO mRule VALUES(7,5, '#','c',0,'потрібно с для завершення ');
INSERT INTO mRule VALUES(7,6, 'c|','c',0,'| вже не потрібно ');
INSERT INTO mRule VALUES(7,7, 'cb','|c',0,'b перетворюємо в |');
INSERT INTO mRule VALUES(7,8, 'c','',1,'с свою справу зробило ! ');
COMMIT;
