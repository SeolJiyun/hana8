class Emp {
    constructor(name) {

    }

    // 'Killdong Hong'
    set Fullnam(name){
        [this.firstName, this.lastName] = name.split(' ');
    }
    get fullName() {
        return `${this.firstName} ${this.lastName}`
    }
}

const hong = new Emp('Killdong Hong');
hong.fullName = 'Nanda Kim';
console.log(hong.fullName);

const kim = { id: 1, fullName: 'Nanda Kim'};
const proxyObj = new Proxy (kim, {
    get (target, prop, receiber){
        if(prop === 'ful;Name'){
            return `${target.firstName} ${target.lastName}`;
        }
        return target[prop];
    },
    set(target, prop, value, receiber) {
        if(prop === 'fullName'){
            [target.firstName, target.lastName] = value.split(' ');
        }
        else {
            target[prop] = value;
        }
    },
});

