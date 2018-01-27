import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.Players;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.*;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import static com.sun.deploy.config.JREInfo.getAll;

/**
 * Created by jerry on 5/30/2017.
 */

@ScriptManifest(author = "Jer",
        info = "hitting hill giants and bank and pick up items",
        logo = "",
        name = "Hill Giants (lumb goblin) 0.3",
        version = 1.0)
public class HillGiants extends Script {

    public static final int BANK_BOOTH_ID = 6943;
    public static final Area FIGHT_AREA = new Area(3135, 3237, 3155, 3223);
    public static final Area BANK_AREA = new Area(3092, 3245, 3095, 3241);
    public static final Position RESET_AGRO = new Position(3151, 3234, 0);

    private String STRINGFOOD = "Herring";

    private String POTIONA = "Strength potion";
    private String POTIONB = "Energy potion";
    private String[] LISTOFPOTIONS = new String[]{POTIONA, POTIONB};

    RS2Object bankBooth;

    Item[] bankItems;
    // items of the bank

    private String[] LOOTLIST = new String[]{"Body rune", "Water rune", "Coins"};
    private GroundItem groundItem;

    private String needToKill = "Goblin";

    private NPC target;


    // State Enumerator
    public enum State {
        FIGHTING, FIND_TARGET, BANKING, WALKING_TO_FIGHT, IDLE
    }

    public State getState() {

        if (myPlayer().isUnderAttack() && areTherePotions(POTIONA) && areTherePotions(POTIONB) && areThereFood(STRINGFOOD) && !inventory.isFull()) {
            return State.FIGHTING;
            // lines that would run while in combat
        }

        if (FIGHT_AREA.contains(myPlayer().getPosition()) && !myPlayer().isUnderAttack() && areTherePotions(POTIONA) && areTherePotions(POTIONB) && areThereFood(STRINGFOOD) && !inventory.isFull()) {
            return State.FIND_TARGET;
            // lines that would run while finding a target
        }

        if (!areTherePotions(POTIONA) || !areTherePotions(POTIONB) || !areThereFood(STRINGFOOD) || inventory.isFull()) {
            return State.BANKING;
            //lines that would run when going to the bank
        }

        if (!FIGHT_AREA.contains(myPlayer().getPosition())) {
            return State.WALKING_TO_FIGHT;
            // lines that would run when going back to the fight
            // after banking
            // when ran outside of the FIGHT_AREA during a fight

        } else return State.IDLE;
        // lines that would run during weird cases


    }


