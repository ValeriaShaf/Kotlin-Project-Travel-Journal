package com.example.kotlinfinalproject.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
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
    private var existingDate: String? = null

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

        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is zero-based
        val currentYear = calendar.get(Calendar.YEAR)

        binding.numberPickerDay.minValue = 1
        binding.numberPickerMonth.minValue = 1
        binding.numberPickerYear.minValue = currentYear

        binding.numberPickerMonth.maxValue = 12
        binding.numberPickerYear.maxValue = 2040




        if (itemId != -1) {
            viewModel.getItemById(itemId).observe(viewLifecycleOwner) { item ->
                if (item != null) {
                    binding.enterItemTitle.setText(item.title)
                    binding.enterItemDescription.setText(item.description)
                    existingPhoto = item.photo
                    binding.imageBtn.setImageURI(Uri.parse(item.photo))

                    existingDate = item.date
                    val day = getDayFromDate(item.date)
                    val month = getMonthFromDate(item.date)
                    val year = getYearFromDate(item.date)

                    binding.numberPickerDay.maxValue = getDaysInMonth(month, year)
                    binding.numberPickerDay.value = day
                    binding.numberPickerMonth.value = month
                    binding.numberPickerYear.value = year

                    // Update day max value on month and year change
                    binding.numberPickerMonth.setOnValueChangedListener { _, _, newVal ->
                        binding.numberPickerDay.maxValue = getDaysInMonth(newVal, binding.numberPickerYear.value)
                    }

                    binding.numberPickerYear.setOnValueChangedListener { _, _, newVal ->
                        binding.numberPickerDay.maxValue = getDaysInMonth(binding.numberPickerMonth.value, newVal)
                    }
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
                    photo = imageUri?.toString() ?: existingPhoto, // Use existing photo if no new image is selected
                    "${binding.numberPickerDay.value}/${binding.numberPickerMonth.value}/${binding.numberPickerYear.value}",
                    binding.enterItemLocation.text.toString()
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
    private fun getDayFromDate(date: String): Int {
        return date.split("/")[0].toIntOrNull() ?: 1
    }

    private fun getMonthFromDate(date: String): Int {
        return date.split("/")[1].toIntOrNull() ?: 1
    }

    private fun getYearFromDate(date: String): Int {
        return date.split("/")[2].toIntOrNull() ?: 2024
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
