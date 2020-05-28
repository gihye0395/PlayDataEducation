# Day31

### 학습목표

1. 서브 쿼리를 이용하여 테이블을 생성할 수 있다.
2. 테이블 생성 후 제약조건을 추가하고 삭제할 수 있다.
3. 생성된 테이블의 컬럼을 추가하고 수정 및 삭제를 할 수 있다.
4. 데이터 무결성에 대한 구문을 명확하게 설계할 수 있다.



------

#### 서브쿼리를 이용하여 테이블 생성

```
CREATE TABLE TABLE_NAME
AS
SELECT ~ FROM 
WHERE SUBQUERY;
```

- 특징  : 테이블을 생성하고, 서브쿼리 실행 결과가 자동으로 입력됨

- 컬럼 정의

  - 데이터 타입 정의 불가 : 컬럼 이름 및 DEFAULT 값만 정의 가능

  - 컬럼 이름 생략 가능 : 서브 쿼리에서 사용한 컬럼 이름이 적용

  - 제약조건 : 서브쿼리에서 사용한 대상 컬럼들의 NOT NULL조건은 자동 반영됨

  - 생성 시점에 컬럼 레벨에서 제약조건 생성 가능 -> REFERENCES 제약조건 불가

    

Q1) BIG6 계정을 생성한 후 권한을 받아 테이블을 생성하자. EMP테이블과 DEPT테이블만 임포트하여 실행한다.

```
DROP TABLE DEPT;
CREATE TABLE DEPT
       (DEPTNO NUMBER(2) CONSTRAINT PK_DEPT PRIMARY KEY,
	DNAME VARCHAR2(14) ,
	LOC VARCHAR2(13) ) ;
DROP TABLE EMP;
CREATE TABLE EMP
       (EMPNO NUMBER(4) CONSTRAINT PK_EMP PRIMARY KEY,
	ENAME VARCHAR2(10),
	JOB VARCHAR2(9),
	MGR NUMBER(4),
	HIREDATE DATE,
	SAL NUMBER(7,2),
	COMM NUMBER(7,2),
	DEPTNO NUMBER(2) CONSTRAINT FK_DEPTNO REFERENCES DEPT);
INSERT INTO DEPT VALUES
	(10,'ACCOUNTING','NEW YORK');
INSERT INTO DEPT VALUES (20,'RESEARCH','DALLAS');
INSERT INTO DEPT VALUES
	(30,'SALES','CHICAGO');
INSERT INTO DEPT VALUES
	(40,'OPERATIONS','BOSTON');
INSERT INTO EMP VALUES
(7369,'SMITH','CLERK',7902,to_date('17-12-1980','dd-mm-yyyy'),800,NULL,20);
INSERT INTO EMP VALUES
(7499,'ALLEN','SALESMAN',7698,to_date('20-2-1981','dd-mm-yyyy'),1600,300,30);
INSERT INTO EMP VALUES
(7521,'WARD','SALESMAN',7698,to_date('22-2-1981','dd-mm-yyyy'),1250,500,30);
INSERT INTO EMP VALUES
(7566,'JONES','MANAGER',7839,to_date('2-4-1981','dd-mm-yyyy'),2975,NULL,20);
INSERT INTO EMP VALUES
(7654,'MARTIN','SALESMAN',7698,to_date('28-9-1981','dd-mm-yyyy'),1250,1400,30);
INSERT INTO EMP VALUES
(7698,'BLAKE','MANAGER',7839,to_date('1-5-1981','dd-mm-yyyy'),2850,NULL,30);
INSERT INTO EMP VALUES
(7782,'CLARK','MANAGER',7839,to_date('9-6-1981','dd-mm-yyyy'),2450,NULL,10);
INSERT INTO EMP VALUES
(7788,'SCOTT','ANALYST',7566,to_date('13-JUL-87')-85,3000,NULL,20);
INSERT INTO EMP VALUES
(7839,'KING','PRESIDENT',NULL,to_date('17-11-1981','dd-mm-yyyy'),5000,NULL,10);
INSERT INTO EMP VALUES
(7844,'TURNER','SALESMAN',7698,to_date('8-9-1981','dd-mm-yyyy'),1500,0,30);
INSERT INTO EMP VALUES
(7876,'ADAMS','CLERK',7788,to_date('13-JUL-87')-51,1100,NULL,20);
INSERT INTO EMP VALUES
(7900,'JAMES','CLERK',7698,to_date('3-12-1981','dd-mm-yyyy'),950,NULL,30);
INSERT INTO EMP VALUES
(7902,'FORD','ANALYST',7566,to_date('3-12-1981','dd-mm-yyyy'),3000,NULL,20);
INSERT INTO EMP VALUES
(7934,'MILLER','CLERK',7782,to_date('23-1-1982','dd-mm-yyyy'),1300,NULL,10);
```



