package Menus;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GameLogic.KeyBindings;

public class ControlsPanel extends JPanel {
	private MainMenu menu;
	private boolean pressed;

	public ControlsPanel(MainMenu menu) {
		super();
		this.menu = menu;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = c.NORTHWEST;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		JButton backtomenu = new JButton("Back To Settings");
		backtomenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsPanel spanel = new SettingsPanel(menu);
				menu.changeFrame(spanel);
			}
		});
		add(backtomenu, c);
		JButton resettodefault = new JButton("Reset To Defaults");
		c.anchor = c.NORTHEAST;
		add(resettodefault, c);
		JPanel bpanel = createButtons();
		c.anchor = c.CENTER;
		bpanel.setOpaque(false);
		add(bpanel, c);
		setBackground(Color.BLACK);

	}

	public JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 4));
		panel = createBwL(panel, "Thrust Fwd");
		panel = createBwL(panel, "Fire 1");
		panel = createBwL(panel, "Thrust Rev");
		panel = createBwL(panel, "Fire 2");
		panel = createBwL(panel, "Pitch Down");
		panel = createBwL(panel, "Fire 3");
		panel = createBwL(panel, "Pitch Up");
		panel = createBwL(panel, "Manual");
		panel = createBwL(panel, "Roll Left");
		panel = createBwL(panel, "Manual Prev");
		panel = createBwL(panel, "Roll Right");
		panel = createBwL(panel, "Manual Next");
		panel = createBwL(panel, "Overdrive");
		return panel;

	}

	public JPanel createBwL(JPanel panel, String name) {
		// TODO make changing controls work visually
		JLabel label = new JLabel(name);
		label.setOpaque(false);
		label.setForeground(Color.WHITE);
		panel.add(label);
		JButton button = new JButton(name);
		button.addActionListener(ev -> {
			pressed = true;

		});
		button.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (pressed) {
					System.out.println(e.getKeyChar());
					button.setText("" + Character.toUpperCase(e.getKeyChar()));
					button.setEnabled(true);
					pressed = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});
		panel.add(button);
		return panel;
	}
}
