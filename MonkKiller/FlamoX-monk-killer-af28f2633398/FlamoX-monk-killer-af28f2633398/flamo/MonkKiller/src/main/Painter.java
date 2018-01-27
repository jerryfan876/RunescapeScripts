package flamo.MonkKiller.src.main;

import flamo.MonkKiller.src.util.Format;
import flamo.MonkKiller.src.util.Timer;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

import java.awt.*;

public class Painter {

    private final Font FONT = new Font("Lucida Sans Unicode", Font.PLAIN, 11);
    private final Timer TIMER = new Timer();
    private final int BASE_X = 4;
    private final int BASE_Y = 248;
    private final int SPACING = 15;

    private Skill[] skillsToTrack;
    private MethodProvider api;
    private double version;
    private String status;
    private String name;
    private int lines;

    public Painter(MethodProvider methodProvider, String name, double version, Skill[] skillsToTrack) {
        this.skillsToTrack = skillsToTrack;
        this.name = name;
        this.version = version;
        api = methodProvider;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void paintTo(Graphics2D g) {
        RenderingHints antialiasing = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        g.setRenderingHints(antialiasing);
        lines = 0;
        g.setFont(FONT);
        g.setColor(Color.WHITE);
        g.drawString("[" + name + " v" + version + "]", BASE_X, nextY());
        g.drawString("Runtime: " + TIMER.toString(), BASE_X, nextY());
        g.drawString("Status: " + status, BASE_X, nextY());
        g.drawString("--------------EXPERIENCE TRACKING--------------", BASE_X, nextY());
        for (Skill skill : skillsToTrack) {
            if (api.getExperienceTracker().getGainedXP(skill) > 0) {
                g.drawString(
                        skill.name().charAt(0) + skill.name().substring(1).toLowerCase() + ": " +
                                api.getSkills().getStatic(skill) +
                                " (" + api.getExperienceTracker().getGainedLevels(skill) + ")" +
                                " - Exp: " + api.getExperienceTracker().getGainedXP(skill) +
                                " (" + api.getExperienceTracker().getGainedXPPerHour(skill) + ")" +
                                " - TTL: " + Format.msToString(api.getExperienceTracker().getTimeToLevel(skill)),
                         BASE_X, nextY()
                );
            }
        }
    }

    private int nextY() {
        return BASE_Y + SPACING * (lines++ - 1);
    }

}
