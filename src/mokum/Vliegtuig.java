package mokum;

import java.util.Random;

//v1.3:Vergeten aanpassing door te voeren, MaakVertrekArray
//old		for(int i=1; i<aantalLandingen; i++) {
//new		for(int i=2; i<aantalLandingen; i++) {

//v1.3: MaakVertrekArray// voorkomt de arrayoutofbounds exception 3, wanneer je een lege route hebt
//old		return new int[][] {{0, 0, 0}};
//new		return new int[][] {{0, 0, 0,0}};

//v1.2:MaakVertrekArray
//old		for(int i=1; i<aantalLandingen -1; i++) {
//new		for(int i=1; i<aantalLandingen; i++) {

//v1.2:GeefRouteDuur
// Converteert niet meer bij elke landing naar een int, maar doet dit pas bij het retouneren van het totaal.

//v1.1: Changelog >> Verwijderlanding if(index < aantalLandingen) >> if(index >= aantalLandignen)
/*	Het Vliegtuig object bevat gegevens van het vliegtuigtype dat Mokum Airlines
 gebruikt en bevat methoden om de vliegtuig routes te maken
 */
public class Vliegtuig {

	private static final String THUISHAVEN = "Amsterdam";
	private static final int MAX_ROUTE_LENGTE = 20; //Meer dan 20 landingen komt niet voor

	private static final int MAX_PASSAGIERS = 199; // Het maximaal aantal
	// passagiers dat het
	// vliegtuig kan vervoeren
	private static final int MAX_BEREIK = 3300; // Het aantal kilometers dat het
	// vliegtuig kan afleggen op 1
	// tank
	private static final int VLIEGTUIG_SNELHEID = 800; // Maximale snelheid van
	// het vliegtuig in km/h
	public int aantalLandingen; // De route die het vliegtuig aflegt: een

	public int aantalMogelijkeBestemmingen; //Gebaseerd op geefMogelijkeLandingen();
	// rijtje landingen
	private Random	RANDOM = new Random(); //Nodig voor het genereren van random getallen
	public Landing[] route;
	private int [][]passagiers;
	private int [][]copyList;

	// Constructors
	public Vliegtuig(int[][]passagiers) {
		aantalLandingen = 0;
		this.passagiers = passagiers;
		copyList();
		route = new Landing[MAX_ROUTE_LENGTE];
	}

	public Vliegtuig(Vliegtuig other) {
		this.aantalLandingen = other.aantalLandingen;
		this.route = new Landing[MAX_ROUTE_LENGTE];
		for(int i=0; i<aantalLandingen; i++){
			route[i] = new Landing(other.route[i]);
		}
	}

	/* Methoden */
	// Geeft het aantal landingen in de huidige route van het vliegtuig
	public int geefAantalLandingen() {
		return aantalLandingen;
	}

	private void copyList(){
		copyList = new int[City.CITIES.size()][City.CITIES.size()];
		for(int i = 0; i < City.CITIES.size(); i++){
			for(int j = 0; j < City.CITIES.size(); j++){
				copyList[i][j] = passagiers[i][j];
			}
		}
	}

	// Geeft de snelheid van het vliegtuig
	public int geefSnelheid() {
		return VLIEGTUIG_SNELHEID;
	}

	// Geeft de capaciteit van het vliegtuig
	public int geefCapaciteit() {
		return MAX_PASSAGIERS;
	}

	// Geeft het bereik van het vliegtuig
	public int geefBereik() {
		return MAX_BEREIK;
	}

	// Geeft de landing op index in de route
	public Landing geefLanding(int index) {
		if (index > aantalLandingen - 1) {
			System.out.println("geefLanding: voor index " + index
					+ " is er geen landing");
			return null;
		}
		return route[index];
	}

	// Geeft aan of de route de thuishaven aandoet
	public boolean langsThuishavenGeweest() {
		for (int i = 0; i < aantalLandingen; i++) {
			if (geefLanding(i).geefLocatieNaam().equalsIgnoreCase(THUISHAVEN)) {
				return true;
			}
		}
		return false;
	}

	public int aantalKeerThuishavenInRoute() {
		int resultaat = 0;
		for (int i = 0; i < aantalLandingen-1; i++) { //tel laatste niet mee
			if (geefLanding(i).geefLocatieNaam().equalsIgnoreCase(THUISHAVEN)) {
				resultaat++;
			}
		}
		return resultaat;
	}

