package io.github.alyphen.ld31.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.action.*;
import io.github.alyphen.ld31.message.AnswerListener;
import io.github.alyphen.ld31.message.Message;
import io.github.alyphen.ld31.message.Question;
import io.github.alyphen.ld31.objects.GameObject;
import io.github.alyphen.ld31.objects.characters.*;
import io.github.alyphen.ld31.objects.furniture.*;

import java.util.Comparator;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameScreen implements Screen {

    private LD31 game;
    private Array<GameObject> objects;
    private Array<GameCharacter> characters;
    private OrthographicCamera camera;
    private Erica erica;
    private Vincent vincent;
    private Humphrey humphrey;
    private Stephen stephen;
    private Alicia alicia;
    private Jeanette jeanette;
    private Robyn robyn;
    private Drew drew;
    private Helena helena;
    private int messageOffset = 0;
    private Message message;
    private Array<Message> queuedMessages;
    private EventQueue eventQueue;
    private Array<String> items;
    private float timeSinceLastMusic;
    private Random random;

    public GameScreen(LD31 game) {
        this.game = game;
        random = new Random();
        timeSinceLastMusic = 0F;
        objects = new Array<>();
        characters = new Array<>();
        eventQueue = new EventQueue();
        queuedMessages = new Array<>();
        items = new Array<>();
        addObject(new Curtains(game, 80, 32, 3));
        addObject(new Curtains(game, 272, 32, 3));
        addObject(new Curtains(game, 496, 32, 3));
        addObject(new Desk(game, 288, 96, 2));
        erica = new Erica(game, 304, 128, 1);
        addCharacter(erica);
        vincent = new Vincent(game, 288, 448, 0);
        humphrey = new Humphrey(game, 288, 448, 0);
        stephen = new Stephen(game, 288, 448, 0);
        alicia = new Alicia(game, 288, 448, 0);
        jeanette = new Jeanette(game, 288, 448, 0);
        robyn = new Robyn(game, 288, 448, 0);
        drew = new Drew(game, 288, 448, 0);
        helena = new Helena(game, 288, 488, 0);
        addObject(new Chair(game, 288, 144, 0));
        addObject(new FlowerTable(game, 352, 96, 2));
        addObject(new Sink(game, 512, 112, 2));
        addObject(new RugCandle(game, 96, 288, 3));
        addObject(new SofaDown(game, 112, 208, 2));
        addObject(new SofaRight(game, 80, 272, 2));
        addObject(new Fireplace(game, 560, 202, 2));
        addObject(new RugOval(game, 464, 304, 3));
        addObject(new RugWelcome(game, 288, 448, 3));
        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        eventQueue.addEvent(createEvent1_1());
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (message == null) return false;
                if (message instanceof Question) {
                    Question question = (Question) message;
                    int y = 32;
                    for (int i = 0; i < question.getAnswers().size; i++) {
                        if (screenY >= y && screenY <= y + 64) {
                            question.selectAnswer(i);
                            message.setHasShown(true);
                            message = getNextMessage();
                            break;
                        }
                        y += 96;
                    }
                } else {
                    message.setHasShown(true);
                    message = getNextMessage();
                }
                return true;
            }
        });
        game.getMusic().play();
    }

    public void addCharacter(GameCharacter character) {
        characters.add(character);
        addObject(character);
    }

    public void removeCharacter(GameCharacter character) {
        characters.removeValue(character, true);
        removeObject(character);
    }

    public Array<GameCharacter> getCharacters() {
        return characters;
    }

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public void removeObject(GameObject object) {
        objects.removeValue(object, true);
    }

    public Array<GameObject> getObjects() {
        return objects;
    }

    @Override
    public void render(float delta) {
        if (!game.getMusic().isPlaying()) timeSinceLastMusic += Gdx.graphics.getDeltaTime();
        if (timeSinceLastMusic >= 60F && !game.getMusic().isPlaying()) {
            timeSinceLastMusic = 0F;
            game.getMusic().play();
        }
        eventQueue.onTick();
        objects.sort(new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                return o2.getDepth() - o1.getDepth();
            }
        });
        for (GameObject object : getObjects()) {
            object.onTick();
        }
        if (message == null && messageOffset > 0) {
            messageOffset -= 200 * Gdx.graphics.getDeltaTime();
            if (messageOffset < 0) messageOffset = 0;
        } else if (message != null && messageOffset < 64) {
            messageOffset += 200 * Gdx.graphics.getDeltaTime();
            if (messageOffset > 64) messageOffset = 64;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(game.getBackground(), 0, 0);
        for (GameObject object : getObjects()) {
            object.render(game.getSpriteBatch());
        }
        game.getSpriteBatch().draw(game.getMessageTexture(), 0, 480 - messageOffset);
        if (message != null) {
            game.getFont().drawWrapped(game.getSpriteBatch(), message.getText(), 16, 496 - messageOffset, 608);
            if (message instanceof Question) {
                Question question = (Question) message;
                int y = 32;
                for (String answer : question.getAnswers()) {
                    game.getSpriteBatch().draw(game.getQuestionTexture(), 80, y);
                    game.getFont().drawWrapped(game.getSpriteBatch(), answer, 96, y + 16, 448);
                    y += 96;
                }
            }
        }
        game.getSpriteBatch().end();
    }

    public Message showMessage(String message) {
        if (queuedMessages.size == 0 && this.message == null) {
            this.message = new Message(message);
            return this.message;
        } else {
            Message m = new Message(message);
            queuedMessages.add(m);
            return m;
        }
    }

    public Question showQuestion(String message, Array<String> answers) {
        if (queuedMessages.size == 0 && this.message == null) {
            this.message = new Question(message, answers);
            return (Question) this.message;
        } else {
            Question q = new Question(message, answers);
            queuedMessages.add(q);
            return q;
        }
    }

    public Message getNextMessage() {
        return queuedMessages.size > 0 ? queuedMessages.removeIndex(0) : null;
    }

    public void giveItem(String item) {
        items.add(item);
    }

    public void takeItem(String item) {
        items.removeValue(item, false);
    }

    public boolean hasItem(String item) {
        return items.contains(item, false);
    }

    private Event createEvent1_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(Hello.)"));
        a1.add(new SpeechAction(erica, "(I know you're there.)"));
        a1.add(new SpeechAction(erica, "(I know you're watching me.)"));
        a1.add(new SpeechAction(erica, "(I'm very observant, you see.)"));
        a1.add(new SpeechAction(erica, "(...I have to be.)"));
        a1.add(new NameAction(erica));
        a1.add(new SpeechAction(erica, "(My name is Erica, although those who know me through the internet know me as entropy.)"));
        a1.add(new SpeechAction(erica, "(My job? It's not important.)"));
        a1.add(new SpeechAction(erica, "(Or at least, I can't tell you, but maybe you can figure it out.)"));
        a1.add(new SpeechAction(erica, "(I work from home most days at the moment.)"));
        a1.add(new SpeechAction(erica, "(It's winter right now, and the snow has been piling up in thick sheets.)"));
        a1.add(new SpeechAction(erica, "(That makes it difficult to get places.)"));
        a1.add(new SpeechAction(erica, "(I have to travel a fair way to get to the office, so it makes it easier if I work from home.)"));
        a1.add(new SpeechAction(erica, "(At least in some respects.)"));
        a1.add(new SpeechAction(erica, "(At least at the office, I am usually left undisturbed.)"));
        a1.add(new SpeechAction(erica, "(You see, because of what I do, there's a lot of people who would rather not have me around.)"));
        a1.add(new SpeechAction(erica, "(While my actions may be to the benefit of my country, some don't see it that way.)"));
        a1.add(new NarrativeAction(this, "*There comes a knocking at the door.*"));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes");
        ans1.add("Yes, but leave it on latch");
        ans1.add("No");
        a1.add(new QuestionAction(this, "Do you answer?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(createEvent2_1());
                        break;
                    case 1:
                        eventQueue.addEvent(createEvent2_2());
                        break;
                    case 2:
                        eventQueue.addEvent(createEvent2_3());
                }
            }

        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent2_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 432));
        a1.add(new CharacterEnterAction(vincent, this));
        a1.add(new TrustChangeAction(vincent, 2));
        a1.add(new SpeechAction(vincent, "Good evening Miss Erica, I came to drop off the documents you requested."));
        a1.add(new SpeechAction(erica, "Ah, thank you for going to the trouble to get these, Vincent."));
        a1.add(new NameAction(vincent));
        a1.add(new NarrativeAction(this, "*Erica takes the documents from Vincent and places them on her desk*"));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new MovementAction(vincent, 288, 360));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 344));
        a1.add(new ObtainItemAction(this, "gchq_document_01"));
        a1.add(new SpeechAction(vincent, "I, uh, remember that's some sensitive information, Miss, so be careful what you do with it."));
        Array<String> ans1 = new Array<>();
        ans1.add("Make up a business-related matter");
        ans1.add("Explain they piqued your curiosity");
        ans1.add("Tell him that your reasoning is confidential");
        a1.add(new AskAction(vincent, "If you don't mind my asking, what would you be using them for?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(random.nextBoolean() ? GameScreen.this.createEvent3_1() : GameScreen.this.createEvent3_2());
                        break;
                    case 1:
                        eventQueue.addEvent(GameScreen.this.createEvent3_2());
                        break;
                    case 2:
                        eventQueue.addEvent(GameScreen.this.createEvent3_3());
                        break;
                }
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent2_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new TrustChangeAction(vincent, -1));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 448));
        a1.add(new SpeechAction(vincent, "Miss Erica?"));
        a1.add(new SpeechAction(erica, "Vincent?"));
        a1.add(new NameAction(vincent));
        a1.add(new SpeechAction(vincent, "I, uh, brought the papers you requested."));
        a1.add(new SpeechAction(erica, "Ah, my apologies for making you come all this way to deliver them in the snow. They are quite important, you see."));
        a1.add(new SpeechAction(vincent, "Ah, yes. Allow me to pass them to you."));
        a1.add(new NarrativeAction(this, "*Vincent awkwardly passes the documents around the side of the door, and Erica takes them under one arm*"));
        a1.add(new ObtainItemAction(this, "gchq_document_01"));
        Array<String> ans1 = new Array<>();
        ans1.add("Make up a business-related matter");
        ans1.add("Explain that they piqued your curiosity");
        ans1.add("Tell him that your reasoning is confidential");
        a1.add(new AskAction(vincent, "(In a hushed tone) May I request the reasoning behind your request, Miss?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(random.nextInt(100) < 25 ? GameScreen.this.createEvent3_1() : GameScreen.this.createEvent3_2());
                        break;
                    case 1:
                        eventQueue.addEvent(random.nextBoolean() ? GameScreen.this.createEvent3_2() : GameScreen.this.createEvent3_3());
                        break;
                    case 2:
                        eventQueue.addEvent(random.nextBoolean() ? GameScreen.this.createEvent3_3() : GameScreen.this.createEvent3_4());
                        break;
                }
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent2_3() {
        Array<Action> a1 = new Array<>();
        a1.add(new TrustChangeAction(vincent, -2));
        Array<String> ans1 = new Array<>();
        ans1.add("Open the envelope");
        ans1.add("Leave the envelope until later");
        ans1.add("Decide the delivery is suspicious and throw the envelope away");
        a1.add(new QuestionAction(this, "*An envelope is passed through the letterbox, landing on the mat*", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent3_5());
                        break;
                    case 1:
                        eventQueue.addEvent(GameScreen.this.createEvent3_6());
                        break;
                    case 2:
                        eventQueue.addEvent(GameScreen.this.createEvent3_7());
                        break;
                }
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent3_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new TrustChangeAction(vincent, 2));
        a1.add(new SpeechAction(erica, "They are quite important for the Aurora project, and they may cause it's success or downfall."));
        a1.add(new SpeechAction(vincent, "Ah, I see."));
        a1.add(new SpeechAction(vincent, "Well, Miss Erica, I shall be going, then."));
        a1.add(new SpeechAction(vincent, "Let me know if you need me!"));
        a1.add(new NarrativeAction(this, "*Vincent smiles at Erica, performs a short bow, and then trudges off into the snow*"));
        a1.add(new MovementAction(vincent, 288, 448));
        a1.add(new CharacterLeaveAction(vincent, this));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent3_8());
                eventQueue.addEvent(createEvent4_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent3_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "It's part of the Aurora project - it's quite important to me."));
        a1.add(new SpeechAction(vincent, "I...see."));
        a1.add(new SpeechAction(vincent, "Well, I won't question you further, Miss, I'm sure I've wasted enough of your time already."));
        a1.add(new NarrativeAction(this, "*Vincent leaves*"));
        a1.add(new MovementAction(vincent, 288, 448));
        a1.add(new CharacterLeaveAction(vincent, this));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent3_8());
                eventQueue.addEvent(createEvent4_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent3_3() {
        Array<Action> a1 = new Array<>();
        a1.add(new TrustChangeAction(vincent, -2));
        a1.add(new SpeechAction(erica, "That information is confidential, I'm afraid I can't tell you."));
        a1.add(new SpeechAction(vincent, "Ah, my apologies for asking, Miss."));
        a1.add(new NarrativeAction(this, "*Vincent leaves, trudging away, leaving deep footprints in the snow*"));
        a1.add(new MovementAction(vincent, 288, 448));
        a1.add(new CharacterLeaveAction(vincent, this));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent3_8());
                eventQueue.addEvent(createEvent4_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent3_4() {
        Array<Action> a1 = new Array<>();
        a1.add(new TrustChangeAction(vincent, -4));
        a1.add(new SpeechAction(erica, "I'm afraid I can't tell you that."));
        a1.add(new NarrativeAction(this, "*Vincent's eyes narrow*"));
        a1.add(new SpeechAction(vincent, "I see."));
        a1.add(new TrustChangeAction(jeanette, -2));
        a1.add(new NarrativeAction(this, "*Vincent leaves*"));
        a1.add(new MovementAction(vincent, 288, 448));
        a1.add(new CharacterLeaveAction(vincent, this));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent3_8());
                eventQueue.addEvent(createEvent4_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent3_5() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(Ah, it's those documents I requested from Vincent."));
        a1.add(new SpeechAction(erica, "(I'd better put these somewhere safe)"));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 448));
        a1.add(new ObtainItemAction(this, "gchq_document_01"));
        a1.add(new WaitAction(2));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent3_8());
                eventQueue.addEvent(createEvent4_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent3_6() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(I'll deal with this later.)"));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 448));
        a1.add(new ObtainItemAction(this, "gchq_envelope_01"));
        a1.add(new WaitAction(2));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent4_1());
                eventQueue.addEvent(createEvent4_3());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent3_7() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(This looks quite suspicious, it's probably best I dispose of this.)"));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 448));
        a1.add(new WaitAction(2));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 320, 176));
        a1.add(new MovementAction(erica, 320, 128));
        a1.add(new WaitAction(2));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent4_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent3_8() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(Vincent is one of my subordinates at work.)"));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return vincent.getTrust() > 0;
            }
        }, new SpeechAction(erica, "(He seems to trust me.)")));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return vincent.getTrust() == 0;
            }
        }, new SpeechAction(erica, "(I'm not sure whether he trusts me, though.)")));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return vincent.getTrust() <= 0;
            }
        }, new SpeechAction(erica, "(I don't think he trusts me, though.)")));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent4_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(I spend most of my life looking at posts on BBSes and talking to people on IRC.)"));
        a1.add(new SpeechAction(erica, "(Due to my work, I have access to a lot of information that others do not.)"));
        a1.add(new SpeechAction(erica, "(Outside of work, I'm part of an organisation known as Anomaly.)"));
        a1.add(new SpeechAction(erica, "(We collect information on GCHQ operations and expose their schemes so that people may protest against those that oppose basic human rights.)"));
        a1.add(new SpeechAction(erica, "(The right to privacy, ha, what a joke.)"));
        a1.add(new SpeechAction(erica, "(And not a funny one, either.)"));
        a1.add(new SpeechAction(erica, "(If you've seen some of the documents...)"));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes");
        ans1.add("No");
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return hasItem("gchq_document_01");
            }
        }, new AskAction(erica, "(Anyway, should I get these documents typed up?)", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent4_2());
                        break;
                    case 1:
                        eventQueue.addEvent(GameScreen.this.createEvent5_1());
                        break;
                }
            }
        })));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent4_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(Let's get these documents typed up so we can share them with the rest of the members of Anomaly.)"));
        a1.add(new WaitAction(3));
        a1.add(new TrustChangeAction(jeanette, -10));
        a1.add(new NarrativeAction(this, "\"<entropy> I was able to gather some more information from GCHQ, so that might be of some use on understanding their current project.\""));
        a1.add(new NarrativeAction(this, "\"<disruption> Ah, yous did, did yous? That'll be perfect, lass, ayv'e almost got enough information te know when we can disrupt this thing.\""));
        a1.add(new NarrativeAction(this, "\"<entropy> disruption: I'm uploading the documents to the BBS right now. You should have access to them in a couple of hours.\""));
        a1.add(new NarrativeAction(this, "\"<disruption> entropy: Tha' it be, if me dial-up stays stable.\""));
        a1.add(new NarrativeAction(this, "\"<entropy> disruption: You still haven't got that sorted?\""));
        a1.add(new NarrativeAction(this, "\"disruption (disruption@users.undernet.org) left IRC: Ping timeout\""));
        a1.add(new SpeechAction(erica, "(disruption lives in Ireland, and apparently that makes his internet inherently terrible.)"));
        a1.add(new SpeechAction(erica, "(While his ability usually makes up for that, we have lost communication with him for extended periods of time before, at sometimes crucial points.)"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent5_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent4_3() {
        Array<Action> a1 = new Array<>();
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(this, "\"<entropy> I received an envelope today - I wasn't sure of the sender, but I haven't opened it yet.\""));
        a1.add(new NarrativeAction(this, "\"<entropy> I'm not sure whether to trust it.\""));
        a1.add(new NarrativeAction(this, "\"<dissent> entropy: I wouldn't - they've been onto us a lot recently.\""));
        a1.add(new NarrativeAction(this, "\"<garbagekid> Y0, 4|\\|y1 901- w4r3z?\""));
        a1.add(new NarrativeAction(this, "\"<entropy> dissent: Yes, I noticed that - you haven't really been online much recently.\""));
        a1.add(new NarrativeAction(this, "\"garbagekid (garbagekid@users.undernet.org) left IRC: Kicked"));
        a1.add(new NarrativeAction(this, "\"<dissent> entropy: I'm thinking of just quitting it all. You know, we have our whole lives ahead of us, right?\""));
        a1.add(new NarrativeAction(this, "\"<entropy> dissent: Of corse, it's just -- I don't know. If no one else does it, what will happen to the state of the country?\""));
        a1.add(new SpeechAction(erica, "(dissent is my next door neighbour, Helena.)"));
        a1.add(new SpeechAction(erica, "(She got into this before I did, but I have since been a more active part of Anomaly.)"));
        a1.add(new SpeechAction(erica, "(She seems to have been getting tired of it recently, and wants to live a normal life.)"));
        Array<String> ans1 = new Array<>();
        ans1.add("Open it");
        ans1.add("Dispose of it");
        ans1.add("Do nothing");
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return hasItem("gchq_envelope_01");
            }
        }, new AskAction(erica, "Now, should I do anything with that envelope?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent4_5());
                        break;
                    case 1:
                        eventQueue.addEvent(GameScreen.this.createEvent4_6());
                }
            }
        })));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent4_5() {
        Array<Action> a1 = new Array<>();
        a1.add(new LoseItemAction(this, "gchq_envelope_01"));
        a1.add(new ObtainItemAction(this, "gchq_document_01"));
        a1.add(new NarrativeAction(this, "*Erica found GCHQ documents inside the envelope.*"));
        a1.add(new SpeechAction(erica, "(Ah, these must be from Vincent.)"));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes");
        ans1.add("No");
        a1.add(new AskAction(
                erica,
                "(Anyway, should I get these documents typed up?)", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent4_2());
                        break;
                    case 1:
                        break;
                }
            }
        }));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent5_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent4_6() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(It's probably best to dispose of this. Helena was probably right.)"));
        a1.add(new MovementAction(erica, 288, 448));
        a1.add(new LookAction(erica, "up"));
        a1.add(new WaitAction(2));
        a1.add(new NarrativeAction(this, "*Erica disposed of the documents*"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent5_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent5_1() {
        Array<Action> a1 = new Array<>();
        a1.add(
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return jeanette.getTrust() <= 0;
                    }
                }, new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent5_2());
                    }
                }, new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent6_1());
                    }
                }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent5_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new NarrativeAction(this, "*There is a knock at the door*"));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes");
        ans1.add("Yes, but leave it on latch");
        ans1.add("No");
        a1.add(new QuestionAction(
                this,
                "Answer it?", ans1
        ).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent5_3());
                        break;
                    case 1:
                        eventQueue.addEvent(GameScreen.this.createEvent5_4());
                        break;
                    case 2:
                        eventQueue.addEvent(GameScreen.this.createEvent6_1());
                        break;
                }
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent5_3() {
        Array<Action> a1 = new Array<>();
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 432));
        a1.add(new CharacterEnterAction(jeanette, this));
        a1.add(new TrustChangeAction(jeanette, 5));
        a1.add(new SpeechAction(erica, "Jeanette?"));
        a1.add(new NameAction(jeanette));
        a1.add(new NarrativeAction(this, "*Jeanette stomps the snow from her shoes, which scatters across the mat*"));
        a1.add(new SpeechAction(jeanette, "Hello darling, I came to visit to see how you were getting on."));
        a1.add(new SpeechAction(erica, "Fine, what prompted this?"));
        a1.add(new SpeechAction(jeanette, "Oh, darling, you know, I just heard it from the grapevine."));
        Array<String> ans1 = new Array<>();
        ans1.add("Let her look");
        ans1.add("Don't let her look");
        a1.add(new AskAction(jeanette, "Would you mind if I had a look at how you are getting on with work recently?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent5_5());
                        break;
                    case 1:
                        eventQueue.addEvent(GameScreen.this.createEvent5_6());
                }
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent5_4() {
        Array<Action> a1 = new Array<>();
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 432));
        a1.add(new TrustChangeAction(jeanette, -2));
        a1.add(new SpeechAction(erica, "Jeanette?"));
        a1.add(new NameAction(jeanette));
        a1.add(new SpeechAction(jeanette, "Yes dear, I just came to check on how you were getting along."));
        a1.add(new SpeechAction(jeanette, "It seems you're getting along fine, so I'll just leave now. Bye!"));
        a1.add(new SpeechAction(erica, "..."));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new SpeechAction(erica, "(That was odd.)"));
        a1.add(new SpeechAction(erica, "(I wonder what she wanted.)"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent5_7());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent5_5() {
        Array<Action> a1 = new Array<>();
        a1.add(new MovementAction(jeanette, 288, 176));
        a1.add(new MovementAction(jeanette, 272, 176));
        a1.add(new MovementAction(jeanette, 272, 128));
        a1.add(new DepthChangeAction(jeanette, 1));
        a1.add(new MovementAction(jeanette, 304, 128));
        a1.add(new LookAction(jeanette, "up"));
        a1.add(new WaitAction(3));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return random.nextBoolean();
            }
        }, new TrustChangeAction(jeanette, 20), new TrustChangeAction(jeanette, -2)));
        a1.add(new MovementAction(jeanette, 272, 128));
        a1.add(new MovementAction(jeanette, 272, 176));
        a1.add(new MovementAction(jeanette, 288, 176));
        a1.add(new MovementAction(jeanette, 288, 448));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new CharacterLeaveAction(jeanette, this));
        a1.add(new SpeechAction(erica, "(Well, that was odd. I hope I didn't leave my IRC client up.)"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent5_7());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent5_6() {
        Array<Action> a1 = new Array<>();
        a1.add(new TrustChangeAction(jeanette, -5));
        a1.add(new SpeechAction(jeanette, "...Okay then!, best of luck, sweetie."));
        a1.add(new LookAction(jeanette, "down"));
        a1.add(new WaitAction(2));
        a1.add(new CharacterLeaveAction(jeanette, this));
        a1.add(new SpeechAction(erica, "(Well, that was odd.)"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent5_7());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent5_7() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(Jeanette is my boss from work.)"));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return jeanette.getTrust() > 0;
            }
        }, new SpeechAction(erica, "(She seems to trust me.)")));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return jeanette.getTrust() == 0;
            }
        }, new SpeechAction(erica, "(I'm not sure whether she trusts me, though.)")));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return jeanette.getTrust() <= 0;
            }
        }, new SpeechAction(erica, "(I don't think she trusts me, though.)")));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent6_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent6_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(It seems I've been able to get in to an IRC conversation between \"hypothermic\" and \"scintilla\")"));
        a1.add(new NarrativeAction(this, "\"<hypothermic> You say they're finding out the plans for GCHQ's surveillance project?\""));
        a1.add(new NarrativeAction(this, "\"<scintilla> Yeah, I saw some of their conversations, darling.\""));
        a1.add(new NarrativeAction(this, "\"<hypothermic> If that's true, they may be trying to stop it from being put in place.\""));
        a1.add(new NarrativeAction(this, "\"<scintilla> Yeah, that would totally stop you from carrying out your dastardly plans, too, smoochum.\""));
        a1.add(new NarrativeAction(this, "\"<hypothermic> *Our* dastardly plans\""));
        a1.add(new NarrativeAction(this, "\"<scintilla> Oh, I'm sorry you want me to have *that* much involvement, diddums.\""));
        a1.add(new NarrativeAction(this, "\"<hypothermic> Anyway, we're getting off topic. We need to stop them from stopping GCHQ if we're to boost profits - that surveillance program could benefit us so much.\""));
        a1.add(new NarrativeAction(this, "\"<scintilla> Very well, I'll do my best, darling.\""));
        a1.add(new NarrativeAction(this, "\"scintilla (scintilla@users.undernet.org) left IRC: Quit.\""));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return humphrey.getTrust() <= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent7_1());
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent8_1());
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent7_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new NarrativeAction(this, "*The doorbell rings.*"));
        a1.add(new SpeechAction(erica, "(I'm really not sure why more people don't use that.)"));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes");
        ans1.add("Yes, but leave it on latch");
        ans1.add("No");
        a1.add(new QuestionAction(this, "Answer the door?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent7_2());
                        break;
                    case 1:
                        eventQueue.addEvent(GameScreen.this.createEvent7_3());
                        break;
                    case 2:
                        humphrey.setTrust(humphrey.getTrust() - 20);
                        robyn.setTrust(robyn.getTrust() - 5);
                        eventQueue.addEvent(GameScreen.this.createEvent8_1());
                        break;
                }
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent7_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 432));
        a1.add(new CharacterEnterAction(humphrey, this));
        a1.add(new TrustChangeAction(humphrey, 2));
        a1.add(new SpeechAction(humphrey, "Hrm, hrm, good evening Erica."));
        a1.add(new SpeechAction(humphrey, "Hrm, I have received reports that you have been acting a little out of line recently, hrm."));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes, and I'll correct my behaviour immediately.");
        ans1.add("When?");
        ans1.add("No!");
        a1.add(new AskAction(humphrey, "Is this true, hrm?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        humphrey.setTrust(humphrey.getTrust() + 5);
                        break;
                    case 1:
                        humphrey.setTrust(humphrey.getTrust() - 5);
                        break;
                    case 2:
                        humphrey.setTrust(humphrey.getTrust() - 10);
                        break;
                }
            }
        }));
        a1.add(new SpeechAction(humphrey, "...Hrm."));
        a1.add(new WaitAction(1));
        a1.add(new SpeechAction(humphrey, "I see. Yes yes."));
        a1.add(new WaitAction(1));
        a1.add(new SpeechAction(humphrey, "I shall make sure to let the higher-ups know, hrm."));
        a1.add(new LookAction(humphrey, "down"));
        a1.add(new WaitAction(2));
        a1.add(new CharacterLeaveAction(humphrey, this));
        a1.add(new SpeechAction(erica, "..."));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new SpeechAction(erica, "(I wonder what that was all about.)"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent8_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent7_3() {
        Array<Action> a1 = new Array<>();
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 448));
        a1.add(new TrustChangeAction(humphrey, -10));
        a1.add(new SpeechAction(erica, "(Hm, there's no one there.)"));
        a1.add(new SpeechAction(erica, "(Perhaps it was a prank call or something.)"));
        a1.add(new WaitAction(2));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent8_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent8_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(It seems I've been able to get in to an IRC conversation between \"antishock\" and \"scintilla\")"));
        a1.add(new NarrativeAction(this, "\"<scintilla> I think she's onto us.\""));
        a1.add(new NarrativeAction(this, "\"<antishock> You know I quit this, right?\""));
        a1.add(new NarrativeAction(this, "\"<antishock> I'm not involving myself in any of Insistence's bullshit any more.\""));
        a1.add(new NarrativeAction(this, "\"<scintilla> Even so, sweetie, it may affect you, since you were initially part of it.\""));
        a1.add(new NarrativeAction(this, "\"<antishock> I know. They just irritate me, so much, you know?\""));
        a1.add(new NarrativeAction(this, "\"<scintilla> If she is causing you that much trouble, just go and kill her already.\""));
        a1.add(new NarrativeAction(this, "\"<antishock> Oh, I will, believe me.\""));
        a1.add(new NarrativeAction(this, "\"antishock (antishock@users.undernet.org) left IRC: Quit.\""));
        a1.add(new NarrativeAction(this, "\"<scintilla> Wait, I didn't mean...\""));
        a1.add(new WaitAction(3));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes");
        ans1.add("No");
        a1.add(new AskAction(
                erica,
                "(I could also listen to a conversation between \"evilgenius\" and \"hypothermic\", should I do so?)",
                ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent9_1());
                        break;
                    case 1:
                        if (humphrey.getTrust() <= 0)
                            eventQueue.addEvent(GameScreen.this.createEvent11_1());
                        else
                            eventQueue.addEvent(GameScreen.this.createEvent12_1());
                        break;
                }
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent9_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(It seems I've been able to get in to an IRC conversation between \"evilgenius\" and \"hypothermic\")"));
        a1.add(new NarrativeAction(this, "\"<evilgenius> So that's it then, you're going to kill her.\""));
        a1.add(new NarrativeAction(this, "\"<evilgenius> You realise she's helping this country into a better state?\""));
        a1.add(new NarrativeAction(this, "\"<hypothermic> What, with more terrorists?\""));
        a1.add(new NarrativeAction(this, "\"<evilgenius> ...\""));
        a1.add(new NarrativeAction(this, "\"<evilgenius> You know what, it's not even worth speaking to someone like you.\""));
        a1.add(new NarrativeAction(this, "\"evilgenius (evilgenius@users.undernet.org) left IRC: Quit.\""));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(...)"));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(Were they talking about me?)"));
        a1.add(new WaitAction(3));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent10_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent10_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new NarrativeAction(this, "*Helena enters, bleeding*"));
        a1.add(new NameAction(helena));
        a1.add(new CharacterEnterAction(helena, this));
        a1.add(new SpeechAction(helena, "Ugh.."));
        a1.add(new SpeechAction(erica, "Helena!"));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 432));
        a1.add(new SpeechAction(helena, "Er-ic-a..."));
        a1.add(new SpeechAction(erica, "Stay with me Helena, you can make it!"));
        a1.add(new NarrativeAction(this, "*Helena smiles weakly then shakes her head*"));
        a1.add(new SpeechAction(helena, "Eric-a... they're... onto us..."));
        a1.add(new NarrativeAction(this, "*tears are streaming down Erica's face*"));
        a1.add(new SpeechAction(erica, "Who, Helena? Who is onto us?"));
        a1.add(new SpeechAction(helena, "White...hair...long...coat...short... do not trust him."));
        a1.add(new SpeechAction(helena, "Ugh."));
        a1.add(new TrustChangeAction(helena, -200));
        a1.add(new SpeechAction(erica, "HELENA!"));
        a1.add(new SpeechAction(erica, "(and with that, Helena hangs limp in my arms, never to move again.)"));
        a1.add(new SpeechAction(erica, "(It seems it was not me they were talking about, but Helena.)"));
        a1.add(new SpeechAction(erica, "(But they may soon come for me.)"));
        a1.add(new SpeechAction(erica, "(I close Helena's eyes, and put her body in the basement until I can organise a proper burial.)"));
        a1.add(new CharacterLeaveAction(helena, this));
        a1.add(new SpeechAction(erica, "(It's gone by morning.)"));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new SpeechAction(erica, "..."));
        a1.add(new WaitAction(3));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent12_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent11_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new NarrativeAction(this, "*Helena enters, bleeding*"));
        a1.add(new NameAction(helena));
        a1.add(new CharacterEnterAction(helena, this));
        a1.add(new SpeechAction(helena, "Ugh.."));
        a1.add(new SpeechAction(erica, "Helena!"));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 432));
        a1.add(new SpeechAction(helena, "Er-ic-a..."));
        a1.add(new SpeechAction(erica, "Stay with me Helena, you can make it!"));
        a1.add(new NarrativeAction(this, "*Helena smiles weakly then shakes her head*"));
        a1.add(new SpeechAction(helena, "Eric-a... they're... onto us..."));
        a1.add(new NarrativeAction(this, "*tears are streaming down Erica's face*"));
        a1.add(new SpeechAction(erica, "Who, Helena? Who is onto us?"));
        a1.add(new SpeechAction(helena, "Long...blonde...hair...blue...eyes... do not trust her."));
        a1.add(new SpeechAction(helena, "Ugh."));
        a1.add(new TrustChangeAction(helena, -200));
        a1.add(new SpeechAction(erica, "HELENA!"));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(and with that, Helena hangs limp in my arms, never to move again.)"));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(It seems it was not me they were talking about, but Helena.)"));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(But they may soon come for me.)"));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(I close Helena's eyes, and put her body in the basement until I can organise a proper burial.)"));
        a1.add(new WaitAction(3));
        a1.add(new CharacterLeaveAction(helena, this));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(erica, "(It's gone by morning.)"));
        a1.add(new WaitAction(3));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new LookAction(erica, "up"));
        a1.add(new SpeechAction(erica, "..."));
        a1.add(new WaitAction(3));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent12_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent12_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(erica, "(It seems disruption is online again.)"));
        a1.add(new SpeechAction(erica, "(I should talk to him.)"));
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(this, "\"<entropy> Hey, you have to get that sorted out.\""));
        a1.add(new NarrativeAction(this, "\"<disruption> Me mam says she's gettin te upgrade next month\""));
        a1.add(new NarrativeAction(this, "\"<entropy> Anyway, we have more pressing matters to talk about.\""));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return helena.getTrust() == -100;
            }
        }, new NarrativeAction(this, "\"<entropy> dissent is dead.\"")));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return helena.getTrust() == -100;
            }
        }, new NarrativeAction(this, "\"<disruption> Yer serious?\"")));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return helena.getTrust() != -100;
            }
        }, new NarrativeAction(this, "\"<entropy> Of course I'm serious.\"")));
        a1.add(new NarrativeAction(this, "\"<entropy> We have to shut down that gchq operation asap.\""));
        a1.add(new NarrativeAction(this, "\"<disruption> I gotcha.\""));
        a1.add(new SpeechAction(erica, "(I can tell he's smilling behind his screen.)"));
        a1.add(new SpeechAction(erica, "(We're going to do this tonight.)"));
        a1.add(new NarrativeAction(this, "*The doorbell rings.*"));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes");
        ans1.add("No");
        a1.add(new QuestionAction(this, "Answer it?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        eventQueue.addEvent(GameScreen.this.createEvent13_1());
                        break;
                    case 1:
                        eventQueue.addEvent(GameScreen.this.createEvent13_2());
                        break;
                }
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent13_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 432));
        a1.add(new WaitAction(2));
        a1.add(new CharacterEnterAction(humphrey, this));
        a1.add(new MovementAction(humphrey, 224, 448));
        a1.add(new MovementAction(humphrey, 224, 384));
        a1.add(new MovementAction(humphrey, 352, 384));
        a1.add(new MovementAction(humphrey, 352, 448));
        a1.add(new MovementAction(humphrey, 288, 448));
        a1.add(new WaitAction(2));
        a1.add(new LookAction(humphrey, "up"));
        a1.add(new SpeechAction(humphrey, "Hrm, it seems you've been doing fine, Erica."));
        a1.add(new SpeechAction(humphrey, "My apologies for the sudden intrusion, but I must leave already, hrm."));
        a1.add(new SpeechAction(erica, "...?"));
        a1.add(new LookAction(humphrey, "down"));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(humphrey, "Farewell, Erica."));
        a1.add(new TrustChangeAction(humphrey, 10));
        a1.add(new CharacterLeaveAction(humphrey, this));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 304, 128));
        a1.add(new SpeechAction(erica, "(Such a weird old chap, Humphrey.)"));
        a1.add(new NameAction(humphrey));
        a1.add(new SpeechAction(erica, "(I wonder what he was after this time.)"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent13_3());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent13_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new TrustChangeAction(humphrey, -5));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent13_3());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent13_3() {
        Array<Action> a1 = new Array<>();
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return humphrey.getTrust() <= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent14_1());
            }
        }, new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return stephen.getTrust() <= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent15_1());
            }
        }, new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return robyn.getTrust() <= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent16_1());
            }
        }, new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return robyn.getTrust() >= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent17_1());
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent17_1());
            }
        })))));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent14_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new CharacterEnterAction(stephen, this));
        a1.add(new SpeechAction(stephen, "Good evening, Erica."));
        a1.add(new SpeechAction(erica, "...?"));
        a1.add(new SpeechAction(erica, "(How on earth did he get in?)"));
        a1.add(new SpeechAction(stephen, "A man named Humphrey suggested I come to see you."));
        a1.add(new SpeechAction(stephen, "I heard you have been up to some somewhat... suspicious activity."));
        Array<String> ans1 = new Array<>();
        ans1.add("Yes");
        ans1.add("No");
        a1.add(new AskAction(stephen, "Have you?", ans1).addListener(new AnswerListener() {
            @Override
            public void onAnswer(int answerIndex) {
                switch (answerIndex) {
                    case 0:
                        stephen.setTrust(stephen.getTrust() + 10);
                        stephen.say("Well, at least we can say you're honest.");
                        break;
                    case 1:
                        stephen.setTrust(stephen.getTrust() - 10);
                        humphrey.setTrust(humphrey.getTrust() - 5);
                        stephen.say("I have reasoning to believe otherwise.");
                        break;
                }
            }
        }));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return humphrey.getTrust() <= 10;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent14_2());
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent14_3());
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent14_3() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(stephen, "Keep an eye on your actions, Erica."));
        a1.add(new CharacterLeaveAction(stephen, GameScreen.this));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return stephen.getTrust() <= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent15_1());
            }
        }, new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return robyn.getTrust() <= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent16_1());
            }
        }, new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return robyn.getTrust() >= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent17_1());
            }
        }))));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent14_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new NarrativeAction(GameScreen.this, "BANG."));
        a1.add(new SpeechAction(stephen, "Goodbye, Erica."));
        a1.add(new SpeechAction(erica, "(So this is where it ends.)"));
        a1.add(new SpeechAction(erica, "(Goodbye, world.)"));
        a1.add(new NarrativeAction(GameScreen.this, "--- BAD ENDING. Thanks for playing. ---"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                end();
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent15_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new CharacterEnterAction(alicia, this));
        a1.add(new SpeechAction(alicia, "Hiya!"));
        a1.add(new SpeechAction(erica, "(I really have no idea how she got in, but she scares me)"));
        a1.add(new SpeechAction(alicia, "We're going to make this quick and painless, so don't ya fret!"));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return jeanette.getTrust() >= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent15_2());
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent15_3());
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent15_3() {
        Array<Action> a1 = new Array<>();
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(GameScreen.this, "BANG!"));
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(GameScreen.this, "*Erica slowly bleeds out from the bullet wound as Alicia walks back out into the snow.*"));
        a1.add(new WaitAction(3));
        a1.add(new LookAction(alicia, "down"));
        a1.add(new WaitAction(3));
        a1.add(new CharacterLeaveAction(alicia, GameScreen.this));
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(GameScreen.this, "--- BAD ENDING. Thanks for playing. ---"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                end();
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent15_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new MovementAction(alicia, 288, 432));
        a1.add(new CharacterEnterAction(jeanette, GameScreen.this));
        a1.add(new SpeechAction(jeanette, "Darling, I've got this one!"));
        a1.add(new NarrativeAction(GameScreen.this, "BANG!"));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(alicia, "Ugh..."));
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(GameScreen.this, "*Alicia drops to the floor, dead.*"));
        a1.add(new WaitAction(3));
        a1.add(new SpeechAction(jeanette, "Sweetie, I'm so glad you're okay - I arrived right in the nick of time!"));
        a1.add(new SpeechAction(jeanette, "Right, I have urgent business to be attending to, so you be a little more careful, darling, okay?"));
        a1.add(new SpeechAction(erica, "..."));
        a1.add(new SpeechAction(erica, "(Blimey, that caught me by surprise.)"));
        a1.add(new WaitAction(2));
        a1.add(new CharacterLeaveAction(jeanette, GameScreen.this));
        a1.add(new WaitAction(3));
        a1.add(new CharacterLeaveAction(alicia, GameScreen.this));
        a1.add(new WaitAction(2));
        a1.add(new SpeechAction(erica, "(Let's hope whoever wants me dead doesn't have enough people to try that again in a hurry.)"));
        a1.add(new WaitAction(3));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent15_4());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent15_4() {
        Array<Action> a1 = new Array<>();
        a1.add(new CharacterEnterAction(drew, GameScreen.this));
        a1.add(new SpeechAction(drew, "Hey, missy."));
        a1.add(new SpeechAction(drew, "The name's Drew."));
        a1.add(new NameAction(drew));
        a1.add(new SpeechAction(drew, "You'd do well to remember that, since it's the last name you'll be hearing."));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return vincent.getTrust() >= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent15_5());
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent15_6());
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent15_6() {
        Array<Action> a1 = new Array<>();
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(GameScreen.this, "BANG!"));
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(GameScreen.this, "*Erica slowly bleeds out from the bullet wound as Drew walks back out into the snow.*"));
        a1.add(new WaitAction(3));
        a1.add(new LookAction(drew, "down"));
        a1.add(new WaitAction(3));
        a1.add(new CharacterLeaveAction(drew, GameScreen.this));
        a1.add(new WaitAction(3));
        a1.add(new NarrativeAction(GameScreen.this, "--- BAD ENDING. Thanks for playing. ---"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                end();
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent15_5() {
        Array<Action> a1 = new Array<>();
        a1.add(new CharacterEnterAction(vincent, GameScreen.this));
        a1.add(new NarrativeAction(GameScreen.this, "BLAM!"));
        a1.add(new SpeechAction(vincent, "Got 'im."));
        a1.add(new SpeechAction(vincent, "I'm, um, I'm very sorry for the hassle Miss Erica. I'll make sure to take him away immediately and get him in prison."));
        a1.add(new CharacterLeaveAction(drew, GameScreen.this));
        a1.add(new MovementAction(vincent, 288, 448));
        a1.add(new WaitAction(2));
        a1.add(new CharacterLeaveAction(vincent, GameScreen.this));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return robyn.getTrust() >= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent16_1());
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent17_1());
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent16_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return robyn.getTrust() >= 0;
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                if (vincent.getTrust() >= 3) {
                    eventQueue.addEvent(createEvent16_2());
                }
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent17_1());
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent16_2() {
        Array<Action> a1 = new Array<>();
        a1.add(new SpeechAction(vincent, "Good evening Miss Erica, I came to drop off the documents you requested."));
        a1.add(new SpeechAction(erica, "Thank you, Vincent."));
        a1.add(new NarrativeAction(GameScreen.this, "*Erica takes the documents from Vincent and places them on her desk*"));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 288, 448));
        a1.add(new LookAction(erica, "up"));
        a1.add(new MovementAction(erica, 288, 176));
        a1.add(new MovementAction(erica, 272, 176));
        a1.add(new MovementAction(erica, 272, 128));
        a1.add(new MovementAction(erica, 288, 128));
        a1.add(new ObtainItemAction(GameScreen.this, "gchq_document_02"));
        a1.add(new SpeechAction(vincent, "I, uh, remember that's some sensitive information, Miss, so be careful what you do with it."));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent17_1());
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent17_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new NarrativeAction(this, "\"<disruption> Top o' the mornin' to ye, entropy\""));
        a1.add(new NarrativeAction(this, "\"<entropy> Good morning, disruption.\""));
        a1.add(new NarrativeAction(this, "\"<entropy> And what a morning it's been.\""));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return GameScreen.this.hasItem("gchq_document_01") && GameScreen.this.hasItem("gchq_document_02");
            }
        }, new NarrativeAction(this, "\"<entropy> I have the documents. They launch today, so we should be able to take them down.\""), new NarrativeAction(this, "--- SATISFACTORY ENDING. Thanks for playing. ---")));
        a1.add(new ConditionalAction(new Condition() {
            @Override
            public boolean isTrue() {
                return GameScreen.this.hasItem("gchq_document_01") && GameScreen.this.hasItem("gchq_document_02");
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                eventQueue.addEvent(createEvent18_1());
            }
        }, new AbstractRunOnceAction() {
            @Override
            public void run() {
                end();
            }
        }));
        return new Event(new ActionQueue(a1));
    }

    private Event createEvent18_1() {
        Array<Action> a1 = new Array<>();
        a1.add(new NarrativeAction(GameScreen.this, "And so, Erica and Shane launched all hell on the GCHQ servers that morning, and their surveillance program was ruined."));
        a1.add(new NarrativeAction(GameScreen.this, "The CPUs on their machines burned out, and the internet was free once again."));
        a1.add(new NarrativeAction(GameScreen.this, "--- GOOD ENDING. Thanks for playing. ---"));
        a1.add(new AbstractRunOnceAction() {
            @Override
            public void run() {
                end();
            }
        });
        return new Event(new ActionQueue(a1));
    }

    private void end() {
        showMessage("Trust scores (These may look pretty weird, as they're mostly intended for internal use!):");
        showMessage("Vincent: " + vincent.getTrust());
        showMessage("Humphrey: " + humphrey.getTrust());
        showMessage("Stephen (hypothermic): " + stephen.getTrust());
        showMessage("Alicia (excelsior): " + alicia.getTrust());
        showMessage("Jeanette (scintilla): " + jeanette.getTrust());
        showMessage("Robyn (antishock): " + robyn.getTrust());
        showMessage("Drew (evilgenius): " + drew.getTrust());
        showMessage("Helena (dissent): " + helena.getTrust());
        showMessage("Characters you do not meet in person do not have trust scores.");
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
