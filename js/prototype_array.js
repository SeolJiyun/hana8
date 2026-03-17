const assert = require('assert');
const arr = [1, 2, 3, 4, 5];
const hong = { id: 1, name: 'Hing' };
const kim = { id: 2, name: 'Kim' };
const lee = { id: 3, name: 'Lee' };
const users = [hong, lee, kim];

Object.defineProperties(Array.prototype, {
    firstObject: {
        get() {
        return this[0];
        },
        set(val) {
            this[0] = val;
        },
    },
    lastObject: {
        get() {
            return this[this.length - 1];
        },
        set(val) {
        this[this.length - 1] = val;
        },
    },
});




// 1) mapBy(key)
Object.defineProperty(Array.prototype, 'mapBy', {
    value: function (key) {
        return this.map(item => item[key]);
    },
});

// 2) findBy(key, value)
Object.defineProperty(Array.prototype, 'findBy', {
    value: function (key, value) {
        return this.find(item => item[key] === value);
    },
});

// 3) filterBy(key, value, isInclude)
// isInclude = true → 부분 포함(match)
// isInclude = false 또는 생략 → 완전 일치
Object.defineProperty(Array.prototype, 'filterBy', {
  value: function (key, value, isInclude = false) {
    return this.filter(item => {
      const v = item[key];
      return isInclude ? v.includes(value) : v === value;
    });
  },
});

// 4) rejectBy(key, value, isInclude)
Object.defineProperty(Array.prototype, 'rejectBy', {
  value: function (key, value, isInclude = false) {
    return this.filter(item => {
      const v = item[key];
      return isInclude ? !v.includes(value) : v !== value;
    });
  },
});

// 5) sortBy("name") or sortBy("name:desc")
Object.defineProperty(Array.prototype, 'sortBy', {
  value: function (keyString) {
    // key: "name" or "name:desc"
    const [key, dir] = keyString.split(':');
    const sign = dir === 'desc' ? -1 : 1;

    // 원본(users) 오염 금지 → shallow copy 후 sort
    return [...this].sort((a, b) => {
      if (a[key] < b[key]) return -1 * sign;
      if (a[key] > b[key]) return 1 * sign;
      return 0;
    });
  },
});



assert.deepStrictEqual([arr.firstObject, arr.lastObject], [1, 5]);
assert.deepStrictEqual(users.mapBy('id'), [1, 3, 2]); // users.map(u => u['id'])
assert.deepStrictEqual(users.mapBy('name'), ['Hing', 'Lee', 'Kim']);
assert.deepStrictEqual(users.filterBy('id', 2), [kim]);
assert.deepStrictEqual(users.filterBy('name', 'i', true), [hong, kim]); // key, value일부, isInclude
assert.deepStrictEqual(users.rejectBy('id', 2), [hong, lee]);
assert.deepStrictEqual(users.rejectBy('name', 'i', true), [lee]);
assert.deepStrictEqual(users.findBy('name', 'Kim'), kim);
assert.deepStrictEqual(users.sortBy('name:desc'), [lee, kim, hong]);
assert.deepStrictEqual(users.sortBy('name'), [hong, kim, lee]);
assert.deepStrictEqual(users.firstObject, hong);
assert.deepStrictEqual(users.lastObject, kim);
users.firstObject = kim;
assert.deepStrictEqual(users.firstObject, kim);
users.lastObject = hong;
assert.deepStrictEqual(users.lastObject, hong);


console.log("=== 테스트 시작 ===");

// mapBy
console.log("mapBy id:", users.mapBy('id'));
console.log("mapBy name:", users.mapBy('name'));

// findBy
console.log("findBy name=Kim:", users.findBy('name', 'Kim'));

// filterBy
console.log("filterBy id=2:", users.filterBy('id', 2));
console.log("filterBy name includes 'i':", users.filterBy('name', 'i', true));

// rejectBy
console.log("rejectBy id=2:", users.rejectBy('id', 2));
console.log("rejectBy name includes 'i':", users.rejectBy('name', 'i', true));

// sortBy
console.log("sortBy name asc:", users.sortBy('name'));
console.log("sortBy name desc:", users.sortBy('name:desc'));

// firstObject / lastObject
console.log("firstObject:", users.firstObject);
console.log("lastObject:", users.lastObject);

// setter 테스트
users.firstObject = kim;
console.log("firstObject (after set to kim):", users.firstObject);

users.lastObject = hong;
console.log("lastObject (after set to hong):", users.lastObject);

console.log("=== 테스트 끝 ===");


class Dog {
    constructor(name) {
        this.name = name;
    }


    getName() {
        return this.name;
    }

    fn() {
        return 'FN';
    }

    static sfn() {  // Dog.sfn
        return 'SFN';
    }
}
const lucy = new Dog('Lucy');
const { sfn } = Dog;
const { fn } = Dog.prototype;
const { name: aa, fn: fnnn } = lucy;
const getName = lucy.getName.bind(lucy);


console.log(aa, sfn(), fnnn(), getName); // ?
console.log(getName());  // Lucy // ?

console.log("=== 테스트 시작 ===");

const getNameBound = lucy.getName.bind(lucy);

console.log("aa:", aa);
console.log("sfn():", sfn());
console.log("fnnn():", fnnn());
console.log("getNameBound():", getNameBound());

console.log("=== 테스트 끝 ===");
