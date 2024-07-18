package com.example.kotlinfinalproject.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.databinding.EditItemLayoutBinding

class EditItemFragment : Fragment() {

    private var _binding: EditItemLayoutBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private var originalImageUri: Uri? = null // Store original image URI
    private val viewModel:ItemsViewModel by activityViewModels()

    private lateinit var pickImageLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditItemLayoutBinding.inflate(inflater, container, false)
        setupPickImageLauncher()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        val itemId = arguments?.getString("itemId")
//
//        // Load item details based on itemId
//        if (itemId != null) {
//            val item = ItemManager.getItemById(itemId)
//            if (item != null) {
//                binding.enterItemTitle.setText(item.title)
//                binding.enterItemDescription.setText(item.description)
//
//                // Store original image URI
//                originalImageUri = Uri.parse(item.photo.toString())
//                binding.imageBtn.setImageURI(originalImageUri)
//            }
//        }
//
//        binding.finishBtn.setOnClickListener {
//            // Create the new item with updated values
//            val newItem = Item(
//                binding.enterItemTitle.text.toString(),
//                binding.enterItemDescription.text.toString(),
//                imageUri?.toString() ?: originalImageUri?.toString() // Use original if no new image
//            )
//
//            if (itemId != null) {
//                // Update the existing item with newItem
//                ItemManager.edit(itemId, newItem)
//            }
//
//            // Navigate back to allItemsFragment
//            findNavController().navigate(R.id.action_editItemFragment_to_allItemsFragment)
//        }
//
//        binding.imageBtn.setOnClickListener {
//            pickImageLauncher.launch(arrayOf("image/*"))
//        }
    }

    private fun setupPickImageLauncher() {
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                binding.imageBtn.setImageURI(it)
                requireActivity().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}