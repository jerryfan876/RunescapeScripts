/**
 * Created by jerry on 5/16/2017.
 */
package Starter;

import org.osbot.rs07.api.Camera;
import org.osbot.rs07.api.Players;
import org.osbot.rs07.api.filter.ContainsNameFilter;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.filter.NameFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.*;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by jerry on 4/1/2017.
 */
@ScriptManifest(version = 0.1,
        info = "only for crabs, has potion but basic, only (kahbob). testing in f2p world",
        logo = "",
        author = "Jerry",
        name = "crabSlap"
)
public class Starter extends Script {




    public static final int BANK_BOOTH_ID = 6943;
    public static final Area BANK_AREA = new Area(3092 , 3245, 3095, 3241);


    private String POTIONA = "Strength potion";
    private String POTIONB = "Energy potion";

    private String[] listOfPotions = {POTIONA, POTIONB};

    private String stringFood = "Herring";
    private String[] allFood = new String[]{"Herring", "Shark", "Monkfish", "Trout", "Pike"};
    private ArrayList<String> listAllFood= new ArrayList<String>();

    private Item itemFoodMore;

    private String missingPot = null;
    private String almostPot = null;

    RS2Object bankBooth;

    /*
    Position fight1 = new Position(1732,3469,0);
    Position fight2 = new Position(1733,3469,0);
    Position fight3 = new Position(1734,3469,0);
    Position fight4 = new Position(1732,3470,0);
    Position fight5 = new Position(1733,3470,0);
    Position fight6 = new Position(1734,3470,0);

*/
    //above is the corrdinate for real carb

    //under is the f2p version
    Position fight1 = new Position(3107, 3215, 0);
    Position fight2 = new Position(3107, 3214, 0);
    Position fight3 = new Position(3107, 3213, 0);
    Position fight4 = new Position(3107, 3212, 0);
    Position fight5 = new Position(3107, 3211, 0);
    Position fight6 = new Position(3107, 3210, 0);

    //TODO:make the position randomly take a tile in an area for reset spot
    //position x is an range
    //position y is an range
    //able to pick from different positions with 4 crabs together and test for people are each of these area
    // Area fightArea = new Area(position1, position2)
    //then getRandomPosition
    // dont need pickRandomFightPosition and pickRandomResetPosition


    Position reset1 = new Position(3121, 3213, 0);
    Position reset2 = new Position(3121, 3211, 0);
    Position reset3 = new Position(3121, 3210, 0);
    Position reset4 = new Position(3121, 3210, 0);
    Position reset5 = new Position(3121, 3211, 0);
    Position reset6 = new Position(3121, 3214, 0);

    //TODO: add a safespot, also need to find one for real crabs
    Position safespot = new Position(3113, 3205, 0);



    // State Enumerator
    public enum State {
        FIGHT, WALK_TO_RESET_BACK, BANKING_FOOD, BANKING_POTIONS
    }

    public State getState() throws InterruptedException {
        //we gunna fight until they fear us
        if (myPlayer().isUnderAttack() && areThereFood(allFood) && areTherePotions(POTIONA) && areTherePotions(POTIONB)) { //TODO: NEED TO ADD && FIGHTAREA CONTAINS MY PLAYER, do we need a list of potions? or is 2 enough for now?
            return State.FIGHT;
        }
        if (!areThereFood(allFood)) {
            return State.BANKING_FOOD;
        }
        if (!areTherePotions(POTIONA) || !areTherePotions(POTIONB)){
            return State.BANKING_POTIONS;
        } else {
            return State.WALK_TO_RESET_BACK;
        }
    }

    public Position pickRandomFightPosition() {
        int numb = random(1, 6);
        if (numb == 1)
            return fight1;

        else if (numb == 2)
            return fight2;

        else if (numb == 3)
            return fight3;

        else if (numb == 4)
            return fight4;

        else if (numb == 5)
            return fight5;

        else return fight6;
    }


