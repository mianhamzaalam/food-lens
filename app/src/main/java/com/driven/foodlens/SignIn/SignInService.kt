import android.content.Context
import com.driven.foodrecipeapp.User.UserRegistration.Model.User
import com.driven.foodrecipeapp.User.UserRegistration.Model.UserRegistration
import com.driven.foodrecipeapp.User.UserRegistration.Object.UserObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInService(private val context: Context) {

    suspend fun signIn(user: User): Result<UserRegistration> {
        return withContext(Dispatchers.IO) {
            try {
                val response = UserObject.api.registerUser(user)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Result.success(responseBody)
                    } else {
                        Result.failure(Exception("Error: Empty response body"))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
