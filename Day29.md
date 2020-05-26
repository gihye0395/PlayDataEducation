# Day29

학습목표

1. 분석 함수의 종류를 이해하고 활용할 수 있다.
2. 테이블 CRUD를 구현하고 컬럼에 대한 수정을 할 수 있다.
3. 제약 조건을 이해할 수 있다.



#### 분석함수

- RDB상에서는 칼럼과 칼럼간의 연산, 미교, 연결은 쉬운 반면, 행과 행간의 관계를 정의하거나 비교, 연산 하는 것을 하나의 SQL문으로 처리 하는 것은 매우 어려운 문제이므로 이를 해결하기 위한 함수이다.
- 프로그램을 작성하거나, 인라인 뷰를 이용해 복잡한 SQL문을 작성해야 하던 것을  Analytic Function 을 활용하면 하나의 SQL문으로 쉽게 해결된다.
- SQL사용자 입장에서는 인라인 뷰 이후 SQL의 중요한 기능이 추가되었다고 할 수 있으면 많은 프로그램이나 튜닝 팁을 대체할 수 있을 것이다.
- Analytic Function은 다른 함수와는 달리 중첩(nest)해서 사용하지 못하지만, 서브쿼리에서는 사용할 수 잇다.
- 종류 :  RANKING FAMILY, AGGREGATE FAMILY, LEAD/LAG FAMILY



##### <<구문>>

SELECT ANALYTIC FUNCTION() OVER

​			([PARTITION BY 절]      [ORDER BY 절]     [Windowing 절])

FROM 테이블명;



##### ANALYTIC FUCTION

: AVG, COUNT, LAG, LEAD, MAC, MIN, RANK, RATIO_TO_REPORT, ROW_NUMBER, SUM 등의 함수가 있다.

argument는 0에서 3개까지 올 수 있다.



###### -PARTITION BY 

쿼리 결과를 <expr_list>별로 그룹핑한다. 생략 시에는 하나의 그룹으로 묶는다.



###### -ORDER BY 구문 

ORDER BY <expr_list> [ASC | DESC NULLS FIRST | LAST] 

​		: PARTITION 로 묶인 칼럼 그룹내에 있는 행의 검색 순서를 나타낸다.

ex) RANK() over(PARTITION BY deptno ORDER BY sal DESC) //순위를 매기는데 DEPTNO로 파티션이 나누어지고 정렬을 기준으로 랭킹을 매기겠다. 



###### -WINDOWING구문:

​		ROWS|RANGE [BETWEEN start_point AND end_point | start_point]

- ROWS는 물리적인 단위 (ROW위치)이고, RANGE는 논리적인 단위(ROW값)이다.

- START_POINT : 그룹별 시작점을 의미하며, UNBOUNDED PRECEDING, CURREN ROW, <expr> PRECEDING | FOLLOWING 이 올 수 있다.

- END_POINT : 그룹별 끝점을 의미하며 UNBOUNDED FOLLOWING, CURRENT ROW, <expr> PRECEDING | FOLLOWING 이 올 수 있다.

------

###### 시험에 나왔음!

1)Analytic function <value_expr1>에는 하나 이상의 컬럼 또는 적합한 표현식이 사용될 수 있다. Analytic function의 아규먼트는 0에서 3개까지 사용 가능하고 *는 count(*)에서만 허용되며 distinct는 해당 집계 함수가 허용할 때만 지원된다.

2) OVER analytic_clause 해당 함수가 퀄리 결과 집합에 대해 적용되라는 지시어로써 FROM, WHERE, GROUP BY와 <u>HAVING 이후에 계산되어 진다</u>. SELECT 구 또는 ORDER BY 구에 Analytic function을 사용할 수 있다.

2-1)PARTITION BY 구 <value_expr2>에는 하나의 컬럼 또는 적합한 표현식이 사용될 수 있고 하나 이상의 컬럼 또는 표현식에 의한 그룹으로 쿼리의 결과를 파티션한다. 생략되면 단일 그룹처럼 쿼리 결과 집합이 처리된다.

2-2)ORDER BY 구 <value_expr3>에는 하나 이상의 컬럼 또는 적합한 표현식이 사용될 수 있고 하나 이상의 컬럼 또는 표현식을 기준으로 파티션 내의 데이터를 정렬한다. 표현식은 컬럼의 별칭 또는 위치를 나타내는 숫자를 사용할 수 없다.

2-3)Windowing 구 Windowing구의 예약어

