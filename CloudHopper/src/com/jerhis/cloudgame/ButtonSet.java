package com.jerhis.cloudgame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import java.util.ArrayList;

public class ButtonSet {

    /*
    1- create atlas regions and initialize buttonset
    2- add buttons
    3- instantiate touch methods
    4- add draw call
    5- use poll click for results - case switch
    6- clear for reset
     */

    public ArrayList<Button> buttons;
    public int finalAnswer = -1, currentPushed = -1;

    public ButtonSet() {
        buttons = new ArrayList<Button>();
    }

    public void addButton(AtlasRegion oButton, AtlasRegion pButton, int x, int y) {
        buttons.add(new Button(oButton, pButton, x, y));
    }

    public int pollClick() {
        if (finalAnswer == -1)
            return -1;
        else {
            int pushedButtonIndex = finalAnswer;
            finalAnswer = -1;
            return pushedButtonIndex;
        }

    }

    public void draw(SpriteBatch batch) {
        for (Button b: buttons) {
            if (b.pushed)
                batch.draw(b.pushedButton, b.x - b.pushedButton.originalWidth/2, b.y - b.pushedButton.originalHeight/2);
            else
                batch.draw(b.openButton, b.x - b.openButton.originalWidth/2, b.y - b.openButton.originalHeight/2);
        }
    }

    public void touchDown(int x, int y) {

        openButtons();

        for (int k = 0; k < buttons.size(); k++) {
            if (inBounds(x, y, buttons.get(k))) {
                currentPushed = k;
                buttons.get(k).pushed = true;
            }
        }
    }

    public void touchDragged(int x, int y) {
        if (currentPushed != -1 && !inBounds(x, y, buttons.get(currentPushed))) {
            buttons.get(currentPushed).pushed = false;
            currentPushed = -1;
        }
    }

    public void touchUp(int x, int y) {
        if (currentPushed != -1 && inBounds(x, y, buttons.get(currentPushed))) {
            finalAnswer = currentPushed;
            buttons.get(currentPushed).pushed = false;
            currentPushed = -1;
        }
    }

    private boolean inBounds(int touchX, int touchY, Button button) {
        return touchX > button.x - button.openButton.originalWidth/2 &&
               touchX < button.x + button.openButton.originalWidth/2 &&
               touchY > button.y - button.openButton.originalHeight/2 &&
               touchY < button.y + button.openButton.originalHeight/2;
    }

    public void clear() {
        finalAnswer = -1;
        currentPushed = -1;
        openButtons();
    }

    private void openButtons() {
        for (Button b: buttons) {
            b.pushed = false;
        }
    }

    public class Button {

        int x, y;
        boolean pushed;
        AtlasRegion openButton, pushedButton;

        public Button(AtlasRegion ob, AtlasRegion pb, int x, int y) {
            pushed = false;
            openButton = ob;
            pushedButton = pb;
            this.x = x;
            this.y = y;
        }
    }

}
