package jihye;

import java.io.IOException;

import jihye.GUI.UserInterface;

public class Main {
	public static void main(String[] args) {
		JihyeController jc = new JihyeController();		
		UserInterface ui = new UserInterface(jc);
		ui.showMainChoiceFrame(); // 새로운 첫 화면으로 .. 
	}
}