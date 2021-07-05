package dev.wannaknow.raidplayernames;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.PluginPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class RaidPlayerNamesPanel extends PluginPanel {

    private List<RaidPlayerNamesBox> boxes = new ArrayList<>();

    RaidPlayerNamesPanel(RaidPlayerNamesPlugin plugin) {
        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.NORTH);
    }

    public void addRaid(List<String> people) {
        if(people.size() > 1) {
            add(new RaidPlayerNamesBox(this, people));
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
