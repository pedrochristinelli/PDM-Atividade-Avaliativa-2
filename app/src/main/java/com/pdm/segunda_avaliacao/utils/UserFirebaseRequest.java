package com.pdm.segunda_avaliacao.utils;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pdm.segunda_avaliacao.model.LocalUser;
import java.util.Map;
import java.util.Objects;

public class UserFirebaseRequest {

    public UserFirebaseRequest() {}

    public FirebaseUser getLoggedUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser();
    }

    public LocalUser getLocalUserData() {
        FirebaseUser firebaseUser = getLoggedUser();
        String idUser = "";
        idUser = firebaseUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> snapshot = db.collection("usuarios")
                .document(idUser)
                .get();

        Map<String, Object> map = Objects.requireNonNull(snapshot.getResult()).getData();
        String email = Objects.requireNonNull(map.get("email")).toString();
        String name = Objects.requireNonNull(map.get("name")).toString();
        String password = Objects.requireNonNull(map.get("password")).toString();

        LocalUser localUser = new LocalUser();
        localUser.setEmail(email);
        localUser.setUsername(name);
        localUser.setId(idUser);
        localUser.setPassword(password);

        return localUser;
    }
}
