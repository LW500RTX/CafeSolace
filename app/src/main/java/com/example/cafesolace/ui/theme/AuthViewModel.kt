package com.example.cafesolace.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel :ViewModel(){
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
private val _authState = MutableLiveData<AuthSate>()
    val authSate : LiveData<AuthSate> = _authState
    init {
        checkAuthState()
    }


    fun checkAuthState(){
       if (auth.currentUser==null){
           _authState.value = AuthSate.Unauthenticated
       }else{
           _authState.value = AuthSate.Authenticated
       }
    }

    fun login (email : String,password : String){
        if (email.isEmpty()|| password.isEmpty()){
            _authState.value = AuthSate.Error("Email or password can't be empty")
            return

        }
        _authState.value = AuthSate.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    _authState.value = AuthSate.Authenticated
            }else{

_authState.value = AuthSate.Error(task.exception?.message?:"Something went Wrong")
                }            }
    }

    fun signup (email : String,password : String){
        if (email.isEmpty()|| password.isEmpty()){
            _authState.value = AuthSate.Error("Email or password can't be empty")
            return

        }
        _authState.value = AuthSate.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    _authState.value = AuthSate.Authenticated
                }else{

                    _authState.value = AuthSate.Error(task.exception?.message?:"Something went Wrong")
                }            }
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthSate.Unauthenticated
    }
}

sealed class AuthSate{
    object Authenticated : AuthSate()
    object Unauthenticated : AuthSate()
    object Loading : AuthSate()
    data class Error(val message : String) : AuthSate()
}