

### 학습목표

1. 계층적 질의를 살펴보고 사용 할 수 있다.
2. PL/SQL을 사용할 수 있다.
3. PL/SQL의 구문을 이용해서 기본 문법과 SQL 구문을 접목할 수 있다.
4. 함수와 프로시저를 사용할 수 있다.



#### 계층적 질의

테이블의 행 사이의 계층적 관련성을 바탕으로 데이터를 검색하는 질의

###### 형식

```
SELECT [LEVEL], COLUMN1, COLUMN2,,,
FROM TABLE  ----->조인 사용 불가
WHERE conditions
[START WITH condition] ----->LEVEL의 의사열을 가진다. ROOT의 계층 시작점을 지정
[CONNECT BY PRIOR condition] -----> 계층의 상 하위에 대한 LEVEL를 관리한다.
									PROIOR바로 다음에 오는 행부터 검색되며 서브쿼리를 사용									 할 수 없다.
```

###### 방법

1. START WITH 절을 이용한 시작점 지정

   ex) START WITH COLUMN1=VALUE  :  시작 행의 위치를 지정할 수 있다.

   ​											 만일 명시 하지 않으면 테이블의 모든 ROOT가 NODE 1(LEVEL=1)이 된다.

2. CONNECT BY 절을 사용한 트리 진행 방향 설정

3. LEVEL 의사열의 활용

4. PRIOR키워드의 활용

5. WHERE 절 조건을 활용한 데이터 제거방법

6. CONNECT BY절 조건을 이용한 데이터 제거 

   *계층적 질의는 START WITH와 CONNECT BY에 존재 여부에 따라서 식별된다.



###### 사용시점

- 관계형 데이터베이스는 레코드를 계층적인 방법으로 저장하지 않는다.

  그러나, 꼐층적 관련성이 단일 테이블의 행 사이에 존재하며 계층적 질의는 하나의 테이블에 있는 행들 사이에 어떤 관련성이 존재할 때 가능하다.

  ex)매니저 직책에 있는 사원이 킹에게 보고할 때 사용

- ANSI-SQL에서는 계층관계를 표현한는 Hierarchical Query를 사용하는 것은 불가능하다.

- 확장된 형태의 recursive PL/SQL이나 CONNECT BY를 이용하면 Hierarchical Query(계층 쿼리)표현이 가능하다.

- 오라클에서는 CONNECT BY라는 확장된 구문을 통해 계층쿼리를 지원하다. 조직구조,[답글게시판],[디렉토리구조] 등에 주로 이용된다.

  ex) CONNECT BY PRIOR 탑키 = 하위키 : TOP DOWN 방향

  ​      CONNECT BY PRIOR 하위키 = 탑키 : BOTTOM UP 방향

​       상위를 부모, 하위를 자식으로 연결시켜서 열 위치에 의해 의사결정을 사용한다.

​       항상 현재 부모 행에 관련된 CONNECT BY조건에 의해 자식을 선택한다.



Q1. 계층적 쿼리를 사용하여 위에서 아래로 사원의 이름과 관리자 이름을 조회하자

```sql
SELECT ENAME || '의 상사 ' || PRIOR ENAME "WALK"
FROM EMP
START WITH ENAME = 'KING'
CONNECT BY PRIOR EMPNO=MGR;
```



Q2. 'SMITH'부터 시작하여 아래서 위로 해당 사원의 매니저가 정보가 나오게 출력하자

```sql
SELECT ENAME, JOB, MGR
FROM EMP
START WITH ENAME = 'SMITH'
CONNECT BY PRIOR MGR=EMPNO;
```



​													KING   (LEVEL 1 : ROOT)

​                        

​						CLARK               JONES               BLAKE     (LEVEL 2 : PARENT/CHILD)           

​			

​          MILLER                FORD          ALLEN  WARD  MARTIN  TUNER  JAMES  (LEVEL 3 : PARENT/CHILD/LEAF)

 								  

​								 SMITH



- ROOT : 최상의 레벨

- PARENT / CHILD : 부모이자 자식입장

- LEAF : 가장 마지막 노드

  

Q3. 가장 높은 레벨을 시작으로 가장 낮은 레벨의 각각의 다음 레벨을 들여쓰기로 출력하자

​		ex) king

​                  clark

​                        miller

```sql
SELECT LPAD(' ',4*LEVEL - 4) || ENAME RES,
	   LEVEL, EMPNO, MGR, DEPTNO
FROM EMP
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR;
```



Q4. 전체 노드 중에서 CLARK의 노드를  제거한 후 출력해보자 

```SQL
SELECT ENAME || '의 상사 ' || PRIOR ENAME "WALK"
FROM EMP
WHERE ENAME != 'CLARK'
START WITH ENAME = 'KING'
CONNECT BY PRIOR EMPNO=MGR;

SELECT DEPTNO, EMPNO, ENAME, JOB, SAL
FROM EMP
WHERE ENAME !='CLARK'
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR;
```



