# Day28

- JDBC connection 을 살펴보고 각 흐름을 숙지 할 수 있다.
- Join을 사용하여 Entity의 조건 병합에 대해서 살펴보고 활용할 수 있다.
- 분산 함수를 살펴보고 활용할 수 있다.





### JDBC connection

1. Class.forName()-> 드라이버 연결

2. Connection conn = DriverManager.getConnection() db연결

3. Statement stmt = conn.createStatement(); ->명령준비

4. 쿼리를 실행해서 결과를 리턴 [boolean, int[], ResultSet, int]

   boolean stmt.execute(String sql)->지정된 쿼리를 실행유무를 리턴

   int[] stmt. executeBatch()->추가된 쿼리들을 일괄[insert, delete,update]로 실행한 결과를 리턴

   수행을 하면 1, 수행을 못하면 다른 값이 들어옴 

   ResultSet stmt. executeQuery(String sql)->지정된 쿼리중 select 쿼리를 테이블로 저장해서 리턴

   int stmt.executeUpdate(String sql) : [insert, delete,update] 실행결과

5. SELECT 쿼리를 ResultSet의 getDataType형 메소드로 결과를 컬럼형을 리턴받는다.



#### JDBC +MVC

- Model : 연산, 디비처리 등의 비즈니스 로직(biz+dao)

- View : 화면 설계 단 (회원가입화면, 게시판 화면 )

- Controller : view에서 입력받는 값들을 조건에 따라 모델로 전달한 후 결과를 다시 view로 리턴하는 기능[데이터 검증 후 로직을 제어]



클라이언트가 이름, 수학,국어,영어 데이터(vo)를 view단에서 입력하면 controller가 이를 받아 유효성검사를 한다. model단의 계산 영역으로가서 합계와 평균을 계산한 뒤 엔티티로 dao에 간다. 여기 db에는 이름, 국,영,수,총점, 평균, 학점의 엔티티가 생성되어 있다.  vo는 biz까지 이고 이 이후에는 엔티티이다. 결과창의 데이터는 엔티티이다. vo는껍데기 이거고 엔티티는 실제 sql에 있는 값이다. 



my_emp 테이블을 사용한 프로젝트(MVC)

Model : com.biz.MyEmpBIZ ->계산이 있으면 계산-> 계산 후 Dao호출

​												  ->계산이 없으면 Dao호출

​			  com.dao.MyEmpDAO

View+Controller : MTest.main()

VO : MVC간에 데이터를 전달할 객체

​        com.vo.MyEmpVO

Entity : com.entity.MyEmpEntity



### Join이란

- 데이터베이스에서 여러 테이블의 데이터가 필요한 경우 조인을 사용한다.
- 조인은 관계형 데이터베이스에서 가장 기본적이고 가장 중요한 기능이다.
- 어떤 테이블을 기준으로 다른 테이블에 있는 Row를 찾아오는 것이다.
- 관계형 데이터베이스에서는 서로 독립적인 데이터들간의 조인을 이용하여 필요 시 원하는 다양한 정보를 참조한다.
- 해당 열에 존재하는 공통 값, 일반적으로 기본 키 및 외래 키 열을 조인 조건으로 사용하여 한 테이블의 행을 다른  테이블의 행에 조인할 수 있다.





### 테이블간의 관계에 따른 분류

- INNER JOIN

- CROSS JOIN

- OUTER JOIN

- SELF JOIN

- NONEQUI JOIN



#### INNER JOIN

각 테이블의 조인 컬럼(공통 컬럼)을 비교하여 조인조건을 만족하는 레코드만 선택하는 조인

```
--Ansi JOIN
SELECT * FROM M INNER JOIN S ON M1=S1;

--SQL SERVER JOIN
SELECT * FROM M,S WHERE M1=S1 ;
```



Q1. INNER JOIN을 이용해서 사원의 이름과 그 사원이 속해있는 부서이름을 출력해 보라

```SQL
--SQL SERVER JOIN
SELECT ENAME, DNAME
FROM EMP, DEPT
WHERE EMP.DEPTNO=DEPT.DEPTNO;

--Ansi JOIN
SELECT ENAME, DNAME
FROM EMP JOIN DEPT
USING(DEPTNO);
```

Q2.사원의 이름과 그 사원이 속해있는 부서이름과 부서번호를 출력하라.

```sql
--SQL SERVER JOIN
SELECT ENAME, DNAME, DEPTNO
FROM EMP, DEPT
WHERE EMP.DEPTNO=DEPT.DEPTNO

--Ansi JOIN
SELECT ENAME, DNAME, DEPTNO
FROM EMP JOIN DEPT
USING(DEPTNO);
```



#### CROSS JOIN

각 테이블의 모든 로우 대해서 가능한 모든 조합을 가지는 쿼리 결과를 만들어 내는 조인

```
--Ansi JOIN
SELECT *
FROM M CROSS JOIN S;

----SQL SERVER JOIN
SELECT *
FROM M,S;
```



#### OUTER JOIN

