/*
 * Copyright (C) 2010 Florian Sundermann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package alvi17.shift;

import java.util.List;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.*;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCTransitionScene;
import org.cocos2d.types.CGSize;

import android.view.KeyEvent;

import alvi17.shift.render.Background;
import alvi17.shift.render.Block;
import alvi17.shift.render.Label;
import alvi17.shift.render.TextEntry;


public class MainMenu extends CCLayer implements KeyHandler {

	private static CCScene fCurrent = null;
	
	public static CCScene scene() {
		if (fCurrent == null) {
			fCurrent = CCScene.node();
			fCurrent.addChild(Background.node());
			
			CCSprite image = Board.getCenterScaledImg("menu.png");
			fCurrent.addChild(image);
			
			fCurrent.addChild(new MainMenu());
		}
		return fCurrent;
	}
	
	private final CCMenu mMenu;
	
	private CCMenu getMenu() {	
		CCMenuItem easy = getTextItem(R.string.easy, "startEasy");
		CCMenuItem normal = getTextItem(R.string.normal, "startNormal");
		CCMenuItem hard = getTextItem(R.string.hard, "startHard");
		CCMenuItem quit = getTextItem(R.string.quit, "onQuit");
		CCMenuItem help = getTextItem(R.string.show_help, "showHelp");
		CCMenuItem about = getTextItem(R.string.about, "showAbout");
		CCMenuItem highscore = getTextItem(R.string.highscore, "showHighscore");
		
        CCMenu result = CCMenu.menu(easy, normal, hard, highscore, help, quit);
		result.alignItemsVertically(2f);
		CGSize winSize = CCDirector.sharedDirector().winSize();
		
		result.setPosition(winSize.width /2f, winSize.height / 4.3f);
		return result;
	}
	
	
	private CCMenuItem getTextItem(int resourceId, String selector) {
		String txt = MyResources.string(resourceId);
		
		CCNode entry = new TextEntry(TextEntry.TextBoxType.NotSelected, null);
		CCNode select = new TextEntry(TextEntry.TextBoxType.Selected, null);
		
		CCMenuItemSprite result = CCMenuItemSprite.item(entry, select, 
				this, selector);
		Label lbl = new Label(txt, Label.DEFAULT);
		lbl.setAnchorPoint(0, 0);
		lbl.setPosition(40f,3f);
		result.addChild(lbl);
		result.setScale(Main.SCALE * Block.SCALE);
		return result;
	}
	
	private MainMenu() {
		mMenu = getMenu();
		this.addChild(mMenu);
	}
	
	public void onQuit(Object sender) {
		CCDirector.sharedDirector().getActivity().finish();		
	}
	
	public void startEasy(Object sender) {
		Game.Current().setDifficulty(Game.Difficulty.Easy);
		CCTransitionScene board = Main.getTransisionFor(Board.scene());
		CCDirector.sharedDirector().replaceScene(board);
	}
	
	public void startNormal(Object sender) {
		Game.Current().setDifficulty(Game.Difficulty.Normal);
		CCTransitionScene board = Main.getTransisionFor(Board.scene());
		CCDirector.sharedDirector().replaceScene(board);		
	}
	
	public void startHard(Object sender) {
		Game.Current().setDifficulty(Game.Difficulty.Hard);
		CCTransitionScene board = Main.getTransisionFor(Board.scene());
		CCDirector.sharedDirector().replaceScene(board);		
	}
	
	public void showHelp(Object sender) {
		CCTransitionScene helpScrn = Main.getTransisionFor(HelpScreen.scene(scene()));
		CCDirector.sharedDirector().replaceScene(helpScrn);
	}
	
	public void showAbout(Object sender) {
		CCTransitionScene aboutScrn = Main.getTransisionFor(AboutScreen.scene(scene()));
		CCDirector.sharedDirector().replaceScene(aboutScrn);
	}
	
	public void showHighscore(Object sender) {
		CCTransitionScene scores = Main.getTransisionFor(Highscores.scene());
		CCDirector.sharedDirector().replaceScene(scores);
	}

	public boolean HandleKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch(event.getKeyCode()) {
				case KeyEvent.KEYCODE_DPAD_DOWN:
					moveSelection(false); break;
				case KeyEvent.KEYCODE_DPAD_UP:
					moveSelection(true); break;
				case KeyEvent.KEYCODE_DPAD_CENTER:
					execSelectedItem(); break;
			}
		}
		return false;
	}
	
	private void execSelectedItem() {
		CCMenuItem selItm = mMenu.getSelectedItem();
		selItm.activate();
	}
	
	private void moveSelection(boolean next) {
		List<CCNode> nodes = mMenu.getChildren();
		CCMenuItem selItm = mMenu.getSelectedItem();
		if (selItm != null)
			selItm.unselected();
		
		int selIdx = nodes.indexOf(selItm);
		if (next)
			selIdx--;
		else
			selIdx++;
		if (selIdx < 0)
			selIdx = nodes.size() -1;
		if (selIdx >= nodes.size())
			selIdx = 0;
		selItm = ((CCMenuItem)nodes.get(selIdx));		
		mMenu.setSelectedItem(selItm);
		selItm.selected();
	}


	
}
