package org.geek8080.physics2d;

public abstract class Shape {

   public Body body;
   public float radius;
   public final Mat22 u = new Mat22();

   public Shape() {
   }

   public abstract Shape clone();

   public abstract void initialize();

   public abstract void computeMass(float density);

   public abstract void setOrient(float radians);

   public abstract Type getType();

}
