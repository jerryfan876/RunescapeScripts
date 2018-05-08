package flamo.MonkKiller.src.main;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.util.LocalPathFinder;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.api.model.Character;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.map.Area;

public class StateHandler {

    private final Area EDGEVILLE_MONASTERY = new Area(3042, 3481, 3061, 3500);
    private final int MAXIMUM_NPC_DISTANCE = 6;
    private final int MINIMUM_HEALTH = 8;

    private LocalPathFinder pathFinder;
    private MethodProvider api;
    private String status;

    public StateHandler(MethodProvider methodProvider) {
        api = methodProvider;
        pathFinder = new LocalPathFinder(api.getBot());
    }

    private enum State {
        WALKING,
        ATTACKING,
        HEALING,
        IDLE
    }

    private State getState() {
        Character opponent = api.myPlayer().getInteracting();
        if (EDGEVILLE_MONASTERY.contains(api.myPlayer()) && api.getCombat().isFighting() && opponent != null && opponent.getHealthPercent() > 0) {
            return State.IDLE;
        } else if (EDGEVILLE_MONASTERY.contains(api.myPlayer()) && !api.getCombat().isFighting() && api.getSkills().getDynamic(Skill.HITPOINTS) <= MINIMUM_HEALTH) {
            return State.HEALING;
        } else if (EDGEVILLE_MONASTERY.contains(api.myPlayer())) {
            return State.ATTACKING;
        }
        return State.WALKING;
    }

    public String getStatus() {
        return status;
    }

    public void handleNextState() throws InterruptedException {
        State state = getState();
        status = state.name().charAt(0) + state.name().substring(1).toLowerCase();
        switch (state) {
            case WALKING:
                api.getWalking().webWalk(EDGEVILLE_MONASTERY);
                break;
            case ATTACKING:
                NPC attackMonk = api.getNpcs().closest((Filter<NPC>) npc -> npc.getName().equals("Monk") && npc.getInteracting() == null && npc.getHealthPercent() > 0);
                interactNPC(attackMonk, "Attack", new ConditionalSleep(6000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return api.getCombat().isFighting() || api.getNpcs().closest((Filter<NPC>) nbpc -> npc.getName().equals("Monk") && npc.getHealthPercent() > 0).getInteracting() != null;
                    }
                });
                break;
            case HEALING:
                if (api.getDialogues().inDialogue()) {
                    api.getDialogues().completeDialogue("Can you heal me? I'm injured.");
                } else {
                    NPC healMonk = api.getNpcs().closest((Filter<NPC>) npc -> (npc.getName().equals("Monk") || npc.getName().equals("Abbot Langley")) && npc.getHealthPercent() > 0);
                    interactNPC(healMonk, "Talk-to", new ConditionalSleep(5000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return api.getDialogues().inDialogue();
                        }
                    });
                }
                break;
            case IDLE:
                api.sleep(25);
                break;
        }
    }

    private void interactNPC(NPC npc, String action, ConditionalSleep conditionalSleep) {
        if (npc != null) {
            if (api.myPlayer().getPosition().distance(npc) <= MAXIMUM_NPC_DISTANCE) {
                if (!npc.isOnScreen()) {
                    api.getCamera().toEntity(npc);
                }
                if (npc.interact(action)) {
                    conditionalSleep.sleep();
                }
            } else {
                api.getWalking().walk(npc);
            }
        }
    }
}