Q2) 사원의 이름, 부서번호, 부서명, 부서위치를 출력해보자.

```
SELECT ENAME, DEPTNO, DNAME, LOC
FROM EMP JOIN DEPT USING(DEPTNO);
```

<img src="C:\Users\Playdata\AppData\Roaming\Typora\typora-user-images\image-20200528092954073.png" alt="image-20200528092954073" style="zoom:67%;" /> 



Q3) Q2를 이용해서 TEST라는 테이블을 생성하자. 컬럼명을 동일하게 구현하자.

```
CREATE TABLE TEST
AS
SELECT ENAME, DEPTNO, DNAME, LOC
FROM EMP JOIN DEPT USING(DEPTNO);
```

<img src="C:\Users\Playdata\AppData\Roaming\Typora\typora-user-images\image-20200528093025038.png" alt="image-20200528093025038" style="zoom:67%;" /> 



Q4) Q2를 이용해서 TEST02라는 테이블을 생성하자. 컬럼명을 이름, 부서번호, 부서명, 위치로 생성하자.

```
CREATE TABLE TEST02
AS
SELECT ENAME, DEPTNO,LOC
FROM EMP JOIN DEPT USING(DEPTNO);
```



Q5) 'JAMES'의 월급보다 많이 받는 사원의 정보 중 이름, 월급을 추출해서 TEST03의 테이블을 생성하자.

```
CREATE TABLE TEST03
AS
SELECT ENAME, SAL
FROM EMP
WHERE SAL > (SELECT SAL
					FROM EMP
					WHERE ENAME= 'JAMES')
```



Q6) 30번 부서의 직원의 평균 월급보다 더 많이 받는 사원의 이름, 월급, 부서번호를 추출해서 TEST04테이블을 생성하자.

```
CREATE TABLE TEST04
AS 
SELECT ENAME,SAL,DEPTNO 
FROM EMP
WHERE SAL> (SELECT AVG(SAL)
			FROM EMP
			WHERE DEPTNO=30);
```

<img src="C:\Users\Playdata\AppData\Roaming\Typora\typora-user-images\image-20200528093842695.png" alt="image-20200528093842695" style="zoom: 80%;" /> 



------

#### 테이블 생성 후 제약조건을 추가하고 삭제



#### 데이터 딕셔너리

- 사용자 테이블

  - 읿란 사용자가 생성하고 유지/ 관리하는 테이블
  - 사용자 데이터를 포함

- 데이터 딕셔너리 

  - 오라클 DBMS가 내부적으로 생성하고 유지/관리하는 테이블

  - 데이터베이스 정보(사용자 이름, 사용자 권한, 객체 정보, 제약 조건 등)를  포함

  - USER_XXXXX 형식의 데이터 딕셔너리에 접근 가능

    

##### 테이블 이름 조회

