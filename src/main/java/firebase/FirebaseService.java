package firebase;

import brugerautorisation.data.Bruger;
import brugerautorisation.data.TestBruger;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.eclipse.jetty.client.api.Request;

import java.util.concurrent.ExecutionException;

public class FirebaseService {

    private Firestore dbFirestore = FirestoreClient.getFirestore();
    private CollectionReference collectionRef = dbFirestore.collection("Highscores");

    // 2jtX8G4VEctxGUhq0GV5
    public TestBruger getBrugerDetails(String name){
        DocumentReference documentReference = dbFirestore.collection("players").document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        TestBruger bruger = null;
        DocumentSnapshot document;
        try {
            document = future.get();

            if (document.exists()){
                bruger = document.toObject(TestBruger.class);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bruger;
    }

    public void loadScores(){
    }


}
