const highScoresList = document.getElementById("highScoresList");
var users;

function getInfo(){
    fetch(window.location.href+"/info", {
        method: 'GET'
    })
        .then((response) => response.json())
        .then(function (data){
            console.log("Fik svar: "+data);

            users = data;
            const highScores = [];
            users.forEach(function (item) {
                const user = {
                    navn: item.navn,
                    score: item.score
                };
                highScores.push(user);
            });
            highScores.sort( (a,b) => b.score - a.score);
            highScores.splice(5);
            console.log(highScores);

            highScoresList.innerHTML = highScores
                .map(user => {
                    return `<li class="high-score">${user.navn}: ${user.score}</li>`;
                })
                .join("");
        })
}