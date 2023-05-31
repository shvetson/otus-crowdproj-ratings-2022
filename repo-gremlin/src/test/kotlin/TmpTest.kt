package com.crowdproj.rating.repo.gremlin

import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.repo.DbRatingIdRequest
import com.crowdproj.rating.common.repo.DbRatingRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.apache.tinkerpop.gremlin.driver.AuthProperties
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.junit.Ignore
import org.junit.Test
import kotlin.test.fail
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as bs

//@Ignore("Тест для экспериментов")
@OptIn(ExperimentalCoroutinesApi::class)
class TmpTest {

    @Test
    fun z() {

        val uri = "localhost"

        val authProp = AuthProperties().apply {
            with(AuthProperties.Property.USERNAME, "root")
            with(AuthProperties.Property.PASSWORD, "root_root")
        }

        val cluster = Cluster.build()
            .enableSsl(false)
            .addContactPoints(uri)
            .port(8182)
            .authProperties(authProp)
            .create()

        val g: GraphTraversalSource = traversal().withRemote(DriverRemoteConnection.using(cluster, "graph"))
        val v1 = g.addV("person").property("name", "Oleg").next()
        val v2 = g.addV("person").property("name","Ivan").next()
        g.V(v1).addE("knows").to(v2).property("weight",0.75).iterate()

        val userId1 = v1.id()
        val userId2 = v2.id()

        println("UserID 1: $userId1,  UserID 2: $userId2")

//        traversal()
//            .withRemote(DriverRemoteConnection.using(cluster, "g"))
//            .use { g ->
//                val userId = g
//                    .addV("User")
//                    .property("name", "Evan")
//                    .next()
//                    .id()
//                println("UserID: $userId")
//            }
        g.close()
    }

    @Test
    @Ignore
    fun y() {

        val uri = "localhost"

        val authProp = AuthProperties().apply {
            with(AuthProperties.Property.USERNAME, "root")
            with(AuthProperties.Property.PASSWORD, "root_root")
        }

        val cluster = Cluster.build()
            .enableSsl(false)
            .addContactPoints(uri)
            .port(8182)
            .authProperties(authProp)
            .create()

        val g: GraphTraversalSource = traversal().withRemote(DriverRemoteConnection.using(cluster, "graph"))

        // пример формирования сложного объекта - как надо!!!
        val x = g.V().hasLabel("Test").`as`("a")
            .project<Any?>("lock", "ownerId", "z")
            .by("lock")
            .by(bs.inE("Owns").outV().id())
            .by(bs.elementMap<Vertex, Map<Any?, Any?>>())
            .toList()

        println("CONTENT: $x")
        g.close()
    }

    @Test
    @Ignore
    fun x() {

        val uri = "localhost"

        val authProp = AuthProperties().apply {
            with(AuthProperties.Property.USERNAME, "root")
            with(AuthProperties.Property.PASSWORD, "root_root")
        }

        val cluster = Cluster.build()
            .enableSsl(false)
            .addContactPoints(uri)
            .port(8182)
            .authProperties(authProp)
            .create()

        val g: GraphTraversalSource = traversal().withRemote(DriverRemoteConnection.using(cluster))

        val userId = g
            .addV("User")
            .property("name", "Ivan")
            .next()
            .id()

        println("UserID: $userId")

        val id = g
            .addV("Test")
            .`as`("a")
            .property("lock", "111")
            .addE("Owns")
            .from(bs.V<Vertex>(userId))
            .select<Vertex>("a")
            .next()
            .id()

        println("ID: $id")

        val owner = g
            .V(userId)
            .outE("Owns")
            .where(bs.inV().id().`is`(id))
            .toList()
        println("OWNER: $owner")

//        val n = g
//            .V(id)
//            .`as`("a")
//            .choose(
//                bs.select<Vertex, Any>("a")
//                    .values<String>("lock")
//                    .`is`("1112"),
//                bs.select<Vertex, String>("a").drop().inject("success"),
//                bs.constant("lock-failure")
//            ).toList()
//        println("YYY: $n")

//        val x = g.V(id).`as`("a")
//            .union(
//                bs.select<Vertex, Edge>("a")
//                    .inE("User")
//                    .outV()
//                    .V(),
//                bs.select<Vertex, Vertex>("a")
//            )
//            .elementMap<Any>()
//            .toList()
//        println("CONTENT: ${x}")
        g.close()
    }

    @Test
//    @Ignore
    fun repoTest() = runTest {

        val initObj = CwpRating(
            scoreTypeId = CwpRatingScoreTypeId("x"),
            objectTypeId = CwpRatingObjectTypeId("y"),
            objectId = CwpRatingObjectId("z"),
            ownerId = CwpRatingUserId("123"),
        )
        val repo = RatingRepoGremlin(hosts = "localhost")
        val resRating = repo.createRating(DbRatingRequest(rating = initObj))

        val rating = resRating.data ?: fail("Empty object")
        val ratingRead = repo.readRating(DbRatingIdRequest(rating.id))
        println("adRead: $ratingRead")

        val ratingDel = repo.deleteRating(DbRatingIdRequest(rating.id, rating.lock))
        println("ratingDel: $ratingDel")

        val ratingDeleted = repo.readRating(DbRatingIdRequest(rating.id))
        println("ratingRead: $ratingDeleted")
    }
}