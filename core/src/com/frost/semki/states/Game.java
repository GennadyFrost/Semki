package com.frost.semki.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frost.semki.Main;

public class Game implements MyScreen {

    static int score = 0;    // Счёт в игре

    SpriteBatch batch;       // Объект, который рисует
    Drops drops;             // Объект, который описывает пачки с семками
    BitmapFont font;         // Объект, который занимается отрисовкой текста

    public Game(SpriteBatch b) {
        score = 0;
        batch = b;
        font = new BitmapFont();
        drops = new Drops(800, 480);
//        setInput();
    }

    @Override
    public void show() {

    }

    /**
     * Рисование
     *
     * @param delta задержка
     */
    @Override
    public void render(float delta) {
        batch.draw((Texture) Main.manager.get(Main.Paths.BACKGROUND), 0, 0, 800, 480);  // Рисование заставки
        drops.render(batch);                                                       // Рисование семок
        font.setColor(Color.BLACK);                                                // Рисование текста
        font.getData().setScale(2, 2);
        font.draw(batch, "SCORE: " + score, 0, 430);
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

    /**
     * Отслеживает нажатия на экран, проверяются коллиции с семками
     */
    @Override
    public void setInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                drops.collisionWithPlayer(screenX, screenY);         // Проверяются коллиции с семками
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean keyDown(int keycode) {
                Gdx.app.exit();
                return super.keyDown(keycode);
            }
        });
    }
}
