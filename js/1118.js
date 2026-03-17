// const assert = require('assert');
// const arr2 = [1, 2, 3, 4, 5];
// // ex1) [2,3]을 추출
// const ex1 = arr2.slice(1,2);
// console.log(ex1)
// assert.deepStrictEqual(ex1, [2, 3]);

// // ex2) [3]부터 모두 다 추출
// const ex2 = arr2.slice(2);
// console.log(ex2)
// assert.deepStrictEqual(ex2, [3, 4, 5]);

// // ex3) [2,3,4] 제거하기
// const ex3 = arr2.splice(1, 3);
// console.log(ex3)
// assert.deepStrictEqual(ex3, [2, 3, 4]);
// assert.deepStrictEqual(arr2, [1, 5]);

// // ex4) 복원하기
// const ex4 = arr2.splice(1, ...ex3);
// console.log(ex4)
// assert.deepStrictEqual(ex4, []);
// assert.deepStrictEqual(arr2, [1, 2, 3, 4, 5]);

// // ex5) [3] 부터 끝까지 제거하기
// const ex5 = arr2.splice(2); // (2, arr2.length 넉넉하게 써도 개수만큼만 지운다.) 
// assert.deepStrictEqual(ex5, [3, 4, 5]);
// assert.deepStrictEqual(arr2, [1, 2]);

// // ex6) 복원하기
// const ex6 = arr2.splice(2, 0, ...ex5);
// assert.deepStrictEqual(ex6, []);
// assert.deepStrictEqual(arr2, [1, 2, 3, 4, 5]);

// // ex7) [1,2, 'X', 'Y', 'Z', 4, 5] 만들기
// // - 방법1) 3부터 모두 지우고 'X', 'Y', 'Z', 4, 5 추가
// const ex7 = arr2.splice(2, arr2.length, 'X', 'Y', 'Z', 4, 5);
// assert.deepStrictEqual(arr2, [1, 2, 'X', 'Y', 'Z', 4, 5]);
// // ==>  복원
// arr2.splice(2, Infinity, ...ex7);
// assert.deepStrictEqual(arr2, [1, 2, 3, 4, 5]);

// // - 방법2) 3만 지우고 'X', 'Y', 'Z' 추가
// const ex7_2 = arr2.splice(2, 1, 'X', 'Y', 'Z');
// assert.deepStrictEqual(arr2, [1, 2, 'X', 'Y', 'Z', 4, 5]);
// arr2.splice();
// assert.deepStrictEqual(arr2, [1, 2, 3, 4, 5]);
// console.log(ex7_2)

// // ex8) 위 7번 문제를 splice를 사용하지 말고 작성하시오.
// const ex8 = [];
// assert.deepStrictEqual(ex8, [1, 2, 'X', 'Y', 'Z', 4, 5]);
const assert = require('assert');
const arr = [1, 2, 3, 4];

// push 순수함수로
// assert.deepStrictEqual(push(arr, 5, 6), [1, 2, 3, 4, 5, 6]);
assert.deepStrictEqual(arr.toSpliced(arr.length, 0, 5, 6), [1, 2, 3, 4, 5, 6]);
console.log(arr.toSpliced(arr.length, 0, 5, 6))

// assert.deepStrictEqual(pop(arr), 4); 
assert.deepStrictEqual(arr.slice(3), [4]);
console.log(arr.slice(3))
// assert.deepStrictEqual(pop(arr, 2), [3, 4]);    // 2개 팝!
assert.deepStrictEqual(arr.slice(2), [3, 4])
console.log(arr.slice(2))

// assert.deepStrictEqual(unshift(arr, 0), [0, 1, 2, 3, 4]);
console.log(arr.toSpliced(0,0,0))
console.log(arr.toSpliced(0,0,7, 8))
// assert.deepStrictEqual(unshift(arr, 7, 8), [7, 8, 1, 2, 3, 4]);

// // [ [shift되는 원소들], [남은 원소들] ]
// assert.deepStrictEqual(shift(arr), [[1], [2, 3, 4]]);

// assert.deepStrictEqual(shift(arr, 2), [[1, 2], [3, 4]]); // 2개 shift
// assert.deepStrictEqual(arr, [1, 2, 3, 4]); 