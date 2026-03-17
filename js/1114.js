// 함수
// 하나의 기능 단위로 작성한다.

// 오버로딩: 같은 함수인데 매개변수가 다름
// 오버라이딩:

function counter() {
    let count = 0;
    return function () {
        return ++count;
    }
}

const gate1counter = counter();
console.log("🚀 ~ gate1counter:", gate1counter)
console.log("🚀 ~ gate1counter:", gate1counter)
console.log("🚀 ~ gate1counter:", gate1counter)

// 즉시 호출 함수: 함수 1번만 쓸때
// 왜 쓸까? 함수 안에서 바로 반환해줘야 되는게 필요할 때/ 함수로 쓰면 스택에 올라간다 / 메모리 효율면에서는 그냥 박스안에 쓰는게 낫지만, 그냥 박스에서 썼을 때는 박스 밖으로(전역변수 영역) 원치않는 변수가 튀어나올 수 있다. 
// 아예 함수 안에서만 변수가 관리되도록 하는 효과도 있다.

//부분 await을 안쓰면 async 못쓴다
let data;
(async function af() {
    const data = await fetch('https://jsonplaceholder.typicode.com/todos/1').then(
        function (res){
            return res.json();
        }
    );
    console.log("🚀 ~ af ~ data:", data);
    return data;
})(),then(data => console.log("🚀 ~ af ~ data:", data)); 


const f33 = () => { };
const fx = (x) => x ** 2;
const f44 = () => fx;
const f = f3();