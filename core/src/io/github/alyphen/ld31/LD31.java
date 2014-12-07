package io.github.alyphen.ld31;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.alyphen.ld31.message.Message;
import io.github.alyphen.ld31.message.Question;
import io.github.alyphen.ld31.screen.GameScreen;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;

public class LD31 extends Game {
	private GameScreen game;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;

	private Music music;

	private Texture background;
	private TextureRegion backgroundRegion;

	private Texture message;
	private TextureRegion messageRegion;

	private Texture question;
	private TextureRegion questionRegion;

	private Texture textures;
	private TextureRegion chairTexture;
	private TextureRegion curtainsTexture;
	private TextureRegion deskTexture;
	private TextureRegion fireplaceTexture;
	private TextureRegion flowerTableTexture;
	private TextureRegion rugCandleTexture;
	private TextureRegion rugOvalTexture;
	private TextureRegion rugWelcomeTexture;
	private TextureRegion sinkTexture;
	private TextureRegion sofaDownTexture;
	private TextureRegion sofaRightTexture;

	private Texture ericaSheet;
	private Animation ericaWalkUpAnimation;
	private Animation ericaWalkDownAnimation;
	private Animation ericaWalkLeftAnimation;
	private Animation ericaWalkRightAnimation;

	private Texture humphreySheet;
	private Animation humphreyWalkUpAnimation;
	private Animation humphreyWalkDownAnimation;
	private Animation humphreyWalkLeftAnimation;
	private Animation humphreyWalkRightAnimation;

	private Texture stephenSheet;
	private Animation stephenWalkUpAnimation;
	private Animation stephenWalkDownAnimation;
	private Animation stephenWalkLeftAnimation;
	private Animation stephenWalkRightAnimation;

	private Texture aliciaSheet;
	private Animation aliciaWalkUpAnimation;
	private Animation aliciaWalkDownAnimation;
	private Animation aliciaWalkLeftAnimation;
	private Animation aliciaWalkRightAnimation;

	private Texture vincentSheet;
	private Animation vincentWalkUpAnimation;
	private Animation vincentWalkDownAnimation;
	private Animation vincentWalkLeftAnimation;
	private Animation vincentWalkRightAnimation;

	private Texture jeanetteSheet;
	private Animation jeanetteWalkUpAnimation;
	private Animation jeanetteWalkDownAnimation;
	private Animation jeanetteWalkLeftAnimation;
	private Animation jeanetteWalkRightAnimation;

	private Texture robynSheet;
	private Animation robynWalkUpAnimation;
	private Animation robynWalkDownAnimation;
	private Animation robynWalkLeftAnimation;
	private Animation robynWalkRightAnimation;

	private Texture drewSheet;
	private Animation drewWalkUpAnimation;
	private Animation drewWalkDownAnimation;
	private Animation drewWalkLeftAnimation;
	private Animation drewWalkRightAnimation;

	private Texture helenaSheet;
	private Animation helenaWalkUpAnimation;
	private Animation helenaWalkDownAnimation;
	private Animation helenaWalkLeftAnimation;
	private Animation helenaWalkRightAnimation;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont(Gdx.files.internal("courier_prime.fnt"), true);
		font.setColor(WHITE);

		music = Gdx.audio.newMusic(Gdx.files.internal("ld31.ogg"));

		background = new Texture(Gdx.files.internal("background.png"));
		backgroundRegion = new TextureRegion(background, 0, 0, 800, 600);
		backgroundRegion.flip(false, true);

		message = new Texture(Gdx.files.internal("message.png"));
		messageRegion = new TextureRegion(message, 0, 0, 640, 64);
		messageRegion.flip(false, true);

		question = new Texture(Gdx.files.internal("question.png"));
		questionRegion = new TextureRegion(question, 0, 0, 480, 64);
		questionRegion.flip(false, true);

		textures = new Texture(Gdx.files.internal("textures.png"));
		chairTexture = new TextureRegion(textures, 0, 48, 48, 48);
		chairTexture.flip(false, true);
		curtainsTexture = new TextureRegion(textures, 64, 0, 96, 64);
		curtainsTexture.flip(false, true);
		deskTexture = new TextureRegion(textures, 0, 0, 64, 48);
		deskTexture.flip(false, true);
		fireplaceTexture = new TextureRegion(textures, 176, 0, 64, 176);
		fireplaceTexture.flip(false, true);
		flowerTableTexture = new TextureRegion(textures, 64, 64, 80, 64);
		flowerTableTexture.flip(false, true);
		rugCandleTexture = new TextureRegion(textures, 0, 208, 80, 80);
		rugCandleTexture.flip(false, true);
		rugOvalTexture = new TextureRegion(textures, 96, 192, 80, 64);
		rugOvalTexture.flip(false, true);
		rugWelcomeTexture = new TextureRegion(textures, 96, 256, 48, 32);
		rugWelcomeTexture.flip(false, true);
		sinkTexture = new TextureRegion(textures, 96, 144, 64, 32);
		sinkTexture.flip(false, true);
		sofaDownTexture = new TextureRegion(textures, 0, 112, 48, 96);
		sofaDownTexture.flip(false, true);
		sofaRightTexture = new TextureRegion(textures, 48, 112, 32, 80);
		sofaRightTexture.flip(false, true);

