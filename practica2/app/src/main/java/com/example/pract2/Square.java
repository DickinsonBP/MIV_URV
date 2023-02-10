package com.example.pract2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;


public class Square {
    private final FloatBuffer vertexBuffer;  // buffer holding the vertices
    private ShortBuffer indexBuffer;
    private FloatBuffer texcoordBuffer;

    private float vertices[] = {
            -2f, -2f, 0.0f,
            -2f,  2f, 0.0f,
            2f,  2f, 0.0f,
            2f, -2f, 0.0f};
    private short faces[] = { 0, 1, 2, 0, 2, 3 };

    int[] textureIDs = new int[1];
    float[] texCoords = { // Texture coords for the above face
            0.0f, 1.0f,  // A. left-bottom 0,1
            0.0f, 0.0f,  // B. right-bottom 1,1
            1.0f, 1.0f,  // C. left-top 0,0
            1.0f, 0.0f   // D. right-top 1,0
    };

    public Square() {
        // a float has 4 bytes, therefore we multiply the number of vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // Setup texture-coords-array buffer, in float. An float has 4
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texcoordBuffer = tbb.asFloatBuffer();
        texcoordBuffer.put(texCoords);
        texcoordBuffer.position(0);

        //Move the faces list into a buffer
        ByteBuffer ibb = ByteBuffer.allocateDirect(faces.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(faces);
        indexBuffer.position(0);
    }

    public void draw(GL10 gl) {

        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
        gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
        gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)
        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture

        // Enabled the vertices buffer for writing and to be used during rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texcoordBuffer); // Define texture-coords buffer

        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);

        gl.glPushMatrix();
        //gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, faces.length,GL10.GL_UNSIGNED_SHORT, indexBuffer);
        gl.glPopMatrix();

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    public void loadTexture(GL10 gl, Context context, int textureResourceId) {
        gl.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Construct an input stream to texture image "res\drawable\nehe.png"
        InputStream istream = context.getResources().openRawResource(textureResourceId);

        Bitmap bitmap;
        try {
            // Read and decode input as bitmap
            bitmap = BitmapFactory.decodeStream(istream);
        } finally {
            try {
                istream.close();
            } catch(IOException e) { }
        }

        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

    }
}