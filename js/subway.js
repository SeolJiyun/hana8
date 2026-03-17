// const LINE2 = [
//     '신도림',
//     '성수',
//     '신설동',
//     '용두',
//     '신답',
//     '용답',
//     '시청',
//     '충정로',
//     '아현',
//     '이대',
//     '신촌',
//     '공항철도',
//     '홍대입구',
//     '합정',
//     '당산',
//     '영등포구청',
//     '문래',
//     '대림',
//     '구로디지털단지',
//     '신대방',
//     '신림',
//     '봉천',
//     '서울대입구',
//     '낙성대',
//     '사당',
//     '방배',
//     '서초',
//     '교대',
//     '강남',
//     '역삼',
//     '선릉',
//     '삼성',
//     '종합운동장',
//     '신천',
//     '잠실',
//     '잠실나루',
//     '강변',
//     '구의',
//     '건대입구',
//     '뚝섬',
//     '한양대',
//     '왕십리',
//     '상왕십리',
//     '신당',
//     '동대문역사문화공원',
//     '을지로4가',
//     '을지로3가',
//     '을지로입구'
// ]

// class Subway {
//     constructor(start, end) {
//         this.start = start;
//         this.end = end;
//         this.startIdx = LINE2.indexOf(start);
//         this.endIdx = LINE2.indexOf(end);
//     }

//     [Symbol.iterator]() {
//         return (function* () {
//             let i = this.startIdx;
//             yield LINE2[i];

//             i = (i + 1) % LINE2.length;
//             if (i === this.endIdx) {
//                 yield LINE2[i];
//                 return;
//             }
//             while (i !== this.endIdx) {   // 도착 전에만 실행
//                 yield LINE2[i];
//                 i = (i + 1) % LINE2.length;
//             }

//             // 도착역 반환
//             yield LINE2[i];
//             return;

//         }).call(this);
//     }


// }

const assert = require('assert');
const routes1 = new Subway('문래', '신림');
console.log([...routes1]);
assert.deepStrictEqual(
  [...routes1],
  ['문래', '대림', '구로디지털단지', '신대방', '신림']
);

const it1 = routes1.iterator();
['문래', '대림', '구로디지털단지', '신대방', '신림'].forEach((value, i) => {
  assert.deepStrictEqual(it1.next(), { value, done: false });
  console.log(i, routes1.toString());
});
assert.deepStrictEqual(it1.next(), { value: undefined, done: true });

const routes2 = new Subway('구로디지털단지', '성수'); // 32개 정거장
routes2.iterator().next();
assert.strictEqual(
  routes2.toString(),
  '구로디지털단지역에서 성수역까지 가는 열차이며, 현재 신대방역입니다'
);
console.log([...routes2]); // ['신대방', ..., '성수']
const it2 = routes2[Symbol.iterator]();
while (true) {
  const x = it2.next();
  console.log(x);
  if (x.done) break;
}

const route3 = new Subway('문래', '합정'); // 46개 정거장이면 통과!
assert.strictEqual([...route3].length, 46);
const route4 = new Subway('신도림', '을지로입구'); // 48개 정거장이면 통과!
assert.strictEqual([...route4].length, 48);

// 게임 같은 맵에 같은 사람들이 들어있어야 한다. 같은 지하철 탔으면 같은 역에 있는 것처럼.
class Subway {
    #start;
    #end;
    #currIdx;
    constructor(start, end) {
        this.#start = start;
        this.#end = end;
        this.#currIdx = LINE2.indexOf(start);
    }

    *[Symbol.iterator]() {
        while (true){
            const nowStation = LINE2[this.#currIdx++];

            if(nowStation ===this.#end){
                yield nowStation;
                this.#currIdx = LINE2.indexOf(this.#start);
                break;
            }

            if(this.#currIdx === LINE2.length) this.#currIdx = 0;

            yield nowStation;
        }
    }
    iterator() {
        return this[Symbol.iterator]();
    }
    toString() {
        return `${this.#start}역에서 ${this.#end}역입니다.`;
    }
}