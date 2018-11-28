package com.frost.semki.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.frost.semki.Main;

public class Intro implements MyScreen {

    SpriteBatch batch;                                 // Объект, который занимается рисованием
    Applet applet;                                     // Мигающее слово TOUCH
    private Array<Touch> touches = new Array<Touch>();     // Массив с эффектами

    public Intro(SpriteBatch b) {
        batch = b;
        applet = new Applet(400 - 125, 280 / 2, 237, 64);
//        setInput();
    }


    @Override
    public void show() {

    }

    /**
     * Слушатель нажатия по экрану
     */
    public void setInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            /**
             * Если нажать на экран, запускется следующая сцена
             */
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touches.add(new Touch((int) (screenX * 800 / (float) Gdx.graphics.getWidth()), 480 - (int) (screenY * 480 / (float) Gdx.graphics.getHeight())));
                Main.main.setState(new CurrentStatus(batch));
                return super.touchUp(screenX, screenY, pointer, button);
            }

            /**
             * Нажатие на кнопку Back приостанавливает приложение
             */
            @Override
            public boolean keyDown(int keycode) {
                Gdx.app.exit();
                return super.keyDown(keycode);
            }
        });
    }

    /**
     * Метод, который рисует графику
     *
     * @param delta задерка перед рисованием (задаёт количество кадров в секунду)
     */
    @Override
    public void render(float delta) {
        batch.draw((Texture) Main.manager.get(Main.Paths.INTRO), 0, 0);
        applet.render(batch);
        for (int i = 0; i < touches.size; i++) {
            if (touches.get(i).render(batch)) {
                touches.removeIndex(i);
                i -= 1;
            }
        }
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
