package com.frost.semki.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.frost.semki.Main;

/**
 * Сцена, которая показывает текущее звание
 */
public class CurrentStatus implements MyScreen {

    SpriteBatch batch;                          // Объект рисования
    Applet applet;                              // Мигающее слово TOUCH
    BitmapFont font;                            // Объект, которые рисует текст
    private Array<Touch> touches = new Array<Touch>(); // Массив с эффектами нажатия

    CurrentStatus(SpriteBatch batch){
        font = new BitmapFont();
        font.setColor(Color.BLACK);
                applet = new Applet(400 - 125, 280 / 2, 237, 64);
        this.batch = batch;
    }

    /**
     * Если нажатие произошло, меняется сцена на следующую
     */
    @Override
    public void setInput() {
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touches.add(new Touch((int) (screenX * 800 / (float)Gdx.graphics.getWidth()), 480 - (int) (screenY * 480 / (float)Gdx.graphics.getHeight())));
                Main.main.setState(new Game(batch));
                return super.touchUp(screenX, screenY, pointer, button);
            }
            @Override
            public boolean keyDown(int keycode) {
                Gdx.app.exit();
                return super.keyDown(keycode);
            }
        });
    }

    @Override
    public void show() {

    }

    /**
     * Рисование сцены
     * @param delta задержка перед рисованием
     */
    @Override
    public void render(float delta) {
        applet.render(batch);
        for (int i = 0; i < touches.size; i++){
            if (touches.get(i).render(batch)){
                touches.removeIndex(i);
                i -= 1;
            }
        }
        if (Main.Status.status == 0){
            batch.draw((Texture) Main.manager.get(Main.Paths.CURRENT_STATUS_FIRST), 0, 0);
        }
        if (Main.Status.status == 1){
            batch.draw((Texture) Main.manager.get(Main.Paths.CURRENT_STATUS_SECOND), 0, 0);
        }
        if (Main.Status.status == 2){
            batch.draw((Texture) Main.manager.get(Main.Paths.CURRENT_STATUS_THIRD), 0, 0);
        }
        font.draw(batch, "" + Main.GLOBAL_SCORE, 310, 110);
        font.draw(batch, (Main.Status.statusInteger.length == Main.Status.status + 1) ? "0" : "" + (Main.Status.statusInteger[Main.Status.status + 1] - Main.GLOBAL_SCORE), 440, 92);
        applet.render(batch);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
