package jihye;

import jihye.GUI.UserInterface;

public class Main {
	public static void main(String[] args) {
		JihyeController jc = new JihyeController();		
		UserInterface ui = new UserInterface(jc);
		ui.showMainFrame();
	}
}