- 두 테이블 간에 주, 종 관계를 두고 주 테이블의 모든 레코드와 종 테이블에서 조인 조건을 만족하는 레코드만 가져올 때 사용
- 주 테이블의 위치에 따라서 LEFT OUTER JOIN, RIGHT OUTER JOIN, 그리고 두 개의 결과를 합한 FULL OUTER JOIN으로 구분된다.

##### LEFT OUTER JOIN

M테이블을 주 테이블로 놓고, S테이블을 종 테이블로 하여 조인을 걸어서 M1=S1 조건을 만족하는 레코드를 가져오는 LEFT OUTER JOIN을 작성

```SQL
--Ansi JOIN
SELECT *
FROM M LEFT OUTER JOIN S
ON M1=S1

----SQL SERVER JOIN
SELECT *
FROM M,S
WHERE M1=S1(+);
```

##### RIGHT OUTER JOIN

S테이블을 주 테이블에 놓고, M 테이블을 종 테이블로 한 RIGTH OUTER JOIN을 작성

```SQL
--Ansi JOIN
SELECT *
FROM M RIGHT OUTER JOIN S
ON M1=S1

----SQL SERVER JOIN
SELECT *
FROM M,S
WHERE M1(+)=S1;
```



#### FULL OUTER JOIN

두 테이블을 LEFT OUTER JOIN, RIGHT OUTER JOIN을 한 결과를 합한 것과 동일한 결과를 가져온다.

```SQL
SELECT *
FROM M FULL OUTER JOIN S
ON M1=S1;
```



LEFT, RIGHT OUTER JOIN을 사용한 쿼리를 UNION으로 합했을 경우

```
SELECT *
FROM M,S
WHERE M1(+)=S1
UNION
SELECT *
FROM M,S
WHERE M1=S1(+);
```



#### 세 개 이상의 테이블에서 조인 걸기

```SQL
--ANSI JOIN
SELECT *
FROM M INNER JOIN S
ON M1=S1
	  INNER JOIN X
	  ON S1=X1

--SQL SERVER JOIN
SELECT *
FROM M,S,X
WHERE M1=S1 AND S1=X1;
```



#### SELECT JOIN

하나의 테이블 내에서 서로 다른 컬럼간에 참조 관계가 있을 때 걸리는 JOIN

```SQL
SELECT 사원.EMPNO, 사원.ENAME, 관리자.EMPNO, 관리자.ENAME
FROM EMP 사원, EMP 관리자
WHERE 사원.MGR=관리자.EMPNO(+);

SELECT EMPNO, ENAME, MGR, ENAME 
FROM EMP LEFT OUTER JOIN EMP 
 ON MGR = EMPNO;

```



#### NONEQUI JOIN

Q1. 각 사원의 이름과 월급, 그리고 그 사원의 급여등급을 출력하라.

```sql
SELECT E.ENAME, E.SAL, S.GRADE
FROM EMP E, SALGRADE S
WHERE E.SAL BETWEEN S.LOSAL AND S.HISAL

SELECT ENAME, SAL, GRADE
FROM EMP JOIN SALGRADE 
ON (SAL BETWEEN LOSAL AND )
```

Q2.각 사원의 이름, 월급, 급여등급, 그가 속한 부서이름을 출력하라.

```SQL
SELECT E.ENAME, E.SAL, D.DNAME, S.GRADE
FROM EMP E, DEPT D, SALGRADE S
WHERE E.DEPTNO=D.DEPTNO
	  AND E.SAL BETWEEN S.LOSAL AND S.HISAL
	  
	  
SELECT ENAME, SAL, DNAME, GRADE
FROM EMP JOIN DEPT USING(DEPTNO)
JOIN SALGRADE ON (SAL BETWEEN LOSAL AND HISAL);
```



##### Example Proplem

------

1. 사원들의 이름, 부서번호, 부서이름을 출력하라.

```sql
--oracle
SELECT E.ENAME, D.DEPTNO, D.DNAME
FROM EMP E, DEPT D
WHERE E.DEPTNO=D.DEPTNO;

--ansi
SELECT ENAME, DEPTNO,DMAPE
FROM EMP inner JOIN DEPT using(deptno)
USING(DEPTNO);
```



2. DALLAS에서 근무하는 사원의 이름, 직위, 부서번호, 부서이름을 출력하라.

```sql
--oracle
SELECT E.NAME, E.JOB, D.DEPTNO,D.DNAME
from emp e, dept d
WHERE e.deptno=d.deptno and D.LOC='DALLAS';

--ansi
SELECT NAME, JOB, DEPTNO, DNAME
from emp inner join dept using(deptno)
WHERE LOC='DALLAS';
```



3. 이름에 'A'가 들어가는 사원들의 이름과 부서이름을 출력하라.

```SQL
--oracle
select e.ename, d.dname
from emp e, dept d
where e.deptno=d.deptno and e.ename LIKE %A%;


--ansi
select ename, dname
from emp inner join dept using(deptno)
where ename LIKE %A%;
```



4. 사원이름과 그 사원이 속한 부서의 부서명, 그리고 월급을 출력하는데 월급이 3000이상인 사원을 출력하라.

