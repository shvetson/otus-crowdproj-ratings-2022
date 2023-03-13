import com.crowdproj.marketplace.ratings.api.v1.models.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {

    private val request = RatingCreateRequest(
        requestId = "1234",
        debug = RatingDebug(
            mode = RatingRequestDebugMode.STUB,
            stub = RatingRequestDebugStubs.SUCCESS,
        ),
        rating = RatingCreateObject(
            typeId = "10",
            objectId = "100",
            score = "3.5",
            voteCount = "200",
            objectType = ObjectType.COMMENT,
            createdAt = "2022-07-01T15:00:00",
            updatedAt = "2022-08-07T15:00:00",
            ownerId = "1",
        ),
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"typeId\":\\s*\"10\""))
        assertContains(json, Regex("\"objectId\":\\s*\"100\""))
        assertContains(json, Regex("\"score\":\\s*\"3.5\""))
        assertContains(json, Regex("\"voteCount\":\\s*\"200\""))
        assertContains(json, Regex("\"objectType\":\\s*\"comment\""))
        assertContains(json, Regex("\"createdAt\":\\s*\"2022-07-01T15:00:00\""))
        assertContains(json, Regex("\"updatedAt\":\\s*\"2022-08-07T15:00:00\""))
        assertContains(json, Regex("\"ownerId\":\\s*\"1\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as RatingCreateRequest

        assertEquals(request, obj)
    }
}