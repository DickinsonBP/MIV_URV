package com.example.pract2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

/**
 * OpenGL Custom renderer used with GLSurfaceView
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private float width, height, aspect;
    private Light light;
    private Light pointLight;
    private Camera camera1, camera2, camera3;

    private int cameraId = 1;


    Context context;   // Application's context
    Planet sun, mercury, venus, earth, mars, jupiter, saturn, saturn_ring, uranus, neptune, moon;
    Planet skySpace;

    Planet orbit1, orbit2, orbit3, orbit4, orbit5, orbit6, orbit7, orbit8;

    /*Other*/
    Planet ufo;
    Square hud;

    int angle = 0;
    float Z = 1;
    long lastTime = 0, currentTime;

    // Constructor with global application context
    public MyGLRenderer(Context context) {
        this.context = context;

        this.skySpace = new Planet(context, R.raw.sphere);
        this.orbit1 = new Planet(context, R.raw.orbit);
        this.orbit2 = new Planet(context, R.raw.orbit);
        this.orbit3 = new Planet(context, R.raw.orbit);
        this.orbit4 = new Planet(context, R.raw.orbit);
        this.orbit5 = new Planet(context, R.raw.orbit);
        this.orbit6 = new Planet(context, R.raw.orbit);
        this.orbit7 = new Planet(context, R.raw.orbit);
        this.orbit8 = new Planet(context, R.raw.orbit);

        this.sun = new Planet(context, R.raw.planet);
        this.mercury = new Planet(context, R.raw.planet);
        this.venus = new Planet(context, R.raw.planet);
        this.earth = new Planet(context, R.raw.planet);
        this.moon = new Planet(context, R.raw.planet);
        this.mars = new Planet(context, R.raw.planet);
        this.jupiter = new Planet(context, R.raw.planet);
        this.saturn = new Planet(context, R.raw.planet);
        this.saturn_ring = new Planet(context, R.raw.saturn_ring);
        this.uranus = new Planet(context, R.raw.planet);
        this.neptune = new Planet(context, R.raw.planet);

        /*other*/
        this.ufo = new Planet(context, R.raw.ufo);
        this.hud = new Square();

    }

    // Call back when the surface is first created or re-created
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, new float[]{0, 0, 0, 0}, 0);

        gl.glEnable(GL10.GL_LIGHTING);

        gl.glEnable(GL10.GL_NORMALIZE);

        // You OpenGL|ES initialization code here
        // ......
        //setCamera(1);
        this.camera2 = new Camera(gl, new Vector4(0, 50.0f, 0, 1.0f), new Vector4(0, 0, 1, 1), new Vector4(0, 1, 0, 1));
        this.camera1 = new Camera(gl, new Vector4(0, 250, 0, 1.0f), new Vector4(0, 0, 1, 1), new Vector4(0,1,0, 1));
        this.camera3 = new Camera(gl, new Vector4(0, 100, 200.0f, 0.0f), new Vector4(0, 0, 0, 1), new Vector4(0, 1, 0, 1));

        /*Other*/
        this.ufo.loadTexture(gl, context, R.raw.ufo_texture);
        this.hud.loadTexture(gl, context, R.raw.texto);


        /*Planets and orbits*/
        this.orbit1.loadTexture(gl, context ,R.raw.orbit_texture);
        this.orbit2.loadTexture(gl, context ,R.raw.orbit_texture);
        this.orbit3.loadTexture(gl, context ,R.raw.orbit_texture);
        this.orbit4.loadTexture(gl, context ,R.raw.orbit_texture);
        this.orbit5.loadTexture(gl, context ,R.raw.orbit_texture);
        this.orbit6.loadTexture(gl, context ,R.raw.orbit_texture);
        this.orbit7.loadTexture(gl, context ,R.raw.orbit_texture);
        this.orbit8.loadTexture(gl, context ,R.raw.orbit_texture);

        this.skySpace.loadTexture(gl, context, R.raw.space);
        this.sun.loadTexture(gl, context, R.raw.sun_texture);
        this.mercury.loadTexture(gl,context, R.raw.mercury_texture);
        this.venus.loadTexture(gl, context, R.raw.venus_texture);
        this.earth.loadTexture(gl, context, R.raw.earth_texture);
        this.moon.loadTexture(gl, context, R.raw.moon_texture);
        this.mars.loadTexture(gl, context, R.raw.mars_texture);
        this.jupiter.loadTexture(gl, context, R.raw.jupiter_texture);
        this.saturn.loadTexture(gl, context, R.raw.saturn_body);
        this.saturn_ring.loadTexture(gl, context, R.raw.eris);
        this.uranus.loadTexture(gl, context, R.raw.uranus_texture);
        this.neptune.loadTexture(gl, context, R.raw.neptune_texture);

        pointLight = new Light(gl, GL10.GL_LIGHT1);
        pointLight.setPosition(new float[]{0.0f, 3, 0, 1.0f});
        //pointLight.setAmbientColor(new float[]{0f, 0f, 0.4f});
        pointLight.setDiffuseColor(new float[]{0.5f, 0.5f, 0.5f});
    }

    // Call back after onSurfaceCreated() or whenever the window's size changes
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        this.aspect = (float) width / height;

        this.width = width;
        this.height = height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 90, aspect, 0.1f, 10000.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset
    }

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {

        //3D
        setPerspectiveProjection(gl);

        // Clear color and depth buffers using clear-value set earlier
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);


        /***
        gl.glTranslatef((float) (40 * Math.cos(Math.PI * 2 * angle/360*2)), (float) (40 * Math.sin(Math.PI * 2 * angle/360*2)),0);
         **/

        if(cameraId == 0)camera1.look();
        else if(cameraId == 2) camera3.look();
        else {
            gl.glTranslatef((float) (100 * Math.cos(Math.PI * 2 * angle/360)), (float) (100 * Math.sin(Math.PI * 2 * angle/360)),0);
            camera2.look();
        }

        gl.glPushMatrix();
        if(cameraId == 0)gl.glTranslatef(camera1.eye.get(0),camera1.eye.get(1),camera1.eye.get(2));
        else if(cameraId == 2)gl.glTranslatef(camera3.eye.get(0),camera3.eye.get(1),camera3.eye.get(2));
        else gl.glTranslatef(camera2.eye.get(0),camera2.eye.get(1),camera2.eye.get(2));
        //gl.glTranslatef(0,250,0);
        gl.glScalef(150, 150, 150);
        gl.glRotatef((angle/20.0f), 0, 1, 0);
        this.skySpace.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(150 - angle, 300 - angle, 0);
        gl.glRotatef((angle * 3)%360, 1, 1, 1); //rotar sobre si mismo
        gl.glScalef(2,2 ,2);
        this.ufo.draw(gl);
        gl.glPopMatrix();

        gl.glDisable(GL10.GL_LIGHTING);
        gl.glPushMatrix();
        gl.glScalef(40,40,40);
        gl.glRotatef((angle * 0.1f)%360, 0, 1, 0);
        this.sun.draw(gl);
        gl.glPopMatrix();
        gl.glEnable(GL10.GL_LIGHTING);

        gl.glPushMatrix();
        gl.glScalef(2.5f, 2.5f,2.5f );
        gl.glRotatef((angle * 3)%360, 0, 1, 0);
        gl.glTranslatef(30, 0, 0);
        gl.glRotatef((angle * 3)%360, 0, 1, 0);
        this.mercury.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(75,75,75);
        this.orbit1.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(3,3,3);
        gl.glRotatef((angle * 3.5f)%360, 0, 1, 0);
        gl.glTranslatef(30, 0, 0);
        gl.glRotatef((angle * 1.5f)%360, 0, 1, 0);
        this.venus.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(90,90,90);
        this.orbit2.draw(gl);
        gl.glPopMatrix();

        /***Earth and moon***/
        gl.glPushMatrix();
        gl.glScalef(4,4,4);
        gl.glRotatef(angle%360, 0, 1, 0);
        gl.glTranslatef(30, 0, 0);
        gl.glRotatef((angle * 1.2f)%360, 0, 1, 0);
        this.earth.draw(gl);
        gl.glRotatef((angle * 3)%360, 0 , 1, 0);
        gl.glTranslatef(0,0,3);
        gl.glScalef(0.5f,0.5f,0.5f);
        this.moon.draw(gl);
        gl.glPopMatrix();
        /*******/

        gl.glPushMatrix();
        gl.glScalef(120,120,120);
        this.orbit3.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(3.5f,3.5f,3.5f);
        gl.glRotatef((angle * 0.8f)%360, 0, 1, 0);
        gl.glTranslatef(40, 0, 0);
        gl.glRotatef((angle * 1.5f)%360, 0, 1, 0);
        this.mars.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(140,140,140);
        this.orbit4.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(8,8,8);
        gl.glRotatef((angle * 0.2f)%360, 0, 1, 0);
        gl.glTranslatef(20, 0, 0);
        gl.glRotatef((angle * 0.8f)%360, 0, 1, 0);
        this.jupiter.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(160,160,160);
        this.orbit5.draw(gl);
        gl.glPopMatrix();

        /**Saturn an his ring**/
        gl.glPushMatrix();
        gl.glScalef(7,7,7);
        gl.glRotatef((angle * 0.15f)%360, 0, 1, 0);
        gl.glTranslatef(25.9f, 0, 0);
        gl.glRotatef((angle * 0.5f)%360, 0, 1, 0);
        this.saturn.draw(gl);
        gl.glScalef(3,3,3);
        gl.glRotatef(angle%360, 0, 1, 0);
        this.saturn_ring.draw(gl);
        gl.glPopMatrix();
        /*******/

        gl.glPushMatrix();
        gl.glScalef(182,182,182);
        this.orbit6.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(6,6,6);
        gl.glRotatef((angle * 0.09f)%360, 0, 1, 0);
        gl.glTranslatef(35, 0, 0);
        gl.glRotatef(angle%360, 0, 1, 0);
        this.uranus.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(210,210,210);
        this.orbit7.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(6,6,6);
        gl.glRotatef((angle * 0.05f)%360, 0, 1, 0);
        gl.glTranslatef(38.5f, 0, 0);
        gl.glRotatef((angle * 0.5f)%360, 0, 1, 0);
        this.neptune.draw(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScalef(230,230,230);
        this.orbit8.draw(gl);
        gl.glPopMatrix();

        angle++;

        //HUD
        setOrthographicProjection(gl);

        gl.glDisable(GL10.GL_LIGHTING);
        gl.glPushMatrix();
        gl.glTranslatef(-3.5f,-2,0);
        gl.glScalef(0.6f,0.6f,0.6f);
        //hud.loadTexture(gl, context, R.raw.texto);
        hud.draw(gl);
        gl.glPopMatrix();
        gl.glEnable(GL10.GL_LIGHTING);
    }

    private void setPerspectiveProjection(GL10 gl) {
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
        gl.glDepthMask(true);  // disable writes to Z-Buffer

        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix

        // Use perspective projection
        GLU.gluPerspective(gl, 90, aspect, 0.1f, 10000.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset
    }

    private void setOrthographicProjection(GL10 gl){
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(-5,5,-4,4,-5,5);
        gl.glDepthMask(false);  // disable writes to Z-Buffer
        gl.glDisable(GL10.GL_DEPTH_TEST);  // disable depth-testing

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    public void setCamera(int camera){
        this.cameraId = camera;
    }

    public int getCamera(){
        return this.cameraId;
    }

}