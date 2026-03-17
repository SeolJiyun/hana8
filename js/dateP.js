// Q1 ----------------------------
const d1 = new Date(1970, 0, 1);
const d2 = new Date(1970, 0, 2);
console.log(`q1: ${d2-d1}`);

// Q2-------------------------------
const now = new Date();
const year = now.getFullYear();
const month = now.getMonth(); // 0부터 시작

// 이번달 마지막 날짜
const lastDay = new Date(year, month + 1, 0).getDate();

// 중복 허용
// const arr = [];
// for (let i = 0; i < 5; i++) {
//     const r = Math.floor(Math.random() * lastDay) + 1;
//     arr.push(r);
// }
function rand(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

// 중복 삭제
const dates = [];
do {
    const r = rand(1, lastDay);
    if(!dates.includes(r)) dates.push(r);
} while(dates.length < 5);

// arr.sort((a, b) => a - b);
dates.sort((a, b) => (a>b ? 1:-1)).reverse();
console.log(...dates.map(d=>`${year}년 ${month+1}월 ${d}일\n`));

// Q3 -------------------------------
const yoil = ['일', '월', '화', '수', '목', '금', '토'];

const nextYearDate = new Date(now.getFullYear() + 1, now.getMonth(), now.getDate());
const d = nextYearDate.getDay();
console.log(`내년 오늘은 ${yoil[d]}요일`);

// Q4 -----------------------------------
const next100 = now
next100.setDate(next100.getDate() + 100);
console.log(`오늘부터 100일 뒤는 ${next100.getFullYear()}년 ${next100.getMonth()+1}월 ${next100.getDate()}일`);