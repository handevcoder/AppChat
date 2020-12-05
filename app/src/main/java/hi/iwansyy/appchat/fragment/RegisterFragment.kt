package hi.iwansyy.appchat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import hi.iwansyy.appchat.ConstantUtil
import hi.iwansyy.appchat.LocalSession
import hi.iwansyy.appchat.UserAdapter
import hi.iwansyy.appchat.clients.NotificationClient.Companion.service
import hi.iwansyy.appchat.databinding.FragmentRegisterBinding
import hi.iwansyy.appchat.model.DataModel
import hi.iwansyy.appchat.model.PayloadModel
import hi.iwansyy.appchat.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RegisterFragment : Fragment(), UserAdapter.UserListener {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var action: ListenerRegistration
    private val adapter by lazy { UserAdapter(requireContext(), this) }
    private val localSession by lazy { LocalSession(requireContext()) }
    private val db by lazy { Firebase.firestore }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false).apply {
            rvUser.adapter = adapter
        }

        action = db.collection(ConstantUtil.COLLECTION).addSnapshotListener { value, error ->
            if (error != null) {
                error.printStackTrace()
                return@addSnapshotListener
            }

            val users = mutableListOf<UserModel>()

            value?.let {
                for (doc in it) {
                    doc.toObject(UserModel::class.java).let { model ->
                        if (model.uid != localSession.uid) {
                            users.add(model)
                        }
                    }
                }
            }

            adapter.list = users

            println(users.map { it.email })
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener { Log.e("TOKEN", it) }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        action.remove()
    }

    override fun onStart() {
        super.onStart()

        if (localSession.uid.isEmpty()) requireActivity().onBackPressed()
    }

    override fun onSend(userModel: UserModel, message: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val payload = PayloadModel(
                    DataModel("New message", message, userModel.email),
                    userModel.token
                )
                val response = service.sendNotification(payload)
                println(response)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
    }
}

