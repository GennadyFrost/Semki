package com.frost.semki.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frost.semki.Main;

/**
 * Описание эффекта нажатия на экран, черный круг, который изменяет размер и прозрачность.
 */
public class Touch {

    public float x, y, h = 1, scaleTouch = 5;


    Touch(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Рисование ффекта
     *
     * @param batch объект, который рисует
     * @return возвращает true, если эффект пропал, иначе false
     */
    public boolean render(SpriteBatch batch) {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, (1 - (h / 200)));
        batch.draw((Texture) Main.manager.get(Main.Paths.CIRCLE), (x - h / 4), (y - h / 4), (h += scaleTouch) / 2, (h += scaleTouch) / 2);
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
        if (h >= 200) {
            return true;
        }
        return false;
    }

}