    public Position pickRandomResetPosition() {
        int numb = random(1, 6);
        if (numb == 1)
            return reset1;

        else if (numb == 2)
            return reset2;

        else if (numb == 3)
            return reset3;

        else if (numb == 4)
            return reset4;

        else if (numb == 5)
            return reset5;

        else return reset6;
    }


    //antiban may not be effective at all since it maybe overly simply
    //TODO: need to complex and randomize antiban actions, and when antiban should occur should also be randomized
    //just need this to be more random and with little rythum
    public void antiban() throws InterruptedException {
        switch (random(1, 20)) {
            case 1:
                getCamera().moveYaw(120 + (random(5, 63)));
                getTabs().open(Tab.FRIENDS);
                mouse.moveRandomly();
                sleep(random(2000, 2500));
                getTabs().open(Tab.INVENTORY);
                break;
            case 2:
                getCamera().movePitch(50 + random(1, 70));
                mouse.moveSlightly();
                break;
            case 3:
                sleep(random(800, 2500));
                mouse.moveSlightly();
                break;
            case 4:
                getTabs().open(Tab.SKILLS);
                sleep(MethodProvider.gRandom(400, 900));
                getSkills().hoverSkill(Skill.STRENGTH);
                sleep(random(1500, 4000));
                mouse.move(random(0, 259), random(0, 250));
                getTabs().open(Tab.INVENTORY);
                mouse.moveRandomly();
                break;
            case 5:
                getTabs().open(Tab.PRAYER);
                sleep(random(1000, 10000));
                getMouse().moveOutsideScreen();
                getTabs().open(Tab.INVENTORY);
            case 6:
            case 7:
                getMouse().moveOutsideScreen();
                sleep(random(1000, 10000));
                break;
            case 8:
            case 9:
            case 10:
                mouse.move(random(100, 253), random(0, 250));
            case 11:
                cameraAdjustUp();
            case 12:
                cameraToRandomObj();
            case 13:
            case 14:
        }
        sleep(random(1800, 2500));
    }



    public void onStart() throws InterruptedException {
        super.onStart();
        log("Debug: is starting.");
        //TODO: if possible, make potion more elegant

    }

