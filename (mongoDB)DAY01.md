mongoDB 사이트 : https://www.mongodb.com/

1. mongover 1.0  + hadoop 1.x + spring 2.5x (싱글노드 4,8,16...)
2.   ver 3.0 /   +. hadoop2.x + spring 4.0x (다중노드)
3.   ver4.0 / + hadoop3.x + spring 4.0x & 5.0 (클라우드)



mongoDB 쿼리

1. 질의 : 하나의 쿼리를 명시하는 키워드 mongoDB 는 6개 정도의 질의를 구현한다.

   - 키-값 질의 : 특정필드와 맵핑되는 값을 포함하는 문서를 말한다.

     주 key에 대한 값을 리턴하는 경우를 말한다.

   - 범위 : 특정 범위에 포함되는 값을 말한다. (비교연산자)

   - 공간 질의 :선, 원, 다각형 등에 대한 공간 근사값 [선박, 항공, 제조업, 포털 사이트]

   - 문자열 탐색 질의 : 논리 연사자를 통해서 특정 문자 열에 결과 값.

   - 집합 질의 : 그룹함수를 지칭하며 count, min, max, average등을 이용한 결과 값

   - mapreduce query : java script로 표현되는 복잡한 데이터를 데이터 베이스에 실행해 반환하는 질의를 말한다.

2. 파일 입출력 및 변환 작업

   css[tsv]  < - >  json  < - > sql

3. 복제 및 샤드 작업 [index]

   

문서에 대한 정보는 외부상태와 내부상태로 확인 할 수 있다.

1)외부적인 상태

1. mongod.lock : 서버의 프로세스 ID를 저장한다.

2. 0파일(ns) : 메타데이터를 네임스페이스 단위로 저장한다.

3. 2번의 크기는 ns 16M를 넘을 수 없다. -28000개의 네임스페이스 하나의 데이터 베이스는 컬렉션과 색인의 개수를 최대 28000개를 생성할 수 있다.

4. Collection.0(64M),collection.1(128M) 등의 파일은 순수 데이터 파일을 저장한다. 

   --nssize arg(=16) 사이즈는 늘일 수 있으나 16M 단위로 처리한다.

   test.1(128M) 등의 파일은 순수 데이터 파일을 저장한다.

5. 몽고는 데이터저장소의 크기를 정적으로 관리한다.

6. wt[wired tiger]: 스토리지 엔진 + db엔진(mysql)로 구현될 수 있게 분리되는 프레임워크로 몽고의 db엔진을 말한다.

7. 스파크에 연됭되어 집계 또는 스트리밍 작업을 원활하게 해준다.



2)내부적인 상태를 확인 --db.stats(1024);

자료를 입력했을 경우 몽고 드라이버의 동작

1. mongodb에 삽입되는 문서의 고유번호 ID인 _id로 필드와 값을 생성한다.

2. 문서를 mongodb의 bsondㅡ로 변환한다.

3. 네트워크 소켓을 통해서 데이터 베이스로 데이터를 전달한다.

   

------

ObjectId("53740ee0c4aae1f14b6297d.")

53740ee0 : 타임스탬프 <4byte> 1970.1.1 표준타임

c4aae1 : 서버ID <3byte> 서버측 식별자

ef14 : 프로세스ID <2byte>

b6297d : 로컬 카운터 <3byte>

<문서에 컬렉션이 추가되거나 문서가 추가될때마다 ID가 생성되며 프로세스가 생성된 ID를 카운팅 해서 관리한다>



------

컬렉션을 생성한 다음 키에 대한 필드 식별자

1. $로 시작할 수 없다.

2. 255 크기 내에 작성한다.

3. 연산자를 포함 할 수 없다.

4. null(공백)이 중간에 들어갈 수 없다.

5. 필드 이름은 하나의 컬렉션 내에서 유일한 값으로 존재한다.

6. 전체 문서의 크기가 16M 제한적이다.(네트워크의 대역폭)

7. 만일 문서가 대용량 (16M이상)GridFS api를 사용해서 구현한다.

   

------

```
>use test
>db.test01.insert({name:111})
>db.test01.insert({addr:"서울"})
>db.test01.find();
{ "_id" : ObjectId("5f3a14e1306a5053b8c36f90"), "name" : 111 }
{ "_id" : ObjectId("5f3a1508306a5053b8c36f91"), "addr" : "서울" }
>var res = db.test01.find();
> res;
{ "_id" : ObjectId("5f3a14e1306a5053b8c36f90"), "name" : 111 }
{ "_id" : ObjectId("5f3a1508306a5053b8c36f91"), "addr" : "서울" }
> db.test01.insert({mykey:new Date()})
> new Date()
> db.test01.find();
{ "_id" : ObjectId("5f3a14e1306a5053b8c36f90"), "name" : 111 }
{ "_id" : ObjectId("5f3a1508306a5053b8c36f91"), "addr" : "서울" }
{ "_id" : ObjectId("5f3a1591306a5053b8c36f92"), "mykey" : ISODate("2020-08-17T05:28:49.349Z") }


```



