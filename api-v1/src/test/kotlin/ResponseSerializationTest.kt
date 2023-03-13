import com.crowdproj.marketplace.ratings.api.v1.models.IResponse
import com.crowdproj.marketplace.ratings.api.v1.models.ObjectType
import com.crowdproj.marketplace.ratings.api.v1.models.RatingCreateResponse
import com.crowdproj.marketplace.ratings.api.v1.models.RatingResponseObject
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {

    private val response = RatingCreateResponse(
        requestId = "1234",
        rating = RatingResponseObject(
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
        val json = apiV1Mapper.writeValueAsString(response)

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
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as RatingCreateResponse
        assertEquals(response, obj)
    }
}