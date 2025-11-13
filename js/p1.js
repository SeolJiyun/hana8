// 연습문제 입니다.


for(let i = 0.1; i < 1; i = i + 0.1) console.log(parseFloat(i.toFixed(1))); 


console.log("무리수만 출력합니다.");
for(let i = 1; i <= 10; i++) {
    const root = Math.sqrt(i);
    if(!Number.isInteger(root)){
        console.log(`${i}의 제곱근은 ${root.toFixed(3)}`); 
    }
    
}

// 날짜, 요일
const today = new Date();
const year = today.getFullYear();   //연
const month = today.getMonth();     //월
const date = today.getDate();       //일
const day = today.getDay();         //요일

const WEEK_NAMES = '일월화수목금토';

console.log(`\n오늘은 ${year}년 ${month}월 ${date}일 ${WEEK_NAMES[day]}요일입니다.\n`);

// 제대로 개산하기
const 
a = 0.2153513465313121846466131321313214, b = 0.1,
c=0.123456789012345678901, d=0.11,                          // 15~16자리까지 표현가능하다. 더 정밀하면 decimal.js, **BigDecimal (Java), decimal.Decimal (Python) 같은 고정소수(정확한 십진법) 타입을 씁니다.
e=0.34, f=0.226;

function addPoints(a, b) {
    const lenA = a.toString().split('.')[1]?.length || 0;   // ?. 이건 옵셔널 체이닝(optional chaining) 연산자
    const lenB = b.toString().split('.')[1]?.length || 0;

    const maxLen = Math.max(lenA, lenB);
    const pow = Math.pow(10, maxLen);

    const result = (Math.round(a * pow) + Math.round(b * pow)) / pow;
    return parseFloat(result.toFixed(maxLen));
}

console.log(addPoints(a, b));
console.log(addPoints(c, d));
console.log(addPoints(e, f));
console.log(addPoints(10.34, 200.226)); // 210.566
console.log(addPoints(0.143, -10.28));  // -10.137
console.log(`${addPoints(0.143, -10)}\n`);     // -9.857


// 정상적인 숫자들의 평균.
const prices = [10.34, 19, 'xxx', 5.678, null, '20.9', 1.005, 0, undefined, 0.5];
let sum = 0;
let count = 0;

for (let i = 0; i < prices.length; i++) {
    const n = Number(prices[i]); // 문자열 포함 숫자로 변환 시도

    // 숫자이거나 숫자 모양 문자열이면 통과
    if (!isNaN(n)) {
        sum = addPoints(sum, n);
        count++;
        console.log(`${i}: ${sum} <== +${n}`);
    }
}
const avg = count ? sum / count : 0;
console.log(`평균 ${avg}`);