Q5. BLAKE와 그의 라인들을 모두 제거하자.

```SQL
SELECT DEPTNO, EMPNO, ENAME, JOB, SAL
FROM EMP
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR AND ENAME!='BLAKE';


SELECT LPAD(' ',4*LEVEL - 4) || ENAME RES,
	   LEVEL, EMPNO, MGR, DEPTNO
FROM EMP
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR AND ENAME!='BLAKE';
```



Q6. LEVEL를 정렬해보자.

```SQL
SELECT LPAD(' ',4*LEVEL - 4) || ENAME RES,
	   LEVEL, EMPNO, MGR, DEPTNO
FROM EMP
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR
ORDER BY ENAME;

SELECT ENAME, JOB, MGR
FROM EMP
START WITH ENAME = 'SMITH'
CONNECT BY PRIOR MGR=EMPNO
ORDER BY ENAME;
```



Q7.  06의 쿼리를 실행하게 되면  TREE 구조를 만들어 정렬을 하기 때문에 계층이 틀어진다라고 본다.

 		TREE 구조를 만들어서 계층을 두고 대상을 정렬하려면 ORDER SIBLINGS BY 라는 키워드를 사용한다.

```SQL
SELECT LPAD(' ',4*LEVEL - 4) || ENAME RES,
	   LEVEL, EMPNO, MGR, DEPTNO
FROM EMP
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR
ORDER SIBLINGS BY ENAME;
```



Q8. SYS_CONNECT_BY_PATH(대상, '구분자')를 이용해서 EMP의 TREE를 출력해보자.

```SQL
SELECT LPAD(' ',4*LEVEL - 4) || ENAME RES,JOB,
	   SYS_CONNECT_BY_PATH(ENAME, '/')
FROM EMP
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR;
```



Q9. CONNECT_BY_ISLEAF :  마지막 노드의 유무를 찾을 수 있다.

CONNECT_BY_ISLEAF = 0 : 현재 ROW가 자식노드를 가지고 있을 때 LEAF NODE가 아니다.
CONNECT_BY_ISLEAF = 1 : 재 ROW가 자식노드를 가지고 있지 않을 때 LEAF NODE이다. 

```SQL 
SELECT LPAD(' ',4*LEVEL - 4) || ENAME RES,JOB,
	  SYS_CONNECT_BY_PATH(ENAME, '/') RES02, CONNECT_BY_ISLEAF
FROM EMP
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR;
```



Q10. 사원테이블의 계층 구조에서 LEAF 노드만 보자. 

```SQL 
SELECT LPAD(' ',4*LEVEL - 4) || ENAME RES,JOB
FROM EMP
WHERE CONNECT_BY_ISLEAF=1
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR;
```



Q11. 사원테이블의 계층 구조에서 LEAF 노드가 아닌 사원을 출력하자.

```SQL 
SELECT LPAD(' ',4*LEVEL - 4) || ENAME RES,JOB
FROM EMP
WHERE CONNECT_BY_ISLEAF=0
START WITH MGR IS NULL
CONNECT BY PRIOR EMPNO = MGR;
```



------

#### MERGE구문

###### 형식

```
MERGE INTO TABLE_NAME ---->변경 또는 추가할 대상 테이블
USING(쿼리)----> 변경 추가할 데이터의 원본을 지정, 테이블, 뷰, 서브 쿼리를 사용
IN(조인조건) --->MERGE 연산이 변경, 추가 되는 조건
WHEN MATCHED THEN ---> 해당 행이 존재 하면  UPDATE 
 UPDATE SET
WHEN NOT MATCHED THEN ---> 존재 하지 않으면 INSERT
 INSERT .. VALUES...
```



```
CREATE TABLE EMP_MAIN
AS
SELECT * FROM EMP;

CREATE TABLE EMP_MASTER
AS
SELECT * FROM EMP WHERE DEPTNO=30;

INSERT INTO EMP_MASTER(EMPNO) VALUES(111);
INSERT INTO EMP_MASTER(EMPNO) VALUES(222);
INSERT INTO EMP_MASTER(EMPNO) VALUES(333);
```



Q12. 사원 EMP_MAIN 테이블을 EMP_MASTER테이블과 비교해서 동일한 사원번호의 데이터가 있으면 EMP_MASTER테이블의 급여, COMM를 EMP테이블의 값으로 수정하고 해당 사원번호를 가진 사원이 없으면 EMP_MASTER에  EMP테이블의 데이터를 이용해서 입력하는 작업을 수향하자.

