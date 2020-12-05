package hi.iwansyy.appchat.services

import hi.iwansyy.appchat.ConstantUtil
import hi.iwansyy.appchat.model.PayloadModel
import hi.iwansyy.appchat.model.ResponseModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationService {
    @POST("send")
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=${ConstantUtil.API_KEY}"
    )
    suspend fun sendNotification(@Body body: PayloadModel): ResponseModel
}