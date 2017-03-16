package Views;

import GameLogic.GameOptions;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by James on 08/02/17.
 * View to show information about one single weapon (name, ammo bar, highlighted).
 * By default the Weapon is unhighlighted and shows no ammo bar.
 */
public class WeaponView extends JPanel{
    private JButton replenishAmmo;
    private Color HIGHLIGHT_COLOUR = Color.red;
    private Color DEFAULT_COLOUR = Color.black;

    private static final Color PROGRESS_BAR_COLOR = new Color(0, 255, 72);

    private JLabel weaponNameLabel;
    private JProgressBar weaponAmmoLevel;

    private boolean showAmmoLevel = false;

    /**
     * Creates a WeaponView with the given label name. Ammo bar does not exist.
     * @param weaponName The name of the weapon
     */
    public WeaponView(String weaponName) {
        // weaponNameLabel = new JLabel(weaponName);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setOpaque(false);
        // this.add(weaponNameLabel);
    }

    /**
     * Creates a WeaponView with the given label name and shows the ammo bar if appropriate.
     * @param weaponName The name of the weapon.
     * @param showAmmoLevel Whether to create and show the ammo bar or not.
     */
    public WeaponView(String weaponName, boolean showAmmoLevel, String replenishNumber, ArrayList<JButton> buttons) {
        setOpaque(false);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.showAmmoLevel = showAmmoLevel;
        if(showAmmoLevel) {
            //this.remove(weaponNameLabel);
            //weaponNameLabel = null;

            weaponAmmoLevel = new JProgressBar();
            weaponAmmoLevel.setString(weaponName);
            weaponAmmoLevel.setStringPainted(true);
            weaponAmmoLevel.setUI(new BasicProgressBarUI());
            weaponAmmoLevel.setIndeterminate(false);
            weaponAmmoLevel.setMaximum(10);
            weaponAmmoLevel.setMinimum(0);
            weaponAmmoLevel.setFont(GameOptions.REGULAR_TEXT_FONT);
            weaponAmmoLevel.setForeground(PROGRESS_BAR_COLOR);
            this.add(weaponAmmoLevel);


            this.replenishAmmo = new JButton("Replenish: " + replenishNumber);
            replenishAmmo.setFont(GameOptions.REGULAR_TEXT_FONT);
            replenishAmmo.setEnabled(false);
            replenishAmmo.setFocusable(false);
            replenishAmmo.setName(weaponName);
            replenishAmmo.setOpaque(true);
            replenishAmmo.setBackground(Color.decode("#cccccc"));
            replenishAmmo.setBorderPainted(false);
            buttons.add(replenishAmmo);

            this.add(replenishAmmo);
        }
    }

    /**
     * Updates the value of the ammo bar to the given value
     * @param ammoLevel The new ammo level
     */
    public void updateWeaponAmmoLevel(int ammoLevel) {
        if(showAmmoLevel) {
            weaponAmmoLevel.setValue(ammoLevel);
        }
    }

    /**
     * Set whether the Weapon is highlighted or not
     * @param highlighted New value for whether the weapon is highlighted or not
     */
    public void setHighlightWeapon(boolean highlighted) {
        if(highlighted) {
            weaponNameLabel.setForeground(HIGHLIGHT_COLOUR);
        } else {
            weaponNameLabel.setForeground(DEFAULT_COLOUR);
        }
    }

    /**
     * Set the maximum amount of ammo this weapon can have
     * @param maxiumumAmmo The maximum amount of ammo
     */
    public void setMaxiumumAmmo(int maxiumumAmmo) {
        if(showAmmoLevel) {
            weaponAmmoLevel.setMaximum(maxiumumAmmo);
        }
    }

    public void setReplenishAmmo(EngineerView parent, ShipState state) {
        replenishAmmo.addActionListener(actionEvent -> {
            if(parent.getState() != state) {
                parent.setState(state);
            } else {
                parent.setState(ShipState.NONE);
            }
        });
        replenishAmmo.setEnabled(true);
    }

    public void setReplenishAmmoNumber(String number) {
        replenishAmmo.setText("Replenish: " + number);
    }
}