package Views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;

import AI.EngineerAI;
import Audio.AudioPlayer;
import ClientNetworking.GameClient.GameClient;
import GameLogic.GameOptions;
import GameLogic.Map;
import GameLogic.Ship;
import Graphics.Screen;
import Physics.Body;

/**
 * Created by James on 01/02/17.
 */
public class PilotView extends JPanel implements Observer {
    private Screen screen;
    private SpeedometerView speedometerView;
    private WeaponView plasmaBlasterView;
    private WeaponView laserBlasterView;
    private WeaponView torpedosView;

    private JButton manual;
    private ManualView instructions;

    private GameClient gameClient;
    private String playerNickname;

    private boolean UIinitialised = false;

    private EngineerAI engAI;

    private JLayeredPane UILayeredPane;
    private JPanel UIBaseLayer;
    private JFrame parentFrame;
    private JPanel UIpanel;

    private MouseMotionListener screenMouseListener;

    /**
     * Creates a new PilotView. This encapsulates the entire View of the Pilot player.
     * @param playerNickname The nickname of the player controlling this view.
     * @param gameClient     The GameClient handling network connections for this player.
     */
    public PilotView(String playerNickname, GameClient gameClient, JFrame parentFrame, boolean ai) {
        super();

        if (ai) {
            engAI = new EngineerAI(gameClient, playerNickname);
            gameClient.addObserver(engAI);
        }

        this.playerNickname = playerNickname;

        this.gameClient = gameClient;
        gameClient.addObserver(this);

        //addKeyListener(this);
        setFocusable(true);

        this.parentFrame = parentFrame;
        UIBaseLayer = new JPanel();

        parentFrame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                initialiseUI();
            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) { }

