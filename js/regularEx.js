const assert = require('assert');

// fmt 함수를 작성
const total = {price: 45000, vat: 4500};
console.log(fmt`주문합계: ${total.price}원`);
console.log(fmt`세액합계: ${total.vat}원`);

function fmt(strings, won){
    result = String(won).padStart(6, ' ');
    return strings[0] + result + strings[1];
}

// 대문자 -> 소문자
assert.strictEqual( upperToLower('Senior Coding Learning JS'), '*s*-enior *c*-oding *l*-earning *j*-*s*-');

// function upperToLower(str){
//     return str.replace(/[A-Z]/g, ch => String.fromCharCode(ch.charCodeAt(0) + 32));
// }

// const upperToLower = str =>
//     str.replace(/[A-Z]/g, matchedeStr => ``
//         ch => String.fromCharCode(ch.charCodeAt(0) + 32));



// 조사 '이/가', '을/를', '은/는' 붙이기
console.log(`고성군${iga('고성군')}`);
console.log(`고성군${eunun('고성군')}`);
console.log(`고성군${eulul('고성군')}`);
function iga(str){
    if(josa(str.length-1)==true){
        return "이";
    }
    else {
        return "가"
    }
}
function eunun(str){
    if(josa(str.length-1)==true){
        return "은";
    }
    else {
        return "는"
    }
}
function eulul(str){
    if(josa(str.length-1)==true){
        return "을";
    }
    else {
        return "를"
    }
}
function josa(str){
    for(let i= '가'.charCodeAt(); i<='깋'.charCodeAt(); i++){
    // (i-16) % 28 받침 구분하기 가능 
    if ((i-16) % 28 == 0){ 
        return true;
    }
    else{ false;}
}
}