```SQL
--테이블 정보관리
SELECT TABLE_NAME
FROM USER_TABLES;

--테이블, 뷰, 시퀀스 정보관리
SELECT TABLE_NAME
FROM USER_CATALOG;

--테이블, 뷰, 시퀀스, 인덱스 등 사용자 객체의 모든 정보 관리
SELECT OBJECT_NAME
FROM USER_OBJECTS
WHERE OBJECT_TYPE='TABLE';
```



##### 테이블 및 객체 정보 조회

```SQL
--사용자 객체 별 개수 현황 확인
SELECT OBJECT_TYPE AS 유형, COUNT(*) AS 개수
FROM USER_OBJECTS
GROUP BY OBJECT_TYPE;

--테이블 별 생성일, 수정일 현황 확인
SELECT OBJECT_NAME AS 이름,
	   OBJECT_TYPE AS 유형,
	   CREATED AS 생성일,
	   LAST_DDL_TIME AS 최종수정일	   
FROM USER_OBJECTS
WHERE OBJECT_TYPE='TABLE';
```





##### 제약조건

```sql
SELECT CONSTRAINT_TYPE, SEARCH_CONDITION, R_CONSTRAINT_NAME, DELETE_RULE
FROM USER_CONSTRAINTS;



SELECT CONSTRAINT_NAME AS 이름, 
	   CONSTRAINT_TYPE AS 유형,
	   COLUMN_NAME AS 컬럼,
	   R_CONSTRAINT_NAME AS 참조,
	   DELETE_RULE AS 삭제규칙,
	   SEARCH_CONDITION AS 내용
FROM USER_CONSTRAINTS
JOIN USER_CONS_COLUMNS USING(CONSTRAINT_NAME, TABLE_NAME)
WHERE TABLE_NAME='CONSTRAINT_EMP';

**USER_CONS_COLUMS -> COLUMN_NAME이 여기에만 있음.
```

```
CREATE TABLE TEST05 AS SELECT EMPNO, ENAME, SAL FROM EMP;
```



##### 테이블 수정 -범위

- 컬럼 관련
  - 컬럼 추가/삭제
  - 데이터 타입 변경, DEFAULT 변경
  - 컬럼 이름 변경
- 제약조건 관련
  - 제약조건 추가/삭제
  - 제약조건 이름 변경
- 테이블 관련
  - 테이블 이름 변경 

```sql
ALTER TABLE table_name
{ 
     ADD [ COLUMN ] ( column_name data_type ) |
     ALTER [ COLUMN ] ( column_name {
                SET DEFAULT | DROP DEFAULT  | NULL } |
     MODIFY COLUMN  ( column_name data_type ) |
     DROP [ COLUMN ] |
     ADD {CONSTRAINT | PRIMARY KEY | UNIQUE} 
     DROP {CONSTRAINT | PRIMARY KEY | UNIQUE} | 
     RENAME COLUMN column_name TO new_column_name |
     MAXROWS | 
     ALL INDEX [ENABLE | DISABLE]
     COMPACT
};

- table_name : 변경할 테이블 이름을 명시한다.
- add column : 테이블에 새로운 칼럼을 추가한다.
- alter column : 기존 칼럼의 기본 값을 변경한다.
- modify column : 기존 칼럼의 자료형(data type)을 변경한다.
- drop column : 하나의 칼럼 혹은 여러 개의 칼럼을 삭제한다.

```

```
ALTER TABLE old_table_name RENAME TO new_table_name;
RENAME old_table_name old new_table_name;

ALTER TABLE table_name RENAME COLUMN old_table_name TO new_table_name;
```



Q7) TEST 테이블을 MYTEST로 변경 해보자.

```
RENAME TEST TO MYTEST;
```



Q8) MYTEST 테이블의 ENAME컬럼을 EMP_NAME으로 변경해보자.

​       MYTEST 테이블의 LOC컬럼을 EMP_LOC로 변경해보자.

```
ALTER TABLE MYTEST 
			RENAME COLUMN ENAME TO EMP_NAME;

ALTER TABLE MYTEST 
			RENAME COLUMN LOC TO EMP_LOC;
```



