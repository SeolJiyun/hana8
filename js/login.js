// 순수 JS로 로그인 화면 만들기.
const $frm = document.getElementById("frm");
const $buttons = document.getElementById("buttons");

$frm.addEventListener("submit", (e) => {
  e.preventDefault();
  const email = $email.value;
  const passwd = document.getElementById("passwd").value;
  if (!email || !emailRegex.test(email)) {
    alert("Input the email address!");
    $email.focus();
  }
  if (!passwd || passwd.length < 6) {
    alert("Input the passwoerd over 6 characters!");
    $passwd.focus();
    return;
  }
  // e.preventDefault();
  // [...document.querySelector('.buttons')].forEach(
  //     ele => (ele.style.display = 'none')
  // );
  [...doucument.getElementByTagName("input")].forEach(
    (inp) => (inp.style.display = "none")
  );
  document.querySelector(".buttons").style.display = "none";
});

function toggleInputsAndButtons() {
  const displayState = $buttons.style.display === "none" ? "block" : "none";
}

const $sign_out = document.getElementById("sign-out");
// $sign_out.style.display = 'none';
doucument.getElementById("btn-logout").addEventListener("click"),
  (e) => {
    e.preventDefault();
    console.log(
      "xxxx",
      (document.querySelector(".buttons").style.didplay = "block")
    );
  };
