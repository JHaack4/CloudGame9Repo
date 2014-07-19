package com.jerhis.cloudgame.game;

import com.badlogic.gdx.math.Vector3;

public class Guy {
	
	public float x,y;
	public float velY, velX;
	public boolean jumping= true, dead = false;
    public Vector3 lastAccel = new Vector3(0,0,0);
	
	public final float  MOMENTUM = 1f, GRAVITY = 9,
			MAXMOMENTUM = 7, RESISTANCE = 0.5f,
			TILTMOMENTUM = 380f, REGULAR_JUMP = 4.0f, SUPER_JUMP = 10.8f;
	
	public Guy(float x, float y){
		this.x = x; 
		this.y = y;
        lastAccel = new Vector3(0,0,0);
	}
	
	public Guy(){
		this(400,150);
	} 
	
	public void update(float delta, boolean right, boolean left, float height, float sensitivity) {
		velY -= delta * GRAVITY;

        sensitivity += velY > 4 ? (velY - 4)/(10.8 - 4)*1.3f : 0;
        sensitivity = sensitivity > 2 ? 2.0f : sensitivity;

		if (!right && !left)
			velX = velX > 0 ? velX - RESISTANCE * sensitivity + 0.01f : velX + RESISTANCE * sensitivity - 0.01f;
		velX = velX >= -RESISTANCE * sensitivity && velX <= RESISTANCE * sensitivity ? 0 : velX;
		
		velX = right ? velX + MOMENTUM * sensitivity : velX;
		velX = left ? velX - MOMENTUM * sensitivity : velX;
		
		velX = velX > MAXMOMENTUM * sensitivity ? MAXMOMENTUM * sensitivity : velX;
		velX = velX < -MAXMOMENTUM * sensitivity ? -MAXMOMENTUM * sensitivity : velX;
		
		x += velX;
		y += velY;
		
		if (x < 0) x = 800;
		if (x > 800) x = 0;
				
		if (y < -40 + height) dead = true;
	}
	
	public void update(float delta, float accelX, float accelY, float accelZ, float height, float sensitivity) {
		velY -= delta * GRAVITY;

        sensitivity += velY > REGULAR_JUMP ? (velY - REGULAR_JUMP)/(SUPER_JUMP - REGULAR_JUMP)*1.3f : 0;
        sensitivity = sensitivity > 2 ? 2.0f : sensitivity;

        // filter the jerky acceleration in the variable accel:
        Vector3 accel = new Vector3(accelX,accelY,accelZ);
        accel.lerp(lastAccel, 0.01f * delta);
               // Vector3.Lerp(accel, Input.acceleration, filter * Time.deltaTime);
        // map accel -Y and X to game X and Y directions:
       // var dir = Vector3(-accel.y, accel.x, 0);
        // limit dir vector to magnitude 1:
        if (accel.len() > 1) accel = accel.nor();
        velX = accel.y * delta * TILTMOMENTUM;
        lastAccel = accel;
        // move the object at the velocity defined in speed:
        //transform.Translate(dir * speed * Time.deltaTime);

		/*if (Math.abs(accelY) < 0.1)
			velX = 0;
		else velX = accelY * TILTMOMENTUM * sensitivity;*/
		
		//velX = velX > MAXMOMENTUM * sensitivity ? MAXMOMENTUM * sensitivity : velX;
		//velX = velX < -MAXMOMENTUM * sensitivity ? -MAXMOMENTUM * sensitivity : velX;

        if (height <= 0) sensitivity *= 0.5;

		x += velX * sensitivity;
		y += velY;
		
		if (x < 0) x = 800;
		if (x > 800) x = 0;
				
		if (y < -40 + height) dead = true;
	}

}
