package com.example.kotlinfinalproject.ui

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.databinding.EditItemLayoutBinding

class EditItemFragment : Fragment() {

    private var _binding: EditItemLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemsViewModel by activityViewModels()

    private var imageUri: Uri? = null
    private var itemId: Int = -1
    private var existingPhoto: String? = null

    val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                requireActivity().contentResolver.takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = uri
                binding.imageBtn.setImageURI(uri)
            }
        }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditItemLayoutBinding.inflate(inflater, container, false)

        arguments?.let {
            itemId = it.getInt("itemId", -1)
        }

        if (itemId != -1) {
            viewModel.getItemById(itemId).observe(viewLifecycleOwner) { item ->
                if (item != null) {
                    binding.enterItemTitle.setText(item.title)
                    binding.enterItemDescription.setText(item.description)
                    existingPhoto = item.photo

                    binding.imageBtn.setImageURI(Uri.parse(item.photo))

                //imageUri = Uri.parse(item.photo)
                    //Glide.with(requireContext()).load(item.photo).into(binding.imageBtn)
                }
            }
        }

        binding.finishBtn.setOnClickListener {
            if (itemId != -1) {
                // Update existing item
                val updatedItem = Item(
                    title = binding.enterItemTitle.text.toString(),
                    description = binding.enterItemDescription.text.toString(),
                    photo = imageUri?.toString() ?: existingPhoto // Use existing photo if no new image is selected
                ).apply { id = itemId } // Set ID of the existing item

                viewModel.updateItem(updatedItem)
            }
            findNavController().navigate(R.id.action_editItemFragment_to_allItemsFragment)
        }

        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