Q9) MYTEST테이블에 EMPNO NUMBER 추가해보고 싶다.

```
ALTER TABLE MYTEST
     ADD EMPNO NUMBER;
```



Q10) MYTEST테이블에 NO NUMBER로 추가하고 기본값을 10으로 주고 싶다.

```
ALTER TABLE MYTEST
	ADD NO NUMBER DEFAULT 10;
```



Q11)  테이블 생성 후 ADD 키워드로 컬럼을 여러개 추가하고 싶다.

```sql
CREATE TABLE TEST06
AS
SELECT EMPNO, ENAME, DEPTNO
FROM EMP
WHERE DEPTNO=30;

ALTER TABLE TEST06
	ADD A NUMBER
	ADD B NUMBER
	ADD C NUMBER;
```



Q12) TEST06에 EMPNO에 PK를 추가하고 싶다.

```
ALTER TABLE TEST06
	ADD CONSTRAINT MY_P PRIMARY KEY(EMPNO);
```



Q13) TEST07 테이블을 생성한 후 NO, NAME을 추가하자.

```sql
NO NUMBER ------PK--> 식별 데이터가 존재하고 있어야 한다.
NAME VARCHAR2(10) ------DEFAULT ABC

CREATE TABLE TEST07
AS
SELECT EMPNO, ENAME, DEPTNO
FROM EMP;

ALTER TABLE TEST07
ADD NO NUMBER
ADD NAME VARCHAR2(10) default 'abc'
ADD CONSTRAINT TEST07_PK PRIMARY KEY(ename);
```

not null은 생성할 때만 가능하다. 컬럼 라벨.

테이블 라벨에서는 안됨.



Q14) TEST04에 ENAME컬럼에 NOT NULL제약 조건을 추가해보자.

```
ALTER TABLE TEST04 
MODIFY ENAME NOT NULL;

INSERT TABLE TEST06 INTO VALUES(11,'A',22,500,300)
```



Q15) TEST06 A,B,C 컬럼 중 A[100], B[200]만 기본 값을 추가하자.

```
ALTER TABLE TEST06
MODIFT A DEFAULT 100
MODIFT B DEFAULT 200;
```





#### MODIFY

```
ALTER TABLE TABLE_NAME
MODIFY(column_name datatype [default] )
```

조건 

- 컬럼 데이터 타입 확인 : 대상 컬럼의 데이터가 비어있는 경우만 해당 (NULL포함)

  단, CHAR < - > VARCHAR2변환 가능 

- 컬럼 크기 확인 : NUMBER(10,2); NUMBER(8) 등으로 숫자를 구현할 때 타입에 따라 크기가 조절되어야 한다. 

- DEFAULT 관련 확인 : 변경 이후 적용된다. ->INSERT가 되지 않은 데이터일 경우 기본값으로 추가하겠다.



Q 15 ) TEST 06 A,B에는 100,200이 추가된 상태이다. 

```
ALTER TABLE TEST06
MODIFY A DEFAULT 300;

INSERT INTO TEST06(EMPNO) VALUES (22);
ALTER TABLE TEST06 
MODIFY D NUMBER DEFAULT 200;
```



Q16) EMPNO NUMBER형을 VARCHAR형으로 바꿔라

```
ALTER TABLE TEST06
MODIFY EMPNO VARCHAR2(10);
```

<img src="C:\Users\Playdata\AppData\Roaming\Typora\typora-user-images\image-20200528115918422.png" alt="image-20200528115918422" style="zoom:80%;" /> 



```
ALTER TABLE TEST06
MODIFY ENAME CHAR(10);  //ENAME을 CHAR형으로 변환

ALTER TABLE TEST06 
MODIFY ENAME VARCHAR(20);//ENAME을 VARCHAR로 다시 변환 
```

```
CREATE TABLE MTEST(NO NUMBER);
ALTER TABLE MTEST MODIFY NO VARCHAR2(20);
INSERT INTO MTEST VALUES(NULL);
```





