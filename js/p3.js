function makeArray(n) {
    if (n === 1) return [1];
    const [...rest] = makeArray(n - 1);
    return [...rest, n];
}

function makeReverseArray(n) {
    if (n === 1) return [1];
    const [...rest] = makeReverseArray(n - 1);
    return [n, ...rest];
}

function makeArrayTCO(n, acc = []) {
    if (acc.length === n) return acc;
    const next = [...acc, acc.length + 1];
    return makeArrayTCO(n, next);
}
console.log(makeArray(10));
// [1,2,3,4,5,6,7,8,9,10]

console.log(makeReverseArray(5));
// [5,4,3,2,1]

console.log(makeArrayTCO(10));
// [1,2,3,4,5,6,7,8,9,10]