-CURRENT ROW : 윈도우의 시작 위치 또는 마지막 위치가 현재 로우임을 지시하는 예약어

-UNBOUNDED PRECEDING:원도우의 시작 위치가 Partition의 첫번째 로우임을 지시하는 예약어

-UNBOUNDED FOLLOWING : 윈도우의 마지막 위치가 Partition의 마지막 로우임을 지시하는 예약어

------

###### 분석함수의 장점

- Query speed 향상 self-join, 절차적 로직으로 표현한 것을 SQL에서 바로 적용할 수 있도록 하여 join이나 프로그램의 over head를 줄인다.
- 향상된 개발 생산력 간결한 sql로 복잡한 분석작업을 수행 가능하며, 유지보수가 간편하여 생산성 향상시킬 수 있다.
- 이행 및 활용이 용이도록 기존 sql syntax를 그대로 따르기 때문에 ansi sql로 채택 되었다.

------



##### RANKING FAMILY 

- 대상 집합에 대하여 특정 컬럼을 기준으로 순위나 등급을 부여한다. 오름차순, 내림차순 등급을 부여하고 NULL은 순위의 가장 처음 또는 마지막으로 강제처리된다.

- 파티션마다 초기화 되며 순우, 등급은 GROUP BY, CUBE, ROLLUP시 초기화 된다.



RANK() : 로우마다 순위를 매기는 함수로 파티션에서 ORDER BY 절에 명시한 대로 정렬한 후 순위를 의미한다. 1부터 시작하고 동일한 값은 동일한 순위를 매기고 순위만큼 순위는 건너뛴다.



DENSE_RANK() : 동일한 값은 동일 순위는 상관없이 1이 증가된 값을 리턴하다.



Q1.사원번호, 이름, 부서번호, 급여, 부서 내에서 급여가 많은 사원부터 순위를 매기자(RANK 함수 이용)

```sql
SELECT empno, ename, deptno, sal,
		RANK() OVER(PARTITION BY deptno ORDER BY sal DESC) "RANK" FROM EMP;
```



Q2. 사원번호, 이름, 부서번호, 급여, 부서 내에서 급여가 많은 사원부터 순위를 매기자(DENSE_RANK함수이용)

```sql
SELECT empno, ename, deptno, sal,
		DENSE_RANK() OVER(PARTITION BY deptno ORDER BY sal DESC) "RK" FROM EMP;
```



Q3. 부서번호가 30번인 사원번호, 이름, 부서번호, 급여, 부서 내에서 급여가 많은 사원부터 순위를 매기자.(RANK 함수 이용)

```sql
SELECT empno, ename, deptno, sal,
		RANK() OVER(PARTITION BY deptno ORDER BY sal DESC) "RK" 
		FROM EMP WHERE empno = 30;
		
		
SELECT empno, ename, deptno, sal,
		RANK() OVER(ORDER BY sal DESC) "RK" 
		FROM EMP WHERE empno = 30;		
```



Q4. 20번 부서원의 이름, 연봉,누적분산 정보를 조회해 본다.

```SQL
SELECT ename, sal, CUME_DIST() over(ORDER BY sal)
FROM EMP WHERE deptno=20;
```

CUME_DIST()_누적 분산 정보 : PARTITION BY절로 나누어진 그룹별로 각 ROW를 ORDER BY 순서대로 정렬를 한 후 그룹별로 상대적 위치(누적 정보)를 구한다. 상대적인 위치는 구하고자 하는 값보다 작거나 같은 값을 가진 ROW수를 그룹 내 전체 ROW수로 나누 것을 의미하며 결과값은 범위는 0보다 크로 1보다 작거나 같다.



Q5. 사원을 월급 기준으로 4등급으로 분류하자.

```SQL
SELECT ENAME, SAL, NTILE(4) OVER(ORDER BY SAL)
FROM EMP;
```

NTILE() : PARTITON BY 대신 BUCKET 이라는 단어로 ()안에 매개 개수로 나눈다.

​				NTILE(4)를 사용해서 ROW 100개가 있으면 25개씩 ROW를 나눈다. 

​				근사치로 배분한 후 남은 값에 대해서 PARTITION을 한개씩 배분한다.

ex) 103개의 ROW가 있다면 NTILE(5)로 적용할 경우 1~3까지는 21의 ROW로 나머지는 20개의 ROW가 들어간다.



Q6. 사원번호, 사원의 이름, 봉급, 입사일을 추력하되 입사일 순으로 순번을 매겨보자.

