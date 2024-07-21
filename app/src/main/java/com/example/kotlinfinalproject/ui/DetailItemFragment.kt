package com.example.kotlinfinalproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.kotlinfinalproject.databinding.DetailItemLayoutBinding

class DetailItemFragment : Fragment(){
    var _binding: DetailItemLayoutBinding?=null
    val binding get()=_binding!!
    val viewModel:ItemsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= DetailItemLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chosenItem.observe(viewLifecycleOwner){
            binding.itemTitle.text=it.title
            binding.itemDescription.text=it.description
            //binding.itemImage.setImageURI(Uri.parse(it.photo.toString()))
            Glide.with(requireContext()).load(it.photo).circleCrop().into(binding.itemImage)
            binding.itemDate.text= it.date

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}