```
db:myScore
collection : Score
=>use myScore

db.Score.insert({name:"aaa",kor:90,eng:80,mat:98,test:"midterm"});
db.Score.insert({name:"bbb",kor:100,eng:100,mat:76,test:"final"});
db.Score.insert({name:"ccc",kor:90,eng:55,mat:67,test:"midterm"});
db.Score.insert({name:"ddd",kor:70,eng:69,mat:89,test:"midterm"});
db.Score.insert({name:"fff",kor:90,eng:65,mat:98,test:"final"});
db.Score.insert({name:"eee",kor:77,eng:80,mat:30,test:"final"});
db.Score.insert({name:"ggg",kor:75,eng:100,mat:98,test:"final"});

```



Q1) Score 의 전체 내용을 출력한다.

```
db.Score.find();
```

Q2)  Score 의 이름만 출력하되 _id는 출력하지 않는다.

```
db.Score.find({},{name:1,_id:0});
```

Q3) 수학 점수 중 70점 이상만 출력하자.

```
db.Score.find({mat:{$gte:70}});
```

Q4) 이름과 국어를 출력하되 국어가 80점 이상만 출력하자.

```
db.Score.find({kor:{$gte:80}},{name:1,kor:1,_id:0});
```

Q4-ex1)합을 구해보자.

```
var sr = db.Score.find({kor:{$gte:80}},{name:1,kor:1,_id:0});
var tot = 0;
while(sr.hasNext()){
	res = sr.next();
	print(res.name+":"+res.kor);
	tot=tot+res.kor;
	print("tot="+tot);
}
print("tot="+tot);
```

Q4-ex2)

```
var sr = db.Score.find({kor:{$gte:80}},{name:1,kor:1,_id:0});
sr.forEach(function(x) {print(x.name+":"+x.kor);})
```

Q4-ex3)printjson을 이용해서 내용을 출력해보자.

```
>var sr = db.Score.find({kor:{$gte:80}},{name:1,kor:1,_id:0});
>sr.forEach(printjson);
{ "name" : "aaa", "kor" : 90 }
{ "name" : "bbb", "kor" : 100 }
{ "name" : "ccc", "kor" : 90 }
{ "name" : "fff", "kor" : 90 }
```

Q4-ex04)print를 사용해서 출력해보자. [객체 주소를 리턴한다.]

```
>var sr = db.Score.find({kor:{$gte:80}},{name:1,kor:1,_id:0});
>sr.forEach(print);
[object BSON]
[object BSON]
[object BSON]
[object BSON]
```

Q5)Score의 내용을 전체출력하자. 단, 이름과 test가 final인 것만 출력해보자.

```
방법 1)
var sm = db.Score.find();
sm.forEach(
	function(x){
		if(x.test == "final")
			print(x.name + ":" +x.test);
	}
)

방법 2)
db.Score.find({test:{$eq:'final'}},{name:1,test:1,_id:0});
```

Q6)Score의 내용을 전체출력하자. 단, test가 midterm인 것만 출력하고 midterm의 수학만 합을  구하자.

```
var res = db.Score.find()
var tot=0;
	res.forEach(function(x){
		if(x.test=='midterm'){
			tot+=x.mat;
		}
	})
	print("tot="+tot);
	
```

Q7) 이름이 a로 시작되는 전체 내용을 출력하자. 

```
db.Score.find({name:/^a/});
```

Q8) 이름이 a로 시작하거나 e로 시작되는 전체 내용을 출력하자.

```
db.Score.find({"$or":  [{name:/^a/},{name:/^e/}]  });
```

Q9) Q8의 갯수를 출력하자.

```
db.Score.find({"$or":  [{name:/^a/},{name:/^e/}]  }).count();
```

Q10) 중복데이터 제거

```
db.Score.distinct(key,query,<optional params>)
	-e.g. db.Score.distinct('x'),optional parameters are: maxTimeMS
db.Score.distinct("name")
```

Q11) test의 중복데이터 제거 후 출력하되 영어점수가 80점 이상인 학생들을 출력해보자.

```
db.Score.distinct('test',{eng:{$gte:80}});
```

 Q12) 이름의 중복데이터 제거 후 출력하되 영어점수가 80점 이상인 학생들을 출력해보자.

```
db.Score.distint('name',{eng:{$gte:80}});
```

Q13)이름과 test를 출력하되 이름을 내림차순으로 출력하자.

```
db.Score.find({},{name:1,test:1}).sort({name:-1});
```

Q14)최대값 최소값에 대한 것을 sort(),limit()메소드를 이용해서 출력해보자. 

  	영어점수가 가장 높은 레코드 1만 출력해보자.

```
db.Score.find({}).sort({eng:-1}).limit(1);
```

Q15)국어 점수가 가장 낮은 이름과 점수를 출력해보자.

```
db.Score.find({},{name:1,kor:1,_id:0}).sort(kor:1).limit(1);
```

Q16)두 개 건너 뛰고 3개를 출력해보자.

```
db.Score.find({}).skip(2).limit(3);
```

------

db:exam

use exam



Collection : exam01

no    value

1,     100

2,     [10,20]

3,     [[10,20],[30,40]]



exam01) insert

```
db.exam01.insert({no:1,value:100});
db.exam01.insert({no:1,value:[10,20]});
db.exam01.insert({no:1,value:[[10,20],[30,40]]});
```

exam02) 30,40만 출력

```
db.exam01.find({value:[30,40]});
```

