var studienr;

function login(){
    studienr = document.getElementById("studienr").value;
    password = document.getElementById("password").value;

    fetch("/login/?studienr="+studienr+"&password="+password, {
        method: 'POST'
    })
        .then((response) => response.status)
        .then(function (data){
            console.log("Fik svar: "+data);
            if (data === 200){
                window.location.assign("/highscores");
            } else if (data === 401){
                document.getElementById("statustekst").innerText = "Forkert brugernavn eller kodeord.";
            } else {
            }
        })
}