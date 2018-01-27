package flamo.MonkKiller.src.main;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;

@ScriptManifest(
        logo = "Combat",
        name = "Flamo's Monk Killer",
        info = "Kills monks at the Edgeville monastery.",
        author = "Flamo",
        version = 0.02
)

public class MonkKillerScript extends Script {

    private final Skill[] skillsToTrack = new Skill[]{Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.RANGED, Skill.MAGIC, Skill.HITPOINTS};
    private StateHandler stateHandler = new StateHandler(this);
    private Painter painter = new Painter(this, getName(), getVersion(), skillsToTrack);

    @Override
    public void onStart() {
        for (Skill skill : skillsToTrack) {
            getExperienceTracker().start(skill);
        }
    }

    @Override
    public int onLoop() throws InterruptedException {
        stateHandler.handleNextState();
        return 1;
    }

    public void onPaint(Graphics2D g) {
        painter.setStatus(stateHandler.getStatus());
        painter.paintTo(g);
    }

}