    public void onStart() throws InterruptedException {
        super.onStart();
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
            case WALKING_TO_FIGHT:
                log("Debug: is in STATE.WALKING_TO_FIGHT");

                walking.webWalk(FIGHT_AREA.getRandomPosition());

                break;
            case FIND_TARGET:
                log("Debug: is in STATE.FIND_TARGET.");
                //below is for testing

                Players players = getPlayers();
                for (Player p : players.getAll()){
                    log("we found player: " + p.getName());
                }


                GroundItem closestGroundItem = (GroundItem) groundItems.closest(LOOTLIST);

                int myX = myPlayer().getPosition().getX();
                int myY = myPlayer().getPosition().getY();

                Area lootArea = new Area(myX - 2, myY + 2, myX + 2, myY - 2);


                if (!myPlayer().isUnderAttack()) {
                    sleep(random(1500, 2000)); //was (2600, 3600)

                    //where loot begins

                    for (GroundItem g : groundItems.getAll()){
                        int gX = g.getPosition().getX();
                        int gY = g.getPosition().getY();
                        if (lootArea.contains(gX, gY)){
                            log("grounditem near: " + g.getName());
                            for (String ll : LOOTLIST){
                                if (ll.equals(g.getName())){
                                    g.interact("Take");
                                    log("Debug: Picked up: " + g.getName());
                                    sleepWhileMoving();
                                    sleep(random(500,1000));
                                }
                            }
                        } else {
                            //dont pick up the loot
                        }
                    }


                    if (target == null) {
                        log("target is null");
                        target = npcs.closest(needToKill);
                        log("target is: " + target);
                        if (target.isOnScreen()) {
                            log("target is chosen");
                            if (!target.isUnderAttack()) {
                                target.interact("Attack");
                                sleepWhileMoving();
                            } else {
                                log("Debug: Target is underattack, running to new position for another target");
                                target = null;
                                walking.webWalk(FIGHT_AREA.getRandomPosition());
                            }
                        } else {
                            walking.webWalk(target.getPosition());
                        }
                    } else {
                        if (target.exists()) {
                            if (!target.isUnderAttack()) {
                                log("target is NOT null, trying to interact(attack)");
                                walking.webWalk(target.getPosition());
                                target.interact("Attack");
                                sleepWhileMoving();
                            } else {
                                log("Debug: Target is underattack, running to new position for another target");
                                target = null;
                                walking.webWalk(FIGHT_AREA.getRandomPosition());
                            }
                        }
                        else {
                            target = npcs.closest(needToKill);
                            log("change the dead npc to a new one");
                        }
                    }
                }


                break;


            case FIGHTING:
                log("Debug: is in STATE.FIGHTING.");
                target = null;
                //reset the target so that when FIND_TARGET runs again, it will be a new target

                //check to see the the npc that we are fighting is the target that we want
                if (myPlayer().isUnderAttack()) {
                    log("we are fighting: " + combat.getFighting().getName());
                    if (combat.getFighting().getName().equals(needToKill)) {

                        if (100 * this.skills.getDynamic(Skill.HITPOINTS) / this.skills.getStatic(Skill.HITPOINTS) <= 50) {
                            log("HP is too low");
                            if (inventory.contains(STRINGFOOD)) {
                                getInventory().getItem(STRINGFOOD).interact("Eat");
                                log("Debug: just ate a " + STRINGFOOD);

                            } else {
                                return random(100, 500);
                            }
                        } else {
                            log("Debug: have enough HP");
                        }

                        //repsonsible for picking up loots
/*
                    groundItem = (GroundItem) groundItems.closest(LOOTLIST);
                    if (groundItem != null) {
                        if (groundItem.isOnScreen()) {
                            String groundItemName = groundItem.getName();
                            log(groundItemName + "is found");
                            groundItem.interact("Take");
                            sleepWhileMoving();
                        }
                    }
                    */


                        if (areTherePotions(POTIONA)) {
                            log("Debug: still have super strength.");
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
                            return random(100, 500);
                        }

                        if (areTherePotions(POTIONB)) {
                            if (this.skills.getDynamic(Skill.STRENGTH) <= this.skills.getStatic(Skill.STRENGTH) + 1) {
                                if (getInventory().contains(POTIONB + "(1)")) {
                                    sleep(random(500, 1000));
                                    inventory.getItem(POTIONB + "(1)").interact("Drink");
                                    log("Debug: just drank a Energy(1)");

                                } else if (getInventory().contains(POTIONB + "(2)")) {
                                    sleep(random(500, 1000));
                                    inventory.getItem(POTIONB + "(2)").interact("Drink");
                                    log("Debug: just drank a Energy(2)");

                                } else if (getInventory().contains(POTIONB + "(3)")) {
                                    sleep(random(500, 1000));
                                    inventory.getItem(POTIONB + "(3)").interact("Drink");
                                    log("Debug: just drank a Energy(3)");

                                } else {
                                    sleep(random(500, 1000));
                                    inventory.getItem(POTIONB + "(4)").interact("Drink");
                                    log("Debug: just drank a Energy(4)");
                                }
                            }

                        } else {
                            return random(100, 500);
                        }
                    } else {
                        //when the target is not the npc we should be killing
                        log("is being under attack by npc not chosen");
                        walking.webWalk(RESET_AGRO);
                        //resets the npc combat
                    }
                }


                break;


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


            case IDLE:
                log("Debug: is in STATE.IDLE");
                walking.webWalk(BANK_AREA.getRandomPosition());
                bot.getScriptExecutor().stop();

                break;
        }

        return random(500, 1000);
    }


    public boolean areTherePotions(String potion) {
        for (int i = 1; i <= 4; i++) {
            if (inventory.getInventory().contains(potion + "(" + i + ")")) {
                log("Debug: areTherePotions: there are enough: " + potion);

                return true;
            }
        }
        log("Debug: areTherePotions: there are NOT enough: " + potion);
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

    public void sleepWhileMoving() throws InterruptedException {
        do {
            sleep(random(1000, 1500));
        }
        while (myPlayer().isMoving());
        {
            sleep(random(500, 1000));
        }
    }

    public void checkForLowHP() {

    }

    public void onExit() throws InterruptedException {
        super.onExit();
        log("Debug: script has ended. --Jerry");
    }

}
