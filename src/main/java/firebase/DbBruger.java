package firebase;

public class DbBruger {

    String navn;

    // placeholder, der kommer ikke til at være "playerID" i den færdige database, spillernes navn skal hentes med studienummer som ID.
    String score;

    public DbBruger(){

    }

    public DbBruger(String navn, String score){
        this.navn = navn;
        this.score = score;
    }

    public String getNavn(){
        return navn;
    }

    public String getScore(){
        return score;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
