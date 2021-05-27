package jp.ac.titech.itpro.sdl.die;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
public class Slide implements Obj{
    private final static float[] VERTICES = {
            // left
            -1, -1, -1,
            -1, -1, 1,
            -1, 1, -1,
            -1, 1, 1,
            // slide
            1, -1, -1,
            -1, -1, 1,
            1, 1, -1,
            -1, 1, 1,
            // back
            -1, -1, -1,
            1, -1, -1,
            -1, -1, 1,
            // front
            -1, 1, -1,
            1, 1, -1,
            -1, 1, 1,
            // bottom
            -1, -1, -1,
            -1, 1, -1,
            1, -1, -1,
            1, 1, -1,
    };
    private final FloatBuffer vbuf;

    Slide(){
        vbuf = ByteBuffer
                .allocateDirect(VERTICES.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vbuf.put(VERTICES);
        vbuf.position(0);
    }

    @Override
    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vbuf);
        //left
        gl.glNormal3f(-1,0,0);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,4);
        //slide
        gl.glNormal3f(1,0,1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,4,4);
        //front
        gl.glNormal3f(0,-1,0);
        gl.glDrawArrays(GL10.GL_TRIANGLES,8,3);
        //back
        gl.glNormal3f(0,1,0);
        gl.glDrawArrays(GL10.GL_TRIANGLES,11,3);
        //bottom
        gl.glNormal3f(0,0,-1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,14,4);
    }
}
