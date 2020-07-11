package org.geek8080.physics2d;

public class Collisions
{

	public static CollisionCallback[][] dispatch =
	{
		{ CollisionCircleCircle.instance, CollisionCirclePolygon.instance },
		{ CollisionPolygonCircle.instance, CollisionPolygonPolygon.instance }
	};

}