    public int onLoop() throws InterruptedException {
        Item itemFood = inventory.getItem(stringFood); //this is for when there is only 1 food

        for (String food : allFood){                    //this is for when there is a list of food to support more food
            if (inventory.contains(food)) {             //this is the same as getting food from the bank, after food does contain, it is still gunna keep looking in the list because of the for
                itemFoodMore = inventory.getItem(food);
                log("Debug: we found: " + food);
            }
        }

        if (getInventory().contains("Vial")){
            Item vial = getInventory().getItem("Vial");
            vial.interact("Drop");
        }


        switch (getState()) {
            case FIGHT:
                log("Debug: is in STATE.FIGHT.");

                /*
                if (inventory.getInventory().contains(stringFood)) {
                    log("Debug: have enough food. Just that one food: " + stringFood);
                    */

                if (100 * this.skills.getDynamic(Skill.HITPOINTS) / this.skills.getStatic(Skill.HITPOINTS) <= 50) {
                    for (String food : allFood) {                    //this is for when there is a list of food to support more food
                        if (inventory.contains(food)) {             //this is the same as getting food from the bank, after food does contain, it is still gunna keep looking in the list because of the for
                            itemFoodMore = inventory.getItem(food);
                            log("Debug: we found: " + food);
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            itemFoodMore.interact("Eat");
                            log("Debug: just ate a " + food);
                        }
                    }

                    //bot.getScriptExecutor().stop();
                } else { //the white space is a little off, i forgot how to fix the indentation
                    walking.webWalk(safespot);
                    log("Debug: went to safespot because out of " + stringFood);
                    antiban();
                    bot.getScriptExecutor().stop();
                }

                /*
                if (inventory.getInventory().contains(POTIONA + "(4)") ||
                        inventory.getInventory().contains(POTIONA + "(3)") ||
                        inventory.getInventory().contains(POTIONA + "(2)") ||
                        inventory.getInventory().contains(POTIONA + "(1)")  ) { */
                if (areTherePotions(POTIONA)) {
                    log("Debug: still have super strength.");
                    antiban();
                    if (100 * this.skills.getDynamic(Skill.STRENGTH) / this.skills.getStatic(Skill.STRENGTH) <= 200) {
                        if (getInventory().contains(POTIONA + "(1)")) {
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            inventory.getItem(POTIONA + "(1)").interact("Drink");
                            log("Debug: just drank a Super Strength(1)");
                        } else if (getInventory().contains(POTIONA + "(2)")) {
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            inventory.getItem(POTIONA + "(2)").interact("Drink");
                            log("Debug: just drank a Super Strength(2)");
                        } else if (getInventory().contains(POTIONA + "(3)")) {
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            inventory.getItem(POTIONA + "(3)").interact("Drink");
                            log("Debug: just drank a Super Strength(3)");
                        } else {
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            inventory.getItem(POTIONA + "(4)").interact("Drink");
                            log("Debug: just drank a Super Strength(4)");
                        }
                    }
                } else {
                    walking.webWalk(safespot);
                    log("Debug: went to safespot because out of Super Strength");
                    missingPot = POTIONA;
                    almostPot = POTIONB;

                }

                //TODO: figure out if the pots need the () at the end for dose


                if (areTherePotions(POTIONB)) {
                    antiban();
                    if (100 * this.skills.getDynamic(Skill.ATTACK) / this.skills.getStatic(Skill.ATTACK) <= 200) { //if percentage HP is less than 20 - 40
                        if (getInventory().contains(POTIONB + "(1)")) {
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            inventory.getItem(POTIONB + "(1)").interact("Drink");
                            log("Debug: just drank a Super Attack(1)");
                        } else if (getInventory().contains(POTIONB + "(2)")) {
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            inventory.getItem(POTIONB + "(2)").interact("Drink");
                            log("Debug: just drank a Super Attack(2)");
                        } else if (getInventory().contains(POTIONB + "(3)")) {
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            inventory.getItem(POTIONB + "(3)").interact("Drink");
                            log("Debug: just drank a Super Attack(3)");
                        } else {
                            sleep(random(200, 500));
                            mouse.moveSlightly();
                            inventory.getItem(POTIONB + "(4)").interact("Drink");
                            log("Debug: just drank a Super Attack(4)");
                        }
                    }
                } else {
                    walking.webWalk(safespot);
                    log("Debug: went to safespot because out of Super Strength");
                    missingPot = POTIONB;
                    almostPot = POTIONA;
                }

                break;

            case WALK_TO_RESET_BACK:
                log("Debug: is in STATE.WALKING_TO_RESET_BACK.");
                //TODO: make sure to move camera angle as u walk, move camera to entity
                antiban();
                if (Script.random(1, 2) == 2) {
                    this.mouse.moveSlightly();
                }
                walking.webWalk(pickRandomResetPosition());
                log("Debug: walking to the reset position.");
                sleep(random(5000, 10000));
                antiban();
                walking.webWalk(pickRandomFightPosition());
                log("Debug: walking back to the fight position.");
                break;

            case BANKING_FOOD:
                log("Debug: is in STATE.BANKING_FOOD");
                Boolean stopLoop = false;
                walking.webWalk(BANK_AREA); // LAMBDA EXPRESSIONS ARE V IMPORTANT, for choosing nearst with filter<>
                bankBooth = getObjects().closest(BANK_BOOTH_ID);

                if (bankBooth.isVisible()){
                    bankBooth.interact("Bank");
                    log("Debug: we just opened the bank for food");


                    for (int i = 0; i < listAllFood.size() || stopLoop = true ; i++) { // we are going the run the food string as an arraylist
                        //idk if i should use : for, for each, while
                        //I need this to cycle like: when found a food from the list in the bank, take it out, then this state and go to a different state. OR when there are no more foodString in the list
                        String theFood = listAllFood.get(i);
                        if (getBank().contains(theFood) && getBank().getAmount(theFood) >= 10) {
                            stop = true;
                            getBank().getItem(theFood).interact("Withdraw-10");

                        } else {
                            log("Debug: bank does not have: " + theFood);
                            //end the script since we got no more on the item and we cant but shit yet
                        }
                    }
                    stopLoop = false;
                }
                /*
                Iterator var3 = this.objects.get(5, 8).iterator();
                var3.hasNext();

                Iterator var16 = this.npcs.getAll().iterator();
                var16.hasNext();
                */

                break;


            case BANKING_POTIONS:
                //TODO:after banking, variables missingPot and almostPot need to be set to null
                log("Debug: is in STATE.BANKING_POTIONS");
                walking.webWalk(BANK_AREA);  // LAMBDA EXPRESSIONS ARE V IMPORTANT
                bankBooth = getObjects().closest(BANK_BOOTH_ID);
                if (bankBooth.isVisible()){
                    bankBooth.interact("Bank");
                    log("Debug: we just opened the bank.");
                    if (getBank().contains(missingPot) && getBank().contains(almostPot)) {
                        if (getBank().getAmount(missingPot) >= 5 && getBank().getAmount(almostPot) >= 5) {
                            log("Debug: we have enough potions");
                            //get the missing item and put it in my bank
                            getBank().getItem(missingPot + "(4)").interact("Withdraw-5"); //but now we needa check that there are enough space in the inventory to withdraw
                            missingPot = null;
                            log("Debug: withdrew missing potion")
                            for (int i = 1; i <= 4; i++) {
                                while (getInventory().contains(almostPot + "(" + i + ")")) {
                                    getInventory().getItem(almostPot + "(" + i + ")").interact("Deposite");
                                }
                            }
                            log("Debug: deposited all almost-potion")
                            getBank().getItem(almostPot + "(4)").interact("Withdraw-5"); //but now we needa check that there are enough space in the inventory to withdraw
                            log("Debug: withdrew 5 almost-pot")
                            almostPot = null;

                        } else {
                            log("Debug: do not have enough pots in bank");
                            bot.getScriptExecutor().stop();
                        }
                    } else {
                        log("Debug: do not have enough pots in bank");
                        bot.getScriptExecutor().stop();
                        //end the script since we got no more on the item and we cant buy shit yet
                    }
                } //jsut for now that it will end since we dont have interaction in the bank
                //in the bank, if there is no more pot or food, it must stay in the bank and scrpt ends. using "if" statment
                break;


        }


        return random(500, 1000);
    }

