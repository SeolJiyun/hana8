// // getPosts(1);
// // [
// //     {
// //         postId: 게시글ID,
// //         title: 게시글 제목,
// //         comments: [댓글 목록]
// //     },
// //     { }
// // 

// const { error } = require("console");
// const { title } = require("process");


// async function getPosts(userId) {
//     postsRes = await fetch(`https://jsonplaceholder.typicode.com/posts?userId=${userId}`);
//     const posts = await postsRes.json();

//     const result = await Promise.allSettled(
//         posts.map(async(post) =>{
//             try {
//                 const commentRes = await fetch(
//                     `https://jsonplaceholder.typicode.com/posts/${post.id}/comments`
//                 );
//                 const comments = await commentRes.json();

//                 return{
//                     postId: post.id,
//                     title: post.title,
//                     comments
//                 };
//             } catch (err){
//                 return {
//                     postId:post.id,
//                     title: post.title,
//                     comments:[],
//                     error: true
//                 };
//             }
//         })
//     );
//     return result.map(r => r.value ?? {error:true});
    
// }

// getPosts(1).then(console.log);

const API = 'https://jsonplaceholder.typicode.com';
const getPosts = async userId => fetch(`${API}/posts?userId=${userId}`).then(res => res.json());

const posts = await getPostsByUserId(1);
console.log("🚀 ~ posts:", posts)

const postComments = await Promise.all(
    posts.map(post => getCommentsByUserId(post.id))
);

const results = [];
for (let i =0; i < posts.length; i++) {
    const { id: postId, title} = posts[i];
    const comments = comments[i];
    result.push({postId, title, comments});
    console.log("🚀 ~ result:", result)

}

fetchData();