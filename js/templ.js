const holiday = '한글날';
const month = 10;
const date = 9;

console.log(`${holiday}은 ${month}월 ${date}`);

// style`${holiday}은 ${month}월 ${date}`;

function f(txts, a, b, c) {
    console.log('txts>>', txts);
    console.log('a>>', a);
    console.log('b>>', b);
    console.log('c>>', c);
}

for(let i= '가'.charCodeAt(); i<='깋'.charCodeAt(); i++){
    // (i-16) % 28 받침 구분하기 가능 
    console.log(i, String.fromCharCode(i), (i-16) % 28);
}