### DROP

###### 규칙

1. 컬럼 하나씩 삭제

2. 컬럼 여러개를 한번에 삭제가 가능

3. 참조되는 부모테이블의 제약 조건을 확인 후 삭제

   - 기존에 데이터가 있을 경우에는 모든 데이터를 포함해서 삭제된다.

   - 기존에 작업 후에는 테이블에  반드시 컬럼이 하나 이상 존재해야 한다.

   - 삭제된 컬럼은 어떤 경우라도 복구가 불가능하다.

     

```
EX)

ALTER TABLE ABC DROP COLUMN ENAME;
ALTER TABLE ABC DROP (ENAME);
ALTER TABLE ABC DROP (ENAME, ENAME, DEPTNO);

```



Q17) B, C, D 컬럼을 동시에 삭제하자.

```
ALTER TABLE TEST06 DROP (B,C,D);
```



Q18) A컬럼 하나를 삭제하자.

```
ALTER TABLE TEST06 DROP COLUMN A;
```



Q19) 테이블을 생성한 후 컬럼을 삭제 해보자

```
CREATE TABLE TEST08(
PK NUMBER PRIMARY KEY, 
FK NUMBER REFERENCES DEPT,
COL NUMBER,
CHECK(PK>0 AND COL>0));

ALTER TABLE TEST08 
DROP(PK);
```

<img src="C:\Users\Playdata\AppData\Roaming\Typora\typora-user-images\image-20200528123949835.png" alt="image-20200528123949835" style="zoom:80%;" /> 



```
CREATE TABLE TEST09(
PK NUMBER PRIMARY KEY, 
FK NUMBER REFERENCES TEST09,
COL NUMBER,
CHECK(PK>0 AND COL>0));

ALTER TABLE TEST09 
DROP(PK);

ALTER TABLE TEST09 
DROP(COL);
```

<img src="C:\Users\Playdata\AppData\Roaming\Typora\typora-user-images\image-20200528124130028.png" alt="image-20200528124130028" style="zoom:80%;" /> 

부모 키 열을 삭제할 수 없다.

<img src="C:\Users\Playdata\AppData\Roaming\Typora\typora-user-images\image-20200528124252658.png" alt="image-20200528124252658" style="zoom:80%;" /> 

열이 다중-열 제약조건에 참조되어 삭제 할 수 없다.



Q20) 제약조건이 있는 컬럼을 삭제하자.

```
ALTER TABLE TEST09 DROP(PK) CASCADE CONSTRAINTS;
-->삭제 되는 컬럼을 참조하고 있는 다른 컬럼에 설정된 제약 조건까지 삭제 하겠다. 

ALTER TABLE TEST09 DROP(COL) CASCADE CONSTRAINTS;

ALTER TABLE DEPT DROP(DEPTNO) CASCADE CONSTRAINTS;
```



Q21) TEST08이 DEPT테이블을 FK가 참조하고 있기 때문에 부모테이블인 DEPT의 DEPTNO를 삭제하게 되면 오류가 발생된다.

```sql
ALTER TABLE DEPT DROP(DEPTNO);  ---> ORA-12992
ALTER TABLE DEPT DROP(DEPTNO) CASCADE CONSTRAINTS; --> 참조하고 제약 조건까지 삭제

SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE FROM USER_CONSTRAINTS WHERE TABLE_NAME ='DEPT';
```





Q23) BIG5의 계정으로 접속한 후 테이블의 목록을 확인한다.

```
CONN big5/admin1234

SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE
FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'MY';

SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE
FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'MY02';

SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE
FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'MYDEPT';
```



Q24) MY가 가진 제약 조건을 삭제하자.

```


ALTER TABLE MY
DROP CONSTRAINTS SYS_C007024;

```



Q25) MY02가 가진 제약 조건을 삭제하자

```
ALTER TABLE MY02
DROP CONSTRAINTS SYS_C007025;
```



