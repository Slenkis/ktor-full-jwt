async function login() {
    // get credentials from inputs
    const userEmail = document.getElementById("email_input").value;
    const userPassword = document.getElementById("password_input").value;

    fetch("/api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email: userEmail,
            password: userPassword,
        }),
    })
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            console.log(data);
            saveToken(data)
        });
}

function saveToken(token) {
    console.log(token)
    localStorage.setItem("accessToken", token.accessToken);
    // sessionStorage.setItem("accessToken", JSON.stringify(token));
    // sessionStorage.setItem("refreshToken", JSON.stringify(token.refreshToken));
}
