package com.example.lab3.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.lab3.R
import com.example.lab3.models.User
import com.example.lab3.models.UserConverter
import com.example.lab3.utils.SharedPreferencesConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile_authorized.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ProfileFragment : Fragment(), CoroutineScope {
    private val GOOGLE_SIGN_IN = 9001
    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var signInButton: SignInButton
    private lateinit var signOutButton: Button
    private lateinit var sharedPreferencesConfig: SharedPreferencesConfig
    private lateinit var user: User
    private var isSignedIn: Boolean = false
    private lateinit var layout: View
    private val job = Job()
    private val database = Firebase.database
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferencesConfig = SharedPreferencesConfig(context)
        isSignedIn = sharedPreferencesConfig.extractingLayout()
        layout = if (!isSignedIn)
            inflater.inflate(R.layout.fragment_profile_signin, container, false)
        else {
            inflater.inflate(R.layout.fragment_profile_authorized, container, false)
        }
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isSignedIn)
            bindViewsSignIn(view)
        else {
            bindViewsSignOut(view)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private fun bindViewsSignIn(view: View) {
        signInButton = view.findViewById(R.id.signInButton)
        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun bindViewsSignOut(view: View) {
        signOutButton = view.findViewById(R.id.signOutButton)
        val user = sharedPreferencesConfig.extractingUser()
        val name = user.username
        val email = user.email
        val image = user.uri?.toUri()
        val wordCount = sharedPreferencesConfig.extractingWordCount()
        if (name != null && email != null && image != null)
            bindingData(name, email, image, wordCount)
        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun readFirebaseDatabase() {
        val reference = database.getReference("user")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                val userList = data.getValue<List<HashMap<String, String>>>()
                val convertedUser = userList?.map {
                    return@map UserConverter().convert(it)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("onCancelled", error.message)
            }
        })
    }

    private fun writeFirebaseDatabase(user: User, userId: String) {
        val reference = database.getReference("user")
        reference.child(userId).setValue(user)
    }

    private fun signOut() {
        launch {
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth?.signOut()
            refreshingFragment(false)
        }
    }

    private fun signIn() {
        launch {
            firebaseAuth = FirebaseAuth.getInstance()

            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context?.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient =
                GoogleSignIn.getClient(requireActivity().applicationContext, options)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        launch {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == GOOGLE_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    firebaseAuth?.signInWithCredential(credential)
                        ?.addOnCompleteListener { authResult ->
                            if (authResult.isSuccessful) {
                                firebaseAuth?.currentUser?.let { profile ->
                                    profile.getIdToken(true).addOnCompleteListener { result ->
                                        result.result?.token
                                        val name = profile.displayName
                                        val email = profile.email
                                        val image = profile.photoUrl
                                        val userId = profile.uid
                                        val wordCount = sharedPreferencesConfig.extractingWordCount()
                                        user = User(name, email, image.toString(), wordCount)
                                        writeFirebaseDatabase(user, userId)
                                        sharedPreferencesConfig.savingUser(user)
                                        if (name != null && email != null && image != null) {
                                            bindingData(name, email, image, wordCount)
                                        }
//                                    Glide.with(this).load(user.photoUrl).into(avatarImageView)
                                    }
                                    if (!isSignedIn)
                                        refreshingFragment(true)
                                }
                            }
                        }
                } catch (e: ApiException) {
                    Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindingData(name: String, email: String, image: Uri, wordCount:String) {
        nameTextView.text = name
        emailTextView.text = email
        wordCountTextView.text=wordCount
        Glide.with(this).load(image).into(avatarImageView)
    }

    private fun refreshingFragment(isSigned: Boolean) {
        sharedPreferencesConfig.savingLayout(isSigned)
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.detach(this)?.attach(this)?.commit()
    }
}