Q26) MYDEPT 테이블의 PK를 삭제한다.

```
ALTER TABLE  MYDEPT
DROP CONSTRAINTS SYS_C007022;
```



Q26)MYDEPT 테이블의 U를 삭제한다.

```
ALTER TABLE  MYDEPT
DROP CONSTRAINTS SYS_C007023;
```



Q27) MY 테이블에 PK를 추가한다. MY_PK로 추가

```
DELETE FROM MY02 WHERE LID IS NULL;
INSERT INTO MY VALUES (111);
ALTER TABLE MY ADD CONSTRAINT MY_PK PRIMARY KEY(LID);

DELETE FROM MY02 WHERE LID=1;
```



Q28) MY02 테이블에 PK를 추가한다. MY02_PK로 추가

```
ALTER TABLE MY02 ADD CONSTRAINT MY02_PK PRIMARY KEY(LID);
```

Q29) MY02에 NO 컬럼을 추가하고 MYDEPT테이블로 참조한다. MY02_REF라고 이름을 부여한다. MYDEPT_PK라는 제약도 있어야 한다. 

```
ALTER TABLE MYDEPT
ADD CONSTRAINT MY02_REF PRIMARY KEY (ID);

ALTER TABLE MY02
ADD NO NUMBER 
ADD CONSTRAINT MY02_REF FOREIGN KEY(NO) REFERENCES(ID) ;
```



Q30) USER_TAB_COLS의 명세서를 살펴보자

```
CREATE TABLE MY_EXAM(
COL1 NUMBER PRIMARY KEY,
ENAME VARCHAR2(10),
FOREIGN KEY(COL1) REFERENCES EMP);

SELECT COLUMN_NAME 
FROM USER_TAB_COLS WHERE TABLE_NAME='MY_EXAM';



SELECT CONSTRAINT_NAME AS 이름,
	   CONSTRAINT_TYPE AS 유형,
	   COLUMN_NAME AS 컬럼
	   R_CONSTRAINT_NAME AS 참조,
	   DELETE_RUE AS 삭제규칙
	   FROM_
	   USER_CONSTRAINTS JOIN
	   USER_CONS COLUMN USING (CONSTRAINT_NAME,TABLE_NAME)
	   WHERE TABLE_NAME='MY_EXAM';
```



Q31) 컬럼 이름 변경을 해보자

MY_EXAM 테이블의 COL1을 EMPNO로 컬럼명을 변경해보자.

```
ALTER TABLE MY_EXAM
RENAME COLUMN COL1 TO EMPNO;
```



Q32)제약조건이름을 변경해보자

MY_EXAM 의 제약조건을 확인하고 제약조건이름을 PK_EXAM 이라고 변경해보자

```
SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE
FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'MY_EXAM';

ALTER TABLE MY_EXAM
RENAME CONSTRAINT SYS_C007093 TO PK_EXAM; 

ALTER TABLE MY_EXAM
RENAME CONSTRAINT SYS_C007094 TO FO_EXAM;
```



Q33) MY 테이블 삭제 DROP : 테이블에 포함 데이터 및 모든 정보가 삭제된다.

```
DROP TABLE MY;
```



Q34) 삭제 대상 테이블의 PK, UK의 제약조건을 참조하는 테이블의 정보도 삭제 되어야 한다.

```
DROP TABLE MYDEPT CASCADE CONSTRAINT;
```



------



#### VIEW

###### 개념

단일 쿼리 및 서브쿼리를 원본 테이블에 있는 데이터를 호출해서 하나의 가상테이블로 만드는 것을 말한다. [선택적인 정보만 제공한다.]

###### 특징 

- 데이터 접근을 제한한다.
- 자주사용하는 SQL문을 효율적으로 사용하기 위함이다. 
- 복잡한 쿼리문으로 VIEW객체를 생성해 놓으면 사용자는 VIEW접근했을 때 단순 SELECT 만 요청하면 된다.
- 실제 테이블이름과 VIEW의 이름은 달라도 된다.