```
MERGE INTO EMP_MASTER T
USING EMP_MAIN E
ON (T.EMPNO = E.EMPNO)
WHEN MATCHED THEN
 UPDATE SET T.SAL = E.SAL, T.COMM=E.COMM
WHEN NOT MATCHED THEN
 INSERT(EMPNO, ENAME, SAL, COMM) VALUES(E.EMPNO, E.ENAME, E.SAL, E.COMM);
```



EMP_MASTER 테이블의 봉급과 커미션을 0로 수정 후 실행 

```
UPDATE EMP_MASTER
SET SAL=0, COMM=0;

MERGE INTO EMP_MASTER T
USING EMP_MAIN E
ON (T.EMPNO = E.EMPNO)
WHEN MATCHED THEN
 UPDATE SET T.SAL = E.SAL, T.COMM=E.COMM
WHEN NOT MATCHED THEN
 INSERT(EMPNO, ENAME, SAL, COMM) VALUES(E.EMPNO, E.ENAME, E.SAL, E.COMM);
```



------

#### 트랜잭션(Transaction)

논리단위를 형성하는 DML문의 모음을 말한다.

- 하나 또는 두 개 이상의 SQL 문으로 이루어진 단위
- 하나의 트랜잭션 안에 모든 SQL은 동일한 효과를 가진다.
- COMMIT, ROLLBACK 으로 모듈단위를 실행한다.



###### 트랜잭션 이벤트 (COMMIT, ROLLBACK, SQL실행종료, 시스템장애 고장, DDL발생_CREATE) 발생 원리

실행 가능한 첫번째 SQL문이 실행될 때 시작되어 다음 이벤트가 발생하면 종료된다.

*트랜잭션이 종료가 되면 실행가능한 SQL구문이 다음 트랜잭션을 자동으로 시작한다.

데이터 추가 (INSERT) -> 데이터 삭제(DELETE) -> COMMIT;  트랜잭션 종료

트랜잭션 시작  데이터 추가 (INSERT) -> 데이터 삭제(DELETE) -> COMMIT;  ->ROLLBACK; 트랜잭션 종료

트랜잭션 시작  데이터 추가 (INSERT) -> 데이터 삭제(DELETE) -> CREATE 트랜잭션 종료



##### 트랜잭션 명령어 

COMMIT, ROLLBACK, SAVEPOINT 이름



##### 암시적 트랜잭션(자동)

자동 COMMIT이 발생할 경우 --> DDL, DCL이 실행되는 경우, COMMIT, ROLLBACK 명시하지 않고 정상적으로 													   SQLPLUS가 종료될 경우

자동 ROLLBACK  --> 비정상적으로 SQLPLUS 종료할 경우, 시스템 장애가 있을 때 



##### 명시적 트랜잭션 제어

COMMIT : 보유 중인 모든 데이터의 변경 내용을 영구저장하고 현재의 트랜잭션을 종료한다.

SAVEPOINT 이름 : 현재에 트랜잭션 내에 저장 점을 표시한다. 

ROLLBACK :  보류 중인 모든 데이터의 변경 내용을 버리고 현재의 트랜잭션을 종료한다.

ROLLBACK TO SAVEPOINT 이름 : 트랜잭션 저장점으로 ROLLBACK하여 저장점 이후에 생성된 SQL 문장이 있는 내용 및 저장점을 버린다. 



```
CREATE TABLE EMP_RES
AS 
SELECT EMPNO, ENAME
FROM EMP;

DELETE FROM EMP_RES;

INSERT INTO EMP_RES VALUES (111,'111');
SAVEPOINT A -->INSERT문의 실행까지를 표시한다.

UPDATE EMP_RES
SAT ENAME='3333'
WHERE EMPNO;
ROLLBACK TO A; --> UPDATE 문만 취소가 되고 INSERT트랜잭션은 유효하다. 

DELETE FROM EMP_RES;
COMMIT;---->SAVEPOINT A 이후에 실행된 INSERT, DELETE 결과가 테이블에 영구적으로 저장된다. 
```



```
CREATE SEQUENCE MYSC
START WITH 1
IN CLEMENT BY 5
MAXVALUE 20
MINVALUE 1
CYCLE
NOCACHE; 

MYSC.CURRVAL : 현재 시퀀스 값 확인
MYSC.NEXTVAL : 실행
```

------

#### 

1. sys / system : id를 생성한다. 권한을 설정한다.
2. ​                       : 테이블 영역을 저장할 수 있는 .dbf 생성한다.
3. ​                         선언된 id에게 .dbf 지정한다.
4. 기존 서비스 (.dbf) 중단하고 새롭게 매핑된 id 로 접속하는 서비스를 실행한다
5. 생성된 id로 접속한다.