```sql
--oracle
select e.ename, d.dname, e.sal
from emp e, dept d
where e.deptno=d.deptno and e.sal>=3000;

--ansi
select ename, dname, sal
from emp inner join dept using(deptno)
where sal>=3000;
```



5. 직위가 'SALEMAN'인 사원들의 직위와 그 사원이름, 그리고 그 사원이 속한 부서 이름을 출력하라.

```sql
--oracle
select e.job, e.ename, d.dname
from emp e, dept d
where e.deptno=d.deptno and e.job='SALESMAN';

--ansi
select job, ename, dname
from emp inner join dept using(deptno)
where job='SALESMAN';

--or
select job, ename, dname
from emp join dept using(deptno)
where job='SALESMAN';
```



6. 커미션이 책정된 사원들의 사원번호, 이름, 연봉, 연봉+커미션, 급여등급을 출력하되, 각각의 컬럼명을 '사원번호', '사원이름', '연봉','실급여','급여등급'으로 하여 출력하라. 

```sql
--oracle
select e.empno as 사원번호,e.ename as 사원이름, e.sal 연봉,(e.sal+e.comm)*12 실급여, s.grade 급여등급
from emp e, salgrade s
where comm is not null and sal between losal and hisal;

--ansi
select empno as 사원번호 , ename as 사원이름, sal, (sal+comm)*12,grade
from emp join salgrade on (sal between losal and hisal)
where comm is not null;
```



7. 부서번호가 10번인 사원들의 부서번호, 부서이름, 사원이름, 월급. 급여등급을 출력하라.

```sql
--oracle
select d.deptno, d.dname, e.ename, e.sal, s.grade
from emp e, dept d, salgrade s
where e.deptno=d.deptno and sal between losal and hisal
and e.deptno =10;

--ansi
select deptno, dname, ename, sal, grade
from emp join dept using(deptno)
join salgrade on (sal between losal and hisal)
where deptno =10;

```



8. 부서번호가 10번, 20번인 사원들의 부서번호, 부서이름, 사원이름, 월급, 급여등급을 출력하라.

그리고 그 출력된 결과물을 부서번호가 낮은 순으로, 월급이 높은 순으로 정렬하라.

```sql
--oracle
select d.deptno,d.dname,e.ename,e.sal, s.grade
from emp e, dept d, salgrade s
where e.deptno=d.deptno 
and sal between losal and hisal 
and d.deptno in (10,20) 
order by d.deptno,e.sal desc;

--ansi
select deptno, dname, ename, sal, grade
from emp join dept using(deptno)
join salgrade on (sal between losal and hisal)
where deptno in (10,20)
order by deptno, sal desc;
```



9. 사원번호와 사원이름, 그리고 그 사원을 관리하는 관리자의 사원번호와 사원이름을 출력하되 각각의 컬럼명을 '사원번호', '사원이름', '관리자번호','관리자이름'으로 하여 출력하라.

```sql
--oracle
select e1.enpno as 사원번호, e1.ename as 사원이름 ,e1.mgr as '관리자번호',e2.ename as '관리자이름'
from emp e1, emp e2
where e1.mgr = e2.empno(+);


--ansi
select e1.enpno as 사원번호, e1.ename as 사원이름 ,e1.mgr as '관리자번호',e2.ename as '관리자이름'
from emp e1, emp e2
on e1.mgr=e2.empno;
```



10. 자신의 관리자보다 먼저 입사한 모든 사원의 이름 및 입사일을 해당 관리자의 이름 및 입사일과 함께 표시하고 열 이름을 각각 --EMPLOYEE,EMPHIREDATE,MANAGER,MGRHIREDATE로 저장한다.

```sql
--oracle
SELECT e1.ename as EMPLOYEE, e1.hiredate as EMPHIREDATE, e2.name as MANAGER, e2.hiredate as MGRHIREDATE
from emp e1, emp e2
where e1.mgr=e2.empno(+)
and e1.hiredate<e2.hiredate;

--ansi
select e1.ename as EMPLOYEE, e1.hiredate as EMPHIREDATE, e2.name as MANAGER, e2.hiredate as MGRHIREDATE
from emp e1 left outer join emp e2
on e1.mgr=e2.empno
where e1.hiredate<e2.hiredate;
```



11. 해당 부서의 모든 사원에 대한 부서 이름, 위치 , 사원 수 및 평균 급여를 --표시하는 정의를 작성한다.

열 이름을 각각 DNAME,LOC,NUMBER OF PEOPLE,SALARY로 한다.

```SQL
--oracle
SELECT D.DNAME AS DNAME, D.LOC AS LOC, COUNT(*) AS "NUMBER OF PEOPLE", AVG(E.SAL) AS SALARY
FROM EMP E, DEPT D
WHERE D.DEPTNO = E.DEPTNO 
GROUP BY D.DNAME, D.LOC;

--ansi
SELECT DNAME, LOC, COUNT(*) AS "NUMBER OF PEOPLE", AVG(SAL) AS SALARY
FROM EMP JOIN DEPT USING(DEPTNO)
GROUP BY DNAME, LOC;
```

