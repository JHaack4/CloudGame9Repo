package com.jerhis.cloudgame.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lightning extends GameObject {

    LightningBolt lightningBolt;

    public Lightning(Vector2 source, Vector2 dest) {
        super(ObjectType.Lightning);
        lightningBolt = new LightningBolt(source, dest);
    }

    public void update(float delta) {
        lightningBolt.Update(delta);

        if (lightningBolt.IsComplete()) gone = true;
    }

    public void draw(SpriteBatch spriteBatch, TextureAtlas.AtlasRegion lineImage, TextureAtlas.AtlasRegion halfCircleImage, float height) {
        lightningBolt.Draw(spriteBatch, lineImage, halfCircleImage, height);
    }

    class LightningBolt
    {
        public List<Line> Segments = new ArrayList<Line>();

        public float Alpha;
        public float FadeOutRate;
        //public Color Tint;

        public boolean IsComplete() { return Alpha <= 0; }

        //public LightningBolt(Vector2 source, Vector2 dest) {
        //    this(source, dest); //, new Color(0.9f, 0.8f, 1f))
        //}

        public LightningBolt(Vector2 source, Vector2 dest) //, Color color)
        {
            Segments = CreateBolt(source, dest, 2);
            //Segments = CreateBolt(new Vector2(100, 400), new Vector2(700, 100),2);

            //Tint = color;
            Alpha = 1f;
            FadeOutRate = 0.03f;
        }

        public void Draw(SpriteBatch spriteBatch, TextureAtlas.AtlasRegion lineImage, TextureAtlas.AtlasRegion halfCircleImage, float height)
        {


            if (Alpha <= 0)
                return;

            //spriteBatch.setColor(0,0,0,1-Alpha);
            //Line line = new Line(new Vector2(100,100), new Vector2(200,200), 2);
            //line.Draw(spriteBatch, lineImage, halfCircleImage, height);

            //spriteBatch.draw(halfCircleImage, 100,100);
            for (int k = 0; k < Segments.size(); k++) {
                Line segment = Segments.get(k);
                if (segment.A.y < height)
                    Segments.remove(k--);
                else
                    segment.Draw(spriteBatch, lineImage, halfCircleImage, height);
            }


        }

        public void Update(float delta)
        {
            Alpha -= delta;
        }

        public List<Line> CreateBolt(Vector2 source, Vector2 dest, float thickness)
        {
            List<Line> results = new ArrayList<Line>();
            Vector2 tangent = new Vector2(dest.x - source.x, dest.y - source.y);
            Vector2 normal = new Vector2(tangent.y, -tangent.x).nor();
            float length = tangent.len();

            List<Float> positions = new ArrayList<Float>();
            positions.add(0.0f);

            for (int i = 0; i < length / 4; i++)
                positions.add((float) Math.random());

            Collections.sort(positions);

            final float Sway = 80;
            final float Jaggedness = 1 / Sway;

            Vector2 prevPoint = source;
            float prevDisplacement = 0;

            for (int i = 1; i < positions.size(); i++)
            {
                float pos = positions.get(i);

                // used to prevent sharp angles by ensuring very close positions also have small perpendicular variation.
                float scale = (length * Jaggedness) * (pos - positions.get(i - 1));

                // defines an envelope. Points near the middle of the bolt can be further from the central line.
                float envelope = pos > 0.95f ? 20 * (1 - pos) : 1;

                float displacement = (float)(Math.random()*2*Sway - Sway);
                displacement -= (displacement - prevDisplacement) * (1 - scale);
                displacement *= envelope;

                Vector2 point = new Vector2(source.x + pos * tangent.x + displacement * normal.x, source.y + pos * tangent.y + displacement * normal.y) ;
                results.add(new Line(prevPoint, point, thickness));
                prevPoint = point;
                prevDisplacement = displacement;
            }

            results.add(new Line(prevPoint, dest, thickness));

           // System.out.println("---------------------------------");
           // for (Line l: results) {
                //System.out.println(" x" + l.A.x + " y" + l.A.y + " x" + l.B.x + " y" + l.B.y);
            //}

            return results;
        }

    }

    static class Line extends Sprite
    {
        public Vector2 A;
        public Vector2 B;
        public Vector2 tangent;
        public float rotation;
        public float Thickness;
        public float lx, ly, deg;

        public Line() { }
        public Line(Vector2 a, Vector2 b, float thickness)
        {
            A = a;
            B = b;
            Thickness = thickness;
            tangent = new Vector2(B.x-A.x, B.y-A.y);
            rotation = (float)Math.atan2(tangent.y, tangent.x);
            lx = A.x - 0.5f + 20 - 20*(float)Math.cos(rotation + Math.PI/2);
            ly = A.y - 20*(float)Math.sin(rotation + Math.PI/2);
            deg = rotation * 180 / 3.14159f;
        }

        public void Draw(SpriteBatch spriteBatch, TextureAtlas.AtlasRegion lineImage, TextureAtlas.AtlasRegion halfCircleImage, float height)
        {
            //float ImageThickness = 8;
            //float thicknessScale = Thickness / ImageThickness;

            //Vector2 capOrigin = new Vector2(halfCircleImage.getRegionWidth(), halfCircleImage.getRegionHeight() / 2f);
            //Vector2 middleOrigin = new Vector2(0, lineImage.getRegionHeight() / 2f);
            //Vector2 middleScale = new Vector2(tangent.len(), thicknessScale);

            spriteBatch.draw(lineImage, lx,ly - height, 0, 0, 1, 40, tangent.len()+1, 1, deg);
            //spriteBatch.draw(halfCircleImage, A.x, A.y - height, 0, 0, halfCircleImage.getRegionWidth(), halfCircleImage.getRegionHeight(), 1, 1, rotation);
            //spriteBatch.draw(halfCircleImage, A.x, A.y - height, 0, 0, halfCircleImage.getRegionWidth(), halfCircleImage.getRegionHeight(), 1, 1, rotation);

            //System.out.println("Ax" + A.x + " Ay" + A.y + " Bx" + B.x + " By" + B.y + " tx" + tangent.x + " ty" + tangent.y + " r" + rotation + " x" + (A.x - 0.5f - 20*(float)Math.cos(rotation)) + " y" + (A.y - 20*(float)Math.sin(rotation)));
        }
    }

}