            @Override
            public void componentShown(ComponentEvent componentEvent) {
                initialiseUI();
            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {
                initialiseUI();
            }
        });

        parentFrame.addKeyListener(new KeyListener() {

        	@Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_1_BUTTON)) {
                    gameClient.send("fireWeapon1");
                } else if (keyEvent.getKeyCode() == GameOptions
                        .getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_2_BUTTON)) {
                    gameClient.send("fireWeapon2");
                } else if (keyEvent.getKeyCode() == GameOptions
                        .getCurrentKeyValueByDefault(GameOptions.DEFAULT_FIRE_WEAPON_3_BUTTON)) {
                    gameClient.send("fireWeapon3");
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ACCELERATE_BUTTON)) {
                    gameClient.send("accelerate");
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_DECELERATE_BUTTON)) {
                    gameClient.send("decelerate");
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_PITCH_DOWN_BUTTON)) {
                    gameClient.send("pitchDown");
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_PITCH_UP_BUTTON)) {
                    gameClient.send("pitchUp");
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_LEFT_BUTTON)) {
                    gameClient.send("rollLeft");
                } else if (keyEvent.getKeyCode() == GameOptions.getCurrentKeyValueByDefault(GameOptions.DEFAULT_ROLL_RIGHT_BUTTON)) {
                    gameClient.send("rollRight");
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        this.UILayeredPane = parentFrame.getLayeredPane();
        parentFrame.setFocusable(true);
        parentFrame.requestFocus();
        initialiseUI();

        // starting the in-game sounds
        AudioPlayer.stopMusic();
        AudioPlayer.playMusic(AudioPlayer.IN_GAME_TUNE);
    }

    /**
     * Creates all the elements of the UI and positions them on the screen. Sets all default values of the UI elements.
     */
    private void initialiseUI() {
        if(UIinitialised) {
            UILayeredPane.removeAll();
            UIBaseLayer.removeAll();
        }

        Ship s = findPlayerShip();
        while (s == null) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s = findPlayerShip();
        }

        initialiseWeapons(s);
        initialiseManualButton();
        initialiseSpeedometer();
        initialiseScreen();

        // Add mouse listener which swaps the cursor between being the default and a crosshair.
        this.removeMouseMotionListener(screenMouseListener);
        screenMouseListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                final int x = mouseEvent.getX();
                final int y = mouseEvent.getY();

                final Dimension screenDimension = screen.getSize();
                final Rectangle screenBounds = new Rectangle(0, 0, (int) screenDimension.getWidth(), (int) screenDimension.getHeight());
                System.out.println(screenBounds);
                if (screenBounds.contains(x, y)) {
                    getParent().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                } else {
                    getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        this.addMouseMotionListener(screenMouseListener);


        addAllComponents();

        this.repaint();
        this.revalidate();
        UIBaseLayer.repaint();
        UIBaseLayer.revalidate();
        UILayeredPane.repaint();
        UILayeredPane.revalidate();

        UIinitialised = true;
        System.out.println("Done initialising the UI. I am the Pilot");
    }

    /**
     * Add all of the UI components to the JPanel.
     */
    private void addAllComponents() {
        try {
            this.setLayout(new BorderLayout());
            Container weaponPanel = new Container();
            weaponPanel.add(plasmaBlasterView);
            weaponPanel.add(laserBlasterView);
            weaponPanel.add(torpedosView);
            weaponPanel.setLayout(new BoxLayout(weaponPanel, BoxLayout.Y_AXIS));

            System.out.println("Width: " + parentFrame.getWidth());
            System.out.println("Height: " + parentFrame.getHeight() / 5);
            UIpanel = new JPanel();
            UIpanel.setLayout(new BoxLayout(UIpanel, BoxLayout.X_AXIS));
            UIpanel.setPreferredSize(new Dimension(parentFrame.getWidth(), parentFrame.getHeight() / 5));
            UIpanel.add(manual);

            BufferedImage steeringWheelImage = ImageIO.read(new File(System.getProperty("user.dir") + "/res/img/steeringwheel.png"));
            JLabel steeringWheelView = new JLabel(new ImageIcon(steeringWheelImage));
            UIpanel.add(steeringWheelView);

            UIpanel.add(speedometerView);

            UIBaseLayer.setLayout(new BorderLayout());
            UIBaseLayer.add(screen, BorderLayout.CENTER);
            UIBaseLayer.add(UIpanel, BorderLayout.SOUTH);
            UIBaseLayer.setBounds(0, 0, parentFrame.getWidth(), parentFrame.getHeight());

            JLayeredPaneLayoutManager layoutManager = new JLayeredPaneLayoutManager();

            UILayeredPane.setLayout(layoutManager);
            UILayeredPane.add(UIBaseLayer, JLayeredPane.DEFAULT_LAYER);
        } catch (IOException e) {
            // should never get here.
            e.printStackTrace();
        }
    }

    /**
     * Initialise the Screen for the UI
     */
    private void initialiseScreen() {
        this.screen = new Screen(playerNickname, true);
        screen.setPreferredSize(new Dimension(this.getWidth(), 4 * (this.getHeight() / 5)));
    }

    /**
     * Initialises the speedometer to its initial values.
     */
    private void initialiseSpeedometer() {
        speedometerView = new SpeedometerView();
    }

    private void initialiseManualButton() {
        this.manual = new JButton("Manual");
        this.manual.addActionListener(e -> showManual());
        this.manual.setFocusable(false);
    }

    private void showManual() {
    	if(this.instructions == null){
    		initialiseManualView(getHeight() - 100);
    		this.instructions.setBounds(50,50, getWidth() - 100, getHeight() - 100);
    		UILayeredPane.add(instructions, JLayeredPane.PALETTE_LAYER);
    	} else {
    		this.instructions.setVisible(!instructions.isVisible());
    	}
    }

    private void initialiseManualView(int height) {
    	this.instructions = new ManualView(gameClient.keySequence.getAllKeys(), gameClient.keySequence.getKeysSize(), height);
        this.instructions.setVisible(false);
    }


    /**
     * Given a Ship, this will initialise the weapon progress bars to their initial values and set their maximum values
     *
     * @param s This players Ship object
     */
    private void initialiseWeapons(Ship s) {
        if (s != null) {
            plasmaBlasterView = new WeaponView("Plasma Blaster", false);
            laserBlasterView = new WeaponView("Laser Blaster", false);
            torpedosView = new WeaponView("Torpedos", false);
        } else {
            System.out.println("Ship is null? Oh dear oh dear");
        }
    }

    /**
     * Finds the players Ship within all of the objects in the Map.
     * @return The players Ship object. If the ship could not be found, this will return null.
     */
    private Ship findPlayerShip() {
        Map m = gameClient.getMap();

        for (Body b : m) {
            if (b instanceof Ship) {
                Ship s = (Ship) b;

                if (s.getPilotName().equals(playerNickname)) {
                    return s;
                }
            }
        }

        return null;
    }

    
    @Override
    public void update(Observable observable, Object o) {
        if (!UIinitialised) {
            try {
                initialiseUI();
                UIinitialised = true;
            } catch (Exception e) {
                // Oh well.
                System.out.println("Update Error: " + e.getMessage());
                System.out.println("Localised message: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        } else {
            Map m = gameClient.getMap();
            screen.setMap(m);
        }
    }
}