		ericaSheet = new Texture(Gdx.files.internal("erica.png"));
		ericaWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(ericaSheet, 0, 0, 16, 32),
				new TextureRegion(ericaSheet, 16, 0, 16, 32),
				new TextureRegion(ericaSheet, 32, 0, 16, 32),
				new TextureRegion(ericaSheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : ericaWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		ericaWalkDownAnimation.setPlayMode(LOOP);
		ericaWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(ericaSheet, 0, 32, 16, 32),
				new TextureRegion(ericaSheet, 16, 32, 16, 32),
				new TextureRegion(ericaSheet, 32, 32, 16, 32),
				new TextureRegion(ericaSheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : ericaWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		ericaWalkUpAnimation.setPlayMode(LOOP);
		ericaWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(ericaSheet, 0, 64, 16, 32),
				new TextureRegion(ericaSheet, 16, 64, 16, 32),
				new TextureRegion(ericaSheet, 32, 64, 16, 32),
				new TextureRegion(ericaSheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : ericaWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		ericaWalkRightAnimation.setPlayMode(LOOP);
		ericaWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(ericaSheet, 0, 96, 16, 32),
				new TextureRegion(ericaSheet, 16, 96, 16, 32),
				new TextureRegion(ericaSheet, 32, 96, 16, 32),
				new TextureRegion(ericaSheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : ericaWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		ericaWalkLeftAnimation.setPlayMode(LOOP);

		humphreySheet = new Texture(Gdx.files.internal("humphrey.png"));
		humphreyWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(humphreySheet, 0, 0, 16, 32),
				new TextureRegion(humphreySheet, 16, 0, 16, 32),
				new TextureRegion(humphreySheet, 32, 0, 16, 32),
				new TextureRegion(humphreySheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : humphreyWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		humphreyWalkDownAnimation.setPlayMode(LOOP);
		humphreyWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(humphreySheet, 0, 32, 16, 32),
				new TextureRegion(humphreySheet, 16, 32, 16, 32),
				new TextureRegion(humphreySheet, 32, 32, 16, 32),
				new TextureRegion(humphreySheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : humphreyWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		humphreyWalkUpAnimation.setPlayMode(LOOP);
		humphreyWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(humphreySheet, 0, 64, 16, 32),
				new TextureRegion(humphreySheet, 16, 64, 16, 32),
				new TextureRegion(humphreySheet, 32, 64, 16, 32),
				new TextureRegion(humphreySheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : humphreyWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		humphreyWalkRightAnimation.setPlayMode(LOOP);
		humphreyWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(humphreySheet, 0, 96, 16, 32),
				new TextureRegion(humphreySheet, 16, 96, 16, 32),
				new TextureRegion(humphreySheet, 32, 96, 16, 32),
				new TextureRegion(humphreySheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : humphreyWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		humphreyWalkLeftAnimation.setPlayMode(LOOP);

		stephenSheet = new Texture(Gdx.files.internal("stephen.png"));
		stephenWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(stephenSheet, 0, 0, 16, 32),
				new TextureRegion(stephenSheet, 16, 0, 16, 32),
				new TextureRegion(stephenSheet, 32, 0, 16, 32),
				new TextureRegion(stephenSheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : stephenWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		stephenWalkDownAnimation.setPlayMode(LOOP);
		stephenWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(stephenSheet, 0, 32, 16, 32),
				new TextureRegion(stephenSheet, 16, 32, 16, 32),
				new TextureRegion(stephenSheet, 32, 32, 16, 32),
				new TextureRegion(stephenSheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : stephenWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		stephenWalkUpAnimation.setPlayMode(LOOP);
		stephenWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(stephenSheet, 0, 64, 16, 32),
				new TextureRegion(stephenSheet, 16, 64, 16, 32),
				new TextureRegion(stephenSheet, 32, 64, 16, 32),
				new TextureRegion(stephenSheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : stephenWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		stephenWalkRightAnimation.setPlayMode(LOOP);
		stephenWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(stephenSheet, 0, 96, 16, 32),
				new TextureRegion(stephenSheet, 16, 96, 16, 32),
				new TextureRegion(stephenSheet, 32, 96, 16, 32),
				new TextureRegion(stephenSheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : stephenWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		stephenWalkLeftAnimation.setPlayMode(LOOP);

		aliciaSheet = new Texture(Gdx.files.internal("alicia.png"));
		aliciaWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(aliciaSheet, 0, 0, 16, 32),
				new TextureRegion(aliciaSheet, 16, 0, 16, 32),
				new TextureRegion(aliciaSheet, 32, 0, 16, 32),
				new TextureRegion(aliciaSheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : aliciaWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		aliciaWalkDownAnimation.setPlayMode(LOOP);
		aliciaWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(aliciaSheet, 0, 32, 16, 32),
				new TextureRegion(aliciaSheet, 16, 32, 16, 32),
				new TextureRegion(aliciaSheet, 32, 32, 16, 32),
				new TextureRegion(aliciaSheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : aliciaWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		aliciaWalkUpAnimation.setPlayMode(LOOP);
		aliciaWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(aliciaSheet, 0, 64, 16, 32),
				new TextureRegion(aliciaSheet, 16, 64, 16, 32),
				new TextureRegion(aliciaSheet, 32, 64, 16, 32),
				new TextureRegion(aliciaSheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : aliciaWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		aliciaWalkRightAnimation.setPlayMode(LOOP);
		aliciaWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(aliciaSheet, 0, 96, 16, 32),
				new TextureRegion(aliciaSheet, 16, 96, 16, 32),
				new TextureRegion(aliciaSheet, 32, 96, 16, 32),
				new TextureRegion(aliciaSheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : aliciaWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		aliciaWalkLeftAnimation.setPlayMode(LOOP);

		vincentSheet = new Texture(Gdx.files.internal("vincent.png"));
		vincentWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(vincentSheet, 0, 0, 16, 32),
				new TextureRegion(vincentSheet, 16, 0, 16, 32),
				new TextureRegion(vincentSheet, 32, 0, 16, 32),
				new TextureRegion(vincentSheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : vincentWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		vincentWalkDownAnimation.setPlayMode(LOOP);
		vincentWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(vincentSheet, 0, 32, 16, 32),
				new TextureRegion(vincentSheet, 16, 32, 16, 32),
				new TextureRegion(vincentSheet, 32, 32, 16, 32),
				new TextureRegion(vincentSheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : vincentWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		vincentWalkUpAnimation.setPlayMode(LOOP);
		vincentWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(vincentSheet, 0, 64, 16, 32),
				new TextureRegion(vincentSheet, 16, 64, 16, 32),
				new TextureRegion(vincentSheet, 32, 64, 16, 32),
				new TextureRegion(vincentSheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : vincentWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		vincentWalkRightAnimation.setPlayMode(LOOP);
		vincentWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(vincentSheet, 0, 96, 16, 32),
				new TextureRegion(vincentSheet, 16, 96, 16, 32),
				new TextureRegion(vincentSheet, 32, 96, 16, 32),
				new TextureRegion(vincentSheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : vincentWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		vincentWalkLeftAnimation.setPlayMode(LOOP);

		jeanetteSheet = new Texture(Gdx.files.internal("jeanette.png"));
		jeanetteWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(jeanetteSheet, 0, 0, 16, 32),
				new TextureRegion(jeanetteSheet, 16, 0, 16, 32),
				new TextureRegion(jeanetteSheet, 32, 0, 16, 32),
				new TextureRegion(jeanetteSheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : jeanetteWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		jeanetteWalkDownAnimation.setPlayMode(LOOP);
		jeanetteWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(jeanetteSheet, 0, 32, 16, 32),
				new TextureRegion(jeanetteSheet, 16, 32, 16, 32),
				new TextureRegion(jeanetteSheet, 32, 32, 16, 32),
				new TextureRegion(jeanetteSheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : jeanetteWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		jeanetteWalkUpAnimation.setPlayMode(LOOP);
		jeanetteWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(jeanetteSheet, 0, 64, 16, 32),
				new TextureRegion(jeanetteSheet, 16, 64, 16, 32),
				new TextureRegion(jeanetteSheet, 32, 64, 16, 32),
				new TextureRegion(jeanetteSheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : jeanetteWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		jeanetteWalkRightAnimation.setPlayMode(LOOP);
		jeanetteWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(jeanetteSheet, 0, 96, 16, 32),
				new TextureRegion(jeanetteSheet, 16, 96, 16, 32),
				new TextureRegion(jeanetteSheet, 32, 96, 16, 32),
				new TextureRegion(jeanetteSheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : jeanetteWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		jeanetteWalkLeftAnimation.setPlayMode(LOOP);

		robynSheet = new Texture(Gdx.files.internal("robyn.png"));
		robynWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(robynSheet, 0, 0, 16, 32),
				new TextureRegion(robynSheet, 16, 0, 16, 32),
				new TextureRegion(robynSheet, 32, 0, 16, 32),
				new TextureRegion(robynSheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : robynWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		robynWalkDownAnimation.setPlayMode(LOOP);
		robynWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(robynSheet, 0, 32, 16, 32),
				new TextureRegion(robynSheet, 16, 32, 16, 32),
				new TextureRegion(robynSheet, 32, 32, 16, 32),
				new TextureRegion(robynSheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : robynWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		robynWalkUpAnimation.setPlayMode(LOOP);
		robynWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(robynSheet, 0, 64, 16, 32),
				new TextureRegion(robynSheet, 16, 64, 16, 32),
				new TextureRegion(robynSheet, 32, 64, 16, 32),
				new TextureRegion(robynSheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : robynWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		robynWalkRightAnimation.setPlayMode(LOOP);
		robynWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(robynSheet, 0, 96, 16, 32),
				new TextureRegion(robynSheet, 16, 96, 16, 32),
				new TextureRegion(robynSheet, 32, 96, 16, 32),
				new TextureRegion(robynSheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : robynWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		robynWalkLeftAnimation.setPlayMode(LOOP);

		drewSheet = new Texture(Gdx.files.internal("drew.png"));
		drewWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(drewSheet, 0, 0, 16, 32),
				new TextureRegion(drewSheet, 16, 0, 16, 32),
				new TextureRegion(drewSheet, 32, 0, 16, 32),
				new TextureRegion(drewSheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : drewWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		drewWalkDownAnimation.setPlayMode(LOOP);
		drewWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(drewSheet, 0, 32, 16, 32),
				new TextureRegion(drewSheet, 16, 32, 16, 32),
				new TextureRegion(drewSheet, 32, 32, 16, 32),
				new TextureRegion(drewSheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : drewWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		drewWalkUpAnimation.setPlayMode(LOOP);
		drewWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(drewSheet, 0, 64, 16, 32),
				new TextureRegion(drewSheet, 16, 64, 16, 32),
				new TextureRegion(drewSheet, 32, 64, 16, 32),
				new TextureRegion(drewSheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : drewWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		drewWalkRightAnimation.setPlayMode(LOOP);
		drewWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(drewSheet, 0, 96, 16, 32),
				new TextureRegion(drewSheet, 16, 96, 16, 32),
				new TextureRegion(drewSheet, 32, 96, 16, 32),
				new TextureRegion(drewSheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : drewWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		drewWalkLeftAnimation.setPlayMode(LOOP);

		helenaSheet = new Texture(Gdx.files.internal("helena.png"));
		helenaWalkDownAnimation = new Animation(
				0.2F,
				new TextureRegion(helenaSheet, 0, 0, 16, 32),
				new TextureRegion(helenaSheet, 16, 0, 16, 32),
				new TextureRegion(helenaSheet, 32, 0, 16, 32),
				new TextureRegion(helenaSheet, 48, 0, 16, 32)
		);
		for (TextureRegion frame : helenaWalkDownAnimation.getKeyFrames()) frame.flip(false, true);
		helenaWalkDownAnimation.setPlayMode(LOOP);
		helenaWalkUpAnimation = new Animation(
				0.2F,
				new TextureRegion(helenaSheet, 0, 32, 16, 32),
				new TextureRegion(helenaSheet, 16, 32, 16, 32),
				new TextureRegion(helenaSheet, 32, 32, 16, 32),
				new TextureRegion(helenaSheet, 48, 32, 16, 32)
		);
		for (TextureRegion frame : helenaWalkUpAnimation.getKeyFrames()) frame.flip(false, true);
		helenaWalkUpAnimation.setPlayMode(LOOP);
		helenaWalkRightAnimation = new Animation(
				0.2F,
				new TextureRegion(helenaSheet, 0, 64, 16, 32),
				new TextureRegion(helenaSheet, 16, 64, 16, 32),
				new TextureRegion(helenaSheet, 32, 64, 16, 32),
				new TextureRegion(helenaSheet, 48, 64, 16, 32)
		);
		for (TextureRegion frame : helenaWalkRightAnimation.getKeyFrames()) frame.flip(false, true);
		helenaWalkRightAnimation.setPlayMode(LOOP);
		helenaWalkLeftAnimation = new Animation(
				0.2F,
				new TextureRegion(helenaSheet, 0, 96, 16, 32),
				new TextureRegion(helenaSheet, 16, 96, 16, 32),
				new TextureRegion(helenaSheet, 32, 96, 16, 32),
				new TextureRegion(helenaSheet, 48, 96, 16, 32)
		);
		for (TextureRegion frame : helenaWalkLeftAnimation.getKeyFrames()) frame.flip(false, true);
		helenaWalkLeftAnimation.setPlayMode(LOOP);

		game = new GameScreen(this);
		setScreen(game);
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
		font.dispose();
		music.dispose();
		background.dispose();
		message.dispose();
		question.dispose();
		textures.dispose();
		ericaSheet.dispose();
		humphreySheet.dispose();
		stephenSheet.dispose();
		aliciaSheet.dispose();
		vincentSheet.dispose();
		jeanetteSheet.dispose();
		robynSheet.dispose();
		drewSheet.dispose();
		helenaSheet.dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public TextureRegion getBackground() {
		return backgroundRegion;
	}

	public TextureRegion getTexture(String name) {
		switch (name) {
			case "chair": return chairTexture;
			case "curtains": return curtainsTexture;
			case "desk": return deskTexture;
			case "fireplace": return fireplaceTexture;
			case "flower_table": return flowerTableTexture;
			case "rug_candle": return rugCandleTexture;
			case "rug_oval": return rugOvalTexture;
			case "rug_welcome": return rugWelcomeTexture;
			case "sink": return sinkTexture;
			case "sofa_down": return sofaDownTexture;
			case "sofa_right": return sofaRightTexture;
			default: return null;
		}
	}

	public Animation getCharacterAnimation(String name, String direction) {
		switch (name) {
			case "erica":
				switch (direction) {
					case "up": return ericaWalkUpAnimation;
					case "down": return ericaWalkDownAnimation;
					case "left": return ericaWalkLeftAnimation;
					case "right": return ericaWalkRightAnimation;
					default: return null;
				}
			case "humphrey":
				switch (direction) {
					case "up": return humphreyWalkUpAnimation;
					case "down": return humphreyWalkDownAnimation;
					case "left": return humphreyWalkLeftAnimation;
					case "right": return humphreyWalkRightAnimation;
				}
			case "stephen":
				switch (direction) {
					case "up": return stephenWalkUpAnimation;
					case "down": return stephenWalkDownAnimation;
					case "left": return stephenWalkLeftAnimation;
					case "right": return stephenWalkRightAnimation;
				}
			case "alicia":
				switch (direction) {
					case "up": return aliciaWalkUpAnimation;
					case "down": return aliciaWalkDownAnimation;
					case "left": return aliciaWalkLeftAnimation;
					case "right": return aliciaWalkRightAnimation;
				}
			case "vincent":
				switch (direction) {
					case "up": return vincentWalkUpAnimation;
					case "down": return vincentWalkDownAnimation;
					case "left": return vincentWalkLeftAnimation;
					case "right": return vincentWalkRightAnimation;
				}
			case "jeanette":
				switch (direction) {
					case "up": return jeanetteWalkUpAnimation;
					case "down": return jeanetteWalkDownAnimation;
					case "left": return jeanetteWalkLeftAnimation;
					case "right": return jeanetteWalkRightAnimation;
				}
			case "robyn":
				switch (direction) {
					case "up": return robynWalkUpAnimation;
					case "down": return robynWalkDownAnimation;
					case "left": return robynWalkLeftAnimation;
					case "right": return robynWalkRightAnimation;
				}
			case "drew":
				switch (direction) {
					case "up": return drewWalkUpAnimation;
					case "down": return drewWalkDownAnimation;
					case "left": return drewWalkLeftAnimation;
					case "right": return drewWalkRightAnimation;
				}
			case "helena":
				switch (direction) {
					case "up": return helenaWalkUpAnimation;
					case "down": return helenaWalkDownAnimation;
					case "left": return helenaWalkLeftAnimation;
					case "right": return helenaWalkRightAnimation;
				}
			default: return null;
		}
	}

	public Message showMessage(String message) {
		return game.showMessage(message);
	}

	public Question showQuestion(String message, String... answers) {
		return game.showQuestion(message, answers);
	}

	public TextureRegion getMessageTexture() {
		return messageRegion;
	}

	public TextureRegion getQuestionTexture() {
		return questionRegion;
	}

	public Music getMusic() {
		return music;
	}
}
