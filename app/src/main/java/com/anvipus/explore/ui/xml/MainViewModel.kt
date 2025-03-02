package com.anvipus.explore.ui.xml

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anvipus.core.models.Resource
import com.anvipus.core.models.UserDetail
import com.anvipus.core.models.Users
import com.anvipus.core.utils.AbsentLiveData
import javax.inject.Inject
import com.anvipus.explore.repo.GeneralRepo
import androidx.lifecycle.switchMap
import com.anvipus.explore.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel @Inject constructor(private val repo: GeneralRepo,
                                        private val db: AppDatabase
) : ViewModel() {
    private val usersTrigger = MutableLiveData<Int>()
    private val usersDetailTrigger = MutableLiveData<String?>()

    val listUsersResult: LiveData<Resource<List<Users>>> = usersTrigger.switchMap { since ->
        if(since == null) AbsentLiveData.create()
        else repo.getListUsers(since)
    }
    val detalUsersResult: LiveData<Resource<UserDetail>> = usersDetailTrigger.switchMap { username ->
        if(username == null) AbsentLiveData.create()
        else repo.getDetailUser(username)
    }
    fun getListUsers(since : Int){
        usersTrigger.value = since
    }

    fun getDetailUsers(username : String){
        usersDetailTrigger.value = username
    }
    fun removeDetalUsersObserve() {
        usersDetailTrigger.value = null
    }

    suspend fun getListUserFromDb(): List<Users>?{
        return withContext(Dispatchers.IO) {
            try {
                db.usersDao().getListUsers()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}