​	  단, 급여가 많은 순으로 순번을 주고 만일에 같은 급여를 받는 경우 입사일이 빠른 순번부터 부여하자.

```SQL
SELECT EMPNO, ENAME, SAL, HIREDATE,
ROW_NUMBER() OVER(ORDER BY SAL DESC, HIREDATE ASC) AS "순번" 
FROM EMP;
```



###### WINDOWING 구문을 활용하자.

윈도우 집계 유형(Window Aggregate Family):

윈도우 집계 함수는 윈도우를 근간으로 하여 정렬된 로우들의 집합과 각각의 로우들에 대한 집계 값을 반환한다. 집계함수(SUM, COUNT, MAX, MIN 등 등)의 파티션에 속하는 로우들의 Sliding Window에 대한 계산을 수행하기 위해 윈도우 문법(Window Syntax)을 추가하여 확장한 형태이다.



보고용 집계 유형(Reporting Aggregate Family) : 한 집합 레벨에 대한 집계 값과 다른 집합 레벨에 대한 집계 값의 비교를 통해 분석작업을 하고자 하는 경우 사용한다.

ex) 한 사원의 급여와 해당 부서의 평균 급여를 비교하고자 하는 경우, 그 사원의 급여를 제외한 해당 부서의 평균 급여를 출력하는 경우, 보고용 집계 유형은 셀프 조인을 할 필요없이 다른 집합 레벨에 대한 집계값을 계산하여 반환한다.

Q7.사원번호, 부서번호, 봉급, 전체 봉급의 합계, 부서별 급여합계를 구하자

```SQL
SELECT ENAME, DEPTNO, SAL, SUM(SAL), OVER() "TOTAL SAL",
SUM(SAL) OVER (PARTITION BY DEPTNO) "DEPT_SUM"
FROM EMP;
```

Q8. 사원이름, 직업, 봉급, 직업별 급여평균, 직업별 최대 급여를 조회하자.

```SQL
SELECT ENAME, JOB, SAL, AVG(SAL) OVER (PARTITION BY JOB) "JOB_SAL_AVG", MAX(SAL) OVER (PARTITION BY JOB) "JOB_SAL_MAX"
FROM EMP;
```

Q9. 사원의 월급을 전체 월급을 50000으로 증가 했을 때 기존 월급 비율로 적용했을 경우 각 사원은 얼마를 받게 되는 지 조회해 보자.

```SQL
SELECT ENAME, SAL,
ratio_to_report(sal) over() as "비율",
TRUNC(ratio_to_report(sal) over()*50000) as "인상급여" FROM EMP;
```

ratio_to_report() :해당 구간에서 차지하는 비율을 리턴하는 함수

Q10.

```SQL
SELECT ENAME, DEPTNO, SAL, LAG(SAL,1,10) OVER (ORDER BY SAL) AS NEXT_SAL
FROM EMP;
```

- Lag/Lead 함수는 특정 로우가 속한 파티션 내에서 상대적 상하 위치에 있는 특정 로우의 컬럼 값을 참조하거나 상호 비교하고자 할 때 사용할 수 있는 함수이다.

- 오름차순 또는 내림차순으로 정렬된 파티션 내에서 상대적으로 상위 또는 하위에 위치하고 있는 특정 로우의 컬럼 값을 위치 지정에 의해 참조할 수 있다.

- 파티션 내에서 참조할 로우가 없을 경우 지정한 값(default = null)으로 출력한다.

  ex)오름차순이면 작은 값, 내림차순이면 큰 값 

Q11.사원의 이름, 부서번호, 봉급과 사원의 이전 사원의 봉급을 조회해보자.

``` SQL
SELECT ENAME, DEPTNO, SAL, LAG(SAL,1,0) OVER (ORDER BY SAL) AS NEXT_SAL02,
						   LAG(SAL,1,SAL) OVER (ORDER BY SAL) AS NEXT_SAL03
						   LAG(SAL,1,SAL) OVER (PARTITION BY DEPTNO ORDER BY SAL)                              AS NEXT_SAL04
FROM EMP;					
```

Q12. 사원의 이름, 부서번호, 봉급과 사원의 다음 사원의 봉급을 조회해보자.

```SQL
SELECT ENAME, DEPTNO, SAL, 
	   LEAD(SAL,1,0) OVER (ORDER BY SAL) NEXT_SAL, 
	   LEAD(SAL,1,SAL) OVER (ORDER BY SAL) NEXT_SAL02 FROM EMP; 
```

