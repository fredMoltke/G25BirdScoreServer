var studienummer, kodeord, spiller;

function login(){
    studienummer = document.getElementById("studienr").value;
    kodeord = document.getElementById("password").value;

    spiller = {
        studienr: studienummer,
        fornavn: "",
        score: "",
        password: kodeord
    };

    fetch("/login/", {
        method: 'POST',
        body: JSON.stringify(spiller),
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