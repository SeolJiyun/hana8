const readline = require('readline');
const { stdin: input, stdout: output } = require('process');


function* add() {
    const x = yield('첫번째수는?');
    const y = yield('두번째수는?');
    return `Total ${x + y}`;
}
const itAdd = add();
const q1 = itAdd.next().value; //첫번째 수?
const rl = readline.createInterface({ input, output });

// rl.question('What do you think of Node.js? ', (answer) => {
//     // TODO: Log the answer in a database
//     console.log(`Thank you for your valuable feedback: ${answer}`);

//     rl.close();
//     });

//     rl.on('close', function () {
//     process.exit();  
//     });

function run({value, done}){
    if(done == true){
        rl.close();
    }
    rl.question(q1, (answerl) => {
    const q2 = itAdd.next(Number(answerl)).value;   // 두번째 수?

    rl.question(q2, (answer2) => {
        const result = itAdd.next(Number(answer2)).value;
        console.log(result);
        rl.close();
    });
});
}

// r1.on('line', answer => {
//     const {value, done} = addListener.next(answer);
//     if(done){

//     }
//     });

rl.on('close', () => process.exit());
// console.log(itAdd.next().value); 
// console.log(itAdd.next(1).value);
// console.log(itAdd.next(2).value);