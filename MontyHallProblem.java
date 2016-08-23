import java.security.SecureRandom;
import java.util.Arrays;
/**
 * https://en.wikipedia.org/wiki/Monty_Hall_problem
 *
 */
public class MontyHallProblem {
    
    private static final double ROUNDING_FACTOR = 100.0;
    private static int NO_DOORS = 3;
    private static final boolean GOAT = false;
    private static final boolean CAR = true;
    private static boolean[] doors = new boolean[NO_DOORS];
    private static int[] carDoorsCnt = new int[NO_DOORS];
    private static int[] firstDoorsPickCnt = new int[NO_DOORS];
    private static int[] secondDoorsPickCnt = new int[NO_DOORS];
    private static int[] goatDoorsRevealCnt = new int[NO_DOORS];
    private static SecureRandom random = new SecureRandom();
    private static int NO_TEST = 1000000;
    
    public static void main(String[] args){
	int firstChoiceWins = 0;
	int secondChoiceWins = 0;

	for(int i = 0; i < NO_TEST; i++){
	    hideTheCar();
	    int firstChoice = pickADoor();
	    int goatIndex = revealAGoat(firstChoice);
	    int secondChoice = pickADoor(firstChoice, goatIndex);
	    assert firstChoice != goatIndex && firstChoice != secondChoice && secondChoice != goatIndex ;
	    if(doors[firstChoice] == CAR){
		firstChoiceWins++;
	    }else if(doors[secondChoice] == CAR){
		secondChoiceWins++;
	    }
	    //System.out.println("round " + i + " firstChoice:" + firstChoice + " goat:" + goatIndex + " secondChoice:" + secondChoice + " car:" + findCar() );
	    carDoorsCnt[findCar()]++;
	    firstDoorsPickCnt[firstChoice]++;
	    secondDoorsPickCnt[secondChoice]++;
	    goatDoorsRevealCnt[goatIndex]++;
	}
	assert NO_TEST == firstChoiceWins + secondChoiceWins;
	System.out.println("Out of " + NO_TEST + " rounds the first pick won " + firstChoiceWins + " times, the second pick won " + secondChoiceWins + " times.");
	
	double firstChoiceWinProb = (double)firstChoiceWins / NO_TEST;
	double secondChoiceWinProb = (double)secondChoiceWins / NO_TEST;
	firstChoiceWinProb = Math.round(firstChoiceWinProb * ROUNDING_FACTOR) /ROUNDING_FACTOR;
	secondChoiceWinProb = Math.round(secondChoiceWinProb * ROUNDING_FACTOR) /ROUNDING_FACTOR;

	System.out.println("Probability of winning with the first choice: " + firstChoiceWinProb + " with the second choice:" + secondChoiceWinProb );
	System.out.println("Per door probability for the car");
	printDoorPickCounters(carDoorsCnt);
	System.out.println("Per door probability for the first pick");
	printDoorPickCounters(firstDoorsPickCnt);
	System.out.println("Per door probability for the second pick");
	printDoorPickCounters(secondDoorsPickCnt);
	System.out.println("Per door probability for the goat reveal");
	printDoorPickCounters(goatDoorsRevealCnt);
    }

    private static void printDoorPickCounters(int[] doors) {
	StringBuilder sb = new StringBuilder();
	for(int i = 0; i < doors.length; i++){
	    sb.append(" ").append(i).append(":");
	    double prob = (double)doors[i] / NO_TEST;
	    prob = Math.round(prob * ROUNDING_FACTOR) /ROUNDING_FACTOR;
	    sb.append(prob);
	}
	System.out.println(sb.toString());
    }

    private static int findCar() {
	for(int i = 0; i < NO_DOORS ; i++ ){
	    if(doors[i] == CAR){
		return i;
	    }
	}
	return 0;
    }

    private static int pickADoor(int...dontPickThese) {
	Arrays.sort(dontPickThese);
	int toPickIndex = random.nextInt(NO_DOORS - dontPickThese.length);
	int doorToPick = 0;
	for(int i = 0; i < NO_DOORS ; i++ ){
	    if(Arrays.binarySearch(dontPickThese, i) >= 0 ){//the door number not is among dontPickThese
		continue;
	    }
	    if(doorToPick == toPickIndex){
		return i;
	    }
	    doorToPick++;
	}
	return 0;
    }

    private static int revealAGoat(int dontRevealThisDoor) {
	int revealIndex;
	if(doors[dontRevealThisDoor] == CAR){
	    revealIndex = random.nextInt(NO_DOORS - 1);//dont reveal the chosen door
	}else if(NO_DOORS > 3){
	    revealIndex = random.nextInt(NO_DOORS - 2);//dont reveal the chosen door or the car
	}else{
	    revealIndex = 0;
	}
	int doorToReveal = 0;
	for(int i = 0; i < NO_DOORS ; i++ ){
	    if(i == dontRevealThisDoor || doors[i] == CAR){
		continue;
	    }
	    if(doorToReveal == revealIndex){
		return i;
	    }
	    doorToReveal++;
	}
	return 0;
    }

    private static int pickADoor() {
	return random.nextInt(NO_DOORS);
    }

    private static void hideTheCar() {
	int carIndex = random.nextInt(NO_DOORS);
	for(int i = 0 ; i < NO_DOORS ; i++){
	    if(i == carIndex){
		doors[i] = CAR;
	    }else{
		doors[i] = GOAT;
	    }
	}
	
    }

}
