/**
 * Created by jerry on 5/17/2017.
 */

import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.Objects;
import org.osbot.rs07.script.Script;


/**
 * Created by jerry on 5/17/2017.
 */

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

@ScriptManifest(version = 0.0,
        info = "only for crabs, has potion but basic, only (herring). testing in f2p world",
        logo = "",
        author = "Jerry",
        name = "clabSlap 0.023")

public class ClabSlap extends Script {

    public static final int BANK_BOOTH_ID = 6943;
    public static final Area BANK_AREA = new Area(3092, 3245, 3095, 3241);


    private String STRINGFOOD = "Herring";


    private String POTIONA = "Strength potion";
    private String POTIONB = "Energy potion";

    private String[] listOfPotions = {POTIONA, POTIONB};


    Item[] bankItems;
    // items of the bank


    private String missingPot = null;
    //potion that is missing
    private String almostPot = null;
    //potion that is running out

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
        if (myPlayer().isUnderAttack() && areThereFood(STRINGFOOD) && areTherePotions(POTIONA) && areTherePotions(POTIONB)) { //TODO: NEED TO ADD && FIGHTAREA CONTAINS MY PLAYER, do we need a list of potions? or is 2 enough for now?
            return State.FIGHT;
        }
        if (!areThereFood(STRINGFOOD)) {
            return State.BANKING_FOOD;
        }
        if (!areTherePotions(POTIONA) || !areTherePotions(POTIONB)) {
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
        /*
        switch (random(1, 20)) {
            case 1:
                getCamera().moveYaw(120 + (random(5, 63)));
                getTabs().open(Tab.FRIENDS);

                sleep(random(2000, 2500));
                getTabs().open(Tab.INVENTORY);
                break;
            case 2:
                getCamera().movePitch(50 + random(1, 70));

                break;
            case 3:
                sleep(random(800, 2500));

                break;
            case 4:
                getTabs().open(Tab.SKILLS);
                sleep(MethodProvider.random(400, 900));
                getSkills().hoverSkill(Skill.STRENGTH);
                sleep(random(1500, 4000));
                mouse.move(random(0, 259), random(0, 250));
                getTabs().open(Tab.INVENTORY);

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
        */
    }


    public void onStart() throws InterruptedException {
        super.onStart();
        log("Debug: is starting...");

    }

