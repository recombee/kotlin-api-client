import com.recombee.apiclientkotlin.RecombeeTest
import com.recombee.apiclientkotlin.requests.RecommendItemSegmentsToItem
import com.recombee.apiclientkotlin.requests.RecommendNextItemSegments
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class TestRecommendNextItemSegments: RecombeeTest() {

    @Test
    fun coroutineTest() = runBlocking {
        val client = getClient()
        val result = client.sendAsync(RecommendItemSegmentsToItem("computer-1", "user-1", 5, scenario = "is-to-i"))
        .onSuccess { firstResponse ->
            Assertions.assertEquals(firstResponse.recomms.size, 5)

            client.sendAsync(RecommendNextItemSegments(firstResponse.recommId, 5))
                .onSuccess {secondResponse -> Assertions.assertEquals(secondResponse.recomms.size, 5) }
                .onFailure { exception ->
                    // Handle failure
                    Assertions.fail("RecommendNextItemSegments request failed with exception: ${exception.message}")
                }
        }.onFailure { exception ->
            // Handle failure
            Assertions.fail("Initial recomm request failed with exception: ${exception.message}")
        }

        // Additional assertions can be added if necessary
        Assertions.assertTrue(result.isSuccess, "The request did not complete successfully.")
    }
}
