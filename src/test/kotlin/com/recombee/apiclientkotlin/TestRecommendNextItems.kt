import com.recombee.apiclientkotlin.RecombeeTest
import com.recombee.apiclientkotlin.requests.RecommendItemsToUser
import com.recombee.apiclientkotlin.requests.RecommendNextItems
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class TestRecommendNextItems: RecombeeTest() {

    @Test
    fun coroutineTest() = runBlocking {
        val client = getClient()
        val result = client.sendAsync(RecommendItemsToUser("user-1", 5))
        .onSuccess { firstResponse ->
            Assertions.assertEquals(firstResponse.recomms.size, 5)

            client.sendAsync(RecommendNextItems(firstResponse.recommId, 5))
                .onSuccess {secondResponse -> Assertions.assertEquals(secondResponse.recomms.size, 5) }
                .onFailure { exception ->
                    // Handle failure
                    Assertions.fail("RecommendNextItemsrequest failed with exception: ${exception.message}")
                }
        }.onFailure { exception ->
            // Handle failure
            Assertions.fail("Initial recomm request failed with exception: ${exception.message}")
        }

        // Additional assertions can be added if necessary
        Assertions.assertTrue(result.isSuccess, "The request did not complete successfully.")
    }
}