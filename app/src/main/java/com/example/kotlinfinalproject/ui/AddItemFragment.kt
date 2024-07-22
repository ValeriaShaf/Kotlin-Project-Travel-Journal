package com.example.kotlinfinalproject.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.usage.ExternalStorageStats
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.databinding.AddItemLayoutBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.net.URI

class AddItemFragment: Fragment() {

    private var _binding: AddItemLayoutBinding?=null
    private val binding get() = _binding!!
    private val viewModel:ItemsViewModel by activityViewModels()
    private lateinit var locationModel: LocationModel
    private lateinit var file: File
    private lateinit var cameraImgLauncher: ActivityResultLauncher<Uri>

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>
    private lateinit var cameraPermissionRequest: ActivityResultLauncher<String>
    lateinit private var imageUri: Uri
//    val pickImageLauncher: ActivityResultLauncher<Array<String>> =
//        registerForActivityResult(ActivityResultContracts.OpenDocument()){
//            binding.imageBtn.setImageURI(it)
//            if (it != null) {
//                requireActivity().contentResolver.takePersistableUriPermission(it,
//                    Intent.FLAG_GRANT_READ_URI_PERMISSION
//                )
//            }
//            imageUri=it
//        }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= AddItemLayoutBinding.inflate(inflater, container, false)
        val permissionDeniedToast = Toast.makeText(this.context, "Permission Denied", Toast.LENGTH_SHORT)
        cameraImgLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
            if(it){
                Glide.with(this.requireContext()).load(file).into(binding.imageBtn)
            }
        }
        locationModel = LocationModel(this.requireContext())
        locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                binding.enterItemLocation.setText(locationModel.getLocation())
            }
            else{
                binding.locationBtn.setOnClickListener {permissionDeniedToast.show()}
            }
        }
        cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                file = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"file.jpg")
                imageUri = FileProvider.getUriForFile(this.requireContext(),"${requireContext().packageName}.provider",file)
                cameraImgLauncher.launch(imageUri)
            }
            else{
                binding.locationBtn.setOnClickListener {permissionDeniedToast.show()}
            }
        }
        binding.locationBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                binding.enterItemLocation.setText(locationModel.getLocation())
            } else {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        binding.imageBtn.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED){
                file = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"file.jpg")
                imageUri = FileProvider.getUriForFile(this.requireContext(),"${requireContext().packageName}.provider",file)
                cameraImgLauncher.launch(imageUri)
            }
            else{ cameraPermissionRequest.launch(Manifest.permission.CAMERA)}
        }
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based
        val currentYear = calendar.get(Calendar.YEAR)

        binding.numberPickerDay.minValue = 1
        binding.numberPickerDay.maxValue = getDaysInMonth(currentMonth, currentYear)
        binding.numberPickerDay.value = currentDay

        binding.numberPickerMonth.minValue = 1
        binding.numberPickerMonth.maxValue = 12
        binding.numberPickerMonth.value = currentMonth

        binding.numberPickerYear.minValue = currentYear
        binding.numberPickerYear.maxValue = 2040
        binding.numberPickerYear.value = currentYear

        binding.numberPickerMonth.setOnValueChangedListener { _, _, newVal ->
            binding.numberPickerDay.maxValue = getDaysInMonth(newVal, binding.numberPickerYear.value)
        }

        binding.numberPickerYear.setOnValueChangedListener { _, _, newVal ->
            binding.numberPickerDay.maxValue = getDaysInMonth(binding.numberPickerMonth.value, newVal)
        }

        binding.finishBtn.setOnClickListener {
            if(binding.enterItemTitle.text!=null&&binding.enterItemDescription.text!=null&&binding.enterItemLocation.text!=null&&imageUri!=null){
            val day = binding.numberPickerDay.value
            val month = binding.numberPickerMonth.value
            val year = binding.numberPickerYear.value

            val selectedDate = "$day/$month/$year"

            val item = Item(
                binding.enterItemTitle.text.toString(),
                binding.enterItemDescription.text.toString(),
                imageUri.toString(),
                selectedDate,
                binding.enterItemLocation.text.toString()
            )
            viewModel.addItem(item)
            findNavController().navigate(
                R.id.action_addItemFragment_to_allItemsFragment,
                bundleOf("item" to item)
            )}
            else {
                Toast.makeText(context, getString(R.string.enter_all_parameters), Toast.LENGTH_SHORT).show()
            }

        }


//        binding.imageBtn.setOnClickListener {
//            pickImageLauncher.launch(arrayOf("image/*"))
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    private fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 30
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

}