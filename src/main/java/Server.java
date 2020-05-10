import brugerautorisation.data.Bruger;
import brugerautorisation.data.TestBruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import com.google.cloud.firestore.CollectionReference;
import firebase.FirebaseInitialize;
import firebase.FirebaseService;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;

import java.rmi.Naming;

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
        /*
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(408);
        });
*/
        app.post("/login/", ctx -> {
            String brugernavn = ctx.queryParam("studienr");
            String kodeord = ctx.queryParam("password");

            try{
                Bruger bruger = ba.hentBruger(brugernavn, kodeord);
                if (bruger.brugernavn.equals(brugernavn) && bruger.adgangskode.equals(kodeord)) {
                    ctx.status(200);
                    String fornavn = bruger.fornavn;
                    ctx.result(fornavn);
                    // logget ind
                    System.out.println("Du er logget ind!");
                }
            }catch  (IllegalArgumentException e){
                ctx.status(401);
                System.out.println("forkert brugernavn eller adgangskode.");
                throw new UnauthorizedResponse("Forkert brugernavn eller adgangskode");
            }

        });

        app.get("/:studienr/spil/info", ctx -> {

//            String[] jsonArray = new String[6];
//            jsonArray[0] = synligtOrd;
//            jsonArray[1] = ordet;
//            jsonArray[2] = brugteBogstaver;
//            jsonArray[3] = status;
//            jsonArray[4] = liv;
//            jsonArray[5] = forkerte;
//            ctx.json(jsonArray);
        });

        app.get("/:studienr/highscores/", ctx -> {
            ctx.render("webapp/highscores.html");
            TestBruger testBruger = firebaseService.getBrugerDetails("2jtX8G4VEctxGUhq0GV5");
            System.out.println("Hentet bruger med navn: '" + testBruger.getName() + "' fra databasen.");
        });

    }
}