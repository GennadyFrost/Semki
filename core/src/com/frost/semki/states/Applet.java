package com.frost.semki.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frost.semki.Main;

/**
 * Мигающее слово TOUCH
 */
public class Applet {


    private double alpha = 1;
    private float scale = 0;

    private int x, y, width, height;

    Applet(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Main.manager.load("touch.png", Texture.class);
        Main.manager.finishLoading();

    }

    /**
     * Рисование эффекта
     *
     * @param batch объект рисования
     */
    public void render(SpriteBatch batch) {
        scale += 0.055f;
        alpha = (Math.sin(scale) + 1) / 2;
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, (float) alpha);
        batch.draw((Texture) Main.manager.get("touch.png"), x, y, width, height);
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);

    }
}

