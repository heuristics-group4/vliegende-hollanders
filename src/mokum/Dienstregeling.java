package mokum;

//v1.3: No changes
//v1.2: Maak verterkarray: 
//	old	for(int i=1; i<dienstRegeling.length -1; i++) {
// new	for(int i=1; i<dienstRegeling.length; i++) {
//v1.1: No Changes
/*	Het Dienstregelingobject staat voor de representatie van het probleem.
	Het bevat algemene informatie van het probleem en het beheert de vloot van
	Mokum Airlines
*/
public class Dienstregeling implements Comparable<Dienstregeling>{
	
	//Het geschatte aantal passagiers dat van rij Y naar kolom X wil vliegen (gaat op index)
	private static int[][] PASSENGERS = {
		{0,213,119,278,89,302,388,153,341,273,112,361,302,324,269,206,147,400,367,172,45,321,100,135,86,95,257,371},
		{373,0,377,341,202,161,354,182,424,69,96,52,141,5,224,425,277,88,380,290,444,89,0,28,376,296,323,7},
		{403,165,0,327,231,403,113,287,218,264,443,166,436,322,37,206,252,291,414,271,223,287,408,251,127,299,3,58},
		{143,238,15,0,110,380,387,205,280,65,208,56,289,82,221,249,273,363,77,272,365,444,175,363,35,428,206,61},
		{320,360,375,345,0,433,246,239,82,20,205,350,271,41,335,327,31,307,17,262,424,349,273,369,253,448,238,263},
		{86,1,281,370,75,0,354,255,22,31,105,342,183,73,120,91,256,89,70,236,108,242,274,421,331,166,329,249},
		{49,264,371,277,292,3,0,330,363,143,197,184,209,50,414,164,421,394,262,390,214,363,28,187,337,187,96,34},
		{110,413,36,366,31,174,75,0,102,138,101,269,283,205,166,38,38,332,118,352,59,338,14,91,332,20,418,163},
		{46,313,371,24,87,414,44,196,0,49,78,351,112,432,290,121,369,5,187,97,55,428,41,264,441,191,37,245},
		{119,262,389,175,147,431,194,368,319,0,62,107,85,120,129,96,342,302,425,245,187,419,189,133,315,424,289,92},
		{261,20,14,81,390,154,41,450,356,165,0,75,291,41,55,138,58,88,350,295,84,150,334,120,101,405,280,65},
		{244,362,175,118,38,201,332,168,417,324,162,0,363,137,4,170,232,326,397,302,3,5,4,359,347,369,413,308},
		{251,248,431,243,318,398,407,290,378,45,70,16,0,140,94,100,215,293,265,202,168,374,51,293,120,435,368,324},
		{91,424,232,148,310,410,120,393,406,266,110,434,450,0,193,288,40,128,442,144,63,95,9,341,110,220,320,138},
		{235,136,341,383,255,414,181,228,223,140,76,304,326,293,0,438,23,93,123,378,449,362,203,194,386,123,365,373},
		{191,359,76,10,39,293,116,129,4,314,428,273,388,342,321,0,18,239,402,164,441,47,429,423,9,276,334,323},
		{179,430,128,330,307,405,87,202,91,325,91,209,42,309,446,434,0,280,415,106,333,161,309,403,383,430,117,246},
		{5,360,144,212,46,48,409,375,267,326,51,306,95,16,365,59,8,0,351,37,219,397,242,245,245,407,30,375},
		{25,61,232,425,309,252,38,69,205,77,310,350,328,393,177,106,21,102,0,219,56,126,318,265,120,247,263,426},
		{79,103,351,40,133,150,116,0,206,187,369,157,295,177,115,152,331,366,154,0,4,122,402,342,379,361,382,325},
		{206,25,450,341,234,100,345,290,179,81,388,285,415,222,83,280,227,352,181,440,0,81,378,331,100,113,304,109},
		{316,4,398,268,419,161,73,36,96,385,373,434,123,295,16,172,109,360,196,356,263,0,382,314,322,352,120,417},
		{157,62,337,246,314,271,250,180,387,386,379,41,255,49,139,332,283,313,337,7,121,97,0,275,357,348,78,413},
		{107,403,353,383,54,407,142,66,360,408,377,80,430,193,131,341,216,176,224,77,251,43,381,0,16,122,175,237},
		{285,335,166,287,215,108,275,131,362,267,250,18,421,302,280,57,368,100,391,390,134,391,77,409,0,157,244,100},
		{373,433,407,186,98,377,122,110,247,398,299,236,12,15,78,177,307,260,135,27,272,288,127,153,415,0,257,324},
		{312,322,57,159,135,450,15,12,92,47,41,440,315,39,193,124,224,68,439,28,290,287,366,153,427,115,0,314},
		{441,256,62,423,215,432,412,128,361,128,138,360,87,181,113,389,200,141,300,281,337,9,180,203,379,290,165,0},
	};
	
	
	
	
	
	private static final	int 			MINUTEN_PER_DAG		= 1200; 	//Het aantal minuten waartussen gevlogen kan worden (20*60)
	private static final	int			VLOOTGROOTTE			= 1;		//Het aantal vliegtuigen in de vloot van Mokum Airlines
	private					Vliegtuig[]	dienstRegeling;					//De verzameling vliegtuigen van Mokum Airlines
	