###### 형식

```
CREATE [OR REPLACE] [FOCE | NOFORCE] VIEW viewname [(alias [, alias ...])]
AS Subquery
[WITH CHECK OPTION [ CONSTRAINT constraint_name ]]
[WITH READ ONLY [ CONSTRAINT constraint_name ]];
```

- CREATE OR RDPLACE

  지정한 이름의 뷰가 없으면 새로 생성, 동일 이름이 존재하면 수정

- FORCE | NOFORCE

  - NOFORCE : 베이스 테이블이 존재하는 경우에만 뷰 생성 가능
  - FORCE : 베이스 테이블이 존재하지 않아도 뷰 생성 가능

- ALIAS

  - 뷰에서 사용할 표현식 이름(테이블 칼럼 이름 의미)
  - 생략 : SUBQUERY에서 사용한 이름 적용
  - ALIAS 개수 :  SUBQUERY에서 사용한 SELECT LIST 개수와 일치

- SUBQUERY

  뷰에서 표현하는 데이터를 생성하는 SELECT 구문

- 제약조건

  - WITH CHECK OPTION : 뷰를 통해 접근 가능한 데이터에 대해서만 DML작업 허용
  - WITH READ ONLY : 뷰를 통해 DML 작업 허용 안함
  - 제약 조건으로 간주되므로 별도 이름 지정 가능

```SQL
CREATE VIEW TEST_VIEW
AS
SELECT ENAME, DEPTNO, DNAME, LOC
FROM EMP JOIN DEPT USING(DEPTNO);

--확인
SELECT COLUMN_NAME, DATA_TYPE, NULLABLE
FROM USER_TAB_COLS
WHERE TABLE_NAME = 'TEST_VIEW';

CREATE OR REPLACE VIEW TEST_VIEW
AS
SELECT ENAME, DEPTNO, DNAME, LOC
FROM EMP JOIN DEPT USING(DEPTNO);

CREATE VIEW TEST02_VIEW(이름, 부서번호,부서명,위치)
AS
SELECT ENAME, DEPTNO, DNAME, LOC
FROM EMP JOIN DEPT USING(DEPTNO);

CREATE OR REPLACE VIEW TEST02_VIEW(이름, 부서번호,부서명,위치)
AS
SELECT ENAME, DEPTNO,DNAME, LOC
FROM EMP JOIN DEPT USING(DEPTNO);
```

특징) VIEW통해서 DML작업이 가능하다.(조건이 맞는 컬럼을 추출 했을 때)

 		COMMIT/ROLLBACK 작업이 필요하다.

​		-WITH READ ONLY : DML 불가

​		-WITH CHECK OPTION : 뷰를 통해 DML 사용 가능. 단, INSERT/UPDATE 작업제한(DELETE 제한이 없다.)

```
CREATE OR REPLACE VIEW TEST03_VIEW
AS
SELECT ENAME, SAL
FROM EMP
WHERE SAL > (SELECT SAL
					FROM EMP
					WHERE ENAME= 'JAMES');

INSERT INTO TEST03_VIEW VALUES('111',100);

```



```
CREATE OR REPLACE VIEW TEST04_VIEW
AS
SELECT * FROM EMP
WITH READ ONLY;
```

<img src="C:\Users\Playdata\AppData\Roaming\Typora\typora-user-images\image-20200528163341418.png" alt="image-20200528163341418" style="zoom:80%;" /> 



Q35) VIEW 생성 시 WITH CHECK OPTION을 써보자.

```
CREATE OR REPLACE VIEW TEST05_VIEW
AS 
SELECT EMPNO, ENAME, SAL 
FROM EMP
WHERE SAL > 2000
WITH CHECK OPTION;

INSERT INTO TEST05_VIEW VALUES(111,'CICI',3000);

UPDATE TEST05_VIEW 
SET SAL = 5000
WHERE EMPNO = 111;
```

