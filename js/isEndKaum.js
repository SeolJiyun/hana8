// const isEndJaum = str => {
//     const alphaNums = 'lmnr136780';
//     const lastChar = str.at(-1);
//     // if(alphaNums.includes(lastChar)) return true;
//     if(/[lmnr136780]/i.test(lastChar)) return true;

//     const lastCharCode = lastChar.charCodeAt();
//     const ㄱ = 'ㄱ'.charCodeAt();
//     const ㅎ = 'ㅎ'.charCodeAt();
//     if (lastCharCode >= ㄱ && lastCharCode <= ㅎ) return true;

//     const 가 = '가'.charCodeAt();
//     const 힣 = '힣'.charCodeAt();
//     if (lastCharCode >= 가 && lastCharCode <= 힣 && (lastCharCode-가)%28 !== 0  ) return true;
    

// };

const assert = require('assert');

// assert.equal(isEndJaum('아지오'), false);
// assert.equal(isEndJaum('북한강'), true);
// assert.equal(isEndJaum('뷁'), true);
// assert.equal(isEndJaum('강원도'), false);
// assert.equal(isEndJaum('바라당'), true);
// assert.equal(isEndJaum('ㅜㅜ'), false);
// assert.equal(isEndJaum('케잌'), true);
// assert.equal(isEndJaum('점수 A'), false);
// assert.equal(isEndJaum('알파벳L'), true);
// assert.equal(isEndJaum('24'), false);
// assert.equal(isEndJaum('23'), true);
// assert.equal(`고성군${iga('고성군')}`, '고성군이');
// assert.equal(`고성군${eunun('고성군')}`, '고성군은');
// assert.equal(`고성군${eulul('고성군')}`, '고성군을');
// assert.equal(`성동구${iga('성동구')}`, '성동구가');
// assert.equal(`성동구${eunun('성동구')}`, '성동구는');
// assert.equal(`성동구${eulul('성동구')}`, '성동구를');
// assert.equal(`고성군${eyuya('고성군')}`, '고성군이어야');
// assert.equal(`성동구${eyuya('성동구')}`, '성동구여야');


// 초성 찾기
const searchByKoreanInitialSound = (data, first) => {
    const regStr = [...first].reduce((reg, c) => {
        const ㄱㄴㄷ = 'ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ';
        const 가나다 = '가까나다띠라마바빠사싸아자짜차카타파하';
        const regStr = [...first].reduce((reg, c)=>{
            const idx = ㄱㄴㄷ.indexOf(c);
            const S = 가나다[idx];
            const eCode = 가나다[idx + 1].charCodeAt() - 1;

            return `${S} - ${String.fromCharCode(eCode)}`; // [ㄱ가-깋][ㄴ나-닣]
        });
    });
    const regexp = new RegExp(regStr);
    return data.filter(d=>regexp.test(d));
};

assert.deepStrictEqual(searchByKoreanInitialSound(s, 'ㄱㅇ'), ['강원도 고성군']);
assert.deepStrictEqual(searchByKoreanInitialSound(s, 'ㄱㅅㄱ'), ['강원도 고성군', '고성군 토성면']);
assert.deepStrictEqual(searchByKoreanInitialSound(s, 'ㅌㅅㅁ'), ['고성군 토성면', '토성면 북면']);
assert.deepStrictEqual(searchByKoreanInitialSound(s, 'ㅂㅁ'), ['토성면 북면', '북면']);
assert.deepStrictEqual(searchByKoreanInitialSound(s, 'ㅍㅁ'), []);
assert.deepStrictEqual(searchByKoreanInitialSound(s, 'ㄱ1ㅅ'), ['김1수']);