	public static String geefThuishaven() {
		return THUISHAVEN;
	}

	// Plant de tankbeurten dusdanig dat het vliegtuig pas tankt als het in de
	// lucht droog komt te staan wanneer dit niet gebeurt. !! GAAT ER VAN UIT
	// DAT
	// ER NOG NIET GETANKT WORDT TIJDENS EEN LANDING
	public void planTankbeurten() {
		if (aantalLandingen > 1) {

			int bereik = MAX_BEREIK;
			for (int i = 1; i < aantalLandingen; i++) {
				int afstand = geefLanding(i - 1).geefAfstandNaar(
						geefLanding(i).geefLoc());
				//if (afstand > MAX_BEREIK) {
				//	System.out
				//			.println("Een vliegtuig vliegt naar een bestemming die buiten zijn bereik ligt");
				//}
				if (afstand > bereik) {
					geefLanding(i - 1).wijzigTankbeurt(true);
					bereik = MAX_BEREIK - afstand;
				} else {
					bereik -= afstand;
				}
			}
		}
	}

	public void resetTankbeurten(){
		for(int i=0; i<aantalLandingen;i++){
			geefLanding(i).wijzigTankbeurt(false);
		}
	}

	// Retouneert de verzameling landingen van dit vliegtuig vanaf beginIndex
	// met lengte
	public Landing[] geefSubroute(int beginIndex, int lengte) {
		if ((aantalLandingen - beginIndex - lengte) >= 0) {
			Landing[] resultaat = new Landing[lengte];
			for (int i = beginIndex; i < beginIndex + lengte; i++) {
				resultaat[i - beginIndex] = route[i].clone();
			}
			return resultaat;
		}

		System.out.println("Een subroute met lengte " + lengte
				+ " is te groot vanaf " + beginIndex);
		return null; // Een subroute met deze lengte is te groot vanaf
		// beginIndex
	}

	// Geeft de duur van de route
	public int geefRouteDuur() {
		double routeTijd = 0;
		if (aantalLandingen == 0) {
			return 0;
		}

		routeTijd += route[0].geefGrondtijd();
		for (int i = 1; i < aantalLandingen; i++) {
			Landing punt = route[i];
			double tmp = (double) punt.geefAfstandNaar(route[i - 1].geefLoc())
					/ VLIEGTUIG_SNELHEID * 60;
			routeTijd += tmp + punt.geefTotaleGrondtijd();
		}

		// Bij de laatste landing maakt het niet meer uit hoe lang het toestel
		// aan de grond staat
		routeTijd -= route[aantalLandingen - 1].geefTotaleGrondtijd();
		if (routeTijd < 0) {
			return 0;
		}
		return (int) routeTijd;
	}

	/*public int geefRouteDuur(){
		double routeTijd = 0;
		for (int i=0; i<aantalLandingen-1; i++){
			Landing landing = route[i];
			double afstand = landing.geefAfstandNaar(route[i+1].geefLoc());
			routeTijd += afstand/VLIEGTUIG_SNELHEID * 60 + landing.geefTotaleGrondtijd();
		}
		return (int) routeTijd;
	}*/

	// Voegt toeTeVoegen achteraan toe aan de huidige route als dit mogelijk is
	
	public boolean voegRouteToe(Landing[] toeTeVoegen) {
		int nieuweLengte = this.aantalLandingen + toeTeVoegen.length;
		if (nieuweLengte > MAX_ROUTE_LENGTE) {
			System.out
			.print("CombineerRoutes: lengte van samengevoegde route is te groot ");
			System.out.println("(" + this.aantalLandingen + " + "
					+ toeTeVoegen.length + " > " + MAX_ROUTE_LENGTE + ")");
			return false;
		}
		for (int i = this.aantalLandingen; i < nieuweLengte; i++) {
			this.route[i] = toeTeVoegen[i - this.aantalLandingen].clone();
		}
		this.aantalLandingen = nieuweLengte;
		return true;
	}

