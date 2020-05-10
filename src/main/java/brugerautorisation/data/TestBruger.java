package brugerautorisation.data;

public class TestBruger {

    String name;

    // placeholder, der kommer ikke til at være "playerID" i den færdige database, spillernes navn skal hentes med studienummer som ID.
    String playerID;

    public TestBruger(){

    }

    public TestBruger(String name, String playerID){
        this.name = name;
        this.playerID = playerID;
    }

    public String getName(){
        return name;
    }

    public String getPlayerID(){
        return playerID;
    }
}
