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

    public Question showQuestion(String message, String... answers) {
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
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(Hello.)"),
                new SpeechAction(erica, "(I know you're there.)"),
                new SpeechAction(erica, "(I know you're watching me.)"),
                new SpeechAction(erica, "(I'm very observant, you see.)"),
                new SpeechAction(erica, "(...I have to be.)"),
                new NameAction(erica),
                new SpeechAction(erica, "(My name is Erica, although those who know me through the internet know me as entropy.)"),
                new SpeechAction(erica, "(My job? It's not important.)"),
                new SpeechAction(erica, "(Or at least, I can't tell you, but maybe you can figure it out.)"),
                new SpeechAction(erica, "(I work from home most days at the moment.)"),
                new SpeechAction(erica, "(It's winter right now, and the snow has been piling up in thick sheets.)"),
                new SpeechAction(erica, "(That makes it difficult to get places.)"),
                new SpeechAction(erica, "(I have to travel a fair way to get to the office, so it makes it easier if I work from home.)"),
                new SpeechAction(erica, "(At least in some respects.)"),
                new SpeechAction(erica, "(At least at the office, I am usually left undisturbed.)"),
                new SpeechAction(erica, "(You see, because of what I do, there's a lot of people who would rather not have me around.)"),
                new SpeechAction(erica, "(While my actions may be to the benefit of my country, some don't see it that way.)"),
                new NarrativeAction(this, "*There comes a knocking at the door.*"),
                new QuestionAction(this, "Do you answer?", "Yes", "Yes, but leave it on latch", "No").addListener(new AnswerListener() {
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

                })
        ));
    }

    private Event createEvent2_1() {
        return new Event(new ActionQueue(
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 432),
                new CharacterEnterAction(vincent, this),
                new TrustChangeAction(vincent, 2),
                new SpeechAction(vincent, "Good evening Miss Erica, I came to drop off the documents you requested."),
                new SpeechAction(erica, "Ah, thank you for going to the trouble to get these, Vincent."),
                new NameAction(vincent),
                new NarrativeAction(this, "*Erica takes the documents from Vincent and places them on her desk*"),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new MovementAction(vincent, 288, 360),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 344),
                new ObtainItemAction(this, "gchq_document_01"),
                new SpeechAction(vincent, "I, uh, remember that's some sensitive information, Miss, so be careful what you do with it."),
                new AskAction(
                        vincent,
                        "If you don't mind my asking, what would you be using them for?",
                        "Make up a business-related matter",
                        "Explain they piqued your curiosity",
                        "Tell him that your reasoning is confidential").addListener(new AnswerListener() {
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
                })
        ));
    }
    private Event createEvent2_2() {
        return new Event(new ActionQueue(
                new TrustChangeAction(vincent, -1),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 448),
                new SpeechAction(vincent, "Miss Erica?"),
                new SpeechAction(erica, "Vincent?"),
                new NameAction(vincent),
                new SpeechAction(vincent, "I, uh, brought the papers you requested."),
                new SpeechAction(erica, "Ah, my apologies for making you come all this way to deliver them in the snow. They are quite important, you see."),
                new SpeechAction(vincent, "Ah, yes. Allow me to pass them to you."),
                new NarrativeAction(this, "*Vincent awkwardly passes the documents around the side of the door, and Erica takes them under one arm*"),
                new ObtainItemAction(this, "gchq_document_01"),
                new AskAction(
                        vincent,
                        "(In a hushed tone) May I request the reasoning behind your request, Miss?",
                        "Make up a business-related matter", "Explain that they piqued your curiosity",
                        "Tell him that your reasoning is confidential").addListener(new AnswerListener() {
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
                })
        ));
    }

    private Event createEvent2_3() {
        return new Event(new ActionQueue(
                new TrustChangeAction(vincent, -2),
                new QuestionAction(
                        this,
                        "*An envelope is passed through the letterbox, landing on the mat*",
                        "Open the envelope",
                        "Leave the envelope until later",
                        "Decide the delivery is suspicious and throw the envelope away"
                ).addListener(new AnswerListener() {
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
                })
        ));
    }

    private Event createEvent3_1() {
        return new Event(new ActionQueue(
                new TrustChangeAction(vincent, 2),
                new SpeechAction(erica, "They are quite important for the Aurora project, and they may cause it's success or downfall."),
                new SpeechAction(vincent, "Ah, I see."),
                new SpeechAction(vincent, "Well, Miss Erica, I shall be going, then."),
                new SpeechAction(vincent, "Let me know if you need me!"),
                new NarrativeAction(this, "*Vincent smiles at Erica, performs a short bow, and then trudges off into the snow*"),
                new MovementAction(vincent, 288, 448),
                new CharacterLeaveAction(vincent, this),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent3_8());
                        eventQueue.addEvent(createEvent4_1());
                    }
                }
        ));
    }

    private Event createEvent3_2() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "It's part of the Aurora project - it's quite important to me."),
                new SpeechAction(vincent, "I...see."),
                new SpeechAction(vincent, "Well, I won't question you further, Miss, I'm sure I've wasted enough of your time already."),
                new NarrativeAction(this, "*Vincent leaves*"),
                new MovementAction(vincent, 288, 448),
                new CharacterLeaveAction(vincent, this),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent3_8());
                        eventQueue.addEvent(createEvent4_1());
                    }
                }
        ));
    }

    private Event createEvent3_3() {
        return new Event(new ActionQueue(
                new TrustChangeAction(vincent, -2),
                new SpeechAction(erica, "That information is confidential, I'm afraid I can't tell you."),
                new SpeechAction(vincent, "Ah, my apologies for asking, Miss."),
                new NarrativeAction(this, "*Vincent leaves, trudging away, leaving deep footprints in the snow*"),
                new MovementAction(vincent, 288, 448),
                new CharacterLeaveAction(vincent, this),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent3_8());
                        eventQueue.addEvent(createEvent4_1());
                    }
                }
        ));
    }

    private Event createEvent3_4() {
        return new Event(new ActionQueue(
                new TrustChangeAction(vincent, -4),
                new SpeechAction(erica, "I'm afraid I can't tell you that."),
                new NarrativeAction(this, "*Vincent's eyes narrow*"),
                new SpeechAction(vincent, "I see."),
                new TrustChangeAction(jeanette, -2),
                new NarrativeAction(this, "*Vincent leaves*"),
                new MovementAction(vincent, 288, 448),
                new CharacterLeaveAction(vincent, this),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent3_8());
                        eventQueue.addEvent(createEvent4_1());
                    }
                }
        ));
    }

    private Event createEvent3_5() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(Ah, it's those documents I requested from Vincent."),
                new SpeechAction(erica, "(I'd better put these somewhere safe)"),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 448),
                new ObtainItemAction(this, "gchq_document_01"),
                new WaitAction(2),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent3_8());
                        eventQueue.addEvent(createEvent4_1());
                    }
                }
        ));
    }

    private Event createEvent3_6() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(I'll deal with this later.)"),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 448),
                new ObtainItemAction(this, "gchq_envelope_01"),
                new WaitAction(2),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent4_1());
                        eventQueue.addEvent(createEvent4_3());
                    }
                }
        ));
    }

    private Event createEvent3_7() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(This looks quite suspicious, it's probably best I dispose of this.)"),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 448),
                new WaitAction(2),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 320, 176),
                new MovementAction(erica, 320, 128),
                new WaitAction(2),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent4_1());
                    }
                }
        ));
    }

    private Event createEvent3_8() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(Vincent is one of my subordinates at work.)"),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return vincent.getTrust() > 0;
                    }
                }, new SpeechAction(erica, "(He seems to trust me.)")),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return vincent.getTrust() == 0;
                    }
                }, new SpeechAction(erica, "(I'm not sure whether he trusts me, though.)")),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return vincent.getTrust() <= 0;
                    }
                }, new SpeechAction(erica, "(I don't think he trusts me, though.)"))
        ));
    }

    private Event createEvent4_1() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(I spend most of my life looking at posts on BBSes and talking to people on IRC.)"),
                new SpeechAction(erica, "(Due to my work, I have access to a lot of information that others do not.)"),
                new SpeechAction(erica, "(Outside of work, I'm part of an organisation known as Anomaly.)"),
                new SpeechAction(erica, "(We collect information on GCHQ operations and expose their schemes so that people may protest against those that oppose basic human rights.)"),
                new SpeechAction(erica, "(The right to privacy, ha, what a joke.)"),
                new SpeechAction(erica, "(And not a funny one, either.)"),
                new SpeechAction(erica, "(If you've seen some of the documents...)"),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return hasItem("gchq_document_01");
                    }
                },
                        new AskAction(
                                erica,
                                "(Anyway, should I get these documents typed up?)",
                                "Yes",
                                "No").addListener(new AnswerListener() {
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
                        }),
                        new AbstractRunOnceAction() {
                            @Override
                            public void run() {
                                eventQueue.addEvent(createEvent5_1());
                            }
                        })
        ));
    }

    private Event createEvent4_2() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(Let's get these documents typed up so we can share them with the rest of the members of Anomaly.)"),
                new WaitAction(3),
                new TrustChangeAction(jeanette, -10),
                new NarrativeAction(this, "\"<entropy> I was able to gather some more information from GCHQ, so that might be of some use on understanding their current project.\""),
                new NarrativeAction(this, "\"<disruption> Ah, yous did, did yous? That'll be perfect, lass, ayv'e almost got enough information te know when we can disrupt this thing.\""),
                new NarrativeAction(this, "\"<entropy> disruption: I'm uploading the documents to the BBS right now. You should have access to them in a couple of hours.\""),
                new NarrativeAction(this, "\"<disruption> entropy: Tha' it be, if me dial-up stays stable.\""),
                new NarrativeAction(this, "\"<entropy> disruption: You still haven't got that sorted?\""),
                new NarrativeAction(this, "\"disruption (disruption@users.undernet.org) left IRC: Ping timeout\""),
                new SpeechAction(erica, "(disruption lives in Ireland, and apparently that makes his internet inherently terrible.)"),
                new SpeechAction(erica, "(While his ability usually makes up for that, we have lost communication with him for extended periods of time before, at sometimes crucial points.)"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent5_1());
                    }
                }
        ));
    }

    private Event createEvent4_3() {
        return new Event(new ActionQueue(
                new WaitAction(3),
                new NarrativeAction(this, "\"<entropy> I received an envelope today - I wasn't sure of the sender, but I haven't opened it yet.\""),
                new NarrativeAction(this, "\"<entropy> I'm not sure whether to trust it.\""),
                new NarrativeAction(this, "\"<dissent> entropy: I wouldn't - they've been onto us a lot recently.\""),
                new NarrativeAction(this, "\"<garbagekid> Y0, 4|\\|y1 901- w4r3z?\""),
                new NarrativeAction(this, "\"<entropy> dissent: Yes, I noticed that - you haven't really been online much recently.\""),
                new NarrativeAction(this, "\"garbagekid (garbagekid@users.undernet.org) left IRC: Kicked"),
                new NarrativeAction(this, "\"<dissent> entropy: I'm thinking of just quitting it all. You know, we have our whole lives ahead of us, right?\""),
                new NarrativeAction(this, "\"<entropy> dissent: Of corse, it's just -- I don't know. If no one else does it, what will happen to the state of the country?\""),
                new SpeechAction(erica, "(dissent is my next door neighbour, Helena.)"),
                new SpeechAction(erica, "(She got into this before I did, but I have since been a more active part of Anomaly.)"),
                new SpeechAction(erica, "(She seems to have been getting tired of it recently, and wants to live a normal life.)"),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return hasItem("gchq_envelope_01");
                    }
                }, new AskAction(
                        erica,
                        "Now, should I do anything with that envelope?",
                        "Open it",
                        "Dispose of it",
                        "Do nothing").addListener(new AnswerListener() {
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
                }))
        ));
    }

    private Event createEvent4_5() {
        return new Event(new ActionQueue(
                new LoseItemAction(this, "gchq_envelope_01"),
                new ObtainItemAction(this, "gchq_document_01"),
                new NarrativeAction(this, "*Erica found GCHQ documents inside the envelope.*"),
                new SpeechAction(erica, "(Ah, these must be from Vincent.)"),
                new AskAction(
                        erica,
                        "(Anyway, should I get these documents typed up?)",
                        "Yes",
                        "No").addListener(new AnswerListener() {
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
                }),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent5_1());
                    }
                }
        ));
    }

    private Event createEvent4_6() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(It's probably best to dispose of this. Helena was probably right.)"),
                new MovementAction(erica, 288, 448),
                new LookAction(erica, "up"),
                new WaitAction(2),
                new NarrativeAction(this, "*Erica disposed of the documents*"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent5_1());
                    }
                }
        ));
    }

    private Event createEvent5_1() {
        return new Event(new ActionQueue(
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
                })
        ));
    }

    private Event createEvent5_2() {
        return new Event(new ActionQueue(
                new NarrativeAction(this, "*There is a knock at the door*"),
                new QuestionAction(
                        this,
                        "Answer it?",
                        "Yes",
                        "Yes, but leave it on latch",
                        "No"
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
                })
        ));
    }

    private Event createEvent5_3() {
        return new Event(new ActionQueue(
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 432),
                new CharacterEnterAction(jeanette, this),
                new TrustChangeAction(jeanette, 5),
                new SpeechAction(erica, "Jeanette?"),
                new NameAction(jeanette),
                new NarrativeAction(this, "*Jeanette stomps the snow from her shoes, which scatters across the mat*"),
                new SpeechAction(jeanette, "Hello darling, I came to visit to see how you were getting on."),
                new SpeechAction(erica, "Fine, what prompted this?"),
                new SpeechAction(jeanette, "Oh, darling, you know, I just heard it from the grapevine."),
                new AskAction(jeanette, "Would you mind if I had a look at how you are getting on with work recently?", "Let her look", "Don't let her look").addListener(new AnswerListener() {
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
                })
        ));
    }

    private Event createEvent5_4() {
        return new Event(new ActionQueue(
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 432),
                new TrustChangeAction(jeanette, -2),
                new SpeechAction(erica, "Jeanette?"),
                new NameAction(jeanette),
                new SpeechAction(jeanette, "Yes dear, I just came to check on how you were getting along."),
                new SpeechAction(jeanette, "It seems you're getting along fine, so I'll just leave now. Bye!"),
                new SpeechAction(erica, "..."),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new SpeechAction(erica, "(That was odd.)"),
                new SpeechAction(erica, "(I wonder what she wanted.)"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent5_7());
                    }
                }
        ));
    }

    private Event createEvent5_5() {
        return new Event(new ActionQueue(
                new MovementAction(jeanette, 288, 176),
                new MovementAction(jeanette, 272, 176),
                new MovementAction(jeanette, 272, 128),
                new DepthChangeAction(jeanette, 1),
                new MovementAction(jeanette, 304, 128),
                new LookAction(jeanette, "up"),
                new WaitAction(3),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return random.nextBoolean();
                    }
                }, new TrustChangeAction(jeanette, 20), new TrustChangeAction(jeanette, -2)),
                new MovementAction(jeanette, 272, 128),
                new MovementAction(jeanette, 272, 176),
                new MovementAction(jeanette, 288, 176),
                new MovementAction(jeanette, 288, 432),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new CharacterLeaveAction(jeanette, this),
                new SpeechAction(erica, "(Well, that was odd. I hope I didn't leave my IRC client up.)"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent5_7());
                    }
                }
        ));
    }

    private Event createEvent5_6() {
        return new Event(new ActionQueue(
                new TrustChangeAction(jeanette, -5),
                new SpeechAction(jeanette, "...Okay then!, best of luck, sweetie."),
                new LookAction(jeanette, "down"),
                new WaitAction(2),
                new CharacterLeaveAction(jeanette, this),
                new SpeechAction(erica, "(Well, that was odd.)"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent5_7());
                    }
                }
        ));
    }

    private Event createEvent5_7() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(Jeanette is my boss from work.)"),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return jeanette.getTrust() > 0;
                    }
                }, new SpeechAction(erica, "(She seems to trust me.)")),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return jeanette.getTrust() == 0;
                    }
                }, new SpeechAction(erica, "(I'm not sure whether she trusts me, though.)")),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return jeanette.getTrust() <= 0;
                    }
                }, new SpeechAction(erica, "(I don't think she trusts me, though.)")),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent6_1());
                    }
                }
        ));
    }

    private Event createEvent6_1() {
        return new Event(new ActionQueue(
                new WaitAction(3),
                new SpeechAction(erica, "(It seems I've been able to get in to an IRC conversation between \"hypothermic\" and \"scintilla\")"),
                new NarrativeAction(this, "\"<hypothermic> You say they're finding out the plans for GCHQ's surveillance project?\""),
                new NarrativeAction(this, "\"<scintilla> Yeah, I saw some of their conversations, darling.\""),
                new NarrativeAction(this, "\"<hypothermic> If that's true, they may be trying to stop it from being put in place.\""),
                new NarrativeAction(this, "\"<scintilla> Yeah, that would totally stop you from carrying out your dastardly plans, too, smoochum.\""),
                new NarrativeAction(this, "\"<hypothermic> *Our* dastardly plans\""),
                new NarrativeAction(this, "\"<scintilla> Oh, I'm sorry you want me to have *that* much involvement, diddums.\""),
                new NarrativeAction(this, "\"<hypothermic> Anyway, we're getting off topic. We need to stop them from stopping GCHQ if we're to boost profits - that surveillance program could benefit us so much.\""),
                new NarrativeAction(this, "\"<scintilla> Very well, I'll do my best, darling.\""),
                new NarrativeAction(this, "\"scintilla (scintilla@users.undernet.org) left IRC: Quit.\""),
                new ConditionalAction(new Condition() {
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
                })
        ));
    }

    private Event createEvent7_1() {
        return new Event(new ActionQueue(
                new NarrativeAction(this, "*The doorbell rings.*"),
                new SpeechAction(erica, "(I'm really not sure why more people don't use that.)"),
                new QuestionAction(this, "Answer the door?", "Yes", "Yes, but leave it on latch", "No").addListener(new AnswerListener() {
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
                })
        ));
    }

    private Event createEvent7_2() {
        return new Event(new ActionQueue(
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 432),
                new CharacterEnterAction(humphrey, this),
                new TrustChangeAction(humphrey, 2),
                new SpeechAction(humphrey, "Hrm, hrm, good evening Erica."),
                new SpeechAction(humphrey, "Hrm, I have received reports that you have been acting a little out of line recently, hrm."),
                new AskAction(humphrey, "Is this true, hrm?", "Yes, and I'll correct my behaviour immediately.", "When?", "No!").addListener(new AnswerListener() {
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
                }),
                new SpeechAction(humphrey, "...Hrm."),
                new WaitAction(1),
                new SpeechAction(humphrey, "I see. Yes yes."),
                new WaitAction(1),
                new SpeechAction(humphrey, "I shall make sure to let the higher-ups know, hrm."),
                new LookAction(humphrey, "down"),
                new WaitAction(2),
                new CharacterLeaveAction(humphrey, this),
                new SpeechAction(erica, "..."),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new SpeechAction(erica, "(I wonder what that was all about.)"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent8_1());
                    }
                }
        ));
    }

    private Event createEvent7_3() {
        return new Event(new ActionQueue(
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 448),
                new TrustChangeAction(humphrey, -10),
                new SpeechAction(erica, "(Hm, there's no one there.)"),
                new SpeechAction(erica, "(Perhaps it was a prank call or something.)"),
                new WaitAction(2),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent8_1());
                    }
                }
        ));
    }

    private Event createEvent8_1() {
        return new Event(new ActionQueue(
                new WaitAction(3),
                new SpeechAction(erica, "(It seems I've been able to get in to an IRC conversation between \"antishock\" and \"scintilla\")"),
                new NarrativeAction(this, "\"<scintilla> I think she's onto us.\""),
                new NarrativeAction(this, "\"<antishock> You know I quit this, right?\""),
                new NarrativeAction(this, "\"<antishock> I'm not involving myself in any of Insistence's bullshit any more.\""),
                new NarrativeAction(this, "\"<scintilla> Even so, sweetie, it may affect you, since you were initially part of it.\""),
                new NarrativeAction(this, "\"<antishock> I know. They just irritate me, so much, you know?\""),
                new NarrativeAction(this, "\"<scintilla> If she is causing you that much trouble, just go and kill her already.\""),
                new NarrativeAction(this, "\"<antishock> Oh, I will, believe me.\""),
                new NarrativeAction(this, "\"antishock (antishock@users.undernet.org) left IRC: Quit.\""),
                new NarrativeAction(this, "\"<scintilla> Wait, I didn't mean...\""),
                new WaitAction(3),
                new AskAction(
                        erica,
                        "(I could also listen to a conversation between \"evilgenius\" and \"hypothermic\", should I do so?)",
                        "Yes",
                        "No").addListener(new AnswerListener() {
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
                })
        ));
    }

    private Event createEvent9_1() {
        return new Event(new ActionQueue(
                new WaitAction(3),
                new SpeechAction(erica, "(It seems I've been able to get in to an IRC conversation between \"evilgenius\" and \"hypothermic\")"),
                new NarrativeAction(this, "\"<evilgenius> So that's it then, you're going to kill her.\""),
                new NarrativeAction(this, "\"<evilgenius> You realise she's helping this country into a better state?\""),
                new NarrativeAction(this, "\"<hypothermic> What, with more terrorists?\""),
                new NarrativeAction(this, "\"<evilgenius> ...\""),
                new NarrativeAction(this, "\"<evilgenius> You know what, it's not even worth speaking to someone like you.\""),
                new NarrativeAction(this, "\"evilgenius (evilgenius@users.undernet.org) left IRC: Quit.\""),
                new WaitAction(3),
                new SpeechAction(erica, "(...)"),
                new WaitAction(3),
                new SpeechAction(erica, "(Were they talking about me?)"),
                new WaitAction(3),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent10_1());
                    }
                }
        ));
    }

    private Event createEvent10_1() {
        return new Event(new ActionQueue(
                new NarrativeAction(this, "*Helena enters, bleeding*"),
                new NameAction(helena),
                new CharacterEnterAction(helena, this),
                new SpeechAction(helena, "Ugh.."),
                new SpeechAction(erica, "Helena!"),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 432),
                new SpeechAction(helena, "Er-ic-a..."),
                new SpeechAction(erica, "Stay with me Helena, you can make it!"),
                new NarrativeAction(this, "*Helena smiles weakly then shakes her head*"),
                new SpeechAction(helena, "Eric-a... they're... onto us..."),
                new NarrativeAction(this, "*tears are streaming down Erica's face*"),
                new SpeechAction(erica, "Who, Helena? Who is onto us?"),
                new SpeechAction(helena, "White...hair...long...coat...short... do not trust him."),
                new SpeechAction(helena, "Ugh."),
                new TrustChangeAction(helena, -200),
                new SpeechAction(erica, "HELENA!"),
                new SpeechAction(erica, "(and with that, Helena hangs limp in my arms, never to move again.)"),
                new SpeechAction(erica, "(It seems it was not me they were talking about, but Helena.)"),
                new SpeechAction(erica, "(But they may soon come for me.)"),
                new SpeechAction(erica, "(I close Helena's eyes, and put her body in the basement until I can organise a proper burial.)"),
                new CharacterLeaveAction(helena, this),
                new SpeechAction(erica, "(It's gone by morning.)"),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new SpeechAction(erica, "..."),
                new WaitAction(3),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent12_1());
                    }
                }
        ));
    }

    private Event createEvent11_1() {
        return new Event(new ActionQueue(
                new NarrativeAction(this, "*Helena enters, bleeding*"),
                new NameAction(helena),
                new CharacterEnterAction(helena, this),
                new SpeechAction(helena, "Ugh.."),
                new SpeechAction(erica, "Helena!"),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 432),
                new SpeechAction(helena, "Er-ic-a..."),
                new SpeechAction(erica, "Stay with me Helena, you can make it!"),
                new NarrativeAction(this, "*Helena smiles weakly then shakes her head*"),
                new SpeechAction(helena, "Eric-a... they're... onto us..."),
                new NarrativeAction(this, "*tears are streaming down Erica's face*"),
                new SpeechAction(erica, "Who, Helena? Who is onto us?"),
                new SpeechAction(helena, "Long...blonde...hair...blue...eyes... do not trust her."),
                new SpeechAction(helena, "Ugh."),
                new TrustChangeAction(helena, -200),
                new SpeechAction(erica, "HELENA!"),
                new WaitAction(3),
                new SpeechAction(erica, "(and with that, Helena hangs limp in my arms, never to move again.)"),
                new WaitAction(3),
                new SpeechAction(erica, "(It seems it was not me they were talking about, but Helena.)"),
                new WaitAction(3),
                new SpeechAction(erica, "(But they may soon come for me.)"),
                new WaitAction(3),
                new SpeechAction(erica, "(I close Helena's eyes, and put her body in the basement until I can organise a proper burial.)"),
                new WaitAction(3),
                new CharacterLeaveAction(helena, this),
                new WaitAction(3),
                new SpeechAction(erica, "(It's gone by morning.)"),
                new WaitAction(3),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new LookAction(erica, "up"),
                new SpeechAction(erica, "..."),
                new WaitAction(3),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent12_1());
                    }
                }
        ));
    }

    private Event createEvent12_1() {
        return new Event(new ActionQueue(
                new SpeechAction(erica, "(It seems disruption is online again.)"),
                new SpeechAction(erica, "(I should talk to him.)"),
                new WaitAction(3),
                new NarrativeAction(this, "\"<entropy> Hey, you have to get that sorted out.\""),
                new NarrativeAction(this, "\"<disruption> Me mam says she's gettin te upgrade next month\""),
                new NarrativeAction(this, "\"<entropy> Anyway, we have more pressing matters to talk about.\""),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return helena.getTrust() == -100;
                    }
                }, new NarrativeAction(this, "\"<entropy> dissent is dead.\"")),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return helena.getTrust() == -100;
                    }
                }, new NarrativeAction(this, "\"<disruption> Yer serious?\"")),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return helena.getTrust() != -100;
                    }
                }, new NarrativeAction(this, "\"<entropy> Of course I'm serious.\"")),
                new NarrativeAction(this, "\"<entropy> We have to shut down that gchq operation asap.\""),
                new NarrativeAction(this, "\"<disruption> I gotcha.\""),
                new SpeechAction(erica, "(I can tell he's smilling behind his screen.)"),
                new SpeechAction(erica, "(We're going to do this tonight.)"),
                new NarrativeAction(this, "*The doorbell rings.*"),
                new QuestionAction(this, "Answer it?", "Yes", "No").addListener(new AnswerListener() {
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
                })
        ));
    }

    private Event createEvent13_1() {
        return new Event(new ActionQueue(
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 288, 432),
                new WaitAction(2),
                new CharacterEnterAction(humphrey, this),
                new MovementAction(humphrey, 224, 448),
                new MovementAction(humphrey, 224, 384),
                new MovementAction(humphrey, 352, 384),
                new MovementAction(humphrey, 352, 448),
                new MovementAction(humphrey, 288, 448),
                new WaitAction(2),
                new LookAction(humphrey, "up"),
                new SpeechAction(humphrey, "Hrm, it seems you've been doing fine, Erica."),
                new SpeechAction(humphrey, "My apologies for the sudden intrusion, but I must leave already, hrm."),
                new SpeechAction(erica, "...?"),
                new LookAction(humphrey, "down"),
                new WaitAction(3),
                new SpeechAction(humphrey, "Farewell, Erica."),
                new TrustChangeAction(humphrey, 10),
                new CharacterLeaveAction(humphrey, this),
                new MovementAction(erica, 288, 176),
                new MovementAction(erica, 272, 176),
                new MovementAction(erica, 272, 128),
                new MovementAction(erica, 304, 128),
                new SpeechAction(erica, "(Such a weird old chap, Humphrey.)"),
                new NameAction(humphrey),
                new SpeechAction(erica, "(I wonder what he was after this time.)"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent13_3());
                    }
                }
        ));
    }

    private Event createEvent13_2() {
        return new Event(new ActionQueue(
                new TrustChangeAction(humphrey, -5),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent13_3());
                    }
                }
        ));
    }

    private Event createEvent13_3() {
        return new Event(new ActionQueue(
                new ConditionalAction(new Condition() {
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
                }))))
        ));
    }

    private Event createEvent14_1() {
        return new Event(new ActionQueue(
                new CharacterEnterAction(stephen, this),
                new SpeechAction(stephen, "Good evening, Erica."),
                new SpeechAction(erica, "...?"),
                new SpeechAction(erica, "(How on earth did he get in?)"),
                new SpeechAction(stephen, "A man named Humphrey suggested I come to see you."),
                new SpeechAction(stephen, "I heard you have been up to some somewhat... suspicious activity."),
                new AskAction(stephen, "Have you?", "Yes", "No").addListener(new AnswerListener() {
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
                }),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return humphrey.getTrust() <= 10;
                    }
                }, new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(new Event(new ActionQueue(
                                new NarrativeAction(GameScreen.this, "BANG."),
                                new SpeechAction(stephen, "Goodbye, Erica."),
                                new SpeechAction(erica, "(So this is where it ends.)"),
                                new SpeechAction(erica, "(Goodbye, world.)"),
                                new NarrativeAction(GameScreen.this, "--- BAD ENDING. Thanks for playing. ---"),
                                new AbstractRunOnceAction() {
                                    @Override
                                    public void run() {
                                        end();
                                    }
                                }
                        )));
                    }
                }, new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(new Event(new ActionQueue(
                                new SpeechAction(stephen, "Keep an eye on your actions, Erica."),
                                new CharacterLeaveAction(stephen, GameScreen.this),
                                new ConditionalAction(new Condition() {
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
                                })))
                        )));
                    }
                })
        ));
    }

    private Event createEvent15_1() {
        return new Event(new ActionQueue(
                new CharacterEnterAction(alicia, this),
                new SpeechAction(alicia, "Hiya!"),
                new SpeechAction(erica, "(I really have no idea how she got in, but she scares me)"),
                new SpeechAction(alicia, "We're going to make this quick and painless, so don't ya fret!"),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return jeanette.getTrust() >= 0;
                    }
                }, new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(new Event(new ActionQueue(
                                new MovementAction(alicia, 288, 432),
                                new CharacterEnterAction(jeanette, GameScreen.this),
                                new SpeechAction(jeanette, "Darling, I've got this one!"),
                                new NarrativeAction(GameScreen.this, "BANG!"),
                                new WaitAction(3),
                                new SpeechAction(alicia, "Ugh..."),
                                new WaitAction(3),
                                new NarrativeAction(GameScreen.this, "*Alicia drops to the floor, dead.*"),
                                new WaitAction(3),
                                new SpeechAction(jeanette, "Sweetie, I'm so glad you're okay - I arrived right in the nick of time!"),
                                new SpeechAction(jeanette, "Right, I have urgent business to be attending to, so you be a little more careful, darling, okay?"),
                                new SpeechAction(erica, "..."),
                                new SpeechAction(erica, "(Blimey, that caught me by surprise.)"),
                                new WaitAction(2),
                                new CharacterLeaveAction(jeanette, GameScreen.this),
                                new WaitAction(3),
                                new CharacterLeaveAction(alicia, GameScreen.this),
                                new WaitAction(2),
                                new SpeechAction(erica, "(Let's hope whoever wants me dead doesn't have enough people to try that again in a hurry.)"),
                                new WaitAction(3),
                                new AbstractRunOnceAction() {
                                    @Override
                                    public void run() {
                                        eventQueue.addEvent(new Event(new ActionQueue(
                                                new CharacterEnterAction(drew, GameScreen.this),
                                                new SpeechAction(drew, "Hey, missy."),
                                                new SpeechAction(drew, "The name's Drew."),
                                                new NameAction(drew),
                                                new SpeechAction(drew, "You'd do well to remember that, since it's the last name you'll be hearing."),
                                                new ConditionalAction(new Condition() {
                                                    @Override
                                                    public boolean isTrue() {
                                                        return vincent.getTrust() >= 0;
                                                    }
                                                }, new AbstractRunOnceAction() {
                                                    @Override
                                                    public void run() {
                                                        eventQueue.addEvent(new Event(new ActionQueue(
                                                                new CharacterEnterAction(vincent, GameScreen.this),
                                                                new NarrativeAction(GameScreen.this, "BLAM!"),
                                                                new SpeechAction(vincent, "Got 'im."),
                                                                new SpeechAction(vincent, "I'm, um, I'm very sorry for the hassle Miss Erica. I'll make sure to take him away immediately and get him in prison."),
                                                                new CharacterLeaveAction(drew, GameScreen.this),
                                                                new MovementAction(vincent, 288, 448),
                                                                new WaitAction(2),
                                                                new CharacterLeaveAction(vincent, GameScreen.this),
                                                                new ConditionalAction(new Condition() {
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
                                                                })
                                                        )));
                                                    }
                                                }, new AbstractRunOnceAction() {
                                                    @Override
                                                    public void run() {
                                                        eventQueue.addEvent(new Event(new ActionQueue(
                                                                new WaitAction(3),
                                                                new NarrativeAction(GameScreen.this, "BANG!"),
                                                                new WaitAction(3),
                                                                new NarrativeAction(GameScreen.this, "*Erica slowly bleeds out from the bullet wound as Drew walks back out into the snow.*"),
                                                                new WaitAction(3),
                                                                new LookAction(drew, "down"),
                                                                new WaitAction(3),
                                                                new CharacterLeaveAction(drew, GameScreen.this),
                                                                new WaitAction(3),
                                                                new NarrativeAction(GameScreen.this, "--- BAD ENDING. Thanks for playing. ---"),
                                                                new AbstractRunOnceAction() {
                                                                    @Override
                                                                    public void run() {
                                                                        end();
                                                                    }
                                                                }
                                                        )));
                                                    }
                                                })
                                        )));
                                    }
                                }
                        )));
                    }
                }, new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(new Event(new ActionQueue(
                                new WaitAction(3),
                                new NarrativeAction(GameScreen.this, "BANG!"),
                                new WaitAction(3),
                                new NarrativeAction(GameScreen.this, "*Erica slowly bleeds out from the bullet wound as Alicia walks back out into the snow.*"),
                                new WaitAction(3),
                                new LookAction(alicia, "down"),
                                new WaitAction(3),
                                new CharacterLeaveAction(alicia, GameScreen.this),
                                new WaitAction(3),
                                new NarrativeAction(GameScreen.this, "--- BAD ENDING. Thanks for playing. ---"),
                                new AbstractRunOnceAction() {
                                    @Override
                                    public void run() {
                                        end();
                                    }
                                }
                        )));
                    }
                })
        ));
    }

    private Event createEvent16_1() {
        return new Event(new ActionQueue(
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return robyn.getTrust() >= 0;
                    }
                }, new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        if (vincent.getTrust() >= 3) {
                            eventQueue.addEvent(new Event(new ActionQueue(
                                    new SpeechAction(vincent, "Good evening Miss Erica, I came to drop off the documents you requested."),
                                    new SpeechAction(erica, "Thank you, Vincent."),
                                    new NarrativeAction(GameScreen.this, "*Erica takes the documents from Vincent and places them on her desk*"),
                                    new MovementAction(erica, 288, 176),
                                    new MovementAction(erica, 272, 176),
                                    new MovementAction(erica, 272, 128),
                                    new MovementAction(erica, 304, 128),
                                    new LookAction(erica, "up"),
                                    new MovementAction(vincent, 288, 360),
                                    new MovementAction(erica, 272, 128),
                                    new MovementAction(erica, 272, 176),
                                    new MovementAction(erica, 288, 176),
                                    new MovementAction(erica, 288, 344),
                                    new ObtainItemAction(GameScreen.this, "gchq_document_02"),
                                    new SpeechAction(vincent, "I, uh, remember that's some sensitive information, Miss, so be careful what you do with it."),
                                    new AbstractRunOnceAction() {
                                        @Override
                                        public void run() {
                                            eventQueue.addEvent(createEvent18_1());
                                        }
                                    }
                            )));
                        }
                    }
                }),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        eventQueue.addEvent(createEvent17_1());
                    }
                }
        ));
    }

    private Event createEvent17_1() {
        return new Event(new ActionQueue(
                new NarrativeAction(this, "\"<disruption> Top o' the mornin' to ye, entropy\""),
                new NarrativeAction(this, "\"<entropy> Good morning, disruption.\""),
                new NarrativeAction(this, "\"<entropy> And what a morning it's been.\""),
                new ConditionalAction(new Condition() {
                    @Override
                    public boolean isTrue() {
                        return GameScreen.this.hasItem("gchq_document_01") && GameScreen.this.hasItem("gchq_document_02");
                    }
                }, new NarrativeAction(this, "\"<entropy> I have the documents. They launch today, so we should be able to take them down.\""), new NarrativeAction(this, "--- SATISFACTORY ENDING. Thanks for playing. ---")),
                new ConditionalAction(new Condition() {
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
                })
        ));
    }

    private Event createEvent18_1() {
        return new Event(new ActionQueue(
                new NarrativeAction(this, "And so, Erica and Shane launched all hell on the GCHQ servers that morning, and their surveillance program was ruined."),
                new NarrativeAction(this, "The CPUs on their machines burned out, and the internet was free once again."),
                new NarrativeAction(this, "--- GOOD ENDING. Thanks for playing. ---"),
                new AbstractRunOnceAction() {
                    @Override
                    public void run() {
                        end();
                    }
                }
        ));
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