	// Maakt een route van random landingen die ONGEVEER duur minuten kost
	public void maakRandomRoute(int duur) {
		aantalLandingen = 0;
		route = new Landing[MAX_ROUTE_LENGTE];
		while (geefRouteDuur() < duur) {
			if (!voegRandomLandingToe()) {
				break;
			}
		}
		if(!langsThuishavenGeweest()){
			int random;
			// Deze while loop voegt pas een landing toe op index [random] als de vorige en volgende bestemming binnen het max-bereik t.o.v. Amsterdam liggen.
			while(true){
				random = RANDOM.nextInt(aantalLandingen-1);
				if(nietLeeg(geefLoc(random),geefLoc(random + 1)) && route[random + 1].geefAfstandNaar(0) <= 3300){
					if(random == 0){
						break;
					} else if(nietLeeg(geefLoc(random-1),geefLoc(random)) && (route[random - 1].geefAfstandNaar(0) <= 3300)){
						break;
					}
				}
			}
			if(random != 0){
				passagiers[geefLoc(random-1)][geefLoc(random)] = copyList[geefLoc(random-1)][geefLoc(random)];
				updatePassagiers(geefLoc(random-1), route[random].geefIndex(THUISHAVEN));
			}
			passagiers[geefLoc(random)][geefLoc(random + 1)] = copyList[geefLoc(random)][geefLoc(random + 1)];
			updatePassagiers(route[random].geefIndex(THUISHAVEN), geefLoc(random + 1));
			route[random] = new Landing(City.CITIES.get(0)); //verander een van de landingen in Amsterdam
		}
		//eindlocatie inplannen -> eindstad = beginstad
		if(route[aantalLandingen-1].geefAfstandNaar(route[0].geefLoc()) <= 3300){
			route[aantalLandingen] = route[0];
		} else { //als de max duur van de laatste landing naar de beginstad wordt overschreden:
			while(true){
				int location = RANDOM.nextInt(City.CITIES.size());
				if(route[aantalLandingen-1].geefAfstandNaar(location) <= 3300 && route[0].geefAfstandNaar(location) <= 3300 && nietLeeg(geefLoc(aantalLandingen-1), location) && nietLeeg(location, geefLoc(0))){
					route[aantalLandingen] = new Landing(City.CITIES.get(location));
					updatePassagiers(geefLoc(aantalLandingen-1), geefLoc(aantalLandingen));
					aantalLandingen++;
					route[aantalLandingen] = route[0];
					break;
				}
			}
		}
		updatePassagiers(geefLoc(aantalLandingen-1), geefLoc(aantalLandingen));
		aantalLandingen++;	
		planTankbeurten();
		ingekort();
	}

	public void ingekort(){
		int random = 0;
		while(geefRouteDuur() > Dienstregeling.MINUTEN_PER_DAG){
			while(true){
				random = RANDOM.nextInt(aantalLandingen-2) + 1;
				if(City.AFSTAND[route[random-1].geefLoc()][route[random+1].geefLoc()] <= 3300){
					if(route[random].geefLoc() != 0){
						break;
					}
				}
			}
			for(int i = random; i < aantalLandingen; i++){
				route[i] = route[i + 1];
			}
			aantalLandingen--;
			resetTankbeurten();
			planTankbeurten();
		}
	}

	public boolean wordtIngekort(int mogelijkeExtraDuur) {
		return geefRouteDuur() + mogelijkeExtraDuur > Dienstregeling.MINUTEN_PER_DAG;
	}

	/*public void voegPassendeLandingToe() {
		int resterendeTijd = Dienstregeling.MINUTEN_PER_DAG - geefRouteDuur();
		int randomBeginpuntInt = RANDOM.nextInt(aantalLandingen-1);
		Landing randomBeginpunt = route[randomBeginpuntInt];
		Landing[] mogelijkeBestemmingen = geefMogelijkeBestemmingen(randomBeginpunt, resterendeTijd, true);

		if (aantalMogelijkeBestemmingen > 0) {
			Landing mogelijkeBestemming = mogelijkeBestemmingen[RANDOM.nextInt(aantalMogelijkeBestemmingen)];
			insertLanding(randomBeginpuntInt+1, mogelijkeBestemming);
		}

	}
	 */

