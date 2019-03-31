package com.danikgames.housesurviver;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

class Block extends GameItem {

    int res_tree_timer;

    Block(MyGdxGame game, TextureRegion[] regions, String type, Rectangle body) {
        super(game, regions, type, body);
        res_tree_timer = 0;

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(body.x + body.width/2, body.y + body.height/2);
        bDef.active = false;
        phBody = game.mainScr.physics.createBody(bDef);
        FixtureDef fDef = new FixtureDef();
        fDef.density = 1;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(body.width/2, body.height/2);
        fDef.shape = shape;
        phBody.createFixture(fDef);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(type != null && !type.equals("filler")) {
            body.height *= 2;
            super.draw(batch, parentAlpha);
            body.height /= 2;
        }
    }

    boolean scaleToShowBack(){
        if(type != null && !type.equals("filler")) {
            if (type.equals("res_tree") ||
                    type.equals("house")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void act(float delta) {
        if(type != null && !type.equals("filler")){
            if(type.equals("res_tree")){
                res_tree_timer ++;
                if(res_tree_timer == 3000){
                    res_tree_timer = 0;
                    if(MathUtils.randomBoolean(0.1f)) res_tree_spawnApple();
                }
            }
            super.act(delta);
        }
    }

    void res_tree_spawnApple() {
        float x = MathUtils.random(body.x - 1, body.x + 1);
        float xTemp = Math.abs(x - body.x);
        float yTemp = (float)Math.sqrt(1 - Math.pow(xTemp, 2));
        float y = MathUtils.random(body.y - yTemp, body.y + yTemp);
        getStage().addActor(new GameItem(game, game.mainScr.appleSprites, "res_apple", new Rectangle(x, y, 1, 1)));
    }
}