    public void cameraAdjustUp() throws InterruptedException {
        if (this.camera.getPitchAngle() < 45) {
            this.camera.movePitch(Script.random(40, 67));
        }
    }

    public void cameraToRandomObj() throws InterruptedException {
        if (random(1, 50) == 1) {
            List objects = this.objects.getAll();
            int random = Script.random(1, objects.size() / 3 + 1);
            RS2Object o = (RS2Object) objects.get(random);
            if (o != null) {
                this.camera.toEntity(o);
                this.log("Random camera move");
            }
        }
    }


    public void hoverNPCRandomly(NPC target) throws InterruptedException {
        if (random(1, 14) == 1) {
            NPC hover = target;

            while (hover != null && hover.isOnScreen() && !this.getMouse().isOnCursor(hover)) {
                hover.hover();
                sleep(random(300, 600));
            }
        }
    }

    public boolean areTherePotions (String potion) {
        for (int i = 1; i <= 4; i++) {
            if (inventory.getInventory().contains(potion + "(" + i + ")")) {
                log("Debug: areTherePotions: there are enough Potions.");

                return true;
            }
        }
        log("Debug: areTherePotion: there are enough potions.");
        return false;
    }

    public boolean areThereFood (String[] allFood) {
        for (String food : allFood) {                    //this is for when there is a list of food to support more food
            if (getInventory().contains(food)){
                log("Debug: areThereFood: there are enough food.");
                return true;
            }
        }
        log("Debug: areThereFood: there arent enough food")
        return false;
    }


    public void onExit() throws InterruptedException {
        super.onExit();
        log("Debug: script has ended. --Jerry");
    }

}
