package com.example.kotlinfinalproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.databinding.FragmentFavoritesBinding


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemAdapter(emptyList(), object : ItemAdapter.ItemListener {
            override fun onItemClicked(index: Int) {
                viewModel.favoriteItems?.value?.get(index)?.let { viewModel.setItem(it) }
                findNavController().navigate(R.id.action_favoritesFragment_to_detailItemFragment)
            }

            override fun onItemLongClicked(id: Int) {
                viewModel.favoriteItems?.value?.find { it.id == id }?.let { viewModel.setItem(it) }
                findNavController().navigate(
                    R.id.action_favoritesFragment_to_editItemFragment,
                    bundleOf("itemId" to id)
                )
            }

            override fun onFavoriteClicked(item: Item) {
                viewModel.toggleFavorite(item)
            }
        })

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.favoriteItems.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
