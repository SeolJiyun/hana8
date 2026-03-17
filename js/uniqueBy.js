const assert = require('assert');

assert.deepStrictEqual(
  users.groupByMap('dept'),
  Map.groupBy(users, user => user.dept)
);
assert.deepEqual(
  users.groupBy('dept'),
  Object.groupBy(users, user => user.dept)
);

// 중복되지 않는 prop 값 배열 반환: ['HR', 'Server', 'Front', 'Sales']
Array.prototype.uniqBy = function (prop) {
  const seen = new Set();
  const result = [];

  for (const item of this) {
    const key = typeof prop === 'function' ? prop(item) : item?.[prop];
    if (!seen.has(key)) {
      seen.add(key);
      result.push(key);
    }
  }

  return result;
};

// { HR: [hong, park], Server: [kim, ko], ... } 형태의 객체 반환
Array.prototype.groupBy = function (prop) {
  const result = {};

  for (const item of this) {
    const key = typeof prop === 'function' ? prop(item) : item?.[prop];
    if (!result[key]) {
      result[key] = [];
    }
    result[key].push(item);
  }

  return result;
};

// Map { 'HR' => [hong, park], 'Server' => [kim, ko], ... } 반환
Array.prototype.groupByMap = function (prop) {
  const map = new Map();

  for (const item of this) {
    const key = typeof prop === 'function' ? prop(item) : item?.[prop];
    if (!map.has(key)) {
      map.set(key, []);
    }
    map.get(key).push(item);
  }

  return map;
};
const hong = {id: 1, name: 'Hong', dept: 'HR'};
const kim = {id: 2, name: 'Kim', dept: 'Server'};
const lee = {id: 3, name: 'Lee', dept: 'Front'};
const park = {id: 4, name: 'Park', dept: 'HR'};
const ko = {id: 7, name: 'Ko', dept: 'Server'};
const loon = {id: 6, name: 'Loon', dept: 'Sales'};
const choi = {id: 5, name: 'Choi', dept: 'Front'};

const users = [ hong, kim, lee, park, ko, loon, choi ];
assert.deepStrictEqual(users.uniqBy('dept'), [ 'HR', 'Server', 'Front', 'Sales' ]);