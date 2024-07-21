package com.example.kotlinfinalproject.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.databinding.AddItemLayoutBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItemFragment: Fragment() {

    private var _binding: AddItemLayoutBinding?=null
    private val binding get() = _binding!!
    private val viewModel:ItemsViewModel by activityViewModels()
    private val locationViewModel:LocationViewModel by activityViewModels()
    private lateinit var location : LocationModel
    private var imageUri: Uri?=null
    val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()){
        binding.imageBtn.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        imageUri=it
        }
    private lateinit var req: ActivityResultLauncher<String>
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= AddItemLayoutBinding.inflate(inflater, container, false)
        val toast = Toast.makeText(requireContext(), "Location Permission Denied", Toast.LENGTH_SHORT)
        req = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                getLocation()
            }
            else{
                binding.locationBtn.setOnClickListener {toast.show()}
            }
        }
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation()
        }
        else{
            req.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        binding.finishBtn.setOnClickListener {
           // val bundle= bundleOf("title" to binding.itemTitle.text.toString(), "description" to binding.itemDescription.text.toString())

            //findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment,bundle)
        val item= Item(
            binding.enterItemTitle.text.toString(),
            binding.enterItemDescription.text.toString(), imageUri.toString(),
//            binding.enterItemLocation.toString()
        )
           // ItemManager.add(item)
        viewModel.addItem(item)
        findNavController().navigate(
            R.id.action_addItemFragment_to_allItemsFragment
        , bundleOf("item" to item)
            )

        }
        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun getLocation(){
        locationViewModel.location.observe(viewLifecycleOwner){
            binding.enterItemLocation.setText(it)
        }
    }
}