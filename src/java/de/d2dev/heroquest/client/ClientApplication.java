package de.d2dev.heroquest.client;

import de.d2dev.fourseasons.gamestate.GameStateException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.d2dev.fourseasons.resource.ResourceLocator;
import de.d2dev.fourseasons.resource.types.TextureResource;
import de.d2dev.heroquest.engine.ai.AISystem;
import de.d2dev.heroquest.engine.ai.MapCommunicator;
import de.d2dev.heroquest.engine.ai.SearchKnot;
import de.d2dev.heroquest.engine.ai.astar.Path;
import de.d2dev.heroquest.engine.files.HqMapFile;
import de.d2dev.heroquest.engine.game.Direction2D;
import de.d2dev.heroquest.engine.game.Field;
import de.d2dev.heroquest.engine.game.RunningGameContext;
import de.d2dev.heroquest.engine.game.Map;
import de.d2dev.heroquest.engine.game.Unit;
import de.d2dev.heroquest.engine.game.action.GameAction;
import de.d2dev.heroquest.engine.rendering.Renderer;
import de.d2dev.heroquest.engine.rendering.quads.Java2DRenderWindow;
import de.d2dev.heroquest.engine.rendering.quads.QuadRenderModel;

import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class ClientApplication implements KeyListener {

    private String settingsPath = "";
    private Map map;
    private ResourceLocator resourceFinder;
    private Renderer renderer;
    private QuadRenderModel renderTarget;
    private Java2DRenderWindow window;
    private Unit hero;
    private Unit monster;
    private AISystem aiSystem;
    private ExecutorService aiExecutor = Executors.newSingleThreadExecutor();
    private boolean heroesRound = true;
    private List<GameAction> actionsToPerform;

    public ClientApplication() {
    }

    public ClientApplication(HqMapFile map, ResourceLocator resourceFinder) {
        this.map = new Map(map.map.getRootElement());
        this.resourceFinder = resourceFinder;
    }

    public void init() throws Exception {
        this.aiSystem = new AISystem(map);

        this.hero = new Unit(this.map.getField(0, 0), Unit.Type.HERO);
        this.monster = new Unit(this.map.getField(this.map.getWidth() - 1, this.map.getHeight() - 1), Unit.Type.MONSTER);

        this.monster.setAiController(this.aiSystem.creatAIController(this.monster));

        this.renderTarget = new QuadRenderModel(map.getWidth(), map.getHeight());

        this.renderer = new Renderer(map, renderTarget, this.resourceFinder);
        this.map.addListener(renderer);
        this.renderer.render();

        this.window = new Java2DRenderWindow(this.renderer.getRederTarget(), this.resourceFinder);
        this.window.setTitle("HeroQuest");
        this.window.setVisible(true);

        this.window.addKeyListener(this);
    }

    public void run() {

//        Field[][] field = this.map.getFields();
//
//        MapCommunicator communicator = new MapCommunicator(this.map);
//        Stack<Path<SearchKnot>> result = communicator.search(field.length - 1, field[0].length - 1, 0, 0, 1);
//        System.out.println(result.size());
//        for (Path<SearchKnot> path : result) {
//            for (SearchKnot knot : path.getTrace()) {
//                int x = knot.getX();
//                int y = knot.getY();
//                this.map.getField(x, y).setTexture(TextureResource.createTextureResource("error.jpg"));
//                System.out.println("Texture.set");
//            }
//        }
//        System.out.println("Hallo");
//        this.renderer.render();
//        this.window.repaint();
    }

    public static void main(String[] args) throws Exception {
        ClientApplication app = new ClientApplication();
        app.init();
        app.run();
    }

    public void heroesRound() {
    }

    public void monstersRound() {
        // monster now! initialization...
        if (this.heroesRound == true) {
            this.heroesRound = false;
        }

        this.actionsToPerform = this.monster.getAIController().getActions();

        this.performMonsterActions();
    }

    public void performMonsterActions() {
        if (this.actionsToPerform.isEmpty()) {
            this.heroesRound = true;
            return;
        }

        this.performAction(this.actionsToPerform.get(0));
        this.actionsToPerform.remove(0);
    }

    public void performAction(GameAction action) {
        try {
            action.excecute();
            System.out.println("DA");
        } catch (GameStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.renderer.render();
        this.window.repaint();
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ClientApplication.this.performMonsterActions();
            }
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());

        if (this.heroesRound) {
            if (e.getKeyChar() == 's') {	// walk down
                try {
                    if (this.hero.canMoveDown()) {
                        this.hero.moveDown();
                        this.hero.setViewDir(Direction2D.DOWN);
                    }
                } catch (GameStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (e.getKeyChar() == 'w') {	// walk up
                try {
                    if (this.hero.canMoveUp()) {
                        this.hero.moveUp();
                        this.hero.setViewDir(Direction2D.UP);
                    }
                } catch (GameStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (e.getKeyChar() == 'a') {	// walk left
                try {
                    if (this.hero.canMoveLeft()) {
                        this.hero.moveLeft();
                        this.hero.setViewDir(Direction2D.LEFT);
                    }
                } catch (GameStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (e.getKeyChar() == 'd') {	// walk right
                try {
                    if (this.hero.canMoveRight()) {
                        this.hero.moveRight();
                        this.hero.setViewDir(Direction2D.RIGHT);
                    }
                } catch (GameStateException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (e.getKeyChar() == 'm') {	// MONSTER!!!
                this.monstersRound();
            }
        }
        if (e.getKeyChar() == 'z') {
            Field[][] field = this.map.getFields();

            for (int y = 0; y < field[0].length; y++) {
                for (int x = 0; x < field.length; x++) {
                    System.out.print(field[x][y].isBlocked() ? "# " : ". ");
                }
                System.out.println();

            }
            
//            for (Field[] col  : field) {
//                for (Field f : col) {
//                   System.out.print(f.isBlocked() ? "# " : ". "); 
//                }
//                System.out.println();
//            }
            

        }

        this.renderer.render();
        this.window.repaint();
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }
}
