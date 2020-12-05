package hi.iwansyy.appchat

import androidx.viewbinding.BuildConfig.*

class ConstantUtil {
    companion object {
        const val API_KEY = "AAAACYrbDb4:APA91bGA5puj5JJ61fYIMB6jVdbdCv41GoLhT20FuFztdJcQNOrmTedofW9rwdTF_LHizOxUZqjRF3IgTzebtM6TLVsGWXylpz4CMUkZzi442_U6MLSgHYJe2CF8t4TaVOWKXD7Nus6N"
        const val COLLECTION = "users"
        const val NOTIFICATION_ID = 123
        const val NOTIFICATION_CHANNEL_ID = "${BuildConfig.APPLICATION_ID}.fcm"
        const val NOTIFICATION_CHANNEL_NAME = "AppChat Push Notification"
    }
}