	//geef een array van landingen met alle mogelijke bestemmingen vanaf een bepaald beginput. Gegeven een duur. Dit kan een minimale of een maximale duur zijn (boolean minimaleDuur)
	public Landing[] geefMogelijkeBestemmingen(Landing beginLocatie, int duur, boolean isMinimaleDuur) {
		Landing[] resultaat = new Landing[City.CITIES.size()];
		int beginLocatieIndex = beginLocatie.geefLoc();
		int aantalElementen = 0;

		for (int i = 0; i<City.CITIES.size(); i++) {
			//als startpunt niet gekozen punt is
			if (beginLocatieIndex != i) {
				if (isMinimaleDuur) {
					if (beginLocatie.geefVliegduurNaar(i, VLIEGTUIG_SNELHEID) <= duur){
						resultaat[aantalElementen] = new Landing(City.CITIES.get(i));
						aantalElementen++;
					}
				} else if (beginLocatie.geefVliegduurNaar(i, VLIEGTUIG_SNELHEID) >= duur) {
					resultaat[aantalElementen] = new Landing(City.CITIES.get(i));
					aantalElementen++;
				}
			}
		}
		aantalMogelijkeBestemmingen = aantalElementen;
		return resultaat;
		//zou kunnen dat dit een conflict veroorzaakt met de tankbeurten.
	}

	// WijzigLanding bestaat uit meerdere methoden die eigenschappen van een
	// landing veranderen
	// Bijvoorbeeld grondtijd, locatie of tanken
	public boolean wijzigLanding(int index, City nieuweLoc, int nieuweGrondtijd,
			boolean hierTanken) {
		// <= : een wijziging mag de eerstvolgende lege index zijn of een
		// bestaande index. Maar nooit groter dan de lengte van de route
		if (index <= aantalLandingen && index < route.length) {
			route[index] = new Landing(nieuweLoc, nieuweGrondtijd, hierTanken);
			return true;
		}

		System.out
		.println("WijzigLanding: meegegeven index " + index
				+ "is te groot. (" + aantalLandingen + "/"
				+ route.length + ")");
		return false;
	}

	public boolean wijzigLanding(int index, int[][] passagiers) {
		// <= : een wijziging mag de eerstvolgende lege index zijn of een
		// bestaande index. Maar nooit groter dan de lengte van de route
		this.passagiers = passagiers;
		City nieuweStad;
		//int index;
		while(nietLeeg(geefLoc(index-1), geefLoc(index + 1)) && nietLeeg(geefLoc(index),geefLoc(index + 1))){
			City.CITIES.get(RANDOM.nextInt(City.CITIES.size()));
		}
		if (index <= aantalLandingen && index < route.length) {
			//route[index].wijzigLocatie(nieuweStad);
			return true;
		}

		System.out
		.println("WijzigLanding: meegegeven index " + index
				+ "is te groot. (" + aantalLandingen + "/"
				+ route.length + ")");
		return false;
	}

	// Wijzigt de grondtijd van de landing op index
	public boolean wijzigGrondtijd(int index, int nieuweGrondtijd) {
		// <= : een wijziging mag de eerstvolgende lege index zijn of een
		// bestaande index. Maar nooit groter dan de lengte van de route
		if (index <= aantalLandingen && index < route.length) {
			route[index].wijzigGrondtijd(nieuweGrondtijd);
			return true;
		}

		System.out
		.println("WijzigLanding: meegegeven index " + index
				+ "is te groot. (" + aantalLandingen + "/"
				+ route.length + ")");
		return false;
	}

	// Voegt een landing toe op de eerstvolgende lege plek in de route
	public boolean voegLandingToe(City nieuweLoc, int nieuweGrondtijd,
			boolean hierTanken) {
		if (aantalLandingen < route.length) {
			route[aantalLandingen] = new Landing(nieuweLoc, nieuweGrondtijd,
					hierTanken);
			aantalLandingen++;
			return true;
		}

		System.out.println("VoegLandingToe: Array is al vol");
		return false;
	}

	// Voegt een random landing toe op de eerstvolgende lege plek in de route
	public boolean voegRandomLandingToe() {
		if (aantalLandingen < route.length) {
			//Deze while-loop voegt pas een landing toe als er meer dan 0 passagiers zijn vanaf de source naar destination.
			while(true){
				route[aantalLandingen] = new Landing();
				if(aantalLandingen == 0){
					break;
				}
				else if(nietLeeg(geefLoc(aantalLandingen-1), geefLoc(aantalLandingen)) && afStand()){
					updatePassagiers(geefLoc(aantalLandingen-1), geefLoc(aantalLandingen));
					break;
				}
			}
			aantalLandingen++;
			return true;
		}
		System.out.println("VoegLandingToe: Array is al vol");
		return false;
	}