    public int onLoop() throws InterruptedException {

        if (getInventory().contains("Vial")) {
            Item vial = getInventory().getItem("Vial");
            vial.interact("Drop");
            log("Debug: checked and dropped Vial");
        } else {
            log("Debug: checked Vial");
        }


        switch (getState()) {
            case FIGHT:
                log("Debug: is in STATE.FIGHT.");
                if (this.skills.getDynamic(Skill.STRENGTH) <= this.skills.getStatic(Skill.STRENGTH) + 1) {
                    log("HP is too low");
                    if (inventory.contains(STRINGFOOD)) {
                        getInventory().getItem(STRINGFOOD).interact("Eat");
                        log("Debug: just ate a " + STRINGFOOD);

                    } else { //the white space is a little off, i forgot how to fix the indentation
                        log("Debug: went to BANK_AREA because out of food");
                        walking.webWalk(BANK_AREA);
                        antiban();
                        bot.getScriptExecutor().stop();

                        //bot.getScriptExecutor().stop();
                    }
                } else {
                    log("Debug: have enough HP");
                }


                if (areTherePotions(POTIONA)) {
                    log("Debug: still have super strength.");
                    antiban();
                    if (this.skills.getDynamic(Skill.STRENGTH) <= this.skills.getStatic(Skill.STRENGTH) + 1) {
                        if (getInventory().contains(POTIONA + "(1)")) {
                            sleep(random(200, 500));
                            inventory.getItem(POTIONA + "(1)").interact("Drink");
                            log("Debug: just drank a Super Strength(1)");

                        } else if (getInventory().contains(POTIONA + "(2)")) {
                            sleep(random(200, 500));
                            inventory.getItem(POTIONA + "(2)").interact("Drink");
                            log("Debug: just drank a Super Strength(2)");

                        } else if (getInventory().contains(POTIONA + "(3)")) {
                            sleep(random(200, 500));
                            inventory.getItem(POTIONA + "(3)").interact("Drink");
                            log("Debug: just drank a Super Strength(3)");

                        } else {
                            sleep(random(200, 500));
                            inventory.getItem(POTIONA + "(4)").interact("Drink");
                            log("Debug: just drank a Super Strength(4)");
                        }
                    }

                } else {
                    walking.webWalk(safespot);
                    log("Debug: went to safespot because out of Super Strength");
                    missingPot = POTIONA;
                }

                //TODO: figure out if the pots need the () at the end for dose


                if (areTherePotions(POTIONB)) {
                    antiban();
                    if (100 * this.skills.getDynamic(Skill.STRENGTH) / this.skills.getStatic(Skill.STRENGTH) <= 200) {
                        if (getInventory().contains(POTIONB + "(1)")) {
                            sleep(random(200, 500));
                            inventory.getItem(POTIONB + "(1)").interact("Drink");
                            log("Debug: just drank a Energy(1)");

                        } else if (getInventory().contains(POTIONB + "(2)")) {
                            sleep(random(200, 500));
                            inventory.getItem(POTIONB + "(2)").interact("Drink");
                            log("Debug: just drank a Energy(2)");

                        } else if (getInventory().contains(POTIONB + "(3)")) {
                            sleep(random(200, 500));
                            inventory.getItem(POTIONB + "(3)").interact("Drink");
                            log("Debug: just drank a Energy(3)");

                        } else {
                            sleep(random(200, 500));
                            inventory.getItem(POTIONB + "(4)").interact("Drink");
                            log("Debug: just drank a Energy(4)");
                        }
                    }

                } else {
                    walking.webWalk(safespot);
                    log("Debug: went to safespot because out of Energy");
                    missingPot = POTIONB;
                }

                break;

            case WALK_TO_RESET_BACK:
                log("Debug: is in STATE.WALKING_TO_RESET_BACK.");
                //TODO: make sure to move camera angle as u walk, move camera to entity
                antiban();

                walking.webWalk(pickRandomResetPosition());
                log("Debug: walking to the reset position.");
                sleep(random(500, 1000));
                antiban();
                walking.webWalk(pickRandomFightPosition());
                log("Debug: walking back to the fight position.");
                return random(5000, 10000);

            case BANKING_FOOD:
                log("Debug: is in STATE.BANKING_FOOD");
                walking.webWalk(BANK_AREA); // LAMBDA EXPRESSIONS ARE V IMPORTANT, for choosing nearst with filter<>
                bankBooth = getObjects().closest(BANK_BOOTH_ID);

                if (!bank.isOpen()) {
                    bankBooth.interact("Bank");
                }

                sleepWhileMoving();

                bankItems = bank.getItems();
                if (bankItems != null) {
                    log("bank is NOT null");
                } else {
                    log("bankItems IS null");
                }


                if (bank.contains(STRINGFOOD)) {
                    log("Debug: " + STRINGFOOD + " found in bank");
                    if (getBank().getAmount(STRINGFOOD) >= 5) {
                        log("Debug: we have enough amount of " + STRINGFOOD);
                        //get the missing item and put it in my bank
                        getBank().withdraw((STRINGFOOD), Bank.WITHDRAW_10); //but now we needa check that there are enough space in the inventory to withdraw
                        log("Debug: withdrew missing STRINGFOOD");

                    } else {
                        log("Debug: not enough amount of " + STRINGFOOD + " in bank");
                        bot.getScriptExecutor().stop();
                    }
                } else {
                    log("Debug: " + STRINGFOOD + "not found in bank");
                    bot.getScriptExecutor().stop();
                }

                if(bank.isOpen()){
                    bank.close();
                }

                break;

            case BANKING_POTIONS:
                log("Debug: is in STATE.BANKING_POTIONS");

                walking.webWalk(BANK_AREA);  // LAMBDA EXPRESSIONS ARE V IMPORTANT
                bankBooth = getObjects().closest(BANK_BOOTH_ID);
                log("Debug: we just opened the bank.");

                if (!bank.isOpen()) {
                    bankBooth.interact("Bank");
                }

                sleepWhileMoving();

                bankItems = bank.getItems();
                if (bankItems != null) {
                    log("bank is NOT null");
                } else {
                    log("bankItems IS null");
                }

                sleep(random(500, 1000));
                if (bank.contains(missingPot + "(4)")) {
                    log("Debug: missingpot found in bank");
                    if (getBank().getAmount(missingPot + "(4)") >= 5) {
                        log("Debug: we have enough amount of potions");
                        //get the missing item and put it in my bank
                        sleep(random(500, 1000));
                        getBank().withdraw((missingPot + "(4)"), Bank.WITHDRAW_5); //but now we needa check that there are enough space in the inventory to withdraw
                        missingPot = null;
                        log("Debug: withdrew missing potion");

                    } else {
                        log("Debug: not enough amount of pots in bank");
                        bot.getScriptExecutor().stop();
                    }
                } else {
                    log("Debug: pots not found in bank");
                    bot.getScriptExecutor().stop();
                    //end the script since we got no more on the item and we cant buy shit yet
                }
                if(bank.isOpen()){
                    bank.close();
                }


                //jsut for now that it will end since we dont have interaction in the bank
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

    public void sleepWhileMoving() throws InterruptedException {
        do {
            sleep(random(400, 900));
        }
        while (myPlayer().isMoving());
        {
            sleep(random(400, 900));
        }
    }

    public boolean areTherePotions(String potion) {
        for (int i = 1; i <= 4; i++) {
            if (inventory.getInventory().contains(potion + "(" + i + ")")) {
                log("Debug: areTherePotions: there are enough: " + potion);

                return true;
            }
        }
        log("Debug: areTherePotions: there are NOT enough: " + potion);
        missingPot = potion;
        return false;
    }

    public boolean areThereFood(String food) {
        if (getInventory().contains(food)) {
            log("Debug: areThereFood: there are enough food.");
            return true;
        }
        log("Debug: areThereFood: there are NOT enough food");
        return false;
    }


    public void onExit() throws InterruptedException {
        super.onExit();
        log("Debug: script has ended. --Jerry");
    }

}

