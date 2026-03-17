const assert = require('assert');

const randTime = (sec) => new Promise((resolve, reject) =>{
  console.log(resolve, sec * 1000 * Math.random())
});

Promise.all([randTime(1), randTime(2), ])

