public class Collision {

    public static CollisionCallback dispatch[][] = { { CollisionCircleCircle.instance, CollisionCircleAABB.instance },
            { CollisionAABBCircle.instance, CollisionAABBAABB.instance } };

}