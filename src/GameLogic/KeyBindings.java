package GameLogic;

import com.sun.glass.events.KeyEvent;

public class KeyBindings {
	
	public static final int DEFAULT_FIRE_WEAPON_1_BUTTON = KeyEvent.VK_1;
	public static final int DEFAULT_FIRE_WEAPON_2_BUTTON = KeyEvent.VK_2;
	public static final int DEFAULT_FIRE_WEAPON_3_BUTTON = KeyEvent.VK_3;
	public static final int DEFAULT_ACCELERATE_BUTTON = KeyEvent.VK_E;
	public static final int DEFAULT_DECELERATE_BUTTON = KeyEvent.VK_Q;
	public static final int DEFAULT_PITCH_DOWN_BUTTON = KeyEvent.VK_W;
	public static final int DEFAULT_PITCH_UP_BUTTON = KeyEvent.VK_S;
	public static final int DEFAULT_ROLL_LEFT_BUTTON = KeyEvent.VK_A;
	public static final int DEFAULT_ROLL_RIGHT_BUTTON = KeyEvent.VK_D;
	
	public static int CURRENT_FIRE_WEAPON_1_BUTTON = KeyEvent.VK_1;
	public static int CURRENT_FIRE_WEAPON_2_BUTTON = KeyEvent.VK_2;
	public static int CURRENT_FIRE_WEAPON_3_BUTTON = KeyEvent.VK_3;
	public static int CURRENT_ACCELERATE_BUTTON = KeyEvent.VK_E;
	public static int CURRENT_DECELERATE_BUTTON = KeyEvent.VK_Q;
	public static int CURRENT_PITCH_DOWN_BUTTON = KeyEvent.VK_W;
	public static int CURRENT_PITCH_UP_BUTTON = KeyEvent.VK_S;
	public static int CURRENT_ROLL_LEFT_BUTTON = KeyEvent.VK_A;
	public static int CURRENT_ROLL_RIGHT_BUTTON = KeyEvent.VK_D;

	
	public void resetKeysToDefaults(){
		CURRENT_FIRE_WEAPON_1_BUTTON = DEFAULT_FIRE_WEAPON_1_BUTTON;
		CURRENT_FIRE_WEAPON_2_BUTTON = DEFAULT_FIRE_WEAPON_2_BUTTON;
		CURRENT_FIRE_WEAPON_3_BUTTON = DEFAULT_FIRE_WEAPON_3_BUTTON;
		CURRENT_ACCELERATE_BUTTON = DEFAULT_ACCELERATE_BUTTON;
		CURRENT_DECELERATE_BUTTON = DEFAULT_DECELERATE_BUTTON;
		CURRENT_PITCH_DOWN_BUTTON = DEFAULT_PITCH_DOWN_BUTTON;
		CURRENT_PITCH_UP_BUTTON = DEFAULT_PITCH_UP_BUTTON;
		CURRENT_ROLL_LEFT_BUTTON = DEFAULT_ROLL_LEFT_BUTTON;
		CURRENT_ROLL_RIGHT_BUTTON = DEFAULT_ROLL_RIGHT_BUTTON;
	}
	
	public static void changeKeyByCurrentvalue(int currentValue, int newValue){
		if(currentValue == CURRENT_FIRE_WEAPON_1_BUTTON){
			CURRENT_FIRE_WEAPON_1_BUTTON = newValue;
		} else if(currentValue == CURRENT_FIRE_WEAPON_2_BUTTON){
			CURRENT_FIRE_WEAPON_2_BUTTON = newValue;
		}else if(currentValue == CURRENT_FIRE_WEAPON_3_BUTTON){
			CURRENT_FIRE_WEAPON_3_BUTTON = newValue;
		}else if(currentValue == CURRENT_ACCELERATE_BUTTON){
			CURRENT_ACCELERATE_BUTTON = newValue;
		}else if(currentValue == CURRENT_DECELERATE_BUTTON){
			CURRENT_DECELERATE_BUTTON = newValue;
		}else if(currentValue == CURRENT_PITCH_DOWN_BUTTON){
			CURRENT_PITCH_DOWN_BUTTON = newValue;
		}else if(currentValue == CURRENT_PITCH_UP_BUTTON){
			CURRENT_PITCH_UP_BUTTON = newValue;
		}else if(currentValue == CURRENT_ROLL_LEFT_BUTTON){
			CURRENT_ROLL_LEFT_BUTTON = newValue;
		}else if(currentValue == CURRENT_ROLL_RIGHT_BUTTON){
			CURRENT_ROLL_RIGHT_BUTTON = newValue;
		}
	}
	
}