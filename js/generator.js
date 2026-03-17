function* gener() {
    const x = yield 1;
    const y = yield (x + 10);
    console.log('x y =', x, y);
    return x + y;
}
const it3 = gener();
console.log(it3.next()); // { value: 1, done: false }
console.log(it3.next(3)); // { value: 13, done: false }

console.log(it3.next(5)); 
// x y = 3 5
// { value: 8, done: true }
