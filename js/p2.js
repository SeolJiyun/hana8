// #01. 이 user 객체를 받아서 id와 name을 출력하는 함수를 작성하시오.

const hong = { id: 1, name: 'Hong' };
const lee  = { id: 2, name: 'Lee' };

//구조분해할당 (Destructuring)
function f1({id, name}){
    console.log(id, name);
}

// 일반 점 표기법
function f2(user){
    console.log(user.id, user.name);
}

f1(hong);
f2(hong);

f1(lee);
f2(lee);

// #02. 다음 user 객체에서 passwd 프로퍼티를 제외한 데이터를 userInfo 라는 변수에 할당하시오.
const user = {id: 1, name: 'Hong', passwd: 'xxx', addr: 'Seoul'};
// passwd만 제외하고 나머지를 새 객체에 담기
const { passwd, ...userInfo } = user;
console.log(userInfo);

// #03. 다음 arr에서 3개의 id를 id1, id2, id3로 할당하시오. (destructuring 활용)
const arr = [[{id: 1}], [{id:2}, {id: 3}]]; //cf. const id1 = arr[0][0].id; // Bad

const [[{id:id1}], [{id:id2}, {id: id3}]] = arr;
console.log(id1, id2, id3);

// #04. 다음과 같이 key를 전달하면 해당 값의 첫 글자를 제외한 문자를 리턴하는 함수를 destructing을 최대한 활용하여 (가),(나),(다) 부분을 작성하시오.
const user_B = {name: 'Hong', passwd: 'xyz', addr: 'Seoul'};
function getUserValueExceptInitial(k) {
    const {[k]: val} = user_B;
    const [, ...rest] = val;
    
    return rest.join('');
}
console.log(getUserValueExceptInitial('name')); // 'ong'
console.log(getUserValueExceptInitial('passwd')); // 'yz'
console.log(getUserValueExceptInitial('addr')); // 'eoul'
