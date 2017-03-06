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

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.*;
import org.cocos2d.types.CGSize;

import android.view.KeyEvent;
import android.view.MotionEvent;

import alvi17.shift.render.Background;
import alvi17.shift.render.Block;
import alvi17.shift.render.Label;


public class AboutScreen extends CCLayer  {

	public static CCScene scene(CCScene next) {
		CCScene result = CCScene.node();
		result.addChild(Background.node());
		
		CGSize s = CCDirector.sharedDirector().winSize();
		CCSprite bg = CCSprite.sprite("about.png");
		bg.setScale(Block.SCALE*Main.SCALE);
		bg.setPosition(s.width / 2, s.height / 2);
		result.addChild(bg);
		
		result.addChild((new AboutScreen(next)).getScaleLayer());
		return result;
	}
	
	private CCScene mNext;
	
	class ScaleLayer extends CCLayer implements KeyHandler{
		public ScaleLayer() {
			addChild(AboutScreen.this);
			setScale(Main.SCALE*Block.SCALE);			
		}
		
		public boolean HandleKeyEvent(KeyEvent event) {
			AboutScreen.this.done();
			return false;
		}
	}
		
	public AboutScreen(CCScene next) {
		mNext = next;
		CGSize s = CCDirector.sharedDirector().winSize();
		String[] text = MyResources.stringArray(R.array.helpscreen);
		int pos=text.length * (int) Label.SMALLER;
		for (String str : text) {		
			Label lbl = new Label(str, Label.SMALLER);		
			lbl.setPosition(0, pos);
			pos -= Label.SMALLER;
			addChild(lbl);
		}
		setPosition(s.width / 2, (s.height / 2) - (text.length * Label.SMALLER)/2);
		setIsTouchEnabled(true);
	}
	
	private ScaleLayer getScaleLayer() {
		return new ScaleLayer();
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		done();
		return super.ccTouchesEnded(event);
	}
	
	void done() {		
		CCTransitionScene transition = Main.getTransisionFor(mNext);
		CCDirector.sharedDirector().replaceScene(transition);
		mNext = null;
	}

	
}
