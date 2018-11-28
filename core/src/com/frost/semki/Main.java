package com.frost.semki;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.frost.semki.states.Intro;
import com.frost.semki.states.MyScreen;

public class Main extends Game {

    static double alpha = 0;                   // Эти переменные используются для отрисовки эффекта
    static double scale = 1;                   //   перехода по страницам (экранам)
    static boolean attenuation = false;        //  <---|  |  |  |
    static boolean attenuationOff = true;      //  <------|  |  |
    boolean setState = false;                  //  <---------|  |
    MyScreen temp;                             //  <------------|

    public static Preferences PREF = null;     // Переменная, которая работает с хранилищем данных на устройстве
    public static int GLOBAL_SCORE;            // Переменная, которая описывает счёт игрока и хранится на устройстве с помощью объекта PREF

    public static AssetManager manager;        // Менеджер картинок, звуковых эффектов и тп. (Контейнер с ресурсами)
    public static Main main;                   // Ссылка на главную страницу в игру


    OrthographicCamera camera;                 // Объект, который занимается масштабированием экрана (камера)

    SpriteBatch batch;          // Графический объекти, именно он занимается отрисовкой графики

    /**
     * Метод, который создаёт главный экран
     */
    @Override
    public void create() {
        PREF = Gdx.app.getPreferences("scoreSemki");               // Инициализация объекта, который отвечает за хранение информации в устройстве
        GLOBAL_SCORE = PREF.getInteger("scoreSemki", 0);    // Значение, которое будет храниться
        manager = new AssetManager();                                     // Инициализация менеджера ресурсов
        main = this;
        batch = new SpriteBatch();                                        // Инициализация графического объекта
        camera = new OrthographicCamera();                                // Инициализация камеры
        camera.setToOrtho(false, 800, 480);

        /**
         * Определение звания игрока
         */
        for (int i = 0; i < Status.statusInteger.length; i++) {
            Status.status = (Status.statusInteger[i] <= GLOBAL_SCORE) ? i : Status.status;
        }

        Gdx.input.setCatchBackKey(true);   // При нажатии на кнопку BACK игра закрывается

        loadAssets();                       // Загрузка ассетов
        setState(new Intro(batch));         // Изменение сцены
    }

    /**
     * Логика смены сцены и запуска эффекта
     *
     * @param s сцена, которая будет основной
     */
    public void setState(MyScreen s) {
        temp = s;
        setState = true;
        scale = 0;
        alpha = 0;
        attenuationOff = true;
    }

    /**
     * Метод, в котором происходит рисование
     */
    @Override
    public void render() {
        if (temp != null) {
            /*
              Настройка параметров графики
             */
            batch.setProjectionMatrix(camera.combined);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            if (screen != null)
                screen.render(Gdx.graphics.getDeltaTime());

            /*
              Начало логики эффекта перехода по сценам и непосредственно смены сцены
             */
            if (setState) {
                if (attenuationOff) {
                    scale += 1.5;
                    alpha += scale;
                    if (alpha >= 200) {
                        attenuationOff = false;
                        scale = 1;
                        attenuation = true;
                        Gdx.input.setInputProcessor(null);
                        setScreen(temp);            // Смена сцены
                        temp.setInput();
                    }
                }
                if (attenuation) {
                    scale += 1.5;
                    alpha -= scale;
                    if (alpha <= 1) {
                        attenuation = false;
                        alpha = 0;
                        scale = 1;
                        setState = false;
                    }
                }
            }
            batch.setColor(
                    batch.getColor().r,
                    batch.getColor().g,
                    batch.getColor().b,
                    (float) (alpha / 255)
            );
            batch.draw(
                    (Texture) manager.get(Paths.BLACK),
                    0, 0,
                    800, 480
            );
            batch.setColor(
                    batch.getColor().r,
                    batch.getColor().g,
                    batch.getColor().b,
                    1
            );
            /*
              КОнец логики эффекта перехода по сценам и непосредственно смены сцены
             */
            batch.end();
        }
    }

    /**
     * Высвобождение памяти, закрытие игры
     */
    public void dispose() {
        manager.dispose();
    }

    /**
     * Загрузка всех ресурсов
     */
    private static void loadAssets() {
        manager.load(Paths.BACKGROUND, Texture.class);
        manager.load(Paths.SEMKI_GREEN, Texture.class);
        manager.load(Paths.SEMKI_YELLOW, Texture.class);
        manager.load(Paths.PLAYER_2, Texture.class);
        manager.load(Paths.PLAYER_1, Texture.class);
        manager.load(Paths.INTRO, Texture.class);
        manager.load(Paths.DEATH, Texture.class);
        manager.load(Paths.BLACK, Texture.class);
        manager.load(Paths.CIRCLE, Texture.class);
        manager.load(Paths.CURRENT_STATUS_FIRST, Texture.class);
        manager.load(Paths.CURRENT_STATUS_SECOND, Texture.class);
        manager.load(Paths.CURRENT_STATUS_THIRD, Texture.class);
        manager.finishLoading();
    }

    /**
     * Ссылки на ресурсы
     */
    public interface Paths {
        String BACKGROUND = "background2.jpg";
        String SEMKI_GREEN = "food not for gods.png";
        String SEMKI_YELLOW = "food's gods.png";
        String PLAYER_2 = "stas_play_2_result.png";
        String PLAYER_1 = "stas_play_1.png";
        String INTRO = "intro.JPG";
        String DEATH = "death.jpg";
        String BLACK = "black.png";
        String CIRCLE = "circle.png";
        String CURRENT_STATUS_FIRST = "currentStatusFirst.JPG";
        String CURRENT_STATUS_SECOND = "currentStatusSecond.JPG";
        String CURRENT_STATUS_THIRD = "currentStatusThird.JPG";
    }

    /**
     * Описывает звание игрока, самое низкое - Новобранец: 0 и тд.
     */
    public static class Status {

        public static int status = 0;

        public static int statusInteger[] = {
                0, 1000, 10000
        };
    }
}
