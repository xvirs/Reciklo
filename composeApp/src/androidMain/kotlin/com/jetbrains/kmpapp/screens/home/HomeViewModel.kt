package com.jetbrains.kmpapp.screens.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.domain.models.LatestProductData
import com.jetbrains.kmpapp.domain.models.Profile
import com.jetbrains.kmpapp.domain.models.SessionManager
import com.jetbrains.kmpapp.domain.models.StatusResult
import com.jetbrains.kmpapp.domain.usecase.LastProductsSavedUseCase
import com.jetbrains.kmpapp.domain.usecase.ProfileDataUseCase
import com.jetbrains.kmpapp.domain.usecase.SessionManagerUseCase
import com.jetbrains.kmpapp.utils.clearsession.CleanSessionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(
    private val latestProductSavedUseCase: LastProductsSavedUseCase,
    private val sessionManagement: SessionManagerUseCase,
    private val profileUseCase:ProfileDataUseCase,
    private val cleanDataUseCase:CleanSessionUseCase
) :ViewModel() {

    fun initHome(){
        when(val res = sessionManagement.session.value){
            is SessionManager.Offline -> {
                _profileData.value = StatusResult.Error("offline")
            }
            is SessionManager.Online -> {
                fetchLatestProductSaved(res.value.token)
                fetchProfileData(res.value.token)
            }
        }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _latestProductsSaved = MutableStateFlow<StatusResult<LatestProductData>>(StatusResult.Error("pendiente de carga"))
    val latestProduct = _latestProductsSaved.asStateFlow()

    private val _profileData = MutableStateFlow<StatusResult<Profile>?>(null)
    val profileData = _profileData.asStateFlow()

    private val _urisPhotos = MutableStateFlow<List<Uri>>(emptyList())
    val urisPhotos = _urisPhotos.asStateFlow()

    private val _showDialogImagePreview = MutableStateFlow<Uri?>(null)
    val showDialogImagePreview = _showDialogImagePreview.asStateFlow()

    private val _uriPhoto = MutableStateFlow<Uri?>(null)
    val uriPhoto = _uriPhoto.asStateFlow()

    private val _buttonTakePhoto = MutableStateFlow(true)
    val buttonTakePhoto = _buttonTakePhoto.asStateFlow()

    private val _session = MutableStateFlow<Boolean>(false)
    val session = _session.asStateFlow()


    fun setLoading(value : Boolean) {
        _isLoading.value = value
    }

    fun setToggleDialog() {
        _showDialog.value = !showDialog.value
    }

    fun onSelectImages(uri: Uri) {
        _urisPhotos.value += uri
    }

    fun deletePhotoSelected(uri: Uri) {
        _urisPhotos.value = urisPhotos.value.filter {
            it != uri
        }
    }

    fun replaceUris(list: List<Uri>){
        _urisPhotos.value = list.toList()
    }

    fun onSelectImageToBitmap(uri: Uri) {
        _uriPhoto.value = uri
    }

    fun changeLoading() {
        _isLoading.value = !isLoading.value
    }

    fun buttonCameraEnabled(status: Boolean){
        _buttonTakePhoto.value = status
    }

    fun loadImages(uri: Uri){
        onSelectImages(uri)
        buttonCameraEnabled(true)
    }
    fun loadImageDemerit(uri: Uri){
        onSelectImageToBitmap(uri)
        changeLoading()
        buttonCameraEnabled(true)
    }
    fun fetchLatestProductSaved(token:String){
        viewModelScope.launch(Dispatchers.IO) {
            when(val response = latestProductSavedUseCase.invoke(token)){
                is StatusResult.Error -> _latestProductsSaved.value = StatusResult.Error(response.message)
                is StatusResult.Success -> _latestProductsSaved.value = StatusResult.Success(response.value)
            }
        }
    }

    private fun fetchProfileData(token: String) {
        viewModelScope.launch(Dispatchers.IO){
            when(val response = profileUseCase.invoke(token)){
                is StatusResult.Error -> _profileData.value = StatusResult.Error("Hola")
                is StatusResult.Success -> _profileData.value = StatusResult.Success(response.value)
            }
        }
    }

     fun cleanSession(filePath:String){
        viewModelScope.launch(Dispatchers.Default) {
            val file = File(filePath)
            when(cleanDataUseCase.invoke(file)){
                true -> _session.value = true
                false -> _session.value = false
            }
        }
    }

    fun setShowDialogImagePreview(uri : Uri?){
        if(uri != null){
            _showDialogImagePreview.value = uri
        } else {
            _showDialogImagePreview.value = null
        }
    }

    fun cleanUris(){
        _urisPhotos.value = emptyList()
        _uriPhoto.value = null
    }

}
