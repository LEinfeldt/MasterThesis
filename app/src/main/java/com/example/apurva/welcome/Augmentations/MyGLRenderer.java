package com.example.apurva.welcome.Augmentations;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by apurv on 12-04-2017.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer{

    private static final String TAG = "Renderer";
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private Rectangle rectangle;
    private Triangle triangle;

    public static volatile float dx;

    public MyGLRenderer(Context context) {

    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //creates instances for the rectangle and triangle classes
        rectangle = new Rectangle();
        triangle = new Triangle();

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //calculates the Projection Matrix according to the width and height of the device
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] iMVPMatrix = new float[16];
        float[] cMVPMatrix = new float[16];
        float[] dMVPMatrix = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        //translate the direction augmentation a bit down
        Matrix.translateM(mMVPMatrix, 0, 0f, -1f, 0f);
        //rotate the triangle according to the orientation of the device and the bearing to the target
        Matrix.rotateM(iMVPMatrix, 0, mMVPMatrix, 0, dx, 0f, 1f, 0f);
        //creates matrices for the other two triangles
        Matrix.translateM(cMVPMatrix, 0, iMVPMatrix, 0, 0f, 0f, -0.8f);
        Matrix.translateM(dMVPMatrix, 0, cMVPMatrix, 0, 0f, 0f, -0.8f);
        //rotates the rectangle according to the orientation of the device and the bearing to the target
        Matrix.rotateM(mMVPMatrix, 0, dx, 0f, 1f, 0f);
        //call to draw the rectangle and three triangles
        rectangle.draw(mMVPMatrix);
        triangle.draw(iMVPMatrix);
        triangle.draw(cMVPMatrix);
        triangle.draw(dMVPMatrix);
    }
}
