import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingContext
import com.crowdproj.rating.common.model.*
import com.crowdproj.rating.common.stub.CwpRatingStubs
import com.crowdproj.rating.stubs.CwpRatingStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RatingCreateStubTest {

    private val processor = CwpRatingProcessor()
    val id = CwpRatingId("111")
    val scoreTypeId = CwpRatingScoreTypeId("11")
    val objectId = CwpRatingObjectId("100")
    val objectTypeId = CwpRatingObjectTypeId("10")

    @Test
    fun create() = runTest {

       val ctx = CwpRatingContext(
           command = CwpRatingCommand.CREATE,
           state = CwpRatingState.NONE,
           workMode = CwpRatingWorkMode.STUB,
           stubCase = CwpRatingStubs.SUCCESS,

           ratingRequest = CwpRating(
               id = id,
               scoreTypeId = scoreTypeId,
               objectTypeId = objectTypeId,
               objectId = objectId
           ),
       )

        processor.exec(ctx)

        assertEquals(CwpRatingStub.get().id, ctx.ratingResponse.id)
    }
}