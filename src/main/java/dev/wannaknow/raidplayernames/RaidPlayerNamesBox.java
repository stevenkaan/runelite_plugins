package dev.wannaknow.raidplayernames;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class RaidPlayerNamesBox extends JPanel {

    private RaidPlayerNamesPanel panel;

    private final JLabel deleteLabel = new JLabel();


    private static final ImageIcon DELETE_ICON;
    private static final ImageIcon DELETE_HOVER_ICON;

    static {
        final BufferedImage deleteImg = ImageUtil.getResourceStreamFromClass(RaidPlayerNamesPlugin.class, "delete.png");
        DELETE_ICON = new ImageIcon(deleteImg);
        DELETE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(deleteImg, -100));
    }

    RaidPlayerNamesBox(RaidPlayerNamesPanel panel, String type, List<String> players) {

        this.panel = panel;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 10, 0));


        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));
        actions.setBorder(new EmptyBorder(7, 7, 7, 7));
        actions.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());

        deleteLabel.setIcon(DELETE_ICON);
        deleteLabel.setToolTipText("Delete this raid");
        deleteLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent mouseEvent)
            {
                int confirm = JOptionPane.showConfirmDialog(RaidPlayerNamesBox.this,
                        "Are you sure you want to permanently delete this raid?",
                        "Warning", JOptionPane.OK_CANCEL_OPTION);

                if (confirm == 0)
                {
                    deleteRaid();
                }
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent)
            {
                deleteLabel.setIcon(DELETE_HOVER_ICON);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent)
            {
                deleteLabel.setIcon(DELETE_ICON);
            }
        });
        actions.add(new JLabel(type + " (" + players.size() + " players)"), BorderLayout.WEST);
        actions.add(Box.createHorizontalGlue());
        actions.add(Box.createRigidArea(new Dimension(5, 0)));
        actions.add(deleteLabel, BorderLayout.EAST);

        add(actions, BorderLayout.NORTH);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout());
        topContainer.setBorder(new EmptyBorder(7, 7, 7, 7));
        topContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JTextArea textArea = new JTextArea();
        textArea.setText(String.join("\n", players));
        topContainer.add(textArea, BorderLayout.NORTH);
        add(topContainer);
    }

    public void deleteRaid() {
        panel.deleteRaid(this);
    }
}
