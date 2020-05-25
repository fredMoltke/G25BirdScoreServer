import brugerautorisation.data.Bruger;
import brugerautorisation.data.Spiller;
import com.google.gson.Gson;
import firebase.DbBruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import firebase.FirebaseInitialize;
import firebase.FirebaseService;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("NonAsciiCharacters")
public class Server {
    private static Javalin app;
    private static Brugeradmin ba;
    private static FirebaseInitialize firebaseInitialize;
    private static FirebaseService firebaseService;

    public static void main(String[] args) throws Exception {
        start();
        ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        firebaseInitialize = new FirebaseInitialize();
        firebaseInitialize.initialize();
        firebaseService = new FirebaseService();
    }

    public static void start() throws Exception {
        if (app != null) return;

        app = Javalin.create(javalinConfig -> javalinConfig.addStaticFiles("webapp")).start(8080);
        app.before(ctx -> {
            System.out.println("Javalin Server fik " + ctx.method() + " på " + ctx.url() + " med query " + ctx.queryParamMap() + " og form " + ctx.formParamMap());
        });

        app.post("/login/", ctx -> {
            Gson gson = new Gson();
            String body = ctx.body();
            Spiller spiller = gson.fromJson(body, Spiller.class);

            try{
                Bruger bruger = ba.hentBruger(spiller.getStudienr(), spiller.getPassword());
                if (bruger.brugernavn.equals(spiller.getStudienr()) && bruger.adgangskode.equals(spiller.getPassword())) {
                    ctx.status(200);
                    String brugerJson = gson.toJson(bruger);
                    ctx.result(brugerJson);
                    // logget ind
                    System.out.println("Du er logget ind!");
                }
            }catch  (IllegalArgumentException e){
                ctx.status(401);
                System.out.println("forkert brugernavn eller adgangskode.");
                throw new UnauthorizedResponse("Forkert brugernavn eller adgangskode");
            }

        });

        app.post("/score/", ctx -> {
            Gson gson = new Gson();
            String body = ctx.body();
            Spiller spiller = gson.fromJson(body, Spiller.class);
            DbBruger dbBruger = new DbBruger(spiller.getFornavn(), spiller.getScore());
            dbBruger.setStudienr(spiller.getStudienr());
            try {
                firebaseService.saveScore(dbBruger);
            } catch (Exception e) {
                ctx.status(500);
                e.printStackTrace();
                return;
            }
            String spillerJson = gson.toJson(spiller);
            ctx.result(spillerJson);
            ctx.status(200);
        });

        app.get("highscores/info", ctx -> {

            HashMap<String, DbBruger> brugerHash = firebaseService.getUsers();
            TimeUnit.MILLISECONDS.sleep(5);
            DbBruger[] jsonArray = new DbBruger[brugerHash.size()];
            List<String> studentIDs = new ArrayList<>();

            for (Map.Entry<String, DbBruger> entry : brugerHash.entrySet()){
                studentIDs.add(entry.getKey());
            }

            for (int i = 0; i < brugerHash.size(); i++){
                jsonArray[i] = brugerHash.get(studentIDs.get(i));
            }
            ctx.json(jsonArray);
        });

        app.get("/highscores/", ctx -> {
            ctx.render("webapp/highscores.html");
        });

    }
}