------

### DML

##### INSERT 

테이블에 행을 추가한다. 

###### 형식

INSERT INTO 테이블명(컬럼,,,) VALUES(값,,,);

###### 조건

1. 테이블명에 컬럼을 명시하지 않을 경우 VALUES 안에 값을 순차로 지정해서 입력한다.
2. 컬럼명과 값은 1:1로 대입되어 입력한다.
3. 값을 지정할 때 문자, 날짜는 ' '로 묶는다. '100'입력시 형변환 주의 
4. 컬럼 제약조건에 NOT NULL이 아닐 경우 NULL입력 할 수 있다.



CREATE TABLE TESTEMP

AS (SELECT * FROM EMP);



Q1.TESTEMP에 데이터를 입력 후 확인해보자.

```SQL
INSERT INTO TESTEMP (EMPNO, ENAME) VALUES(111,'111');
```

Q2. 222의 사원번호인 홍길동의 입사일은 오늘이고 봉급은 1500이며 부서와 커미션은 앚기 배정받지 않았다. 데이터를 추가해보자

```SQL
INSERT INTO TESTEMP(EMPNO, ENAME, HIREDATE,SAL) VALUES (222, '홍길동', SYSDATE, 1500);
```

------

   

##### UPDATE 

객체에 저장된 데이터를 갱신

###### 형식

UPDATE 테이블명

SET 컬럼이름 = 값,,,

WHERE 조건식



Q3. TESTEMP에 있는 사원의 월급을 수정해라

```SQL
UPDATE TESTEMP
SET SAL=0;
```

Q4.'SELESMAN'인 사원의 SAL을 3000으로 수정하자

```SQL
UPDATE TESTEMP
SET SAL=3000
WHERE JOB='SELESMAN';
```

------



##### DELETE

저장된 데이터를 삭제

###### 형식

DELETE FROM 테이블명

WHERE 조건문



Q5. 사원번호가 7902인 사원의 데이터를 삭제하자.

```SQL
DELETE FROM TESTEMP
WHERE EMPNO=7902;
```

Q6. 'SALESMAN'인 사원을 모두 삭제하자.

```SQL
DELETE FROM TESTEMP
WHERE JOB='SALESMAN';
```

------

##### +서브쿼리 +

Q7.SMITH의 월급을 KING이랑 동일하게 수정하자.

```SQL
UPDATE TESTEMP
SET SAL= (SELECT SAL 
		  FROM TESTEMP 
		  WHERE ENAME = 'KING')
WHERE ENAME='SMITH';
```

Q8. 사원번호가 7369인 사원과 같은 직업을 가진 사원들의 부서 번호를 7788번 사원의 부서 번호로 수정하자.

```SQL
UPDATE TESTEMP
SET DEPTNO =(SELECT DEPTNO
			 FROM TESTEMP
			 WHERE EMPNO=7788)
WHERE EMPNO=(SELECT JOB
			 FROM TESTEMP
			 WHERE EMPNO=7369);
```

Q9.'ALLEN'의 직업을 'JONES의 직업으로 수정하자'

```SQL
UPDATE TESTEMP
SET JOB =(SELECT JOB
 		  FROM TESTEMP
 		  WHERE ENAME ='JONES')
WHERE ENAME ='ALLEN';
```

Q10.'SMITH'의 커미션을 'KING'의 커미션으로 변경해라

```SQL
UPDATE TESTEMP
SET COMM=(SELECT COMM
		  FROM TESTEMP
		  WHERE ENAME='KING')
WHERE ENAME='SMITH';
```

Q11.부서번호가 10번인 사원들의 직업을 'SMITH'와 동일하게 변경해라 

```SQL
UPDATE TESTEMP
SET JOB = (SELECT JOB
		   FROM TESTEMP
		   WHERE ENAME='SMITH')
WHERE DEPTNO=10;
ROLLBACK;
```

Q12.'SMITH'와 같은 직업의 사원을 모두 삭제

```SQL
DELETE FROM TESTEMP
WHERE JOB=(SELECT JOB
		 FROM TESTEMP
		 WHERE ENAME='SMITH');
```

------



```
INSERT INTO TESTEMP
SELECT * FROM EMP
WHERE JOB='SALESMAN';

INSERT INTO TESTEMP(EMNO, ENAME, JOB)
SELECT (EMNO, ENAME, JOB) FROM EMP
WHERE JOB='CLERK'; 

```