```SQL

conn as sysdba
sys
admin1234


--3
create tablespace ts_test
logging
datafile 'C:\Test\ts_test.dbf'
size 32m
autoextend on
next 32m maxsize 2048m
extent management local;

--4
create user testman
identified by admin1234
default tablespace ts_test
temporary tablespace temp;

--5. 권한부여
grant connect, resource to testman;

```



```SQL
테이블 영역 생성을 잘못했을 경우

1. conn as sysdba
   sys
   admin1234
2. 실행하는 .dbf 서비스를 중단시킨다.
   shutdown immediate;
3. 수작업으로 'c:\Test\ts_test.dbf'를 이동한다.
4. 경로 수정 한다.
   startup mount;
   
   alter database rename file 'C:\Test\ts_test.dbf' TO 'C:\Test02\ts_test.dbf'
5. 재시작 후 접속한다.
   alter database open;
   
dba :
```



##### 권한에 대한 5개의 대표적인 룰

- connect : 접속 권한
- resource: 객체 생성, 변경, 삭제 등 시스템에 대한 기본 권한
- dba : 데이터베이스 관리에 대한 권한
- sysdba : 데이터베이스 시작과 종료 및 관리 권한
- sysoper: sysdba+데이터베이스 생성에 관한 권한

```mariadb
select * from dba_sys_privs; — sys 권한
select * from role_sys_privs; — role 확인 
```

------



#### PL/SQL

###### 형식

```
DECLARE    -- Declarative part (optional)
  -- Declarations of local types, variables, & subprograms

BEGIN      -- Executable part (required)
  -- Statements (which can use items declared in declarative part)

[EXCEPTION -- Exception-handling part (optional)
  -- Exception handlers for exceptions (errors) raised in executable part]
END;
```



Q1. 기본문장을 출력해보자.

```sql
SET SERVEROUTPUT ON 
BEGIN
    DBMS_OUTPUT.ENABLE;
    DBMS_OUTPUT.PUT_LINE('1.Hello oracle');
    DBMS_OUTPUT.DISABLE;
    DBMS_OUTPUT.PUT_LINE('2.Hello java');
END;
/
```



Q2. i라는 변수를 선언하고 20을 대입 후 출력해보자.

```
SET SERVEROUTPUT ON ;

DECLARE
	i integer := 20;
BEGIN 
	DBMS_OUTPUT.PUT_LINE('i의 값은 '||i);
END;
/
```



Q3. 4칙연산을 출력해보자

```
SET SERVEROUTPUT ON ;
DECLARE
	i int :=200;
	j int :=20;
	hap int :=0;
	sub int :=0;
	mul int :=0;
	div int :=0;
BEGIN
	hap := i+j;
	sub := i-j;
	mul := i*j;
	div := i/j;
	DBMS_OUTPUT.PUT_LINE(i||'+'||j||'='||hap);
	DBMS_OUTPUT.PUT_LINE('i-j의 값은 '||sub);
	DBMS_OUTPUT.PUT_LINE('i*j의 값은 '||mul);
	DBMS_OUTPUT.PUT_LINE('i/j의 값은 '||div);
END;
/


```



Q4

```
SET SERVEROUTPUT ON ;
BEGIN
  FOR i IN 1..3 LOOP
    IF i < 4 THEN
      DBMS_OUTPUT.PUT_LINE (TO_CHAR(i));
    ELSE
      DBMS_OUTPUT.PUT_LINE ('=========');
    END IF;
  END LOOP;
END;
/
 
```



Q5. 테이블을 생성하자

```
CREATE TABLE TEST01(
NO NUMBER(3),
IRUM VARCHAR2(20));

BEGIN
  FOR I IN 1..10 LOOP
  	INSERT INTO TEST01 VALUES(I,'홍길동'||I);
  END LOOP;
END;
/
```



Q6.  사원테이블에 10번 부서의 평균 월급을 구하는 구문을 작성하자.

```
SELECT AVG(SAL)
FROM EMP
WHERE DEPTNO=10;


DECLARE
	AVG01 NUMBER(7,3) :=0;
	DEPTNO01 NUMBER(7) :=10;
BEGIN
	SELECT AVG(SAL) INTO AVG01
	FROM EMP
	WHERE DEPTNO=DEPTNO01
	GROUP BY DEPTNO;
	DBMS_OUTPUT.PUT_LINE (DEPTNO01||'부서의 평균은'||'['||AVG01||']');
END;
/

DECLARE
	AVG01 FLOAT(7) :=0;
	DEPTNO01 NUMBER(7) :=10;
BEGIN
	SELECT AVG(SAL) INTO AVG01
	FROM EMP
	WHERE DEPTNO=DEPTNO01
	GROUP BY DEPTNO;
	DBMS_OUTPUT.PUT_LINE (DEPTNO01||'부서의 평균은'||'['||AVG01||']');
END;
/
```



