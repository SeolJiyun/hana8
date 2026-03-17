const assert = require('assert');
console.log('Hello mods!');
const add = (...args) => args.reduce((acc, a) => acc+a);

assert.strictEqual(add(1, 3), 4);
assert.strictEqual(add(1, 2, 3), 6);