	// Update de passagiers-lijst na een vlucht
	private void updatePassagiers(int source, int destination){
		if(passagiers[source][destination] >= 199){
			passagiers[source][destination] = passagiers[source][destination] - 199;
		} else {
			passagiers[source][destination] = passagiers[source][destination] = 0;
		}
	}

	//Boolean die kijkt of de afstand tussen twee locaties niet groter dan 3300 bedraagt
	private boolean afStand(){
		return route[aantalLandingen-1].geefAfstandNaar(route[aantalLandingen].geefLoc()) <= 3300;
	}

	// Geef locatie op index
	int geefLoc(int index){
		return route[index].geefLoc();
	}

	//Boolean die kijkt of het aantal passagiers van source naar destination groter dan 0 is.
	private boolean nietLeeg(int source, int destination){
		return passagiers[source][destination] >= 0;
	}

	//retourneert de passagierslijst
	public int[][] geefPassagiersLijst(){
		return passagiers;
	}

	// Verwijdert de landing op index
	public boolean verwijderLanding(int index) {
		if (index < route.length) {
			aantalLandingen--; // door dit hier te zetten krijgen we geen array
			// out of bounds in de komende for loop
			for (int i = index; i < aantalLandingen; i++) {
				route[i] = route[i + 1];
			}
			route[aantalLandingen] = null;

			if (index >= aantalLandingen) {
				System.out.println("VerwijderLanding: index was al leeg");
			}
			return true;
		}

		System.out.println("verwijderLanding: te grote index (" + index + "/"
				+ (route.length - 1) + ")");
		return false;
	}

	// Voegt een Landing toe op index
	public boolean insertLanding(int index, Landing landing) {
		if (index < route.length) {
			for (int i = aantalLandingen; i > index; i--) {
				route[i] = route[i - 1];
			}
			route[index] = landing;
			aantalLandingen++;
			return true;
		}
		System.out.println("insertLanding: te grote index (" + index + "/"
				+ (route.length - 1) + ")");
		return false;
	}

	// Maakt een array met vertrektijden van dit vliegtuig
	// Het geeft terug een array van opstijgingen met de info:
	// 1) tijdstip van vertrek 2) vertreklocatie 3)bestemming 4)vliegtuig
	// capaciteit
	public int[][] maakVertrekArray() {
		// Als het vliegtuig opstijgt
		if (aantalLandingen > 1) {
			int time = route[0].geefGrondtijd(); // niet totale grondtijd want
			// het is het eerste punt op
			// de route
			int[][] result = { { time, route[0].geefLoc(), route[1].geefLoc(),
				MAX_PASSAGIERS } };

			for (int i = 2; i < aantalLandingen; i++) {
				Landing a = route[i - 1];
				Landing b = route[i];
				double tmp = (double) a.geefAfstandNaar(b.geefLoc())
						/ VLIEGTUIG_SNELHEID * 60; // *60 om per min ipv uur te
				// krijgen
				time += (int) tmp + b.geefTotaleGrondtijd();
				int[][] array = { { time, a.geefLoc(), b.geefLoc(),
					MAX_PASSAGIERS } };
				result = joinMultiArrays(result, array);
			}

			return result;
		}
		// Het vliegtuig stijgt niet eens op
		return new int[][] { { 0, 0, 0, 0 } };
	}

	// Voegt twee multi arrays samen
	private int[][] joinMultiArrays(int[][] a, int[][] b) {
		int[][] result = new int[a.length + b.length][2]; // de [2] kan elke
		// waarde zijn,
		// tijdelijke
		// stakeholder die
		// overschreven
		// wordt
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i];
		}
		for (int i = a.length; i < result.length; i++) {
			result[i] = b[i - a.length];
		}

		return result;
	}

	// Cloont een vliegtuig object
	public Vliegtuig clone() {
		Vliegtuig result = new Vliegtuig(passagiers);
		for (int i = 0; i < this.aantalLandingen; i++) {
			result.route[i] = this.route[i].clone();
			result.aantalLandingen++;
		}

		return result;
	}
}
