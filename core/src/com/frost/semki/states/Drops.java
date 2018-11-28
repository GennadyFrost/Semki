package com.frost.semki.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.frost.semki.Main;

import java.util.Iterator;

/**
 * Описание пачек семок
 */
public class Drops {

    public static float speed;                     // Скорость семок

    private Array<Drop> drops;                     // Массив с семками
    private float score = 1;                       // Счет
    private static boolean catchDrop = false;      // Если на пачку нажали - true

    private int width, height, widthDrop, heightDrop;

    private Array<Touch> touches = new Array<Touch>();  // Массиво с эффектами нажатия по экрану

    public Drops(int width, int height) {
        speed = 0.55f;
        this.width = width;
        this.height = height;
        drops = new Array<Drop>();
        widthDrop = (int) (120 * 0.78534 * 0.8);
        heightDrop = (int) (120 * 0.8);
        createDrop();

    }

    /**
     * Рисование
     * @param g2 объект, который рисует
     */
    public void render(SpriteBatch g2) {
        for (Drop drop : drops) {
            drop.render(g2);
        }
        for (int i = 0; i < drops.size; i++) {
            drops.get(i).addPosition();

            if (drops.get(i).y <= height / 2) {
                createDrop();
            }
            if (drops.get(i).x <= 50) {
                /*
                 Если нажали на зеленую пачку (соленые семки)
                 */
                if (drops.get(i).isGreen) {
                    Game.score = 0;
                    Main.GLOBAL_SCORE = Main.Status.statusInteger[Main.Status.status];
                    Main.PREF.putInteger("scoreSemki", Main.GLOBAL_SCORE);
                    Main.PREF.flush();
                    Main.main.setState(new Death(g2));
                    drops.clear();
                }
                catchDrop = false;
                createDrop();
                try {
                    drops.removeIndex(i);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
            }

            /*
             Логика поведения семок при нажати
             */
            if (drops.get(i).y <= -heightDrop - 100) {
                if (!drops.get(i).isGreen) {
                    if (Main.Status.status != 2)
                        if (Game.score >= Main.Status.statusInteger[Main.Status.status + 1])
                            Main.Status.status++;
                    Main.GLOBAL_SCORE += Game.score;
                    if (Main.Status.status != Main.Status.statusInteger.length - 1)
                        if (Main.GLOBAL_SCORE >= Main.Status.statusInteger[Main.Status.status + 1])
                            Main.Status.status++;
                    Main.PREF.putInteger("scoreSemki", Main.GLOBAL_SCORE);
                    Main.PREF.flush();
                    Main.main.setState(new CurrentStatus(g2));
                    drops.clear();

                } else {
                    createDrop();
                    drops.removeIndex(i);
                    i--;
                }
            }
        }
        /*
         Рисование эффектов нажатия
         */
        for (int i = 0; i < touches.size; i++) {
            if (touches.get(i).render(g2)) {
                touches.removeIndex(i);
                i -= 1;
            }
        }

        /*
         Рисование друга
         */
        g2.draw((catchDrop) ? (Texture) Main.manager.get(Main.Paths.PLAYER_2)
                : (Texture) Main.manager.get(Main.Paths.PLAYER_1), -50, 0);
    }

    /**
     * Расчет совпадения координат, если совпадение есть - удалить почку
     * @param x координата
     * @param y координата
     */
    public void collisionWithPlayer(int x, int y) {
        touches.add(new Touch((int) (x * 800 / (float) Gdx.graphics.getWidth()), 480 - (int) (y * 480 / (float) Gdx.graphics.getHeight())));
        boolean removed = false;
        Iterator<Drop> iterator = drops.iterator();
        while (iterator.hasNext()) {
            Drop drop = iterator.next();

            if (drop.focus(x, y)) {
                catchDrop = true;
                removed = true;
                drop.remove = true;
                drop.endX = 50;
                drop.speed = 10;
                drop.endY = 160;
                if (Game.score == 10)
                    score = 2;
                score += score / 10;
                Game.score += (score < 1) ? 1 : score;
            }
        }
        if (removed) {
            createDrop();
        }
    }

    /**
     * Логика создания семок, задание начальных координат и начальной скорости
     */
    private void createDrop() {
        if (drops.size >= 2) return;
        int random;
        if ((int) (Math.random() * 10) == 5)
            drops.add(new Drop((random = (MathUtils.random(250, width - widthDrop))), height,
                    random, -heightDrop - 200,
                    widthDrop, heightDrop, Main.Paths.SEMKI_GREEN, true, speed
            ));
        else drops.add(new Drop((random = (MathUtils.random(250, width - widthDrop))), height,
                random, -heightDrop - 200,
                widthDrop, heightDrop, Main.Paths.SEMKI_YELLOW, false, speed
        ));
        speed += 0.55f;
    }


    public Array<Drop> getDrops() {
        return drops;
    }

    /**
     * Класс, который описывает одну пачку
     */
    private class Drop {

        float speed;
        boolean remove = false;
        float newSize = 0;
        boolean isGreen;
        float x, y, endX, endY, width, height;
        TextureRegion image;
        private float rotate = 0f;
        private float scaleRotate = 1f;

        Drop(int x, int y, int endX, int endY, int w, int h, String path, boolean isGreen, float speed) {
            image = new TextureRegion((Texture) Main.manager.get(path));
            this.speed = speed;
            this.isGreen = isGreen;
            this.x = x;
            this.y = y;
            this.endX = endX;
            this.endY = endY;
            this.width = w;
            this.height = h;
        }

        /**
         * Рисование пачки
         * @param g2 объект, который рисует пачку
         */
        public void render(SpriteBatch g2) {
            if (remove) {
                newSize += 0.25f;
                width = (width <= 40) ? 40 : width - newSize;
                height = (height <= 40) ? 40 : height - newSize;
            }

            g2.draw(
                    image,
                    (int) (x - newSize),
                    (int) (y - newSize),
                    (width) / 2,
                    (height) / 2,
                    (int) width,
                    (int) height,
                    1, 1,
                    rotate
            );
            if (x != endX) {
                catchDrop = true;
                scaleRotate = (scaleRotate >= 50) ? 50 : (scaleRotate + 1.2f);
                rotate += scaleRotate;
            }
        }

        /**
         * Изменение позиции пачки
         */
        public void addPosition() {

            float scaleX = endX - x;
            float scaleY = endY - y;
            double dx = 0, dy = 0;
            double gip = Math.sqrt(Math.pow(scaleX, 2) + Math.pow(scaleY, 2));

            speed += 0.03f;

            dy += scaleY / gip;
            dx += scaleX / gip;

            dx *= speed;
            dy *= speed;

            x += Double.isNaN(dx) ? 0 : dx;
            y += Double.isNaN(dy) ? 0 : dy;
        }

        /**
         * Проверка на совпадение координат нажатия и координат позиции пачки
         * @param x координата
         * @param y координата
         * @return если совпадение координат есть - true
         */
        boolean focus(int x, int y) {
            x = (int) (x * 800 / (float) Gdx.graphics.getWidth());
            y = 480 - (int) (y * 480 / (float) Gdx.graphics.getHeight());
            return (x >= this.x - 35 && x <= (this.x + width + 35) &&
                    y >= this.y - 35 && y <= (this.y + height + 35));
        }
    }
}
