package com.danikgames.housesurviver;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

class GameItem extends Active {

    boolean showBack;
    float forShowBack;

    GameItem(MyGdxGame game, TextureRegion[] regions, String type, Rectangle body) {
        super(game, regions, type, body);
        showBack = false;
        forShowBack = 1;

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
        if(showBack) {
            if(forShowBack > 0.5f)
                forShowBack -= 0.02f;
        }else{
            if(forShowBack < 1)
                forShowBack += 0.02f;
        }
        batch.setColor(1, 1, 1, forShowBack);
        super.draw(batch, parentAlpha);
        batch.setColor(1, 1, 1, 1);
    }

    @Override
    public void act(float delta) {
        if(body.x < -body.width || body.y < -body.height || body.x > 80 || body.y > 80){
            remove();
        }
    }
}
