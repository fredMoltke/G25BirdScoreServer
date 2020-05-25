package firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseService {

    private Firestore dbFirestore = FirestoreClient.getFirestore();

    public DbBruger getBrugerDetails(String studienr){
        CollectionReference collectionRef = dbFirestore.collection("Highscores");
        DocumentReference documentReference = collectionRef.document(studienr);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DbBruger bruger = null;
        DocumentSnapshot document;
        try {
            document = future.get();

            if (document.exists()){
                bruger = document.toObject(DbBruger.class);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bruger;
    }

    // https://firebase.google.com/docs/firestore/query-data/get-data#java_11
    public HashMap<String, DbBruger> getUsers(){
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("Highscores").get();
        HashMap<String, DbBruger> brugerHash = new HashMap<>();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents){
                DbBruger dbBruger = document.toObject(DbBruger.class);
                brugerHash.put(document.getId(), dbBruger);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return brugerHash;
    }

    public void saveScore(DbBruger bruger){
        Map<String, Object> dbBrugerHash = new HashMap<>();
        dbBrugerHash.put("navn", bruger.getNavn());
        dbBrugerHash.put("score", bruger.getScore());
        dbFirestore.collection("Highscores").document(bruger.getStudienr()).set(dbBrugerHash);
        System.out.println("Score upload - navn: " + bruger.getNavn() + " | score: " + bruger.getScore());
    }


}
