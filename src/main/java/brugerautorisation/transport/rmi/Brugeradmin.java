package brugerautorisation.transport.rmi;

import brugerautorisation.data.Bruger;

@SuppressWarnings("NonAsciiCharacters")
public interface Brugeradmin extends java.rmi.Remote {
    /**
     * Henter alle en brugers data
     * @return et Bruger-objekt med alle data
     */
    Bruger hentBruger(String brugernavn, String adgangskode) throws java.rmi.RemoteException;

    /**
     * �ndrer en brugers adgangskode
     * @return et Bruger-objekt med alle data
     */

    Bruger �ndrAdgangskode(String brugernavn, String glAdgangskode, String nyAdgangskode) throws java.rmi.RemoteException;

    /**
     * Sender en email til en bruger
     * @param brugernavn Brugeren, som emailen skal sendes til
     * @param emne Emnet - teksten DIST: bliver foranstillet i mailen
     * @param tekst Br�dteksten - teksten 'Sendt fra xxxx ' bliver tilf�jet  i mailen
     * @throws java.rmi.RemoteException Hvis der sker en fejl i transport eller p� serveren
     */
    void sendEmail(String brugernavn, String adgangskode, String emne, String tekst) throws java.rmi.RemoteException;

    void sendGlemtAdgangskodeEmail(String brugernavn, String f�lgetekst) throws java.rmi.RemoteException;

    /**
     * Giver mulighed for at gemme et ekstra felt for brugeren. Det kunne f.eks. v�re at en Galgeleg-backend �nskede at gemme hvor mange point brugeren har, til en highscoreliste
     * @param brugernavn Brugeren det drejer sig om. Adgangskode skal v�re korrekt, dvs det er ikke muligt at hente felter for brugere, der ikke er logget ind.
     * @param feltnavn Navnet p� feltet. Brug dit studie- eller gruppenummer som pr�fix, f.eks. "g22_galgeleg_point"
     * @param v�rdi V�rdien er et vilk�rligt objekt, f.eks. 223 (Integer) eller "223" (String)
     * @throws java.rmi.RemoteException Hvis der sker en fejl i transport eller p� serveren
     */
    void setEkstraFelt(String brugernavn, String adgangskode, String feltnavn, Object v�rdi) throws java.rmi.RemoteException;

    /**
     * Afl�ser et ekstra felt. Se setEkstraFelt
     */
    Object getEkstraFelt(String brugernavn, String adgangskode, String feltnavn) throws java.rmi.RemoteException;

    /**
     * Fjern en brugers ekstrafelter
     * @param brugernavn Brugeren det drejer sig om. Adgangskode skal v�re korrekt, dvs det er ikke muligt at slette felter for brugere, der ikke er logget ind.
     * @throws java.rmi.RemoteException Hvis der sker en fejl i transport eller p� serveren
     */
    void fjernAlleEkstraFelter(String brugernavn, String adgangskode) throws java.rmi.RemoteException;

    /**
     * Henter en brugers offentlige data
     * @return et Bruger-objekt med de offentlige data (brugernavn, fornavn, efternavn, email, campusnetId, studieretning, sidst aktiv)
     */
    Bruger hentBrugerOffentligt(String brugernavn) throws java.rmi.RemoteException;

}
