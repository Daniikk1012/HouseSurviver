package com.danikgames.housesurviver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Comparator;

class GameScreen extends Stage implements Screen {

    MyGdxGame game;

    World physics;

    Rectangle world;

    Player player;
    TextureRegion[] playerSprites, houseSprites, treeSprites, stoneSprites, ironOreSprites, appleSprites;
    Block[][] blocks;
    Block house;
    int spawnChance, spawnIndex;
    int spawnTimer;
    String spawnType;
    Array<Block> spawnArr;
    Block spawnTemp;

    private boolean firstRun;
    private float frameBetween, dt, xGrassBack, yGrassBack;

    private class ActorComparator implements Comparator<Actor>{
        @Override
        public int compare(Actor t0, Actor t1) {
            if(t0.getY() < t1.getY()){
                return 1;
            }else if(t0.getY() > t1.getY()){
                return -1;
            }
            return 0;
        }
    }
    private ActorComparator actorComparator;

    GameScreen(MyGdxGame game){
        super(new FitViewport(16,9), game.batch);
        this.game = game;
        frameBetween = 0.02f;
        dt = 0;
        physics = new World(new Vector2(0, 0), false);
        world = new Rectangle(0,0,80,80);
        firstRun = true;
        actorComparator = new ActorComparator();
    }

    @Override
    public void show() {
        if(firstRun) {
            playerSprites = new TextureRegion[]{new TextureRegion(game.test, 0, 0, 256, 256)};
            playerSprites[0].flip(true,false);
            houseSprites = new TextureRegion[]{new TextureRegion(game.houseT, 0, 0, 64, 128)};
            treeSprites = new TextureRegion[]{new TextureRegion(game.treeT, 0, 0, 32, 64)};
            stoneSprites = new TextureRegion[]{new TextureRegion(game.stoneT, 0, 0, 32, 32)};
            ironOreSprites = new TextureRegion[]{new TextureRegion(game.ironOreT, 0, 0, 32, 32)};
            appleSprites = new TextureRegion[]{new TextureRegion(game.appleT, 0, 0, 32, 32)};

            player = new Player(game, playerSprites, "player", new Rectangle(39.5f, 38, 1, 1), 0.1f);
            addActor(player);
            player.phBody.setActive(true);

            blocks = new Block[80][80];
            for (int i = 0; i < 80; i++) {
                for(int j = 0; j < 80; j++){
                    blocks[i][j] = new Block(game, null, null, new Rectangle(i, j, 1, 1));
                    addActor(blocks[i][j]);
                }
            }
            blocks[39][40].type = blocks[40][39].type = blocks[40][40].type = "filler";

            house = new Block(game, houseSprites, "house", new Rectangle(39,39,2,2));
            addActor(house);
            house.phBody.setActive(true);

            firstRun = false;
            spawnTimer = 0;
            spawnChance = 0;
            spawnArr = new Array<Block>();
            spawnType = null;
            spawnTemp = null;
            for(int i = 0; i < 50; i++){
                spawnBlock();
            }
        }
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        dt+=delta;
        while(dt>=frameBetween){
            dt-=frameBetween;
            update(frameBetween);
        }
        Gdx.gl.glClearColor(0,0.2f,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        for(xGrassBack = -((-world.x) % 1); xGrassBack < 16; xGrassBack += 1){
            for(yGrassBack = -((-world.y) % 1); yGrassBack < 9; yGrassBack += 1){
                game.batch.draw(game.grassBlockT, xGrassBack, yGrassBack, 1, 1);
            }
        }
        game.batch.end();

        draw();
    }

    private void update(float delta){
        player.showBack = false;
        getActors().sort(actorComparator);
        for(Actor a : getActors()){
            if(a instanceof GameItem) {
                if(a instanceof Block && ((Block) a).scaleToShowBack()) ((GameItem) a).body.height *= 2;

                ((GameItem) a).showBack = ((GameItem) a).body.overlaps(player.body) && player.getY() >= a.getY();
                if(player.body.contains(((GameItem) a).body)){
                    player.showBack = true;
                }

                if(a instanceof Block && ((Block) a).scaleToShowBack()) ((GameItem) a).body.height /= 2;
            }
        }
        act(delta);
        physics.step(frameBetween, 5, 5);

        world.x = world.x + (getWidth()/2 - player.body.x - (!player.mirrored ? 1 : -1) - player.body.width/2 - world.x) / 10;
        world.y = world.y + (getHeight()/2 - player.body.y - (player.lookUp ? 1 : -1) - player.body.height/2 - world.y) / 10;
        if(world.x > 0)
            world.x = 0;
        if(world.y > 0)
            world.y = 0;
        if(world.x < getWidth() - world.width)
            world.x = getWidth() - world.width;
        if(world.y < getHeight() - world.height)
            world.y = getHeight() - world.height;
        spawnTimer ++;
        if(spawnTimer == 100){
            spawnTimer = 0;
            spawnBlock();
        }
    }

    private void spawnBlock(){
        spawnChance = MathUtils.random(1,100);
        spawnArr.clear();
        for(Actor a : getActors()){
            if(a instanceof Block){
                if(((Block) a).type == null || ((Block) a).type.startsWith("res_")) {
                    spawnArr.add((Block) a);
                }
            }
        }
        spawnIndex = MathUtils.random(0, spawnArr.size - 1);
        spawnTemp = spawnArr.get(spawnIndex);
        int x = (int)(spawnTemp.getX() - spawnTemp.getX()%1),
                y = (int)(spawnTemp.getY() - spawnTemp.getY()%1);

        if(spawnChance > 50){
            if(spawnChance <= 75){
                blocks[x][y].regions = treeSprites;
                blocks[x][y].type = "res_tree";
                blocks[x][y].body = spawnTemp.body;
                blocks[x][y].mirrored = false;
                blocks[x][y].frame = 0;
                blocks[x][y].showBack = false;
                blocks[x][y].forShowBack = 1;
                blocks[x][y].res_tree_timer = 0;
                blocks[x][y].phBody.setActive(true);
            }else if(spawnChance <= 92){
                blocks[x][y].regions = stoneSprites;
                blocks[x][y].type = "res_stone";
                blocks[x][y].body = spawnTemp.body;
                blocks[x][y].mirrored = false;
                blocks[x][y].frame = 0;
                blocks[x][y].showBack = false;
                blocks[x][y].forShowBack = 1;
                blocks[x][y].res_tree_timer = 0;
                blocks[x][y].phBody.setActive(true);
            }else{
                blocks[x][y].regions = ironOreSprites;
                blocks[x][y].type = "res_ironOre";
                blocks[x][y].body = spawnTemp.body;
                blocks[x][y].mirrored = false;
                blocks[x][y].frame = 0;
                blocks[x][y].showBack = false;
                blocks[x][y].forShowBack = 1;
                blocks[x][y].res_tree_timer = 0;
                blocks[x][y].phBody.setActive(true);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
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
        physics.dispose();
        super.dispose();
    }
}
