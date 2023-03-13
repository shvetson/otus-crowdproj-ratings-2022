import com.crowdproj.marketplace.ratings.api.v1.models.*
import kotlinx.datetime.Instant
import models.*
import org.junit.Test
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = RatingCreateRequest(
            requestId = "1234",
            debug = RatingDebug(
                mode = RatingRequestDebugMode.STUB,
                stub = RatingRequestDebugStubs.SUCCESS,
            ),
            rating = RatingCreateObject(
                typeId = "1",
                objectId = "10",
                objectType = ObjectType.PRODUCT,
                score = "5",
                voteCount = "100",
                createdAt = null,
                updatedAt = null,
                ownerId = "1000",
            ),
        )

        val context = MkplContext()
        context.fromTransport(req)

        assertEquals(MkplStub.SUCCESS, context.stubCase)
        assertEquals(MkplWorkMode.STUB, context.workMode)

        assertEquals("1", context.ratingRequest.typeId.asString())
        assertEquals("10", context.ratingRequest.objectId.asString())
        assertEquals(MkplObjectType.PRODUCT, context.ratingRequest.objectType)
        assertEquals("5", context.ratingRequest.score)
        assertEquals("100", context.ratingRequest.voteCount)
        assertEquals(Instant.NONE, context.ratingRequest.createdAt)
        assertEquals(Instant.NONE, context.ratingRequest.updatedAt)
        assertEquals("1000", context.ratingRequest.ownerId.asString())
    }

    @Test
    fun toTransport() {
        val context = MkplContext(
            requestId = MkplRequestId("1234"),
            command = MkplCommand.CREATE,
            ratingResponse = MkplRating(
                typeId = MkplScoreTypeId("1"),
                objectId = MkplObjectId("10"),
                objectType = MkplObjectType.PRODUCT,
                score = "5",
                voteCount = "100",
                createdAt = Instant.parse("2023-01-11T12:22:53Z"),
                updatedAt = Instant.NONE,
                ownerId = MkplUserId("1000")

            ),
            errors = mutableListOf(
                MkplError(
                    code = "err",
                    group = "request",
                    field = "score",
                    message = "wrong value",
                )
            ),
            state = MkplState.RUNNING,
        )

        val req = context.toTransport() as RatingCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("1", req.rating?.typeId)
        assertEquals("10", req.rating?.objectId)
        assertEquals(ObjectType.PRODUCT, req.rating?.objectType)
        assertEquals("5", req.rating?.score)
        assertEquals("100", req.rating?.voteCount)
        assertEquals("2023-01-11T12:22:53Z", req.rating?.createdAt)
        assertEquals("null", req.rating?.updatedAt)
        assertEquals("1000", req.rating?.ownerId)

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("score", req.errors?.firstOrNull()?.field)
        assertEquals("wrong value", req.errors?.firstOrNull()?.message)
    }
}