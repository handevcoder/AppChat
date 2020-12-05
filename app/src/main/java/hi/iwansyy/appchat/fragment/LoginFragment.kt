package hi.iwansyy.appchat.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hi.iwansyy.appchat.ConstantUtil
import hi.iwansyy.appchat.LocalSession
import hi.iwansyy.appchat.R
import hi.iwansyy.appchat.databinding.FragmentLoginBinding
import hi.iwansyy.appchat.model.UserModel
import hi.iwansyy.appchat.utils.showToast

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }
    private val localSession by lazy { LocalSession(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            btnSignUp.setOnClickListener {
                if (tieEmail.text.toString().isNotEmpty() &&
                    tiePassword.text.toString().isNotEmpty()
                ) {
                    showLoading(true)

                    auth.createUserWithEmailAndPassword(
                        tieEmail.text.toString(),
                        tiePassword.text.toString()
                    ).addOnSuccessListener {
                        it.user?.uid?.let { uid ->
                            localSession.uid = uid

                            db.collection(ConstantUtil.COLLECTION).document(uid)
                                .set(UserModel(uid, tieEmail.text.toString()))
                                .addOnSuccessListener {
                                    showLoading(false)

                                    findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                                }.addOnFailureListener { exc ->
                                    exc.printStackTrace()

                                    showLoading(false)
                                }
                        }

                        showLoading(false)
                    }
                } else {
                    showToast("Email dan password tidak boleh kosong")
                }
            }

            btnSignIn.setOnClickListener {
                if (tieEmail.text.toString().isNotEmpty() &&
                    tiePassword.text.toString().isNotEmpty()
                ) {
                    showLoading(true)

                    auth.signInWithEmailAndPassword(
                        tieEmail.text.toString(),
                        tiePassword.text.toString()
                    ).addOnSuccessListener {
                        showLoading(false)

                        it.user?.uid?.let { uid ->
                            localSession.uid = uid

                            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                        }
                    }.addOnFailureListener {
                        it.printStackTrace()

                        showLoading(false)
                    }
                } else {
                    showToast("Email dan password tidak boleh kosong")
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (localSession.uid.isNotEmpty()) findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE

        binding.btnSignIn.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.btnSignUp.visibility = if (!isLoading) View.VISIBLE else View.GONE
        binding.tvOr.visibility = if (!isLoading) View.VISIBLE else View.GONE
    }

}