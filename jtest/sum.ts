export const sum = (...args: number[]) => args.reduce((acc, n) => acc + n);
type User ={
    id: Number;
    username: string;
}

export const sumId = async() => {
    const users = (await fetch)
}