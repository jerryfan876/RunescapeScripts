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
        info = "For sand Crab Island",
        logo = "",
        author = "J.eerie",
        name = "SandCrabIsland 0.0")

public class SandCrabIsland extends Script {

    public static final int BANK_BOOTH_ID = 6943;
    public static final Area BANK_AREA = new Area(3092, 3245, 3095, 3241);

    public static final Area FIGHT_AREA1 = new Area();

    public static final Area FIGHT_AREA2 = new Area();

    public static final Area FIGHT_AREA3 = new Area();

    public static final Area FIGHT_AREA4 = new Area();

    public static final Area FIGHT_AREA5 = new Area();

    public static final Area RESET_AREA = new Area();

    public Area currentFightArea;


    private String STRINGFOOD = "Herring";


    private String POTIONA = "Strength potion";
    private String POTIONB = "Energy potion";
    private String[] LISTOFPOTIONS = new String[]{POTIONA, POTIONB};


    Item[] bankItems;
    // items of the bank


    private String missingPot = null;
    //potion that is missing

    private RS2Object bankBooth;

    //TODO: add a safespot, also need to find one for real crabs
    Position safespot = new Position(3113, 3205, 0);


    // State Enumerator
    public enum State {
        FIGHT, WALK_TO_RESET_BACK, BANKING, FIND_AREA
    }

    public State getState() throws InterruptedException {
        //we gunna fight until they fear us
        if (isPlayersAround() && !myPlayer().isUnderAttack() && areThereFood(STRINGFOOD) && areTherePotions(POTIONA) && areTherePotions(POTIONB)) { //TODO: NEED TO ADD && FIGHTAREA CONTAINS MY PLAYER, do we need a list of potions? or is 2 enough for now?
            return State.FIND_AREA;
            //ambiguous if i am already underattack and someone comes, do i leave?
        }
        if (myPlayer().isUnderAttack() && areThereFood(STRINGFOOD) && areTherePotions(POTIONA) && areTherePotions(POTIONB)) { //TODO: NEED TO ADD && FIGHTAREA CONTAINS MY PLAYER, do we need a list of potions? or is 2 enough for now?
            return State.FIGHT;
        }
        if (!areThereFood(STRINGFOOD) || !areTherePotions(POTIONA) || !areTherePotions(POTIONB)) {
            return State.BANKING;
        } else {
            return State.WALK_TO_RESET_BACK;
        }
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
            case FIND_AREA:

                walking.webWalk(FIGHT_AREA1);
                if (!isPlayersAround()) {
                    break;//go fight here
                } else {
                    walking.webWalk(FIGHT_AREA2);
                }

                if (!isPlayersAround()) {
                    break;
                } else {
                    walking.webWalk(FIGHT_AREA3);
                }

                if (!isPlayersAround()) {
                    break;
                } else {
                    walking.webWalk(FIGHT_AREA4);
                }

                if (!isPlayersAround()) {
                    break;
                } else {
                    walking.webWalk(FIGHT_AREA5);
                }

                if (!isPlayersAround()) {
                    break;
                } else {
                    getWorlds().hopToF2PWorld();
                }


                break;

            case FIGHT:
                log("Debug: is in STATE.FIGHT.");
                if (this.skills.getDynamic(Skill.HITPOINTS) <= this.skills.getStatic(Skill.HITPOINTS) + 1) { //todo: does this needa be fixed? skill.strength???
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

                log("Debug: walking to the reset position.");
                walking.webWalk(RESET_AREA.getRandomPosition());
                sleep(random(500, 1000));
                antiban();

                log("Debug: walking back to the fight position.");
                walking.webWalk(currentFightArea);
                return random(5000, 10000);


            case BANKING:
                log("Debug: is in STATE.BANKING");
                walking.webWalk(BANK_AREA.getRandomPosition()); // LAMBDA EXPRESSIONS ARE V IMPORTANT, for choosing nearst with filter<>
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

                bank.depositAll();

                sleep(random(500, 1000));
                if (inventory.isEmpty()) {
                    if (bank.contains(STRINGFOOD) && getBank().getAmount(STRINGFOOD) >= 5) {
                        log("Debug: " + STRINGFOOD + " found in bank and enough");
                        getBank().withdraw((STRINGFOOD), Bank.WITHDRAW_5); //but now we needa check that there are enough space in the inventory to withdraw
                        log("Debug: withdrew missing STRINGFOOD");
                    } else {
                        log("Debug: " + STRINGFOOD + " not found in bank or not enough");
                        bot.getScriptExecutor().stop();
                    }


                    sleep(random(100, 1500));


                    for (String potionName : LISTOFPOTIONS) {
                        if (bank.contains(potionName + "(4)") && getBank().getAmount(potionName + "(4)") >= 5) {
                            log("Debug: " + potionName + " found in bank and enough");
                            //get the missing item and put it in my bank
                            sleep(random(500, 1000));
                            getBank().withdraw((potionName + "(4)"), Bank.WITHDRAW_5); //but now we needa check that there are enough space in the inventory to withdraw
                            log("Debug: withdrew: " + potionName);

                        } else {
                            log("Debug:" + potionName + " not found in bank or not enough");
                            bot.getScriptExecutor().stop();
                            //end the script since we got no more on the item and we cant buy shit yet
                        }
                    }

                    if (bank.isOpen()) {
                        bank.close();
                    }

                } else {
                    bank.depositAll();
                }

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

    public boolean isPlayersAround() {
        Area myPlayerArea = myPlayer().getArea(2);
        Players players = getPlayers();

        for (Player p : players.getAll()){
            if (!p.equals(myPlayer()) && myPlayerArea.contains(p.getPosition())){
                return true;
            }
        }
        return false;
    }


    public void onExit() throws InterruptedException {
        super.onExit();
        log("Debug: script has ended. --Jerry");
    }

}

