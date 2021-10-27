package dev.wannaknow.raidplayernames;

import net.runelite.client.ui.PluginPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class RaidPlayerNamesPanel extends PluginPanel {

    RaidPlayerNamesPanel(RaidPlayerNamesPlugin plugin) {
        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.NORTH);
    }

    public void addRaid(String type, List<String> people) {
        if(people.size() > 1) {
            add(new RaidPlayerNamesBox(this, type, people));
            validate();
            repaint();
        }
    }

    public void deleteRaid(RaidPlayerNamesBox box) {
        remove(box);
        validate();
        repaint();
    }
}
