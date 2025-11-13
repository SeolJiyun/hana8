// function getWeekNameBad(date) {
//     const date = arguments[0];
//     console.log("🚀 ~ getWeekName ~ date:", date);
// }

// getWeekNameBad(new Date());''

const WEEKNAMES = '일월화수목금토';
getWeekName();
getWeekName(new Date());

function getWeekName(date) {
    const weekName = WEEKNAMES[(date ?? new Date()).getDay()];
    console.log(`오늘은 ${weekName}요일입니다.`)
    console.log("🚀 ~ getWeekName ~ date:", date);
}