/*
public void Draw(SpriteBatch spriteBatch, Color color)
{
    Vector2 tangent = B - A;
    float rotation = (float)Math.Atan2(tangent.Y, tangent.X);

    const float ImageThickness = 8;
    float thicknessScale = Thickness / ImageThickness;

    Vector2 capOrigin = new Vector2(Art.HalfCircle.Width, Art.HalfCircle.Height / 2f);
    Vector2 middleOrigin = new Vector2(0, Art.LightningSegment.Height / 2f);
    Vector2 middleScale = new Vector2(tangent.Length(), thicknessScale);

    spriteBatch.Draw(Art.LightningSegment, A, null, color, rotation, middleOrigin, middleScale, SpriteEffects.None, 0f);
    spriteBatch.Draw(Art.HalfCircle, A, null, color, rotation, capOrigin, thicknessScale, SpriteEffects.None, 0f);
    spriteBatch.Draw(Art.HalfCircle, B, null, color, rotation + MathHelper.Pi, capOrigin, thicknessScale, SpriteEffects.None, 0f);
}


    protected static List<Line> CreateBolt(Vector2 source, Vector2 dest, float thickness)
    {
        var results = new List<Line>();
        Vector2 tangent = dest - source;
        Vector2 normal = Vector2.Normalize(new Vector2(tangent.Y, -tangent.X));
        float length = tangent.Length();

        List<float> positions = new List<float>();
        positions.Add(0);

        for (int i = 0; i < length / 4; i++)
            positions.Add(Rand(0, 1));

        positions.Sort();

        const float Sway = 80;
        const float Jaggedness = 1 / Sway;

        Vector2 prevPoint = source;
        float prevDisplacement = 0;
        for (int i = 1; i < positions.Count; i++)
        {
            float pos = positions[i];

            // used to prevent sharp angles by ensuring very close positions also have small perpendicular variation.
            float scale = (length * Jaggedness) * (pos - positions[i - 1]);

            // defines an envelope. Points near the middle of the bolt can be further from the central line.
            float envelope = pos > 0.95f ? 20 * (1 - pos) : 1;

            float displacement = Rand(-Sway, Sway);
            displacement -= (displacement - prevDisplacement) * (1 - scale);
            displacement *= envelope;

            Vector2 point = source + pos * tangent + displacement * normal;
            results.Add(new Line(prevPoint, point, thickness));
            prevPoint = point;
            prevDisplacement = displacement;
        }

        results.Add(new Line(prevPoint, dest, thickness));

        return results;
    }

class LightningBolt
{
    public List<Line> Segments = new List<Line>();

    public float Alpha { get; set; }
    public float FadeOutRate { get; set; }
    public Color Tint { get; set; }

    public bool IsComplete { get { return Alpha <= 0; } }

    public LightningBolt(Vector2 source, Vector2 dest) : this(source, dest, new Color(0.9f, 0.8f, 1f)) { }

    public LightningBolt(Vector2 source, Vector2 dest, Color color)
    {
        Segments = CreateBolt(source, dest, 2);

        Tint = color;
        Alpha = 1f;
        FadeOutRate = 0.03f;
    }

    public void Draw(SpriteBatch spriteBatch)
    {
        if (Alpha <= 0)
            return;

        foreach (var segment in Segments)
        segment.Draw(spriteBatch, Tint * (Alpha * 0.6f));
    }

    public virtual void Update()
{
    Alpha -= FadeOutRate;
}

    protected static List<Line> CreateBolt(Vector2 source, Vector2 dest, float thickness)
    {
        // ...
    }

    // ...
}

LightningBolt bolt;
MouseState mouseState, lastMouseState;

protected override void Update(GameTime gameTime)
        {
        lastMouseState = mouseState;
mouseState = Mouse.GetState();

var screenSize = new Vector2(GraphicsDevice.Viewport.Width, GraphicsDevice.Viewport.Height);
var mousePosition = new Vector2(mouseState.X, mouseState.Y);

if (MouseWasClicked())
        bolt = new LightningBolt(screenSize / 2, mousePosition);

if (bolt != null)
        bolt.Update();
}

private bool MouseWasClicked()
        {
        return mouseState.LeftButton == ButtonState.Pressed && lastMouseState.LeftButton == ButtonState.Released;
}

protected override void Draw(GameTime gameTime)
        {
        GraphicsDevice.Clear(Color.Black);

spriteBatch.Begin(SpriteSortMode.Texture, BlendState.Additive);

if (bolt != null)
        bolt.Draw(spriteBatch);

spriteBatch.End();
}*/