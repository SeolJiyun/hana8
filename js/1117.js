// // globalThis.name = 'Global Name';

// // const obj = {
// //   name: 'Obj Name',
// //   printName() {
// //     console.log(this.name);
// //   },
// // };

// // const printName = obj.printName;  // <f.o> address
// // // obj = null;
// // printName();

// const dog = {
//   name: 'Maxx',
//   showMyName() {
//     console.log(`My name is ${this.name}.`);
//   },
//   whatsYourName() {
//     //불가: setTimeout(this.showMyName, 1000);
//     // setTimeout(this.showMyName.bind(this), 1000);
//     // setTimeout(function() {
//     //     this.showMyName();
//     // }, 1000);
//     setTimeout(() => this.showMyName(), 1000);
//   },
// };

// dog.whatsYourName();


// const debounce = (cb, delay) => {
//   let timer;
//   return (...args) => {  
//     if (timer) clearTimeout(timer);
//     timer = setTimeout(cb, delay, ...args);
//   };
// }
// const act = debounce(a => a + 1, 1000);
// act(100);
// // 1초 동안 n번 호출 => 실행은 1회만!
// // 1초 후 => cb(100) 실행
// // 1.5초 후
// act(100);   // 마지막 호출부터 delay 후 cb 실행!!

// const throttle = (cb, delay) => {
//     let timer;
//     return (...args) => {
//     if (timer) return;  // 타이머가 돌고 있으면 씹어. 단위시간마다 한번씩 실행되겠지.
//     timer = setTimeout(() => {
//         cb(...args);
//         timer = null;
//     }, delay);
//     };
// }
// const act = throttle(a => a + 1, 1000);
// act(1);

//연습문제 1
const once = (f) => {
  let done = false;
  let result;

  return (...args) => {
    if (done) return result;   // ✅ 두 번째부터는 실행 안 함

    done = true;
    result = f(...args);
    return result;
  };
};


const fn = once((x, y) => `금일 운행금지 차량은 끝번호 ${x}, ${y}입니다!`);
console.log(fn(1, 6)); // 금일 운행금지 차량은 끝번호 1, 6입니다!
console.log(fn(2, 7)); // undefined
console.log(fn(3, 8)); // undefined

let cnt = 0;
const intl = setInterval(() => {
  console.log(fn(cnt, -cnt), 100);
  if (++cnt === 5) clearInterval(intl); // 5번 찍고 멈추기
}, 100);

// 연습문제 2
const before = () => console.log('before....');
const after = (result) => console.log('after...', result);

const someFn = (name, greeting) => `${greeting}, ${name}`;
const someFn2 = (id, nickname, email, level) => `${id}/${nickname}/${email}/${level}`;

const template = (f) => (...args) => { 
    before();
    const ret = f(...args);
    after();
    return ret;
};// 코드를 완성하세요.

const temp = template(someFn);  // before → someFn → after 실행
const temp2 = template(someFn2);  // before → someFn2 → after 실행

console.log('temp1>>', temp('sico', 'hello'));
console.log('temp2>>', temp2(1, 'sico', 'sico@gmail.com', 5));