	//constructors
	public Dienstregeling() {
		dienstRegeling = new Vliegtuig[VLOOTGROOTTE];
	}
	
	public Dienstregeling(boolean x) {
		dienstRegeling = new Vliegtuig[VLOOTGROOTTE];
		maakRandomDienstregeling();
	}
	
	/* METHODEN */
	
	//Geeft het vliegtuig op index
	public Vliegtuig geefVliegtuig(int index) {
		if(index > VLOOTGROOTTE -1) {
			System.out.println("geefVliegtuig: index " + index + "is groter dan vlootgrootte " + VLOOTGROOTTE);
			return null;
		}
		return dienstRegeling[index];
	}
		
	//Geeft de grootte van de vloot
	public int geefGrootte() {
		return VLOOTGROOTTE;
	}
	
	//Maakt een volledig random dienstregeling
	public void maakRandomDienstregeling() {
		for(int i=0; i<VLOOTGROOTTE;i++) {
			dienstRegeling[i] = new Vliegtuig();
			dienstRegeling[i].maakRandomRoute(MINUTEN_PER_DAG);
		}
	}
	
	//Telt het totaal aantal kilometers dat passagiers hebben afgelegd
	public int telPassagiersKilometers() {
		int		passagiersKM		= 0;
		int[][] 	vertrekArray 		= maakVertrekArray();
		int[][] 	passagiersMatrix	= new int[PASSENGERS.length][PASSENGERS.length];
		multiArrayCopy(PASSENGERS, passagiersMatrix);
		
		for(int[] vertrek : vertrekArray) {
			Landing l		 = new Landing(); //om de afstanden op te vragen
			int vertrekpunt = vertrek[1];
			int bestemming  = vertrek[2];
			int capaciteit  = vertrek[3];
			
			//Neem de maximale capaciteit mee, of anders het aantal overgebleven passagiers dat wilt vliegen
			int passagiersMee = Math.min(capaciteit, passagiersMatrix[vertrekpunt][bestemming]);
			passagiersKM		+= l.geefAfstandTussen(vertrekpunt,bestemming) * passagiersMee;
			passagiersMatrix[vertrekpunt][bestemming] -= passagiersMee;
		}
		return passagiersKM;
	}
	
	//Maakt een vertrekArray met de volgende info:
	//1)Vertrektijd 2)Vertrekpunt 3)Bestemming 4)vliegtuig capaciteit
	//De array die teruggegeven wordt is gesorteert op vertrektijd van vroeg naar laat
	private int[][] maakVertrekArray() {
		int[][] teSorteren = dienstRegeling[0].maakVertrekArray();
		for(int i=1; i<dienstRegeling.length; i++) {
			int[][] nieuw = dienstRegeling[i].maakVertrekArray();
			teSorteren = joinMultiArrays(teSorteren, nieuw);
		}
		
		sort2dArray(teSorteren);
		return teSorteren;
	}
	
	//Voegt twee multi arrays samen
	private int[][] joinMultiArrays(int[][] a, int[][] b) {
		int[][] result = new int[a.length + b.length][2]; //de [2] kan elke waarde zijn, tijdelijke stakeholder die overschreven wordt
		for(int i=0; i<a.length; i++) {
			result[i] = a[i];
		}
		for(int i=a.length; i<result.length; i++) {
			result[i] = b[i - a.length];
		}
		
		return result;
	}
	
	//Sorteert een twee dimensionale array
	private void sort2dArray(int[][] toSort)
	{
		quicksort(toSort, 0, toSort.length -1);
	}
	
	//Sorteert een tweedimensionale array
	private void quicksort(int[][] a, int left, int right) {
		if (right <= left) return;
		int i = partition(a, left, right);
		quicksort(a, left, i-1);
		quicksort(a, i+1, right);
	}

   // Helpt een multidimensionale array efficient te sorteren 
	// partition a[left] to a[right], assumes left < right
    private int partition(int[][] a, int left, int right) {
        int i = left - 1;
        int j = right;
        while (true) {
            while (a[++i][0] < a[right][0])     
                ;                               
            while (a[right][0] < a[--j][0])     
                if (j == left) break;           
            if (i >= j) break;                  
            exch(a, i, j);                      
        }
        exch(a, i, right);                      
        return i;
    }

    // Helpt een multidimensionale array efficient te sorteren
    private void exch(int[][] a, int i, int j) {
        int[] swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
	
    //Kloont een multi array van source naar destination
    private void multiArrayCopy(int[][] source,int[][] destination) {
		for (int i=0;i<source.length;i++)
		{
		System.arraycopy(source[i],0,destination[i],0,source[i].length);
		}
	}
    
    // Vergelijkt de dienstregeling met een andere dienstregeling object aan de hand van pasagierskilometers
    public int compareTo(Dienstregeling other_dienstregeling){
    	Integer kilometers2 = telPassagiersKilometers();
    	Integer kilometers1 = other_dienstregeling.telPassagiersKilometers();
    	return kilometers1.compareTo(kilometers2);
    }
}
